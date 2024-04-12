package com.example.chessproject.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.GamesRecords.GameRecordRecyclerViewInterface;
import com.example.chessproject.R;
import com.example.chessproject.GamesRecords.gameRecord;
import com.example.chessproject.GamesRecords.gameRecordAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreviousGamesActivity extends AppCompatActivity implements GameRecordRecyclerViewInterface {
    ArrayList<gameRecord> allPreviousGame;
    RecyclerView previousGamesRecycler;
    gameRecordAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_games);

        allPreviousGame = new ArrayList<>();

        previousGamesRecycler = findViewById(R.id.gamesRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        previousGamesRecycler.setLayoutManager(layoutManager);

        adapter = new gameRecordAdapter(allPreviousGame, this);
        previousGamesRecycler.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("GamesRecords").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allPreviousGame.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    allPreviousGame.add(0,data.getValue(gameRecord.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        gameRecord gameHolder = allPreviousGame.get(position);
        Intent intent = new Intent(PreviousGamesActivity.this, ShowPreviousGameActivity.class);
        intent.putExtra("gameBoardPositions", gameHolder.gameMoves);
        intent.putExtra("EnemyUID", gameHolder.enemyPlayer.uid);
        intent.putExtra("UserColor", gameHolder.enemyPlayer.playerColor == Color.BLACK ? "white" : "black");
        startActivity(intent);

    }
}