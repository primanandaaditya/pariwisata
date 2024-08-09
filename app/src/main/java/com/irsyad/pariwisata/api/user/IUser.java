package com.irsyad.pariwisata.api.user;

import com.irsyad.pariwisata.api.login.LoginModel;
import com.irsyad.pariwisata.base.BaseModel;
import com.irsyad.pariwisata.helper.Endpoint;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IUser {

    @GET(Endpoint.list_user)
    rx.Observable<UserModel> getUsers();

    @FormUrlEncoded
    @POST(Endpoint.update_user)
    rx.Observable<BaseModel> updateUser(
            @Field("nama") String nama,
            @Field("tipe") String tipe,
            @Field("role") String role,
            @Field("password") String password,
            @Field("alamat") String alamat,
            @Field("tempat") String tempat,
            @Field("tgllahir") String tgllahir,
            @Field("gender") String gender,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(Endpoint.update_user)
    rx.Observable<BaseModel> editUser(
            @Field("nama") String nama,
            @Field("tipe") String tipe,
            @Field("role") String role,
            @Field("alamat") String alamat,
            @Field("tempat") String tempat,
            @Field("tgllahir") String tgllahir,
            @Field("gender") String gender,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(Endpoint.delete_user)
    rx.Observable<BaseModel> deleteUser(
            @Field("id") String id
    );
}
