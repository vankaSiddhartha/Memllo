package com.techvanka.memllo.model

data class FriendRequestModel(
    var isFriend:String?=null,
    var uid:String?=null,
    var profile:String?=null,
    var name:String?=null,
    var reqUid:String?=null,
    var fcmToken:String?=null
)
