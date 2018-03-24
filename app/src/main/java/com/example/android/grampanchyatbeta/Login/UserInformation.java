package com.example.android.grampanchyatbeta.Login;


import java.util.ArrayList;

public class UserInformation {

    public String Name;
    public String Email;
    public String ImageUrl;
    public ArrayList<String> VillageAdopted=new ArrayList<>();


    public UserInformation(String Name, String Email)
    {
        this.Name=Name;
        this.Email=Email;
        VillageAdopted.add("0");
        ImageUrl="";
    }

    public UserInformation(String villageId)
    {
        VillageAdopted.add(villageId);
    }

}
