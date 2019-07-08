package com.example.tpkkoja.Content;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpkkoja.AccountActivity.LoginActivity;
import com.example.tpkkoja.MainActivity;
import com.example.tpkkoja.R;
import com.example.tpkkoja.Services.PreferenceHelper;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadPatrolRisk extends AppCompatActivity {

    String pathgambar,filename;

    EditText risk_name,risk_phone,risk_position,risk_department,risk_deskripsi;
    Button risk_file,risk_upload;
    ImageView risk_image;
    Spinner risk_shift;
    ProgressBar risk_progress;

    private static final String urlix= "https://nyoobie.com/upload.php";
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_patrol_risk);

        risk_name = findViewById(R.id.upload_nama_risk);
        risk_phone = findViewById(R.id.upload_phone_risk);
        risk_position = findViewById(R.id.upload_position_risk);
        risk_department = findViewById(R.id.judul_department_risk);
        risk_shift = findViewById(R.id.upload_shift_risk);
        risk_deskripsi = findViewById(R.id.upload_deskripsi_risk);
        risk_file = findViewById(R.id.button_choose_file);
        risk_upload = findViewById(R.id.upload_button_risk);
        risk_image = findViewById(R.id.image_risk);

        risk_progress = findViewById(R.id.risk_progressbar);

        risk_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        risk_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }

    private void uploadImage() {
        risk_progress.setVisibility(View.VISIBLE);
        String mime = "image/"+filename.substring(filename.lastIndexOf(".")).replace(".","");
        final MediaType MEDIA_TYPE = MediaType.parse(mime);
        File file = new File(pathgambar);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("nama",risk_name.getText().toString())
                .addFormDataPart("phone",risk_phone.getText().toString())
                .addFormDataPart("position",risk_position.getText().toString())
                .addFormDataPart("department",risk_department.getText().toString())
                .addFormDataPart("shift",risk_shift.getSelectedItem().toString())
                .addFormDataPart("deskripsi",risk_deskripsi.getText().toString())
                .addFormDataPart("tipe","RISK")
                .addFormDataPart("filegambar",filename,RequestBody.create(MEDIA_TYPE,file))
                .addFormDataPart("submit","submit")
                .build();

        Request request = new Request.Builder()
                .url(urlix)
                .post(requestBody)
                .cacheControl(new CacheControl.Builder().noCache().build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("On Failure",e.getStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String hasil = response.body().string();
                Log.d("Response",hasil);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        risk_progress.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(UploadPatrolRisk.this, MainActivity.class));
                    }
                });
            }
        });
    }

    private void chooseFile() {
        Intent choose = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(choose,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==1&&resultCode==RESULT_OK&&null!=data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA};
                String[] fileName = { MediaStore.Images.Media.TITLE };

                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                pathgambar = cursor.getString(columnIndex);

                Cursor cursornama = getContentResolver().query(selectedImage,fileName,null,null,null);
                cursornama.moveToFirst();

                int nameIndex = cursornama.getColumnIndex(fileName[0]);
                filename = pathgambar.substring(pathgambar.lastIndexOf('/')+1,pathgambar.length());
                cursornama.close();
                cursor.close();

                Log.d("Alamat",pathgambar);
                Log.d("Nama File",filename);
                Log.d("Extensi ",filename.substring(filename.lastIndexOf('.')).replace(".",""));

                Picasso.get()
                        .load(new File(pathgambar))
                        .into(risk_image);

                Toast.makeText(UploadPatrolRisk.this,pathgambar,Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(UploadPatrolRisk.this,"Gambar Pilih Terlebih Dahulu",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(UploadPatrolRisk.this,"ERROR"+e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
