package com.irsyad.pariwisata.api.tempat;

import com.irsyad.pariwisata.api.RetrofitClient;
import com.irsyad.pariwisata.api.reqres.IUser;
import com.irsyad.pariwisata.helper.Endpoint;

public class TempatUtil {

    private TempatUtil() {}

    public static final String BASE_URL = Endpoint.base_url;

    public static ITempat getTempatInterface() {
        return RetrofitClient.getClient(BASE_URL).create(ITempat.class);
    }
}
