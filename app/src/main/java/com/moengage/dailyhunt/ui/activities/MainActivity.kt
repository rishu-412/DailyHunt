package com.moengage.dailyhunt.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.moengage.dailyhunt.R
import com.moengage.dailyhunt.core.data.model.NewsArticle
import com.moengage.dailyhunt.core.data.model.SortOrder
import com.moengage.dailyhunt.databinding.ActivityMainBinding
import com.moengage.dailyhunt.ui.recycler.NewsArticleRecyclerAdapter
import com.moengage.dailyhunt.ui.viewmodel.MainActivityViewModel
import com.moengage.dailyhunt.utils.createNotificationChannels
import dagger.hilt.android.AndroidEntryPoint

private const val MAX_NOTIFICATION_REQUEST_LIMIT = 2
private const val INTENT_EXTRA_APP_PACKAGE = "app_package"
private const val INTENT_APP_UID = "app_uid"
private const val INTENT_ACTION_NAVIGATE_TO_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private var _viewBinding: ActivityMainBinding? = null
    private val viewBinding: ActivityMainBinding get() = _viewBinding!!
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(tag, "onPermissionResult(): Permission Granted")
                createNotificationChannels(this)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
            }
        }
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getNewsArticles().observe(this) { newsArticles ->
            showArticles(newsArticles)
        }
    }

    override fun onResume() {
        super.onResume()
        askNotificationPermissionIfRequired()
    }

    /**
     * Launches an activity to read the detailed article.
     *
     * @param url Link of the News Article
     */
    private fun launchReadArticleIntent(url: String) {
        if (url.isEmpty()) {
            Log.i(
                tag,
                "launchReadArticleIntent(): URL is empty. Cannot redirect to the news article page"
            )
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        menu[0].setOnMenuItemClickListener {
            showProgressBar()
            viewModel.getSortedArticles(SortOrder.DESCENDING).observe(this) { articles ->
                showArticles(articles)
            }
            return@setOnMenuItemClickListener true
        }
        menu[1].setOnMenuItemClickListener {
            showProgressBar()
            viewModel.getSortedArticles(SortOrder.ASCENDING).observe(this) { articles ->
                showArticles(articles)
            }
            return@setOnMenuItemClickListener true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun showProgressBar() {
        viewBinding.progressBar.visibility = View.VISIBLE
        viewBinding.newsRecycler.visibility = View.GONE
    }

    private fun showArticles(articles: List<NewsArticle>) {
        with(viewBinding) {
            newsRecycler.adapter = NewsArticleRecyclerAdapter(
                articles
            ) { _, article ->
                launchReadArticleIntent(article.url)
            }
            progressBar.visibility = View.GONE
            newsRecycler.visibility = View.VISIBLE
        }
    }

    /**
     * Requests notification permission if required.
     */
    private fun askNotificationPermissionIfRequired() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(tag, "askNotificationPermissionIfRequired(): Permission Already Present")
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                // Show an informative dialog. Explaining why we need the notification permission
                showNotificationRequestDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                viewModel.updateNotificationRequestCount()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNotificationRequestDialog() {
        AlertDialog.Builder(this)
            .setTitle("We require notification permission")
            .setMessage("If you want to stay updated. Please \"allow\" the notifications")
            .setPositiveButton("allow") { dialog, _ ->
                if (canRequestPermission()) {
                    viewModel.updateNotificationRequestCount()
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    navigateToSettings()
                }
                dialog.dismiss()
            }.setNegativeButton("not now") { dialog, _ ->
                dialog.dismiss()
            }.setCancelable(false)
            .show()
    }

    private fun canRequestPermission(): Boolean {
        return viewModel.getNotificationRequestCount() < MAX_NOTIFICATION_REQUEST_LIMIT
    }

    private fun navigateToSettings() {
        val settingIntent = Intent(INTENT_ACTION_NAVIGATE_TO_SETTINGS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settingIntent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        } else {
            settingIntent.putExtra(INTENT_EXTRA_APP_PACKAGE, packageName)
            settingIntent.putExtra(INTENT_APP_UID, applicationInfo.uid)
        }
        startActivity(settingIntent)
    }

}