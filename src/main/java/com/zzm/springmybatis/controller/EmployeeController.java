package com.zzm.springmybatis.controller;

import com.zzm.springmybatis.common.PasswordUtil;
import com.zzm.springmybatis.dao.IMember;
import com.zzm.springmybatis.entities.Member;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {
    @Autowired
    IMember iMember;


    //查询所有员工返回列表页面
    @GetMapping("/emps")
    public String list(Model model, HttpSession session,Map<String, Object> map) {
        List<Member> employees = iMember.findAll();
        String loginId = (String) session.getAttribute("loginId");
        //放在请求域中
        model.addAttribute("emps", employees);
        model.addAttribute("nowuser",loginId);
        // thymeleaf默认就会拼串
        return "emp/list";
    }

    //
    //来到成员添加页面
    @GetMapping("/emp")
    public String toAddPage(Map<String, Object> map, HttpSession session) {
        return "emp/add";
    }

    //
    //员工添加
    //SpringMVC自动将请求参数和入参对象的属性进行一一绑定；要求请求参数的名字和javaBean入参的对象里面的属性名是一样的
    @PostMapping("/emp")
    public String addEmp(Model model,Member member,Map<String, Object> map, HttpSession session) {
        //添加成员
        if(StringUtils.isNotBlank(member.getStudentId()) && !iMember.exist(member.getStudentId())){
            member.setPassword(PasswordUtil.getEncryptedPwd("123456"));
            iMember.insertMember(member);
            // redirect: 表示重定向到一个地址  /代表当前项目路径
            // forward: 表示转发到一个地址
            return "redirect:/emps";
        }else{
            map.put("msg","添加失败，成员已存在或学号为空！");
            return list(model,session,map);

        }
    }

    //
    //来到修改页面，查出当前员工，在页面回显
    @GetMapping("/emp/{id}")
    public String toEditPage(@PathVariable("id") String id, Model model,Map<String, Object> map, HttpSession session) {
        Object loginId = session.getAttribute("loginId");
        if ("0000".equals((String) loginId)){
            Member member = iMember.findById(id);
            model.addAttribute("emp", member);
            //回到修改页面(add是一个修改添加二合一的页面);
            return "emp/add";
        }
        map.put("msg","非管理员无法修改");
        return "emp/list";
    }

    //
    //成员修改；需要提交id；
    @PutMapping("/emp")
    public String updateEmployee(Member member) {
        Member member1 = iMember.find(member.getId());
        member1.setName(member.getName());
        member1.setStudentId(member.getStudentId());
        member1.setGender(member.getGender());
        member1.setGrade(member.getGrade());
        iMember.updateMember(member1);
        return "redirect:/emps";
    }

    //成员删除
    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") String id, Map<String, Object> map, HttpSession session) {
        Object loginId = session.getAttribute("loginId");
        if ("0000".equals(id))
            map.put("msg", "管理员无法被删除权限");
        else if ("0000".equals((String) loginId)) {
            iMember.deleteById(id);
            return "redirect:/emps";
        }
        map.put("msg", "非管理员没有删除权限");
        return "emp/list";
    }
}
