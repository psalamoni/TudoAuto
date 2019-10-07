package com.icti.tudoauto;

import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.icti.tudoauto.Model.Application;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity
        implements MenuFragment.OnCallActivityInteractionListener {

    private static LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.menutoolbar);
        setSupportActionBar(toolbar);

        //Setting the fragment for activity
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.menu_container, menuFragment).commit();

        //Start loading Layout
        loading = (LinearLayout) findViewById(R.id.loadlay);
        Glide.with(this)
                .load(R.drawable.load) // aqui é teu gif
                .asGif()
                .into((ImageView) findViewById(R.id.loadgif));

    }

    @Override
    protected void onResume(){
        super.onResume();

        //Start Service
        Intent intent = new Intent(this, MenuService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Verify logged user
        if (Application.VerifyNoLogin(this)) {
            finish();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Application.setImeasure(null);
            Application.setLogindata(null);
            Application.setMeasures(null);
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        //Kill Service
        Intent intent = new Intent(this, MenuService.class);

        super.onPause();
    }

    public void processingin() {
        loading.setVisibility(View.VISIBLE);
    }

    public static void processingout() {
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
