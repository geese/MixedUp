package com.chodos.gisela.mixedupmealcalculator;
//http://www.flaticon.com/free-icon/restaurant-utensils_18500

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FoodListFragment extends Fragment {
    public static final String SELECTED_FOODS_DIALOG_FRAG_TAG = "show_foods";
    public static final String SELECTED_FOODS_BUNDLE_KEY = "selected_foods";
    protected Button btn_showSelected;
    protected ImageButton imgBtn_makeMeal;
    protected ImageButton imgBtn_showSelected;
    protected Button btn_makeMeal;

    protected ArrayList<FoodItem> foodItemArrayList;
    protected ArrayList<String> foodNameArrayList;
    protected ArrayList<String> selectedFoodsList;
    protected ArrayList<String> alphabetArrayList;

    protected SharedPreferences mainPrefs;
    protected SharedPreferences.Editor editor;
    protected String[] alphabetArray;
    protected ListView lsv_foodItems;
    protected ListView lsv_alphabet;
    protected ArrayAdapter<String> foodItemAdapter;
    protected AlphabetArrayAdapter alphabetAdapter;
    public static final String TAG = "debugList";

    public FoodListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_food_list, container, false);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Food List");
        mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();

        lsv_foodItems = (ListView) rootView.findViewById(R.id.lsv_food_items);
        lsv_alphabet = (ListView) rootView.findViewById(R.id.lsv_alphabet);
        lsv_alphabet.setDividerHeight(0);

        alphabetArrayList = new ArrayList<String>();
        alphabetArray = populate_Alphabet();
        alphabetAdapter = new AlphabetArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, alphabetArray);

        lsv_alphabet.setAdapter(alphabetAdapter);

        //click on a letter in the Alphabet ListView, and the FoodItem ListView will scroll to the position of the first
        //FoodItem that begins with that letter.
        lsv_alphabet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String theLetter = ((TextView)view).getText().toString();
                int thePosition = find_letterIndex(theLetter, foodNameArrayList);
                lsv_foodItems.smoothScrollToPosition(thePosition);
            }
        });

        lsv_foodItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        foodItemArrayList = populate_FoodItemArrayList();
        foodNameArrayList = populate_FoodNameArrayList();

        alphabetAdapter.notifyDataSetChanged(); //in case returning from deleting a Food Item
        selectedFoodsList = new ArrayList<>();

        Collections.sort(foodNameArrayList, new Comparator<String>() {  //putting FoodItems in alphabetical order by Item Name.
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });
        foodItemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.my_simple_list_item_multiple_choice, foodNameArrayList);
        lsv_foodItems.setAdapter(foodItemAdapter);

        //maintaining checkedness across interruptions.
        ArrayList<String> checkedItemsList = ((MainActivity)getActivity()).getCheckedIngredientStrings();
        if (checkedItemsList != null){
            for (String s : checkedItemsList){
                String[] parts = s.split("\\?");
                String item_name = parts[0];
                String brand_name = parts[1];
                String searchTerm = item_name + "\n(" + brand_name + ")";
                int nameIndex;
                if (foodNameArrayList.contains(searchTerm)) {
                    nameIndex = foodNameArrayList.indexOf(searchTerm);
                    lsv_foodItems.setItemChecked(nameIndex, true);
                }
            }
        }

        lsv_foodItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putBoolean(MainActivity.FROM_SQLITE, true).apply();  //so that "delete" is enabled, and "save" is disabled.
                show_foodView(((TextView)view).getText().toString().trim());
                return true;
            }
        });

        imgBtn_showSelected = (ImageButton)rootView.findViewById(R.id.img_btn_show_selected);
        imgBtn_showSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_selectedFoodsDialog();
            }
        });

        btn_showSelected = (Button)rootView.findViewById(R.id.btn_show_selected);
        btn_showSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_selectedFoodsDialog();
            }
        });

        imgBtn_makeMeal = (ImageButton)rootView.findViewById(R.id.img_btn_make_meal);
        imgBtn_makeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populate_selectedFoodsList();
                ((MainActivity)getActivity()).show_MealViewFragment(selectedFoodsList);
            }
        });

        btn_makeMeal = (Button)rootView.findViewById(R.id.btn_make_meal);
        btn_makeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populate_selectedFoodsList();
                ((MainActivity)getActivity()).show_MealViewFragment(selectedFoodsList);
            }
        });

        return rootView;
    }

    private void show_selectedFoodsDialog() {
        //this dialog is called from this fragment rather than from MainActivity, because it "belongs" to this fragment.
        populate_selectedFoodsList();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("SELECTED_FOODS_LIST", selectedFoodsList);
        SelectedFoodsDialogFrag showFoodsDialog = new SelectedFoodsDialogFrag();
        showFoodsDialog.setArguments(bundle);
        showFoodsDialog.show(getFragmentManager(), SELECTED_FOODS_DIALOG_FRAG_TAG);
    }

    protected void populate_selectedFoodsList() {
        SparseBooleanArray sparseBooleanArray = lsv_foodItems.getCheckedItemPositions();
        String text;
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            text = (String)lsv_foodItems.getItemAtPosition(sparseBooleanArray.keyAt(i));
            if(sparseBooleanArray.valueAt(i) == true) {
                if (!selectedFoodsList.contains(text))
                    selectedFoodsList.add(text);
            }else
                selectedFoodsList.remove(text);
        }
    }

    protected void deselectFoodItem(String food){
        int position = foodNameArrayList.indexOf(food);
        lsv_foodItems.setItemChecked(position, false);
    }

    protected void showPopup(View v){
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_add_food_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.add_search:
                        ((MainActivity)getActivity()).show_NutrxListFragment();
                        break;
                    case R.id.add_custom:
                        ((MainActivity)getActivity()).show_FoodEditFragment(null);
                        break;
                }
                return true;
            }
        });
        popup.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showPopup(getView().findViewById(R.id.popup_menu_anchor));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean("from_home")) {
                editor.clear().apply();
                bundle.putBoolean("from_home", false);
            }
        }
        }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SELECTED_FOODS_BUNDLE_KEY, selectedFoodsList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            selectedFoodsList = savedInstanceState.getStringArrayList(SELECTED_FOODS_BUNDLE_KEY);
    }



    protected void show_foodView(String theFoodNameAndBrand){
        //We're pulling information from the SQLite database.  We access a food item record by using item name and brand name
        //as a composite primary key.  We store info from the cursor, into a FoodItem object that will get passed to the
        //FoodView fragment, for display.

        String[] nameAndBrand = theFoodNameAndBrand.split("\n");
        String item_name = nameAndBrand[0];
        String brand_name = nameAndBrand[1].substring(1, nameAndBrand[1].length()-1);
        FoodItem theFoodItem = null;
        Cursor cursor = MainActivity.databaseHelper.getFoodItem(item_name, brand_name);
        if(cursor.moveToFirst()){//moveToFirst returns false if the cursor is empty
            int index = cursor.getInt(0);
            SharedPreferences mainPrefs = getActivity().getPreferences(MainActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mainPrefs.edit();
            editor.putInt(MainActivity.INDEX_OF_FOOD_ITEM_TO_EDIT, index).apply();
                try {
                    theFoodItem = new FoodItem(
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
                    );
                }catch (NumberFormatException nfe){
                    //log.d(TAG, "show_foodView: " + nfe.getMessage());
                }
            }
        ((MainActivity)getActivity()).show_FoodViewFragment(theFoodItem);
    }

    protected ArrayList<String> getSelectedFoodsList(){
        return selectedFoodsList;
    }


    protected ArrayList<FoodItem> populate_FoodItemArrayList(){
        /*  This was one of the first things I wrote in this project, and I'm not sure it isn't overkill.  I think
            I thought it would be useful to have an ArrayList of FoodItems, for populating...something.  Really I
            probably only needed item names and brand names.  Not sure I needed to do this whole method.
         */
        ArrayList<FoodItem> foodItemList = new ArrayList<>();
        Cursor cursor = MainActivity.databaseHelper.getAllFoodItems();
        //log.d(TAG, "populate_FoodItemArrayList: cursor count: " + cursor.getCount());

        if(cursor.moveToFirst()){//moveToFirst returns false if the cursor is empty
            do {
                try {
                    foodItemList.add(new FoodItem(
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
                    alphabetArrayList.add(cursor.getString(cursor.getColumnIndex(MainActivity.databaseHelper.ITEM_NAME)).substring(0,1).toUpperCase());
                }catch (NumberFormatException nfe){
                    //log.d(TAG, "populate_FoodItemArrayList: " + nfe.getMessage());
                }
            }while(cursor.moveToNext());
        }
        return foodItemList;
    }

    protected ArrayList<String> populate_FoodNameArrayList(){
        ArrayList<String> theList = new ArrayList<>();
        for (FoodItem foodItem : foodItemArrayList){
            theList.add(foodItem.item_name + "\n(" + foodItem.brand_name + ")");
        }
        return theList;
    }

    protected String[] populate_Alphabet(){
        String[] theArray = new String[26];

        for (int i = 65; i < (65+26); i++) {
            theArray[i-65] = String.valueOf((char)i);
        }
        return theArray;
    }

    protected int find_letterIndex(String letter, ArrayList<String> theArrayList){
        for (String s: theArrayList) {
            if (s.startsWith(letter) || s.startsWith(letter.toLowerCase()))
                return theArrayList.indexOf(s);
        }
        return 0;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_food_list_add, menu);
    }

    protected class AlphabetArrayAdapter extends ArrayAdapter<String>{

        public AlphabetArrayAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView txv = (TextView)row.findViewById(android.R.id.text1);
            String theLetter = txv.getText().toString();
            if (alphabetArrayList.contains(theLetter))
                txv.setTextColor(Color.parseColor("#E64A19"));  //a deep red-orange, to show that a food item exists that starts with this letter
            else
                txv.setTextColor(Color.parseColor("#36000000"));  //light gray, no food items begin with this letter.
            return row;
        }
    }


    //setRetainInstance(true);  //not necessary???  Just need an id for every view ???
    //https://developer.android.com/training/basics/activity-lifecycle/recreating.html

}
