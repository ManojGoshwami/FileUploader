package com.sushi.fileUploader.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Floor implements Serializable {
    @SerializedName("floorname")
    @ColumnInfo(name = "floor_name")
    private String floorName;
    @SerializedName("floorCoveredarea")
    @ColumnInfo(name = "covered_area")
    private String coveredArea;
    @SerializedName("Propertytype")
    @ColumnInfo(name = "property_type")
    private String propertyType;
    @SerializedName("nonresidentialcategory")
    @ColumnInfo(name = "non_residential_type")
    private String nonResidentialType;
    @SerializedName("nonresidentialsubcategory")
    @ColumnInfo(name = "non_residential_use")
    private String nonResidentialUse;
    @ColumnInfo(name = "non_res_cat_id")
    private String nonResCatId;
    @ColumnInfo(name = "non_res_sub_cat_id")
    private String nonResSubCatId;
    @SerializedName("IfOpenPlot")
    @ColumnInfo(name = "open_plot")
    private String openPlot;

    @SerializedName("IfOpenPlot_Defecation")
    @ColumnInfo(name = "used_for_defecation")
    private String usedForDefecation;


    @SerializedName("IfOpenPlot_Grabagedumping")
    @ColumnInfo(name = "used_for_grabage")
    private String usedForGrabage;


    @ColumnInfo(name = "used_for_garbage_dumping")
    private String usedForGarbageDumping;
    @SerializedName("Ownershiptype")
    @ColumnInfo(name = "ownership_type")
    private String ownershipType;
    @SerializedName("SubOwnershipCategory")
    @ColumnInfo(name = "sub_ownership_category")
    private String subOwnershipCategory;
    @SerializedName("FloorOccupier")
    @ColumnInfo(name = "floor_occupier")
    private String floorOccupier;
    @SerializedName("FloorOccupiertenantDate")
    @ColumnInfo(name = "occupancy_date")
    private String occupancyDate;
    @SerializedName("FloorConstructionType")
    @ColumnInfo(name = "floor_construction_type")
    private String floorConstructionType;
    @SerializedName("FloorYearConstruction")
    @ColumnInfo(name = "construction_year")
    private String constructionYear;
    @SerializedName("RateofConstruction")
    @ColumnInfo(name = "construction_rate")
    private String constructionRate;
    @SerializedName("uid")
    @ColumnInfo(name = "property_id")
    private String floorPropertyId;
    @ColumnInfo(name = "created_date")
    private String createdDate;
    @ColumnInfo(name = "flag")
    private int flag;

    @SerializedName("PropertyMainUse")
    @ColumnInfo(name = "property_use")
    private String property_use;

    @SerializedName("InstitutionType")
    @ColumnInfo(name = "institution_type")
    private String institution_type;

    @SerializedName("InstitutionName")
    @ColumnInfo(name = "institution_name")
    private String institution_name;

    @SerializedName("Status")
    @ColumnInfo(name = "status")
    private String status;


    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getCoveredArea() {
        return coveredArea;
    }

    public void setCoveredArea(String coveredArea) {
        this.coveredArea = coveredArea;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getSubOwnershipCategory() {
        return subOwnershipCategory;
    }

    public void setSubOwnershipCategory(String subOwnershipCategory) {
        this.subOwnershipCategory = subOwnershipCategory;
    }

    public String getFloorOccupier() {
        return floorOccupier;
    }

    public void setFloorOccupier(String floorOccupier) {
        this.floorOccupier = floorOccupier;
    }

    public String getFloorConstructionType() {
        return floorConstructionType;
    }

    public void setFloorConstructionType(String floorConstructionType) {
        this.floorConstructionType = floorConstructionType;
    }

    public String getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(String constructionYear) {
        this.constructionYear = constructionYear;
    }

    public String getConstructionRate() {
        return constructionRate;
    }

    public void setConstructionRate(String constructionRate) {
        this.constructionRate = constructionRate;
    }

    public String getFloorPropertyId() {
        return floorPropertyId;
    }

    public void setFloorPropertyId(String floorPropertyId) {
        this.floorPropertyId = floorPropertyId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getNonResidentialType() {
        return nonResidentialType;
    }

    public void setNonResidentialType(String nonResidentialType) {
        this.nonResidentialType = nonResidentialType;
    }

    public String getNonResidentialUse() {
        return nonResidentialUse;
    }

    public void setNonResidentialUse(String nonResidentialUse) {
        this.nonResidentialUse = nonResidentialUse;
    }

    public String getOccupancyDate() {
        return occupancyDate;
    }

    public void setOccupancyDate(String occupancyDate) {
        this.occupancyDate = occupancyDate;
    }

    public String getOpenPlot() {
        return openPlot;
    }

    public void setOpenPlot(String openPlot) {
        this.openPlot = openPlot;
    }

    public String getUsedForDefecation() {
        return usedForDefecation;
    }

    public void setUsedForDefecation(String usedForDefecation) {
        this.usedForDefecation = usedForDefecation;
    }
    public String getUsedForGrabage() {
        return usedForGrabage;
    }

    public void setUsedForGrabage(String usedForGrabage) {
        this.usedForGrabage = usedForGrabage;
    }


    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getUsedForGarbageDumping() {
        return usedForGarbageDumping;
    }

    public void setUsedForGarbageDumping(String usedForGarbageDumping) {
        this.usedForGarbageDumping = usedForGarbageDumping;
    }

    public String getNonResCatId() {
        return nonResCatId;
    }

    public void setNonResCatId(String nonResCatId) {
        this.nonResCatId = nonResCatId;
    }

    public String getNonResSubCatId() {
        return nonResSubCatId;
    }

    public void setNonResSubCatId(String nonResSubCatId) {
        this.nonResSubCatId = nonResSubCatId;
    }

//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProperty_use() {
        return property_use;
    }

    public void setProperty_use(String property_use) {
        this.property_use = property_use;
    }

    public String getInstitution_type() {
        return institution_type;
    }

    public void setInstitution_type(String institution_type) {
        this.institution_type = institution_type;
    }

    public String getInstitution_name() {
        return institution_name;
    }

    public void setInstitution_name(String institution_name) {
        this.institution_name = institution_name;
    }
}
