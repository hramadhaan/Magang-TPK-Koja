package com.example.tpkkoja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tpkkoja.AccountActivity.LoginActivity;
import com.example.tpkkoja.AccountActivity.RegisterActivity;
import com.example.tpkkoja.Services.ParseContent;
import com.example.tpkkoja.Services.PreferenceHelper;

public class AwalActivity extends AppCompatActivity {

    Button btn_register;
    TextView btn_login;

    PreferenceHelper preferenceHelper;

    ParseContent parseContent;

    private final int LoginTask = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awal);

        preferenceHelper = new PreferenceHelper(this);
        parseContent = new ParseContent(this);

        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AwalActivity.this, RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AwalActivity.this, LoginActivity.class));
            }
        });

        if (preferenceHelper.getIsLogin()){
            Intent intent = new Intent(AwalActivity.this, Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }

    }
}
