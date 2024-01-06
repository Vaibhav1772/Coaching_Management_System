package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ChangePassword extends AppCompatActivity {
    private ImageView submit;
    private EditText user, newpass, cfpass;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_passwrd);
        submit = findViewById(R.id.create_btn);
        user = findViewById(R.id.mobile);
        newpass = findViewById(R.id.pass);
        cfpass = findViewById(R.id.cf_pass);
        bar = findViewById(R.id.progressBar2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                Thread th1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(300);
                                }
                                catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }).start();
                        String p=newpass.getText().toString();
                        String cp=cfpass.getText().toString();
                        if(p.equals(cp)){
                            login(user.getText().toString(),p);
                            Intent intent = new Intent(ChangePassword.this, Dashboard.class);
                            startActivity(intent);
                        }
                        else{
                            ViewCompat.postOnAnimation(ChangePassword.this.getCurrentFocus(), new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChangePassword.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    private static void login(String loginID,String passwrd){

        int studentID=0;
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "UPDATE login_info SET passwrd =? WHERE login_id=?";
            String hashedPassword = BCrypt.hashpw(passwrd, BCrypt.gensalt());

            PreparedStatement prep_st = connection.prepareStatement(query);
            //prep_st.setInt(1, studentID);
            prep_st.setString(1, hashedPassword);
            prep_st.setString(2, loginID);
            prep_st.executeUpdate();
            prep_st.close();

        }

        catch (SQLException e){
            e.printStackTrace();
        }

    }

}


