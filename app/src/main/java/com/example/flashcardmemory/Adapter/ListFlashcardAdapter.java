package com.example.flashcardmemory.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.flashcardmemory.Activity.FlipcardViewActivity;
import com.example.flashcardmemory.Model.Flashcard;
import com.example.flashcardmemory.R;

import java.util.List;

public class ListFlashcardAdapter extends RecyclerView.Adapter<ListFlashcardAdapter.IdviewHolder> {

    private List<Flashcard> listFlashcard;

    public ListFlashcardAdapter(List<Flashcard> listFlashcard ) {
        this.listFlashcard = listFlashcard;
    }

    @Override
    public ListFlashcardAdapter.IdviewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_list_item, parent, false);
        IdviewHolder idviewHolder = new IdviewHolder(view);
        return idviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final IdviewHolder idviewHolder, final int i) {
        idviewHolder.title.setText(listFlashcard.get(i).getName());
        idviewHolder.btModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ENvoyer vers modification flashcard
            }
        });
        idviewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: supprimer la flashcard dans la bd et dans la liste.
            }
        });
        idviewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make intent for going to activity.
                Intent intent = new Intent (v.getContext(), FlipcardViewActivity.class);
                intent.putExtra("flashcardId", listFlashcard.get(i).getIdFlashcard());
                v.getContext().startActivity(intent);
            }
        });
        if (listFlashcard.get(i).isLearned()) {
            idviewHolder.tvIsLearned.setText("Learned");
            idviewHolder.tvIsLearned.setVisibility(View.VISIBLE);
        }
        else {
            idviewHolder.tvIsLearned.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listFlashcard.size();
    }

    public static class IdviewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageButton btModify;
        public ImageButton btDelete;
        public View container;
        public TextView tvIsLearned;


        public IdviewHolder(View v) {
            super(v);
            container = v;
            title = v.findViewById(R.id.tvFlashCardName);
            btModify = v.findViewById(R.id.btEdit);
            btDelete = v.findViewById(R.id.btDelete);
            tvIsLearned = v.findViewById(R.id.tvIsLearned);
        }
    }
}
