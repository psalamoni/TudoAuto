package com.icti.tudoauto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

public class MeasureActivity extends AppCompatActivity {

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Antes de Inserir o Combustível:");

        builder.setMessage("Zere o odômetro (medidos de km), encha o tanque e insira o combustível que deseja a medição.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MeasureActivity.this, "Ok=" + arg1, Toast.LENGTH_SHORT).show();
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

}
