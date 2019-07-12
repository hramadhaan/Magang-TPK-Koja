package com.example.tpkkoja.Content;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpkkoja.Dashboard;
import com.example.tpkkoja.MainActivity;
import com.example.tpkkoja.R;
import com.example.tpkkoja.Services.PreferenceHelper;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadPatrolSafe extends AppCompatActivity {

    EditText safe_nama,safe_phone,safe_position,safe_deskripsi,safe_department;
    Button safe_upload;
    Spinner safe_shift;
    ProgressBar safe_progress;

    TextView judul;

    Toolbar toolbar;

    private static final String url= "https://nyoobie.com/upload.php";
    private OkHttpClient client = new OkHttpClient();

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_patrol_safe);

        preferenceHelper = new PreferenceHelper(getApplicationContext());

        toolbar = findViewById(R.id.safe_toolbar);
        judul = toolbar.findViewById(R.id.safe_judul);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        safe_nama = findViewById(R.id.upload_nama);
        safe_phone = findViewById(R.id.upload_phone);
        safe_position = findViewById(R.id.upload_position);
        safe_deskripsi = findViewById(R.id.upload_deskripsi);
        safe_upload = findViewById(R.id.upload_button);
        safe_shift = findViewById(R.id.upload_shift);
        safe_progress = findViewById(R.id.upload_progressbar);
        safe_department = findViewById(R.id.judul_department);

        safe_nama.setText(preferenceHelper.getNama());
        safe_phone.setText(preferenceHelper.getPhone());
        safe_position.setText(preferenceHelper.getPosition());
        safe_department.setText(preferenceHelper.getDepartment());

        safe_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSafe();
            }
        });

    }

    private void uploadSafe() {
        safe_progress.setVisibility(View.VISIBLE);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("nama",safe_nama.getText().toString())
                .addFormDataPart("phone",safe_phone.getText().toString())
                .addFormDataPart("position",safe_position.getText().toString())
                .addFormDataPart("department",safe_department.getText().toString())
                .addFormDataPart("shift",safe_shift.getSelectedItem().toString())
                .addFormDataPart("deskripsi",safe_deskripsi.getText().toString())
                .addFormDataPart("tipe","SAFE")
                .addFormDataPart("filegambar","")
                .addFormDataPart("submit","submit")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .cacheControl(new CacheControl.Builder().noCache().build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("On Failure",e.getStackTrace().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadPatrolSafe.this,"Upload gagal, coba lagi", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String hasil = response.body().string();
                Log.d("Response",hasil);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        safe_progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(UploadPatrolSafe.this,"Upload Sukses",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UploadPatrolSafe.this, Dashboard.class));
                        finish();
                    }
                });
            }
        });
    }
}
