package com.irsyad.pariwisata.ui.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.api.user.IUser;
import com.irsyad.pariwisata.api.user.UserUtil;
import com.irsyad.pariwisata.base.BaseModel;
import com.irsyad.pariwisata.helper.Endpoint;
import com.irsyad.pariwisata.session.SessionManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserupdateActivity extends AppCompatActivity {

    EditText etNama,etAlamat,etTempat,etTgllahir,etGender,etPassword,etUPassword;
    LinearLayout llPass;
    Button btn, btnHapus;
    String jenis, nama, alamat, tempat, tgllahir, gender, id;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userupdate);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        findID();
    }

    void findID(){

        Intent intent = getIntent();
        jenis = intent.getStringExtra("jenis");
        llPass = findViewById(R.id.llPass);

        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etTempat = findViewById(R.id.etTempat);
        etTgllahir = findViewById(R.id.etTgllahir);
        etGender = findViewById(R.id.etGender);
        etPassword = findViewById(R.id.etPassword);
        etUPassword = findViewById(R.id.etUPassword);
        btn = findViewById(R.id.btn);
        btnHapus = findViewById(R.id.btnHapus);

        if (jenis.equals("2")){
            etNama.setText(intent.getStringExtra("nama"));
            etAlamat.setText(intent.getStringExtra("alamat"));
            etTempat.setText(intent.getStringExtra("tempat"));
            etTgllahir.setText(intent.getStringExtra("tgllahir"));
            etGender.setText(intent.getStringExtra("gender"));
            id = intent.getStringExtra("id");
            llPass.setVisibility(View.GONE);
            btnHapus.setVisibility(View.VISIBLE);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jenis.equals("1")){
                    if (etNama.getText().toString().isEmpty()){
                        Toast.makeText(UserupdateActivity.this,"Nama harus diisi", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (etPassword.getText().toString().isEmpty() || etUPassword.getText().toString().isEmpty()){
                        Toast.makeText(UserupdateActivity.this,"Password harus diisi", Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        if (!etPassword.getText().toString().equals(etUPassword.getText().toString())){
                           Toast.makeText(UserupdateActivity.this, "Password dan pengulangannya harus sama", Toast.LENGTH_LONG).show();
                           return;
                        }
                    }
                    update();
                }

                if (jenis.equals("2")){
                    if (etNama.getText().toString().isEmpty()){
                        Toast.makeText(UserupdateActivity.this,"Nama harus diisi", Toast.LENGTH_LONG).show();
                        return;
                    }

                    update();
                }

            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserupdateActivity.this);
                builder.setMessage("Apakah Anda akan hapus data ini?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }

    void delete(){
        progressDialog = new ProgressDialog(UserupdateActivity.this);
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        IUser iUser = UserUtil.getUserInterface();
        iUser.deleteUser(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseModel>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(UserupdateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(BaseModel baseModel) {
                Toast.makeText(UserupdateActivity.this, "Berhasil hapus", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }



    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    delete();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    void update(){
        progressDialog = new ProgressDialog(UserupdateActivity.this);
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        IUser iUser = UserUtil.getUserInterface();

        if (jenis.equals("1")){
            iUser.updateUser(
                    etNama.getText().toString(), jenis, "guest",
                    etPassword.getText().toString(),etAlamat.getText().toString(),
                    etTempat.getText().toString(), etTgllahir.getText().toString(), etGender.getText().toString(),
                    "0").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseModel>() {
                @Override
                public void onCompleted() {
                    progressDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                    Toast.makeText(UserupdateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNext(BaseModel baseModel) {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(UserupdateActivity.this, "Sukses simpan", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            iUser.updateUser(
                    etNama.getText().toString(), jenis, "guest",
                    etPassword.getText().toString(),etAlamat.getText().toString(),
                    etTempat.getText().toString(), etTgllahir.getText().toString(), etGender.getText().toString(),
                    id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseModel>() {
                @Override
                public void onCompleted() {
                    progressDialog.dismiss();
                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                    Toast.makeText(UserupdateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNext(BaseModel baseModel) {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(UserupdateActivity.this, "Sukses simpan", Toast.LENGTH_LONG).show();
                }
            });
        }


    }
}