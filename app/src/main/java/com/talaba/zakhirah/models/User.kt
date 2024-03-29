package com.talaba.zakhirah.models

import java.time.LocalDateTime
import kotlin.properties.Delegates

class User {
    lateinit var user_username : String
    lateinit var user_email : String
    lateinit var user_phone: String
    lateinit var user_password : String
    lateinit var user_state : String
    var user_creation_time by Delegates.notNull<Long>()

    constructor()
    constructor(
        user_username: String,
        user_email: String,
        user_phone: String,
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