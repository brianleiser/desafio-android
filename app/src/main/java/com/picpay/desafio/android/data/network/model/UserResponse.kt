package com.picpay.desafio.android.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "img") val image: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "username") val username: String?
)