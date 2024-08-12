package com.irsyad.pariwisata.api.tempat;

import com.irsyad.pariwisata.base.BaseModel;
import com.irsyad.pariwisata.helper.Endpoint;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

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

    @FormUrlEncoded
    @POST(Endpoint.tempat_terdekat)
    rx.Observable<TempatModel> tempatTerdekat(
        @Field("latitude") Double latitude,
        @Field("longitude") Double longitude
    );

    @Multipart
    @POST(Endpoint.tempat_insert)
    rx.Observable<BaseModel> insertTempat(
            @Part("nama") RequestBody nama,
            @Part("alamat") RequestBody alamat,
            @Part("detail") RequestBody detail,
            @Part("id_kategori") RequestBody id_kategori,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part foto
    );
}
