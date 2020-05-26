package com.umn.imergency.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jaeger.library.StatusBarUtil;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Fetch;
import com.umn.imergency.ui.drawer.news.NewsDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;
    FirebaseAuth auth;

    TextView textview_dikirim_ke;
    Button button_request_otp, button_submit_otp;
    EditText edittext_kode_otp;
    LinearLayout container_phone_verification, progressbar_phone_verification;

    String verification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        StatusBarUtil.setColor(PhoneVerificationActivity.this, Color.parseColor("#FF4081"));
        getSupportActionBar().setTitle("Phone Verification");

        Intent intent = getIntent();
        final String no_handphone = intent.getStringExtra("no_handphone");

        auth = FirebaseAuth.getInstance();
        textview_dikirim_ke = findViewById(R.id.textview_dikirim_ke);
        button_request_otp = findViewById(R.id.button_request_otp);
        button_submit_otp = findViewById(R.id.button_submit_otp);
        edittext_kode_otp = findViewById(R.id.edittext_kode_otp);
        progressbar_phone_verification = findViewById(R.id.progressbar_phone_verification);
        container_phone_verification = findViewById(R.id.container_phone_verification);

        textview_dikirim_ke.setText("Kode OTP Akan Dikirim ke Nomor "+no_handphone);
        button_submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputted_kode_otp = edittext_kode_otp.getText().toString();

                if(inputted_kode_otp.isEmpty()){
                    Toast.makeText(PhoneVerificationActivity.this, "Kode tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyOTP(inputted_kode_otp);
                }
            }
        });
        button_request_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginCountdownForRerequest();
                sendOTP(no_handphone);
            }
        });

        onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d(">>>>", e.toString());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(PhoneVerificationActivity.this, "Error : Invalid Request", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(PhoneVerificationActivity.this, "Error : Maximum Request Exceeded", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(">>>", "onCodeSent:" + verificationId);
                super.onCodeSent(verificationId, token);
                verification_id = verificationId;
                Toast.makeText(PhoneVerificationActivity.this, "Kode berhasil dikirim !", Toast.LENGTH_SHORT).show();
            }
        };

        sendOTP(no_handphone);
        beginCountdownForRerequest();
    }

    private void beginCountdownForRerequest() {
        new CountDownTimer(60000, 1000) {
            int i=60;
            public void onTick(long millisUntilFinished) {
                button_request_otp.setText("Request ("+i+"s)");
                button_request_otp.setClickable(false);
                i--;
            }

            public void onFinish() {
                button_request_otp.setText("Request");
                button_request_otp.setClickable(true);
            }
        }.start();
    }

    private void sendOTP(String no_handphone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(no_handphone, 60, TimeUnit.SECONDS, PhoneVerificationActivity.this, onVerificationStateChangedCallbacks);
        Toast.makeText(PhoneVerificationActivity.this, "Kode telah dikirim !", Toast.LENGTH_SHORT).show();
    }

    private void verifyOTP(String inputted_kode_otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, inputted_kode_otp);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onSuccessOTPVerification();
                        }
                        else{
                            Toast.makeText(PhoneVerificationActivity.this, "Kode yang anda masukkan salah!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void onSuccessOTPVerification() {
        showProgressBar();

        Intent intent = getIntent();
        String no_handphone = intent.getStringExtra("no_handphone");
        String password = intent.getStringExtra("password");
        String tanggal_lahir = intent.getStringExtra("tanggal_lahir");
        String golongan_darah = intent.getStringExtra("golongan_darah");
        String full_name = intent.getStringExtra("full_name");
        String jenis_kelamin = intent.getStringExtra("jenis_kelamin");

        Map<String, String> request_body = new HashMap<>();
        request_body.put("no_handphone", no_handphone);
        request_body.put("password", password);
        request_body.put("full_name", full_name);
        request_body.put("birth_date", tanggal_lahir);
        request_body.put("gender", jenis_kelamin);
        request_body.put("blood_type", golongan_darah);

        String URL = "https://us-central1-imergency-latest.cloudfunctions.net/MUTATION_SIGN_UP";
        new Fetch(PhoneVerificationActivity.this, "POST", URL, request_body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(">>>>", response.toString());
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            String unique_id = response.getString("unique_id");
                            JSONObject user_info = response.getJSONObject("user_info");

                            if(success) {
                                // If success, add user info and change flag in shared pref
                                onSuccessSignUp(PhoneVerificationActivity.this, unique_id, user_info);
                            } else {
                                Toast.makeText(PhoneVerificationActivity.this, message, Toast.LENGTH_SHORT).show();
                                hideProgressBar();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        hideProgressBar();
                    }
                });
    }

    private void onSuccessSignUp(Context context, String unique_id, JSONObject user_info) {
        try {
            Log.d(">>>>", user_info.toString());
            String user_info_full_name = user_info.getString("full_name");
            String user_info_blood_type = user_info.getString("blood_type");
            String user_info_gender = user_info.getString("gender");
            String user_info_phone_number = user_info.getString("phone_number");

            SharedPreferences sharedPreferences = getSharedPreferences("imergency", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.sp_key_unique_id), unique_id);
            editor.putBoolean(getString(R.string.sp_key_is_logged_in), true);
            editor.putString(getString(R.string.sp_key_user_info_full_name), user_info_full_name);
            editor.putString(getString(R.string.sp_key_user_info_blood_type), user_info_blood_type);
            editor.putString(getString(R.string.sp_key_user_info_gender), user_info_gender);
            editor.putString(getString(R.string.sp_key_user_info_phone_number), user_info_phone_number);
            editor.apply();

            Intent intent = new Intent(context, PermissionCallActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showProgressBar() {
        progressbar_phone_verification.setVisibility(LinearLayout.VISIBLE);
        container_phone_verification.setVisibility(LinearLayout.GONE);
    }

    private void hideProgressBar() {
        progressbar_phone_verification.setVisibility(LinearLayout.GONE);
        container_phone_verification.setVisibility(LinearLayout.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
