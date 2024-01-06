package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateAccount extends AppCompatActivity {

    private static String std_name;
    private static String phn;
    private EditText name,number,mail,credential;
    private ImageView create;
    private String usr,cred;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
        number = findViewById(R.id.mobile);
        credential = findViewById(R.id.acc_pass);
        create = findViewById(R.id.create_btn);
        ProgressBar bar = findViewById(R.id.progressBar_acc);



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usr=mail.getText().toString();
                cred=credential.getText().toString();
                std_name=name.getText().toString();
                phn= number.getText().toString();
                Log.i("CHECK______",""+usr+" "+cred);
                bar.setVisibility(View.VISIBLE);
                Thread th1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bar.setVisibility(View.INVISIBLE);
                            }
                        });
                        signup(usr,cred);
                        Intent intent = new Intent(CreateAccount.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                th1.start();
            }
        });
    }
    private static void signup(String loginID,String passwrd){

       // insert_log(loginID, passwrd);

        try {

            Connection connection =DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement prep= connection.prepareStatement(query);
            prep.setString(1,loginID);
            ResultSet res= prep.executeQuery();
            int usr=0;
            while(res.next()){
                usr=res.getInt("user_id");
            }
            prep.close();
            String query1="INSERT INTO students (first_name,phone,email,student_id) VALUES(?,?,?,?)";
            PreparedStatement prep1= connection.prepareStatement(query1);
            prep1.setString(1,std_name);
            prep1.setString(2,phn);
            prep1.setString(3,loginID);
            prep1.setInt(4,usr);
            prep1.executeUpdate();
            prep1.close();
        }

        catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void insert_log(String log, String cred){
        String url = "jdbc:mysql://192.168.147.39:3306/my_db";
        String username = "vaibhav";
        String password = "Vaibhav.1772@";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            String query1="INSERT INTO login_info (login_id,passwrd,success) VALUES(?,?,1)";
            PreparedStatement prep= connection.prepareStatement(query1);
            String hpass= BCrypt.hashpw(cred,BCrypt.gensalt());
            prep.setString(1,log);
            prep.setString(2,hpass);
            prep.executeUpdate();
            prep.close();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
