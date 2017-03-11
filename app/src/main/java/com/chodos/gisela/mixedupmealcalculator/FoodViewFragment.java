package com.chodos.gisela.mixedupmealcalculator;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FoodViewFragment extends Fragment {
    protected MenuItem menuItem_edit;
    protected MenuItem menuItem_delete;
    public static final String TAG = "debugView";
    protected SharedPreferences mainPrefs;
    protected SharedPreferences.Editor editor;
    protected TextView txv_servingUnitFoodView;
    FoodItem foodItemToView;
    EditText et_itemFoodView, et_brandFoodView, et_servingWeightFoodView, et_caloriesFoodView, et_totalFatFoodView, et_satFatFoodView, et_transFatFoodView,
            et_totalCarbsFoodView, et_fiberCarbsFoodView, et_sugarsCarbsFoodView, et_cholesterolFoodView, et_sodiumFoodView, et_proteinFoodView;

    public FoodViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_food_view, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Food View");
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();
        instantiate_EditTexts(rootView);
        txv_servingUnitFoodView = (TextView) rootView.findViewById(R.id.txv_serving_unit_food_view);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        FoodItem theFoodItem = ((MainActivity) getActivity()).getFoodItemToView();
        if (theFoodItem != null) {
            foodItemToView = theFoodItem;
            fillIn_FoodViewFields(foodItemToView);
        }
    }


    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_food_view_edit, menu);
        menuItem_edit = menu.getItem(1);
        menuItem_delete = menu.getItem(2);

        if (mainPrefs.getBoolean(MainActivity.FROM_NUTRX, false)) {
            menuItem_delete.setEnabled(false);
            editor.putBoolean(MainActivity.FROM_NUTRX, false).apply();  //BUT WHAT IF THE USER CLICKS THE BACK BUTTON IN FOOD_EDIT???  (solved by popping backstack)
        }
        if (mainPrefs.getBoolean(MainActivity.FROM_SQLITE, false)) {
            menuItem_edit.setEnabled(false);
            editor.putBoolean(MainActivity.FROM_SQLITE, false).apply();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                ((MainActivity) getActivity()).show_FoodEditFragment(foodItemToView);
                return true;
            case R.id.menu_item_save:
                Cursor cursor = MainActivity.databaseHelper.getFoodItem(et_itemFoodView.getText().toString().trim(), et_brandFoodView.getText().toString().trim());
                if (cursor.getCount() > 0) {
                    Toast toast = Toast.makeText(getContext(), "This food item is already saved.", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }

                double servingWeightGrams = getServingWeightGrams();//methods does conversions on value in EditText serving amount
                if (servingWeightGrams == -1) { //unit of measurement not g, oz, ml, or fl oz
                    Toast toast = Toast.makeText(getContext(), "Not an accepted unit of measurement.\nPlease weigh one serving " +
                            "\n in g, oz, mL, or fl oz," +
                            "\nand edit the food item record.",
                            Toast.LENGTH_LONG);
                    toast.show();
                    return true;
                }
                try {
                    insertFoodItem(servingWeightGrams);
                } catch (Exception e) {
                    //log.d(TAG, "onOptionsItemSelected: " + e.getMessage());
                    return true;
                }
                ((MainActivity)getActivity()).clear_BackStack(); //let's not go back after saving
                MainActivity.hideKeyboard(getContext());
                ((MainActivity) getActivity()).show_FoodListFragment(false);
                return true;
            case R.id.menu_item_delete:
                ((MainActivity)getActivity()).show_ConfirmDeleteFoodDialog(et_itemFoodView.getText().toString());

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private double getServingWeightGrams() {
        double servingWeightGrams = 0;
        switch (txv_servingUnitFoodView.getText().toString()) {
            case ("g"):
            case ("mL")://treating 1 mL as 1 gram
                if (!et_servingWeightFoodView.getText().toString().isEmpty())
                    servingWeightGrams = Double.parseDouble(et_servingWeightFoodView.getText().toString());
                else
                    servingWeightGrams = 0;
                break;
            case ("oz"):
                if (!et_servingWeightFoodView.getText().toString().isEmpty())
                    servingWeightGrams = 28.35 * Double.parseDouble(et_servingWeightFoodView.getText().toString());
                else
                    servingWeightGrams = 0;
                break;
            case ("fl oz"):
                if (!et_servingWeightFoodView.getText().toString().isEmpty())
                    servingWeightGrams = 29.57 * Double.parseDouble(et_servingWeightFoodView.getText().toString());
                else
                    servingWeightGrams = 0;
                break;
            default:
                servingWeightGrams = -1;
        }
        return servingWeightGrams;
    }

    private void insertFoodItem(double servingWeightGrams) throws Exception {
        MainActivity.databaseHelper.insertFoodItem(
                et_itemFoodView.getText().toString().trim(), et_brandFoodView.getText().toString().trim(),
                et_caloriesFoodView.getText().toString().isEmpty() ? "0" : et_caloriesFoodView.getText().toString(), "0"/*calFat*/,
                et_totalFatFoodView.getText().toString().isEmpty() ? "0" : et_totalFatFoodView.getText().toString(),
                et_satFatFoodView.getText().toString().isEmpty() ? "0" : et_satFatFoodView.getText().toString(),
                et_transFatFoodView.getText().toString().isEmpty() ? "0" : et_transFatFoodView.getText().toString(),
                et_cholesterolFoodView.getText().toString().isEmpty() ? "0" : et_cholesterolFoodView.getText().toString(),
                et_sodiumFoodView.getText().toString().isEmpty() ? "0" : et_sodiumFoodView.getText().toString(),
                et_totalCarbsFoodView.getText().toString().isEmpty() ? "0" : et_totalCarbsFoodView.getText().toString(),
                et_fiberCarbsFoodView.getText().toString().isEmpty() ? "0" : et_fiberCarbsFoodView.getText().toString(),
                et_sugarsCarbsFoodView.getText().toString().isEmpty() ? "0" : et_sugarsCarbsFoodView.getText().toString(),
                et_proteinFoodView.getText().toString().isEmpty() ? "0" : et_proteinFoodView.getText().toString(),
                et_servingWeightFoodView.getText().toString().isEmpty() ? "0" : et_servingWeightFoodView.getText().toString(),
                txv_servingUnitFoodView.getText().toString(),
                String.valueOf(servingWeightGrams));
    }

    private void updateFoodItem(int theIndex, double servingWeightGrams) throws Exception {
        MainActivity.databaseHelper.updateFoodItem(
                theIndex,
                et_itemFoodView.getText().toString().trim(), et_brandFoodView.getText().toString().trim(),
                et_caloriesFoodView.getText().toString().isEmpty() ? "0" : et_caloriesFoodView.getText().toString(), "0"/*calFat*/,
                et_totalFatFoodView.getText().toString().isEmpty() ? "0" : et_totalFatFoodView.getText().toString(),
                et_satFatFoodView.getText().toString().isEmpty() ? "0" : et_satFatFoodView.getText().toString(),
                et_transFatFoodView.getText().toString().isEmpty() ? "0" : et_transFatFoodView.getText().toString(),
                et_cholesterolFoodView.getText().toString().isEmpty() ? "0" : et_cholesterolFoodView.getText().toString(),
                et_sodiumFoodView.getText().toString().isEmpty() ? "0" : et_sodiumFoodView.getText().toString(),
                et_totalCarbsFoodView.getText().toString().isEmpty() ? "0" : et_totalCarbsFoodView.getText().toString(),
                et_fiberCarbsFoodView.getText().toString().isEmpty() ? "0" : et_fiberCarbsFoodView.getText().toString(),
                et_sugarsCarbsFoodView.getText().toString().isEmpty() ? "0" : et_sugarsCarbsFoodView.getText().toString(),
                et_proteinFoodView.getText().toString().isEmpty() ? "0" : et_proteinFoodView.getText().toString(),
                et_servingWeightFoodView.getText().toString().isEmpty() ? "0" : et_servingWeightFoodView.getText().toString(),
                txv_servingUnitFoodView.getText().toString(),
                String.valueOf(servingWeightGrams));
    }


    protected void instantiate_EditTexts(View rootView) {
        et_itemFoodView = (EditText) rootView.findViewById(R.id.et_item_food_view);
        et_brandFoodView = (EditText) rootView.findViewById(R.id.et_brand_food_view);
        et_servingWeightFoodView = (EditText) rootView.findViewById(R.id.et_serving_weight_food_view);
        et_caloriesFoodView = (EditText) rootView.findViewById(R.id.et_calories_food_view);
        et_totalFatFoodView = (EditText) rootView.findViewById(R.id.et_total_fat_food_view);
        et_satFatFoodView = (EditText) rootView.findViewById(R.id.et_sat_fat_food_view);
        et_transFatFoodView = (EditText) rootView.findViewById(R.id.et_trans_fat_food_view);
        et_totalCarbsFoodView = (EditText) rootView.findViewById(R.id.et_total_carbs_food_view);
        et_fiberCarbsFoodView = (EditText) rootView.findViewById(R.id.et_fiber_carbs_food_view);
        et_sugarsCarbsFoodView = (EditText) rootView.findViewById(R.id.et_sugars_carbs_food_view);
        et_cholesterolFoodView = (EditText) rootView.findViewById(R.id.et_cholesterol_food_view);
        et_sodiumFoodView = (EditText) rootView.findViewById(R.id.et_sodium_food_view);
        et_proteinFoodView = (EditText) rootView.findViewById(R.id.et_protein_food_view);
    }

    protected void fillIn_FoodViewFields(FoodItem foodItemToView) {
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

        et_itemFoodView.setText(itemName);
        et_brandFoodView.setText(brandName);

        if (servingWeight.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
            et_servingWeightFoodView.setText(servingWeight.endsWith(".0") ? servingWeight.substring(0, servingWeight.indexOf(".")) : servingWeight);
        } else {
            et_servingWeightFoodView.setText(qty.endsWith(".0") ? qty.substring(0, qty.indexOf(".")) : qty);
        }

        txv_servingUnitFoodView.setText(unit);
        et_caloriesFoodView.setText(calories.endsWith(".0") ? calories.substring(0, calories.indexOf(".")) : calories);
        et_totalFatFoodView.setText(totalFat.endsWith(".0") ? totalFat.substring(0, totalFat.indexOf(".")) : totalFat);
        et_satFatFoodView.setText(satFat.endsWith(".0") ? satFat.substring(0, satFat.indexOf(".")) : satFat);
        et_transFatFoodView.setText(transFat.endsWith(".0") ? transFat.substring(0, transFat.indexOf(".")) : transFat);
        et_totalCarbsFoodView.setText(totalCarbs.endsWith(".0") ? totalCarbs.substring(0, totalCarbs.indexOf(".")) : totalCarbs);
        et_fiberCarbsFoodView.setText(fiber.endsWith(".0") ? fiber.substring(0, fiber.indexOf(".")) : fiber);
        et_sugarsCarbsFoodView.setText(sugars.endsWith(".0") ? sugars.substring(0, sugars.indexOf(".")) : sugars);
        et_cholesterolFoodView.setText(cholesterol.endsWith(".0") ? cholesterol.substring(0, cholesterol.indexOf(".")) : cholesterol);
        et_sodiumFoodView.setText(sodium.endsWith(".0") ? sodium.substring(0, sodium.indexOf(".")) : sodium);
        et_proteinFoodView.setText(protein.endsWith(".0") ? protein.substring(0, protein.indexOf(".")) : protein);
    }
}




