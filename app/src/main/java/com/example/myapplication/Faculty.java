package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CancellationException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Faculty#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Faculty extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView phn1,phn2;

    public Faculty() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Faculty.
     */
    // TODO: Rename and change types and number of parameters
    public static Faculty newInstance(String param1, String param2) {
        Faculty fragment = new Faculty();
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
        View view= inflater.inflate(R.layout.faculty, container, false);

        phn1=view.findViewById(R.id.textView36);
        phn2=view.findViewById(R.id.textView37);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Pair<String,String>pair= getPhoneData();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        phn1.setText(pair.first);
                        phn2.setText(pair.second);
                    }
                });
            }
        }).start();
        return view;
    }

    private Pair<String,String> getPhoneData() {


        try {
            Connection connection =DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT DISTINCT instructors,instructor_phn FROM courses WHERE instructors=? OR instructors=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,"VAIBHAV");
            preparedStatement.setString(2,"UJJWAL");
            ResultSet resultSet= preparedStatement.executeQuery();
            String [] phone=new String[2];

            int i=0;
            while(resultSet.next()){
                phone[i]=resultSet.getString("instructor_phn");
                i++;
            }
            String ph1=phone[0];
            String ph2=phone[1];
            DatabaseManager.closeConnection();
            return new Pair<>(ph1,ph2);
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        return new Pair<>("-1","-1");
    }
}