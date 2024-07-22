package com.example.demo;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
public class Splash_login extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private WebServiceManager webServiceManager;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextUsername = findViewById(R.id.Users);
        editTextPassword = findViewById(R.id.passwords);
        webServiceManager = new WebServiceManager(this);

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = editTextPassword.getSelectionEnd();
                        if (passwordVisible) {
                            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);
                            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        editTextPassword.setSelection(selection);
                    }
                }
                return false;
            }
        });
    }

    public void loginClicked(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("nombre", username);
        properties.put("password", password);

        webServiceManager.callWebService("AutenticarUsuario", properties, new WebServiceManager.WebServiceCallback() {
            @Override
            public void onWebServiceCallComplete(String result) {
                handleLoginResult(result);
            }
        });
    }

    private void handleLoginResult(String result) {
        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("id_Usuario")) {
                // Autenticación exitosa
                Intent intent = new Intent(Splash_login.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (jsonResult.has("error")) {
                // Error en la autenticación
                String errorMessage = jsonResult.getString("error");
                Toast.makeText(this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Login failed: Unexpected response", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Login failed: Error parsing response: " + result, Toast.LENGTH_LONG).show();
        }
    }
}
