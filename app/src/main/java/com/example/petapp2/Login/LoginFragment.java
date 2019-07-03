package com.example.petapp2.Login;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    private Button loginBtn;
    private Button registerBtn;
    private TextInputEditText usernameLogin;
    private TextInputEditText passwordLogin;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        loginBtn = view.findViewById(R.id.loginBtn);
        usernameLogin = view.findViewById(R.id.usernameLogin);
        passwordLogin = view.findViewById(R.id.passwordLogin);
        registerBtn = view.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_login_main,new RegisterFragment()).addToBackStack("loginbackstack").commit();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(usernameLogin.getText().toString(),passwordLogin.getText().toString());
            }
        });
    }



    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }



    private void signOut() {
        firebaseAuth.signOut();

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = usernameLogin.getText().toString();
        if (TextUtils.isEmpty(email)) {
            usernameLogin.setError("Required.");
            valid = false;
        } else {
            usernameLogin.setError(null);
        }

        String password = passwordLogin.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordLogin.setError("Required.");
            valid = false;
        } else {
            passwordLogin.setError(null);
        }

        return valid;
    }
}
