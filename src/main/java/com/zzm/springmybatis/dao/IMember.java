package com.zzm.springmybatis.dao;

import com.zzm.springmybatis.entities.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author zzm
 * @data 2020/3/9 17:20
 */
@Mapper
public interface IMember {


    @Select("select * from member where studentid=#{stuId}")
    @Results(id="memberMap",value = {
            @Result(id=true,column = "id",property = "id"),
            @Result(column = "studentid",property = "studentId")
    })
    public Member findById(String stuId);


    @Select("select * from member where id=#{id}")
    @ResultMap("memberMap")
    public Member find(Integer id);

    @Select("select count(1)=1 from member where studentid=#{stuId}")
    public boolean exist(String stuId);

    @Update("update member set studentid=#{studentId},name=#{name}, gender=#{gender}, age=#{age},grade=#{grade},password=#{password}," +
            "email=#{email},sign=#{sign},img=#{img} where id=#{id}")
    public void updateMember(Member member);

    @Select("select * from member")
    public List<Member> findAll();

    @Delete("delete from member where studentid=#{id}")
    public void deleteById(String id);

    @Insert("insert into member (studentid, name,gender,grade,password) values (#{studentId},#{name},#{gender},#{grade},#{password})")
    public void insertMember(Member member);
}
