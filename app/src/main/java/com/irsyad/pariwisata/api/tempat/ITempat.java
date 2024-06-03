package com.irsyad.pariwisata.api.tempat;

import com.irsyad.pariwisata.helper.Endpoint;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ITempat {
    @GET(Endpoint.all_tempat)
    rx.Observable<TempatModel> allTempat();

    @FormUrlEncoded
    @POST(Endpoint.tempat_by_kategori)
    rx.Observable<TempatModel> tempatByKategori(
            @Field("id_kategori") String id_kategori
    );

    @FormUrlEncoded
    @POST(Endpoint.tempat_search)
    rx.Observable<TempatModel> tempatSearch(
            @Field("key") String key
    );
}
