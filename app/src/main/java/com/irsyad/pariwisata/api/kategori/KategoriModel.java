package com.irsyad.pariwisata.api.kategori;

import java.util.List;

public class KategoriModel {
    boolean error;
    List<Kategori> pesan;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Kategori> getPesan() {
        return pesan;
    }

    public void setPesan(List<Kategori> pesan) {
        this.pesan = pesan;
    }
}
