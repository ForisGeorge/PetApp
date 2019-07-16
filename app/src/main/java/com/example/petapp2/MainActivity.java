package com.example.petapp2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.petapp2.Login.LoginActivity;
import com.example.petapp2.Pets.PetForm;
import com.example.petapp2.Pets.Pets;
import com.example.petapp2.ViewController.ViewPets;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference db;

    FirebaseAuth auth;
    private ImageView petImage;
    private TextView nameView;
    private TextView raceView;
    private TextView birthdayView;
    private TextView ageView;
    private Button selectPet;
    private FloatingActionButton addPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        addPet = findViewById(R.id.addPet);
        petImage = findViewById(R.id.petImage);
        nameView = findViewById(R.id.nameView);
        raceView = findViewById(R.id.raceView);
        birthdayView = findViewById(R.id.birthdayView);
        ageView = findViewById(R.id.ageView);
        selectPet = findViewById(R.id.selectPet);

        db=FirebaseDatabase.getInstance().getReference();

        fillContent();
    }

    public void selectFromPets (View view){
        Intent intent = new Intent(this, ViewPets.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void addPetForm (View view){
        Intent intent = new Intent(this, PetForm.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signOut) {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fillContent(){
        Intent intent =getIntent();
        if(intent!=null){
            Pets pet = getIntent().getParcelableExtra("Pet");
            if(pet != null){
                nameView.setText(pet.getName());
                raceView.setText(pet.getRace());
                Glide.with(this).load(pet.getUri()).centerCrop().into(petImage);
                birthdayView.setText(pet.getBirthday());
                this.ageView.setText(ageCalculator(pet.getBirthday()));
            }
        }
    }

    private String ageCalculator (String birthday){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        try{
            calendar.setTime(format.parse(birthday));
        }catch (ParseException e){

        }

       Calendar today = Calendar.getInstance();
       long date = today.getTimeInMillis() - calendar.getTimeInMillis();
       Calendar age = Calendar.getInstance();
       age.setTimeInMillis(date);
       Integer years = age.YEAR;
       Integer months = age.MONTH;
       String ageString =  years.toString() + " years and " + months.toString() + " months ";
       return ageString;
    }

}