package com.whoasys.queda.entities

class Post(
    val title: String,
    val author: User,
    var content: String,
    val isPromo: Boolean = false,
    val promoStart: String? = null,
    val promoEnd: String? = null,
    val attached0: String? = null,
    val attached1: String? = null,
    val attached2: String? = null,
    var id: Int? = null,
    val addedMillis: Long? = null
)