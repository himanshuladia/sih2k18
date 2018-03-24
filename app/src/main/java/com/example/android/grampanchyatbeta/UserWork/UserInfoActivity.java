package com.example.android.grampanchyatbeta.UserWork;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.grampanchyatbeta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class UserInfoActivity extends AppCompatActivity{
    private DatabaseReference mUserDataBase;
    private FirebaseUser mCurrentUser;
    private TextView mDisplayName;
    private TextView mDisplayEmail;
    private TextView mVillageAdopted;
    private ProgressDialog mProgressDialog;
    private ImageView profileImageView;
    private Uri selectedImage;
    private String ImageUrl;
    public StorageReference userProfileReference;
    private static int RESULT_LOAD_IMAGE = 1;
    private Intent i;
    private ImageView updateProfilePicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uuid=mCurrentUser.getUid();
        mUserDataBase= FirebaseDatabase.getInstance().getReference().child("User").child(current_user_uuid);
        userProfileReference= FirebaseStorage.getInstance().getReference();
        updateProfilePicture=(ImageView)findViewById(R.id.user_info_profile_update_image);
        mDisplayName=(TextView)findViewById(R.id.user_info_display_name);
        mDisplayEmail=(TextView)findViewById(R.id.user_info_display_email);
        mVillageAdopted=(TextView)findViewById(R.id.user_info_village_adopted);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);
        updateProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadProfilePicture();
            }
        });
        //Opening the Gallery
        profileImageView = (ImageView) findViewById(R.id.user_info_profile_image);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mUserDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName=dataSnapshot.child("Name").getValue().toString();
                String displayEmail=dataSnapshot.child("Email").getValue().toString();
                String displayProfilePicture=dataSnapshot.child("ImageUrl").getValue().toString();
                if(!(displayProfilePicture.equals("")))
                {
                    Glide.with(getApplicationContext()).load(displayProfilePicture).into(profileImageView);
                }
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
    private void UploadProfilePicture()
    {
        final String locationProfilePicture = "UserImage/" + mCurrentUser.getUid() + ".jpg";
        if (selectedImage != null) {
            StorageReference riversRef = userProfileReference.child(locationProfilePicture);
            UploadTask uploadTask= riversRef.putFile(selectedImage);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    ImageUrl=downloadUrl.toString();
                    saveUserInformation();
                    mProgressDialog.dismiss();
                }
            });
        }
    }
    private void saveUserInformation()
    {
        mUserDataBase.getDatabase().getReference().child("User").child(mCurrentUser.getUid()).child("ImageUrl").setValue(ImageUrl);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            profileImageView.setImageURI(selectedImage);
        }

    }
}
