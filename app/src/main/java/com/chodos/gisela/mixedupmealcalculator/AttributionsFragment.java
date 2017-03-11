package com.chodos.gisela.mixedupmealcalculator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttributionsFragment extends Fragment {

    Button btn_backFromAttributions;
    ImageButton img_btn_backFromAttributions;

    public AttributionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_attributions, container, false);

        btn_backFromAttributions = (Button) rootView.findViewById(R.id.btn_back_from_attributions);
        img_btn_backFromAttributions = (ImageButton) rootView.findViewById(R.id.img_btn_back_from_attributions);

        btn_backFromAttributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        img_btn_backFromAttributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

}
