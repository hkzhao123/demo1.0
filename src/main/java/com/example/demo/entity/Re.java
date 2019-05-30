package com.example.demo.entity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Re {
 private String u_id;
 private Date c_time;
 private Date d_time;
 private double d_money;
 private int duration;

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_id() {
        return u_id;
    }

    public Date getC_time() {
        return c_time;
    }

    public Date getD_time() {
        return d_time;
    }

    public double getD_money() {
        return d_money;
    }

    public int getDuration() {
        return duration;
    }

    public void setC_time(Date c_time) {
        this.c_time = c_time;
    }

    public void setD_money(double d_money) {
        this.d_money = d_money;
    }

    public void setD_time(Date d_time) {
        this.d_time = d_time;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String sd(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒");
        String string = dateFormat.format(c_time);
        return string;
    }
    public String ed(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分ss秒");
        String string  = dateFormat.format(d_time);
        return string;
    }
}
