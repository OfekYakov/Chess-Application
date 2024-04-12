package com.example.chessproject.LeaderBoard;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chessproject.R;

import java.util.ArrayList;

public class leaderBoardAdapter extends RecyclerView.Adapter<leaderBoardAdapter.leaderboardViewHolder> {

    ArrayList<LeaderBoardItem> usersTemplates;

    public leaderBoardAdapter(ArrayList<LeaderBoardItem> usersTemplates){
        this.usersTemplates = usersTemplates;
    }

    @NonNull
    @Override
    public leaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View leaderboardItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item , parent, false);
        return new leaderBoardAdapter.leaderboardViewHolder(leaderboardItem);
    }

    @Override
    public void onBindViewHolder(@NonNull leaderboardViewHolder holder, int position) {
        LeaderBoardItem dataHolder = usersTemplates.get(position);
        holder.UserName.setText(dataHolder.getUserName() + "");
        holder.UserScore.setText(String.valueOf("(" + dataHolder.getUserScore()) + ")");
        if (dataHolder.UserPicture == null)
            holder.UserPicture.setImageBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.default_profile_picture));
        else
            holder.UserPicture.setImageBitmap(dataHolder.getUserPicture());
    }

    @Override
    public int getItemCount() {
        return usersTemplates.size();
    }

    public static class leaderboardViewHolder extends RecyclerView.ViewHolder{

        TextView UserName;
        TextView UserScore;
        ImageView UserPicture;
        public leaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            UserName = itemView.findViewById(R.id.itemUname);
            UserScore = itemView.findViewById(R.id.itemUscore);
            UserPicture = itemView.findViewById(R.id.itemUpicture);
        }
    }
}
