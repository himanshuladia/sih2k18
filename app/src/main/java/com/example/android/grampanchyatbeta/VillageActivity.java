package com.example.android.grampanchyatbeta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.grampanchyatbeta.Login.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;

public class VillageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village);

        mAuth=FirebaseAuth.getInstance();
        //Check whether user is already login in or not
        if(mAuth.getCurrentUser()==null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
