package com.example.android.grampanchyatbeta.UserWork;


import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.grampanchyatbeta.R;
import com.example.android.grampanchyatbeta.VillageInformation;

import java.util.ArrayList;

public class VillageAdapter extends ArrayAdapter<VillageInformation> {

    private Activity context;
    private ArrayList<VillageInformation> adoptedVillage;

    public VillageAdapter(@NonNull Activity context, ArrayList<VillageInformation> adoptedVillage) {
        super(context, R.layout.villagestructure, adoptedVillage);
        this.context=context;
        this.adoptedVillage=adoptedVillage;
    }

    @Override
    public int getCount() {
        return adoptedVillage.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        LayoutInflater inflater=context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.villagestructure,null,true);

        //
       TextView villageRating=(TextView)listItemView.findViewById(R.id.village_adopted_rating);
       TextView villageRanking=(TextView)listItemView.findViewById(R.id.village_adopted_ranking);
       TextView location=(TextView)listItemView.findViewById(R.id.village_adopted_village_location);
       TextView villageName=(TextView)listItemView.findViewById(R.id.village_adopted_village_name);

        VillageInformation currentVillageInformation=adoptedVillage.get(position);
        villageRating.setText(currentVillageInformation.getVillageRating());
        villageName.setText(currentVillageInformation.getNameVillage());
        villageRanking.setText(currentVillageInformation.getVillageRanking());
        location.setText(currentVillageInformation.getLocation());
        GradientDrawable magnitudeCircle = (GradientDrawable) villageRating.getBackground();
        int magnitudeColor=getMagnitudeColor(currentVillageInformation.getVillageRating());
        magnitudeCircle.setColor(magnitudeColor);
        return listItemView;
    }

    private int getMagnitudeColor(String villageRating) {
        int magnitudeColorResourceId;
        double magnitude = Double.parseDouble(villageRating);
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
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    }

