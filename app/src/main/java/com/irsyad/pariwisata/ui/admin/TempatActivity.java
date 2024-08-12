package com.irsyad.pariwisata.ui.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.adapter.TempatAdapter;
import com.irsyad.pariwisata.api.tempat.ITempat;
import com.irsyad.pariwisata.api.tempat.TempatModel;
import com.irsyad.pariwisata.api.tempat.TempatUtil;
import com.irsyad.pariwisata.helper.Endpoint;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TempatActivity extends AppCompatActivity {

    ListView lv;
    Button btnRefresh, btnTambah;
    ProgressDialog progressDialog;
    TempatAdapter tempatAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempat);

        findId();
    }

    void findId(){
        lv = findViewById(R.id.lv);
        btnTambah = findViewById(R.id.btnTambah);
        btnRefresh = findViewById(R.id.btnRefresh);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempatActivity.this, TempatUpdateActivity.class);
                startActivity(intent);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllTempat();
            }
        });

        getAllTempat();
    }

    void getAllTempat(){
        progressDialog = new ProgressDialog(TempatActivity.this);
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        ITempat iTempat = TempatUtil.getTempatInterface();
        iTempat.allTempat().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TempatModel>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(TempatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onNext(TempatModel tempatModel) {
                        progressDialog.dismiss();
                        tempatAdapter = new TempatAdapter(tempatModel, TempatActivity.this);
                        lv.setAdapter(tempatAdapter);
                    }
                });
    }
}