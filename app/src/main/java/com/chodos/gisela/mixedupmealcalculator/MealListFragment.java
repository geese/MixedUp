package com.chodos.gisela.mixedupmealcalculator;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MealListFragment extends Fragment {
    ListView lsv_mealList;
    ArrayList<String> mealList;
    ArrayAdapter<String> mealAdapter;
    ArrayList<String> selectedFoodsList; //selectedFoodsList is what it's called coming from FoodList
    SharedPreferences mainPrefs;
    SharedPreferences.Editor editor;
    public static final String MEAL_NAME_KEY = "theMealName";
    public static final String SERVING_INFO = "servingInfo";
    public static final String TAG = "debugMealList";
    String servingInfoToSplit;

    public MealListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meal_list, container, false);
        mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Meal List");
        servingInfoToSplit = "";
        lsv_mealList = (ListView) rootView.findViewById(R.id.lsv_mealList);
        mealList = populate_mealList();
        mealAdapter = new ArrayAdapter<String>(getContext(), R.layout.my_list_view, mealList);
        lsv_mealList.setAdapter(mealAdapter);
        lsv_mealList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mealName = ((TextView) view).getText().toString();
                selectedFoodsList = populate_selectedFoodsList(mealName);
                editor.putString(MEAL_NAME_KEY, mealName).apply();
                ((MainActivity) getActivity()).show_MealViewFragment(selectedFoodsList);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        editor.clear().apply();  //when choosing to view a Meal, I don't want OLD DATA from a previously displayed meal
        //superceding the data that's coming from SQLite.
    }

    protected ArrayList<String> populate_mealList() {
        ArrayList<String> theMeals = new ArrayList<>();
        Cursor cursor = MainActivity.databaseHelper.getAllMeals();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                theMeals.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
        Collections.sort(theMeals, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });
        return theMeals;
    }

    protected ArrayList<String> populate_selectedFoodsList(String mealName) {
        ArrayList<String> foodsList = new ArrayList<>();
        ArrayList<Integer> foodIndexList = new ArrayList<>();
        int mealIndex = MainActivity.databaseHelper.getMealIndex(mealName);
        Cursor mealCursor = null;

        if (mealIndex != -1)
            mealCursor = MainActivity.databaseHelper.getMeal(mealIndex);
        if (mealCursor.getCount() > 0) {
            mealCursor.moveToFirst();
            while (!mealCursor.isAfterLast()) {
                //For each food ingredient, we store the serving portion weight and its serving unit, and compile them all
                //into one string with each part separated by "?".  The string will get split by the "?"s, and the info parsed into a String array,
                //in the MealView Fragment.

                servingInfoToSplit += mealCursor.getString(3) + "?" + mealCursor.getString(4) + "?"; //stored meal ingredient serving amounts and units
                foodIndexList.add(mealCursor.getInt(2));  //getting the index numbers of all food items that are ingredients of the selected meal.
                mealCursor.moveToNext();
            }
            for (Integer i : foodIndexList) {
                Cursor foodCursor = MainActivity.databaseHelper.getFoodItem(i);
                if (foodCursor.getCount() > 0) {
                    foodCursor.moveToFirst();
                    String itemName = foodCursor.getString(1);
                    String brandName = foodCursor.getString(2);
                    foodsList.add(itemName + "\n(" + brandName + ")");
                }
            }
        }
        editor.putString(SERVING_INFO, servingInfoToSplit).apply();
        return foodsList;
    }


}
