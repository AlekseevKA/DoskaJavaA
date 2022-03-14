package com.example.doskajava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView nav_view;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private TextView userEmail;
    private AlertDialog dialog;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init()
    {
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        drawerLayout =findViewById(R.id.drawerLayout);

        toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.toggle_open, R.string.toggle_close);
        userEmail = nav_view.getHeaderView(0).findViewById(R.id.tvEmail);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserData();

    }

    public void onClickEdit(View view)
    {
        Intent i = new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);
    }

    private void getUserData()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            userEmail.setText(currentUser.getEmail());
        }
        else{
            userEmail.setText(R.string.sign_in_or_ign_up);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.id_my_ads:
                Toast.makeText(this, "Pressed id My Ads", Toast.LENGTH_SHORT).show();
                break;

            case R.id.id_cars_ads:
                Toast.makeText(this, "Pressed id cars", Toast.LENGTH_SHORT).show();
                break;


            case R.id.id_pc_ads:
                Toast.makeText(this, "Pressed id pc", Toast.LENGTH_SHORT).show();
                break;

            case R.id.id_my_smartphone_ads:
                Toast.makeText(this, "Pressed id smart", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_dm_ads:
                Toast.makeText(this, "Pressed id dm", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_up:
                signUpDialog(R.string.sign_up, R.string.sign_up_button, 0);
                Toast.makeText(this, "Pressed id sign up", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_in:
                signUpDialog(R.string.sign_in, R.string.sign_in_button, 1);
                Toast.makeText(this, "Pressed id sign in", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sign_out:
                signOut();
                Toast.makeText(this, "Pressed id sign out", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }

    private void signUp(String email, String password)
    {
        if(!email.equals("") && !password.equals("")) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                getUserData();


                            } else {
                                Log.w("MyLogMainActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentification failed",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Email or Password is empty!", Toast.LENGTH_SHORT).show();
        }

    }


    private void signUpDialog(int title, int buttonTitle, int index)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sign_up_layout, null);
        dialogBuilder.setView(dialogView);
        TextView titleTextView = dialogView.findViewById(R.id.tvAlertTitle);

        EditText edEmail = dialogView.findViewById(R.id.edEmail);
        EditText edPassword = dialogView.findViewById(R.id.edPassword);


        titleTextView.setText(title);
        Button b = dialogView.findViewById(R.id.buttonSignUp);
        b.setText(buttonTitle);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (index == 0)
            {
                signUp(edEmail.getText().toString(), edPassword.getText().toString());
            }
            else
                {
                signIn(edEmail.getText().toString(),edPassword.getText().toString());
            }
            dialog.dismiss();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();

    }
    private void signIn(String email, String password)
    {
        if(!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                           getUserData();

                            } else {
                                Log.w("MyLogMainActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Email or Password is empty!", Toast.LENGTH_SHORT).show();
        }
    }
    private void signOut()
    {
        mAuth.signOut();
        getUserData();
    }
}