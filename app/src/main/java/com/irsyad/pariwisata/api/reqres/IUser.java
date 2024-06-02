package com.irsyad.pariwisata.api.reqres;

import retrofit2.Callback;
import retrofit2.http.GET;

public interface IUser {
    @GET("users/2")
    rx.Observable<UserModel> getUser();
}
