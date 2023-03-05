package com.techvanka.memllo.model

data class OrderItemModel(
    var size:String?= null,
    var quantity:String?=null,
    var productId:String?=null,
    var phoneNo:String?=null,
    var address:String?=null,
    var UserId:String?=null,
    var productImg:String?=null,
    var productCost: String? = null,
    var productDis: String?=null,
    var productTrack:String?= null,
    var productName:String? =null
)
