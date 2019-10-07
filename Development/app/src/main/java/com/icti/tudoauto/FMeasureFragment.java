package com.icti.tudoauto;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Measure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class FMeasureFragment extends Fragment {

    private OnFragmentInteractionListener mFragmentInteractionListener;
    private EditText fueltotal;
    private EditText odometertotal;
    private Button fmeascreate;
    private Button fmeasback;
    private Context context;

    public FMeasureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fmeasure, container, false);

        if (Application.getImeasure()==null) {
            mFragmentInteractionListener.onKill();
        }

        startComps(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;

        if (context instanceof OnFragmentInteractionListener) {
            mFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnKillListener");
        }

        super.onAttach(context);
    }

    private void startComps(View view) {
        fueltotal = (EditText) view.findViewById(R.id.fmeas_fueltotal);
        odometertotal = (EditText) view.findViewById(R.id.fmeas_odometertotal);
        fmeascreate = (Button) view.findViewById(R.id.fmeas_create);
        fmeasback = (Button) view.findViewById(R.id.fmeas_back);

        // Implement buttons
        fmeasback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragmentInteractionListener != null) {

                    mFragmentInteractionListener.onKill();
                }
            }
        });
        fmeascreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] nullobj = {"","0"};
                if (Arrays.asList(nullobj).contains(fueltotal.getText().toString()) == false && Arrays.asList(nullobj).contains(odometertotal.getText().toString()) == false) {
                    if (mFragmentInteractionListener != null) {
                        Measure measure = Application.getImeasure();

                        long tslong = System.currentTimeMillis();
                        /* Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(tslong);

                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH); */

                        measure.setVolume(Float.parseFloat(fueltotal.getText().toString()));
                        measure.setDistance(Float.parseFloat(odometertotal.getText().toString()));
                        measure.setMeasureavg(measure.getDistance() / measure.getVolume());
                        measure.setTimestamp(tslong);

                        mFragmentInteractionListener.onAddMeasure(measure);
                    }
                } else {
                    mFragmentInteractionListener.alert("Insira o valor total abastecido e odômetro");
                }
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAddMeasure(Measure measure);
        void alert(String msg);
        void onKill();
    }

}
