package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {

    public EditText username,password,cnfPassword;
    private ImageView click;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_passwrd);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forget_password), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        username=findViewById(R.id.email);
        password=findViewById(R.id.Password);
        cnfPassword=findViewById(R.id.cnf_password);
        click=findViewById(R.id.create_btn);
        click.setOnClickListener(v -> {
            String pass=password.getText().toString();
            String cnfPass=cnfPassword.getText().toString();
            if(pass.equals(cnfPass)) {
                Users usr=new Users(-1,username.getText().toString(),pass,"USER");
                Call<Status> call=apiService.updatePassword(usr);
                call.enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if(response.isSuccessful() && response.body()!=null) {
                            Log.i("password changed",response.body().getStatus());
                            Toast.makeText(ForgetPassword.this,"Password Updated",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetPassword.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else
                            Log.d("error",response.message());
                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Toast.makeText(ForgetPassword.this, "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        });
    }
}
