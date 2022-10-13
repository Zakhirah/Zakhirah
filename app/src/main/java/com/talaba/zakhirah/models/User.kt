package com.talaba.zakhirah.models

import kotlin.properties.Delegates

class User {
    lateinit var user_username : String
    lateinit var user_email : String
    var user_phone by Delegates.notNull<Int>()
    lateinit var user_password : String
    lateinit var user_state : String
    var user_creation_time : Long = 0
    constructor()
    constructor(
        user_username: String,
        user_email: String,
        user_phone: Int,
        user_password: String,
        user_state: String,
        user_creation_time: Long
    ) {
        this.user_username = user_username
        this.user_email = user_email
        this.user_phone = user_phone
        this.user_password = user_password
        this.user_state = user_state
        this.user_creation_time = user_creation_time
    }
}