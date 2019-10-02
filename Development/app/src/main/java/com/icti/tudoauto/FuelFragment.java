package com.icti.tudoauto;


import android.content.Context;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Measure;
import com.icti.tudoauto.Model.Price;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FuelFragment extends Fragment {

    private Context context;
    private Button fuelcalculate;
    private Button fuelback;
    private OnCalculateListener mCalculateListener;
    private OnKillListener mKillListener;
    private EditText fuelprice;

    public FuelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fuel, container, false);

        startComps(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;

        if (context instanceof OnKillListener) {
            mKillListener = (OnKillListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnKillListener");
        }

        if (context instanceof OnCalculateListener) {
            mCalculateListener = (OnCalculateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddMeasureListener");
        }

        super.onAttach(context);
    }

    private void startComps(View view) {

        fuelcalculate = (Button) view.findViewById(R.id.fuel_calculate);
        fuelback = (Button) view.findViewById(R.id.fuel_back);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fuel_layout);

        //get mean values and fuel types ids
        List<Measure> means = Application.getMeans();
        int[] ids = getResources().getIntArray(R.array.id_arr);
        final List<Integer> usedids = new ArrayList<Integer>();

        //implement fuel types available
        for (int i=0; i<means.size(); i++) {

            if (means.get(i).getVolume() != 0) {
                usedids.add(i);

                String name = means.get(i).getFueltype();

                TextView newTextView = new TextView(context);
                newTextView.setText("Preço " + name);
                newTextView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                newTextView.setGravity(Gravity.CENTER);
                newTextView.setTypeface(newTextView.getTypeface(), Typeface.BOLD);

                linearLayout.addView(newTextView);

                EditText newEditText = new EditText(context);
                newEditText.setId(ids[i]);
                newEditText.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                newEditText.setGravity(Gravity.CENTER);
                newEditText.setHint("Insira o preço - " + name);
                newEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                linearLayout.addView(newEditText);
            }

        }

        // Implement buttons
        fuelback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mKillListener != null) {

                    mKillListener.onKill();
                }
            }
        });

        fuelcalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] ids = getResources().getIntArray(R.array.id_arr);
                List<Measure> means = Application.getMeans();
                Price efficiency = new Price();

                if (mCalculateListener != null) {
                    Price price = new Price();

                    for (int i:usedids) {
                        fuelprice = (EditText) view.findViewById(ids[i]);
                        price.setPricefuel(i, Float.parseFloat(fuelprice.getText().toString()));
                        efficiency.setPricefuel(i, means.get(i).getMeasureavg()/price.getPricefuel(i));
                    }

                    mCalculateListener.onCalculate(price, efficiency);
                }
            }
        });

    }

    public interface OnCalculateListener {
        // TODO: Update argument type and name
        void onCalculate(Price price, Price efficiency);
    }

    public interface OnKillListener {
        // TODO: Update argument type and name
        void onKill();
    }

}
