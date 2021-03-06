package com.icti.tudoauto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Position;
import com.icti.tudoauto.Model.Price;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class FuelActivity extends AppCompatActivity implements FuelFragment.OnCalculateListener, FuelFragment.OnKillListener {

    private AlertDialog alert;
    private LinearLayout loading;
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
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

        builder.setMessage(getString(R.string.fuelAlertMessageEnd) + "\n\n" + fuelTypeList.get(idmax) + "\n" + efficiency.getPricefuel(idmax) + " Kilometros por Real");

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



    @RequiresApi(api = Build.VERSION_CODES.M)
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
        if (application.getPosition() != null) {
            price.setPosition(application.getPosition());
            price.setTimestamp(System.currentTimeMillis());

            Application.setPrice(price);

            if (Application.CreatePrices(this)) {
                processingout();
                FuelEnd(fuelTypeList, efficiency, idmax);
            }
            ;
        } else {
            processingout();
            requestPermissions(LOCATION_PERMS, INITIAL_REQUEST);
        }
    }

    @Override
    public void onKill() {
        finish();
    }
}
