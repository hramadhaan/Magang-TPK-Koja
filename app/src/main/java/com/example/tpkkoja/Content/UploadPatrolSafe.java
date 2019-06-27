package com.example.tpkkoja.Content;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpkkoja.AccountActivity.LoginActivity;
import com.example.tpkkoja.R;
import com.example.tpkkoja.Services.PreferenceHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class UploadPatrolSafe extends AppCompatActivity {
    EditText safe_nama,safe_phone,safe_position,safe_department,safe_shift,safe_deskripsi;
    Button safe_upload;
    PreferenceHelper preferenceHelper;

    String getNama,getPhone,getPosition,getDepartment,getShift,getDeskripsi;

    ProgressDialog progressDialog;

    String nama = "nama" ;
    String phone = "phone" ;
    String position = "position";
    String department = "department";
    String shift = "shift";
    String deskripsi = "deskripsi";
    boolean check = true;


    String ServerUploadPath ="https://tpkkojaapps.000webhostapp.com/services/upload.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_patrol_safe);

        safe_nama = findViewById(R.id.upload_nama);
        safe_phone = findViewById(R.id.upload_phone);
        safe_position = findViewById(R.id.upload_position);
        safe_department = findViewById(R.id.judul_department);
        safe_shift = findViewById(R.id.upload_shift);
        safe_deskripsi = findViewById(R.id.upload_deskripsi);

        safe_upload = findViewById(R.id.upload_button);

        safe_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNama = safe_nama.getText().toString();
                getPhone = safe_phone.getText().toString();
                getPosition = safe_position.getText().toString();
                getDepartment = safe_department.getText().toString();
                getShift = safe_shift.getText().toString();
                getDeskripsi = safe_deskripsi.getText().toString();

                upload();
            }
        });


        preferenceHelper = new PreferenceHelper(this);

        Toolbar tool = findViewById(R.id.toolbar_upload);
        TextView title = tool.findViewById(R.id.toolbar_judul_upload);
        setSupportActionBar(tool);;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void upload() {
        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(UploadPatrolSafe.this,"Uploading...","Please wait...",false,false);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(UploadPatrolSafe.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... voids) {
                ProcessClass processClass = new ProcessClass();
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put(nama,getNama);
                hashMap.put(phone,getPhone);
                hashMap.put(position,getPosition);
                hashMap.put(department,getDepartment);
                hashMap.put(shift,getShift);
                hashMap.put(deskripsi,getDeskripsi);
                String data = processClass.ImageHttpRequest(ServerUploadPath,hashMap);
                return data;
            }
        }
        AsyncTaskUploadClass asyncTaskUploadClass = new AsyncTaskUploadClass();
        asyncTaskUploadClass.execute();
    }

    public class ProcessClass{
        public String ImageHttpRequest(String requestURL, HashMap<String,String> PData){
            StringBuilder stringBuilder = new StringBuilder();
            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                preferenceHelper.putIsLogin(false);
                Intent intent = new Intent(UploadPatrolSafe.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                UploadPatrolSafe.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar,menu);
        return true;
    }
}
