package com.example.myapplication;

import static com.example.myapplication.MainActivity.val;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<TextView> list = new ArrayList<>();
    TextView name,dob,address,phone,email,enroll_date,regId,attendance;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView profile;
    public PersonalInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInfo newInstance(String param1, String param2) {
        PersonalInfo fragment = new PersonalInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_personal_info, container, false);
        name=view.findViewById(R.id.textView16);
        dob=view.findViewById(R.id.textView17);
        email=view.findViewById(R.id.textView18);
        phone=view.findViewById(R.id.textView21);
        address=view.findViewById(R.id.textView22);
        enroll_date=view.findViewById(R.id.textView23);
        regId=view.findViewById(R.id.reg_id);
        attendance=view.findViewById(R.id.attendence);
        profile=view.findViewById(R.id.profile);
        list.add(name);
        list.add(dob);
        list.add(email);
        list.add(phone);
        list.add(address);
        list.add(enroll_date);
        list.add(regId);
        final ArrayList<String>[] getData = new ArrayList[]{new ArrayList<>()};
        ProgressBar progressBar=view.findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
               getData[0] =getInfo(MainActivity.val);

                if(getProfileImage(val)!=null){
                    byte[] imgData=getProfileImage(MainActivity.val);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                    Bitmap newBitmap=getRoundedBitmap(bitmap);
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            profile.setImageBitmap(newBitmap);
                        }
                    });
                }
               requireActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       progressBar.setVisibility(View.VISIBLE);
                       for(int i=0;i<list.size();i++){
                           list.get(i).setText(getData[0].get(i));
                       }
                   }

               });
               int days=getAttenndance(MainActivity.val);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        attendance.setText(Integer.toString(days));
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
        progressBar.setVisibility(View.INVISIBLE);
        return view;
    }

    private ArrayList<String> getInfo(String loginId) {
        ArrayList<String> listArray=new ArrayList<>();


        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String get_query="SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement prep =connection.prepareStatement(get_query);
            prep.setString(1,loginId);
            ResultSet res= prep.executeQuery();
            int id=-1;
            while(res.next())
                id=res.getInt("user_id");
            String query="SELECT first_name,date_of_birth,email,phone,address,enrollment_date,student_id FROM students WHERE student_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);

            preparedStatement.setInt(1,id);

            ResultSet resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                listArray.add(resultSet.getString("first_name"));
                listArray.add(resultSet.getString("date_of_birth"));
                listArray.add(resultSet.getString("email"));
                listArray.add(resultSet.getString("phone"));
                listArray.add(resultSet.getString("address"));
                listArray.add(resultSet.getString("enrollment_date"));
                listArray.add(Integer.toString(resultSet.getInt("student_id")));
            }
            preparedStatement.close();

        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return listArray;
    }
    private int getAttenndance(String loginId) {
        ArrayList<String> listArray=new ArrayList<>();


        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String get_query="SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement prep =connection.prepareStatement(get_query);
            prep.setString(1,loginId);
            ResultSet res= prep.executeQuery();
            int id=-1;
            while(res.next())
                id=res.getInt("user_id");
            String query="SELECT DISTINCT curr_date FROM attendance WHERE student_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);

            preparedStatement.setInt(1,id);

            ResultSet resultSet=preparedStatement.executeQuery();
            int countDays=0;
            while(resultSet.next()){
                countDays++;
            }
            preparedStatement.close();
            return countDays;
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
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

}