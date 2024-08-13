package com.irsyad.pariwisata.ui.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.irsyad.pariwisata.adapter.TempatAdapter;
import com.irsyad.pariwisata.api.tempat.ITempat;
import com.irsyad.pariwisata.api.tempat.Tempat;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void findId(){
        lv = findViewById(R.id.lv);
        btnTambah = findViewById(R.id.btnTambah);
        btnRefresh = findViewById(R.id.btnRefresh);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempatActivity.this, TempatUpdateActivity.class);
                intent.putExtra("jenis","1");
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

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Tempat tempat =(Tempat) adapterView.getAdapter().getItem(i);
                                Intent intent = new Intent(TempatActivity.this, TempatUpdateActivity.class);
                                intent.putExtra("jenis","2");
                                intent.putExtra("nama", tempat.getNama());
                                intent.putExtra("alamat", tempat.getAlamat());
                                intent.putExtra("detail", tempat.getDetail());
                                intent.putExtra("id_kategori", tempat.getId_kategori());
                                intent.putExtra("latitude", tempat.getLatitude());
                                intent.putExtra("longitude", tempat.getLongitude());
                                intent.putExtra("id", tempat.getId_tempat());
                                startActivity(intent);
                            }
                        });
                    }
                });
    }
}