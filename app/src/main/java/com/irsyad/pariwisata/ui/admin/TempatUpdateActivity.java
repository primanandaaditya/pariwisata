package com.irsyad.pariwisata.ui.admin;

import static com.irsyad.pariwisata.MainActivity.hasPermissions;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.adapter.TempatAdapter;
import com.irsyad.pariwisata.api.kategori.IKategori;
import com.irsyad.pariwisata.api.kategori.Kategori;
import com.irsyad.pariwisata.api.kategori.KategoriModel;
import com.irsyad.pariwisata.api.kategori.KategoriUtil;
import com.irsyad.pariwisata.api.tempat.ITempat;
import com.irsyad.pariwisata.api.tempat.TempatUtil;
import com.irsyad.pariwisata.base.BaseModel;
import com.irsyad.pariwisata.helper.Endpoint;
import com.irsyad.pariwisata.helper.ResizeImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.BuildConfig;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TempatUpdateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etNama, etAlamat, etDeskripsi, etLatitude, etLongitude;
    File photoFile = null;
    String pathPariwisata, pathGaleri;
    ProgressDialog progressDialog;
    Spinner spKategori;
    ImageView iv;
    Button btnSimpan, btnHapus;
    HashMap<String, String> hashMapKategori;
    String selectedIdKategori = "";
    List<String> kategoris;
    String imageFilePath;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempat_update);

        //minta izin user untuk aktifkan kamera
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        findID();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    void findID(){

        pathPariwisata = Environment.getExternalStorageDirectory() + File.separator + "DCIM/Pariwisata/";
        pathGaleri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();


        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        spKategori = findViewById(R.id.spKategori);
        spKategori.setOnItemSelectedListener(TempatUpdateActivity.this);
        iv= findViewById(R.id.iv);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnHapus = findViewById(R.id.btnHapus);

        getKategori();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpan();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    void getKategori(){
        progressDialog = new ProgressDialog(TempatUpdateActivity.this);
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        IKategori iKategori = KategoriUtil.getKategoriInterface();
        iKategori.getKategori().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<KategoriModel>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                progressDialog.dismiss();
                Toast.makeText(TempatUpdateActivity.this, "Gagal get kategori", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(KategoriModel kategoriModel) {
                progressDialog.dismiss();

                kategoris = new ArrayList<>();
                hashMapKategori = new HashMap<>();

                for(Kategori kategori: kategoriModel.getPesan()){
                    kategoris.add(kategori.getNama());
                    hashMapKategori.put(kategori.getNama(), kategori.getId_kategori());
                }

                ArrayAdapter arrayAdapter =
                        new ArrayAdapter(
                                TempatUpdateActivity.this,
                                android.R.layout.simple_spinner_item,
                                kategoris.toArray()
                        );

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spKategori.setAdapter(arrayAdapter);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                Glide.with(TempatUpdateActivity.this)
                        .load(imageFilePath)
                        .into(iv);

//                File file = new File(imageFilePath);
//                try {
//                    File compressedImage = new Compressor(this)
//                            .setMaxWidth(640)
//                            .setMaxHeight(480)
//                            .setQuality(50)
//                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                            .setDestinationDirectoryPath(pathPariwisata)
//                            .compressToFile(file);
//
//                    if (file.exists()){
//                        file.delete();
//                    }
//                } catch (IOException e) {
//                    Toast.makeText(TempatUpdateActivity.this, "Gagal kompres/hapus file gambar kamera!", Toast.LENGTH_SHORT).show();
//                }

            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectImage() {
        final CharSequence[] options = {"Dari kamera", "Dari galeri", "Batal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TempatUpdateActivity.this);
        builder.setTitle("Foto Dokumen");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Dari kamera")) {
                    if(ContextCompat.checkSelfPermission(TempatUpdateActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        dispatchTakenPictureIntent();
                    }
                    else{
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                            Toast.makeText(getApplicationContext(), "Mohon diberikan izin untuk kamera/galeri", Toast.LENGTH_SHORT).show();
                        }
                        ActivityCompat.requestPermissions(TempatUpdateActivity.this, PERMISSIONS, PERMISSION_ALL);
                    }
                } else if (options[item].equals("Dari galeri")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    pickerActivityResultLauncher.launch(Intent.createChooser(intent, "Pilih foto tempat pariwisata"));

                } else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakenPictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(TempatUpdateActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(TempatUpdateActivity.this,
                         "com.irsyad.pariwisata.provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, 100);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "UPL_" + timeStamp;
        File folder = new File(pathGaleri);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File image = File.createTempFile(imageFileName,".jpg",folder);
        imageFilePath = image.getAbsolutePath();
        Log.d("imageFilePath", imageFilePath);
        return image;
    }

    void simpan(){
        progressDialog = new ProgressDialog(TempatUpdateActivity.this);
        progressDialog.setMessage(Endpoint.mohon_tunggu);
        progressDialog.show();

        String nama = etNama.getText().toString();
        String alamat = etAlamat.getText().toString();
        String detail = etDeskripsi.getText().toString();
        String id_kategori = selectedIdKategori;
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();
        Map<String, RequestBody> map = new HashMap<>();


        File file = new File(imageFilePath);
        try {
            File compressedImage = new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(50)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(pathPariwisata)
                    .compressToFile(file);

            if (file.exists()){
                file.delete();
            }

            RequestBody r_nama = RequestBody.create(MultipartBody.FORM, nama);
            RequestBody r_alamat = RequestBody.create(MultipartBody.FORM, alamat);
            RequestBody r_detail = RequestBody.create(MultipartBody.FORM, detail);
            RequestBody r_id_kategori = RequestBody.create(MultipartBody.FORM, id_kategori);
            RequestBody r_latitude = RequestBody.create(MultipartBody.FORM, latitude);
            RequestBody r_longitude = RequestBody.create(MultipartBody.FORM, longitude);
            RequestBody requestFile = RequestBody.create(MultipartBody.FORM, compressedImage);
            MultipartBody.Part foto = MultipartBody.Part.createFormData("foto", imageFilePath, requestFile);


            ITempat iTempat = TempatUtil.getTempatInterface();
            iTempat
                    .insertTempat(r_nama, r_alamat, r_detail, r_id_kategori, r_latitude, r_longitude, foto)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseModel>() {
                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            Log.d("error upload", e.toString());
                            Toast.makeText(TempatUpdateActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(BaseModel baseModel) {
                            progressDialog.dismiss();
                            Toast.makeText(TempatUpdateActivity.this, baseModel.getPesan(), Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (IOException e) {
            Toast.makeText(TempatUpdateActivity.this, "Gagal kompres/hapus file gambar kamera!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedIdKategori = hashMapKategori.get(kategoris.get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    private ActivityResultLauncher<Intent> pickerActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Uri selectedImage = result.getData().getData();
            imageFilePath = getPath(getApplicationContext(), selectedImage );

            Glide.with(TempatUpdateActivity.this)
                    .asBitmap()
                    .load(selectedImage)
                    .addListener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(@NonNull Bitmap resource, @NonNull Object model, Target<Bitmap> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(iv);
        }
    });

}