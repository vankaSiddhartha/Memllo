package com.techvanka.memllo.model

import android.icu.text.CaseMap

data class VideoUploadModel (
    var videoTitle: String?=null,
    var videoLink: String?=null,
    var videoId:String?=null,
    var CreatorId:String?=null,
    var CreatorName: String? = null,
    var CreatorProfile:String?=null,
    var list:ArrayList<String> = arrayListOf<String>()

        )