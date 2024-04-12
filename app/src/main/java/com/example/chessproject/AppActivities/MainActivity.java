package com.example.chessproject.AppActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.chessproject.Fragments.HomeScreenFragment;
import com.example.chessproject.R;
import com.example.chessproject.Fragments.SettingsScreenFragment;
import com.example.chessproject.UserUtils.User;
import com.example.chessproject.UserUtils.UserData;
import com.example.chessproject.Fragments.UserProfileScreenFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    BottomNavigationView bottomNavigationView;
    ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        // check if there is a user
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (firebaseAuth.getCurrentUser() != null) {
            retrieveUserData();
        }

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Leaderboard:
                        startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                        break;
                    case R.id.PreviousGames:
                        startActivity(new Intent(MainActivity.this, PreviousGamesActivity.class));
                        break;
                    case R.id.Logout:
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                }
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        replaceFragment(new HomeScreenFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        replaceFragment(new UserProfileScreenFragment());
                        break;
                    case R.id.settings:
                        replaceFragment(new SettingsScreenFragment());
                        break;
                    case R.id.home:
                        replaceFragment(new HomeScreenFragment());
                        break;
                }
                return true;
            }
        });

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void retrieveUserData() {
        progressDialog.setTitle("Retrieving data");
        progressDialog.show();
        DatabaseReference userData = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User temp = snapshot.getValue(User.class);
                UserData.password = temp.password;
                UserData.ChessScore = temp.ChessScore;
                UserData.username = temp.username;
                UserData.email = firebaseAuth.getCurrentUser().getEmail();
                getUserProfileImageFromStorage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void getUserProfileImageFromStorage(){
        StorageReference imageReference =  FirebaseStorage.getInstance().getReference().child("Images/" + firebaseAuth.getCurrentUser().getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        imageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                UserData.bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // set default image from drawable
                UserData.bitmap = null;
                progressDialog.dismiss();
            }
        });
    }

}