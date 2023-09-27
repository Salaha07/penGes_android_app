package com.example.pengesnotesapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreatAccountActivity extends AppCompatActivity {
    EditText emailEditText,passwordEditText,confirmPswdEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_account);

        emailEditText=findViewById(R.id.email_edit_text);
        passwordEditText=findViewById(R.id.pswd_edit_text);
        confirmPswdEditText=findViewById(R.id.confirm_pswd_edit_text);

        createAccountBtn=findViewById(R.id.create_acc_btn);
        progressBar=findViewById(R.id.progress_bar);
        loginBtn=findViewById(R.id.login_text_view_btn);

        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtn.setOnClickListener(v-> finish());

    }

    void createAccount() {
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String confirmPassword=confirmPswdEditText.getText().toString();

        boolean isValidated=validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);
    }

    void createAccountInFirebase(String email, String password) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreatAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                changeInProgress(false);
                if (task.isSuccessful()){
                    //create acc done
                    Utility.showToast(CreatAccountActivity.this, "Succesfully created account,Check email to verify");
                    //Toast.makeText(CreatAccountActivity.this, "Succesfully created account,Check email to verify", Toast.LENGTH_SHORT).show();
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else{
                    Utility.showToast(CreatAccountActivity.this, task.getException().getLocalizedMessage());
                    //Toast.makeText(CreatAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);

        }else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String password,String confirmPassword){
        //validating data that are input by user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Invalid Email id");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Invalid Password length");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPswdEditText.setError("Password not matching");
            return false;
        }
        return true;
    }
}