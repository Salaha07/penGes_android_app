package com.example.pengesnotesapplication;

import static com.example.pengesnotesapplication.R.id.add_note_btn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;

    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn=findViewById(add_note_btn);
        recyclerView=findViewById(R.id.recycler_view);
        menuBtn=findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener(v-> startActivity(new Intent(MainActivity.this, NoteDetailesActivity.class)));
        menuBtn.setOnClickListener(v-> showMenu());
        setupRecyclerView();
    }

    void showMenu() {
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.getMenu().add("Settings");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    void setupRecyclerView() {
        Query query=Utility.getCollectionReferenceForeNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter=new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}