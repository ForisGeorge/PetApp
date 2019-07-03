package com.example.petapp2.Pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petapp2.MainActivity;
import com.example.petapp2.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PetForm extends AppCompatActivity implements View.OnClickListener {

    private String sex,Url;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseUser user;

    public static final int PICK_IMAGE =1;
    private ImageView photoForm;
    private Button addPetPhoto;
    private Button addPet;
    private TextInputEditText name;
    private TextInputEditText race;
    private EditText birthday;
    private Switch gender;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);
        storageReference = FirebaseStorage.getInstance().getReference("users");
        database = FirebaseDatabase.getInstance().getReference("users");
        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        name = findViewById(R.id.nameForm);
        race = findViewById(R.id.raceForm);
        gender= findViewById(R.id.genderForm);
        birthday = findViewById(R.id.birthdayForm);
        photoForm = findViewById(R.id.photoForm);
        addPetPhoto = findViewById(R.id.addPetPhoto);
        addPet = findViewById(R.id.addPet);

        addPetPhoto.setOnClickListener(this);
        addPet.setOnClickListener(this);
        gender.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){
                selectedImage = data.getData();
                if(selectedImage != null)
                {
                    Glide.with(PetForm.this).load(selectedImage).into(photoForm);

                }
                else
                {
                    Toast.makeText(this,"Couldn`t load image",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static boolean checkPermission (Activity activity, String permission){
        List<String> listPermissionNeeded = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(permission);

        if(!listPermissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(activity,listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),10);
            return false;
        }
        return true;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();

        if (i == addPetPhoto.getId()) {
            checkPermission(PetForm.this, Manifest.permission.READ_EXTERNAL_STORAGE);

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selectaza o imagine"), PICK_IMAGE);
        }

        if (i == gender.getId()) {
            if (gender.isChecked()) {
                gender.setText("Male ");
                sex = "Male";
            } else {
                gender.setText("Female");
                sex = "Female";
            }
        }

        if (i == addPet.getId()) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setLenient(false);
            try {
                format.parse(birthday.getText().toString().trim());
            } catch (ParseException ex) {
                Toast.makeText(this, "Invalid date format!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(name.getText().toString().trim().equals("")){
                name.setError("Insert Name");
                return;}
            if(race.getText().toString().trim().equals("")){
                race.setError("Insert race");
                return;}
            if(photoForm.getDrawable() == null){
                Toast.makeText(this,"Select a Image",Toast.LENGTH_SHORT).show();
                return;
            }

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(selectedImage));


            fileReference.putFile(selectedImage);

            UploadTask uploadTask = fileReference.putFile(selectedImage);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Url = task.getResult().toString();
                        addPetComplete();
                    } else {


                    }
                }
            });

        }


    }

    public void addPetComplete (){
        Pets Pet = new Pets(name.getText().toString(), race.getText().toString(), sex, birthday.getText().toString().trim(),Url);
        database.child(user.getUid()).child("Pets").child(Pet.getName()).setValue(Pet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(PetForm.this,"Pet created!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PetForm.this,MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PetForm.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }});
    }

}
