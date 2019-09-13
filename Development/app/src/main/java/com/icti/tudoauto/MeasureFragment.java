package com.icti.tudoauto;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.icti.tudoauto.Model.Measure;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MeasureFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Context context;
    private String fueltype;
    private Button meascreate;
    private Button measback;
    private OnCofirmInteractionListener mConfirmListener;
    private OnKillListener mKillListener;
    private Measure measure = new Measure();
    private FirebaseAuth mAuth;

    public MeasureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        loadIMeasureFromDb();

        //If a mesure is incomplete, the activity is suspended
        if (measure.getFueltype() != null){
            mKillListener.onKill();
        }


        View view = inflater.inflate(R.layout.fragment_measure, container, false);

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
                    + " must implement OnListFragmentInteractionListener");
        }

        super.onAttach(context);
    }

    private void startComps(View view) {
        spinner = view.findViewById(R.id.meas_fueltype);
        meascreate = (Button) view.findViewById(R.id.meas_create);
        measback = (Button) view.findViewById(R.id.meas_back);

        // Implement fuel Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.fueltype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Implement buttons
        measback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mKillListener != null) {

                    mKillListener.onKill();
                }
            }
        });
        meascreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConfirmListener != null) {
                    Measure measure = new Measure();

                    measure.setFueltype(fueltype);

                    mConfirmListener.onConfirmInteraction(measure);
                }
            }
        });


    }

    private void loadIMeasureFromDb() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        ValueEventListener dbListener = db.child("userdata").child(mAuth.getCurrentUser().getUid()).child("incompletemeasure").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    measure = objSnapshot.getValue(Measure.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                alert("Falha ao buscar as medições");
            }
        });
    }

    private void alert(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        fueltype = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnCofirmInteractionListener {
        // TODO: Update argument type and name
        void onConfirmInteraction(Measure measure);
    }

    public interface OnKillListener {
        // TODO: Update argument type and name
        void onKill();
    }


}
