package com.example.notes.note;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import com.example.notes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddNote extends AppCompatActivity {
    FirebaseFirestore fStore;
    EditText noteTitle,noteContent;
    ProgressBar progressBar;
    FirebaseUser user;
    ImageView mic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
progressBar=findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        user=FirebaseAuth.getInstance().getCurrentUser();
        mic=findViewById(R.id.mic);


        fStore=FirebaseFirestore.getInstance();
        noteContent=findViewById(R.id.edittextcontent);
        noteTitle=findViewById(R.id.addNoteTitle);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak NOW");
                try {
                    startActivityForResult(intent, 1);
                }
                catch (ActivityNotFoundException e){
                    Toast.makeText(AddNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String mTitle=noteTitle.getText().toString();
               String mContent=noteContent.getText().toString();
               if(mTitle.isEmpty()||mContent.isEmpty()){
                   Toast.makeText(AddNote.this, "Cannot save note with empty field", Toast.LENGTH_SHORT).show();
                   return;
               }
               else {
                   progressBar.setVisibility(View.VISIBLE);
                   DocumentReference docRef=fStore.collection("notes").document(user.getUid()).collection("myNotes").document();
                   Map<String,Object> note=new HashMap<>();
                   note.put("Title",mTitle);
                   note.put("content",mContent);
                   docRef.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(AddNote.this, "Note Added", Toast.LENGTH_SHORT).show();
                           onBackPressed();
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(AddNote.this, "Error Saving.!,Try Again", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.INVISIBLE);
                       }
                   });
               }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Closetab) {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK&&null!=data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    noteContent.setText(result.get(0));
                }

                break;
        }
    }
}