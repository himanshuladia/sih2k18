package com.example.android.grampanchyatbeta.UserWork;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.grampanchyatbeta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity{
    private DatabaseReference mUserDataBase;
    private FirebaseUser mCurrentUser;
    private TextView mDisplayName;
    private TextView mDisplayEmail;
    private TextView mVillageAdopted;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uuid=mCurrentUser.getUid();
        mUserDataBase= FirebaseDatabase.getInstance().getReference().child("User").child(current_user_uuid);

        mDisplayName=(TextView)findViewById(R.id.user_info_display_name);
        mDisplayEmail=(TextView)findViewById(R.id.user_info_display_email);
        mVillageAdopted=(TextView)findViewById(R.id.user_info_village_adopted);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);

        mUserDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName=dataSnapshot.child("Name").getValue().toString();
                String displayEmail=dataSnapshot.child("Email").getValue().toString();
                ArrayList<String>  villageAdopted= (ArrayList<String>) dataSnapshot.child("VillageAdopted").getValue();
                int noOfVillageAdopted=villageAdopted.size();
                String displayNoOfVillageAdopted=getApplicationContext().getResources().getString(R.string.user_info_village_adoption)+":-"+noOfVillageAdopted;

                mDisplayName.setText(displayName);
                mDisplayEmail.setText(displayEmail);
                mVillageAdopted.setText(displayNoOfVillageAdopted);
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserInfoActivity.this,R.string.user_info_toast_error,Toast.LENGTH_SHORT);
                mProgressDialog.dismiss();

            }
        });






    }
}
