package com.example.chessproject.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.chessproject.BoardUtilities.Color;
import com.example.chessproject.BoardUtilities.Player;
import com.example.chessproject.R;
import com.example.chessproject.BoardUtilities.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WaitingRoomActivity extends AppCompatActivity {

    ValueEventListener newRefListener;
    ProgressDialog progressDialog;
    DatabaseReference  playingRoomsReference;
    DatabaseReference waitingRoomsReference;
    DatabaseReference newRoomRef;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        progressDialog.setTitle("Waiting for an enemy...");
        progressDialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();

        waitingRoomsReference = firebaseDatabase.getReference().child("Rooms").child("WaitingRooms");
        playingRoomsReference = firebaseDatabase.getReference().child("Rooms").child("PlayingRooms");

        searchForRoom();
    }

    public void searchForRoom(){
        waitingRoomsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Choose to copy all the values to array list instead of for each loop in order to know when the loop reaches the end of the snapshot so new room can be created
                // this method avoid from the program to create the new room before the for each loop (the search for empty room) is end
                ArrayList<DataSnapshot> arrayList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren())
                    arrayList.add(data);

                // no rooms at all
                if (arrayList.size() == 0) {
                    createNewRoom();
                }

                else {
                    for (int i = 0; i < arrayList.size(); i++) {
                        DataSnapshot roomRef = arrayList.get(i);
                        Room roomHolder = roomRef.getValue(Room.class);
                        if (roomHolder.player2.uid.equals("")) {

                            // move the room from waitingRoom to playingRoom
                            roomHolder.player2.uid = FirebaseAuth.getInstance().getUid();
                            playingRoomsReference.child(roomRef.getKey()).setValue(roomHolder);

                            // update the creator of the room
                            roomRef.getRef().child("player2").child("uid").setValue(FirebaseAuth.getInstance().getUid());

                            // the room key is same in both waitingRooms and playingRooms data reference
                            Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
                            intent.putExtra("roomKey", String.valueOf(roomRef.getKey()));
                            intent.putExtra("UserColor", "black");

                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                        // end of the loop, no room has been found, create a new room
                        else if (i == arrayList.size() - 1) {
                            createNewRoom();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createNewRoom() {
        Room newRoom = new Room(new Player(Color.WHITE, FirebaseAuth.getInstance().getUid()), new Player(Color.BLACK, ""), getResources().getString(R.string.StartBoardFEN));
        newRoomRef = waitingRoomsReference.push();
        newRoomRef.setValue(newRoom);

        newRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room roomHolder = snapshot.getValue(Room.class);

                if (!roomHolder.player2.uid.equals("")) {
                    Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
                    intent.putExtra("UserColor", "white");
                    intent.putExtra("roomKey", newRoomRef.getKey());

                    progressDialog.dismiss();
                    startActivity(intent);
                    finish();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        newRoomRef.addValueEventListener(newRefListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            progressDialog.dismiss();

        if (newRoomRef != null && newRefListener != null) {
            newRoomRef.removeEventListener(newRefListener);
            newRoomRef.getRef().removeValue();
        }
    }
}