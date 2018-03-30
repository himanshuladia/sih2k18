package com.example.android.grampanchyatbeta.MainContent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.grampanchyatbeta.Login.OnBoardingScreenActivity;
import com.example.android.grampanchyatbeta.Login.SignInActivity;
import com.example.android.grampanchyatbeta.R;
import com.example.android.grampanchyatbeta.UserWork.UserInfoActivity;
import com.example.android.grampanchyatbeta.UserWork.VillageAdoptionActivity;
import com.example.android.grampanchyatbeta.UserWork.VillageAdapter;
import com.example.android.grampanchyatbeta.VillageInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class VillageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolBar;
    private ListView villageListView;
    private ArrayList<VillageInformation> informationVillage;
    private ProgressDialog progressDialog;
    DatabaseReference villageDataBase;
    private VillageAdapter villageAdapter;
    private String currentGramPanchayatName;
    public HashMap<String,String> gramPanchayatFirebaseUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village);

        mAuth=FirebaseAuth.getInstance();
        //Check whether user is already login in or not
        if(mAuth.getCurrentUser()==null) {
            startActivity(new Intent(this, OnBoardingScreenActivity.class));
            finish();
        }
        gramPanchayatFirebaseUID=new HashMap<>();
        progressDialog=(ProgressDialog)new ProgressDialog(this);
        informationVillage=new ArrayList<>();
        villageListView=(ListView)findViewById(R.id.village_list_view);
            villageDataBase= FirebaseDatabase.getInstance().getReference().child("Village");
            mToolBar=(Toolbar)findViewById(R.id.tool_bar_village);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(R.string.village_activity_toolbar_title);
        villageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                VillageInformation currentVillageInformation=villageAdapter.getItem(position);
                currentGramPanchayatName=currentVillageInformation.getNameVillage();
                Intent intent=new Intent(VillageActivity.this,GramPanchayatDetailsActivity.class);
                String GramPanchayatUID=gramPanchayatFirebaseUID.get(currentGramPanchayatName);
                intent.putExtra("currentGrampanchayatUID",GramPanchayatUID);
                intent.putExtra("currentGrampanchayat",currentGramPanchayatName);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.village_progress_dialog));
        progressDialog.show();
        villageDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                informationVillage.clear();
                for(DataSnapshot gramPanchayatSnapshot: dataSnapshot.getChildren())
                {
                    String GramPanchayatUID=gramPanchayatSnapshot.getKey().toString();
                    String nameGramPanchayat=gramPanchayatSnapshot.child("nameVillage").getValue().toString();
                    gramPanchayatFirebaseUID.put(nameGramPanchayat,GramPanchayatUID);
                    String location=gramPanchayatSnapshot.child("location").getValue().toString();
                    String grampPanchayatRanking=gramPanchayatSnapshot.child("villageRanking").getValue().toString();
                    String gramPanchayatRating=gramPanchayatSnapshot.child("villageRating").getValue().toString();
                    VillageInformation mTemporaryVillage=new VillageInformation(location,nameGramPanchayat,grampPanchayatRanking,gramPanchayatRating);
                    informationVillage.add(mTemporaryVillage);

                }
                progressDialog.dismiss();
                 villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.villagesetting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.village_adopted_menu)
        {
            startActivity(new Intent(VillageActivity.this,VillageAdoptionActivity.class));
            return true;
        }
        if(item.getItemId()==R.id.log_out_menu)
        {
            mAuth.signOut();
            startActivity(new Intent(VillageActivity.this,SignInActivity.class));
            finish();
        }
        if(item.getItemId()==R.id.settings_menu)
        {
            startActivity(new Intent(VillageActivity.this, UserInfoActivity.class));

        }
        return true;

    }
}
