package com.icti.tudoauto;


import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Measure;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EfficiencyFragment extends Fragment {

    Context context;
    private View view;

    public EfficiencyFragment() {
        // Required empty public constructor
    }

    public static EfficiencyFragment newInstance() {
        return new EfficiencyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_efficiency, container, false);

        startcomps(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {

        this.context = context;
        super.onAttach(context);
    }

    public void startcomps(View view) {

        List<Measure> means = Application.getMeans();

        List<TextView> effvalue = new ArrayList<TextView>();
        List<TextView> efftitlesvalue = new ArrayList<TextView>();
        List<Integer> effids = new ArrayList<Integer>();
        List<Integer> efftitleids = new ArrayList<Integer>();

        TypedArray effta = getResources().obtainTypedArray(R.array.eff);
        for (int i = 0; i < effta.length(); i++) {
            effids.add(effta.getResourceId(i, 0));
        }
        TypedArray efftitleta = getResources().obtainTypedArray(R.array.efftitle);
        for (int i = 0; i < efftitleta.length(); i++) {
            efftitleids.add(efftitleta.getResourceId(i, 0));
        }

        //implement fuel types available
        for (int i=0; i<means.size(); i++) {

            efftitlesvalue.add((TextView) view.findViewById(efftitleids.get(i)));
            effvalue.add((TextView) view.findViewById(effids.get(i)));

            if (means.get(i).getVolume() != 0) {

                efftitlesvalue.get(i).setVisibility(View.VISIBLE);
                effvalue.get(i).setVisibility(View.VISIBLE);
                effvalue.get(i).setText(String.format("%s Km/l", means.get(i).getMeasureavg()));

            }

        }
    }
}
