package com.icti.tudoauto;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.icti.tudoauto.Model.Login;
import com.icti.tudoauto.Model.Register;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements RegisterFragment.OnCreateButtonFragmentInteractionListener, LoginFragment.OnChangeFragmentInteractionListener, RegisterFragment.OnChangeFragmentInteractionListener, LoginFragment.OnLoginInteractionListener {

    private FirebaseAuth mAuth;
    private Register register;
    DatabaseReference databaseReference;
    private LinearLayout loading;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting toolbar for the activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        //Initialize auth
        mAuth = FirebaseAuth.getInstance();

        //Make loading layout
        loading = (LinearLayout) findViewById(R.id.main_loadlay);
        Glide.with(this)
                .load(R.drawable.load) // aqui é teu gif
                .asGif()
                .into((ImageView) findViewById(R.id.main_load));

        //Setting the fragment for activity

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            LoginFragment login = new LoginFragment();
            login.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, login).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        startFirebase();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser==null) {
            return;
        }
    }

    private void startFirebase() {
        FirebaseApp.initializeApp(MainActivity.this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addInfoUser() {
        String usuarioId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        this.register.setID_user(usuarioId);
        databaseReference.child("userdata").child(register.getID_user()).child("userifo").setValue(register);
        this.register = null;
        alert("Usuario Cadastrado com sucesso");
        LoginFragment login = new LoginFragment();
        onChangeFragmentInteraction(login);
        processingout();
    }

    private void alert(String msg){
        Toast.makeText(MainActivity.this, msg,Toast.LENGTH_SHORT).show();
    }

    private void processingin() {
       loading.setVisibility(View.VISIBLE);
    }

    private void processingout() {
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onCreateButtonFragmentInteraction(Login login, Register register) {

        processingin();
        String email = login.getEmail();
        String password = login.getPassword();
        this.register = register;

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    addInfoUser();
                }else{
                    alert("Erro no Cadastro");
                    RegisterFragment register = new RegisterFragment();
                    processingout();
                    onChangeFragmentInteraction(register);
                }
            }
        });
    }

    @Override
    public void onChangeFragmentInteraction(Fragment newFragment) {

        processingin();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        processingout();

        transaction.commit();
    }

    @Override
    public void onLoginInteraction(Login login) {

        processingin();

        String email = login.getEmail();
        String password = login.getPassword();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(MainActivity.this, MenuActivity.class);
                            processingout();
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            alert("Email ou senha não conferem");
                            LoginFragment login = new LoginFragment();
                            processingout();
                            onChangeFragmentInteraction(login);
                        }

                        // ...
                    }
                });
    }
}
