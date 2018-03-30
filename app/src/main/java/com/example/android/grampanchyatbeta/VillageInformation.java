package com.example.android.grampanchyatbeta;



public class VillageInformation {

    private String villageRating;
    private String location;
    private String nameVillage;
    private String educationIndex;
    private String sanitationIndex;
    private String healthIndex;
    private String literacyIndex;
    private String waterIndex;
    private String wasteIndex;
    private String agricultureIndex;
    private String drainageIndex;
    private String infraStructureIndex;





    public VillageInformation(String location,String nameVillage,String villageRating,String educationIndex,String sanitationIndex,String healthIndex,String literacyIndex,String waterIndex
    ,String wasteIndex,String agricultureIndex,String drainageIndex,String infraStructureIndex)
    {
        this.villageRating=villageRating;
        this.location=location;
        this.nameVillage=nameVillage;
        this.educationIndex=educationIndex;
        this.sanitationIndex=sanitationIndex;
        this.healthIndex=healthIndex;
        this.literacyIndex=literacyIndex;
        this.waterIndex=waterIndex;
        this.wasteIndex=wasteIndex;
        this.agricultureIndex=agricultureIndex;
        this.drainageIndex=drainageIndex;
        this.infraStructureIndex=infraStructureIndex;
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
    public String getEducationIndex()
    {
        return educationIndex;
    }
    public String getSanitationIndex()
    {
        return sanitationIndex;
    }
    public String getHealthIndex()
    {
        return healthIndex;
    }
    public String getLiteracyIndex() {
        return literacyIndex;
    }

    public String getWaterIndex() {
        return waterIndex;
    }

    public String getWasteIndex() {
        return wasteIndex;
    }

    public String getAgricultureIndex() {
        return agricultureIndex;
    }

    public String getDrainageIndex() {
        return drainageIndex;
    }

    public String getInfraStructureIndex() {
        return infraStructureIndex;
    }
}
