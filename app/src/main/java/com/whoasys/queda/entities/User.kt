package com.whoasys.queda.entities

import com.whoasys.queda.entities.Store

class User (
    val id: String,
    var pw: String,
    val name: String,
    var email: String,
    var latitude: Double?,
    var longitude: Double?,
    var isManager: Boolean = false,
    var store: Store? = null
)