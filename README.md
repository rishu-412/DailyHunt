# DailyHunt
A Pseudo-Mobile App that displays and open best-outlinked news articles from the app.

## Features Implemented
- Visually interactive design to show News Articles
- Implement Firebase Cloud Messaging to receive and show notifications.
- Added Support for FCM Data Message to show the notification
- A feature to sort and list articles based on `old-to-new` and `new-to-old`
- Add flow for requesting the notification permission.

## FCM Server Key

<code>AAAAZGTjn_Q:APA91bEwMHxqw_WHqVPhorGEOcF3Xg9QTkzEewuzJkwwkkYutzEWverWGtQ0avozbd-Dx53d1UOeCseoZnRX0yocM1CCGXuuUQqJ3tr7hDyCOCeUmRl1L9nv52BAfd0Q8I9HIUD69E_6</code>

## Sample FCM Payload

<code>
  {
    "notification": {
        "image": "https://picsum.photos/400/200",
        "title": "RIP Chandler Bing",
        "body": "Tap to read more...",
        "click_action": "com.moengage.dailyhunt.ui.activities.MainActivity"
    },
    "data": {
        "news_description": "Tap to read more...",
        "news_title": "RIP Chandler Bing",
        "news_url": "https://developers.moengage.com/hc/en-us/articles/4407395989268-Installing-Version-Catalog"
    }
  }
</code>
