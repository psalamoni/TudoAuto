package com.icti.tudoauto;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.icti.tudoauto.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuFragment extends Fragment {

    private LinearLayout loading;
    private Button menufuel;
    private Button menumeasure;
    private Button menuefficiency;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        startComps(view);

        return view;
    }

    private void startComps(View view) {
        menufuel = (Button) view.findViewById(R.id.menu_fuel);
        menumeasure = (Button) view.findViewById(R.id.menu_measure);
        menuefficiency = (Button) view.findViewById(R.id.menu_efficiency);
    }

    private void processingin() {
        loading.setVisibility(View.VISIBLE);
    }

    private void processingout() {
        loading.setVisibility(View.GONE);
    }

}
