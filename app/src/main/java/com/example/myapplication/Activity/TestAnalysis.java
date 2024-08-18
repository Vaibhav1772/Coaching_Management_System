package com.example.myapplication.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.myapplication.Model.ExamResult;
import com.example.myapplication.Model.Tests;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.checkerframework.checker.units.qual.C;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestAnalysis extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView avg,grad,remarks;

    public TestAnalysis() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestAnalysis.
     */
    // TODO: Rename and change types and number of parameters
    public static TestAnalysis newInstance(String param1, String param2) {
        TestAnalysis fragment = new TestAnalysis();
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
        View view = inflater.inflate(R.layout.charts, container, false);
        avg=view.findViewById(R.id.avg_val);
        grad=view.findViewById(R.id.grade_val);
        remarks=view.findViewById(R.id.remark_val);

        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.submit(this::getAnalysisData);

        return view;
    }
    private void getAnalysisData(){
        ApiService apiService= ApiClient.getClient().create(ApiService.class);
        SessionManager sessionManager=new SessionManager(getContext());

        Call<ExamResult> call=apiService.getStudentResult(sessionManager.getSessionId(),"1","1");
        call.enqueue(new Callback<ExamResult>() {
            @Override
            public void onResponse(Call<ExamResult> call, Response<ExamResult> response) {
                if(response.isSuccessful() && response.body()!=null){
                    createPieChart(response.body());
                    showDetails(response.body());
                }
                else {
                    Log.e("Error empty response body result",response.message());
                }
            }

            @Override
            public void onFailure(Call<ExamResult> call,Throwable throwable) {
                Log.e("Error fetching result",throwable.getMessage());
            }
        });
    }
    private void createPieChart(ExamResult examResult){
        AnyChartView anyChartView = getView().findViewById(R.id.pie_chart);
        anyChartView.setProgressBar(getView().findViewById(R.id.progressBar));
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Correct", examResult.getCorrectAns()));
        data.add(new ValueDataEntry("Wrong", examResult.getWrongAns()));
        data.add(new ValueDataEntry("Unattempted", examResult.getUnattempted()));
        pie.data(data);
        pie.title("Exam Result Analysis");


        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Question Attempted Chart")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
    }
    private void showDetails(ExamResult examResult){

        BigDecimal marks=examResult.getAverageMarks().multiply(new BigDecimal(100));
        avg.setText(String.valueOf(marks));
        grad.setText(examResult.getGrade());
        remarks.setText(examResult.getRemarks());
    }
}
