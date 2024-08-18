package com.example.myapplication.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.BatchAdapter;
import com.example.myapplication.Model.Batches;
import com.example.myapplication.Model.Enrollment;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.var;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rcView;

    public BatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BatchFragment newInstance(String param1, String param2) {
        BatchFragment fragment = new BatchFragment();
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
        View view = inflater.inflate(R.layout.batches, container, false);
        rcView=view.findViewById(R.id.recycleview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setItemPrefetchEnabled(true);
        rcView.setLayoutManager(linearLayoutManager);
        rcView.setAdapter(new BatchAdapter(getContext(), new ArrayList<>(), batches -> {}));

        ApiService apiService= ApiClient.getClient().create(ApiService.class);

        Call<Set<Batches>> call = apiService.getBatchesById(new SessionManager(getContext()).getSessionId());
        call.enqueue(new Callback<Set<Batches>>() {
            @Override
            public void onResponse(Call<Set<Batches>> call, Response<Set<Batches>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    List<Batches> batchesSet = new ArrayList<>(response.body());
                    rcView.setAdapter(new BatchAdapter(getContext(),batchesSet,batches -> {
                        getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new Classroom()).addToBackStack(null).commit();
                    }));
                }
            }

            @Override
            public void onFailure(Call<Set<Batches>> call, Throwable t) {
                Log.e("Enrollment list fetch error",t.getMessage());
            }
        });

        return view;
    }
}
