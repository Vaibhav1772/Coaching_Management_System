package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.Status;
import com.example.myapplication.Model.Tests;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;

import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockTest extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button next,submit;
    private TextView question;
    private RadioButton a,b,c,d;
    private TextView timer;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mock_test_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mock_test_new), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        next=findViewById(R.id.next);
        submit=findViewById(R.id.submit);
        question=findViewById(R.id.question);
        a=findViewById(R.id.radioButton);
        b=findViewById(R.id.radioButton2);
        c=findViewById(R.id.radioButton3);
        d=findViewById(R.id.radioButton4);
        TextView qno=findViewById(R.id.qno);
        timer=findViewById(R.id.timer);
        counter();
        sessionManager=new SessionManager(this);
        Integer ques_count=getIntent().getExtras().getInt("ques_count");

        Integer course_id=getIntent().getExtras().getInt("course_id");

        int test_id=getIntent().getExtras().getInt("test_id");
        Log.i("initial test id", Integer.toString(test_id));
        AtomicReference<Integer> start= new AtomicReference<>(1);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Show the confirmation dialog
                if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                    new AlertDialog.Builder(MockTest.this)
                            .setMessage("Are you sure you want to quit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) ->{
                               Intent intent=new Intent(MockTest.this,DashboardDrawer.class);
                               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               startActivity(intent);
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else
                    getSupportFragmentManager().popBackStack();
            }
        });
        AtomicReference<String> ans= new AtomicReference<>();
        ApiService apiService= ApiClient.getClient().create(ApiService.class);

        qno.setText(String.format("%s",start.get().toString()));
        nextQuestion(apiService,course_id, start.get());
        radioGroup=findViewById(R.id.radio_group_test);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            RadioButton btn=findViewById(checkedId);
            if(btn!=null){
                String text=btn.getText().toString();
                ans.set(text);
            }
        });
            next.setOnClickListener(v -> {
                if(ans.get()==null)
                    Toast.makeText(MockTest.this, "Please select an option", Toast.LENGTH_SHORT).show();
                else {
                    updateResponse(apiService, (course_id), start.get(), ans.get());
                    start.getAndSet(start.get() + 1);
                    if (start.get() <= ques_count) {
                        qno.setText(String.format("%s",start.get().toString()));
                         nextQuestion(apiService, course_id, start.get());
                            radioGroup.clearCheck();
                            ans.set(null);
                    } else {
                        new AlertDialog.Builder(MockTest.this)
                                .setMessage("Are you sure you want to finish?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", (dialog, id) -> {
                                    insertResults(sessionManager.getSessionId(),course_id,test_id) ;
                                    Intent intent = new Intent(MockTest.this, DashboardDrawer.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                }
            });

        submit.setOnClickListener(v -> new AlertDialog.Builder(MockTest.this)
                .setMessage("Are you sure you want to submit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) ->{
                    insertResults(sessionManager.getSessionId(),course_id,test_id);
                    Intent intent=new Intent(MockTest.this,DashboardDrawer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show());
    }

    private void insertResults(String sessionId, Integer courseId, Integer testId) {
        ApiService apiService=ApiClient.getClient().create(ApiService.class);
        Log.i("test_id",testId.toString());
        Call<Status> call=apiService.updateResult(sessionId,Integer.toString(courseId),Integer.toString(testId));
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body()!=null){

                    if(response.body().getStatus().equals("OK"))
                        Toast.makeText(MockTest.this,"Test Completed Successfully",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MockTest.this,"Test Unsuccessfully",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.e("Submission Error",t.getMessage());
            }
        });

    }

    private void counter() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer text on each tick
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                String time = "Remaining Time: " + String.format("%02d:%02d", minutes, seconds);
                timer.setText(time);
            }

            @Override
            public void onFinish() {
                if (!MockTest.this.isFinishing() && !MockTest.this.isDestroyed()) {

                // Show your dialog here
                new AlertDialog.Builder(MockTest.this)
                        .setMessage("Time's up, Exit")
                        .setCancelable(false)
                        .setPositiveButton("Exit", (dialog, id) -> {
                            timer.setText("Remaining Time: " + String.format("%02d:%02d", 0, 0));
                            Intent intent = new Intent(MockTest.this, DashboardDrawer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }).show();
                }
            }
        }.start();

    }

    public boolean nextQuestion(ApiService apiService,Integer course_id,Integer start){
        AtomicReference<Boolean> status= new AtomicReference<>(false);
        Log.i("course_id , start ",course_id.toString()+" "+start.toString());
        Call<Tests> call=apiService.getQuestions(Integer.toString(course_id),Integer.toString(start));
        call.enqueue(new Callback<Tests>() {
            @Override
            public void onResponse(Call<Tests> call, Response<Tests> response) {
                Log.i("Tests ques status ",response.isSuccessful()+" "+response.body());
                if(response.isSuccessful() && response.body()!=null){
                    question.setText(response.body().getQuestion());
                    a.setText(response.body().getOptA());
                    b.setText(response.body().getOptB());
                    c.setText(response.body().getOptC());
                    d.setText(response.body().getOptD());
                    Log.i("CurrentStatus ","true");
                    status.set(true);
                }
                else{
                    Log.d("Question Fetch error",response.message());
                }
            }

            @Override
            public void onFailure(Call<Tests> call, Throwable throwable) {
                status.set(false);
                Log.e("faliure status", throwable.getMessage());
            }
        });
//        new Thread(()->{
//            Response<Tests> response= null;
//            try {
//                response = call.execute();
//                if(response.isSuccessful() && response.body()!=null){
//                    Response<Tests> finalResponse = response;
//                    runOnUiThread(()->{
//                        question.setText(finalResponse.body().getQuestion());
//                        a.setText(finalResponse.body().getOptA());
//                        b.setText(finalResponse.body().getOptB());
//                        c.setText(finalResponse.body().getOptC());
//                        d.setText(finalResponse.body().getOptD());
//                    });
//
//                    Log.i("CurrentStatus ","true");
//                    status[0] =true;
//                }
//
//            } catch (IOException e) {
//                status[0]=false;
//                throw new RuntimeException(e);
//            }
//
//        }).start();
        Log.i("next_status",status.get().toString());
        return status.get();
    }
    public void updateResponse(ApiService apiService, Integer course_id, Integer start, String ans){
        final Boolean[] status = {false};
        Call<Status> call=apiService.updateAnswer(Integer.toString(course_id),Integer.toString(start),ans);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body()!=null){
                    status[0] =true;
                }

            }

            @Override
            public void onFailure(Call<Status> call, Throwable throwable) {
                status[0] =false;
            }
        });
    }
}
