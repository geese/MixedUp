package com.chodos.gisela.mixedupmealcalculator;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedFoodsDialogFrag extends DialogFragment {
    public static final String TAG = "debugSelectedFoods";
    //TextView txv_noneSelected;
    Button btn_done;
    ListView lsv_selectedFoods;
    ArrayAdapter<String> selectedFoodsAdapter;
    FoodListFragment foodListFragment;
    ArrayList<String> selectedFoodsArrayList;

    public SelectedFoodsDialogFrag() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_selected_foods_dialog, container, false);
        foodListFragment = (FoodListFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag(MainActivity.FOOD_LIST_FRAG_TAG);
        //txv_noneSelected = (TextView)rootView.findViewById(R.id.txv_none_selected);
        btn_done = (Button)rootView.findViewById(R.id.btn_done);
        //selectedFoodsArrayList = savedInstanceState.getStringArrayList(foodListFragment.SELECTED_FOODS_BUNDLE_KEY);
        Bundle bundle = getArguments();
        if (bundle != null)
            selectedFoodsArrayList = bundle.getStringArrayList("SELECTED_FOODS_LIST");
        //selectedFoodsArrayList = foodListFragment.getSelectedFoodsList();
        if (selectedFoodsArrayList != null) {
            Collections.sort(selectedFoodsArrayList, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareToIgnoreCase(rhs);
                }
            });
        }

        lsv_selectedFoods = (ListView)rootView.findViewById(R.id.lsv_selected_foods);
        selectedFoodsAdapter = new ListWithDeleteButton_ArrayAdapter();
        lsv_selectedFoods.setAdapter(selectedFoodsAdapter);


        if (selectedFoodsArrayList.isEmpty()){
            getDialog().setTitle("No Food Items Selected");
        }


        btn_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getDialog() != null) {
                    getDialog().cancel();
                }
            }
        });
        return rootView;
    }
//(getActivity(), R.layout.listview_show_selected_foods, selectedFoodsArrayList);


    protected void deselectFoodItem(String food){
        selectedFoodsArrayList.remove(food);
        selectedFoodsAdapter.notifyDataSetChanged();
        foodListFragment.deselectFoodItem(food);
        if (selectedFoodsArrayList.isEmpty()){
            getDialog().setTitle("No Food Items Selected");
        }

    }


    class ListWithDeleteButton_ArrayAdapter extends ArrayAdapter<String>{

        public ListWithDeleteButton_ArrayAdapter() {
            super(getActivity(), R.layout.listview_show_selected_foods, R.id.the_selected_food, selectedFoodsArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);
            TextView txv_selectedFood = (TextView)row.findViewById(R.id.the_selected_food);
            final String theSelectedFood = selectedFoodsArrayList.get(position);
            ImageButton deleteButton = (ImageButton)row.findViewById(R.id.btn_X);
            txv_selectedFood.setText(theSelectedFood);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deselectFoodItem(theSelectedFood);
                }
            });

            return row;
        }
    }
}

/*   @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        form = getActivity().getLayoutInflater()
                        .inflate(R.layout.fragment_selected_foods_dialog, null);

        foodListFragment = (FoodListFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag(MainActivity.FOOD_LIST_FRAG_TAG);
        txv_noneSelected = (TextView) form.findViewById(R.id.txv_none_selected);
        selectedFoodsArrayList = foodListFragment.getSelectedFoodsList();
        lsv_selectedFoods = (ListView)form.findViewById(R.id.lsv_selected_foods);
        selectedFoodsAdapter = new ListWithDeleteButton_ArrayAdapter();
        lsv_selectedFoods.setAdapter(selectedFoodsAdapter);

        if (selectedFoodsArrayList.isEmpty()){
            txv_noneSelected.setText("No Food Items Selected");
        }

        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        dialog = builder
                .setView(form)
                .setCancelable(true)
                        .setPositiveButton("Done", null)
                        .create();
        return dialog;
    }*/