package com.techvanka.memllo.model

data class ShopingModel(
    var productId:String?=null,
    var title:String?=null,
    var discretion:String?=null,
    var imageList:ArrayList<String> = arrayListOf(),
    var cost:String?=null
)