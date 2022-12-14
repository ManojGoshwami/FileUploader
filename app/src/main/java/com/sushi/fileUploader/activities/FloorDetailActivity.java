package com.sushi.fileUploader.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sushi.fileUploader.R;
import com.sushi.fileUploader.common.Utility;
import com.sushi.fileUploader.models.Floor;
import com.sushi.fileUploader.utility.AppConstants;
import com.sushi.fileUploader.utility.TinyDB;
import com.sushi.fileUploader.utility.Util;
import com.sushi.fileUploader.viewModel.PropertyViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FloorDetailActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher{
    TextView floorName;
    String[] fArray;
    ArrayAdapter<String> itemsArrayList;
    ImageButton plus, minus;
    ScrollView floorDetailsLayout;
    private List<Floor> floorList;
    private ConstraintLayout rootConstraint;
    Spinner floorNameSpinner, propertyTypeSpinner, propertyUseSPIN, floorConstructionTypeSpinner, nonResidentialPropertySpinner,
            noResidentialPropertyUseSpinner, ownershipTypeSpinner, subOwnershipTypeSpinner, instutionTpyeSPIN;
    TextInputEditText floorArea, occupancyDate, floorConstructionYear, floorRateOfConstruction,
            floorInstitutionTypeEDT,floorCount,floorInstitutionNameEDT;

    RadioGroup openPlotRadioGrp, floorOccupierRadioGrp, usedForDefecationGrp, used_for_Grabagedumping_grp;
    LinearLayout openPlotLayout, nonResLayout, nonResPropUseLayout, usedForDefecationLayout, ll_usedForgrabage;
    String[] nonResdentialPropertyUseArray;
    MaterialButton addFloorBtn, submitBtn;
    TextInputLayout occupancyDateLayout;
    private String property_use = "";
    private String institutionType = "";
    private String total_plot_area, floor_area;
    private String florName = "", built_up_value = "", propertyType = "", openPlot = "", usedForDefecation = "", usedForGrabagedumping = "", uid = "",
            nonResidentialType = "", nonResidentialUse = "", nonResCatId = "", nonResSubCatId = "", ownershipType = "", subOwnershipType = "", floorOccupier = "", floorConstructionType = "";
    private int totalFloors = 3, insertedFloors = 0;
    private LinearLayout ll_institutionNameType;
    private PropertyViewModel propertyViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.hideStatusBar(this);
        setContentView(R.layout.activity_floor_detail);
        uid = Util.getString(this, AppConstants.propertyUID);//getIntent().getStringExtra(AppConstants.propertyUID);
//        String str = getIntent().getStringExtra(AppConstants.TOTAL_FLOORS);
//        total_plot_area = getIntent().getStringExtra(AppConstants.TOTAL_PLOAT_AREA);
        setToolbar();
        initializeViews();
        if (floorCount.getText().toString().equals("")) {
//            Toast.makeText(this, "Please enter no of floor first", Toast.LENGTH_LONG).show();
            floorNameSpinner.setEnabled(false);
        }


    }
    /*method for setting toolbar*/
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = findViewById(R.id.title);
        title.setText("Floor");
        // hiding default title text on toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

    }

    /*method for initializing views*/
    private void initializeViews() {
        rootConstraint=findViewById(R.id.rootConstraint);
        ll_institutionNameType = findViewById(R.id.ll_institutionNameType);
        floorNameSpinner = findViewById(R.id.floor_name_dropdown);
        floorArea = findViewById(R.id.floor_covered_area);
        floorCount=findViewById(R.id.floor_count);
        propertyUseSPIN = findViewById(R.id.property_use_dropdown);
        instutionTpyeSPIN = findViewById(R.id.spinner_institution_type);
        floorInstitutionNameEDT = findViewById(R.id.floor_institution_name);
        occupancyDate = findViewById(R.id.occupying_date_edit_txt);
        propertyTypeSpinner = findViewById(R.id.property_type_dropdown);
        propertyUseSPIN = findViewById(R.id.property_use_dropdown);
        nonResidentialPropertySpinner = findViewById(R.id.non_residential_type_dropdown);
        noResidentialPropertyUseSpinner = findViewById(R.id.non_residential_use_dropdown);
        ownershipTypeSpinner = findViewById(R.id.ownership_dropdown);
        subOwnershipTypeSpinner = findViewById(R.id.sub_ownership_dropdown);
        openPlotLayout = findViewById(R.id.open_plot_ll);
        openPlotRadioGrp = findViewById(R.id.open_plot_grp);
        floorOccupierRadioGrp = findViewById(R.id.floor_occupier_grp);
        usedForDefecationGrp = findViewById(R.id.used_for_defecation_grp);



        // new added
        used_for_Grabagedumping_grp = findViewById(R.id.used_for_Grabagedumping_grp);
        ll_usedForgrabage = findViewById(R.id.ll_usedForgrabage);
        ///
        nonResLayout = findViewById(R.id.no_residential_layout);
        nonResPropUseLayout = findViewById(R.id.non_res_use_ll);
        usedForDefecationLayout = findViewById(R.id.used_for_defecation_layout);


        occupancyDateLayout = findViewById(R.id.occupying_date_text_input);
        floorConstructionYear = findViewById(R.id.floor_cons_yr);
        floorRateOfConstruction = findViewById(R.id.floor_cons_rate);
        floorConstructionTypeSpinner = findViewById(R.id.floor_cons_type_dropdown);
        propertyViewModel= ViewModelProviders.of(this).get(PropertyViewModel.class);
        addFloorBtn = findViewById(R.id.add_floor_btn);
        addFloorBtn.setOnClickListener(this);
        occupancyDate.setOnClickListener(this);
        floorCount.addTextChangedListener(this);
        setPropertyUseDropDownListener();
        setFloorDropDownListeners();
        setFloorGroupListeners();

    }


    private void setFloorGroupListeners() {
        openPlotRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.open_plot_yes:
                        openPlot = "Yes";
                        usedForDefecationLayout.setVisibility(View.VISIBLE);
                        ll_usedForgrabage.setVisibility(View.VISIBLE);
                        break;
                    case R.id.open_plot_no:
                        openPlot = "No";
                        usedForDefecationLayout.setVisibility(View.GONE);
                        ll_usedForgrabage.setVisibility(View.GONE);
                        break;
                }
            }
        });

        usedForDefecationGrp.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.used_for_def_yes:
                    usedForDefecation = "Yes";
                    break;
                case R.id.used_for_def_no:
                    usedForDefecation = "No";
                    break;
            }
        });

        used_for_Grabagedumping_grp.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.used_for_Grabagedumping_yes:
                    usedForGrabagedumping = "Yes";
                    break;
                case R.id.used_for_Grabagedumping_no:
                    usedForGrabagedumping = "No";
                    break;
            }
        });


        floorOccupierRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.unoccupied:
                        floorOccupier = "Unoccupied";
                        occupancyDateLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.self:
                        floorOccupier = "Self";
                        occupancyDateLayout.setVisibility(View.GONE);
                        break;

                    case R.id.rented_radiobtn:
                        floorOccupier = "Rented";
                        occupancyDateLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    //TODO method for setting property user dropdown
    private void setPropertyUseDropDownListener() {
        String[] property = getResources().getStringArray(R.array.property_use_array);
        String nonResarray[] = getResources().getStringArray(R.array.non_residential_array);
        // non residential use arrays
        String[] hotelArray = getResources().getStringArray(R.array.hotel_array);
        String[] shopsArray = getResources().getStringArray(R.array.shop_dhaba_array);
        String[] healthArray = getResources().getStringArray(R.array.health_array);
        String[] educationArray = getResources().getStringArray(R.array.educational_array);
        String[] industrialUnitArray = getResources().getStringArray(R.array.industrial_array);
        String[] nonResCatIDArray = getResources().getStringArray(R.array.non_res_category_array);
        int[] hotelCatIdArray = getResources().getIntArray(R.array.hotel_cat_id_array);
        int[] shopCatIdArray = getResources().getIntArray(R.array.shop_cat_id_array);
        int[] healthCatIdArray = getResources().getIntArray(R.array.health_cat_id_array);
        int[] educationCatIdArray = getResources().getIntArray(R.array.education_id_array);
        int[] industryCatIdArray = getResources().getIntArray(R.array.industrial_cat_array);
        propertyUseSPIN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    property_use = property[position];
                    if (property_use.equalsIgnoreCase("Non-Residential")) {
                        nonResLayout.setVisibility(View.VISIBLE);
                        setNonResidentialDropDown(nonResarray);
                    } else {
                        nonResLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nonResidentialPropertySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        nonResCatId = "1";
                        nonResidentialType = nonResarray[position];
                        Log.d("dslfka",nonResCatId);
                        nonResdentialPropertyUseArray = hotelArray;
                        nonResPropUseLayout.setVisibility(View.VISIBLE);
                        setNonResidentialUseDropDown(hotelArray);
                        break;
                    case 2:
                    case 3:
                    case 6:
                    case 9:
                    case 10:
                        nonResidentialType = nonResarray[position];
                        nonResidentialUse = "";
                        nonResPropUseLayout.setVisibility(View.GONE);
                        break;
                    case 4:
                        nonResCatId = "14";
                        nonResidentialType = nonResarray[position];
                        nonResdentialPropertyUseArray = shopsArray;
                        nonResPropUseLayout.setVisibility(View.VISIBLE);
                        setNonResidentialUseDropDown(shopsArray);
                        break;
                    case 5:
                        nonResCatId = "9";
                        nonResidentialType = nonResarray[position];
                        nonResdentialPropertyUseArray = healthArray;
                        nonResPropUseLayout.setVisibility(View.VISIBLE);
                        setNonResidentialUseDropDown(healthArray);
                        break;
                    case 7:
                        nonResCatId = "10";
                        nonResidentialType = nonResarray[position];
                        nonResdentialPropertyUseArray = educationArray;
                        nonResPropUseLayout.setVisibility(View.VISIBLE);
                        setNonResidentialUseDropDown(educationArray);
                        break;
                    case 8:
                        //nonResSubCatId = nonResCatIDArray[position];
                        nonResidentialType = nonResarray[position];
                        nonResPropUseLayout.setVisibility(View.GONE);
                        break;
                    case 11:
                        nonResCatId = "11";
                        nonResidentialType = nonResarray[position];
                        nonResdentialPropertyUseArray = industrialUnitArray;
                        nonResPropUseLayout.setVisibility(View.VISIBLE);
                        setNonResidentialUseDropDown(industrialUnitArray);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        noResidentialPropertyUseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Non Res Use", "Called " + nonResCatId);
                if (position != 0) {
                    Log.v("Non Res Use inside", "Called" + nonResCatId);
                    //nonResidentialUse = nonResdentialPropertyUseArray[position];
                    switch (nonResCatId) {
                        case "1":
                            nonResidentialUse = hotelArray[position];
                            nonResSubCatId = String.valueOf(hotelCatIdArray[position]);
                            Log.d("dsfddddddd",nonResSubCatId);
                            break;
                        case "11":
                            nonResidentialUse = industrialUnitArray[position];
                            nonResSubCatId = String.valueOf(industryCatIdArray[position]);
                            break;
                        case "9":
                            nonResidentialUse = healthArray[position];
                            nonResSubCatId = String.valueOf(healthCatIdArray[position]);
                            break;
                        case "10":
                            nonResidentialUse = educationArray[position];
                            nonResSubCatId = String.valueOf(educationCatIdArray[position]);
                            break;
                        case "14":

                            nonResidentialUse = shopsArray[position];
                            nonResSubCatId = String.valueOf(shopCatIdArray[position]);
                            break;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /*method for setting floor dropdown listener*/
    private void setFloorDropDownListeners() {
//        String fArray[] = getResources().getStringArray(R.array.floor_name_array);
        /*---------------------------Dynamic Floor Spinner Start--------------------------------------------*/
//        totalFloors=Integer.parseInt(floorCount.getText().toString().trim());

//        ArrayAdapter<String> itemsArrayList = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1);
//        fArray = getResources().getStringArray(R.array.floor_name_array);
//        for (int i = 0; i <= totalFloors; i++) {
//            itemsArrayList.add(fArray[i]);
//        }
//        floorNameSpinner.setAdapter(itemsArrayList);

        /*---------------------------Dynamic Floor Spinner End--------------------------------------------*/

        floorNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    florName = fArray[position];
//                    TinyDB tinyDB = new TinyDB(FloorDetailActivity.this);
//                    ArrayList<String> list = tinyDB.getListString(AppConstants.FLOOR_NAME);
////                    if (checkFloors()) {
////                        //florName = fArray[position];
////                        //list.add(florName);
////                        //tinyDB.putListString(AppConstants.FLOOR_NAME,list);
////                    } else {
////                        floorNameSpinner.setSelection(0);
////                        Toast.makeText(FloorDetailActivity.this, "You have already added total number of floors.", Toast.LENGTH_LONG).show();
////                    }
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String propertyTypeArray[] = getResources().getStringArray(R.array.property_type_array);
        // String nonResarray[] = getResources().getStringArray(R.array.non_residential_array);
        propertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        openPlotLayout.setVisibility(View.GONE);
                        usedForDefecationLayout.setVisibility(View.GONE);
                        ll_usedForgrabage.setVisibility(View.GONE);
                        // nonResLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        propertyType = propertyTypeArray[position];
                        openPlotLayout.setVisibility(View.VISIBLE);
                        // nonResLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        openPlotRadioGrp.clearCheck();
                        usedForDefecationGrp.clearCheck();
                        used_for_Grabagedumping_grp.clearCheck();
                        propertyType = propertyTypeArray[position];
                        openPlotLayout.setVisibility(View.GONE);
                        usedForDefecationLayout.setVisibility(View.GONE);
                        ll_usedForgrabage.setVisibility(View.GONE);
                        break;
                    case 3:
                        openPlotRadioGrp.clearCheck();
                        usedForDefecationGrp.clearCheck();
                        used_for_Grabagedumping_grp.clearCheck();
                        propertyType = propertyTypeArray[position];
                        openPlotLayout.setVisibility(View.GONE);
                        usedForDefecationLayout.setVisibility(View.GONE);
                        ll_usedForgrabage.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] ownershipArray = getResources().getStringArray(R.array.ownership_type_array);
        ownershipTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ownershipType = ownershipArray[position];
                    if (ownershipType.equalsIgnoreCase("Single Owner") || ownershipType.equalsIgnoreCase("Multiple Owners")) {
                        ll_institutionNameType.setVisibility(View.GONE);
                    } else {
                        ll_institutionNameType.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] subOwnershipArray = getResources().getStringArray(R.array.sub_ownership_type_array);
        subOwnershipTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    subOwnershipType = subOwnershipArray[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] instutionTypeArray = getResources().getStringArray(R.array.institution_type_array);
        instutionTpyeSPIN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    institutionType = instutionTypeArray[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] constructionTypeArray = getResources().getStringArray(R.array.floor_construction_type_array);
        floorConstructionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    floorConstructionType = constructionTypeArray[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*method for setting non residential drop down*/
    private void setNonResidentialDropDown(String[] array) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, array);
        nonResidentialPropertySpinner.setAdapter(arrayAdapter);
    }

    /*method for setting non residential use drop down*/
    private void setNonResidentialUseDropDown(String[] array) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, array);
        noResidentialPropertyUseSpinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.occupying_date_edit_txt:
                setDate();
                break;
            case R.id.add_floor_btn:
                validateFloorDetails();
                break;
        }

    }

    /*method for  setting date*/
    private void setDate() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        occupancyDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        ;
        datePickerDialog.show();
    }

    /*method for formatting Date*/
    private String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        String date = sdf.format(calendar.getTimeInMillis());
        return date;
    }

    /*method for validating floor values*/
    private void validateFloorDetails() {
        if (floorCount.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter no of floor first", Toast.LENGTH_LONG).show();
            floorNameSpinner.setEnabled(false);
        }
        else if (propertyType.equals("")) {
            Toast.makeText(this, "Please select property type", Toast.LENGTH_LONG).show();
        } else if (propertyType.equals("Vacant Land") && openPlot.equals("") && usedForDefecation.equals("")) {
            Toast.makeText(this, "Please select if open plot or not", Toast.LENGTH_LONG).show();
        } else if (property_use.equals("")){
            Toast.makeText(this,"Please select property use",Toast.LENGTH_LONG).show();
        }else if (ownershipType.equals("")) {
            Toast.makeText(this, "Please select ownership type", Toast.LENGTH_LONG).show();
        } else if (floorOccupier.equals("")) {
            Toast.makeText(this, "Please select floor occupier", Toast.LENGTH_LONG).show();
        } else if (floorOccupier.equals("Tenant") && occupancyDate.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter occupancy date", Toast.LENGTH_LONG).show();
        } else if (floorConstructionType.equals("")) {
            Toast.makeText(this, "Please enter floor construction type", Toast.LENGTH_LONG).show();
        } else if (built_up_value.equals("1")) {
            Toast.makeText(FloorDetailActivity.this, "You can't add super built up area value grater then to total plot area", Toast.LENGTH_LONG).show();

        } else {
            Floor floor = new Floor();
            floor.setFloorName(florName);
            //floor.setUid(uid);
            floor.setCoveredArea(floorArea.getText().toString());
            floor.setInstitution_type(institutionType);
            floor.setInstitution_name(floorInstitutionNameEDT.getText().toString());
            floor.setProperty_use(property_use);
            floor.setPropertyType(propertyType);
            if (propertyType.equals("Vacant Land")) {
                floor.setOpenPlot(openPlot);
                floor.setUsedForDefecation(usedForDefecation);
                floor.setUsedForGrabage(usedForGrabagedumping);
            }
            floor.setFloorPropertyId(uid);
            if (property_use.equals("Non-Residential")) {
                floor.setNonResidentialType(nonResidentialType);
                floor.setNonResidentialUse(nonResidentialUse);
                floor.setNonResCatId(nonResCatId);
                floor.setNonResSubCatId(nonResSubCatId);
            }
            floor.setStatus("NEW");
            floor.setOwnershipType(ownershipType);
            floor.setSubOwnershipCategory(subOwnershipType);
            floor.setFloorOccupier(floorOccupier);
            if (floorOccupier.equals("Unoccupied")) {
                floor.setOccupancyDate(occupancyDate.getText().toString());
            }
            floor.setFloorConstructionType(floorConstructionType);
            floor.setConstructionYear(floorConstructionYear.getText().toString());
            floor.setConstructionRate(floorRateOfConstruction.getText().toString());
            floor.setCreatedDate(getFormattedDate());

            TinyDB tinyDB = new TinyDB(this);
            ArrayList<String> list = tinyDB.getListString(AppConstants.FLOOR_NAME);

            saveFloorName();


            //if (insertedFloors<totalFloors){
            propertyViewModel.init();
            propertyViewModel.insertFloorRecord(floor);
            //insertedFloors++;
            ProgressDialog progressDialog = ProgressDialog.show(this, "", "Please wait..", false);
            propertyViewModel.getIsLoading().observe(this, aBoolean -> {
                if (aBoolean) {
                    progressDialog.show();
                } else {
                    Toast.makeText(FloorDetailActivity.this, "Floor Record is saved successfully", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    clearControls();
                }
            });

        }

    }

    /*method for clearing controls*/
    private void clearControls() {
        floorNameSpinner.setSelection(0);
        florName = "";
        floorArea.setText("");
        propertyTypeSpinner.setSelection(0);
        propertyType = "";
        openPlot = "";
        openPlotRadioGrp.clearCheck();
        usedForDefecation = "";
        usedForGrabagedumping = "";
        usedForDefecationGrp.clearCheck();
        used_for_Grabagedumping_grp.clearCheck();
        nonResidentialType = "";
        nonResidentialPropertySpinner.setSelection(0);
        nonResidentialUse = "";
        noResidentialPropertyUseSpinner.setSelection(0);
        ownershipType = "";
        ownershipTypeSpinner.setSelection(0);
        subOwnershipType = "";
        subOwnershipTypeSpinner.setSelection(0);
        floorOccupier = "";
        floorOccupierRadioGrp.clearCheck();
        occupancyDate.setText("");
        floorConstructionYear.setText("");
        floorRateOfConstruction.setText("");
    }

    /*method for checking if a vacant property type is saved already*/
    private boolean isVacantAlreadyAdded() {
        boolean isVacantAvailable = false;
        if (floorList != null && floorList.size() > 0) {
            for (Floor floor : floorList) {
                if (floor.getPropertyType().equals("Vacant Land")) {
                    isVacantAvailable = true;
                    break;
                }
            }
        }
        return isVacantAvailable;
    }

    // method for checking total no of floors
    private boolean checkFloors() {
        boolean isValid = false;
        TinyDB tinyDB = new TinyDB(this);
        ArrayList<String> list = tinyDB.getListString(AppConstants.FLOOR_NAME);
        if (list != null && !list.contains(florName) && list.size() < totalFloors) {

            isValid = true;
        } else if (list != null && list.contains(florName) && list.size() <= totalFloors) {
            isValid = true;
        }


        return isValid;
    }

    // method for savinf floor name in list
    private void saveFloorName() {
        TinyDB tinyDB = new TinyDB(this);
        ArrayList<String> list = tinyDB.getListString(AppConstants.FLOOR_NAME);
        if (list != null && !list.contains(florName) && list.size() < totalFloors) {
            list.add(florName);
        } else if (list == null) {
            list = new ArrayList<>();
            list.add(florName);
        }

        tinyDB.putListString(AppConstants.FLOOR_NAME, list);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//        totalFloors=5;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().isEmpty()){
            Toast.makeText(this, "Please Enter no if floor first", Toast.LENGTH_SHORT).show();
            floorNameSpinner.setEnabled(false);
        }
//            totalFloors=Integer.parseInt(charSequence.toString().trim() )    ;
//        totalFloors=3;

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString().equals("")){
//            totalFloors=3;
//            itemsArrayList = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1);
////            fArray = getResources().getStringArray(R.array.floor_name_array);
//            for (int i = 0; i < totalFloors; i++) {
////                itemsArrayList.add(fArray[i]);
//                if (i==0){
//                    itemsArrayList.add("Select");
//                    continue;
//                }
//                itemsArrayList.add(i+"F");
//            }
//
//            floorNameSpinner.setAdapter(itemsArrayList);
            floorNameSpinner.setEnabled(false);

        }else{
            totalFloors=Integer.parseInt(editable.toString().trim() );

             itemsArrayList = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1);
//            fArray = getResources().getStringArray(R.array.floor_name_array);
            for (int i = 0; i < totalFloors; i++) {
                if (i==0){
                    itemsArrayList.add("GF");
                    continue;
                }
                itemsArrayList.add(i+"F");
            }
            floorNameSpinner.setAdapter(itemsArrayList);
            floorNameSpinner.setEnabled(true);
        }

    }
}