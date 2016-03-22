package com.home.reservas;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.home.reservas.fragment.LinkedInLoginFragment;



public class LoginActivity extends AppCompatActivity implements LinkedInLoginFragment.loginListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeActivity();
    }

    private void initializeActivity()
    {
        ImageButton btnLoginLinkedIn = (ImageButton) findViewById(R.id.btnLoginLinkedIn);
        ImageButton btnLoginGoogle = (ImageButton) findViewById(R.id.btnLoginGoogle);
        FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        btnLoginLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkedInLoginFragment fragment = new LinkedInLoginFragment();
                fragmentTransaction.add(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });


        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO conectar con login de Google
                Intent intent = new Intent(LoginActivity.this, ListaActivity.class);
                startActivity(intent);
            }
        });

    }

    /* interface callback */
    @Override
    public void onLogin() {
        Intent intent = new Intent(LoginActivity.this, ListaActivity.class);
        startActivity(intent);
    }
}

