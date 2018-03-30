package com.example.android.grampanchyatbeta.MainContent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.grampanchyatbeta.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GramPanchayatDetailsActivity extends AppCompatActivity {
    private String mCurrentGramPanchayat;
    private String mCurrentGramPanchayatUID;
    private TextView currentGramPanchayatNameView;
    private DatabaseReference gramPanchayatDetailDatabase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gram_panchayat_details);

        Intent intent=getIntent();
        mCurrentGramPanchayat=intent.getStringExtra("currentGrampanchayat");
        mCurrentGramPanchayatUID=intent.getStringExtra("currentGrampanchayatUID");
        gramPanchayatDetailDatabase= FirebaseDatabase.getInstance().getReference().child("Village").child(mCurrentGramPanchayatUID);
        currentGramPanchayatNameView=(TextView)findViewById(R.id.gram_panchayat_detail_name);
        currentGramPanchayatNameView.setText(mCurrentGramPanchayat);
    }
}
