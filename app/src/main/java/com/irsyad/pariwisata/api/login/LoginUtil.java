package com.irsyad.pariwisata.api.login;

import com.irsyad.pariwisata.api.RetrofitClient;
import com.irsyad.pariwisata.api.kategori.IKategori;
import com.irsyad.pariwisata.helper.Endpoint;

public class LoginUtil {

    private LoginUtil(){}

    public static final String BASE_URL = Endpoint.base_url;

    public static ILogin getLogin() {
        return RetrofitClient.getClient(BASE_URL).create(ILogin.class);
    }
}
