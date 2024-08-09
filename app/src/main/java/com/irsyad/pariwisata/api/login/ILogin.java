package com.irsyad.pariwisata.api.login;

import com.irsyad.pariwisata.helper.Endpoint;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ILogin {

    @FormUrlEncoded
    @POST(Endpoint.login)
    rx.Observable<LoginModel> doLogin(
            @Field("nama") String nama,
            @Field("password") String password
    );
}
