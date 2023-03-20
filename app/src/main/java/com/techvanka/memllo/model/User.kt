package com.techvanka.memllo.model

data class User(
    var name:String?=null,
    var uid:String?=null,
    var profile:String?=null,
    var list:ArrayList<String> = arrayListOf<String>(),
    var fcmToken:String?=null


)
