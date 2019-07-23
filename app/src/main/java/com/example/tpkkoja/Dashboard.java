package com.example.tpkkoja;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tpkkoja.AccountActivity.LoginActivity;
import com.example.tpkkoja.Content.ListUpload;
import com.example.tpkkoja.Fragment.HomeFragment;
import com.example.tpkkoja.Fragment.PagerAdapter;
import com.example.tpkkoja.Fragment.ProfileFragment;
import com.example.tpkkoja.Services.PreferenceHelper;


public class Dashboard extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    PreferenceHelper preferenceHelper;
    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        preferenceHelper = new PreferenceHelper(this);

        Toolbar toolbar = findViewById(R.id.dashboard_toolbar);
        TextView judul = toolbar.findViewById(R.id.dashboard_judul);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomNavigationView = findViewById(R.id.dashboard_bottom);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.dashboard_frame,new HomeFragment()).commit();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.bottom_profile:
                        fragment = new ProfileFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.dashboard_frame,fragment).commit();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                startActivity(new Intent(Dashboard.this, ListUpload.class));
                break;
            case R.id.logout:
                preferenceHelper.putIsLogin(false);
                Intent intent = new Intent(Dashboard.this, AwalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Dashboard.this.finish();
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
