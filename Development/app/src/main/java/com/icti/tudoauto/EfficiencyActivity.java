package com.icti.tudoauto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Measure;

import java.util.List;

public class EfficiencyActivity extends AppCompatActivity implements EfficiencyFragmentList.OnDeleteInteractionListener {

    private AlertDialog alert;
    private LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_efficiency);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting the fragment for activity
        EfficiencyFragment efficiencyFragment = new EfficiencyFragment();
        changeFragment(efficiencyFragment, R.id.efficiency_container);

        //Setting the fragment for activity
        EfficiencyFragmentList efficiencyFragmentList = new EfficiencyFragmentList();
        changeFragment(efficiencyFragmentList, R.id.measure_list);

        //Shows initial instruction
        EfficiencyBegin();

        //Make loading layout
        loading = (LinearLayout) findViewById(R.id.loadlay);
        Glide.with(this)
                .load(R.drawable.load) // aqui é teu gif
                .asGif()
                .into((ImageView) findViewById(R.id.loadgif));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    private void EfficiencyBegin() {

        //Create Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.fuelAlertTitleBegin);

        builder.setMessage(getString(R.string.fuelAlertMessageBegin));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                onKill();
            }
        });

        alert = builder.create();

        alert.show();
    }

    private void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void processingin() {
        loading.setVisibility(View.VISIBLE);
    }

    private void processingout() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteInteraction(int position) {

        processingin();
        List<Measure> measures = Application.getMeasures();

        measures.remove(position);

        Application.setMeasures(measures);

        Application.CreateMeasures(this);

        //Setting the fragment for activity
        EfficiencyFragment efficiencyFragment = new EfficiencyFragment();
        changeFragment(efficiencyFragment, R.id.efficiency_container);

        //Setting the fragment for activity
        EfficiencyFragmentList efficiencyFragmentList = new EfficiencyFragmentList();
        changeFragment(efficiencyFragmentList, R.id.measure_list);

        processingout();

    }

    private void changeFragment(Fragment newFragment, int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, newFragment);
        transaction.commit();
    }

    public void onKill() {
        finish();
    }

    public interface OnUpdatedMeasuresListener {
        void onDeletedMeasures();
    }
}
