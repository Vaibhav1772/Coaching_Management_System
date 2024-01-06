package com.example.myapplication;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;
//----To fix email issuse to get email by email for my_db-----
public class My_Database {
    public static boolean check=false,pres=false; //--Login checking flags--
    public static Scanner sc= new Scanner(System.in);
   /* public static void main(String[] args) {

        //login("Vaibhav","Vaibhavcse124");
        String login_id="Ujjwal@123";
        String passwrd="ujjwal@123";
        if(valid_log(login_id,passwrd)) {
            System.out.println("Already Logged In...");
            display_info(login_id);
            display_course();
            course_description("CALCULUS");
        }
        else {
            if(!check && pres)
                System.out.println("INCORRECT CREDENTIALS...");
                //login(login_id, passwrd);
            else if(!check && !pres){
                System.out.println("USER NOT EXISTS. KINDLY SIGN-UP....");
                insert_info(login_id,passwrd);
            }

        }
    }*/
    public static int login(String login_id,String pass){     //---login into database---
        String url = "jdbc:mysql://192.168.147.39:3306/my_db";
        String username = "root";
        String password = "Vaibhav.1772@";
        int studentID=0;
        //login("Vaibhav","Vaibhavcse124");
        try{

            String query="INSERT INTO login_info (login_id,passwrd,success) VALUES(?,?,1)";
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());

            PreparedStatement prep_st = connection.prepareStatement(query);
            //prep_st.setInt(1, studentID);
            prep_st.setString(1, login_id);
            prep_st.setString(2, hashedPassword);
            prep_st.executeUpdate();
            prep_st.close();
            String query1="SELECT * FROM login_info";
            ResultSet resultSet = statement.executeQuery(query1);
            while(resultSet.next()){
                String log=resultSet.getString("login_id");
                String pass1=resultSet.getString("passwrd");
                System.out.println("Login-Id:"+log+"\n"+"Password:"+pass1+"\n");
            }

            String query_id="SELECT user_id FROM login_info WHERE passwrd=?";
            PreparedStatement prep_st_id = connection.prepareStatement(query_id);
            //prep_st.setInt(1, studentID);

            prep_st_id.setString(1, hashedPassword);
            ResultSet res = prep_st_id.executeQuery();


            while (res.next()) {
                studentID = res.getInt("user_id");
            }
            res.close();
            prep_st_id.close();
            return studentID;
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentID;
    }
    public static boolean valid_log(String loginID,String pass){
        String url = "jdbc:mysql://192.168.147.39:3306/my_db";
        String username = "root";
        String password = "Vaibhav.1772@";

        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            //String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());
            //System.out.println("hashedPassword "+hashedPassword);
            String query="SELECT login_id,passwrd FROM login_info";
            ResultSet resultSet = statement.executeQuery(query);
            boolean temp=false;
            while(resultSet.next())
            {
                String log=resultSet.getString("login_id");

                String passwrd=resultSet.getString("passwrd");

                boolean match = BCrypt.checkpw(pass, passwrd);

                if(match && loginID.equals(log)) {
                    check=true;
                    pres=true;
                    //temp=true;
                    return true;
                }
                else if(loginID.equals(log) ) {
                    //check=false;
                    pres=true;
                }
            }
            //System.out.println(check+" "+pres+ " "+ temp);
            if(pres && !temp)
                return false;
        }
        catch (ClassNotFoundException e) {
            //e.printStackTrace();
            check=false;
            pres=false;
            return false;
        } catch (SQLException e) {
            check=false;
            pres=false;
            //e.printStackTrace();
            return false;
        }
        return false;
    }   //---Authenicating Credentials---

    public static void insert_info(String loginID,String passwrd){
        String url = "jdbc:mysql://192.168.147.39/my_db";
        String username = "root";
        String password = "Vaibhav.1772@";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String fname,lname,phn,email,addrs,paymt_history,enroll_date,dob;
            System.out.println("Fname lname phn email addrs paymt_history enroll_date dob");
            fname=sc.nextLine();
            lname=sc.nextLine();
            phn=sc.nextLine();
            email=sc.nextLine();
            addrs=sc.nextLine();
            paymt_history=sc.nextLine();
            enroll_date=sc.nextLine();
            dob=sc.nextLine();

            int studentID=login(loginID,passwrd);
            String query="INSERT INTO students(first_name,last_name,date_of_birth,phone,email,address,enrollment_date,payment_history,student_id) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement prep_st = connection.prepareStatement(query);
            prep_st.setString(1, fname);
            prep_st.setString(2, lname);
            prep_st.setString(3, dob);
            prep_st.setString(4, phn);
            prep_st.setString(5, email);
            prep_st.setString(6, addrs);
            prep_st.setString(7, enroll_date);
            prep_st.setString(8, paymt_history);
            prep_st.setInt(9, studentID);
            prep_st.executeUpdate();
            prep_st.close();

        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    } //---Inserting Student Data---

    public static List<String> get_details(String loginID){
        String url = "jdbc:mysql://localhost:3306/my_db";
        String username = "root";
        String password = "Vaibhav.1772@";
        ArrayList<String> details =new ArrayList<>();
        String [] arr={"first_name","last_name","date_of_birth","phone",
                "email","address","enrollment_date","payment_history"};
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query="SELECT first_name,last_name,date_of_birth,phone,email,address,enrollment_date,payment_history FROM students WHERE student_id=?";
            PreparedStatement prep_st= connection.prepareStatement(query);

            String get_id="SELECT user_id FROM login_info WHERE login_id=?";
            PreparedStatement prep_st_id= connection.prepareStatement(get_id);

            prep_st_id.setString(1,loginID);
            ResultSet resultSet=prep_st_id.executeQuery();

            int id=0;

            while(resultSet.next()){
                id=resultSet.getInt("user_id");
            }
            System.out.println(id);
            prep_st.setInt(1,id);
            ResultSet result_Set= prep_st.executeQuery();
            int i=0;

            while(result_Set.next() ) {
                while(i < arr.length){
                    details.add(result_Set.getString(arr[i]));
                    i++;
                }
            }
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return details;
    } //---Getting details in ArrayList---
    public static void display_info(String loginID) {
        List<String> info= get_details(loginID);
        System.out.println(info);
        //---more features to add to connect to javafx---
    } //---Displaying all Student details---

    public static void display_course() {
        String url = "jdbc:mysql://localhost:3306/my_db";
        String username = "root";
        String password = "Vaibhav.1772@";
        int studentID=0;
        //login("Vaibhav","Vaibhavcse124");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT course_name FROM courses ";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("COURSES:"+"\n");
            while(resultSet.next())
            {
                System.out.println(resultSet.getString("course_name"));
            }
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    } //---display course name---

    public static void course_description(String course) {
        String url = "jdbc:mysql://localhost:3306/my_db";
        String username = "root";
        String password = "Vaibhav.1772@";
        int studentID=0;
        //login("Vaibhav","Vaibhavcse124");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query="SELECT des FROM courses WHERE course_name=?";
            PreparedStatement prep= connection.prepareStatement(query);
            prep.setString(1,course);
            ResultSet resultSet=prep.executeQuery();
            System.out.println("COURSE DESCRIPTION....");
            System.out.println(course);
            while(resultSet.next()){
                System.out.println(resultSet.getString("des"));
            }

        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    } //---display course description---
    public static void get_course(String course) {
        String url = "jdbc:mysql://localhost:3306/my_db";
        String username = "root";
        String password = "Vaibhav.1772@";
        int studentID=0;
        //login("Vaibhav","Vaibhavcse124");
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query="";
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
