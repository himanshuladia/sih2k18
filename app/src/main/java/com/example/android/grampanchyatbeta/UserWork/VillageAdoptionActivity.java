package com.example.android.grampanchyatbeta.UserWork;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.grampanchyatbeta.R;
import com.example.android.grampanchyatbeta.VillageInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;

public class VillageAdoptionActivity extends AppCompatActivity {

    private DatabaseReference mUserDataBase;
    private FirebaseUser mCurrentUser;
    private Toolbar mToolBar;
    private DatabaseReference mVillageAdoptedDatabase;
    private VillageAdapter mAdapter;
    private ListView villageAdoptionlistView;
    private ArrayList<VillageInformation> informationVillageAdopted;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_adoption);

        mToolBar=(Toolbar)findViewById(R.id.tool_bar_village_adoption);
        setSupportActionBar(mToolBar);
       getSupportActionBar().setTitle(R.string.village_adoption_toolbar_tiltle);
        villageAdoptionlistView=(ListView)findViewById(R.id.village_adoption_list_view);
        informationVillageAdopted=new ArrayList<>();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_user_uuid=mCurrentUser.getUid();
        mUserDataBase= FirebaseDatabase.getInstance().getReference().child("User").child(current_user_uuid);
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);




        mUserDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> villageAdopted=(ArrayList<String>)dataSnapshot.child("VillageAdopted").getValue();
                getListOfVillageAdopted(villageAdopted);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      /** villageAdoptionlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                VillageInformation currentVillage=mAdapter.getItem(position);

            }
        });**/

    }

    public void getListOfVillageAdopted(ArrayList<String> villageAdopted)
    {
        for(int i=0;i<villageAdopted.size();i++)
        {
        mVillageAdoptedDatabase=FirebaseDatabase.getInstance().getReference().child("Village").child("0");
        mVillageAdoptedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nameVillage=dataSnapshot.child("nameVillage").getValue().toString();
                String location=dataSnapshot.child("location").getValue().toString();
                String villageRating=dataSnapshot.child("villageRating").getValue().toString();
                //VillageInformation mTemporaryVillage=new VillageInformation(location,nameVillage,villageRating);
               // informationVillageAdopted.add(mTemporaryVillage);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }
        mAdapter=new VillageAdapter(VillageAdoptionActivity.this,informationVillageAdopted);
        villageAdoptionlistView.setAdapter(mAdapter);
        mProgressDialog.dismiss();

    }

}
