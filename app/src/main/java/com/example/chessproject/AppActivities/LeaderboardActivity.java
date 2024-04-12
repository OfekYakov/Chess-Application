package com.example.chessproject.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.chessproject.LeaderBoard.LeaderBoardItem;
import com.example.chessproject.R;
import com.example.chessproject.UserUtils.User;
import com.example.chessproject.LeaderBoard.leaderBoardAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    ArrayList<LeaderBoardItem> userArrayList;
    DatabaseReference userRef;
    RecyclerView recyclerView;
    leaderBoardAdapter adapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Retrieving data");
        progressDialog.setCancelable(false);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userArrayList = new ArrayList<>();

        adapter = new leaderBoardAdapter(userArrayList);
        recyclerView.setAdapter(adapter);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.orderByChild("ChessScore").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User UserdataHolder = dataSnapshot.getValue(User.class);
                    LeaderBoardItem userTemplate = new LeaderBoardItem(UserdataHolder.username, UserdataHolder.ChessScore, null);
                    // add item in the last place of the array list
                    userArrayList.add(0 , userTemplate);

                    getUserImage(userTemplate, dataSnapshot.getKey());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void getUserImage(LeaderBoardItem userTemplate , String pictureKey){
        progressDialog.show();
        StorageReference imageReference =  FirebaseStorage.getInstance().getReference().child("Images").child(pictureKey);
        final long ONE_MEGABYTE = 1024 * 1024;
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                userTemplate.setUserPicture( BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });

    }

}