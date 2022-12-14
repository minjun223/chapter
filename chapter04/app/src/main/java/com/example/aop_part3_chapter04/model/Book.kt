package com.example.aop_part3_chapter04.model

import com.google.gson.annotations.SerializedName

data class Book (
    @SerializedName("itemId") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("desciription") val description: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: Long,
)
