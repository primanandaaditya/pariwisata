package com.irsyad.pariwisata.ui.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.adapter.UserAdapter;
import com.irsyad.pariwisata.api.user.IUser;
import com.irsyad.pariwisata.api.user.User;
import com.irsyad.pariwisata.api.user.UserModel;
import com.irsyad.pariwisata.api.user.UserUtil;
import com.irsyad.pariwisata.helper.Endpoint;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserActivity extends AppCompatActivity {

    ListView lv;
    Button btnTambah, btnRefresh;
    ProgressDialog progressDialog;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        findID();
    }


    void findID(){
        lv  = findViewById(R.id.lv);
        btnTambah = findViewById(R.id.btnTambah);
        btnRefresh = findViewById(R.id.btnRefresh);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user =(User)adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(UserActivity.this, UserupdateActivity.class);
                intent.putExtra("jenis","2");
                intent.putExtra("nama", user.getNama());
                intent.putExtra("alamat", user.getAlamat());
                intent.putExtra("tempat", user.getTempat());
                intent.putExtra("tgllahir", user.getTgllahir());
                intent.putExtra("gender", user.getGender());
                intent.putExtra("id", user.getId_user());
                startActivity(intent);
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserActivity.this, UserupdateActivity.class);
                i.putExtra("jenis","1");
                startActivity(i);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser();
            }
        });
        getUser();
    }

    void getUser(){
        progressDialog = new ProgressDialog(UserActivity.this);
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        IUser iUser = UserUtil.getUserInterface();
        iUser.getUsers().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UserModel>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(UserModel userModel) {
                progressDialog.dismiss();
                userAdapter = new UserAdapter(userModel, UserActivity.this);
                lv.setAdapter(userAdapter);
            }
        });
    }

}