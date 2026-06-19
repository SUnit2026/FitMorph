package com.example.fitmorph;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import manager.AutoLoginManager;

public class MainActivity extends AppCompatActivity {

    EditText edtId;
    EditText edtPw;

    Button btnLogin;
    Button btnJoin;

    SharedPreferences sp;

    AutoLoginManager autoLoginManager;

    @Override
    protected void onCreate(Bundle s) {

        super.onCreate(s);

        setContentView(
                R.layout.activity_main
        );

        edtId =
                findViewById(
                        R.id.edtId
                );

        edtPw =
                findViewById(
                        R.id.edtPw
                );

        btnLogin =
                findViewById(
                        R.id.btnLogin
                );

        btnJoin =
                findViewById(
                        R.id.btnJoin
                );

        sp =
                getSharedPreferences(
                        "user",
                        MODE_PRIVATE
                );

        autoLoginManager =
                new AutoLoginManager(
                        sp
                );

        if(autoLoginManager.isAutoLogin()){

            startActivity(
                    new Intent(
                            MainActivity.this,
                            HomeActivity.class
                    )
            );

            finish();

            return;
        }

        btnJoin.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            JoinActivity.class
                    )
            );
        });

        btnLogin.setOnClickListener(v -> {

            String id =
                    edtId.getText()
                            .toString()
                            .trim();

            String pw =
                    edtPw.getText()
                            .toString()
                            .trim();

            String saveId =
                    sp.getString(
                            "id",
                            ""
                    );

            String savePw =
                    sp.getString(
                            "pw",
                            ""
                    );

            if(id.equals(saveId)
                    && pw.equals(savePw)){

                autoLoginManager
                        .saveLoginState(
                                true
                        );

                Toast.makeText(
                        this,
                        "로그인 성공",
                        Toast.LENGTH_SHORT
                ).show();

                startActivity(
                        new Intent(
                                MainActivity.this,
                                HomeActivity.class
                        )
                );

                finish();
            }
            else{

                Toast.makeText(
                        this,
                        "아이디 또는 비밀번호 오류",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}