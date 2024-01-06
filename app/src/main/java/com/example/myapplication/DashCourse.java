package com.example.myapplication;

import static com.example.myapplication.MainActivity.val;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashCourse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashCourse extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static String cid;
    ArrayList<CourseModel> courseModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    public DashCourse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashCourse.
     */
    // TODO: Rename and change types and number of parameters
    public static DashCourse newInstance(String param1, String param2) {
        DashCourse fragment = new DashCourse();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ArrayList<CourseModel>[] arrayList = new ArrayList[]{new ArrayList<>()};
        View view= inflater.inflate(R.layout.courses, container, false);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                arrayList[0]=fetch_data();
                Log.i("Data:-",""+arrayList[0]);
                if(arrayList[0].size()==0){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setVisibility(View.INVISIBLE);
                            ImageView icon=view.findViewById(R.id.imageView54);
                            icon.setVisibility(View.VISIBLE);
                            TextView msg=view.findViewById(R.id.textView61);
                            msg.setVisibility(View.VISIBLE);

                        }
                    });
                }
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerAdapter adapter = new RecyclerAdapter(getContext(), arrayList[0]);
                            Log.i("status ", "recycle");
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });


        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView id= view.findViewById(R.id.course_id);
                        cid= id.getText().toString();
                        Intent intent = new Intent(getContext(), BuyCourse.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        thread.start();


        Log.i("print "," "+arrayList[0]);

        return view;
    }

    public ArrayList<CourseModel> fetch_data(){

        //int studentID=0;
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT course_id FROM enrollments WHERE student_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,get_id(val));
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Character> arr=new ArrayList<>();
            while(resultSet.next()) {
                int index=resultSet.getInt("course_id");
                arr.add((char)(index+'0'));
            }
            StringBuilder sb=new StringBuilder();
            for(Character ch: arr){
                sb.append(ch);
                sb.append(',');
            }
            sb.deleteCharAt(sb.length()-1);
            String values=sb.toString();
            System.out.println("values"+values+"!");
            String query1 = "SELECT course_id,course_name,des FROM courses WHERE course_id NOT IN ("+values+")";

            ResultSet res= statement.executeQuery(query1);
            while(res.next()) {
                int index1=res.getInt("course_id");
                String str = Integer.toString(index1);
                courseModelArrayList.add(new CourseModel(str,
                        res.getString("course_name"), res.getString("des")));
            }
            connection.close();
        }

        catch (SQLException e){
            e.printStackTrace();
            Log.i("error"," "+e.getMessage());
        }
        return courseModelArrayList;
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
}