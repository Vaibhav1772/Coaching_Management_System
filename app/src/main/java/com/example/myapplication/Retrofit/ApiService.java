package com.example.myapplication.Retrofit;

import com.example.myapplication.Model.Batches;
import com.example.myapplication.Model.Courses;
import com.example.myapplication.Model.Enrollment;
import com.example.myapplication.Model.ExamResult;
import com.example.myapplication.Model.Lecture;
import com.example.myapplication.Model.Status;
import com.example.myapplication.Model.Students;
import com.example.myapplication.Model.Tests;
import com.example.myapplication.Model.Users;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("/message")
    Call<Status>getStatus();

    @Headers("Content-Type: application/json")
    @POST("/login")
    Call<Status>checkLoginDetails(@Body Users user);

    @Headers("Content-Type: application/json")
    @GET("/loginDetails/{username}")
    Call<Status>getLoginDetails(@Path("username") String usr);

    @Headers("Content-Type: application/json")
    @POST("/createUser")
    Call<Status>createUser(@Body Users users);

    @Headers("Content-Type: application/json")
    @POST("/createStudent")
    Call<Status>createStudent(@Body Students students);

    @Headers("Content-Type: application/json")
    @GET("/studentDetails/{id}")
    Call<Students>getStudentDetails(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("/tests/count/{id}")
    Call<Status>getQuestionCount(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @PUT("/updatePassword")
    Call<Status>updatePassword(@Body Users user);

    @Headers("Content-Type: application/json")
    @GET("/tests/questions/{course_id}/{ques_no}")
    Call<Tests>getQuestions(@Path("course_id") String id,@Path("ques_no") String qno);

    @Headers("Content-Type: application/json")
    @PUT("/tests/answered/{course_id}/{ques_no}/{user_ans}")
    Call<Status>updateAnswer(@Path("course_id") String id,@Path("ques_no") String qno,@Path("user_ans") String ans);

    @Headers("Content-Type: application/json")
    @GET("/getStudentResults/{user_id}/{course_id}/{test_id}")
    Call<ExamResult>getStudentResult(@Path("user_id") String user_id,@Path("course_id") String course_id,@Path("test_id") String test_id);

    @Headers("Content-Type: application/json")
    @POST("/tests/updateResult/{user_id}/{course_id}/{test_id}")
    Call<Status>updateResult(@Path("user_id") String user_id,@Path("course_id") String course_id,@Path("test_id") String test);

    @Headers("Content-Type: application/json")
    @GET("/courses/all")
    Call<List<Courses>>getAllCourses();

    @Headers("Content-Type: application/json")
    @GET("/courses/{id}")
    Call<Courses>getCurrentCourse(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("/lecture/{id}")
    Call<Lecture>getCurrentLecture(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("/enrollments/{id}")
    Call<List<Enrollment>>getEnrolledBatches(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("/batches/all/{id}")
    Call<Set<Batches>> getBatchesById(@Path("id") String id);

    @Headers("Content-Type: application/json")
    @GET("/enrollments/{user_id}/{course_id}")
    Call<Status> doEnrollment(@Path("user_id") String id,@Path("course_id") String course_id);

}
