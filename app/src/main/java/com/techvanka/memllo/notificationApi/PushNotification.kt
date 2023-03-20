package com.techvanka.memllo.notificationApi

 data class PushNotification(
    val data:NotificationData,
    val to:String?=""
)