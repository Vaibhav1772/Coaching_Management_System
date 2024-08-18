package com.example.myapplication.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Courses;
import com.example.myapplication.Model.Status;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;
import com.google.ai.client.generativeai.type.SafetyRating;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDescription extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView courseName,duration,instructor,amount,syllabus;
    private ProgressBar bar;
    private Button enroll;
    private ImageView image;
    public CourseDescription() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseDescription.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseDescription newInstance(String param1, String param2) {
        CourseDescription fragment = new CourseDescription();
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
        View view = inflater.inflate(R.layout.course_details, container, false);
        courseName=view.findViewById(R.id.course_name);
        duration=view.findViewById(R.id.duration);
        syllabus= view.findViewById(R.id.syllabus);
        amount=view.findViewById(R.id.amount);
        instructor=view.findViewById(R.id.instructors);
        enroll=view.findViewById(R.id.enroll_btn);
        image=view.findViewById(R.id.image);
        bar=view.findViewById(R.id.progressBar_des);
        bar.setVisibility(View.VISIBLE);
        TextView courseId=view.findViewById(R.id.course_des_id);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, result) -> {

            String id=result.getString("course_id");
            ApiService apiService= ApiClient.getClient().create(ApiService.class);
            Call<Courses> call = apiService.getCurrentCourse(id);
            call.enqueue(new Callback<Courses>() {
                @Override
                public void onResponse(Call<Courses> call, Response<Courses> response) {
                    if(response.isSuccessful() && response.body()!=null){

                        image.setImageDrawable(AppCompatResources.getDrawable(getActivity(),R.drawable.course_icn));
                        courseId.setText(String.format("Course ID: %s",response.body().getId().toString()));
                        courseName.setText(response.body().getCourseName());
                        syllabus.setText(response.body().getSyllabus());
                        duration.setText(String.format("%s days" ,response.body().getDuration()));
                        instructor.setText(response.body().getInstructors());
                        amount.setText(String.format("$ %s",response.body().getFeeStructure().toString()));
                        bar.setVisibility(View.GONE);;
                    }
                    else
                        Log.e("some api error",response.message());
                }

                @Override
                public void onFailure(Call<Courses> call, Throwable t) {
                    Log.e("Course des Fetch error",t.getMessage());
                }
            });
        });
        enroll.setOnClickListener(v -> {
            bar.setVisibility(View.VISIBLE);
            ApiService apiService=ApiClient.getClient().create(ApiService.class);
            Call<Status>call=apiService.doEnrollment(new SessionManager(getContext()).getSessionId(),courseId.getText().toString());
            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        Toast.makeText(getContext(),"Enrollment Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), DashboardDrawer.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        bar.setVisibility(ProgressBar.GONE);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable throwable) {
                    Log.e("Enrollment Failure",throwable.getMessage());
                }
            });

        });

        return view;
    }

}
