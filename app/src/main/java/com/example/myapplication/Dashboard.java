package com.example.myapplication;

import static com.example.myapplication.MainActivity.val;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accessibilityservice.AccessibilityService;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;

import android.os.Looper;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {
    private ActionBarDrawerToggle bar;
    private DrawerLayout draw;
    private RecyclerView recyclerView;
    private  ActivityResultLauncher<Intent> pickImageLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),this::processPickedImage);;
    private NavigationView navView;
    private static String student;
    private static int idx;

    private TextView name,uid;
    private ImageView profile;
    private Toolbar toolbar;
    private byte[] bytes;
    private Bitmap roundedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        Log.i("dump "," "+name+" "+uid);
        navView = findViewById(R.id.nav_view);
        draw= findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        name=navView.getHeaderView(0).findViewById(R.id.textView);
        uid=navView.getHeaderView(0).findViewById(R.id.textView2);
        profile=navView.getHeaderView(0).findViewById(R.id.profile);
        setSupportActionBar(toolbar);
        bar = new ActionBarDrawerToggle(this,draw,toolbar, R.string.open,R.string.close);
        draw.addDrawerListener(bar);
        bar.syncState();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.dash_frag, DashFragment.class, null)
                .commit();

        new Thread(new Runnable() {
            @Override
            public void run() {

                if(getProfileImage(val)!=null){
                    byte[] imgData=getProfileImage(MainActivity.val);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                    Bitmap newBitmap=getRoundedBitmap(bitmap);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profile.setImageBitmap(newBitmap);
                        }
                    });
                }
               Pair<String,Integer> data= display();
                Log.i("pair"," "+data);
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(data.first.toString());
                        uid.setText("Registration ID: "+data.second.toString());
                    }
                });
            }

        }).start();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();

            }
        });


        //Log.i("Values","name "+ data[0].first+" "+"id "+ data[0].second);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if(R.id.item1==item.getItemId()){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragmentContainerView, PersonalInfo.class,null)
                            .setReorderingAllowed(true).addToBackStack("dash_frag");
                    fragmentTransaction.commit();
                }
                if(R.id.item3==item.getItemId()){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.fragmentContainerView, MyCourses.class,null)
                            .setReorderingAllowed(true).addToBackStack("dash_frag");
                    fragmentTransaction.commit();

                }
                if(R.id.item4==item.getItemId()){
                    Toast.makeText(Dashboard.this, "Exams", Toast.LENGTH_SHORT).show();
                }
                if(R.id.item5==item.getItemId()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                    builder.setTitle("Logout!!")
                            .setMessage("Are You Sure ?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle the positive button click
                                    Intent intent = new Intent(Dashboard.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    onDestroy();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle the negative button click or do nothing
                                   return;
                                }
                            });

                    // Create and show the alert dialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
                draw.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop your thread when the activity is destroyed
        if (getMainLooper().getThread() != null && getMainLooper().getThread().isAlive()) {
            getMainLooper().getThread().interrupt();
        }
    }
    public byte[] getProfileImage(String loginId) {

        byte[] img=null;
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();

            String get_query = "SELECT profile_img FROM login_info WHERE login_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(get_query);

            preparedStatement.setString(1, loginId);
            ResultSet resultSet= preparedStatement.executeQuery();

            while(resultSet.next()){
                img=resultSet.getBytes("profile_img");
            }

        }

        catch (SQLException e){
            e.printStackTrace();
        }

        return img;
    }

    @Override
    public void onBackPressed() {
        if(draw.isDrawerOpen(GravityCompat.START)){
            draw.closeDrawer(GravityCompat.START);
        }
        else {
            System.out.println("backPressed");
            super.onBackPressed();
            finish();
        }

    }

    private static Pair<String,Integer> display() {


        String user=val;
        System.out.println("User "+user);
        String res_name="xyz";
        int res_id=-1;
        //int studentID=0;
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement prep = connection.prepareStatement(query);
            prep.setString(1,user);
            ResultSet resultSet = prep.executeQuery();
            int id=-1;
            while(resultSet.next()) {
                id = resultSet.getInt("user_id");
                System.out.println("ID: "+id);
            }
            String query1 = "SELECT first_name FROM students WHERE student_id=?";
            PreparedStatement prep1 = connection.prepareStatement(query1);
            prep1.setInt(1,id);
            ResultSet res = prep1.executeQuery();
            String std_name = "null";
            while(res.next()){
                std_name=res.getString("first_name");
            }
            student= std_name;
            idx = id;
           Log.i("result " ,idx+" - "+student);

        }

        catch (SQLException e){
            e.printStackTrace();
            Log.i("error"," "+e.getMessage());
        }
        DatabaseManager.closeConnection();
        return new Pair<>(student,idx);
    }
    // In your activity or fragment:

    private static final int PICK_IMAGE_REQUEST = 1;

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"));

    }

    private void processPickedImage(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    roundedBitmap = getRoundedBitmap(bitmap);
                    profile.setBackground(getDrawable(R.drawable.circular_bg));
                    profile.setImageBitmap(roundedBitmap);
                    bytes=bitmapToByteArray(roundedBitmap);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            storeImageInDatabase(bytes, MainActivity.val);
                        }
                    }).start();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        else
            return;
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width, height);

        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, size, size);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, new Rect(0, 0, width, height), rect, paint);

        return output;
    }
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }
    private void storeImageInDatabase(byte[] imageBytes,String loginId) {

        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();

            String query = "UPDATE login_info SET profile_img=? WHERE login_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setBytes(1, bytes);
            preparedStatement.setString(2,loginId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }

        catch (SQLException e){
            e.printStackTrace();
        }
    }
}