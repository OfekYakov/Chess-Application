package com.example.chessproject.GamesRecords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chessproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gameRecordAdapter extends RecyclerView.Adapter<gameRecordAdapter.gameRecordViewHolder>{

    ArrayList<gameRecord> allGames;
    private final GameRecordRecyclerViewInterface gameRecordRecyclerViewInterface;

    public gameRecordAdapter(ArrayList<gameRecord> allGames, GameRecordRecyclerViewInterface gameRecordRecyclerViewInterface){
        this.allGames = allGames;
        this.gameRecordRecyclerViewInterface = gameRecordRecyclerViewInterface;
    }

    @NonNull
    @Override
    public gameRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gameRecordItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_game_item , parent, false);
        return new gameRecordAdapter.gameRecordViewHolder(gameRecordItem, gameRecordRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull gameRecordViewHolder holder, int position) {
        gameRecord gameHolder = allGames.get(position);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.enemyName.setText("Enemy : " + snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.gameResult.setText("Game Result : " + gameHolder.gameResult);
    }

    @Override
    public int getItemCount() {
        return allGames.size();
    }

    public static class gameRecordViewHolder extends RecyclerView.ViewHolder{
        TextView enemyName;
        TextView gameResult;

        public gameRecordViewHolder(@NonNull View itemView, GameRecordRecyclerViewInterface gameRecordRecyclerViewInterface) {
            super(itemView);
            enemyName = itemView.findViewById(R.id.gameRecordItmeEnemyName);
            gameResult = itemView.findViewById(R.id.recordGameGameResult);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gameRecordRecyclerViewInterface != null ){
                        if (getAdapterPosition() != RecyclerView.NO_POSITION)
                            gameRecordRecyclerViewInterface.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
