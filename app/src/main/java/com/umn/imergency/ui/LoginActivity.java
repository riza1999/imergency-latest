package com.umn.imergency.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.functions.FirebaseFunctions;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Encryption;
import com.umn.imergency.helpers.Fetch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseFunctions mFunctions;

    private TextView textview_sign_up;
    private Button button_sign_in;
    private EditText edittext_no_handphone, edittext_password;
    private Spinner spinner_kode_negara;
    private ProgressBar progressbar_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFunctions = FirebaseFunctions.getInstance();

        textview_sign_up = findViewById(R.id.textview_sign_up);
        button_sign_in = findViewById(R.id.button_sign_in);
        edittext_no_handphone = findViewById(R.id.edittext_no_handphone);
        edittext_password = findViewById(R.id.edittext_password);
        spinner_kode_negara = findViewById(R.id.spinner_kode_negara);
        progressbar_sign_in = findViewById(R.id.progressbar_sign_in);

        // On Click Listener
        textview_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startNewActivity = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(startNewActivity);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBarSignIn();

                String no_handphone = spinner_kode_negara.getSelectedItem().toString() + edittext_no_handphone.getText().toString(), password = edittext_password.getText().toString();

                if(no_handphone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    hideProgressBarSignIn();
                } else {
                    // If the field is not empty do the query
                    Encryption encryption = new Encryption();

                    String hashed_password = encryption.md5(password);

                    Map<String, String> request_body = new HashMap<>();
                    request_body.put("no_handphone", no_handphone);
                    request_body.put("password", hashed_password);

                    String URL = "https://us-central1-imergency-latest.cloudfunctions.net/QUERY_LOGIN";
                    new Fetch(LoginActivity.this, "POST", URL, request_body,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        boolean success = response.getBoolean("success");
                                        String message = response.getString("message");

                                        if(success) {
                                            // TODO: Shared preference and intent
                                            Log.d(">>>>", "BERHASIL");
                                        } else {
                                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                        hideProgressBarSignIn();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    hideProgressBarSignIn();
                                }
                            });
                }
            }
        });
    }

    private void showProgressBarSignIn() {
        // Show the progress bar and hide the login button
        progressbar_sign_in.setVisibility(ProgressBar.VISIBLE);
        button_sign_in.setVisibility(Button.GONE);
    }

    private void hideProgressBarSignIn() {
        // Show the progress bar and hide the login button
        progressbar_sign_in.setVisibility(ProgressBar.GONE);
        button_sign_in.setVisibility(Button.VISIBLE);
    }
}
