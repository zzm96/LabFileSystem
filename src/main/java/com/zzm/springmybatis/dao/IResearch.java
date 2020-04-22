package com.zzm.springmybatis.dao;

import com.zzm.springmybatis.entities.Research;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author zzm
 * @data 2020/3/10 21:26
 */
@Mapper
public interface IResearch {

    @Select("select * from research where id=#{id}")
    public Research findFileById(Integer id);

    @Select("SELECT * FROM research where category=#{category}")
    public List<Research> findByClass(String category);

    @Select("SELECT * FROM research where author=#{stuId}")
    public List<Research> findByAuthor(String stuId);

    @Select("SELECT * FROM research where category!='论文' AND category!='专利'")
    public List<Research> findOtherr();

    @Delete("delete from research where id=#{id}")
    public void delById(Integer id);

    @Insert("INSERT INTO research (author,datetime,filename,description,category) values (#{author},#{datetime},#{filename},#{description},#{category})")
    public void insertResearch(Research research);
}
