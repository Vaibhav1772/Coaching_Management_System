package com.example.myapplication;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashFragment newInstance(String param1, String param2) {
        DashFragment fragment = new DashFragment();
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
        View view=inflater.inflate(R.layout.dash_fragment, container, false);
        CardView courses =view.findViewById(R.id.cardView3);
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked"," "+true);
                    FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, DashCourse.class,null)
                        .setReorderingAllowed(true).addToBackStack("dash_frag");
                fragmentTransaction.commit();
            }
        });
        CardView about =view.findViewById(R.id.cardView6);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked"," "+true);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, About.class,null)
                        .setReorderingAllowed(true).addToBackStack("dash_frag");
                fragmentTransaction.commit();
            }
        });
        CardView batch =view.findViewById(R.id.batches);
        batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked"," "+true);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, Batches.class,null)
                        .setReorderingAllowed(true).addToBackStack("dash_frag");
                fragmentTransaction.commit();
            }
        });
        CardView faculty =view.findViewById(R.id.cardView4);
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked"," "+true);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, Faculty.class,null)
                        .setReorderingAllowed(true).addToBackStack("dash_frag");
                fragmentTransaction.commit();
            }
        });
        CardView test =view.findViewById(R.id.cardView2);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Clicked"," "+true);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, Tests.class,null)
                        .setReorderingAllowed(true).addToBackStack("dash_frag");
                fragmentTransaction.commit();
            }
        });
        CardView insight=view.findViewById(R.id.cardView5);
        insight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.fragmentContainerView, Insights.class,null)
                        .setReorderingAllowed(true).addToBackStack("dash_frag");
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}