package com.bignerdranch.android.cinemaapp

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/register")
    fun registerUser(@Body registrationData: RegistrationData): Call<RegistrationResponse>

    @POST("api/auth/login")
    fun loginUser(@Body loginData: LoginData): Call<LoginResponse>

    @POST("social/new/post")
    fun newPost(@Body postData: NewPostData): Call<NewPostResponse>

    @GET("social/events/{id}")
    fun getEvents(
        @Path("id") userId: String,
        @Query("popular") popular: Boolean,
        @Query("upcomings") upcomings: Boolean,
        @Query("location") location: Boolean
    ): Call<EventsResponse>

    @GET("social/profile/{id}")
    fun getUserProfile(@Path("id") userId: String): Call<UserProfileResponse>

    @GET("social/group/{id}")
    fun getGroup(
        @Path("id") userId: String
    ): Call<GroupResponse>
}


data class Event(
    val id: String,
    val name: String,
    val eventDate: String,
    val description: String,
    val imageUrl: String
)

data class RegistrationData(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

data class LoginData(
    val email: String,
    val password: String
)

data class RegistrationResponse(
    val status: String,
    val requestId: String,
    val data: Data
) {
    data class Data(
        val _id: String,
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val role: String,
        val active: Boolean,
        val __v: Int
    )
}
data class LoginResponse(val data: Data) {
    data class Data(
        val accessToken: String,
    )
}
data class EventsResponse(
    val events: List<Event>
)

data class UserProfileResponse(
    val success: Boolean,
    val user: User
) {
    data class User(
        val username: String,
        val location: String
    )
}

data class GroupResponse(
    val success: Boolean,
    val group: Group
) {
    data class Group(
        val id: String,
        val name: String,
        val description: String,
        val posts: List<Post>
    )

    data class Post(
        val id: String,
        val username: String,
        val title: String,
        val caption: String,
        val publishDate: String,
        val urlMedia: String,
        val avatar: String
    )
}

data class NewPostData(
    val userId: String,
    val urlMedia: String,
    val content: String
)
data class NewPostResponse(
    val success: Boolean,
    val message: String
)
