package me.weyxin99.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private ParseUser user;
    private String username;
    private String password;
    private String email;
    private Button registerButton;
    //private EditText handleInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ParseUser.getCurrentUser().logOut();
        getSupportActionBar().hide();
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Log.d("SignUpActivity", "Successfully registered new user!");
                            login(username, password);
                        }
                        else {
                            Log.d("SignUpActivity", "Failed to register new user.");
                        }
                    }
                });
            }
        });
    }

    private void registerUser() {
        usernameInput = findViewById(R.id.signUpUsername);
        passwordInput = findViewById(R.id.signUpPassword);
        emailInput = findViewById(R.id.signUpEmail);
        //handleInput = findViewById(R.id.signUpHandle);
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        email = emailInput.getText().toString();
        user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        //user.put("handle", handleInput);
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.d("SignUpActivity", "Log in successful!");
                    final Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("SignUpActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}
