package com.chodos.gisela.mixedupmealcalculator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("MixedUp");
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onPause() {
        super.onPause();
        View root_layout = (View)getActivity().findViewById(R.id.root_layout);
        if (getResources().getConfiguration().orientation == 2){ //if landscape, set left padding to back to 85
            root_layout.setPadding(85, 21, 85, 21);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        View root_layout = (View)getActivity().findViewById(R.id.root_layout);

        if (getResources().getConfiguration().orientation == 2){ //if landscape, set left padding to zero
            root_layout.setPadding(0, 21, 85, 21);
        }else{
            root_layout.setPadding(21, 21, 21, 21);
        }

        //Log.d("padding", "mainActivity padding: " + root_layout.getPaddingLeft() +
        //", " + root_layout.getPaddingTop() + ", " + root_layout.getPaddingRight() + ", " + root_layout.getPaddingBottom());
        //Log.d("padding", "orientation: " + (getResources().getConfiguration().orientation == 2));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_attr, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //((MainActivity)getActivity()).show_AttributionsFragment();
        switch(item.getItemId()){
            case R.id.attributions:
                ((MainActivity)getActivity()).show_AttributionsFragment();
                break;
            case R.id.instructions:
                ((MainActivity)getActivity()).show_InstructionsFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
