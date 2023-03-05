package com.whoasys.queda

import java.net.URL

class Store (
    val name: String,
    val registration: String,
    var address: String,
    val latitude: Double,
    val longitude: Double,
    var phone: String,
    var openTime: String,
    var noticeId: Int? = null,
    var profilePic: URL? = null,
    var backgroundPic: URL? = null,
    var onPromotion: Boolean = false,
    var id: Int? = null
)