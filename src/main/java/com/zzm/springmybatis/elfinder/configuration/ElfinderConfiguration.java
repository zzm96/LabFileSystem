package com.zzm.springmybatis.elfinder.configuration;

import com.zzm.springmybatis.elfinder.param.Node;
import com.zzm.springmybatis.elfinder.param.Thumbnail;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
//@ConfigurationProperties(prefix="filemanager") //接收application.yml中的file-manager下面的属性
public class ElfinderConfiguration {

    private Thumbnail thumbnail =new Thumbnail();

    private String command = "com.zzm.springmybatis.elfinder.command";

    private List<Node> volumes = new ArrayList<>();

    private Long maxUploadSize = -1L;//默认不限制

    {
        volumes.add(new Node());
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<Node> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Node> volumes) {
        this.volumes = volumes;
    }

    public Long getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(Long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }
}
