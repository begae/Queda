package com.whoasys.queda.entities

class Store (
    val name: String,
    val registration: String,
    var address: String,
    val latitude: Double,
    val longitude: Double,
    var phone: String,
    var openTime: String,
    var noticeId: Int? = null,
    var profilePic: String? = null,
    var backgroundPic: String? = null,
    var onPromotion: Boolean = false,
    var id: Int? = null
)