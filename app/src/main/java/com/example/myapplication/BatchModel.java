package com.example.myapplication;

public class BatchModel {
    String name,sdate,edate,days,teachers;
    String idx;
    public BatchModel(String idx,String name,String sdate,String edate,String days,String teachers){
        this.idx=idx;
        this.name=name;
        this.sdate=sdate ;
        this.edate=edate;
        this.days=days;
        this.teachers=teachers;
    }
}
