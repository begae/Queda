package com.whoasys.queda.entities

class Post(
    val title: String,
    val author: User,
    var content: String,
    val isPromo: Boolean,
    val promoStart: String?,
    val promoEnd: String?,
    val attached: String? = null,
    var id: Int? = null,
    val addedMillis: Long? = null
)