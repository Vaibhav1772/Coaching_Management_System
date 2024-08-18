package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.Status;
import com.example.myapplication.Model.Students;
import com.example.myapplication.Model.Users;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser extends AppCompatActivity {

    private EditText name, mail, mobile,password,dob,address;
    private ImageView createUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_account), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        name=findViewById(R.id.name);
        mail=findViewById(R.id.mail);
        mobile=findViewById(R.id.mobile);
        password=findViewById(R.id.acc_pass);
        createUser=findViewById(R.id.create_btn);
        dob=findViewById(R.id.dob);
        address=findViewById(R.id.address);

        createUser.setOnClickListener(view -> {

            Call<Status> call = apiService.createUser(new Users(-1, mail.getText().toString(), password.getText().toString(), "USER"));

            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        Log.d("createStatusCheck", "true");
                        Toast.makeText(AddUser.this, "User Created", Toast.LENGTH_SHORT).show();
                        getUserId(mail.getText().toString(), apiService);

                    } else
                        Toast.makeText(AddUser.this, "User Not Created", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    Log.e("User Insertion Error",t.getMessage());
                }
            });

        });

    }
    public void getUserId(String usr,ApiService apiService){
        Log.d("usr",usr);

        Call<Status> call = apiService.getLoginDetails(usr);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body() != null) {

                    Log.i("HII", response.body().getValue().toString());
                    insertStudent(new Users(Integer.parseInt(response.body().getValue()),null,null,"USER"),apiService);
                }
                else {
                    Log.e("Student Fetch Error", response.errorBody().toString());
                    runOnUiThread(() -> Toast.makeText(AddUser.this, "User Not Found", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.e("User Fetch Error",t.getMessage());
            }
        });

    }
    public void insertStudent(Users user,ApiService apiService) {


        Call<Status> call = apiService.createStudent(new Students(-1,name.getText().toString(),(dob.getText().toString()),mobile.getText().toString(),mail.getText().toString(),address.getText().toString(), null,null,user));

        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    Log.d("createStudentStatus", response.toString());
                   // Toast.makeText(AddUser.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddUser.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                    Log.e("Student Null error",response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable throwable) {
                Log.e("Student error",throwable.getMessage());
            }
        });
    }
}
