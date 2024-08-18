package com.example.myapplication.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Barrier;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.Batches;
import com.example.myapplication.Model.Lecture;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Classroom extends Fragment {


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
    public Classroom() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Classroom.
     */
    // TODO: Rename and change types and number of parameters
    public static Classroom newInstance(String param1, String param2) {
        Classroom fragment = new Classroom();
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
        View view = inflater.inflate(R.layout.classroom, container, false);
        WebView webView=view.findViewById(R.id.video);
        ProgressBar bar=view.findViewById(R.id.progressBar4);
        TextView title=view.findViewById(R.id.textView2);
        EditText feedback= view.findViewById(R.id.feedback);
        Button submit=view.findViewById(R.id.button);
        bar.setVisibility(View.VISIBLE);
        ApiService apiService= ApiClient.getClient().create(ApiService.class);
        Call<Lecture>call=apiService.getCurrentLecture("1");
        call.enqueue(new Callback<Lecture>() {
            @Override
            public void onResponse(Call<Lecture> call, Response<Lecture> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    webView.loadData(response.body().getLectureLink(), "text/html", "utf-8");
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient(){

                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                            bar.setVisibility(View.VISIBLE);
                            super.onPageStarted(view, url, favicon);
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            bar.setVisibility(View.GONE);
                            super.onPageFinished(view, url);

                        }
                    });
                    Batches batches =response.body().getBatch();
                    title.setText(String.format("%s",batches.getClassroom()));
                    bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Lecture> call, Throwable t) {
                Log.e("No lecture",t.getMessage());
            }
        });
        RatingBar ratingBar=view.findViewById(R.id.ratingBar);
        float rate=ratingBar.getRating();
        if(feedback.getText()!=null){
            submit.setOnClickListener(v->{
                feedback.getText().clear();
                Toast.makeText(getContext(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
            });

        }

        return view;
    }
}
