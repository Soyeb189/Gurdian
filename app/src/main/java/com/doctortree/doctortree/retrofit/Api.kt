package com.doctortree.doctortree.retrofit

import com.doctortree.doctortree.data.*
import io.reactivex.Single
import retrofit2.http.*

interface Api {


    @GET("menu")
    fun getMenu(): Single<List<MenuDataM>>

    @GET("menu/disease/{id}")
    fun getDieses(
        @Path("id") id:String?
    ): Single<List<DiesesListDataM>>

    @GET("menu/disease/treatment/{menu_id}/{disease_id}/{type}")
    fun getSolution(
        @Path("menu_id") menu_id:String?,
        @Path("disease_id") disease_id:String?,
        @Path("type") type:String?
    ): Single<List<SolutionListDataM>>

    @FormUrlEncoded
    @POST("register")
    fun doRegistration(
        @Field("email") email:String?,
        @Field("password") password:String?,
        @Field("name") name:String?,
        @Field("password_confirmation") password_confirmation:String?
    ): Single<RegistrationDataM>

    @FormUrlEncoded
    @POST("login")
    fun doLogin(
        @Field("email") email:String?,
        @Field("password") password:String?
    ): Single<LoginDataM>

    @GET("view/message/{sender_id}/{receiver_id}")
    fun getMessage(
        @Path("receiver_id") receiver_id:String?,
        @Path("sender_id") sender_id:String?

    ): Single<List<MessageListDataM>>

}