package com.zzm.springmybatis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zzm
 * @data 2020/3/9 16:51
 */
public class Report implements Serializable{
    private Integer id;
    private String author;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date datetime;
    private String filename;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }


    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", author=" + author +
                ", datetime=" + datetime +
                '}';
    }
}
