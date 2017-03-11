package com.chodos.gisela.mixedupmealcalculator;

/*
Navigation Drawer Help!  (tutorials)
http://blog.teamtreehouse.com/add-navigation-drawer-android
https://www.codeofaninja.com/2014/02/android-navigation-drawer-example.html
 */


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static DatabaseHelper databaseHelper;
    public static final String DATABASE_NAME = "MixedUp Database";
    public static final int SCHEMA = 1;
    public static final String INDEX_OF_FOOD_ITEM_TO_EDIT = "food_item_index";
    public static final String FROM_NUTRX = "from_nutrx";
    public static final String FROM_SQLITE = "from_sqlite";
    public static final String TAG = "debugMain";
    public static final String HOME_FRAG_TAG = "home";
    public static final String MEAL_LIST_FRAG_TAG = "meal_list";
    public static final String FOOD_LIST_FRAG_TAG = "food_list";
    public static final String FOOD_EDIT_FRAG_TAG = "food_edit";
    public static final String NUTRX_LIST_FRAG_TAG = "nutrx_list";
    public static final String FOOD_VIEW_FRAG_TAG = "food_view";
    public static final String MEAL_VIEW_FRAG_TAG = "meal_view";
    public static final String MEAL_INFO_FRAG_TAG = "meal_info";
    public static final String MEAL_TOTALS_FRAG_TAG = "meal_totals";

    private ListView mDrawerList;                  //online help with this is credited up in comments at top
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    FoodItem foodItemToView;
    ArrayList<String> selectedFoodsList;
    ArrayList<String> ingredientStrings;
    ArrayList<String> checkedIngredientStrings;
    SharedPreferences mainPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPrefs = getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();

        databaseHelper = new DatabaseHelper(getApplicationContext(), DATABASE_NAME, null, SCHEMA);//done this way (static and application context)
        //because recommended in "Busy Coder's Guide..."

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //navigation drawer related
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        setupDrawer();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //"from_home" signals FoodList (the next screen) onResume() to clear the Preferences Editor.  That way
                //old data is cleared so that new meals can be formed with new ingredients and new amounts, without old stuff
                //getting in the way.
                Bundle bundle = new Bundle();
                bundle.putBoolean("from_home", true);

                switch (position) {
                    case 0:
                        clear_BackStack();  //just don't want the backStack growing to monstrous size.
                        FoodListFragment foodListFragment = new FoodListFragment();
                        foodListFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.food_list_container, foodListFragment, FOOD_LIST_FRAG_TAG)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 1:
                        clear_BackStack();
                        MealListFragment mealListFragment = new MealListFragment();
                        mealListFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.food_list_container, mealListFragment, MEAL_LIST_FRAG_TAG)
                                .addToBackStack(null)
                                .commit();
                        break;
                    case 2:
                        clear_BackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.food_list_container, new HomeFragment(), HOME_FRAG_TAG)
                                .commit();
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        if (getSupportFragmentManager().findFragmentByTag(HOME_FRAG_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.food_list_container, new HomeFragment(), HOME_FRAG_TAG)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {  //don't use the one with Persistent Bundle
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("LIST", selectedFoodsList);   //needs to be passed elsewhere, so shouldn't be lost.
        outState.putStringArrayList("INGR_LIST", ingredientStrings);  //same as above
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedFoodsList = savedInstanceState.getStringArrayList("LIST");
        ingredientStrings = savedInstanceState.getStringArrayList("INGR_LIST");
    }

    protected void clear_BackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private void addDrawerItems() {
        String[] pathArray = {"Food List", "Meal List", "Home"};
        mAdapter = new ArrayAdapter<String>(this, R.layout.my_list_view, pathArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            String actionBarTitle = getSupportActionBar().getTitle().toString();
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void show_FoodListFragment(boolean itemDeleted) {

        if (checkedIngredientStrings != null)
            checkedIngredientStrings.clear();
        /*if (itemDeleted){
            getSupportFragmentManager().popBackStack();
        }*/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, new FoodListFragment(), FOOD_LIST_FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }

    protected void show_FoodListFragment(ArrayList<String> checkedIngrStrings) {
        this.checkedIngredientStrings = checkedIngrStrings;   //this was a solution I implemented before discovering how to
        //pass data to Fragments in Bundles.  The ArrayList checkedIngrStrings holds the names of "checked" foods, so that
        //if a user goes from FoodList to FoodView and then back again, the items previously checked will be checked again.
        //(so it looks like they stayed checked).

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, new FoodListFragment(), FOOD_LIST_FRAG_TAG)
                .addToBackStack(null)
                .commit();

    }

    protected void show_FoodViewFragment(FoodItem foodItem) {
        //the food item information is stored as a FoodItem object, making it simpler to access its nutrient information.
        foodItemToView = foodItem;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, new FoodViewFragment(), FOOD_VIEW_FRAG_TAG)
                .addToBackStack(null)
                .commit();
        hideKeyboard(this);
    }

    protected void show_FoodEditFragment(FoodItem foodItem) {
        foodItemToView = foodItem;
        getSupportFragmentManager().popBackStack(); // so that the correct menu options are enabled/disabled depending on coming from SQLite or Nutritionix
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, new FoodEditFragment(), FOOD_EDIT_FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }

    protected void show_NutrxListFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, new NutrxListFragment(), NUTRX_LIST_FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }

    protected void show_MealViewFragment(ArrayList<String> selectedFoodsList) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("SELECTED_FOODS_LIST", selectedFoodsList);  //SO MUCH EASIER TO USE THE BUNDLE!!! (replaced the next line)
        // this.selectedFoodsList = selectedFoodsList;
        MealViewFragment mealViewFragment = new MealViewFragment();
        mealViewFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, mealViewFragment, MEAL_VIEW_FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }

    protected void show_MealInfoFragment(ArrayList<String> ingredientStrings) {
        //using two fragments on one screen because something went really buggy with the TextViews in the MealTotals part,
        //when everything was in one fragment.
        //this.ingredientStrings = ingredientStrings;
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("ings", ingredientStrings);
        MealInfoFragment mealInfoFragment = new MealInfoFragment();
        mealInfoFragment.setArguments(bundle);

        if (getSupportFragmentManager().findFragmentByTag(MEAL_INFO_FRAG_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.food_list_container, mealInfoFragment, MEAL_INFO_FRAG_TAG)
                    .addToBackStack(null).commit();
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.totals_container, new MealTotalsFragment(), MEAL_TOTALS_FRAG_TAG)
                    .commit();*/

        }
    }

    protected void show_MealTotalsFragment(){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.totals_container, new MealTotalsFragment(), MEAL_TOTALS_FRAG_TAG)
                    .commit();
    }

    protected void show_MealListFragment(boolean itemDeleted) {
/*
        if (itemDeleted){
            getSupportFragmentManager().popBackStack();
        }*/

        Bundle bundle = new Bundle();
        bundle.putBoolean("from_home", true);  //signals MealList onResume() to clear the Preferences editor to clear out old data

        MealListFragment mealListFragment = new MealListFragment();
        mealListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, mealListFragment, MEAL_LIST_FRAG_TAG)
                .addToBackStack(null)
                .commit();
    }


    protected void show_AttributionsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, new AttributionsFragment(), "Attributions")
                .addToBackStack(null).commit();

    }

    protected void show_InstructionsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.food_list_container, new InstructionsFragment(), "Instructions")
                .addToBackStack(null).commit();

    }

    protected void show_ConfirmDeleteFoodDialog(String foodName) {
        Bundle bundle = new Bundle();
        bundle.putString("FOOD_NAME", foodName);
        ConfirmDeleteFoodDialog confirmDeleteFoodDialog = new ConfirmDeleteFoodDialog();
        confirmDeleteFoodDialog.setArguments(bundle);
        confirmDeleteFoodDialog.show(getSupportFragmentManager(), "confirmDeleteFood");
    }

    protected void show_ConfirmDeleteMealDialog(int mealIndex) {
        Bundle bundle = new Bundle();
        bundle.putInt("MEAL_INDEX", mealIndex);
        ConfirmDeleteMealDialog confirmDeleteMealDialog = new ConfirmDeleteMealDialog();
        confirmDeleteMealDialog.setArguments(bundle);
        confirmDeleteMealDialog.show(getSupportFragmentManager(), "confirmDeleteMeal");
    }

    protected FoodItem getFoodItemToView() {
        return foodItemToView;
    }

    protected ArrayList<String> getSelectedFoodsList() {
        return selectedFoodsList;
    }

    protected ArrayList<String> getIngredientStrings() {
        return ingredientStrings;
    }

    protected ArrayList<String> getCheckedIngredientStrings() {
        return checkedIngredientStrings;
    }

    /*
       hideKeyboard method obtained from http://stackoverflow.com/questions/26911469/hide-keyboard-when-navigating-from-a-fragment-to-another
       and no, I don't know enough to have written that myself.  :-(
    */
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

   /* public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }*/

}
