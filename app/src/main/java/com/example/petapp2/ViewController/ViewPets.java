package com.example.petapp2.ViewController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.petapp2.MainActivity;
import com.example.petapp2.Pets.Pets;
import com.example.petapp2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewPets extends AppCompatActivity implements CardViewAdapter.OnClickListener {

    private FirebaseStorage storage;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private CardViewAdapter cardViewAdapter;
    private ValueEventListener dbListener;
    private DatabaseReference db;
    private List<Pets> pets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pets);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        pets = new ArrayList<>();

        cardViewAdapter = new CardViewAdapter(ViewPets.this,pets);
        recyclerView.setAdapter(cardViewAdapter);

        cardViewAdapter.setOnItemClickListener(ViewPets.this);

        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("Pets");

        dbListener = db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pets.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Pets pet = snapshot.getValue(Pets.class);
                    pet.setKey(snapshot.getKey());
                    pets.add(pet);
                }
                cardViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewPets.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(ViewPets.this,"Hold click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectItemClick(int position) {
        Pets selectedPet = pets.get(position);
        Intent intent = new Intent(ViewPets.this, MainActivity.class);
        intent.putExtra("Pet",selectedPet);
        startActivity(intent);
    }

    @Override
    public void onDeleteItemClick(int position) {
        //Toast.makeText(ViewPets.this,"DELETE", Toast.LENGTH_SHORT).show();
        final Pets selectedPet = pets.get(position);
        final String selectedKey = selectedPet.getKey();

        StorageReference imageRef = storage.getReferenceFromUrl(selectedPet.getUri());


        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.child(selectedKey).removeValue();
                Toast.makeText(ViewPets.this,"Pet deleted!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewPets.this,"Failed to delete!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.removeEventListener(dbListener);
    }
}
