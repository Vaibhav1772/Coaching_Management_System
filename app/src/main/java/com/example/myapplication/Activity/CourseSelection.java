package com.example.myapplication.Activity;

import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.CourseAdapter;
import com.example.myapplication.Model.Courses;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseSelection extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ProgressBar bar;
    private ExtendedFloatingActionButton doubt;

    public CourseSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseSelection.
     */
    // TODO: Rename and change types and number of parameters
        public static CourseSelection newInstance(String param1, String param2) {
        CourseSelection fragment = new CourseSelection();
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
        View view = inflater.inflate(R.layout.courses, container, false);
        setEnterTransition(new Slide());
        setExitTransition(new Fade());
        ProgressBar bar=view.findViewById(R.id.progressBar_rec);

        recyclerView = view.findViewById(R.id.recycleview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new CourseAdapter(getContext(), new ArrayList<>(), course -> {}));
        ApiService apiService= ApiClient.getClient().create(ApiService.class);
        Call<List<Courses>> call=apiService.getAllCourses();
        bar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<Courses>>() {
            @Override
            public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {

                if(response.isSuccessful() && (response.body()!=null)) {

                    recyclerView.setAdapter(new CourseAdapter(getContext(), response.body(), course -> {
                        CourseDescription description=new CourseDescription();
                        Bundle bundle=new Bundle();
                        bundle.putString("course_id",course.getId().toString());
                        getParentFragmentManager().setFragmentResult("requestKey",bundle);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,description).addToBackStack(null).commit();
                    }));
                    bar.setVisibility(View.GONE);

                }
                else
                    Log.e("Course fetch null",response.message());
            }

            @Override
            public void onFailure(Call<List<Courses>> call, Throwable throwable) {
                Log.e("Course fetch error",throwable.getMessage());
            }
        });

        return view;
    }

}
