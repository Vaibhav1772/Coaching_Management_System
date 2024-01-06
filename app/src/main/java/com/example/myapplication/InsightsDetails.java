package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsightsDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsightsDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView score,correct_ans,wrong_ans;
    public InsightsDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InsightsDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static InsightsDetails newInstance(String param1, String param2) {
        InsightsDetails fragment = new InsightsDetails();
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
       View view= inflater.inflate(R.layout.insights, container, false);
       score=view.findViewById(R.id.textView41);
       correct_ans=view.findViewById(R.id.textView42);
       wrong_ans=view.findViewById(R.id.textView43);
       new Thread(new Runnable() {
           @Override
           public void run() {
               getData();
           }
       }).start();
       return view;
    }
    private void getData() {
        try {
            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT COUNT(test_id) AS total FROM tests WHERE course_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,1);
            ResultSet resultSet= preparedStatement.executeQuery();
            final int total[]=new int[1];
            while (resultSet.next()) {
                int val=resultSet.getInt("total");
                Log.i("val",""+val);
                total[0]=val;
            }
            String query1 = "SELECT COUNT(correct_ans) AS total_correct FROM tests WHERE correct_ans=user_ans";
            PreparedStatement preparedStatement1=connection.prepareStatement(query1);
            preparedStatement.setInt(1,1);
            ResultSet resultSet1= preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                int val=resultSet1.getInt("total_correct");
                Log.i("val1",""+val);
                getActivity().runOnUiThread(() -> correct_ans.setText(Integer.toString(val)));
                getActivity().runOnUiThread(() -> score.setText(val +"/"+Integer.toString(total[0])));
            }
            String query2 = "SELECT COUNT(correct_ans) AS total_wrong FROM tests WHERE correct_ans!=user_ans";
            PreparedStatement preparedStatement2=connection.prepareStatement(query2);
            preparedStatement.setInt(1,1);
            ResultSet resultSet2= preparedStatement2.executeQuery();
            while (resultSet2.next()) {
                int val=resultSet2.getInt("total_wrong");
                Log.i("val2",""+val);
                getActivity().runOnUiThread(() -> wrong_ans.setText(Integer.toString(val)));
            }
            DatabaseManager.closeConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
            Log.i("error"," "+e.getMessage());
        }

    }
}