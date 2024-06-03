package com.irsyad.pariwisata.api.kategori;

import com.irsyad.pariwisata.api.RetrofitClient;
import com.irsyad.pariwisata.api.reqres.IUser;
import com.irsyad.pariwisata.helper.Endpoint;

public class KategoriUtil {

    private KategoriUtil() {}

    public static final String BASE_URL = Endpoint.base_url;

    public static IKategori getKategoriInterface() {
        return RetrofitClient.getClient(BASE_URL).create(IKategori.class);
    }
}
