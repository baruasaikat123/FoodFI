package com.example.foodsi.ProjectFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsi.ProjectActivity.AdminDashboard;
import com.example.foodsi.ProjectClass.ReplacementFragment;
import com.example.foodsi.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class AdminLogin extends Fragment {

    View view;
    Button backToCategoryPage, loginBtn;
    FragmentManager fragmentManager;
    FirebaseAuth fAuth;
    TextView loginEmail, loginPassword;
    TextInputLayout loginEmailLayout, loginPasswordLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        //binding
        binding();

        //Initialize Firebase Auth
        fAuth = FirebaseAuth.getInstance();

        //Fragment Manager
        fragmentManager = getActivity().getSupportFragmentManager();

        //Back to Category Page
        backToCategoryPage();

        //Click on Login --> Navigate To Admin Dashboard
        adminLogin();

        return view;
    }

    private void binding(){
        backToCategoryPage = view.findViewById(R.id.back_to_category_btn);
        loginBtn = view.findViewById(R.id.login_btn);
        loginEmail = view.findViewById(R.id.login_email);
        loginPassword = view.findViewById(R.id.login_password);
        loginEmailLayout = view.findViewById(R.id.login_email_layout);
        loginPasswordLayout = view.findViewById(R.id.login_password_layout);
    }

    private void backToCategoryPage(){
        backToCategoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReplacementFragment(fragmentManager, new CategoryList(), R.id.main_activity_frame_layout);
            }
        });
    }

    private void adminLogin(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                loginPasswordLayout.setError("");
                loginEmailLayout.setError("");

                if (email.isEmpty()) {
                    loginEmailLayout.setError("Email is required");
                    return;
                }
                else if(!email.matches(emailPattern)){
                    loginEmailLayout.setError("Invalid email");
                    return;
                }

                if (password.isEmpty()) {
                    loginPasswordLayout.setError("Password is required");
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Validating ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        Context context = getContext();
                        CharSequence text = "Logged in successful";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                        Intent intent = new Intent(getContext(), AdminDashboard.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Context context = getContext();
                        CharSequence errorText = e.getMessage();
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, errorText, duration).show();
                    }
                });
            }
        });
    }
}