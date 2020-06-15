package com.ls.controller;

import com.ls.entity.User;
import com.ls.entity.monitor.MonitorData;
import com.ls.entity.monitor.MonitorTarget;
import com.ls.service.UserService;
import com.ls.service.monitor.MonitorDataService;
import com.ls.service.monitor.MonitorTargetService;
import com.ls.util.Consts;
import com.ls.util.CryptographyUtil;
import com.ls.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

/**
 * 用户控制器
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private MonitorTargetService monitorTargetService;

    @Autowired
    private MonitorDataService monitorDataService;

    /**
     * 用户注册
     */
    @ResponseBody
    @PostMapping("/register")
    public Map<String,Object> register(@Valid User user, BindingResult bindingResult){
        Map<String,Object> map = new HashMap<>();
        if(bindingResult.hasErrors()){
            map.put("success",false);
            map.put("errorInfo",bindingResult.getFieldError().getDefaultMessage());
        }else if(userService.findByUserName(user.getUserName())!=null){
            map.put("success",false);
            map.put("errorInfo","用户名已存在，请更换！");
        }else if(userService.findByEmail(user.getEmail())!=null){
            map.put("success",false);
            map.put("errorInfo","邮箱已存在，请更换！");
        }else{
            user.setPassword(CryptographyUtil.md5(user.getPassword(),CryptographyUtil.SALT));
            user.setRegistrationDate(new Date());
            user.setLatelyLoginTime(new Date());
            user.setHeadPortrait("tou.jpg");
            userService.save(user);
            map.put("success",true);
        }
        return map;
    }


    /**
     * 用户登录
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String,Object> login(User user, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        if(StringUtil.isEmpty(user.getUserName())){
            map.put("success",false);
            map.put("errorInfo","请输入用户名！");
        }else if(StringUtil.isEmpty(user.getPassword())){
            map.put("success",false);
            map.put("errorInfo","请输入密码！");
        }else{
            Subject subject = SecurityUtils.getSubject();
            //加密后的密码
            String password = CryptographyUtil.md5(user.getPassword(), CryptographyUtil.SALT);

            UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),password);
            try {
                subject.login(token);       //登录验证
                String userName = (String) SecurityUtils.getSubject().getPrincipal();
                User currentUser = userService.findByUserName(userName);
                if (currentUser.isOff()) {
                    map.put("success", false);
                    map.put("errorInfo", "该用户已封禁，请联系管理员！");
                    subject.logout();
                } else {
                    currentUser.setLatelyLoginTime(new Date());
                    userService.save(currentUser);
                    session.setAttribute(Consts.CURRENT_USER,currentUser);
                    map.put("success", true);
                }
            }catch (Exception e){
                e.printStackTrace();
                map.put("success", false);
                map.put("errorInfo", "用户名或密码错误！");
            }
        }
        return map;
    }


    /**
     * 发送邮件
     */
    @ResponseBody
    @PostMapping("/sendEmail")
    public Map<String,Object> sendEmail(String email, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        if(StringUtil.isEmpty(email)){
            map.put("success",false);
            map.put("errorInfo","邮箱不能为空！");
            return map;
        }
        //验证邮件是否存在
        User u = userService.findByEmail(email);
        if(u==null){
            map.put("success",false);
            map.put("errorInfo","邮箱不存在！");
            return map;
        }
        //验证码
        String mailCode = StringUtil.genSixRandom();
        //发邮件
        SimpleMailMessage message = new SimpleMailMessage();        //消息构造器
        message.setFrom(Consts.V_EMAIL);                            //发件人
        message.setTo(email);                                       //收件人
        message.setSubject("V楼市，舆情监控平台-用户找回密码");         //主题
        message.setText("您本次的验证码是：" +mailCode);            //正文内容
        mailSender.send(message);
        log.info("您本次的验证码是:{}",mailCode);
        //验证码存到session
        session.setAttribute(Consts.MAIL_CODE_NAME,mailCode);
        session.setAttribute(Consts.USER_ID_NAME,u.getUserId());

        map.put("success",true);
        return map;
    }

    /**
     * 邮件验证码判断
     */
    @ResponseBody
    @PostMapping("/checkYzm")
    public Map<String,Object> checkYzm(String yzm, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        if(StringUtil.isEmpty(yzm)){
            map.put("success",false);
            map.put("errorInfo","验证码不能为空！");
            return map;
        }
        String mailCode = (String) session.getAttribute(Consts.MAIL_CODE_NAME);
        Integer userId = (Integer) session.getAttribute(Consts.USER_ID_NAME);

        if(!yzm.equals(mailCode)){
            map.put("success",false);
            map.put("errorInfo","验证码错误！");
            return map;
        }

        //给用户重置密码为123456
        User user = userService.getById(userId);
        user.setPassword((CryptographyUtil.md5(Consts.PASSWORD,CryptographyUtil.SALT)));
        userService.save(user);
        map.put("success",true);
        return map;
    }


    /**
     * 资源管理
     */
    @GetMapping("/articleManage")
    public String articleManage(){
        return "/user/articleManage";
    }

    @ResponseBody
    @RequestMapping("/articleList")
    public Map<String,Object> articleList(MonitorData s_article, @RequestParam(value="page",required = false) Integer page,
                                          @RequestParam(value="limit",required = false) Integer pageSize, HttpSession session){
        Map<String,Object> map = new HashMap<>();
        User currentUser = (User) session.getAttribute(Consts.CURRENT_USER);
        MonitorTarget monitorTarget = new MonitorTarget();
        monitorTarget.setUser(currentUser);
        List<MonitorTarget> list = monitorTargetService.list(monitorTarget, null, page, pageSize, Sort.Direction.DESC, "mtId");
        List<MonitorData> monitorDataList = new LinkedList<>();
        for (MonitorTarget m:list) {
            s_article.setMonitorTarget(m);
            List<MonitorData> listData = monitorDataService.list(s_article,page, pageSize, Sort.Direction.DESC, "eventDate");
            monitorDataList.addAll(listData);
        }
        map.put("data",monitorDataList);
        map.put("count",monitorDataList.size());     //总计录数
        map.put("code",0);
        return map;
    }


    /**
     * 进入资源发布页面
     */
    @GetMapping("toAddArticle")
    public String toAddArticle(){
        return "/user/addArticle";
    }
}
