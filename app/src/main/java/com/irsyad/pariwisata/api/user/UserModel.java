package com.irsyad.pariwisata.api.user;
import java.util.List;
public class UserModel {
    boolean error;
    List<User> pesan;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<User> getPesan() {
        return pesan;
    }

    public void setPesan(List<User> pesan) {
        this.pesan = pesan;
    }
}
