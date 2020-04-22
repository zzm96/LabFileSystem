package com.zzm.springmybatis.controller;

import com.zzm.springmybatis.common.FileUtils;
import com.zzm.springmybatis.dao.IResearch;
import com.zzm.springmybatis.entities.Research;
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
 * @data 2020/3/10 22:07
 */
@Controller
public class ResearchController {

    @Autowired
    private IResearch iResearch;

    private String basePath = "D:/cloudFile/research";


    @GetMapping("/research")
    public String showResearch(Model model) {
        List<Research> patent = iResearch.findByClass("专利");
        List<Research> papper = iResearch.findByClass("论文");
        List<Research> others = iResearch.findOtherr();
        model.addAttribute("patent", patent);
        model.addAttribute("papper", papper);
        model.addAttribute("others", others);
        return "research/resrerch";
    }

    @GetMapping("/researchdownload/{id}")
    public void downLoadFile(@PathVariable("id") Integer id, HttpServletResponse response) throws UnsupportedEncodingException {
        Research research = iResearch.findFileById(id);
        String filePath = basePath + "/" + new SimpleDateFormat("yyyy-MM").format(research.getDatetime()).toString().substring(0, 7)
                + "/" + research.getFilename();
        FileUtils.downloadFile(response, filePath, research.getFilename());
    }

    @DeleteMapping("/delresearch/{id}")
    public String delCode(@PathVariable("id") Integer id) {
        Research research = iResearch.findFileById(id);
        String filePath = basePath + "/" + new SimpleDateFormat("yyyy-MM").format(research.getDatetime()).toString().substring(0, 7)
                + "/" + research.getFilename();
        iResearch.delById(id);

        File file = new File(filePath);
        if(file.exists()) file.delete();

        return "redirect:/research";
    }

    //到添加页面
    @GetMapping("/addresearch")
    public String addCode() {
        return "/research/addresearch";
    }


    //添加操作
    @PostMapping("/uploadresearch")
    public String uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("category") String category,
                              @RequestParam("description") String description, HttpSession session) {
        if (StringUtils.isNotBlank(file.getOriginalFilename())) {
            try {
                Research research = new Research();
                research.setAuthor((String) session.getAttribute("loginId"));
                String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                String filename = uuid.substring(0, 10) + file.getOriginalFilename();
                research.setFilename(filename);
                research.setDatetime(new Date());
                research.setCategory(category);
                research.setDescription(description);
                iResearch.insertResearch(research);

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
        return "redirect:/research";
    }
}
