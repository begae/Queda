package com.whoasys.queda.entities

class User constructor(
    val id: String,
    var pw: String,
    val name: String,
    var email: String,
    var isManager: Boolean = false,
    var storeId: Int? = null
)