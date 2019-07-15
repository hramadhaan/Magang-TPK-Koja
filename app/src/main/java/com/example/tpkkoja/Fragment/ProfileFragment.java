package com.example.tpkkoja.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tpkkoja.R;
import com.example.tpkkoja.Services.PreferenceHelper;


public class ProfileFragment extends Fragment {
    EditText nama,phone,position,department;
    private PreferenceHelper preferenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        preferenceHelper = new PreferenceHelper(getContext());
        nama = view.findViewById(R.id.profile_nama);
        phone = view.findViewById(R.id.profile_telp);
        position = view.findViewById(R.id.profile_position);
        department = view.findViewById(R.id.profile_department);

        nama.setText(preferenceHelper.getNama());
        phone.setText(preferenceHelper.getPhone());
        position.setText(preferenceHelper.getPosition());
        department.setText(preferenceHelper.getDepartment());

        return view;
    }

    public interface OnFragmentInteractionListener {
    }
}
