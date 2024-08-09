package com.irsyad.pariwisata.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irsyad.pariwisata.MainActivity;
import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.api.login.ILogin;
import com.irsyad.pariwisata.api.login.LoginModel;
import com.irsyad.pariwisata.api.login.LoginUtil;
import com.irsyad.pariwisata.helper.Endpoint;

import java.util.Objects;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    boolean showPass = false;
    ProgressDialog progressDialog;
    ImageView iv_show_pass;
    Button btn;
    EditText etNama,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        findID();
    }

    void findID(){
        btn = findViewById(R.id.btn);
        etNama = findViewById(R.id.etNama);
        etPassword = findViewById(R.id.etPass);
        iv_show_pass = findViewById(R.id.iv_show_pass);

        iv_show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPass){
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPassword.setTransformationMethod(null);
                    showPass = false;
                    iv_show_pass.setImageResource(R.drawable.ic_eye);
                }else{
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPass = true;
                    iv_show_pass.setImageResource(R.drawable.ic_eye);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNama.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Nama dan password wajib diisi", Toast.LENGTH_LONG).show();
                }else{
                    doLogin(etNama.getText().toString(), etPassword.getText().toString());
                }
            }
        });
    }

    void doLogin(String nama, String password){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        ILogin iLogin = LoginUtil.getLogin();
        iLogin.doLogin(nama, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginModel>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(LoginModel loginModel) {
                progressDialog.show();
                if (loginModel.getPesan().getError().equals("1")){
                    Toast.makeText(LoginActivity.this, "Nama atau password salah", Toast.LENGTH_LONG).show();
                }else{
                    Log.d("role", loginModel.getPesan().getRole() );
                    if (loginModel.getPesan().getRole().equals("guest")){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,"ADMIN", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}