package com.irsyad.pariwisata.ui.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.helper.Endpoint;

public class DetailActivity extends AppCompatActivity {
    ImageView iv;
    TextView tvNama,tvAlamat,tvDetail;
    Button btn;
    String nama, alamat, detail, foto, latitude, longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDetail();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void getDetail(){
        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        alamat = intent.getStringExtra("alamat");
        detail = intent.getStringExtra("detail");
        foto = intent.getStringExtra("foto");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        iv = findViewById(R.id.iv);
        tvNama = findViewById(R.id.tvNama);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvDetail = findViewById(R.id.tvDetail);
        btn = findViewById(R.id.btn);

        tvNama.setText(nama);
        tvAlamat.setText(alamat);
        tvDetail.setText(detail);
        Glide.with(DetailActivity.this)
                .load(Endpoint.base_url_foto + foto)
                .into(iv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                startActivity(intent);
            }
        });
    }
}