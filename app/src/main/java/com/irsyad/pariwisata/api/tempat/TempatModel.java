package com.irsyad.pariwisata.api.tempat;

import java.util.List;

public class TempatModel {
    boolean error;
    List<Tempat> pesan;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Tempat> getPesan() {
        return pesan;
    }

    public void setPesan(List<Tempat> pesan) {
        this.pesan = pesan;
    }
}
