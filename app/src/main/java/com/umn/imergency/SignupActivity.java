package com.umn.imergency;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {
    private TextView textview_login, textview_tanggal_lahir;
    private Button button_set_date;

    private int birth_day=0, birth_month=0, birth_year=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textview_login = findViewById(R.id.textview_login);
        textview_tanggal_lahir = findViewById(R.id.textview_tanggal_lahir);
        button_set_date = findViewById(R.id.button_set_date);

        // Onclick Listeners
        textview_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startNewActivity = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(startNewActivity);
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
    }
}
