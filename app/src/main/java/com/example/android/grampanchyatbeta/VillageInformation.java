package com.example.android.grampanchyatbeta;



public class VillageInformation {

    private String villageRating;
    private String location;
    private String nameVillage;
    private String villageRanking;

    public VillageInformation(String location,String nameVillage,String villageRanking,String villageRating)
    {
        this.villageRating=villageRating;
        this.location=location;
        this.nameVillage=nameVillage;
        this.villageRanking=villageRanking;
    }
    public VillageInformation()
    {

    }
    public String getVillageRating()
    {
        return villageRating;
    }
    public String getLocation()
    {
        return location;
    }
    public String getNameVillage()
    {
        return nameVillage;
    }
    public String getVillageRanking()
    {
        return villageRanking;
    }
}
