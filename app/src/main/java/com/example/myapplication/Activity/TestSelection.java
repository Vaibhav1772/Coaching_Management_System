package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.Status;
import com.example.myapplication.Model.Students;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestSelection extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quiz), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CardView cardView = findViewById(R.id.Test);
        cardView.setOnClickListener(v -> {
            ApiService apiService= ApiClient.getClient().create(ApiService.class);
            Call<Status> call=apiService.getQuestionCount(Integer.toString(1));
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        Toast.makeText(TestSelection.this,"Questions Count : "+response.body().getValue(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TestSelection.this, MockInstructions.class);
                        int val=Integer.parseInt(response.body().getValue());
                        intent.putExtra("ques_count",val);
                        intent.putExtra("course_id",1);
                        intent.putExtra("test_id",1);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable throwable) {

                }
            });

        });

    }
}
