package com.irsyad.pariwisata.api.kategori;

import com.irsyad.pariwisata.helper.Endpoint;

import retrofit2.http.GET;


public interface IKategori {
    @GET(Endpoint.all_kategori)
    rx.Observable<KategoriModel> getKategori();
}
