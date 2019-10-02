package com.icti.tudoauto;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.icti.tudoauto.Model.Application;
import com.icti.tudoauto.Model.Measure;
import com.icti.tudoauto.Model.Register;

import java.util.ArrayList;
import java.util.List;
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
        
        //Initialize auth
        mAuth = FirebaseAuth.getInstance();

        //Make loading layout
        loading = (LinearLayout) findViewById(R.id.loadlay);
        Glide.with(this)
                .load(R.drawable.load) // aqui é teu gif
                .asGif()
                .into((ImageView) findViewById(R.id.loadgif));

        //Setting the fragment for activity
        if (findViewById(R.id.main_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            LoginFragment login = new LoginFragment();
            login.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.main_container, login).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        startFirebase();
        updateUI(currentUser);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Verify logged user
        if (Application.VerifyNoLogin(this)==false) {
            loginSuccessfull();
        }
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
        databaseReference.child("userdata").child(register.getID_user()).child("userinfo").setValue(register);
        this.register = null;
        alert("Usuario Cadastrado com sucesso");
        LoginFragment login = new LoginFragment();
        onChangeFragmentInteraction(login);
        processingout();
    }

    private void alert(String msg){
        Toast.makeText(MainActivity.this, msg,Toast.LENGTH_SHORT).show();
    }

    private void loginSuccessfull(){
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(i);
        Application.getUserData(this);
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
        transaction.replace(R.id.main_container, newFragment);
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
                            loginSuccessfull();
                            processingout();
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
