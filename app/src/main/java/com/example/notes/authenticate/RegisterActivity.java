package com.example.notes.authenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.note.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;

public class RegisterActivity extends AppCompatActivity {
    EditText userName,userEmail,userPass,userconpass;
    Button syncAccount;
    TextView loginAct;
    ProgressBar progressBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Create New Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName=findViewById(R.id.regname);
        userEmail=findViewById(R.id.regemail);
        userPass=findViewById(R.id.regpassword);
        userconpass=findViewById(R.id.regconfirmpassword);
        syncAccount=findViewById(R.id.syncBtn);
        loginAct=findViewById(R.id.logintxt);
        progressBar=findViewById(R.id.regprogressBar);
        fAuth=FirebaseAuth.getInstance();
        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        syncAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username=userName.getText().toString();
                String useremail=userEmail.getText().toString();
                String userpass=userPass.getText().toString();
                String usercon=userconpass.getText().toString();
                if(username.isEmpty()||useremail.isEmpty()||userpass.isEmpty()||usercon.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                return;
                }
                if(!userpass.equals(usercon)){
                    userconpass.setError("Password do not match");
                }
                if(userpass.length()<6) {
                userPass.setError("Password length to be more than 6 characters long");
                }
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential= EmailAuthProvider.getCredential(useremail,userpass);
                fAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "Synced Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        FirebaseUser user=fAuth.getCurrentUser();
                        UserProfileChangeRequest request=new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        user.updateProfile(request);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Failed to connect.try again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}