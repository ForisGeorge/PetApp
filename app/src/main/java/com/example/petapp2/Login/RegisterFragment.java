 package com.example.petapp2.Login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.petapp2.MainActivity;
import com.example.petapp2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;

 /**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

private TextInputEditText registerUsername;
private TextInputEditText registerPassword;
private Button finalRegisterBtn;
private FirebaseAuth firebaseAuth;



    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        finalRegisterBtn = view.findViewById(R.id.finalRegisterBtn);
        finalRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        registerUsername = view.findViewById(R.id.registerUsername);
        registerPassword = view.findViewById(R.id.registerPassword);

        registerUsername.setOnClickListener(this);
        registerPassword.setOnClickListener(this);



    }


    private void registerUser(){
        String email = registerUsername.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(),"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 7){
            Toast.makeText(getActivity(),"Password requires at least 7 characters!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(),"Please enter password!",Toast.LENGTH_SHORT).show();
            return;
        }
            Toast.makeText(getActivity(),"Registering...",Toast.LENGTH_SHORT).show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Registration succesfull!",Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_login_main,new LoginFragment()).commit();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(),"Registration unsuccesfull!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
     @Override
     public void onClick(View view) {


     }
 }


