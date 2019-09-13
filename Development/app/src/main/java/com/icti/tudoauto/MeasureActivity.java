package com.icti.tudoauto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.icti.tudoauto.Model.Measure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Toast;

public class MeasureActivity extends AppCompatActivity implements MeasureFragment.OnKillListener, MeasureFragment.OnCofirmInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Shows initial instruction
        MeasureBegin();


    }

    private AlertDialog alert;

    private void MeasureBegin() {

        //Create Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.measAlertTitle);

        builder.setMessage(R.string.measAlertMessage);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MeasureActivity.this, "Cancelar=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MeasureActivity.this, "Cancelar=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });

        alert = builder.create();

        alert.show();
    }

    @Override
    public void onKill() {
        finish();
    }

    @Override
    public void onConfirmInteraction(Measure measure) {
        finish();
    }

}
