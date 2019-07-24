package com.example.flashcardmemory.Activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcardmemory.Model.Flashcard;
import com.example.flashcardmemory.R;
import com.example.flashcardmemory.Singleton.VolleySingleton;

public class FlipcardViewActivity extends AppCompatActivity {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flipcard_view);

        Bundle bdl = getIntent().getExtras();
        final Long idFlashcard = bdl.getLong("flashcardId");
        final Flashcard flashcardToUp = new Flashcard();

        VolleySingleton.getInstance(getApplicationContext()).getFlashcardById(idFlashcard, new Consumer<Flashcard>() {
            @Override
            public void accept(Flashcard flashcard) {
                TextView frontQuestion = findViewById(R.id.tvQuestionFc);
                TextView backAnswer = findViewById(R.id.tvAnswerFc);
                frontQuestion.setText(flashcard.getQuestion());
                backAnswer.setText(flashcard.getAnswer());
                findViews();
                loadAnimations();
                changeCameraDistance();
            }
        });



        Button btLearned = findViewById(R.id.btLearned);
        btLearned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleySingleton.getInstance(getApplicationContext()).updateFlashcard(idFlashcard, flashcardToUp, new Consumer<Flashcard>() {
                    @Override
                    public void accept(Flashcard flashcard) {
                        flashcardToUp.setLearned(true);
                        Toast.makeText(FlipcardViewActivity.this, "Flashcard Learned !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FlipcardViewActivity.this, ListFlashcardActivity.class));
                    }
                });

            }
        });
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    private void findViews() {
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
    }

    public void flipCard(View view) {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mIsBackVisible = false;
        }
    }
}
