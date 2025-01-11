package com.example.todo.network

import com.example.todo.model.ApiReminder
import retrofit2.http.GET

interface ApiService {

    @GET("/todos")
    suspend fun getTodos(): ArrayList<ApiReminder>
}