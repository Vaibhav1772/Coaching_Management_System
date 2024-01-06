package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import java.sql.*;
public class CheckUser extends AppCompatActivity {
    private ImageView check;
    private EditText user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_user);
        check = findViewById(R.id.imageView15);
        user = findViewById(R.id.email);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean flag = check_status(user.getText().toString());
                        if(flag){
                            Intent intent = new Intent(CheckUser.this, ChangePassword.class);
                            startActivity(intent);
                        }
                        else{
                            ViewCompat.postOnAnimation(CheckUser.this.getCurrentFocus(), new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CheckUser.this, "Username not Found", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).start();
            }
        });
    }
    private static Boolean check_status(String usr){

        int studentID=0;
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query="SELECT login_id FROM login_info WHERE login_id=?";
            PreparedStatement prep = connection.prepareStatement(query);
            prep.setString(1,usr);
            ResultSet resultSet = prep.executeQuery();
            while(resultSet.next()){
                String usr_name=resultSet.getString("login_id");
                if(usr_name.equals(usr))
                    return true;
            }

        }

        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
