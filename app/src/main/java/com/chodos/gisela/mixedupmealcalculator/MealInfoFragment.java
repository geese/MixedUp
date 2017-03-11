package com.chodos.gisela.mixedupmealcalculator;

//Dynamically adding Views:
//http://android-er.blogspot.com/2013/05/add-and-remove-view-dynamically.html

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import java.util.ArrayList;


public class MealInfoFragment extends Fragment {
    protected String testString;
    public static final String VIEW_TAG = "LinearLayout_";
    public static final String TEXT_VIEW_TAG = "TextView_";
    public static final String TAG = "debugMealInfo";
    TextView txv_MealName;
    Button btn_backToMealView;
    ImageButton img_btn_backToMealView;
    SharedPreferences mainPrefs;
    SharedPreferences.Editor editor;

    LinearLayout theContainer;
    ArrayList<String> selectedFoodsList;
    ArrayList<FoodItem> ingredientFoodItems;
    ArrayList<String> ingredientStrings;
    ArrayList<String> servingAmountsList;
    ArrayList<String> servingUnitsList;
    double totalMealWeight, totalMealCals, totalMealProt, totalMealTotFat, totalMealSatFat, totalMealTransFat,
            totalMealCarbs, totalMealFiber, totalMealSugars, totalMealSodium, totalMealChol;
    double calPerGram, protPerGram, totFatPerGram, satFatPerGram, transFatPerGram, carbsPerGram, fiberPerGram,
        sugarsPerGram, sodiumPerGram, cholPerGram;
    TextView txv_calsMealTotal, txv_protMealTotal, txv_calsPerGram, txv_protPerGram;


    public MealInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_meal_info, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Meal Info");
        btn_backToMealView = (Button)rootView.findViewById(R.id.btn_back_to_meal_view);
        img_btn_backToMealView = (ImageButton)rootView.findViewById(R.id.img_btn_back_to_meal_view);
        mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();
        theContainer = (LinearLayout) rootView.findViewById(R.id.container_meal_info);
        txv_MealName = (TextView)rootView.findViewById(R.id.txv_meal_name);

        servingAmountsList = new ArrayList<>();
        servingUnitsList = new ArrayList<>();

        img_btn_backToMealView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btn_backToMealView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return rootView;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }




    @Override
    public void onResume() {
        super.onResume();

        totalMealWeight = totalMealCals = totalMealProt = totalMealTotFat = totalMealSatFat = totalMealTransFat =
                totalMealCarbs = totalMealFiber = totalMealSugars = totalMealSodium = totalMealChol = 0;

        ((MainActivity)getActivity()).show_MealTotalsFragment();

        Bundle bundle = getArguments();

        txv_MealName.setText(mainPrefs.getString("meal_name", ""));
        if (txv_MealName.getText().toString().isEmpty())
            txv_MealName.setText("(Meal Name)");

        ingredientStrings = bundle.getStringArrayList("ings");
        //turn ingredientStrings into ArrayList of FoodItems.
        ingredientFoodItems = populate_ingredientFoodItems(ingredientStrings);
        totalMealCals = 0;

        for (int i = 0; i < ingredientFoodItems.size(); i++) {
            String[] parts = ingredientStrings.get(i).split("\\?");
            servingAmountsList.add(parts[2]);
            servingUnitsList.add(parts[3]);
            makeRow(i);
            LinearLayout theRow = (LinearLayout) getView().findViewWithTag(VIEW_TAG + String.valueOf(i));
            ((TextView) theRow.findViewById(R.id.item_name_meal_row)).setText(ingredientFoodItems.get(i).item_name);
            ((TextView) theRow.findViewById(R.id.brand_name_meal_row)).setText(ingredientFoodItems.get(i).brand_name);
        }

        //log.d(TAG, "Total Calories: " + totalMealCals);
        //log.d(TAG, "Total Protein: " + totalMealProt);
        editor.putString(CALORIES, String.format("%.2f",totalMealCals))
                .putString(CAL_PER_GRAM, String.format("%.2f",(totalMealCals / totalMealWeight)))
                .putString(PROTEIN, String.format("%.2f",totalMealProt))
                .putString(PROT_PER_GRAM, String.format("%.2f",(totalMealProt / totalMealWeight)))
                .putString(TOT_FAT, String.format("%.2f",(totalMealTotFat)))
                .putString(SAT_FAT, String.format("%.2f",(totalMealSatFat)))
                .putString(TRANS_FAT, String.format("%.2f",(totalMealTransFat)))
                .putString(TOT_CARBS, String.format("%.2f",(totalMealCarbs)))
                .putString(FIBER, String.format("%.2f",(totalMealFiber)))
                .putString(SUGARS, String.format("%.2f",(totalMealSugars)))
                .putString(SODIUM, String.format("%.2f",(totalMealSodium)))
                .putString(CHOL, String.format("%.2f",(totalMealChol)))

                .putString(TOT_FAT_PER_GRAM, String.format("%.2f",(totalMealTotFat / totalMealWeight)))
                .putString(SAT_FAT_PER_GRAM, String.format("%.2f",(totalMealSatFat / totalMealWeight)))
                .putString(TRANS_FAT_PER_GRAM, String.format("%.2f",(totalMealTransFat / totalMealWeight)))
                .putString(TOT_CARBS_PER_GRAM, String.format("%.2f",(totalMealCarbs / totalMealWeight)))
                .putString(FIBER_PER_GRAM, String.format("%.2f",(totalMealFiber / totalMealWeight)))
                .putString(SUGARS_PER_GRAM, String.format("%.2f",(totalMealSugars / totalMealWeight)))
                .putString(SODIUM_PER_GRAM, String.format("%.2f",(totalMealSodium / totalMealWeight)))
                .putString(CHOL_PER_GRAM, String.format("%.2f",(totalMealChol / totalMealWeight)))


                .apply();

    }

    protected void makeRow(int rowIndex) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.row_meal_info, null);
        addView.setId(View.generateViewId()); //??  needed?
        addView.setTag(VIEW_TAG + String.valueOf(rowIndex));

        FoodItem foodItem = ingredientFoodItems.get(rowIndex);
        //Log.d(TAG, "makeRow: foodItem weight in grams: " + foodItem.nf_serving_weight_grams);

        double foodAmountGrams = getAmountWeightGrams(servingAmountsList.get(rowIndex), servingUnitsList.get(rowIndex));
        double caloriesPerGram = foodItem.nf_calories / foodItem.nf_serving_weight_grams;
        double proteinPerGram = foodItem.nf_protein / foodItem.nf_serving_weight_grams;
        double totFatPerGram = foodItem.nf_total_fat / foodItem.nf_serving_weight_grams;
        double satFatPerGram = foodItem.nf_saturated_fat / foodItem.nf_serving_weight_grams;
        double transFatPerGram = foodItem.nf_trans_fatty_acid / foodItem.nf_serving_weight_grams;
        double totCarbsPerGram = foodItem.nf_total_carbohydrate / foodItem.nf_serving_weight_grams;
        double fiberPerGram = foodItem.nf_dietary_fiber / foodItem.nf_serving_weight_grams;
        double sugarsPerGram = foodItem.nf_sugars / foodItem.nf_serving_weight_grams;
        double sodiumPerGram = foodItem.nf_sodium / foodItem.nf_serving_weight_grams;
        double cholPerGram = foodItem.nf_cholesterol / foodItem.nf_serving_weight_grams;

        double caloriesThisIngredient = foodAmountGrams * caloriesPerGram;
        double proteinThisIngredient = foodAmountGrams * proteinPerGram;
        final double totFatThisIngredient = foodAmountGrams * totFatPerGram;
        final double satFatThisIngredient = foodAmountGrams * satFatPerGram;
        final double transFatThisIngredient = foodAmountGrams * transFatPerGram;
        final double totCarbsThisIngredient = foodAmountGrams * totCarbsPerGram;
        final double fiberThisIngredient = foodAmountGrams * fiberPerGram;
        final double sugarsThisIngredient = foodAmountGrams * sugarsPerGram;
        final double sodiumThisIngredient = foodAmountGrams * sodiumPerGram;
        final double cholThisIngredient = foodAmountGrams * cholPerGram;

        totalMealWeight += foodAmountGrams;
        totalMealCals += caloriesThisIngredient;
        totalMealProt += proteinThisIngredient;
        totalMealTotFat += totFatThisIngredient;
        totalMealSatFat += satFatThisIngredient;
        totalMealTransFat += transFatThisIngredient;
        totalMealCarbs += totCarbsThisIngredient;
        totalMealFiber += fiberThisIngredient;
        totalMealSugars += sugarsThisIngredient;
        totalMealSodium += sodiumThisIngredient;
        totalMealChol += cholThisIngredient;

        TextView txv_Amount = (TextView) addView.findViewById(R.id.txv_amount_weight_meal_row);
        txv_Amount.setText(servingAmountsList.get(rowIndex) + " " + servingUnitsList.get(rowIndex));

        TextView txv_Calories = (TextView) addView.findViewById(R.id.txv_calories_meal_row);
        TextView txv_Protein = (TextView) addView.findViewById(R.id.txv_protein_meal_row);

        txv_Calories.setText((String.format("%.2f", caloriesThisIngredient)));
        txv_Protein.setText((String.format("%.2f", proteinThisIngredient) + " g"));

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> nutrientsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.nutrient_array, R.layout.spinner_item_meal_row);

        // Specify the layout to use when the list of choices appears

        nutrientsAdapter.setDropDownViewResource(R.layout.spinner_item_meal_row);

        // Apply the adapter to the spinner

        ImageButton buttonOverflowNutrients = (ImageButton) addView.findViewById(R.id.overflow_nutrients);
        buttonOverflowNutrients.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, totFatThisIngredient, satFatThisIngredient, transFatThisIngredient,
                        totCarbsThisIngredient, fiberThisIngredient, sugarsThisIngredient, sodiumThisIngredient, cholThisIngredient);
            }
        });

        theContainer.addView(addView);
    }


    protected void showPopup(View v,double totFat, double satFat, double transFat, double carbs, double fiber,
                             double sugars, double sodium, double cholesterol) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_show_nutrients, popup.getMenu());

        String[] theNutrients =
                {
                        StringUtils.padRight("Total Fat", 21) + (String.format("%.2f",totFat)) + " g",
                        StringUtils.padRight("Sat Fat", 22) + (String.format("%.2f",satFat)) + " g",
                        StringUtils.padRight("Trans Fat", 20) + (String.format("%.2f",transFat)) + " g",
                        StringUtils.padRight("Total Carbs", 18) + (String.format("%.2f",carbs)) + " g",
                        StringUtils.padRight("Fiber", 23) + (String.format("%.2f",fiber)) + " g",
                        StringUtils.padRight("Sugars", 20) + (String.format("%.2f",sugars)) + " g",
                        StringUtils.padRight("Sodium", 18) + (String.format("%.2f",sodium)) + " mg",
                        StringUtils.padRight("Cholesterol", 16) + (String.format("%.2f",cholesterol)) + " mg"
                };
        ArrayList<MenuItem> theMenuItems = new ArrayList<>();
        for (int i = 0; i < theNutrients.length; i++) {
            theMenuItems.add(popup.getMenu().getItem(i));
            theMenuItems.get(i).setTitle(theNutrients[i]);
        }
        popup.show();
    }

    protected ArrayList<FoodItem> populate_ingredientFoodItems(ArrayList<String> ingredientStrings) {
        ArrayList<FoodItem> theIngredientFoodItems = new ArrayList<>();

        for (String food : ingredientStrings) {
            //split item_name from brand_name;
            String[] ingredientInfo = food.split("\\?");
            String item_name = ingredientInfo[0];
            String brand_name = ingredientInfo[1];
          //  Log.d(TAG, "populate_ingredientFoodItems: item_name and brand_name: " + item_name + ", " + brand_name);

            FoodItem theFoodItem = null;
            Cursor cursor = MainActivity.databaseHelper.getFoodItem(item_name, brand_name);
            cursor.moveToFirst();
            theIngredientFoodItems.add(new FoodItem(
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

        return theIngredientFoodItems;
    }


    private double getAmountWeightGrams(String servAmt, String servUnit) {
        double amountWeightGrams = 0;
        switch (servUnit) {
            case ("g"):
            case ("mL")://treating 1 mL as 1 gram
                amountWeightGrams = Double.parseDouble(servAmt);
                break;
            case ("oz"):
                amountWeightGrams = 28.35 * Double.parseDouble(servAmt);
                break;
            case ("fl oz"):
                amountWeightGrams = 29.57 * Double.parseDouble(servAmt);
                break;
        }
        return amountWeightGrams;
    }


    public static final String CALORIES = "calories";
    public static final String CAL_PER_GRAM = "cal_per_gram";
    public static final String PROTEIN = "protein";
    public static final String PROT_PER_GRAM = "prot_per_gram";
    public static final String TOT_FAT = "tot_fat";
    public static final String SAT_FAT = "sat_fat";
    public static final String TRANS_FAT = "trans_fat";
    public static final String TOT_CARBS = "tot_carbs";
    public static final String FIBER = "fiber";
    public static final String SUGARS = "sugars";
    public static final String SODIUM = "sodium";
    public static final String CHOL = "chol";

    public static final String TOT_FAT_PER_GRAM = "tot_fat_per_gram";
    public static final String SAT_FAT_PER_GRAM = "sat_fat_per_gram";
    public static final String TRANS_FAT_PER_GRAM = "trans_fat_per_gram";
    public static final String TOT_CARBS_PER_GRAM = "tot_carbs_per_gram";
    public static final String FIBER_PER_GRAM = "fiber_per_gram";
    public static final String SUGARS_PER_GRAM = "sugars_per_gram";
    public static final String SODIUM_PER_GRAM = "sodium_per_gram";
    public static final String CHOL_PER_GRAM = "chol_per_gram";


}

