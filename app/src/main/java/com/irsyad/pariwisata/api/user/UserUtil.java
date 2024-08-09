package com.irsyad.pariwisata.api.user;

import com.irsyad.pariwisata.api.RetrofitClient;
import com.irsyad.pariwisata.api.tempat.ITempat;
import com.irsyad.pariwisata.helper.Endpoint;

public class UserUtil {

    private  UserUtil(){}

    public static final String BASE_URL = Endpoint.base_url;

    public static IUser getUserInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IUser.class);
    }
}
