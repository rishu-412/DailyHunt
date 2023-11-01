package com.moengage.dailyhunt.core.data.model

/**
 * A Generic Model for obtaining network response
 *
 * @param T Defines the type of data for a specific network call
 * @property status Contains the status parsed from the response payload
 * @property data Contains the parsed data with data-type = [T]
 */
data class NetworkResponse<T>(
    val status: String,
    val data: T,
) {

}