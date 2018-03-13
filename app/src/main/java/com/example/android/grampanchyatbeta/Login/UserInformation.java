package com.example.android.grampanchyatbeta.Login;


import java.util.ArrayList;

public class UserInformation {

    public String Name;
    public String Email;
    public ArrayList<Integer> VillageAdopted=new ArrayList<>();

    public UserInformation(String Name, String Email)
    {
        this.Name=Name;
        this.Email=Email;
        VillageAdopted.add(0);

    }

    public UserInformation(int villageId)
    {
        VillageAdopted.clear();
        VillageAdopted.add(villageId);
    }

}
