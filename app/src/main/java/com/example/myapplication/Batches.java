package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Batches#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Batches extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<BatchModel> batchModelArrayList = new ArrayList<>();
    RecyclerView recyclerView;

    public Batches() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Batches.
     */
    // TODO: Rename and change types and number of parameters
    public static Batches newInstance(String param1, String param2) {
        Batches fragment = new Batches();
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
        final ArrayList<BatchModel>[] arrayList = new ArrayList[]{new ArrayList<>()};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                arrayList[0]=fetch_data();
                Log.i("Data:-",""+arrayList[0]);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerAdapterBatch adapter = new RecyclerAdapterBatch(getContext(), arrayList[0]);
                        Log.i("status ","recycle");
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });

        View view= inflater.inflate(R.layout.courses, container, false);
        recyclerView = view.findViewById(R.id.recycleview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        thread.start();
        Log.i("print "," "+arrayList[0]);

        return view;
    }

    public ArrayList<BatchModel> fetch_data(){

        //int studentID=0;
        try {

            Connection connection = DatabaseManager.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT batch_id,classroom,start_date,end_date,schedule,instructors FROM batches";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                int index=resultSet.getInt("batch_id");
                String str=Integer.toString(index);
                batchModelArrayList.add(new BatchModel(str,resultSet.getString("classroom"),
                        resultSet.getString("start_date"),resultSet.getString("end_date"),
                        resultSet.getString("schedule"),resultSet.getString("instructors")));
            }
            connection.close();
        }

        catch (SQLException e){
            e.printStackTrace();
            Log.i("error"," "+e.getMessage());
        }
        return batchModelArrayList;
    }
}