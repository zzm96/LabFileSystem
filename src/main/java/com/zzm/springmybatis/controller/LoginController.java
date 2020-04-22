package com.zzm.springmybatis.controller;

import com.zzm.springmybatis.common.PasswordUtil;
import com.zzm.springmybatis.dao.IMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author zzm
 * @data 2020/3/8 18:06
 */
@Controller
public class LoginController {
    @Autowired
    private IMember iMember;


    @PostMapping("/login")
    public String login(@RequestParam("username") String studentId,
                        @RequestParam("password") String password,
                        Map<String, Object> map, HttpSession session) {
        if (iMember.exist(studentId) && PasswordUtil.validPassword(password,iMember.findById(studentId).getPassword())) {
            //登陆成功，防止表单重复提交，可以重定向到主页
            session.setAttribute("loginUser", iMember.findById(studentId).getName());
            session.setAttribute("loginId", studentId);
            return "redirect:/toIndex";
        } else {
            //登陆失败
            map.put("msg", "用户名密码错误");
            return "login";
        }
    }

    @GetMapping("logout")
    public String logout(HttpSession session,
                         Map<String, Object> map) {

        session.invalidate();
        map.put("msg", "注销登录成功");
        return "login";
    }

}
