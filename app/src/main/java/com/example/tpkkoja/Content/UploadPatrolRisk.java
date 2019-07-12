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
import android.text.InputType;
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

import com.bumptech.glide.Glide;
import com.example.tpkkoja.AccountActivity.LoginActivity;
import com.example.tpkkoja.Dashboard;
import com.example.tpkkoja.MainActivity;
import com.example.tpkkoja.R;
import com.example.tpkkoja.Services.PreferenceHelper;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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


    String pathgambar,pathgrambar2,filename1,filename2;

    EditText risk_name,risk_phone,risk_position,risk_department,risk_deskripsi;
    Button risk_file,risk_upload,risk_file2;
    ImageView risk_image,risk_image2;
    Spinner risk_shift;
    ProgressBar risk_progress;

    TextView textview_choose,textview_choose_2,judul;
    Toolbar toolbar;

    private PreferenceHelper preferenceHelper;

    private static final String urlix= "https://nyoobie.com/upload.php";
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_patrol_risk);



        risk_image2 = findViewById(R.id.image_risk_2);

        textview_choose_2 = findViewById(R.id.file_upload_risk_2);

        risk_file2 = findViewById(R.id.button_choose_file_2);

        risk_file2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile2();
            }
        });

        preferenceHelper = new PreferenceHelper(getApplicationContext());

        toolbar = findViewById(R.id.risk_toolbar);
        judul = toolbar.findViewById(R.id.risk_judul);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        risk_name = findViewById(R.id.upload_nama_risk);
        risk_phone = findViewById(R.id.upload_phone_risk);
        risk_position = findViewById(R.id.upload_position_risk);
        risk_department = findViewById(R.id.judul_department_risk);
        risk_shift = findViewById(R.id.upload_shift_risk);
        risk_deskripsi = findViewById(R.id.upload_deskripsi_risk);
        risk_file = findViewById(R.id.button_choose_file);
        risk_upload = findViewById(R.id.upload_button_risk);
        risk_image = findViewById(R.id.image_risk);
//
        risk_name.setText(preferenceHelper.getNama());
        risk_name.setInputType(InputType.TYPE_NULL);
        risk_phone.setText(preferenceHelper.getPhone());
        risk_phone.setInputType(InputType.TYPE_NULL);
        risk_position.setText(preferenceHelper.getPosition());
        risk_position.setInputType(InputType.TYPE_NULL);
        risk_department.setText(preferenceHelper.getDepartment());
        risk_department.setInputType(InputType.TYPE_NULL);

        textview_choose = findViewById(R.id.file_upload_risk);

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

    private void chooseFile2() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
        }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }


    private void uploadImage() {
        risk_progress.setVisibility(View.VISIBLE);
        String mime = "image/"+filename1.substring(filename1.lastIndexOf(".")).replace(".","");
        String mime2 = "image/"+filename2.substring(filename2.lastIndexOf(".")).replace(".","");

        final MediaType MEDIA_TYPE = MediaType.parse(mime);
        final MediaType MEDIA_TYPE_2 = MediaType.parse(mime2);

        File file1 = new File(pathgambar);
        File file2 = new File(pathgrambar2);

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("nama",risk_name.getText().toString())
                .addFormDataPart("phone",risk_phone.getText().toString())
                .addFormDataPart("position",risk_position.getText().toString())
                .addFormDataPart("department",risk_department.getText().toString())
                .addFormDataPart("shift",risk_shift.getSelectedItem().toString())
                .addFormDataPart("deskripsi",risk_deskripsi.getText().toString())
                .addFormDataPart("tipe","RISK")
                .addFormDataPart("filegambar1",filename1,RequestBody.create(MEDIA_TYPE,file1))
                .addFormDataPart("filegambar2",filename2,RequestBody.create(MEDIA_TYPE,file2))
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadPatrolRisk.this,"Upload Gagal, Coba Ulangi",Toast.LENGTH_LONG).show();
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
                        risk_progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(UploadPatrolRisk.this,"Upload Sukses",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UploadPatrolRisk.this, Dashboard.class));
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==1 && resultCode==RESULT_OK && null!=data) {
               Uri selectedImage = data.getData();
               String[] filePathColumn = { MediaStore.Images.Media.DATA };
               String[] fileName = { MediaStore.Images.Media.TITLE };

               Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
               cursor.moveToFirst();

                Cursor cursor2 = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                cursor2.moveToFirst();

               int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
               pathgambar = cursor.getString(columnIndex);

               int columnIndex2 = cursor.getColumnIndex(filePathColumn[1]);
               pathgrambar2 = cursor2.getString(columnIndex2);

                Cursor cursornama = getContentResolver().query(selectedImage,fileName,null,null,null);
                cursornama.moveToFirst();

                Cursor cursornama2 = getContentResolver().query(selectedImage,fileName,null,null,null);
                cursornama2.moveToFirst();

                int nameIndex = cursornama.getColumnIndex(fileName[0]);
                int namaIndex2 = cursornama2.getColumnIndex(fileName[1]);

                filename1 = pathgambar.substring(pathgambar.lastIndexOf('/')+1,pathgambar.length());
                cursornama.close();
                cursor.close();

                filename2 = pathgrambar2.substring(pathgrambar2.lastIndexOf('/')+1,pathgrambar2.length());
                cursornama2.close();
                cursor.close();

                textview_choose.setText(filename1);

                textview_choose_2.setText(filename2);

                Log.d("Alamat",pathgambar);
                Log.d("Nama File",filename1);
                Log.d("Extensi ",filename1.substring(filename1.lastIndexOf('.')).replace(".",""));

                Log.d("Alamat",pathgrambar2);
                Log.d("Nama File",filename2);
                Log.d("Extensi ",filename2.substring(filename2.lastIndexOf('.')).replace(".",""));

                Picasso.get()
                        .load(new File(pathgambar))
                        .into(risk_image);

                Picasso.get()
                        .load(new File(pathgrambar2))
                        .into(risk_image2);

                Toast.makeText(UploadPatrolRisk.this,pathgambar,Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(UploadPatrolRisk.this,"Gambar Pilih Terlebih Dahulu",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(UploadPatrolRisk.this,"ERROR"+e.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
