package com.example.chessproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chessproject.AppActivities.WaitingRoomActivity;
import com.example.chessproject.R;

public class HomeScreenFragment extends Fragment  {

    public HomeScreenFragment() {
    }

    Button btnPlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        btnPlay = view.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WaitingRoomActivity.class));
            }
        });

        return view;
    }

}