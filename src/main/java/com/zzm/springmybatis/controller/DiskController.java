package com.zzm.springmybatis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zzm
 * @data 2020/4/5 20:58
 */
@Controller
public class DiskController {

    @GetMapping("fileListPage")
    public String fileListPage(){
        return "disk/fileList";
    }
    @GetMapping("fileList")
    public String fileList(){
        return "disk/disk";
    }

    @GetMapping("sharedFileListPage")
    public String sharedFileListPage(){
        return "disk/sharedFileList";
    }
    @GetMapping("sharedFileList")
    public String sharedFileList(){
        return "disk/shareddisk";
    }
}
