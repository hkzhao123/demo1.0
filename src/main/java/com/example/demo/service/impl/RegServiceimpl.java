package com.example.demo.service.impl;
import com.example.demo.entity.Re;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Calendar;
import java.util.Date;

@Service
public class RegServiceimpl implements IRegService {
    @Autowired
    private UserMapper userMapper;

    public User regUser(String userId, String pwd) {
        User user = userMapper.loginin(userId, pwd);
        return user;
    }

    @Override
    public double sBalance(String userId) {
        double balance = userMapper.sbalance(userId);
        return balance;
    }

    @Override
    public void wBalance(String userId, double money) {
        userMapper.withdraw(userId, money);
    }

    @Override

    public void dBalance(String userId, double money) {
        userMapper.deposit(userId, money);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void tBalance(String selfId, double s_balance, String othersId, double o_balance) {
        userMapper.withdraw(selfId, s_balance);
        userMapper.deposit(othersId, o_balance);
    }

    public void buyGold(String userId, double money) {
        Date dat = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        double pr=userMapper.getprice(w);
        double vm=money;
        double am=vm/pr;
        userMapper.buyGold(userId,am);
    }

    public void SaleGold(String userId,double  money) {
        Date dat = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        double pr=userMapper.getprice(w);
        double vm=money;
        double am=vm/pr;
        userMapper.SaleGold(userId,am);
    }

    public double getGold(String userId)
    {
        return userMapper.getGold(userId)*Getprice();
    }
    public int Getprice()
    {
        Date d = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK);
        return userMapper.getprice(w);
    }
    @Override
    public double sFund(String userId) {
        double f_money = userMapper.sfund(userId);
        return  f_money;}

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void bFund(String userId, double balance, double f_money) {
        userMapper.withdraw(userId,balance);
        userMapper.buyfund(userId,f_money);
    }

    public Re sRegular(String userId) {
        Re re = userMapper.sregular(userId);
        return re;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void iRegular(String userId, Date today, Date nextday, double money, int duration,double balance) {
        userMapper.iregular(userId,today,nextday,money,duration);
        userMapper.withdraw(userId,balance);
    }
}