package com.irsyad.pariwisata.api.login;

public class LoginModel {
    public  boolean error;
    public Login pesan;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Login getPesan() {
        return pesan;
    }

    public void setPesan(Login pesan) {
        this.pesan = pesan;
    }
}
