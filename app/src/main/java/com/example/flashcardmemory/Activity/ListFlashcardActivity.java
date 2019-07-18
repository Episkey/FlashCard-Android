package com.example.flashcardmemory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.flashcardmemory.Adapter.ListFlashcardAdapter;
import com.example.flashcardmemory.Model.Flashcard;
import com.example.flashcardmemory.R;
import com.example.flashcardmemory.Singleton.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class ListFlashcardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_flashcard);

        FloatingActionButton fbCreateFlashcard = findViewById(R.id.fbAddFlashcard);
        fbCreateFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListFlashcardActivity.this, CreateFlashcardActivity.class));
            }
        });

        final RecyclerView rvHome = findViewById(R.id.rvList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvHome.setLayoutManager(layoutManager);
        VolleySingleton.getInstance(getApplicationContext()).getFlashcard(new Consumer<List<Flashcard>>() {
            @Override
            public void accept(List<Flashcard> flashcards) {
                List<Flashcard> flashcardPublished = new ArrayList<>();
                final ListFlashcardAdapter adapter = new ListFlashcardAdapter(flashcardPublished);
                rvHome.setAdapter(adapter);
            }
        });
    }
}

