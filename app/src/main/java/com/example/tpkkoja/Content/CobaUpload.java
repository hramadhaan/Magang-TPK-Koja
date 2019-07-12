package com.example.tpkkoja.Content;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tpkkoja.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CobaUpload extends AppCompatActivity {
    String pathgambar;
    String namafile;
    private ProgressBar progressBar;
    private static final String urlix= "https://cumabelajar.com/latihan/upload.php";
    private OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coba_upload);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
    }
    public void ambilGambar(View v){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }

    public void kirimGambar(View v){
        progressBar.setVisibility(View.VISIBLE);
        String mime="image/"+ namafile.substring(namafile.lastIndexOf(".")).replace(".","");
        final MediaType MEDIA_TYPE = MediaType.parse(mime);
        File file = new File(pathgambar);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filegambar", namafile, RequestBody.create(MEDIA_TYPE, file))
                .addFormDataPart("pesan", "haloooooooooooooo ini percobaan")
                .addFormDataPart("submit", "submit")
                .build();

        Request request = new Request.Builder()
                .url(urlix)
                .post(requestBody)
                .cacheControl(new CacheControl.Builder().noCache().build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ON FAILURE", e.getStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.d("ON RESPON ERROR", String.valueOf(response));
                    throw new IOException("Unexpected code " +  response);
                } else {
                    final String hasil =response.body().string();
                    Log.d("RESPON", hasil);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            };
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == 1 && resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                String[] fileName = {MediaStore.Images.Media.TITLE };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                pathgambar = cursor.getString(columnIndex);

                Cursor cursornama = getContentResolver().query(selectedImage,
                        fileName, null, null, null);
                cursornama .moveToFirst();
                int nameIndex = cursornama.getColumnIndex(fileName[0]);
                namafile=pathgambar.substring(pathgambar.lastIndexOf('/')+1,pathgambar.length() );//cursornama.getString(nameIndex);
                cursornama.close();
                cursor.close();
                ImageView img=(ImageView) findViewById(R.id.im_upload);
                Log.d("Alamat",pathgambar);
                Log.d("Nama File",namafile);
                Log.d("Extendsi",namafile.substring(namafile.lastIndexOf(".")).replace(".",""));


                Picasso.get()
                        .load(new File(pathgambar))
                        .into(img);
                Toast.makeText(this, pathgambar,
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "GAMBAR BELUM DIPILIH",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR" + e.toString(), Toast.LENGTH_LONG)
                    .show();
        }

    }
}

