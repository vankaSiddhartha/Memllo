package com.techvanka.memllo.notificationApi


import com.techvanka.memllo.notificationApi.Constant.Companion.CONTENT_TYPE
import com.techvanka.memllo.notificationApi.Constant.Companion.SERVER_KEY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiPost{

    @Headers(
        "Content-Type:application/json",
    "Authorization: AAAA9URTom0:APA91bHpusBLncoaolgMGOYSM54jEX1pNa2vicQ4aTHfCkYEUJlt7eQSwAs6rNQXDawigp9T5I5RLRwfGgZZeuqNQGhhn4d4nSP4OPNTwEMntAWT0TDP8b1TbpBwMeoqx47Uo6sIAMT7",
        )
    @POST("fcm/send")
    fun sendNotification(@Body notification:PushNotification)
    : Call<PushNotification>
}