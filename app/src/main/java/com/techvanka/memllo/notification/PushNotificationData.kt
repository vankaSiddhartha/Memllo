package com.techvanka.memllo.notification

data class PushNotificationData(
    var data:NotificationDataClass,
    var to:String?=null
)
