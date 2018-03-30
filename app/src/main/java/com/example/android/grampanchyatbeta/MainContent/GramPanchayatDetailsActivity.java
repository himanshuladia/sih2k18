package com.example.android.grampanchyatbeta.MainContent;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.grampanchyatbeta.R;
import com.example.android.grampanchyatbeta.VillageInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GramPanchayatDetailsActivity extends AppCompatActivity {
    private String mCurrentGramPanchayat;
    private ProgressDialog progressDialog;
    private String mCurrentGramPanchayatUID;
    private TextView currentGramPanchayatNameView;
    private DatabaseReference gramPanchayatDetailDatabase;
    private VillageInformation gramPanchayatDetail;
    private TextView currentGramPanchayatEducationView;
    private TextView currentGramPanchayatSanitationView;
    private TextView currentGramPanchayatHealthView;
    private TextView currentGramPanchayatLiteracyView;
    private TextView currentGramPanchayatWaterView;
    private TextView currentGramPanchayatWasteView;
    private TextView currentGramPanchayatAgricultureView;
    private TextView currentGramPanchayatDrainageView;
    private TextView currentGramPanchayatInfrastructureView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gram_panchayat_details);

        Intent intent = getIntent();
        mCurrentGramPanchayat = intent.getStringExtra("currentGrampanchayat");
        mCurrentGramPanchayatUID = intent.getStringExtra("currentGrampanchayatUID");
        gramPanchayatDetailDatabase = FirebaseDatabase.getInstance().getReference().child("Village").child(mCurrentGramPanchayatUID);
        currentGramPanchayatNameView = (TextView) findViewById(R.id.gram_panchayat_detail_name);
        currentGramPanchayatNameView.setText(mCurrentGramPanchayat);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        gramPanchayatDetailDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot gramPanchayatSnapshot) {

                String location = gramPanchayatSnapshot.child("location").getValue().toString();
                String gramPanchayatEducationIndex = gramPanchayatSnapshot.child("educationIndex").getValue().toString();
                String gramPanchayatSanitationIndex = gramPanchayatSnapshot.child("sanitationIndex").getValue().toString();
                String gramPanchayatHealthIndex = gramPanchayatSnapshot.child("healthIndex").getValue().toString();
                String gramPanchayatLiteracyIndex = gramPanchayatSnapshot.child("literacyIndex").getValue().toString();
                String gramPanchayatWaterIndex = gramPanchayatSnapshot.child("waterIndex").getValue().toString();
                String gramPanchayatWasteIndex = gramPanchayatSnapshot.child("wasteIndex").getValue().toString();
                String gramPanchayatAgricultureIndex = gramPanchayatSnapshot.child("agricultureIndex").getValue().toString();
                String gramPanchayatDrainageIndex = gramPanchayatSnapshot.child("drainageIndex").getValue().toString();
                String gramPanchayatRating = gramPanchayatSnapshot.child("villageRating").getValue().toString();
                String gramPanchayatInfraStructureIndex = gramPanchayatSnapshot.child("infraStructureIndex").getValue().toString();
                gramPanchayatDetail = new VillageInformation(location, mCurrentGramPanchayat, gramPanchayatRating, gramPanchayatEducationIndex, gramPanchayatSanitationIndex,
                        gramPanchayatHealthIndex, gramPanchayatLiteracyIndex, gramPanchayatWaterIndex, gramPanchayatWasteIndex, gramPanchayatAgricultureIndex, gramPanchayatDrainageIndex, gramPanchayatInfraStructureIndex);
                            setGramLayout();
                            progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
        private void setGramLayout()
        {
        //Setting the Layout
        currentGramPanchayatEducationView=(TextView)findViewById(R.id.gram_panchayat_education_index);
        currentGramPanchayatSanitationView=(TextView)findViewById(R.id.gram_panchayat_sanitation_index);
        currentGramPanchayatHealthView=(TextView)findViewById(R.id.gram_panchayat_health_index);
        currentGramPanchayatLiteracyView=(TextView)findViewById(R.id.gram_panchayat_literacy_index);
        currentGramPanchayatWaterView=(TextView)findViewById(R.id.gram_panchayat_water_index);
        currentGramPanchayatWasteView=(TextView)findViewById(R.id.gram_panchayat_waste_index);
        currentGramPanchayatAgricultureView=(TextView)findViewById(R.id.gram_panchayat_agriculture_index);
        currentGramPanchayatDrainageView=(TextView)findViewById(R.id.gram_panchayat_drainage_index);
        currentGramPanchayatInfrastructureView=(TextView)findViewById(R.id.gram_panchayat_infraStructure_index);

        currentGramPanchayatEducationView.setText(gramPanchayatDetail.getEducationIndex());
        GradientDrawable magnitudeEducationCircle = (GradientDrawable) currentGramPanchayatEducationView.getBackground();
        int magnitudeEducationColor=getMagnitudeColor(gramPanchayatDetail.getSanitationIndex());
        magnitudeEducationCircle.setColor(magnitudeEducationColor);

        currentGramPanchayatSanitationView.setText(gramPanchayatDetail.getSanitationIndex());
        GradientDrawable magnitudeSanitationCircle = (GradientDrawable) currentGramPanchayatSanitationView.getBackground();
        int magnitudeSanitationColor=getMagnitudeColor(gramPanchayatDetail.getSanitationIndex());
        magnitudeSanitationCircle.setColor(magnitudeSanitationColor);

        currentGramPanchayatHealthView.setText(gramPanchayatDetail.getHealthIndex());
        GradientDrawable magnitudeHealthCircle = (GradientDrawable) currentGramPanchayatHealthView.getBackground();
        int magnitudeHealthColor=getMagnitudeColor(gramPanchayatDetail.getHealthIndex());
        magnitudeHealthCircle.setColor(magnitudeHealthColor);

        currentGramPanchayatLiteracyView.setText(gramPanchayatDetail.getLiteracyIndex());
        GradientDrawable magnitudeLiteracyCircle = (GradientDrawable) currentGramPanchayatLiteracyView.getBackground();
        int magnitudeLiteracyColor=getMagnitudeColor(gramPanchayatDetail.getLiteracyIndex());
        magnitudeLiteracyCircle.setColor(magnitudeLiteracyColor);

        currentGramPanchayatWaterView.setText(gramPanchayatDetail.getWaterIndex());
        GradientDrawable magnitudeWaterCircle = (GradientDrawable) currentGramPanchayatWaterView.getBackground();
        int magnitudeWaterColor=getMagnitudeColor(gramPanchayatDetail.getWaterIndex());
        magnitudeWaterCircle.setColor(magnitudeWaterColor);

        currentGramPanchayatWasteView.setText(gramPanchayatDetail.getWasteIndex());
        GradientDrawable magnitudeWasteCircle = (GradientDrawable) currentGramPanchayatWasteView.getBackground();
        int magnitudeWasteColor=getMagnitudeColor(gramPanchayatDetail.getWasteIndex());
        magnitudeWasteCircle.setColor(magnitudeWasteColor);

        currentGramPanchayatAgricultureView.setText(gramPanchayatDetail.getAgricultureIndex());
        GradientDrawable magnitudeAgricultureCircle = (GradientDrawable) currentGramPanchayatAgricultureView.getBackground();
        int magnitudeAgricultureColor=getMagnitudeColor(gramPanchayatDetail.getAgricultureIndex());
        magnitudeAgricultureCircle.setColor(magnitudeAgricultureColor);

        currentGramPanchayatDrainageView.setText(gramPanchayatDetail.getDrainageIndex());
        GradientDrawable magnitudeDrainageCircle = (GradientDrawable) currentGramPanchayatDrainageView.getBackground();
        int magnitudeDrainageColor=getMagnitudeColor(gramPanchayatDetail.getDrainageIndex());
        magnitudeDrainageCircle.setColor(magnitudeDrainageColor);

        currentGramPanchayatInfrastructureView.setText(gramPanchayatDetail.getInfraStructureIndex());
        GradientDrawable magnitudeInfraStructureCircle = (GradientDrawable) currentGramPanchayatInfrastructureView.getBackground();
        int magnitudeInfraStructureColor=getMagnitudeColor(gramPanchayatDetail.getInfraStructureIndex());
        magnitudeInfraStructureCircle.setColor(magnitudeInfraStructureColor);


    }
    private int getMagnitudeColor(String paraMeter) {
        int magnitudeColorResourceId;
        double magnitude = Double.parseDouble(paraMeter);
        int magnitudeFloor=(int)Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
                magnitudeColorResourceId = R.color.ratingcolor1;
                break;
            case 1:
                magnitudeColorResourceId = R.color.ratingcolor2;
                break;
            case 2:
                magnitudeColorResourceId = R.color.ratingcolor3;
                break;
            case 3:
                magnitudeColorResourceId = R.color.ratingcolor4;
                break;
            default:
                magnitudeColorResourceId = R.color.ratingcolor5;
                break;
        }
        return ContextCompat.getColor(getApplicationContext(), magnitudeColorResourceId);
    }

}
