package com.icti.tudoauto;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.icti.tudoauto.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity
        implements MenuFragment.OnCallActivityInteractionListener {

    private LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start loading Layout
        loading = (LinearLayout) findViewById(R.id.menu_loadlay);
        Glide.with(this)
                .load(R.drawable.load) // aqui é teu gif
                .asGif()
                .into((ImageView) findViewById(R.id.menu_load));
    }

    private void processingin() {
        loading.setVisibility(View.VISIBLE);
    }

    private void processingout() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onCallActivityInteraction(Class newClass) {

        processingin();
        Intent i = new Intent(MenuActivity.this, newClass);
        processingout();
        startActivity(i);
    }

}
