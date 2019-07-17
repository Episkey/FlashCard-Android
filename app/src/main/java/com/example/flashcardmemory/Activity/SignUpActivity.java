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
import android.widget.Toast;

import com.example.flashcardmemory.Model.User;
import com.example.flashcardmemory.R;
import com.example.flashcardmemory.Singleton.UserSingleton;
import com.example.flashcardmemory.Singleton.VolleySingleton;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView alreadyUser = findViewById(R.id.tvGoSignIn);
        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        final EditText etMail = findViewById(R.id.etMail);
        final EditText etpassword = findViewById(R.id.etPassword);

        Button btSingUp = findViewById(R.id.btSignUp);
        btSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etMail.getText().toString().isEmpty()
                        & !etpassword.getText().toString().isEmpty()) {
                    User user = new User();
                    user.setMail(etMail.getText().toString());
                    HashCode hashCode = Hashing.sha256().hashString(etpassword.getText().toString(), Charset.defaultCharset());
                    user.setPassword(hashCode.toString());
                    VolleySingleton.getInstance(getApplicationContext()).createUser(user, new Consumer<User>() {
                        @Override
                        public void accept(User user) {
                            UserSingleton.getInstance().setUser(user);
                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                            Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle(getString(R.string.please_fill));
                    builder.setPositiveButton(getString(R.string.ok), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
