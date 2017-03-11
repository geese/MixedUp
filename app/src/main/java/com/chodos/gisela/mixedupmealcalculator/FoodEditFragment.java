package com.chodos.gisela.mixedupmealcalculator;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


public class FoodEditFragment extends Fragment {
    public static final String TAG = "debugEdit";
    protected SharedPreferences mainPrefs;
    protected SharedPreferences.Editor editor;
    FoodItem foodItemToEdit;
    Spinner spn_servingUnit;
    ArrayList<String> spinnerItems;
    ArrayAdapter<String> servingUnitsAdapter;
    EditText et_item, et_brand, et_servingWeight, et_calories, et_totalFat, et_satFat, et_transFat,
            et_totalCarbs, et_fiberCarbs, et_sugarsCarbs, et_cholesterol, et_sodium, et_protein;

    public FoodEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_food_edit, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();
        instantiate_EditTexts(rootView);
        spinnerItems = instantiate_spinnerList();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Food Edit");
        spn_servingUnit = (Spinner)rootView.findViewById(R.id.spn_serving_unit_food_view);
        spn_servingUnit.setEnabled(true);
        servingUnitsAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinnerItems);
        spn_servingUnit.setAdapter(servingUnitsAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //the fields will either be blank (Custom New Food) or loaded with info from Nutritionix database (can be edited and saved in edited form)
        if (((MainActivity)getActivity()).foodItemToView != null) {
            foodItemToEdit = ((MainActivity) getActivity()).foodItemToView;

            fillIn_Fields(foodItemToEdit);
        }

    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_food_view_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//the only option is "SAVE"
        double servingWeightGrams = 0;
        //do some conversions, http://www.asian-recipe.com/methods/convert-measurements.html
        servingWeightGrams = getServingWeightGrams(servingWeightGrams);
        int index = mainPrefs.getInt(MainActivity.INDEX_OF_FOOD_ITEM_TO_EDIT, -1);//need the index to know if inserting or updating
        if (index == -1) {  //the food isn't yet saved in the SQLite database and we're inserting
            try {
                insertFoodItem(servingWeightGrams);
            } catch (Exception e) {
                //log.d(TAG, "onOptionsItemSelected: " + e.getMessage());
                return true;
            }
        }else{
            try {
                editor.putInt(MainActivity.INDEX_OF_FOOD_ITEM_TO_EDIT, -1).apply();
                updateFoodItem(index, servingWeightGrams);  //updating the food item, accessing by foodItem index
            } catch (Exception e) {
                //log.d(TAG, "onOptionsItemSelected: " + e.getMessage());
                return true;
            }
        }
        MainActivity.hideKeyboard(getContext());
        ((MainActivity)getActivity()).show_FoodListFragment(false);
        return true;
    }

    private void insertFoodItem(double servingWeightGrams) throws Exception {
        MainActivity.databaseHelper.insertFoodItem(
                et_item.getText().toString().trim(), et_brand.getText().toString().trim(),
                et_calories.getText().toString().isEmpty() ? "0" : et_calories.getText().toString(), "0"/*calFat*/,
                et_totalFat.getText().toString().isEmpty() ? "0" : et_totalFat.getText().toString(),
                et_satFat.getText().toString().isEmpty() ? "0" : et_satFat.getText().toString(),
                et_transFat.getText().toString().isEmpty() ? "0" : et_transFat.getText().toString(),
                et_cholesterol.getText().toString().isEmpty() ? "0" : et_cholesterol.getText().toString(),
                et_sodium.getText().toString().isEmpty() ? "0" : et_sodium.getText().toString(),
                et_totalCarbs.getText().toString().isEmpty() ? "0" : et_totalCarbs.getText().toString(),
                et_fiberCarbs.getText().toString().isEmpty() ? "0" : et_fiberCarbs.getText().toString(),
                et_sugarsCarbs.getText().toString().isEmpty() ? "0" : et_sugarsCarbs.getText().toString(),
                et_protein.getText().toString().isEmpty() ? "0" : et_protein.getText().toString(),
                et_servingWeight.getText().toString().isEmpty() ? "0" : et_servingWeight.getText().toString(),
                spn_servingUnit.getSelectedItem().toString(),
                String.valueOf(servingWeightGrams));
    }

    private void updateFoodItem(int theIndex, double servingWeightGrams) throws Exception {
        MainActivity.databaseHelper.updateFoodItem(
                theIndex,
                et_item.getText().toString().trim(), et_brand.getText().toString().trim(),
                et_calories.getText().toString().isEmpty() ? "0" : et_calories.getText().toString(), "0"/*calFat*/,
                et_totalFat.getText().toString().isEmpty() ? "0" : et_totalFat.getText().toString(),
                et_satFat.getText().toString().isEmpty() ? "0" : et_satFat.getText().toString(),
                et_transFat.getText().toString().isEmpty() ? "0" : et_transFat.getText().toString(),
                et_cholesterol.getText().toString().isEmpty() ? "0" : et_cholesterol.getText().toString(),
                et_sodium.getText().toString().isEmpty() ? "0" : et_sodium.getText().toString(),
                et_totalCarbs.getText().toString().isEmpty() ? "0" : et_totalCarbs.getText().toString(),
                et_fiberCarbs.getText().toString().isEmpty() ? "0" : et_fiberCarbs.getText().toString(),
                et_sugarsCarbs.getText().toString().isEmpty() ? "0" : et_sugarsCarbs.getText().toString(),
                et_protein.getText().toString().isEmpty() ? "0" : et_protein.getText().toString(),
                et_servingWeight.getText().toString().isEmpty() ? "0" : et_servingWeight.getText().toString(),
                spn_servingUnit.getSelectedItem().toString(),
                String.valueOf(servingWeightGrams));
    }

    private double getServingWeightGrams(double servingWeightGrams) {
        switch(spn_servingUnit.getSelectedItem().toString()){
            case ("g"):
            case ("mL")://treating 1 mL as 1 gram
                if (!et_servingWeight.getText().toString().isEmpty())
                    servingWeightGrams = Double.parseDouble(et_servingWeight.getText().toString());
                else
                    servingWeightGrams = 0;
                break;
            case ("oz"):
                if (!et_servingWeight.getText().toString().isEmpty())
                    servingWeightGrams = 28.35 * Double.parseDouble(et_servingWeight.getText().toString());
                else
                    servingWeightGrams = 0;
                break;
            case ("fl oz"):
                if (!et_servingWeight.getText().toString().isEmpty())
                    servingWeightGrams = 29.57 * Double.parseDouble(et_servingWeight.getText().toString());
                else
                    servingWeightGrams = 0;
                break;
        }
        return servingWeightGrams;
    }

    protected void instantiate_EditTexts(View rootView){
        et_item = (EditText)rootView.findViewById(R.id.et_item_food_view);
        et_brand = (EditText)rootView.findViewById(R.id.et_brand_food_view);
        et_servingWeight = (EditText)rootView.findViewById(R.id.et_serving_weight_food_view);
        et_servingWeight.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_calories = (EditText)rootView.findViewById(R.id.et_calories_food_view);
        et_calories.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_totalFat = (EditText)rootView.findViewById(R.id.et_total_fat_food_view);
        et_totalFat.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_satFat = (EditText)rootView.findViewById(R.id.et_sat_fat_food_view);
        et_satFat.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_transFat = (EditText)rootView.findViewById(R.id.et_trans_fat_food_view);
        et_transFat.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_totalCarbs = (EditText)rootView.findViewById(R.id.et_total_carbs_food_view);
        et_totalCarbs.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_fiberCarbs = (EditText)rootView.findViewById(R.id.et_fiber_carbs_food_view);
        et_fiberCarbs.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_sugarsCarbs = (EditText)rootView.findViewById(R.id.et_sugars_carbs_food_view);
        et_sugarsCarbs.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_cholesterol = (EditText)rootView.findViewById(R.id.et_cholesterol_food_view);
        et_cholesterol.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_sodium = (EditText)rootView.findViewById(R.id.et_sodium_food_view);
        et_sodium.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_protein = (EditText)rootView.findViewById(R.id.et_protein_food_view);
        et_protein.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    protected ArrayList<String> instantiate_spinnerList(){
        ArrayList<String> theSpinnerList = new ArrayList<>();
        theSpinnerList.add("g");
        theSpinnerList.add("oz");
        theSpinnerList.add("mL");
        theSpinnerList.add("fl oz");
        return theSpinnerList;
    }





    protected void fillIn_Fields(FoodItem foodItemToView){
        String itemName = foodItemToView.item_name;
        String brandName = foodItemToView.brand_name;
        String servingWeight = String.format("%.1f", foodItemToView.nf_serving_weight_grams);
        //log.d(TAG, "Serving Weight: " + servingWeight);
        String calories = String.format("%.1f", foodItemToView.nf_calories);
        String totalFat = String.format("%.1f", foodItemToView.nf_total_fat);
        String satFat = String.format("%.1f", foodItemToView.nf_saturated_fat);
        String transFat = String.format("%.1f", foodItemToView.nf_trans_fatty_acid);
        String totalCarbs = String.format("%.1f", foodItemToView.nf_total_carbohydrate);
        String fiber = String.format("%.1f", foodItemToView.nf_dietary_fiber);
        String sugars = String.format("%.1f", foodItemToView.nf_sugars);
        String cholesterol = String.format("%.1f", foodItemToView.nf_cholesterol);
        String sodium = String.format("%.1f", foodItemToView.nf_sodium);
        String protein = String.format("%.1f", foodItemToView.nf_protein);
        String qty = String.format("%.1f", foodItemToView.nf_serving_size_qty);
        String unit = servingWeight.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? "g" : foodItemToView.nf_serving_size_unit;


        calories = calories.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? calories : "0";
        totalFat = totalFat.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? totalFat : "0";
        satFat = satFat.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? satFat : "0";
        transFat = transFat.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? transFat : "0";
        totalCarbs = totalCarbs.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? totalCarbs : "0";
        fiber = fiber.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? fiber : "0";
        sugars = sugars.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? sugars : "0";
        cholesterol = cholesterol.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? cholesterol : "0";
        sodium = sodium.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? sodium : "0";
        protein = transFat.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+") ? protein : "0";


        //regex from http://www.coderanch.com/t/401142/java/java/check-String-numeric

        et_item.setText(itemName);
        et_brand.setText(brandName);

        if (servingWeight.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
            et_servingWeight.setText(servingWeight.endsWith(".0") ? servingWeight.substring(0, servingWeight.indexOf(".")) : servingWeight);
        }else{
            et_servingWeight.setText(qty.endsWith(".0") ? qty.substring(0, qty.indexOf(".")) : qty);
        }

        final int unitIndex = spinnerItems.indexOf(unit);
        spn_servingUnit.post(new Runnable() {  //???
            @Override
            public void run() {
                spn_servingUnit.setSelection(unitIndex);
            }
        });//http://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically
        et_calories.setText(calories.endsWith(".0") ? calories.substring(0, calories.indexOf(".")) : calories);
        et_totalFat.setText(totalFat.endsWith(".0") ? totalFat.substring(0, totalFat.indexOf(".")) : totalFat);
        et_satFat.setText(satFat.endsWith(".0") ? satFat.substring(0, satFat.indexOf(".")) : satFat);
        et_transFat.setText(transFat.endsWith(".0") ? transFat.substring(0, transFat.indexOf(".")) : transFat);
        et_totalCarbs.setText(totalCarbs.endsWith(".0") ? totalCarbs.substring(0, totalCarbs.indexOf(".")) : totalCarbs);
        et_fiberCarbs.setText(fiber.endsWith(".0") ? fiber.substring(0, fiber.indexOf(".")) : fiber);
        et_sugarsCarbs.setText(sugars.endsWith(".0") ? sugars.substring(0, sugars.indexOf(".")) : sugars);
        et_cholesterol.setText(cholesterol.endsWith(".0") ? cholesterol.substring(0, cholesterol.indexOf(".")) : cholesterol);
        et_sodium.setText(sodium.endsWith(".0") ? sodium.substring(0, sodium.indexOf(".")) : sodium);
        et_protein.setText(protein.endsWith(".0") ? protein.substring(0, protein.indexOf(".")) : protein);
    }
}








//https://androidcookbook.com/Recipe.seam?recipeId=4012
//http://android--code.blogspot.com/2015/08/android-spinner-text-size.html
//http://stackoverflow.com/questions/11188398/how-to-change-the-spinner-background-design-and-color-for-android


//spn_servingUnits = (Spinner) rootView.findViewById(R.id.spn_serving_unit_food_view);
//spn_servingUnits.setEnabled(false);

// Create an ArrayAdapter using the string array and a default spinner layout
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.serving_units_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner

        spn_servingUnits.setAdapter(adapter);*/
       /* spn_servingUnits.post(new Runnable() {
            @Override
            public void run() {
                spn_servingUnits.setSelection(1);
            }
        });//http://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically*/

//Log.d(TAG, "viewsHolder.getChildCount(): " + viewsHolder.getChildCount());