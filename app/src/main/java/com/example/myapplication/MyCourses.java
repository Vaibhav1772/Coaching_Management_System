package com.example.myapplication;

import static com.example.myapplication.MainActivity.val;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCourses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCourses extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<CourseModel> courseModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    public MyCourses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCourses.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCourses newInstance(String param1, String param2) {
        MyCourses fragment = new MyCourses();
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
        final ArrayList<CourseModel>[] arrayList = new ArrayList[]{new ArrayList<>()};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                arrayList[0]=fetch_data();
                Log.i("Data:-",""+arrayList[0]);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerAdapterMyCourse adapter = new RecyclerAdapterMyCourse(getContext(), arrayList[0]);
                        Log.i("status ","recycle");
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        View view=inflater.inflate(R.layout.courses, container, false);
        recyclerView = view.findViewById(R.id.recycleview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        thread.start();
        Log.i("print "," "+arrayList[0]);

        return view;
    }
    private int get_id(String loginId){
        int idx = -1;
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();

            String get_query = "SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(get_query);

            preparedStatement.setString(1, loginId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                idx = resultSet.getInt("user_id");
            preparedStatement.close();

        }

        catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i("idx",Integer.toString(idx));
        return idx;
    }
    public ArrayList<CourseModel> fetch_data(){

        //int studentID=0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT course_id FROM enrollments WHERE student_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,get_id(val));
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int idx=resultSet.getInt("course_id");
                Statement statement1 = connection.createStatement();
                String query1 = "SELECT course_id,course_name,des FROM courses WHERE course_id=?";
                PreparedStatement preparedStatement1= connection.prepareStatement(query1);
                preparedStatement1.setInt(1,idx);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                while(resultSet1.next()) {
                    int index=resultSet1.getInt("course_id");
                    String str=Integer.toString(index);
                    courseModelArrayList.add(new CourseModel(str,
                            resultSet1.getString("course_name"),resultSet1.getString("des")));
                }
            }
            DatabaseManager.closeConnection();
        }

        catch (ClassNotFoundException e){
            e.printStackTrace();
            Log.i("error"," "+e.getMessage());
        }
        catch (SQLException e){
            e.printStackTrace();
            Log.i("error"," "+e.getMessage());
        }
        return courseModelArrayList;
    }
}