package com.talaba.zakhirah.models

import java.time.LocalDateTime

class Kitab {
    lateinit var kitab_name : String
    lateinit var kitab_description : String
    lateinit var kitab_author : String
    lateinit var kitab_url : String
    lateinit var kitab_upload_time : LocalDateTime
    lateinit var kitab_language : String
    var kitab_permission : Boolean = false

    constructor()
    constructor(
        kitab_name: String,
        kitab_description: String,
        kitab_author: String,
        kitab_url: String,
        kitab_upload_time: LocalDateTime,
        kitab_language: String,
        kitab_permission: Boolean
    ) {
        this.kitab_name = kitab_name
        this.kitab_description = kitab_description
        this.kitab_author = kitab_author
        this.kitab_url = kitab_url
        this.kitab_upload_time = kitab_upload_time
        this.kitab_language = kitab_language
        this.kitab_permission = kitab_permission
    }
}