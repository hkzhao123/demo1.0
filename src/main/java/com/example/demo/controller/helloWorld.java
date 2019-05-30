package com.example.demo.controller;
import com.example.demo.entity.Re;
import com.example.demo.entity.User;
import com.example.demo.service.IRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Controller
public class helloWorld {
    @Autowired(required = true)
    private IRegService regService;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    Date date;

    //强制登陆页面
    @RequestMapping(value = "/")
    public String index() {
        return "loginPage";
    }

    //处理登陆的验证
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("userId") String userId,
                              @RequestParam("pwd") String pwd,
                              HttpServletRequest request) {
        User user = regService.regUser(userId, pwd);
        if (user != null) {
            ModelAndView modelAndView = new ModelAndView("mainPage");
            modelAndView.addObject("userName", user.getName());
            request.getSession().setAttribute("userName", user.getName());
            request.getSession().setAttribute("userId", user.getU_id());
            return modelAndView;

        } else {
            ModelAndView modelAndView = new ModelAndView("fail");
            return modelAndView;
        }
    }

    //方便返回主页
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView returnhome(HttpServletRequest request) {
        if (request.getSession().getAttribute("userName") != null) {
            ModelAndView modelAndView = new ModelAndView("mainPage");
            modelAndView.addObject("userName", request.getSession().getAttribute("userName"));
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("fail");
            return modelAndView;
        }
    }

    //查询余额
    @RequestMapping(value = "/user/selectbalance")
    public String selectbalance(HttpServletRequest request, Model model) {
        String userId = (String) request.getSession().getAttribute("userId");
        double balance = regService.sBalance(userId);
        model.addAttribute("userbalance", balance);
        return "checkMoney";
    }

    //返回取钱的页面
    @RequestMapping(value = "/user/withdraw", method = RequestMethod.GET)
    public String withdrwaview() {
        return "withdraw";
    }

    //验证取钱的细节
    @RequestMapping(value = "/user/withdraw", method = RequestMethod.POST)
    public String withdrawBalance(@RequestParam("money") String money
            , HttpServletRequest request) {
        Double m = Double.parseDouble(money);//要取得钱
        String userId = (String) request.getSession().getAttribute("userId");
        Double balance = regService.sBalance(userId);//取之前得余额
        if (balance > m) {
            regService.wBalance(userId, balance - m);//把剩下得钱进行更新
            return "success";
        } else {
            return "fail";
        }
    }

    //返回一个存钱的页面
    @RequestMapping(value = "/user/deposit", method = RequestMethod.GET)
    public String depoistview() {
        return "saveMoney";
    }

    //验证存钱的细节
    @RequestMapping(value = "/user/deposit", method = RequestMethod.POST)
    public String depositBalance(@RequestParam("money") String money,
                                 HttpServletRequest request) {
        Double m = Double.valueOf(money);
        String userId = (String) request.getSession().getAttribute("userId");
        Double balance = regService.sBalance(userId);
        Double afterbalance = balance + m;
        regService.dBalance(userId, afterbalance);
        return "success";
    }

    //返回一个转账的页面
    @RequestMapping(value = "/user/transfer", method = RequestMethod.GET)
    public String transferview() {
        return "transfer";
    }

    //实现转账细节
    @RequestMapping(value = "/user/transfer", method = RequestMethod.POST)
    public String transferBalance(@RequestParam("userId") String userId,
                                  @RequestParam("money") String money,
                                  HttpServletRequest request) {

        Double m = Double.valueOf(money);//转给人家的钱
        String selfUserId = (String) request.getSession().getAttribute("userId");//自己的Id
        Double s_balance = regService.sBalance(selfUserId);
        String othersUserId = userId;//人家的ID
        Double o_balance = regService.sBalance(othersUserId);
        //相当于自己取钱，人家存钱,service已经加了事务管理，保证了原子性
        if (s_balance > m) {
            //更新取钱后的值,更新存钱后的值
            regService.tBalance(selfUserId, s_balance - m, othersUserId, o_balance + m);
            return "success";
        } else {
            return "fail";
        }

    }

    @RequestMapping(value = "/user/buygold", method = RequestMethod.GET)
    public String bg1() {
        return "gold";
    }

    @RequestMapping(value = "/user/buygold", method = RequestMethod.POST)
    public String bg2(@RequestParam("money") String money, HttpServletRequest request) {
        Double m = Double.valueOf(money);
        String Uid = (String) request.getSession().getAttribute("userId");
        double s_balance = regService.sBalance(Uid);
        if (s_balance > m) {
            regService.buyGold(Uid, m);
            regService.wBalance(Uid, s_balance - m);
            return "success";
        } else return "fail";
    }

    @RequestMapping(value = "/user/salegold", method = RequestMethod.GET)
    public String bb1() {
        return "gold";
    }

    @RequestMapping(value = "/user/salegold", method = RequestMethod.POST)
    public String bb2(@RequestParam("money") String money, HttpServletRequest request) {
        Double m = Double.valueOf(money);
        String Uid = (String) request.getSession().getAttribute("userId");
        double sm=regService.getGold(Uid);
        if (m>sm) return "fail";
        double s_balance = regService.sBalance(Uid);
        regService.wBalance(Uid, s_balance + m);
        regService.SaleGold(Uid,m);
        return "success";
    }

    //得到理财页面
    @RequestMapping(value = "/user/management", method = RequestMethod.GET)
    public String manangementview() {
        return "manageMoney";
    }

    //得到定期页面
    @RequestMapping(value = "/user/management/regularSave", method = RequestMethod.GET)
    public String regularSaveview(HttpServletRequest request,Model model) {
       String userId  = (String)request.getSession().getAttribute("userId");
       Re re = regService.sRegular(userId);
          model.addAttribute("re",re);
          return "regularSave";
    }
      //处理定期
    @RequestMapping(value = "user/management/regularSave/in",method = RequestMethod.POST)
    public String regularSave(@RequestParam("money") double money,
                               @RequestParam("duration") int d
            ,HttpServletRequest request)
    {
        String userId  = (String) request.getSession().getAttribute("userId");
        Re re = regService.sRegular(userId);
        if(re==null)
        {
            Date today = new Date();
            int duration =  d;
            long time  = today.getTime();
            duration = duration*24*60*60*1000;
            time = time+duration;
            Date nextday = new Date(time);
            double d_money = money;
          double balance = regService.sBalance(userId);
          if(balance>=d_money)
          {
              regService.iRegular(userId,today,nextday,d_money,duration,balance-d_money);
             return  "success";
          }
          else
              return "fail";
        }
        else
            return "fail";
    }
    //得到基金页面
    @RequestMapping(value = "/user/management/fund", method = RequestMethod.GET)
    public String fundview(HttpServletRequest request,
                           Model model) {
        String userId  = (String)request.getSession().getAttribute("userId");
        double f_money = regService.sFund(userId);
        model.addAttribute("f_money",f_money);
        return "fund";
    }
    //处理基金页面 1 买 2卖
    @RequestMapping(value = "/user/management/fund/buy",method = RequestMethod.POST)
    public String buyfund(@RequestParam("buymoney") double money,
                          HttpServletRequest request)
    {   String userId = (String) request.getSession().getAttribute("userId");
        double f_money = regService.sFund(userId);
        double balance = regService.sBalance(userId);
        if(balance>=money)
        {
                 regService.bFund(userId,balance-money,f_money+money);
                 return "success";
        }
        else
        {
            return "fail";
        }
    }
    @RequestMapping(value = "/user/management/fund/sell",method = RequestMethod.POST)
    public String sellfund(@RequestParam("sellmoney") double money,
                          HttpServletRequest request)
    {   String userId = (String) request.getSession().getAttribute("userId");
        double f_money = regService.sFund(userId);
        double balance = regService.sBalance(userId);
            regService.bFund(userId,balance+money,f_money-money);
            return "success";
    }
    //得到黄金页面
    @RequestMapping(value = "/user/management/gold", method = RequestMethod.GET)
    public String goldview(Model md)
    {
        int x= regService.Getprice();
        md.addAttribute("gp",x);
        return "gold";
    }
}
