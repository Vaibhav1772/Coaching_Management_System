package com.example.myapplication;

import static com.example.myapplication.DashCourse.cid;
import static com.example.myapplication.MainActivity.val;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class BuyCourse extends AppCompatActivity {

    private TextView details;
    private ImageView payment;
    final Pair<Integer, Integer>[] data = new Pair[]{new Pair<>(0, 0)};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_course);
        details=findViewById(R.id.textView58);
        payment=findViewById(R.id.imageView51);

        new Thread(new Runnable() {
            @Override
            public void run() {
                data[0] = fetch_data();
            }
        }).start();
        System.out.println("cid"+cid);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setpayment(data[0].first, data[0].second);
                        }

                }).start();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BuyCourse.this, "Payment Successful", Toast.LENGTH_SHORT).show();

                    }
                });
                startActivity(new Intent(BuyCourse.this,Dashboard.class));
            }
        });
    }

    private void setpayment(int sid,int cid) {
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            LocalDate date = LocalDate.now();
            String current_date= DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date);
            String set_query = "INSERT INTO payments (student_id,course_id,payment_date,amount,payment_method,status) VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(set_query);
            preparedStatement.setInt(1,sid);
            preparedStatement.setInt(2,cid);
            preparedStatement.setString(3,current_date);
            preparedStatement.setDouble(4,9999);
            preparedStatement.setString(5,"online");
            preparedStatement.setString(6,"success");
            preparedStatement.execute();
            String queryupdate="INSERT INTO enrollments (payment_id,student_id,course_id,enrollment_date,enrollment_status,batch_id) VALUES(?,?,?,?,?,?)";
            PreparedStatement prep =connection.prepareStatement(queryupdate);
            prep.setInt(1,getpaymentId(val,cid).first);
            prep.setInt(2, (get_id(val)));
            prep.setInt(3,cid);
            prep.setString(4,getpaymentId(val,cid).second);
            prep.setString(5,"success");
            prep.setInt(6,cid);
            prep.execute();
            DatabaseManager.closeConnection();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Pair<Integer,String> getpaymentId(String val, int cid) {
        int sid=get_id(val);
        int idx=-1;
        String date="";
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();

            String get_query = "SELECT payment_id,payment_date FROM payments WHERE student_id=? AND course_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(get_query);

            preparedStatement.setInt(1, sid);
            preparedStatement.setInt(2,cid);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                idx = resultSet.getInt("payment_id");
                date = resultSet.getString("payment_date");
            }
            preparedStatement.close();

        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        //Log.i("idx",Integer.toString(idx));
        return new Pair<>(idx,date);
    }

    private int get_id(String loginId){
        int idx = -1;
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();

            String get_query = "SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(get_query);

            preparedStatement.setString(1, loginId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                idx = resultSet.getInt("user_id");
            preparedStatement.close();

        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i("idx1",Integer.toString(idx));
        return idx;
    }
    public Pair<Integer,Integer> fetch_data(){

        //int studentID=0;
        int courseId=-1;
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();

                String query1 = "SELECT course_id,course_name,des FROM courses WHERE course_id=?";
                PreparedStatement preparedStatement1= connection.prepareStatement(query1);
                preparedStatement1.setInt(1,Integer.parseInt(new DashCourse().cid));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                courseId=Integer.parseInt(new DashCourse().cid);
                while(resultSet1.next()) {
                    int index=resultSet1.getInt("course_id");

                    String str=Integer.toString(index);
                    String setData="COURSE ID: "+str+"\n\n"+"COURSE NAME: "+resultSet1.getString("course_name")
                            +"\n\n"+"COURSE DESCRIPTION: "+resultSet1.getString("des");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            details.setText(setData);
                        }
                    });

                }


            DatabaseManager.closeConnection();

        }

        catch (SQLException e){
            e.printStackTrace();
            Log.i("error"," "+e.getMessage());
        }
        return new Pair<Integer,Integer>(get_id(val),courseId);
    }

    /*public void showAlert(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Title");
        builder.setMessage(message);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            // Set negative button and its click listener (optional)
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }*/


}