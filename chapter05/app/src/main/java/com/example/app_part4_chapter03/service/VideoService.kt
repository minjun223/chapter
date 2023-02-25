package com.example.app_part4_chapter03.service

import com.example.app_part4_chapter03.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("/v3/60b83d72-1a29-4148-9ad2-46bc8fd62964")
    fun listVideos(): Call<VideoDto>
}