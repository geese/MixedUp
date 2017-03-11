package com.chodos.gisela.mixedupmealcalculator;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class NutrxListFragment extends Fragment {

    public static final String TAG = "debugNutrxList";
    protected Gson gson;
    protected GsonBuilder gsonBuilder;
    private List<FoodItem> foodsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NutrxAdapter nutrxAdapter;
    protected SearchView srchv_nutrx;
    protected String theSearchQuery;
    protected SharedPreferences mainPrefs;
    protected SharedPreferences.Editor editor;
    ProgressBar progressBar;

    public NutrxListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nutrx_list, container, false);
        setRetainInstance(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("New Food Search");
        mainPrefs = ((MainActivity)getActivity()).getPreferences(MainActivity.MODE_PRIVATE);
        editor = mainPrefs.edit();
        progressBar = (ProgressBar)rootView.findViewById(R.id.a_progressbar);
        theSearchQuery = "";
        srchv_nutrx = (SearchView)rootView.findViewById(R.id.srchv_nutrx);
        srchv_nutrx.setSubmitButtonEnabled(true);
        srchv_nutrx.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                theSearchQuery = srchv_nutrx.getQuery().toString();
                theSearchQuery = prepareSearchQuery(theSearchQuery);
                ((MainActivity)getActivity()).hideKeyboard(getContext());
                new GetNutrxFoods().execute();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (srchv_nutrx.getQuery().toString().isEmpty()){
                    foodsList.clear();
                    nutrxAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });


        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        nutrxAdapter = new NutrxAdapter(foodsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(nutrxAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                editor.putBoolean(MainActivity.FROM_NUTRX, true).apply();
                FoodItem foodItem = foodsList.get(position);
                ((MainActivity)getActivity()).show_FoodViewFragment(foodItem);
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.root_layout).setPadding(0,0,0,0);
        srchv_nutrx.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.root_layout).setPadding(16, 16, 16, 16); //back to regular padding for everything else
    }

    protected String prepareSearchQuery(String theSearchQuery){
            return theSearchQuery.replace(" ", "%20").trim();
            }



    public class GetNutrxFoods extends AsyncTask<String, Integer, String> {
        protected SharedPreferences mainPrefs;
        protected SharedPreferences.Editor editor;
        protected int ApiIdIndex;
        protected int ApiKeyIndex;
        String APPLICATION_ID;
        String APPLICATION_KEY;
        String rawJSON;

        protected GetNutrxFoods(){
            mainPrefs = ((MainActivity)getActivity()).getPreferences(MainActivity.MODE_PRIVATE);
            editor = mainPrefs.edit();

            ApiIdIndex = mainPrefs.getInt("ApiIdIndex", 0);
            ApiKeyIndex = mainPrefs.getInt("ApiKeyIndex", 0);

            APPLICATION_ID = Authorization.APPLICATION_IDS[ApiIdIndex];
            APPLICATION_KEY = Authorization.APPLICATION_KEYS[ApiIdIndex][ApiKeyIndex];

            //Log.d(TAG, "ApiIdIndex: " + ApiIdIndex + ", API_Id: " + APPLICATION_ID);
            //Log.d(TAG, "ApiKeyIndex: " + ApiKeyIndex + ", API_Key: " + APPLICATION_KEY);

            if (ApiKeyIndex == 4) {
                ApiIdIndex = ApiIdIndex == 0 ? 1 : 0;
            }
            ApiKeyIndex = ApiKeyIndex != 4 ?  ApiKeyIndex + 1 : 0;

            editor.putInt("ApiIdIndex", ApiIdIndex)
                    .putInt("ApiKeyIndex", ApiKeyIndex)
                    .apply();

            rawJSON = "";
        }




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            //https://guides.codepath.com/android/Handling-ProgressBars
        }

        @Override
        protected String doInBackground(String... params) {
            int count = 0;
            while (count < 5) {
                SystemClock.sleep(1000);
                count++;
                publishProgress(count * 20);
            }

            try {
                String theUrl = "https://" +
                        "api.nutritionix.com/v1_1/search/" +
                        theSearchQuery + "?" +
                        "results=0" + COLON + "50" +
                        "&cal_min=0&cal_max=50000" +
                        "&fields=" +
                        ITEM_NAME + COMMA + BRAND_NAME + COMMA + ITEM_ID + COMMA + BRAND_ID + COMMA + NF_CALORIES + COMMA +
                        NF_CALORIES_FROM_FAT + COMMA + NF_TOTAL_FAT + COMMA + NF_SATURATED_FAT + COMMA + NF_TRANS_FATTY_ACID + COMMA +
                        NF_CHOLESTEROL + COMMA + NF_SODIUM + COMMA + NF_TOTAL_CARBOHYDRATE + COMMA + NF_DIETARY_FIBER + COMMA +
                        NF_SUGARS + COMMA + NF_PROTEIN + COMMA + NF_SERVING_SIZE_QTY + COMMA + NF_SERVING_SIZE_UNIT + COMMA + NF_SERVING_WEIGHT_GRAMS +
                        "&appId=" + APPLICATION_ID + "&appKey=" + APPLICATION_KEY;
                URL url = new URL(theUrl);
                //Log.d(TAG, "the URL: " + theUrl);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                //connection.setRequestProperty("Authorization", "Bearer " + AUTH_TOKEN);   //???
                connection.connect();
                int status = connection.getResponseCode();
                switch(status){
                    case 200://200 indicates success
                        BufferedReader bufferedReader =
                                new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        rawJSON = bufferedReader.readLine();
                        int index_firstBracket = rawJSON.indexOf("[");
                        int index_lastBracket = rawJSON.lastIndexOf("]");
                        rawJSON = rawJSON.substring(index_firstBracket, index_lastBracket + 1);


                        //Log.d(TAG, "raw JSON ALL OF IT: " + rawJSON);
                        break;
                    default:
                        //Log.d(TAG, "doInBackground: Status = " + status);
                }
            }catch(MalformedURLException e){
                //Log.d(TAG, "doInBackground: " + e.getMessage());
            }catch(IOException e){
                //Log.d(TAG, "doInBackground: " + e.getMessage());
            }
            return rawJSON;
        }

        @Override
        protected void onPostExecute(String result) { //result is returned from doInBackground()
            super.onPostExecute(result);
            progressBar.setVisibility(ProgressBar.GONE);

            nutrxAdapter.clear();  //???   http://stackoverflow.com/questions/14286460/how-to-clear-arrayadapter-with-custom-listview

            try{
                FoodItemWithFields[] foodItemsWithFields = jsonParse(result);
                for (FoodItemWithFields f : foodItemsWithFields  ) {
                    foodsList.add(new FoodItem(f));
                }
                nutrxAdapter.notifyDataSetChanged();


                //Log.d(TAG, "foodsList.get(1).item_name: " + foodsList.get(1).item_name);
                //new GetAllCourses().execute("");

            }catch(Exception e){
                //Log.d(TAG, "onPostExecute: " + e.getMessage());
            }
        }


        private FoodItemWithFields[] jsonParse(String rawJSON){
            FoodItemWithFields[] foodItemWithFieldsArray = null;

            try{
                foodItemWithFieldsArray = gson.fromJson(rawJSON, FoodItemWithFields[].class);
                //Log.d(TAG, "jsonParse: Number of FoodItems returned: " + foodItemWithFieldsArray.length);
                //Log.d(TAG, "jsonParse: First FoodItem returned: " + foodItemWithFieldsArray[0].fields.item_name);
            }catch(Exception e){
                //Log.d(TAG, "jsonParse: " + e.getMessage());
            }

            return foodItemWithFieldsArray;
        }


            String SPACE = "%20";
            String COLON = "%3A";
            String COMMA = "%2C";

            String BRAND_NAME = "brand_name";
            String ITEM_NAME = "item_name";
            String ITEM_ID = "item_id";
            String BRAND_ID = "brand_id";
            String NF_CALORIES = "nf_calories";
            String NF_CALORIES_FROM_FAT = "nf_calories_from_fat";
            String NF_TOTAL_FAT = "nf_total_fat";
            String NF_SATURATED_FAT = "nf_saturated_fat";
            String NF_TRANS_FATTY_ACID = "nf_trans_fatty_acid";
            String NF_CHOLESTEROL = "nf_cholesterol";
            String NF_SODIUM = "nf_sodium";
            String NF_TOTAL_CARBOHYDRATE = "nf_total_carbohydrate";
            String NF_DIETARY_FIBER = "nf_dietary_fiber";
            String NF_SUGARS = "nf_sugars";
            String NF_PROTEIN = "nf_protein";
            String NF_SERVING_SIZE_QTY = "nf_serving_size_qty";
            String NF_SERVING_SIZE_UNIT = "nf_serving_size_unit";
            String NF_SERVING_WEIGHT_GRAMS = "nf_serving_weight_grams";
    }


    //the following code is adapted from www.androidhive.info/2016/01/android-working-with-recycler-view/
    //interface ClickListener and class RecyclerTouchListener

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));//use getChildAdapterPosition(View) or getChildLayoutPosition(View).
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));//use getChildAdapterPosition(View) or getChildLayoutPosition(View).
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



}
