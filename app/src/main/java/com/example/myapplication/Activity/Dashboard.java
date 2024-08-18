package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;

import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView courses,faculty,batches,insights,tests,about;
    private ProgressBar bar;
    private ExtendedFloatingActionButton doubt;
    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
        View view= inflater.inflate(R.layout.dash_fragment, container, false);
        setEnterTransition(new Slide());
        setExitTransition(new Fade());
        courses=view.findViewById(R.id.courses);
        faculty=view.findViewById(R.id.faculty);
        batches=view.findViewById(R.id.batches);
        insights=view.findViewById(R.id.insights);
        tests=view.findViewById(R.id.tests);
        about=view.findViewById(R.id.about);
        doubt=view.findViewById(R.id.doubt_bar);
        bar=view.findViewById(R.id.progressBar);
        tests.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), TestSelection.class);
            startActivity(intent);
        });
        about.setOnClickListener(v -> {

           getParentFragmentManager().beginTransaction().setReorderingAllowed(true).
                    replace(R.id.fragmentContainerView,AboutUser.class,null).addToBackStack(null).commit();
        });
        doubt.setOnClickListener(v->{
            FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
            new UserDoubts().setEnterTransition(new android.transition.Slide());
            new UserDoubts().setExitTransition(new android.transition.Fade());
            fragmentTransaction.replace(R.id.fragmentContainerView,UserDoubts.class,null).addToBackStack(null).commit();
        });
        insights.setOnClickListener(v->{
            new Handler(Looper.getMainLooper()).postDelayed(()-> {
                requireActivity().runOnUiThread(() ->bar.setVisibility(View.VISIBLE));
            },1000);
            FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
            bar.setVisibility(View.GONE);
                    fragmentTransaction.replace(R.id.fragmentContainerView,TestAnalysis.class,null).addToBackStack(null).commit();
        });
        courses.setOnClickListener(v -> {
            new Handler(Looper.getMainLooper()).postDelayed(()-> {
                requireActivity().runOnUiThread(() ->bar.setVisibility(View.VISIBLE));
            },1000);
            FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
            bar.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.fragmentContainerView,CourseSelection.class,null).addToBackStack(null).commit();

        });
        batches.setOnClickListener(v->{
            new Handler(Looper.getMainLooper()).postDelayed(()-> {
                requireActivity().runOnUiThread(() ->bar.setVisibility(View.VISIBLE));
            },1000);
            FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
            bar.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.fragmentContainerView,BatchFragment.class,null).addToBackStack(null).commit();

        });
        faculty.setOnClickListener(v->{
            new Handler(Looper.getMainLooper()).postDelayed(()-> {
                requireActivity().runOnUiThread(() ->bar.setVisibility(View.VISIBLE));
            },1000);
            FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
            bar.setVisibility(View.GONE);
            fragmentTransaction.replace(R.id.fragmentContainerView,Faculty.class,null).addToBackStack(null).commit();

        });
        return view;
    }
}