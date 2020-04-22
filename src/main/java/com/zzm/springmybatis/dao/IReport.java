package com.zzm.springmybatis.dao;

import com.zzm.springmybatis.entities.Report;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * @author zzm
 * @data 2020/3/10 17:20
 */
@Mapper
public interface IReport {

    @Insert("insert into report (author,datetime,filename) values (#{author},#{datetime},#{filename})")
    public void insert(Report report);

    @Select("SELECT report.* FROM report INNER JOIN  (SELECT author, MAX(`datetime`) AS TIME FROM report GROUP BY author)b " +
            "ON report.author=b.author AND report.`datetime`=b.time")
    public List<Report> findLatestReport();

    @Select("SELECT * FROM report where id=#{id}")
    public Report findById(Integer id);

    @Select("SELECT * FROM report where author=#{id}")
    public List<Report> findByUser(String id);

    @Delete("DELETE FROM report where id=#{id}")
    public void delById(Integer id);
}
