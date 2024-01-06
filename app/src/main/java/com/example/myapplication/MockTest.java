package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MockTest extends AppCompatActivity {

    ArrayList<Pair<Integer,Pair<TextView,ImageView>>> arrayList=new ArrayList<>(4);
    TextView ques,id,opt_a,opt_b,opt_c,opt_d,timer;
    ImageView submit,next;
    ImageView a,b,c,d;
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_fragment_mock);
        CountDownTimer countDownTimer;

        final int[] idx = {1};
        timer = findViewById(R.id.textView56);
        ques=findViewById(R.id.textView46);
        id=findViewById(R.id.textView47);
        opt_a=findViewById(R.id.textView49);
        opt_b=findViewById(R.id.textView55);
        opt_c=findViewById(R.id.textView52);
        opt_d=findViewById(R.id.textView54);
        submit=findViewById(R.id.imageView58);
        a=findViewById(R.id.imageView48);
        b=findViewById(R.id.imageView50);
        c=findViewById(R.id.imageView52);
        d=findViewById(R.id.imageView53);
        arrayList.add(Pair.create(0,(Pair.create(opt_a,a))));
        arrayList.add(Pair.create(0,(Pair.create(opt_b,b))));
        arrayList.add(Pair.create(0,(Pair.create(opt_c,c))));
        arrayList.add(Pair.create(0,(Pair.create(opt_d,d))));
        // Set the countdown time in milliseconds (e.g., 5 minutes)
        long countdownTimeInMillis = 6 * 10 * 1000;  // 5 minutes

        countDownTimer = new CountDownTimer(countdownTimeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                // Update the timer text on each tick
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                timer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                // Handle what to do when the timer finishes (e.g., submit the quiz)
                showAlert("Time's Up!!",true);
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i=0;i< arrayList.size();i++) {
                            if(arrayList.get(i).first==1){
                                int finalI = i;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setData(1,id,arrayList.get(finalI).second.first.getText().toString());
                                    }
                                }).start();
                                arrayList.get(i).second.second.setImageResource((R.drawable.rectangle_1_shape));
                                arrayList.get(i).second.first.setTextColor(getColor(R.color.black));
                                arrayList.set(i,new Pair<>(0,new Pair<>(arrayList.get(i).second.first,arrayList.get(i).second.second)));
                                break;
                            }

                        }
                    }
                });
                thread.setName("timer");
                thread.start();

                timer.setText("00:00");
            }
        };

        countDownTimer.start();
        new Thread(new Runnable(){
            @Override
            public void run() {

                getData(ques,id,opt_a,opt_b,opt_c,opt_d, idx[0]);
            }
        }).start();
        next=findViewById(R.id.imageView56);
        next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               for (int i=0;i< arrayList.size();i++) {
                   if(arrayList.get(i).first==1){
                       int finalI = i;
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               setData(1,id,arrayList.get(finalI).second.first.getText().toString());
                           }
                       }).start();
                       arrayList.get(i).second.second.setImageResource((R.drawable.rectangle_1_shape));
                       arrayList.get(i).second.first.setTextColor(getColor(R.color.black));
                       arrayList.set(i,new Pair<>(0,new Pair<>(arrayList.get(i).second.first,arrayList.get(i).second.second)));
                       break;
                   }

               }
               System.out.println("idx"+idx[0]);
               idx[0]++;
               if(idx[0]==6) {
                   String message="Submit ?";
                   showAlert(message,false);

               }
               new Thread(new Runnable() {
                   @Override
                   public void run(){
                       getData(ques,id,opt_a,opt_b,opt_c,opt_d, idx[0]);
                   }
               }).start();

           }
        });
        submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               showAlert("Submit?",false);

           }
        });

    }



    private void getData(TextView ques, TextView id, TextView optA, TextView optB, TextView optC, TextView optD, int idx) {

        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query="SELECT question,test_id,opt_a,opt_b,opt_c,opt_d FROM tests WHERE course_id=? AND test_id=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2,idx);
            ResultSet resultSet= preparedStatement.executeQuery();

            String ques1,id1,opt_a1,opt_b1,opt_c1,opt_d1;
            while(resultSet.next()) {

                ques1=resultSet.getString("question");
                id1=Integer.toString(resultSet.getInt("test_id"));
                opt_a1=resultSet.getString("opt_a");
                opt_b1=resultSet.getString("opt_b");
                opt_c1=resultSet.getString("opt_c");
                opt_d1=resultSet.getString("opt_d");
                String finalQues = ques1;
                String finalId = id1;
                String finalOpt_a = opt_a1;
                String finalOpt_b = opt_b1;
                String finalOpt_c = opt_c1;
                String finalOpt_d = opt_d1;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            ques.setText(finalQues);

                            id.setText(finalId);

                            optA.setText(finalOpt_a);

                            optB.setText(finalOpt_b);

                            optC.setText(finalOpt_c);

                            optD.setText(finalOpt_d);

                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        a.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                a.setImageResource((R.drawable.rectangle_colored));

                                b.setImageResource((R.drawable.rectangle_1_shape));

                                c.setImageResource((R.drawable.rectangle_1_shape));

                                d.setImageResource((R.drawable.rectangle_1_shape));
                                arrayList.set(0,Pair.create(1,(Pair.create(optA,a))));
                                arrayList.set(1,Pair.create(0,(Pair.create(optB,b))));
                                arrayList.set(2,Pair.create(0,(Pair.create(optC,c))));
                                arrayList.set(3,Pair.create(0,(Pair.create(optD,d))));
                            }
                        });
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                b.setImageResource((R.drawable.rectangle_colored));

                                a.setImageResource((R.drawable.rectangle_1_shape));;

                                c.setImageResource((R.drawable.rectangle_1_shape));;

                                d.setImageResource((R.drawable.rectangle_1_shape));;
                                arrayList.set(0,Pair.create(0,(Pair.create(optA,a))));
                                arrayList.set(1,Pair.create(1,(Pair.create(optB,b))));
                                arrayList.set(2,Pair.create(0,(Pair.create(optC,c))));
                                arrayList.set(3,Pair.create(0,(Pair.create(optD,d))));

                            }
                        });
                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                c.setImageResource((R.drawable.rectangle_colored));

                                b.setImageResource((R.drawable.rectangle_1_shape));

                                a.setImageResource((R.drawable.rectangle_1_shape));

                                d.setImageResource((R.drawable.rectangle_1_shape));
                                arrayList.set(0,Pair.create(0,(Pair.create(optA,a))));
                                arrayList.set(1,Pair.create(0,(Pair.create(optB,b))));
                                arrayList.set(2,Pair.create(1,(Pair.create(optC,c))));
                                arrayList.set(3,Pair.create(0,(Pair.create(optD,d))));

                            }
                        });
                        d.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                d.setImageResource((R.drawable.rectangle_colored));

                                b.setImageResource((R.drawable.rectangle_1_shape));

                                c.setImageResource((R.drawable.rectangle_1_shape));

                                a.setImageResource((R.drawable.rectangle_1_shape));
                                arrayList.set(0,Pair.create(0,(Pair.create(optA,a))));
                                arrayList.set(1,Pair.create(0,(Pair.create(optB,b))));
                                arrayList.set(2,Pair.create(0,(Pair.create(optC,c))));
                                arrayList.set(3,Pair.create(1,(Pair.create(optD,d))));
                            }
                        });
                    }

                });

            }
            DatabaseManager.closeConnection();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setData(int id, TextView test_id,String data) {
        if(data.equals(null))
            data="*";
        try {

            Connection connection = null;
            connection=DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "UPDATE tests SET user_ans=? WHERE test_id=? AND course_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            System.out.println(Integer.parseInt(test_id.getText().toString()));
            preparedStatement.setString(1,data);
            preparedStatement.setInt(2,Integer.parseInt(test_id.getText().toString()));
            preparedStatement.setInt(3,id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showAlert(String message,boolean flag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(message);
        if(!flag){
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MockTest.this,"Submitted Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MockTest.this,Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
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
        }

        // Set positive button and its click listener

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop your thread when the activity is destroyed
        if (getMainLooper().getThread() != null && getMainLooper().getThread().isAlive()) {
            getMainLooper().getThread().interrupt();
        }
    }

}