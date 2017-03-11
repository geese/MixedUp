package com.chodos.gisela.mixedupmealcalculator;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmDeleteFoodDialog extends DialogFragment {


    public ConfirmDeleteFoodDialog() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final String foodName = bundle.getString("FOOD_NAME");
        //log.d("debugDeleteFood", "onCreateDialog: " + foodName);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog dialog =
                builder.setMessage("Are you sure you want to delete \n" + foodName + "?")
                        .setTitle("Confirm Delete")
                        .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.databaseHelper.deleteFoodItem(foodName);
                                ((MainActivity)getActivity()).clear_BackStack();
                                MainActivity.hideKeyboard(getContext());
                                ((MainActivity) getActivity()).show_FoodListFragment(true);
                            }
                        })
                        .setNeutralButton("No, Don't Delete", null)
                        .setCancelable(true)
                        .create();
        return dialog;
    }

}
