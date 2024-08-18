package com.example.myapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraExtensionSession;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.Status;
import com.example.myapplication.Model.Users;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;
import com.google.firebase.FirebaseApp;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    public EditText username;
    private EditText password;
    private  ProgressBar bar;

    SessionManager sessionManager;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
        sessionManager = new SessionManager(this);
        if (!sessionManager.checkSession()) {
            bar.setVisibility(ProgressBar.VISIBLE);
            Intent intent = new Intent(MainActivity.this, DashboardDrawer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            bar.setVisibility(ProgressBar.GONE);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username=findViewById(R.id.email);
        password=findViewById(R.id.Password);
        TextView admin = findViewById(R.id.adminlog);
        TextView createAccount = findViewById(R.id.create_acc);
        ImageView click = findViewById(R.id.imageView12);
        TextView forget = findViewById(R.id.forgot_your_password_);
        bar= findViewById(R.id.progressBar);

        createAccount.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddUser.class);
            startActivity(intent);
        });
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        click.setOnClickListener(view -> {
            bar.setVisibility(ProgressBar.VISIBLE);
            Call<Status> call = apiService.checkLoginDetails(new Users(-1,username.getText().toString(),password.getText().toString(),"USER"));

            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    Log.d("check","true");
                    if(response.isSuccessful() && response.body() != null) {
                        String currentStatus=response.body().getStatus();
                        Log.i("GetResultStatus",currentStatus);

                        if(currentStatus.equals("OK")) {
                            Log.i("Verification","true");
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            sessionManager.createSession(response.body().getValue());
                            sessionManager.createDate(LocalDate.now().toString());
                            Intent intent = new Intent(MainActivity.this, DashboardDrawer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            bar.setVisibility(ProgressBar.GONE);
                            startActivity(intent);

                        }
                        else {
                            bar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        bar.setVisibility(ProgressBar.GONE);
                        Toast.makeText(MainActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Status> call, Throwable throwable) {
                    Log.e("error",throwable.getMessage());
                    Toast.makeText(MainActivity.this, "Unexpected Error Occurred" , Toast.LENGTH_SHORT).show();
                }
            });
        });

        forget.setOnClickListener(v -> {
            bar.setVisibility(ProgressBar.VISIBLE);
            Intent intent = new Intent(MainActivity.this, ForgetPassword.class);
            bar.setVisibility(ProgressBar.GONE);
            startActivity(intent);
        });
    }

}