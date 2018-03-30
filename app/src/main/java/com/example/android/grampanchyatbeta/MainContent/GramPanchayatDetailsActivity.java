package com.example.android.grampanchyatbeta.MainContent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.grampanchyatbeta.R;

public class GramPanchayatDetailsActivity extends AppCompatActivity {
    private String mCurrentGramPanchayat;
    private String mCurrentGramPanchayatUID;
    private TextView currentGramPanchayatNameView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gram_panchayat_details);

        Intent intent=getIntent();
        mCurrentGramPanchayat=intent.getStringExtra("currentGrampanchayat");
        mCurrentGramPanchayatUID=intent.getStringExtra("currentGrampanchayatUID");

        currentGramPanchayatNameView=(TextView)findViewById(R.id.gram_panchayat_detail_name);
        currentGramPanchayatNameView.setText(mCurrentGramPanchayat);
    }
}
