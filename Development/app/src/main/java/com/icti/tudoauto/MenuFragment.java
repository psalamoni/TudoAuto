package com.icti.tudoauto;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
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

    private Button menufuel;
    private Button menumeasure;
    private Button menuefficiency;
    private OnCallActivityInteractionListener mCallActivityListener;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        startComps(view);
        eventClicks();

        return view;
    }

    private void eventClicks() {
        menumeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallActivityListener != null) {
                    Class measure = MeasureActivity.class;

                    mCallActivityListener.onCallActivityInteraction(measure);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.OnCallActivityInteractionListener) {
            mCallActivityListener = (MenuFragment.OnCallActivityInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateButtonFragmentInteractionListener");
        }
    }

    private void startComps(View view) {
        menufuel = (Button) view.findViewById(R.id.menu_fuel);
        menumeasure = (Button) view.findViewById(R.id.menu_measure);
        menuefficiency = (Button) view.findViewById(R.id.menu_efficiency);
    }

    public interface OnCallActivityInteractionListener {
        // TODO: Update argument type and name
        void onCallActivityInteraction(Class newClass);
    }

}
