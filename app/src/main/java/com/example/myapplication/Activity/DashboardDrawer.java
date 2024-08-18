package com.example.myapplication.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.Model.Students;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardDrawer extends AppCompatActivity {

    private DrawerLayout draw;
    private TextView std_name,std_id;
    private SessionManager sessionManager;
    private StorageReference storageReference;
    private ImageView profile;
    private String userId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        sessionManager=new SessionManager(this);
        userId = sessionManager.getSessionId();
        NavigationView navView = findViewById(R.id.nav_view);
        draw= findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("C.M.S");
        ActionBarDrawerToggle bar = new ActionBarDrawerToggle(this, draw, toolbar, R.string.open, R.string.close);
        draw.addDrawerListener(bar);
        bar.syncState();

        progressBar= navView.getHeaderView(0).findViewById(R.id.progressBar);
        profile= navView.getHeaderView(0).findViewById(R.id.profile);
        FloatingActionButton edit = navView.getHeaderView(0).findViewById(R.id.edit_fab);
        std_name= navView.getHeaderView(0).findViewById(R.id.student_name);
        std_id= navView.getHeaderView(0).findViewById(R.id.student_id);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).
                add(R.id.dash_frag,Dashboard.class,null).commit();

        edit.setOnClickListener(v-> openPhotoPicker());
        fetchAndDisplayImage();

        ApiService apiService= ApiClient.getClient().create(ApiService.class);
        Call<Students> call=apiService.getStudentDetails(sessionManager.getSessionId());
        call.enqueue(new Callback<Students>() {
            @Override
            public void onResponse(Call<Students> call, Response<Students> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    std_name.setText(response.body().getName());
                    std_id.setText(String.format("ID: %s", sessionManager.getSessionId()));
                }
            }

            @Override
            public void onFailure(Call<Students> call, Throwable throwable) {
                Log.e("Student Fetch Navigation Header Error",throwable.getMessage());
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Show the confirmation dialog
                if(getSupportFragmentManager().getBackStackEntryCount()==0) {
                    new AlertDialog.Builder(DashboardDrawer.this)
                            .setMessage("Are you sure you want to exit?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> {
                                // Close the app
                                finishAffinity();
                                System.exit(0);
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else
                    getSupportFragmentManager().popBackStack();
            }
        });
        navView.setNavigationItemSelectedListener(item -> {
            int id= item.getItemId();
            if(id==R.id.logout) {
                new AlertDialog.Builder(DashboardDrawer.this)
                        .setMessage("Logout ?")
                        .setCancelable(false).setPositiveButton("Yes",(dialog,idx)->{
                            new SessionManager(DashboardDrawer.this).clearSession();
                            finishAffinity();
                            System.exit(0);

                        }).setNegativeButton("No",null).show();

            }
            else if(id==R.id.profile){
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).
                        replace(R.id.fragmentContainerView,UserProfile.class,null).addToBackStack(null).commit();
            }
            else if(id==R.id.my_courses){
                Toast.makeText(DashboardDrawer.this, "My Courses", Toast.LENGTH_SHORT).show();
            }
            else if(id==R.id.results){
                Toast.makeText(DashboardDrawer.this, "Results", Toast.LENGTH_SHORT).show();
            }
            draw.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void uploadImage(Uri imageUri) {
        try {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));

            // Resize the bitmap to match ImageView dimensions
            int width = profile.getWidth();
            int height = profile.getHeight();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            // Create a reference to Firebase Storage
            StorageReference imageRef = storageReference.child("profile_images/" + userId + ".png");

            // Upload image
            imageRef.putBytes(data)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        //saveImageUrlToDatabase(imageUrl);
                        displayImage(imageUrl);
                        progressBar.setVisibility(ProgressBar.GONE);
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(ProgressBar.GONE);
                    });

        } catch (IOException e) {
            Log.e("Image Upload Error", e.getMessage());
        }
    }


    private final ActivityResultLauncher<Intent> photoPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    uploadImage(imageUri);
                }
            });


    private void openPhotoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerLauncher.launch(intent);
    }

    private void displayImage(String imageUrl) {
        Picasso.get().load(imageUrl).transform(new CircularTransform()).fit().centerCrop().into(profile);
    }

    private void fetchAndDisplayImage() {
        progressBar.setVisibility(ProgressBar.VISIBLE); // Show progress bar
        StorageReference fileReference = storageReference.child("profile_images/" +userId+ ".png");
        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).transform(new CircularTransform()).fit().centerCrop(5).into(profile);
            progressBar.setVisibility(ProgressBar.GONE); // Hide progress bar
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(ProgressBar.GONE); // Hide progress bar
        });
    }
}
