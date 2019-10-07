package com.icti.tudoauto;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.icti.tudoauto.Model.Measure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.icti.tudoauto.Model.Application;

import java.util.List;

public class MeasureActivity extends AppCompatActivity implements MeasureFragment.OnKillListener, MeasureFragment.OnConfirmListener, FMeasureFragment.OnFragmentInteractionListener {

    private AlertDialog alert;
    private LinearLayout loading;
    private Context context = this;
    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting the fragment for activity
        Measure imeasure = Application.getImeasure();
        if (imeasure == null) {

            MeasureFragment measureFragment = new MeasureFragment();
            measureFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.measure_container, measureFragment).commit();

            //Shows initial instruction
            MeasureBegin();
        } else {

            FMeasureFragment fmeasureFragment = new FMeasureFragment();
            fmeasureFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.measure_container, fmeasureFragment).commit();

            //Shows initial instruction
            FMeasureBegin();
        }

        //Make loading layout
        loading = (LinearLayout) findViewById(R.id.loadlay);
        Glide.with(this)
                .load(R.drawable.load) // aqui é teu gif
                .asGif()
                .into((ImageView) findViewById(R.id.loadgif));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Verify logged user
        if (Application.VerifyNoLogin(this)) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    private void MeasureBegin() {

        //Create Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.measAlertTitleBegin);

        builder.setMessage(getString(R.string.measAlertMessageBegin));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alert = builder.create();

        alert.show();
    }

    private void MeasureEnd() {

        //Create Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.measAlertTitleEnd);

        builder.setMessage(getString(R.string.measAlertMessageEnd));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                processingin();

                if (Application.CreateIMeasure(context)) {
                    alert("Combustível salvo com sucesso");

                    processingout();
                    onKill();
                };
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alert = builder.create();

        alert.show();
    }

    private void FMeasureBegin() {

        //Create Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.fmeasAlertTitleBegin);

        builder.setMessage(getString(R.string.fmeasAlertMessageBegin1) + " " + Application.getImeasure().getFueltype() + " " + getString(R.string.fmeasAlertMessageBegin2));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alert = builder.create();

        alert.show();
    }

    private void FMeasureEnd() {

        //Create Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.measAlertTitleEnd);

        builder.setMessage(getString(R.string.fmeasAlertMessageEnd) + " " + Application.getImeasure().getFueltype() + ".\n\n" + Float.toString(Application.getImeasure().getMeasureavg()) + " km/l");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                processingin();

                Application.getMeasures().add(Application.getImeasure());
                Application.setImeasure(null);

                if (Application.CreateMeasures(context)) {
                    alert("Medição salva com sucesso");

                    processingout();
                    onKill();
                };

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alert = builder.create();

        alert.show();
    }

    private void processingin() {
        loading.setVisibility(View.VISIBLE);
    }

    private void processingout() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onKill() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConfirm(Measure measure) {
        Application application = new Application();

        application.setMbaseContext(getBaseContext());
        if (application.getPosition() != null) {
            measure.setPosition(application.getPosition());

            Application.setImeasure(measure);

        } else {
            processingout();
            requestPermissions(LOCATION_PERMS, INITIAL_REQUEST);
        }
        MeasureEnd();
    }

    @Override
    public void onAddMeasure(Measure measure) {
        Application.setImeasure(measure);
        FMeasureEnd();
    }

    @Override
    public void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
