package com.example.demo.service;
import com.example.demo.entity.Re;
import com.example.demo.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface IRegService {
//登陆的验证API

    User regUser(String userId, String pwd);
    //查询余额的API

    double sBalance(String userId);
    //取钱操作的API

    void wBalance(String userId, double money);
    //存钱操作的API

    void dBalance(String userId, double money);

    //转账操作的api可以由上面2个完成，可以看作为A转账给B，A取钱，B存钱
    @Transactional
    void tBalance(String selfId, double s_balance, String othersId, double o_balance);

    void buyGold(String userId, double money);

    void SaleGold(String userId, double money);

    double getGold(String userId);

    int Getprice();
    //查询基金额度
    double sFund(String userId);
    //处理插入基金 涉及事物管理

      void bFund(String userId, double balance,double f_money);

    //查定期
   Re sRegular(String userId);
   //插定期
   void iRegular(String userId, Date today,Date nextday,double money,int duration,double balance);
}
