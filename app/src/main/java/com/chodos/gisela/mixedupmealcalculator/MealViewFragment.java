package com.chodos.gisela.mixedupmealcalculator;

//Dynamically adding Views:
//http://android-er.blogspot.com/2013/05/add-and-remove-view-dynamically.html

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;


public class MealViewFragment extends Fragment {

    Bundle bundle;
    public static final String VIEW_TAG = "LinearLayout_";
    public static final String SPINNER_TAG = "Spinner_";
    public static final String MEAL_NAME_KEY = "meal_name";
    public static final String INGR_STRING_KEY = "ingredients_string";
    public static final String EDIT_TEXT_KEY = "et_text";
    public static final String SPINNER_KEY = "spinner";
    public static final String LIST_SIZE_KEY = "number of selected foods";
    public static final String TAG = "debugMealView";
    SharedPreferences mainPrefs;
    SharedPreferences.Editor editor;
    EditText et_mealName;
    ImageButton imgBtn_addFoodItem;
    LinearLayout theContainer;
    ArrayList<String> selectedFoodsList;
    ArrayList<FoodItem> selectedFoodItems;
    ArrayList<String> savedServingAmounts;
    ArrayList<Integer> savedUnitSpinnerSelectedPositions;
    ArrayList<String> ingredientStrings;
    String servingInfoString;  //string of servingAmounts and servingUnits delineated by "?"
    String[] savedServingInfo;
    String mealName;

    public MealViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
        editor.putString(MEAL_NAME_KEY, et_mealName.getText().toString()).apply();

        String servingWt;
        int servingUt;
        for (int i = 0; i < selectedFoodsList.size(); i++) {
            /*if (getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.MEAL_INFO_FRAG_TAG) == null
                    && getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.FOOD_LIST_FRAG_TAG) == null) {*/
            View theRow = getView().findViewWithTag(VIEW_TAG + String.valueOf(i));
                if (theRow != null) {
                    String itemName = (((TextView)theRow.findViewById(R.id.item_name_meal_row)).getText().toString().trim());
                    String brandName = (((TextView)theRow.findViewById(R.id.brand_name_meal_row)).getText().toString().trim());
                    String savedValues =
                            ((EditText) theRow.findViewById(R.id.et_amount_weight_meal_row)).getText().toString()
                            + "?"
                            + String.valueOf(((Spinner) theRow.findViewById(R.id.spn_unit_meal_row)).getSelectedItemPosition());

                    editor.putString("SAVED_VALUES" + itemName + brandName, savedValues).apply();
                    //editor.putString(EDIT_TEXT_KEY + String.valueOf(i), ((EditText) getView().findViewWithTag(VIEW_TAG + String.valueOf(i)).findViewById(R.id.et_amount_weight_meal_row)).getText().toString()).apply();
                    //editor.putInt(SPINNER_KEY + String.valueOf(i), ((Spinner) getView().findViewWithTag(VIEW_TAG + String.valueOf(i)).findViewById(R.id.spn_unit_meal_row)).getSelectedItemPosition()).apply();
                }
            //}
        }
        editor.putInt(LIST_SIZE_KEY, selectedFoodsList.size()).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_meal_view, container, false);
        //log.d(TAG, "onCreateView: ");
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();
        bundle = getArguments();

        //will be dynamically adding "rows" to this LinearLayout.
        theContainer = (LinearLayout) rootView.findViewById(R.id.container);


        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Meal Ingredients");

        imgBtn_addFoodItem = (ImageButton)rootView.findViewById(R.id.img_btn_add_food_item);
        imgBtn_addFoodItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).hideKeyboard(getContext());
                ((MainActivity)getActivity()).show_FoodListFragment(get_MealInfo("", "", "", ""));//Probably passing strings in is stupid.
            }
        });
        savedServingAmounts = new ArrayList<>();
        savedUnitSpinnerSelectedPositions = new ArrayList<>();

        et_mealName = (EditText)rootView.findViewById(R.id.meal_name);
        mealName = "";
        servingInfoString = "";
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*String servingWt;
        int servingUt;
        for (int i = 0; i < selectedFoodsList.size(); i++) {
            if (getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.MEAL_INFO_FRAG_TAG) == null
                    && getActivity().getSupportFragmentManager().findFragmentByTag(MainActivity.FOOD_LIST_FRAG_TAG) == null) {
                if (getView().findViewWithTag(VIEW_TAG + String.valueOf(i)) != null) {
                    outState.putString(EDIT_TEXT_KEY + String.valueOf(i), ((EditText) getView().findViewWithTag(VIEW_TAG + String.valueOf(i)).findViewById(R.id.et_amount_weight_meal_row)).getText().toString());
                    outState.putInt(SPINNER_KEY + String.valueOf(i), ((Spinner) getView().findViewWithTag(VIEW_TAG + String.valueOf(i)).findViewById(R.id.spn_unit_meal_row)).getSelectedItemPosition());
                }
            }
        }
        outState.putInt(LIST_SIZE_KEY, selectedFoodsList.size());*/
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            /*if (savedInstanceState.containsKey(LIST_SIZE_KEY)) {
                int numFoods = savedInstanceState.getInt(LIST_SIZE_KEY);
                for (int i = 0; i < numFoods; i++) {
                    if (savedInstanceState.containsKey(EDIT_TEXT_KEY + String.valueOf(i)))
                        savedServingAmounts.add(savedInstanceState.getString(EDIT_TEXT_KEY + String.valueOf(i)));
                    if (savedInstanceState.containsKey(SPINNER_KEY + String.valueOf(i)))
                        savedUnitSpinnerSelectedPositions.add(savedInstanceState.getInt(SPINNER_KEY + String.valueOf(i)));
                }
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundle != null)
            selectedFoodsList = bundle.getStringArrayList("SELECTED_FOODS_LIST");
        //this.selectedFoodsList = ((MainActivity) getActivity()).getSelectedFoodsList();

            if (mainPrefs.contains(MealListFragment.MEAL_NAME_KEY)) {
                et_mealName.setText(mainPrefs.getString(MealListFragment.MEAL_NAME_KEY, ""));
                editor.remove(MealListFragment.MEAL_NAME_KEY).apply();
            }

            if (mainPrefs.contains(MEAL_NAME_KEY)) {
                et_mealName.setText(mainPrefs.getString(MEAL_NAME_KEY, ""));
                editor.remove(MEAL_NAME_KEY).apply();
            }

        if (mainPrefs.contains(LIST_SIZE_KEY)) {
            int numFoods = mainPrefs.getInt(LIST_SIZE_KEY, 0);
            for (int i = 0; i < numFoods; i++) {
                if (mainPrefs.contains(EDIT_TEXT_KEY + String.valueOf(i))) {
                    savedServingAmounts.add(mainPrefs.getString(EDIT_TEXT_KEY + String.valueOf(i), null));
                    editor.remove(EDIT_TEXT_KEY + String.valueOf(i)).apply();
                }
                if (mainPrefs.contains(SPINNER_KEY + String.valueOf(i))) {
                    savedUnitSpinnerSelectedPositions.add(mainPrefs.getInt(SPINNER_KEY + String.valueOf(i), 0));
                    editor.remove(SPINNER_KEY + String.valueOf(i)).apply();
                }
            }
        }


        if (mainPrefs.contains(MealListFragment.SERVING_INFO))
        {
            servingInfoString = mainPrefs.getString(MealListFragment.SERVING_INFO, "0?0?");
            savedServingInfo = servingInfoString.split("\\?");
            editor.remove(MealListFragment.SERVING_INFO).apply();
        }

        if (selectedFoodsList != null) {
            selectedFoodItems = populate_selectedFoodItems(selectedFoodsList);
            for (int i = 0; i < selectedFoodsList.size(); i++) {
                makeRow(i);
                LinearLayout theRow = (LinearLayout) getView().findViewWithTag(VIEW_TAG + String.valueOf(i));
                ((TextView) theRow.findViewById(R.id.item_name_meal_row)).setText(selectedFoodItems.get(i).item_name);
                ((TextView) theRow.findViewById(R.id.brand_name_meal_row)).setText(selectedFoodItems.get(i).brand_name);


                    if (mainPrefs.contains("SAVED_VALUES" + selectedFoodItems.get(i).item_name + selectedFoodItems.get(i).brand_name)) {
                        String[] parts = mainPrefs.getString("SAVED_VALUES" + selectedFoodItems.get(i).item_name + selectedFoodItems.get(i).brand_name,
                                "").split("\\?");
                        editor.remove("SAVED_VALUES" + selectedFoodItems.get(i).item_name + selectedFoodItems.get(i).brand_name).apply();
                        String amount = parts[0];
                        int unit = Integer.parseInt(parts[1]);

                        if (getView().findViewWithTag(VIEW_TAG + String.valueOf(i)) != null)
                            ((EditText) getView().findViewWithTag(VIEW_TAG + String.valueOf(i)).findViewById(R.id.et_amount_weight_meal_row)).setText(amount);
                        if (getView().findViewWithTag(SPINNER_TAG + String.valueOf(i)) != null)
                            ((Spinner) getView().findViewWithTag(SPINNER_TAG + String.valueOf(i)).findViewById(R.id.spn_unit_meal_row)).setSelection(unit);
                    }

            }

        }
    }


    protected void makeRow(final int testIndex) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View viewToAdd = layoutInflater.inflate(R.layout.row, null);
        viewToAdd.setId(View.generateViewId());//necessary for retain instance?  I don't know.
        viewToAdd.setTag(VIEW_TAG + String.valueOf(testIndex));

        final Spinner spn_Units = (Spinner) viewToAdd.findViewById(R.id.spn_unit_meal_row);
        spn_Units.setTag(SPINNER_TAG + String.valueOf(testIndex));

        EditText et_amount = (EditText) viewToAdd.findViewById(R.id.et_amount_weight_meal_row);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> weightUnitsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.amount_weight_array, R.layout.spinner_item_meal_row);
        // Specify the layout to use when the list of choices appears
        weightUnitsAdapter.setDropDownViewResource(R.layout.spinner_item_meal_row);
        // Apply the adapter to the spinner
        spn_Units.setAdapter(weightUnitsAdapter);

        if (servingInfoString.length() > 0) {//serving info from a saved meal
            et_amount.setText(savedServingInfo[testIndex * 2]);
            final int unitIndex = weightUnitsAdapter.getPosition(savedServingInfo[(testIndex * 2 )+ 1]);
            spn_Units.post(new Runnable() {  //???
                @Override
                public void run() {
                    spn_Units.setSelection(unitIndex);
                }
            });//http://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically
        }
        ImageButton buttonRemove = (ImageButton) viewToAdd.findViewById(R.id.btn_X_meal_row);
        buttonRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_name =
                        ((TextView)((LinearLayout) viewToAdd.getParent()).findViewById(R.id.item_name_meal_row)).getText().toString();
                String brand_name =
                        ((TextView)((LinearLayout) viewToAdd.getParent()).findViewById(R.id.brand_name_meal_row)).getText().toString();
                String itemAndBrand = item_name + "\n(" + brand_name + ")";
                if (selectedFoodsList.contains(itemAndBrand))
                    selectedFoodsList.remove(itemAndBrand);
                LinearLayout rowParent = ((LinearLayout) viewToAdd.getParent());
                rowParent.removeView(viewToAdd);
                resetRowTags(rowParent);  //important to have the tag numbers correspond to the food item's positions after deletion.
            }
        });
        theContainer.addView(viewToAdd);
    }

    public void resetRowTags(LinearLayout rowParent){
        for (int i = 0; i < selectedFoodsList.size(); i++) {
            ((View)rowParent.getChildAt(i)).setTag(VIEW_TAG + String.valueOf(i));
        }
    }

    protected ArrayList<FoodItem> populate_selectedFoodItems(ArrayList<String> selectedFoodsList) {
        //this is so when the user wants to add one or more food items to an existing meal,
        //upon arriving back at the FoodList, the food items already in the meal are already checked.
        //Again, I don't know if it was necessary to actually create FoodItem objects for this
        ArrayList<FoodItem> theSelectedFoodItems = new ArrayList<>();

        for (String food : selectedFoodsList) {
            //split item_name from brand_name;
            String[] nameAndBrand = food.split("\n");
            String item_name = nameAndBrand[0];
            String brand_name = nameAndBrand[1].substring(1, nameAndBrand[1].length() - 1);
            FoodItem theFoodItem = null;
            Cursor cursor = MainActivity.databaseHelper.getFoodItem(item_name, brand_name);
            cursor.moveToFirst();
            theSelectedFoodItems.add(new FoodItem(
                    null, cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.ITEM_NAME)),
                    null, cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.BRAND_NAME)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_CALORIES))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_CALORIES_FROM_FAT))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_TOTAL_FAT))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_SATURATED_FAT))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_TRANS_FATTY_ACID))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_CHOLESTEROL))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_SODIUM))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_TOTAL_CARBOHYDRATE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_DIETARY_FIBER))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_SUGARS))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_PROTEIN))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_SERVING_SIZE_QTY))),
                    cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_SERVING_SIZE_UNIT)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.NF_SERVING_WEIGHT_GRAMS)))
            ));
        }
        return theSelectedFoodItems;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_food_view_calculate, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mealName = ((EditText)getView().findViewById(R.id.meal_name)).getText().toString().trim();
        int mealIndex = MainActivity.databaseHelper.getMealIndex(mealName);
        String foodName, brandName, servAmount, servUnit;
        foodName = brandName = servAmount = servUnit = "";
        ArrayList<String> ings = get_MealInfo(foodName, brandName, servAmount, servUnit);
        //get_MealInfo(foodName, brandName, servAmount, servUnit);

        switch (item.getItemId()) {
            case R.id.menu_item_save:
                if (mealName.isEmpty()){
                    Toast toast = Toast.makeText(getContext(), "Please give this meal a name.", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }

                if (selectedFoodsList.isEmpty()){
                    Toast toast = Toast.makeText(getContext(), "Please select at least one food item.", Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                try {
                    if (mealIndex > -1) {  //meal already exists, so we're deleting and resaving it.
                        MainActivity.databaseHelper.deleteMeal(mealIndex);
                        Toast toast = Toast.makeText(getContext(), mealName + "\nhas been updated.", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(getContext(), mealName + "\nhas been saved.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    long rowID = MainActivity.databaseHelper.insertMeal(mealName, ings);
                    //log.d(TAG, "Meal inserted, rowID " + rowID);
                }catch (Exception e){
                    //log.d(TAG, "onOptionsItemSelected: " + e.getMessage());
                }

                /*  My initial idea was to snap back to the MealList when the user saved or updated the meal.  But while using
                    this app in my kitchen, especially with meals of many ingredients, I wanted to hit "save" now and then to
                    make sure I didn't lose those values I had already weighed on my food scale and entered.  I didn't want to have
                    to load the meal up again every time I hit save.  So now the MealList sticks around after saving, and there's
                    a Toast that tells the user that the meal has been saved, or has been updatead.
                 */

                //selectedFoodsList.clear();
               // ((MainActivity)getActivity()).clear_BackStack();
               // MainActivity.hideKeyboard(getContext());
               // ((MainActivity)getActivity()).show_MealListFragment(false);//boolean itemDeleted

                return true;

            case R.id.menu_item_calculate:
                MainActivity.hideKeyboard(getContext());
                ((MainActivity)getActivity()).show_MealInfoFragment(ings);
                return true;

            case R.id.menu_item_delete:
                int meal_index = MainActivity.databaseHelper.getMealIndex(et_mealName.getText().toString());
                if ((et_mealName.getText().toString() == null) || meal_index == -1){  //meal has no name or is not saved in database so nothing happens
                    return true;
                }else {
                    ((MainActivity) getActivity()).show_ConfirmDeleteMealDialog(mealIndex);
                    return true;
                }

        }
        return super.onOptionsItemSelected(item);//calculate
    }

    protected ArrayList<String> get_MealInfo(String foodName, String brandName, String servAmount, String servUnit) {
        ArrayList<String> ings = new ArrayList<>();
        //ingredientStrings.clear();
        //clear the List first???
        //or change this method to return a list instead of modify a fragment instance variable
        for (int i = 0; i < selectedFoodsList.size(); i++) {
            if (getView().findViewWithTag(VIEW_TAG + String.valueOf(i)) != null) {
                foodName = ((TextView) getView().findViewWithTag(VIEW_TAG + String.valueOf(i))
                        .findViewById(R.id.item_name_meal_row)).getText().toString();
                brandName = ((TextView) getView().findViewWithTag(VIEW_TAG + String.valueOf(i))
                        .findViewById(R.id.brand_name_meal_row)).getText().toString();
                servAmount = ((EditText) getView().findViewWithTag(VIEW_TAG + String.valueOf(i))
                        .findViewById(R.id.et_amount_weight_meal_row)).getText().toString();
                servUnit = ((Spinner) getView().findViewWithTag(VIEW_TAG + String.valueOf(i))
                        .findViewById(R.id.spn_unit_meal_row)).getSelectedItem().toString();
                if (servAmount.isEmpty())
                    servAmount = "0";
            }
            //ingredientStrings.add(foodName + "?" + brandName + "?" + servAmount + "?" + servUnit + "?");
            ings.add(foodName + "?" + brandName + "?" + servAmount + "?" + servUnit + "?");
        }
        return ings;
    }

}




/*

buttonAdd = (Button)rootView.findViewById(R.id.add);
        buttonAdd.setOnClickListener(new OnClickListener(){
@Override
public void onClick(View arg0) {
        makeRow(0);
        if (rootView.findViewWithTag("0") != null){
        LinearLayout theRow = (LinearLayout)rootView.findViewWithTag("0");
        if (selectedFoodsList != null) //maybe try/catch is better here
        if (!selectedFoodsList.isEmpty()) {
        ((TextView) theRow.findViewById(R.id.item_name_meal_row)).setText(selectedFoodItems.get(0).item_name);
        ((TextView) theRow.findViewById(R.id.brand_name_meal_row)).setText(selectedFoodItems.get(0).brand_name);
        }
        }
        //Log.d(TAG, "onCreateView: " + rootView.findViewWithTag("0").getClass().toString());
        }});*/


/*
    protected void showPopup(View v){  //add HashMap parameter
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_show_nutrients, popup.getMenu());

        String [] theNutrients = {"Protein", "Total Fat", "Sat Fat", "Trans Fat", "Total Carbs", "Fiber", "Sugars",
                "Sodium", "Cholesterol"}; //feed in the values from...HashMap object modified by TextWatcher (HashMap lives in MyTextWatcher object)
        ArrayList<MenuItem> theMenuItems = new ArrayList<>();
        for (int i = 0; i < theNutrients.length; i++) {
            theMenuItems.add(popup.getMenu().getItem(i));
            theMenuItems.get(i).setTitle(theNutrients[i]);
        }
        popup.show();
    }*/

  /*  ArrayAdapter<CharSequence> nutrientsAdapter = ArrayAdapter.createFromResource(getContext(),
            R.array.nutrient_array, R.layout.spinner_item_meal_row);
        nutrientsAdapter.setDropDownViewResource(R.layout.spinner_item_meal_row);
        //spn_Nutrients.setAdapter(nutrientsAdapter);*/