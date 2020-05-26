package com.umn.imergency.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umn.imergency.R;
import com.umn.imergency.helpers.Encryption;
import com.umn.imergency.helpers.Fetch;
import com.umn.imergency.ui.drawer.news.NewsDetail;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private TextView textview_login, textview_tanggal_lahir;
    private Button button_set_date, button_submit;
    private EditText edittext_nama_lengkap, edittext_no_handphone, edittext_password;
    private RadioGroup radiogroup_gender;
    private Spinner spinner_golongan_darah, spinner_kode_negara;
    private ProgressBar progressbar_sign_up;

    private int birth_day=0, birth_month=0, birth_year=0;
    private String jenis_kelamin = "Laki-Laki";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textview_login = findViewById(R.id.textview_login);
        textview_tanggal_lahir = findViewById(R.id.textview_tanggal_lahir);
        edittext_nama_lengkap = findViewById(R.id.edittext_nama_lengkap);
        edittext_no_handphone = findViewById(R.id.edittext_no_handphone);
        edittext_password = findViewById(R.id.edittext_password);
        radiogroup_gender = findViewById(R.id.radiogroup_gender);
        spinner_golongan_darah = findViewById(R.id.spinner_golongan_darah);
        spinner_kode_negara = findViewById(R.id.spinner_kode_negara);
        button_set_date = findViewById(R.id.button_set_date);
        button_submit = findViewById(R.id.button_submit);
        progressbar_sign_up = findViewById(R.id.progressbar_sign_up);

        // Onchecked Listener
        radiogroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radio_laki : {
                        jenis_kelamin = "Laki-Laki";
                        break;
                    }
                    case R.id.radio_perempuan: {
                        jenis_kelamin = "Perempuan";
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        });

        // Onclick Listeners
        textview_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        button_set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                LayoutInflater inflater = LayoutInflater.from(SignupActivity.this);
                View content = inflater.inflate(R.layout.dialog_date, null);
                builder.setView(content);
                final AlertDialog dialog = builder.create();

                Button btn_accept_date = content.findViewById(R.id.button_acceptDate);
                final DatePicker datepicker_date = content.findViewById(R.id.datepicker_date);

                btn_accept_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String new_month, new_day;

                        birth_day = datepicker_date.getDayOfMonth();
                        birth_month = datepicker_date.getMonth()+1;
                        birth_year = datepicker_date.getYear();

                        if(birth_day<=9){
                            new_day="0"+birth_day;
                        }
                        else{
                            new_day=String.valueOf(birth_day);
                        }

                        if(birth_month<=9){
                            new_month = "0"+birth_month;
                        }
                        else{
                            new_month = String.valueOf(birth_month);
                        }

                        textview_tanggal_lahir.setText(new_day+"-"+new_month+"-"+birth_year);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        showProgressBarSignUp();

        String kode_negara = spinner_kode_negara.getSelectedItem().toString(),
                no_handphone = edittext_no_handphone.getText().toString(),
                password = edittext_password.getText().toString(),
                full_name = edittext_nama_lengkap.getText().toString(),
                tanggal_lahir = textview_tanggal_lahir.getText().toString(),
                golongan_darah = spinner_golongan_darah.getSelectedItem().toString();

        if(isValid(no_handphone, password, full_name, tanggal_lahir)) {
            String hashed_password = new Encryption().md5(password);
            String full_no_handphone = kode_negara + no_handphone;
            hideProgressBarSignUp();

            Intent intent = new Intent(SignupActivity.this, PhoneVerificationActivity.class);
            intent.putExtra("no_handphone", full_no_handphone);
            intent.putExtra("password", hashed_password);
            intent.putExtra("tanggal_lahir", tanggal_lahir);
            intent.putExtra("golongan_darah", golongan_darah);
            intent.putExtra("full_name", full_name);
            intent.putExtra("jenis_kelamin", jenis_kelamin);
            startActivity(intent);
        } else {
            Toast.makeText(SignupActivity.this, "Mohon isi field yang kosong", Toast.LENGTH_SHORT).show();
            hideProgressBarSignUp();
        }
    }


    private boolean isValid(String no_handphone, String password, String full_name, String tanggal_lahir) {
        return !no_handphone.isEmpty() && !password.isEmpty() && !full_name.isEmpty() && !tanggal_lahir.isEmpty();
    }

    private void showProgressBarSignUp() {
        // Show the progress bar and hide the login button
        progressbar_sign_up.setVisibility(ProgressBar.VISIBLE);
        button_submit.setVisibility(Button.GONE);
    }

    private void hideProgressBarSignUp() {
        // Show the progress bar and hide the login button
        progressbar_sign_up.setVisibility(ProgressBar.GONE);
        button_submit.setVisibility(Button.VISIBLE);
    }
}
