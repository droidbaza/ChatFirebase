package com.droidbaza.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.droidbaza.chatapp.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       LoginFragment fragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.cont, fragment)
                .commit();

    }
}