package com.example.flashcardmemory.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashcardmemory.Model.Flashcard;
import com.example.flashcardmemory.Model.User;
import com.example.flashcardmemory.R;
import com.example.flashcardmemory.Singleton.UserSingleton;
import com.example.flashcardmemory.Singleton.VolleySingleton;

public class CreateFlashcardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard);

        UserSingleton userSingleton = UserSingleton.getInstance();
        User user = userSingleton.getUser();
        final Long userId = user.getIdUser();

        final EditText tvTitleFc = findViewById(R.id.tvTitleFlashcard);
        final RadioButton rbGeneral = findViewById(R.id.rbText);
        final RadioButton rbCode = findViewById(R.id.rbCode);
        final EditText etQuestion = findViewById(R.id.etQuestion);
        final EditText etAnswer = findViewById(R.id.etAnswer);
        Button btCreateFc = findViewById(R.id.btCreateFlashcard);

        btCreateFc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvTitleFc.getText().toString().isEmpty()
                && !etQuestion.getText().toString().isEmpty()
                && !etAnswer.getText().toString().isEmpty()
                && rbGeneral.isChecked()
                || !tvTitleFc.getText().toString().isEmpty()
                && !etQuestion.getText().toString().isEmpty()
                && !etAnswer.getText().toString().isEmpty()
                && rbCode.isChecked() ) {

                    Flashcard flashcard = new Flashcard();
                    flashcard.setName(tvTitleFc.getText().toString());
                    flashcard.setQuestion(etQuestion.getText().toString());
                    flashcard.setAnswer(etAnswer.getText().toString());
                    if (rbGeneral.isChecked()) {
                        flashcard.setGeneral(true);
                        flashcard.setCode(false);
                        rbCode.setChecked(false);
                    }
                    if (rbCode.isChecked()) {
                        flashcard.setCode(true);
                        flashcard.setGeneral(false);
                        rbCode.setChecked(false);
                    }
                    VolleySingleton.getInstance(getApplicationContext()).createFlashcard(flashcard, userId, new Consumer<Flashcard>() {
                        @Override
                        public void accept(Flashcard flashcard) {
                            Toast.makeText(CreateFlashcardActivity.this, getString(R.string.flcreated), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateFlashcardActivity.this, ListFlashcardActivity.class));
                        }
                    });
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateFlashcardActivity.this);
                    builder.setMessage(getString(R.string.please_fill));
                    builder.setPositiveButton(getString(R.string.ok), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
