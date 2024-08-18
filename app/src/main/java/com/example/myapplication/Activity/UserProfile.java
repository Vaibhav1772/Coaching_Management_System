package com.example.myapplication.Activity;

import static java.time.temporal.ChronoUnit.DAYS;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.Students;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.ApiClient;
import com.example.myapplication.Retrofit.ApiService;
import com.example.myapplication.Session.SessionManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends Fragment {


    private TextView name, mobile,dob,address,email,user_id;
    private ImageView profile;
    private ProgressBar progressBar;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public UserProfile() {
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
    public static UserProfile newInstance(String param1, String param2) {
        UserProfile fragment = new UserProfile();
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
        View view= inflater.inflate(R.layout.fragment_personal_info, container, false);
        name=view.findViewById(R.id.textView16);
        dob=view.findViewById(R.id.textView17);
        email=view.findViewById(R.id.textView18);
        mobile=view.findViewById(R.id.textView21);
        address=view.findViewById(R.id.textView22);
        profile=view.findViewById(R.id.profile);
        progressBar=view.findViewById(R.id.progressBar3);
        user_id=view.findViewById(R.id.reg_id);
        TextView attendance=view.findViewById(R.id.attendence);
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        firebaseStorage=FirebaseStorage.getInstance();

        user_id.setText(new SessionManager(requireActivity()).getSessionId());
        SessionManager sessionManager=new SessionManager(getContext());
        LocalDate previousDate=LocalDate.parse(sessionManager.getDate());
        fetchAndDisplayImage();
        LocalDate currentDate=LocalDate.now();
        long days=DAYS.between(previousDate,currentDate);
        attendance.setText(String.format("%s", days));
        new Thread(() -> {
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<Students> call = apiService.getStudentDetails(new SessionManager(requireActivity()).getSessionId());
            call.enqueue(new Callback<Students>() {
                @Override
                public void onResponse(Call<Students> call, Response<Students> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Students students = response.body();
                        requireActivity().runOnUiThread(() -> {
                            name.setText(students.getName());
                            dob.setText((students.getDateOfBirth()) == null ? currentDate.toString() : students.getDateOfBirth().toString());
                            email.setText(students.getEmail());
                            mobile.setText(students.getPhone());
                            address.setText(((students.getAddress())== null ? "xyz" : students.getAddress()));
                        });
                    } else {
                        Log.i("GetStudentDetailsStatus", "false");
                    }
                }

                @Override
                public void onFailure(Call<Students> call, Throwable throwable) {
                    Log.e("error inn studentDetails Fetching", throwable.getMessage());
                }
            });
        }).start();
        return view;
    }

    private void fetchAndDisplayImage() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        new Thread(()->{
            StorageReference fileReference = storageReference.child("profile_images/" +user_id.getText().toString()+ ".png");
            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {

                Picasso.get().load(uri).transform(new CircularTransform()).fit().centerCrop(5).into(profile);

                requireActivity().runOnUiThread(()->progressBar.setVisibility(ProgressBar.GONE)); // Hide progress bar

            }).addOnFailureListener(e -> {

                Toast.makeText(requireActivity(), "Failed to load image", Toast.LENGTH_SHORT).show();

                requireActivity().runOnUiThread(()->progressBar.setVisibility(ProgressBar.GONE));// Hide progress bar
            });
        }).start();
    }
}
