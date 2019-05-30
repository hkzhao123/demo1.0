package com.example.demo.mapper;
import com.example.demo.entity.Re;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;

@Mapper
@Component
public interface UserMapper {
    //登陆验证查询
    User loginin(@Param("u_id") String UserId, @Param("password") String pwd);

    //查询余额操作
    double sbalance(@Param("u_id") String UserId);

    //取钱操作
    void withdraw(@Param("u_id") String UserId, @Param("money") double money);

    //存钱操作
    void deposit(@Param("u_id") String UserId, @Param("money") double money);

    //买金操作
    //买金操作
    void buyGold(@Param("u_id") String UserId, @Param("money") double money);

    //卖金操作
    void SaleGold(@Param("u_id") String UserId,@Param("money") double money);

    //查找金余额
    double getGold(@Param("u_id") String UserId);

    //找到did对应订单的日期
    Date getDate(@Param("d_id") int d_id);
    //找到w对应的价格
    int getprice(@Param("day") int day);
    //查基金
    double sfund(@Param("u_id") String userId);
    //买入基金
    void buyfund(@Param("u_id")String userId , @Param("money") double money);
    //查定期
   Re  sregular(@Param("u_id")String userId);
   //插定期
    void iregular(@Param("u_id")String userId, @Param("c_time") Date today,@Param("d_time")Date nextday,@Param("d_money") double d_money,@Param("duration") int duration);
}