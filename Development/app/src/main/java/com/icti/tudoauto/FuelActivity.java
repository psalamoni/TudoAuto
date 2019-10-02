package com.icti.tudoauto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Price;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FuelActivity extends AppCompatActivity implements FuelFragment.OnCalculateListener, FuelFragment.OnKillListener {

    private AlertDialog alert;
    private LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting the fragment for activity
        FuelFragment fuelFragment = new FuelFragment();
        fuelFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.fuel_container, fuelFragment).commit();

        //Shows initial instruction
        FuelBegin();

        //Make loading layout
        loading = (LinearLayout) findViewById(R.id.loadlay);
        Glide.with(this)
                .load(R.drawable.load) // aqui é teu gif
                .asGif()
                .into((ImageView) findViewById(R.id.loadgif));
    }

    private void FuelBegin() {

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

    private void FuelEnd(List<String> fuelTypeList, Price efficiency, int idmax) {



        //Create Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.fuelAlertTitleEnd);

        builder.setMessage(getString(R.string.fuelAlertMessageEnd) + "\n\n" + fuelTypeList.get(idmax) + "\n" + efficiency.getPricefuel(idmax));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                onKill();
            }
        });

        builder.setNegativeButton("Ver Todos", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
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
    public void onCalculate(Price price, Price efficiency) {
        Application application = new Application();

        String[] fuelType = getResources().getStringArray(R.array.fueltype);
        List<String> fuelTypeList = Arrays.asList(fuelType);
        fuelTypeList = new ArrayList<String>(fuelTypeList);

        int idmax = 0;

        for (int i=0; i<fuelTypeList.size(); i++) {
            if (efficiency.getPricefuel(i)!=0) {
                if (efficiency.getPricefuel(i)>efficiency.getPricefuel(idmax)) {
                    idmax = i;
                }
            }
        }

        processingin();

        application.setMbaseContext(getBaseContext());
        price.setLocationPosition(application.getPosition());

        Application.setPrice(price);

        if (Application.CreatePrices(this)) {
            alert("Combustível salvo com sucesso");

            processingout();
            FuelEnd(fuelTypeList, efficiency, idmax);
        };
    }

    @Override
    public void onKill() {
        finish();
    }
}
