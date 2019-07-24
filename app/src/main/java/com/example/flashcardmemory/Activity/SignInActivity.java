package com.example.flashcardmemory.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.flashcardmemory.Model.Authentication;
import com.example.flashcardmemory.Model.User;
import com.example.flashcardmemory.R;
import com.example.flashcardmemory.Singleton.UserSingleton;
import com.example.flashcardmemory.Singleton.VolleySingleton;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

import static com.example.flashcardmemory.Singleton.VolleySingleton.ERROR_EMAIL;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView goSignUp = findViewById(R.id.tvGoSignUp);
        goSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        final EditText etMail = findViewById(R.id.etMail);
        final EditText etPassword = findViewById(R.id.etPassword);

        Button btSignIn = findViewById(R.id.btSignIn);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etMail.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
                    HashCode hashCode = Hashing.sha256().hashString(etPassword.getText().toString(), Charset.defaultCharset());
                    final User newUser = new User();
                    newUser.setMail(etMail.getText().toString());
                    newUser.setPassword(etPassword.getText().toString());
                    newUser.setPassword(hashCode.toString());
                    VolleySingleton.getInstance(getApplicationContext()).getUserByEmail(newUser, new Consumer<Authentication>() {
                        @Override
                        public void accept(Authentication authentication) {
                            if (authentication == null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                builder.setTitle(getString(R.string.fail_connexion));
                                builder.setMessage(getString(R.string.please_check));
                                builder.setPositiveButton(getString(R.string.ok), null);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else if (authentication.getError() != null) {
                                String error = getString(R.string.error_mail);
                                if (authentication.getError().equals(ERROR_EMAIL)) {
                                    error = getString(R.string.error_mail);
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                builder.setTitle(getString(R.string.error_mail));
                                builder.setMessage(error);
                                builder.setPositiveButton(getString(R.string.ok), null);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else if (authentication.getUser() == null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                builder.setTitle(getString(R.string.warning));
                                builder.setMessage(getString(R.string.no_account));
                                builder.setPositiveButton(getString(R.string.ok), null);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            } else {
                                UserSingleton.getInstance().setUser(authentication.getUser());
                                startActivity(new Intent(SignInActivity.this, ListFlashcardActivity.class));
                            }
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                    builder.setMessage(getString(R.string.please_fill));
                    builder.setPositiveButton(getString(R.string.ok), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
