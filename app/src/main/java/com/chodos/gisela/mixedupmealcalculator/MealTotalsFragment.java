package com.chodos.gisela.mixedupmealcalculator;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class MealTotalsFragment extends Fragment {
    public static final String TAG = "debugMealTotals";
    SharedPreferences mainPrefs;
    SharedPreferences.Editor editor;
    TextView txv_calsMealTotal, txv_protMealTotal, txv_calsPerGram, txv_protPerGram;
    ImageButton imgBtn_nutrientTotals, imgBtn_nutrientsPerGram;


    public MealTotalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meal_totals, container, false);
        //setRetainInstance(true);  //bad idea for this fragment because it's contained in another fragment??
        mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();
        imgBtn_nutrientTotals = (ImageButton) rootView.findViewById(R.id.img_btn_nutrient_totals);
        imgBtn_nutrientsPerGram = (ImageButton) rootView.findViewById(R.id.img_btn_nutrient_per_gram);
        txv_calsMealTotal = (TextView) rootView.findViewById(R.id.txv_freaking_cals);
        txv_protMealTotal = (TextView) rootView.findViewById(R.id.txv_protein_meal_total);
        txv_calsPerGram = (TextView) rootView.findViewById(R.id.txv_freaking_cals_per_gram);
        txv_protPerGram = (TextView) rootView.findViewById(R.id.txv_protein_per_gram);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> nutrientsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.nutrient_array, R.layout.spinner_item_meal_row);

        // Specify the layout to use when the list of choices appears

        nutrientsAdapter.setDropDownViewResource(R.layout.spinner_item_meal_row);

        // Apply the adapter to the spinner

        imgBtn_nutrientTotals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        imgBtn_nutrientsPerGram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        txv_calsMealTotal.setText(mainPrefs.getString(MealInfoFragment.CALORIES, "yo mama"));
        txv_calsPerGram.setText(mainPrefs.getString(MealInfoFragment.CAL_PER_GRAM, "yo mama"));
        txv_protMealTotal.setText(mainPrefs.getString(MealInfoFragment.PROTEIN, "yo mama")+ " g");
        txv_protPerGram.setText(mainPrefs.getString(MealInfoFragment.PROT_PER_GRAM, "yo mama")+ " g");
    }

    protected void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_show_nutrients, popup.getMenu());
        switch (v.getId()) {
            case (R.id.img_btn_nutrient_totals):
                String[] theNutrients =
                        {
                                StringUtils.padRight("Total Fat", 21) + mainPrefs.getString(MealInfoFragment.TOT_FAT, "yo mama") + " g",
                                StringUtils.padRight("Sat Fat", 22) + mainPrefs.getString(MealInfoFragment.SAT_FAT, "yo mama") + " g",
                                StringUtils.padRight("Trans Fat", 20) + mainPrefs.getString(MealInfoFragment.TRANS_FAT, "yo mama") + " g",
                                StringUtils.padRight("Total Carbs", 18) + mainPrefs.getString(MealInfoFragment.TOT_CARBS, "yo mama") + " g",
                                StringUtils.padRight("Fiber", 23) + mainPrefs.getString(MealInfoFragment.FIBER, "yo mama") + " g",
                                StringUtils.padRight("Sugars", 20) + mainPrefs.getString(MealInfoFragment.SUGARS, "yo mama") + " g",
                                StringUtils.padRight("Sodium", 18) + mainPrefs.getString(MealInfoFragment.SODIUM, "yo mama") + " mg",
                                StringUtils.padRight("Cholesterol", 16) + mainPrefs.getString(MealInfoFragment.CHOL, "yo mama") + " mg"
                        };

                ArrayList<MenuItem> theMenuItems = new ArrayList<>();
                for (int i = 0; i < theNutrients.length; i++) {
                    theMenuItems.add(popup.getMenu().getItem(i));
                    theMenuItems.get(i).setTitle(theNutrients[i]);
                }
                break;
            case (R.id.img_btn_nutrient_per_gram):
                String[] theOtherNutrients =
                        {
                                StringUtils.padRight("Total Fat", 21) + mainPrefs.getString(MealInfoFragment.TOT_FAT_PER_GRAM, "yo mama") + " g",
                                StringUtils.padRight("Sat Fat", 22) + mainPrefs.getString(MealInfoFragment.SAT_FAT_PER_GRAM, "yo mama") + " g",
                                StringUtils.padRight("Trans Fat", 20) + mainPrefs.getString(MealInfoFragment.TRANS_FAT_PER_GRAM, "yo mama") + " g",
                                StringUtils.padRight("Total Carbs", 18) + mainPrefs.getString(MealInfoFragment.TOT_CARBS_PER_GRAM, "yo mama") + " g",
                                StringUtils.padRight("Fiber", 23) + mainPrefs.getString(MealInfoFragment.FIBER_PER_GRAM, "yo mama") + " g",
                                StringUtils.padRight("Sugars", 20) + mainPrefs.getString(MealInfoFragment.SUGARS_PER_GRAM, "yo mama") + " g",
                                StringUtils.padRight("Sodium", 18) + mainPrefs.getString(MealInfoFragment.SODIUM_PER_GRAM, "yo mama") + " mg",
                                StringUtils.padRight("Cholesterol", 16) + mainPrefs.getString(MealInfoFragment.CHOL_PER_GRAM, "yo mama") + " mg"
                        };

                ArrayList<MenuItem> theOtherMenuItems = new ArrayList<>();
                for (int i = 0; i < theOtherNutrients.length; i++) {
                    theOtherMenuItems.add(popup.getMenu().getItem(i));
                    theOtherMenuItems.get(i).setTitle(theOtherNutrients[i]);
                }
                break;
        }
        popup.show();
    }
}
