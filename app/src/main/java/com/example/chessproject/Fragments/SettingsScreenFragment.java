package com.example.chessproject.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.chessproject.R;

import java.util.HashMap;

public class SettingsScreenFragment extends Fragment {


    RadioGroup radioGroup;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SettingsScreenFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_screen, container, false);

        sharedPreferences = getActivity().getSharedPreferences("board_color", 0);
        editor = sharedPreferences.edit();

        String checkedColor = sharedPreferences.getString("color", "brown");

        radioGroup = view.findViewById(R.id.radioGroup);

        switch (checkedColor){
            case "brown":
                radioGroup.check(R.id.brownColor);
                break;
            case "green":
                radioGroup.check(R.id.greenColor);
                break;
            case "blue":
                radioGroup.check(R.id.blueColor);
                break;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.blueColor:
                        editor.putString("color", "blue");
                        editor.commit();
                        break;
                    case R.id.brownColor:
                        editor.putString("color", "brown");
                        editor.commit();
                        break;
                    case R.id.greenColor:
                        editor.putString("color", "green");
                        editor.commit();
                        break;
                }
            }
        });

        return view;
    }
}