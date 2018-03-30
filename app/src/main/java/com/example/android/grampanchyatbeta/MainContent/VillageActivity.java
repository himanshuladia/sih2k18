package com.example.android.grampanchyatbeta.MainContent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.android.grampanchyatbeta.Login.OnBoardingScreenActivity;
import com.example.android.grampanchyatbeta.Login.SignInActivity;
import com.example.android.grampanchyatbeta.R;
import com.example.android.grampanchyatbeta.UserWork.UserInfoActivity;

import com.example.android.grampanchyatbeta.UserWork.VillageAdapter;
import com.example.android.grampanchyatbeta.VillageInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private Spinner sortingSpinner;
    private  String itemSelected;
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
        //Sorting Activity
        sortingSpinner=(Spinner)findViewById(R.id.gram_panchayat_spinner);
        String[] items = new String[]{getResources().getString(R.string.over_all_index),getResources().getString(R.string.education_index),getResources().getString(R.string.sanitation_index),getResources().getString(R.string.health_index),
               getResources().getString(R.string.literacy_index),getResources().getString(R.string.water_index),getResources().getString(R.string.waste_land_index),
                getResources().getString(R.string.agriculture_land_index),getResources().getString(R.string.drainage_index),
                getResources().getString(R.string.built_up_index)};
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sortingSpinner.setAdapter(dropDownAdapter);
        //
       /** sortingSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String itemSelected=adapterView.getItemAtPosition(position).toString();
            }
        });**/
        sortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected=parent.getItemAtPosition(position).toString();
                if(itemSelected.contains(" ")){
                    itemSelected= itemSelected.substring(0, itemSelected.indexOf(" "));
                }
                Sorting(itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    String gramPanchayatEducationIndex=gramPanchayatSnapshot.child("educationIndex").getValue().toString();
                    String gramPanchayatSanitationIndex=gramPanchayatSnapshot.child("sanitationIndex").getValue().toString();
                    String gramPanchayatHealthIndex=gramPanchayatSnapshot.child("healthIndex").getValue().toString();
                    String gramPanchayatLiteracyIndex=gramPanchayatSnapshot.child("literacyIndex").getValue().toString();
                    String gramPanchayatWaterIndex=gramPanchayatSnapshot.child("waterIndex").getValue().toString();
                    String gramPanchayatWasteIndex=gramPanchayatSnapshot.child("wasteIndex").getValue().toString();
                    String gramPanchayatAgricultureIndex=gramPanchayatSnapshot.child("agricultureIndex").getValue().toString();
                    String gramPanchayatDrainageIndex=gramPanchayatSnapshot.child("drainageIndex").getValue().toString();
                    String gramPanchayatRating=gramPanchayatSnapshot.child("villageRating").getValue().toString();
                    String gramPanchayatInfraStructureIndex=gramPanchayatSnapshot.child("infraStructureIndex").getValue().toString();
                    VillageInformation mTemporaryVillage=new VillageInformation(location,nameGramPanchayat,gramPanchayatRating,gramPanchayatEducationIndex,gramPanchayatSanitationIndex,
                            gramPanchayatHealthIndex,gramPanchayatLiteracyIndex,gramPanchayatWaterIndex,gramPanchayatWasteIndex,gramPanchayatAgricultureIndex,gramPanchayatDrainageIndex,gramPanchayatInfraStructureIndex);
                    informationVillage.add(mTemporaryVillage);

                }
                progressDialog.dismiss();
                 Sorting(itemSelected);
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
        public void Sorting(String typeOfSort)
        {
            if(typeOfSort.equals("Education"))
            {

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getEducationIndex().compareTo(gramPanchayat2.getEducationIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("Sanitation"))
            {

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getSanitationIndex().compareTo(gramPanchayat2.getSanitationIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("Health"))
            {

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getHealthIndex().compareTo(gramPanchayat2.getHealthIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("Literacy"))
            {

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getLiteracyIndex().compareTo(gramPanchayat2.getLiteracyIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("Water")){

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getWaterIndex().compareTo(gramPanchayat2.getWaterIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);

            }
            if(typeOfSort.equals("Waste")){

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getWasteIndex().compareTo(gramPanchayat2.getWasteIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("Agriculture"))
            {

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getAgricultureIndex().compareTo(gramPanchayat2.getAgricultureIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("Drainage"))
            {

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getDrainageIndex().compareTo(gramPanchayat2.getDrainageIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("InfraStructure"))
            {

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getInfraStructureIndex().compareTo(gramPanchayat2.getInfraStructureIndex());
                    }
                });
                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
            if(typeOfSort.equals("Over")){

                Collections.sort(informationVillage, new Comparator<VillageInformation>() {
                    public int compare(VillageInformation gramPanchayat1, VillageInformation gramPanchayat2) {
                        return gramPanchayat1.getVillageRating().compareTo(gramPanchayat2.getVillageRating());
                    }
                });

                villageAdapter =new VillageAdapter(VillageActivity.this,informationVillage);
                villageListView.setAdapter(villageAdapter);
            }
        }


}
