package com.example.tpkkoja.AccountActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpkkoja.Dashboard;
import com.example.tpkkoja.R;
import com.example.tpkkoja.Services.Constants;
import com.example.tpkkoja.Services.HttpRequest;
import com.example.tpkkoja.Services.ParseContent;
import com.example.tpkkoja.Services.PreferenceHelper;
import com.example.tpkkoja.Services.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText username,password,nama,phone;
    Spinner position,department;
    Button btn_register;
    TextView login,judul;

    Toolbar toolbar;

    PreferenceHelper preferenceHelper;
    ParseContent parseContent;

    private final int RegTask = 1;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        judul = toolbar.findViewById(R.id.register_title);
        nama = findViewById(R.id.register_nama);
        phone = findViewById(R.id.register_phone);
        position = findViewById(R.id.register_position);
        department = findViewById(R.id.register_department);

        btn_register = findViewById(R.id.register_button);


        preferenceHelper = new PreferenceHelper(this);
        parseContent = new ParseContent(this);

        if (preferenceHelper.getIsLogin()){
            Intent intent = new Intent(RegisterActivity.this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    register();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void register() throws IOException,JSONException {
        if (!Utils.isNetworkAvailable(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showSimpleProgressDialog(RegisterActivity.this);
        final HashMap<String, String> map = new HashMap<>();
        map.put(Constants.Params.NAMA,nama.getText().toString());
        map.put(Constants.Params.PHONE,phone.getText().toString());
        map.put(Constants.Params.POSITION,position.getSelectedItem().toString());
        map.put(Constants.Params.DEPARTMENT,department.getSelectedItem().toString());
        map.put(Constants.Params.USERNAME, username.getText().toString());
        map.put(Constants.Params.PASSWORD, password.getText().toString());
        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    HttpRequest req = new HttpRequest(Constants.ServiceType.REGISTER);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss", result);
                onTaskCompleted(result, RegTask);
            }
        }.execute();
    }

    private void onTaskCompleted(String response,int task) {
        Log.d("responsejson", response.toString());
        Utils.removeSimpleProgressDialog();  //will remove progress dialog
        switch (task) {
            case RegTask:

                if (parseContent.isSuccess(response)) {
                    parseContent.saveInfo(response);
                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();
                }else {
                    Toast.makeText(RegisterActivity.this, parseContent.getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
        }
    }
}
