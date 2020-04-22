package com.zzm.springmybatis.controller;

import com.zzm.springmybatis.common.PasswordUtil;
import com.zzm.springmybatis.dao.IMember;
import com.zzm.springmybatis.dao.IReport;
import com.zzm.springmybatis.dao.IResearch;
import com.zzm.springmybatis.entities.Member;
import com.zzm.springmybatis.entities.Report;
import com.zzm.springmybatis.entities.Research;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author zzm
 * @data 2020/3/12 22:45
 */
@Controller
public class IndexController {

    @Autowired
    private IMember iMember;

    @Autowired
    private IResearch iResearch;

    @Autowired
    private IReport iReport;

    private String reportBasePath = "D:/cloudFile/report";
    private String researchBasePath = "D:/cloudFile/research";

    @GetMapping("/toIndex")
    public String ind(HttpSession session, Model model) {
        String studentId = (String) session.getAttribute("loginId");
        Member i = iMember.findById(studentId);
        List<Report> report = iReport.findByUser(studentId);
        List<Research> research = iResearch.findByAuthor(studentId);
        model.addAttribute("user", i);
        model.addAttribute("report", report);
        model.addAttribute("research", research);
        return "dashboard";
    }

    //来到个人资料修改页面
    @GetMapping("/toUpdatemyself")
    public String updateEmployee(HttpSession session, Model model) {
        String stuId = (String) session.getAttribute("loginId");
        Member me = iMember.findById(stuId);
        model.addAttribute("me", me);
        return "ind/updateInfo";
    }

    //个人资料修改
    @PostMapping("/update")
    public String addEmp(Member member, @RequestParam("file") MultipartFile file, @Param("password2") String pass2) {
        if (StringUtils.isEmpty(member.getPassword()) || member.getPassword().equals(pass2))
            member.setPassword(iMember.find(member.getId()).getPassword());
        else
            member.setPassword(PasswordUtil.getEncryptedPwd(member.getPassword()));
        try {
            byte[] fileBytes = file.getBytes();
            if(null==fileBytes || fileBytes.length==0) member.setImg(iMember.find(member.getId()).getImg());
            else member.setImg(fileBytes);
            iMember.updateMember(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/toIndex";
    }

    @DeleteMapping("/indexDelReport/{id}")
    public String indexDelReport(@PathVariable("id") Integer id) {
        Report report = iReport.findById(id);
        String filePath = reportBasePath + "/" + new SimpleDateFormat("yyyy-MM").format(report.getDatetime()).toString().substring(0, 7)
                + "/" + report.getFilename();
        iReport.delById(id);

        File file = new File(filePath);
        if (file.exists()) file.delete();
        return "redirect:/toIndex";
    }

    @DeleteMapping("/indexDelResearch/{id}")
    public String indexDelResearch(@PathVariable("id") Integer id) {
        Research research = iResearch.findFileById(id);
        String filePath = researchBasePath + "/" + new SimpleDateFormat("yyyy-MM").format(research.getDatetime()).toString().substring(0, 7)
                + "/" + research.getFilename();
        iResearch.delById(id);

        File file = new File(filePath);
        if (file.exists()) file.delete();
        return "redirect:/toIndex";
    }

    @GetMapping("getImg")
    public void getImg(HttpSession session, HttpServletResponse response) {
        String stuId = (String) session.getAttribute("loginId");
        byte[] img = iMember.findById(stuId).getImg();
        if (null == img || img.length == 0) img = iMember.findById("0000").getImg();

        try {
            response.setContentType("image/jpeg");
            response.setCharacterEncoding("UTF-8");
            OutputStream outputSream = response.getOutputStream();
            outputSream.write(img);
            outputSream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
