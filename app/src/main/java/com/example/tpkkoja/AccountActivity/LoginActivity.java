package com.example.tpkkoja.AccountActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    Button btn_login;
    TextView register;
    EditText user,pass;

    ParseContent parseContent;
    PreferenceHelper preferenceHelper;
    String ini_user;

    private final int LoginTask = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.login_button);
        register = findViewById(R.id.register);
        user = findViewById(R.id.login_username);
        pass = findViewById(R.id.login_password);

        parseContent = new ParseContent(this);
        preferenceHelper = new PreferenceHelper(this);

        if (preferenceHelper.getIsLogin()){
            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("username",user.getText().toString());
            startActivity(intent);
            this.finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    private void login() throws IOException,JSONException {
        if (!Utils.isNetworkAvailable(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, "Internet is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.showSimpleProgressDialog(LoginActivity.this);
        final HashMap<String, String> map = new HashMap<>();
        map.put(Constants.Params.USERNAME, user.getText().toString());
        map.put(Constants.Params.PASSWORD, pass.getText().toString());
        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    HttpRequest req = new HttpRequest(Constants.ServiceType.LOGIN);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response=e.getMessage();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss", result);
                onTaskCompleted(result,LoginTask);
            }
        }.execute();
    }

    private void onTaskCompleted(String response,int task) {
        Log.d("responsejson", response.toString());
        Utils.removeSimpleProgressDialog();  //will remove progress dialog
        switch (task) {
            case LoginTask:

                if (parseContent.isSuccess(response)) {

                    parseContent.saveInfo(response);
                    Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    this.finish();

                }else {
                    Toast.makeText(LoginActivity.this, parseContent.getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(this,"Tekan BACK untuk keluar",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}
