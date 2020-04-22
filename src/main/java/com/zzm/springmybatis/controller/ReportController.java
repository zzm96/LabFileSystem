package com.zzm.springmybatis.controller;

import com.zzm.springmybatis.common.FileUtils;
import com.zzm.springmybatis.dao.IMember;
import com.zzm.springmybatis.dao.IReport;
import com.zzm.springmybatis.entities.Member;
import com.zzm.springmybatis.entities.Report;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zzm
 * @data 2020/3/10 16:46
 */
@Controller
public class ReportController {
    @Autowired
    private IReport iReport;

    @Autowired
    IMember iMember;

    private String basePath = "D:/cloudFile/report";

    //查询所有周报返回列表页面
    @GetMapping("/reports")
    public String list(Model model) {
        List<Report> latestReport = iReport.findLatestReport();
        List<Member> member = iMember.findAll();

        model.addAttribute("latestReport", latestReport);
        model.addAttribute("members", member);
        return "rep/list";
    }

    @PostMapping("/uploadreport")
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpSession session) {
        if(StringUtils.isNotBlank(file.getOriginalFilename())){
            try {
                Report report = new Report();
                report.setAuthor((String) session.getAttribute("loginId"));
                String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                String filename = uuid.substring(0,10) + file.getOriginalFilename();
                report.setFilename(filename);
                report.setDatetime(new Date());
                iReport.insert(report);


                String datePath = new SimpleDateFormat("yyyy-MM").format(new Date());

                File newFile = new File(basePath + "/" + datePath);
                if (!newFile.exists()) {
                    newFile.mkdirs();
                }
                file.transferTo(new File(newFile, filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return "redirect:/reports";
    }


    @GetMapping("/reportdownload/{id}")
    public void downLoadFile(@PathVariable("id") Integer id, HttpServletResponse response) throws UnsupportedEncodingException {
        Report report = iReport.findById(id);
        String filePath = basePath + "/" + new SimpleDateFormat("yyyy-MM").format(report.getDatetime()).toString().substring(0, 7)
                + "/" + report.getFilename();
        FileUtils.downloadFile(response, filePath, report.getFilename());
    }

    @DeleteMapping("/delreport/{id}")
    public String delCode(@PathVariable("id") Integer id) {
        Report report = iReport.findById(id);
        String filePath = basePath + "/" + new SimpleDateFormat("yyyy-MM").format(report.getDatetime()).toString().substring(0, 7)
                + "/" + report.getFilename();
        iReport.delById(id);

        File file = new File(filePath);
        if(file.exists()) file.delete();

        return "redirect:/research";
    }
}
