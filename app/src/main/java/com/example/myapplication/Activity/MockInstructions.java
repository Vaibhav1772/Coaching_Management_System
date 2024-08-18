package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class MockInstructions extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_description);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.test_des), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView start = findViewById(R.id.imageView60);
        start.setOnClickListener(v -> {
            Intent intent = new Intent(MockInstructions.this, MockTest.class);
            intent.putExtra("course_id",getIntent().getIntExtra("course_id",0));
            intent.putExtra("ques_count",getIntent().getIntExtra("ques_count",0));
            intent.putExtra("test_id",getIntent().getIntExtra("test_id",0));
            startActivity(intent);
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(MockInstructions.this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            Intent intent = new Intent(MockInstructions.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

        });

    }
}
