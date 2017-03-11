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
public class ConfirmDeleteMealDialog extends DialogFragment {


    public ConfirmDeleteMealDialog() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final int mealIndex = bundle.getInt("MEAL_INDEX");

        String mealName = MainActivity.databaseHelper.getMealName(mealIndex);
        //log.d("debugDeleteFood", "onCreateDialog: " + mealName);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Dialog dialog =
                builder.setMessage("Are you sure you want to delete \n" + mealName + "?")
                        .setTitle("Confirm Delete")
                        .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.databaseHelper.deleteMeal(mealIndex);
                                ((MainActivity)getActivity()).clear_BackStack();
                                MainActivity.hideKeyboard(getContext());
                                ((MainActivity)getActivity()).show_MealListFragment(true);//boolean itemDeleted
                            }
                        })
                        .setNeutralButton("No, Don't Delete", null)
                        .setCancelable(true)
                        .create();
        return dialog;
    }

}
