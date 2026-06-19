package com.example.fitmorph;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class JoinActivity extends AppCompatActivity {

    EditText edtId;
    EditText edtPw;
    EditText edtName;
    EditText edtAge;
    EditText edtH;
    EditText edtW;

    RadioButton rbM;

    Button btnSave;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle s) {

        super.onCreate(s);

        setContentView(
                R.layout.activity_join
        );

        edtId =
                findViewById(
                        R.id.edtId
                );

        edtPw =
                findViewById(
                        R.id.edtPw
                );

        edtName =
                findViewById(
                        R.id.edtName
                );

        edtAge =
                findViewById(
                        R.id.edtAge
                );

        edtH =
                findViewById(
                        R.id.edtH
                );

        edtW =
                findViewById(
                        R.id.edtW
                );

        rbM =
                findViewById(
                        R.id.rbM
                );

        btnSave =
                findViewById(
                        R.id.btnSave
                );

        sp =
                getSharedPreferences(
                        "user",
                        MODE_PRIVATE
                );

        loadUserInfo();

        btnSave.setOnClickListener(v -> {

            saveUserInfo();

            startActivity(
                    new Intent(
                            JoinActivity.this,
                            MainActivity.class
                    )
            );

            finish();
        });
    }

    private void loadUserInfo(){

        edtId.setText(
                sp.getString(
                        "id",
                        ""
                )
        );

        edtPw.setText(
                sp.getString(
                        "pw",
                        ""
                )
        );

        edtName.setText(
                sp.getString(
                        "name",
                        ""
                )
        );

        edtAge.setText(
                sp.getString(
                        "age",
                        ""
                )
        );

        edtH.setText(
                sp.getString(
                        "height",
                        ""
                )
        );

        edtW.setText(
                sp.getString(
                        "weight",
                        ""
                )
        );

        String gender =
                sp.getString(
                        "gender",
                        "남자"
                );

        rbM.setChecked(
                gender.equals("남자")
        );
    }

    private void saveUserInfo(){

        String gender;

        if(rbM.isChecked()){

            gender = "남자";
        }
        else{

            gender = "여자";
        }

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putString(
                "id",
                edtId.getText().toString()
        );

        ed.putString(
                "pw",
                edtPw.getText().toString()
        );

        ed.putString(
                "name",
                edtName.getText().toString()
        );

        ed.putString(
                "age",
                edtAge.getText().toString()
        );

        ed.putString(
                "height",
                edtH.getText().toString()
        );

        ed.putString(
                "weight",
                edtW.getText().toString()
        );

        ed.putString(
                "gender",
                gender
        );

        ed.apply();
    }
}