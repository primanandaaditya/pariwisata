package com.irsyad.pariwisata.ui.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irsyad.pariwisata.MainActivity;
import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.session.SessionManager;

public class AdminActivity extends AppCompatActivity {

    Button btnUser,btnKategori,btnTempat, btnJelajah,btnLogout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findID();

    }

    void findID(){
        sessionManager = new SessionManager(AdminActivity.this);
        btnUser = findViewById(R.id.btnUser);
        btnKategori = findViewById(R.id.btnKategori);
        btnTempat = findViewById(R.id.btnTempat);
        btnJelajah = findViewById(R.id.btnJelajah);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setMessage("Apakah Anda akan logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        btnJelajah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                intent.putExtra("jenis","1");
                startActivity(intent);
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

        btnKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, KategoriActivity.class);
                startActivity(intent);
            }
        });

        btnTempat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminActivity.this, TempatActivity.class);
                startActivity(intent);
            }
        });

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    sessionManager = new SessionManager(AdminActivity.this);
                    sessionManager.logoutUser();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}