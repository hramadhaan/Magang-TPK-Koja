package com.example.tpkkoja.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tpkkoja.BuildConfig;
import com.example.tpkkoja.MainActivity;
import com.example.tpkkoja.R;
import com.example.tpkkoja.RecyclerView.RecyclerViewAdapter;
import com.example.tpkkoja.Services.PreferenceHelper;
import com.example.tpkkoja.Weather.OpenWeatherMap;
import com.example.tpkkoja.Weather.ServiceHelper;
import com.example.tpkkoja.Weather.WeatherEndPoint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private static final String TAG = "MainActivity";

    RecyclerView recyclerView;
    TextView condition,temperature,location,hello;
    private WeatherEndPoint helper = new ServiceHelper().getWeatherService();
    private ImageView weatherIcon;
    private ArrayList<String> names = new ArrayList<>();

    private PreferenceHelper preferenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        condition = view.findViewById(R.id.textView_condition);
        temperature = view.findViewById(R.id.textView_temperature);
        location = view.findViewById(R.id.textView_location);
        recyclerView = view.findViewById(R.id.home_recycler);

        preferenceHelper = new PreferenceHelper(getContext());

        hello = view.findViewById(R.id.home_hello);
        hello.setText("Hello, "+preferenceHelper.getNama());

        iniImages();

        getLocation("Koja,ID");

        return view;
    }

    private void getLocation(String name) {
        helper.getWeatherFromLocationName(name, BuildConfig.API_KEY).enqueue(new Callback<OpenWeatherMap>() {
            @SuppressLint({"SetTextI18n","DefaultLocale"})
            @Override
            public void onResponse(@SuppressWarnings("NullableProblems") Call<OpenWeatherMap> call, @SuppressWarnings("NullableProblems") Response<OpenWeatherMap> response) {
                OpenWeatherMap result = response.body();

                if (result!=null) {
                    condition.setText(result.getWeatherList().get(0).getDescription());
                    location.setText(result.getName());
                    double temp = result.getMain().getTemp()-273;
                    temperature.setText(String.format("%.1f°C", temp));
                } else {
                    Toast.makeText(getActivity(),"Data tidak Muncul",Toast.LENGTH_LONG).show();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                call.cancel();
                Log.w("HomeFragemnt",t.getLocalizedMessage());
            }
        });
    }


    public interface OnFragmentInteractionListener {
    }

    private void iniImages() {
        names.add("Safe Patrol");
        names.add("Risk Patrol");
        names.add("Fitur Lainnya");
        initRecycllerView();
    }

    private void initRecycllerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(),names);
        recyclerView.setAdapter(adapter);
    }

}
