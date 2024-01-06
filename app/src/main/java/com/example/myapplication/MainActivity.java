package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.color.DynamicColors;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView login,signup,forget;

    private ImageView sign_in;
    public static String val;
    private static Boolean check=false,pres=false;
    private static String login_id ,pass;
    public EditText email_val;
    private EditText pass_val;
    private TextView admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        login = findViewById(R.id.Sign_in);
        signup = findViewById(R.id.create_acc);
        email_val = findViewById(R.id.email);
        pass_val = findViewById(R.id.Password);
        forget = findViewById(R.id.forgot_your_password_);
        sign_in = findViewById(R.id.imageView12);
        ProgressBar bar = findViewById(R.id.progressBar);
        admin=findViewById(R.id.adminlog);
        email_val.setText("");
        pass_val.setText("");
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_id=email_val.getText().toString();
                pass=pass_val.getText().toString();

                System.out.println(login_id+" "+pass);

                final Boolean[] status = {false};
                bar.setVisibility(View.VISIBLE);
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        status[0] =valid_log(login_id,pass);
                        if(status[0]) {
                            Log.i("MY_APP","Already Logged In...");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bar.setVisibility(View.INVISIBLE);
                                }
                            });
                            LocalDate date = LocalDate.now();
                            String current_date= DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date);
                            attendance(current_date,login_id);

                            Intent intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);
                            val=login_id;
                            //Toast.makeText(MainActivity.this, "Already Logged In...", Toast.LENGTH_LONG).show();
                        }
                        else{
                            bar.setVisibility(View.INVISIBLE);
                            ViewCompat.postOnAnimation(Objects.requireNonNull(MainActivity.this.getCurrentFocus()), new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Username not Found", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                });
                t1.start();
                Log.d("MY_STATUS","STATUS "+ status[0]);

            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, CheckUser.class);
                        startActivity(intent);
                    }
                }).start();;
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread signup = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                        startActivity(intent);

                   }
                });
                signup.start();
                //super.onCreate(savedInstanceState);

            }
        });
     /*   admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AdminActivity.class);
                startActivity(intent);
            }
        });*/
    }


    private void attendance(String date, String loginId) {

        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();

            String get_query="SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(get_query);

            preparedStatement.setString(1,loginId);

            ResultSet resultSet=preparedStatement.executeQuery();
            int idx=-1;
            while(resultSet.next())
                idx=resultSet.getInt("user_id");
            preparedStatement.close();

            String query ="INSERT INTO attendance (student_id,curr_date,stat,remarks) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement1=connection.prepareStatement(query);

            preparedStatement1.setInt(1,idx);
            preparedStatement1.setString(2, date);
            preparedStatement1.setString(3,"Success");
            preparedStatement1.setString(4,"P");
            preparedStatement1.executeUpdate();
            preparedStatement1.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean valid_log(String loginID,String pass){

        try{
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query="SELECT login_id,passwrd FROM login_info";
            ResultSet resultSet = statement.executeQuery(query);
            boolean temp=false;
            while(resultSet.next())
            {
                String log=resultSet.getString("login_id");
                //Log.i("logIN",log);
                String passwrd=resultSet.getString("passwrd");
                //Log.i("pass",passwrd);
                boolean match = BCrypt.checkpw(pass, passwrd);
                //Log.i("match","match_status "+match);
                if(match && loginID.equals(log)) {
                    check=true;
                    pres=true;
                    temp=true;
                    return true;
                }
                else if(loginID.equals(log) ) {
                    //check=false;
                    pres=true;
                }
            }
            //System.out.println(check+" "+pres+ " "+ temp);
            if(pres && !temp)
                return false;
        }

        catch (SQLException e) {
            check=false;
            pres=false;
            e.printStackTrace();
            return false;
        }
        return false;
    }//---Authenicating Credentials---

}