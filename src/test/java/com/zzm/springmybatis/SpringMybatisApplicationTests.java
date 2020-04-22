package com.zzm.springmybatis;

import com.zzm.springmybatis.common.PasswordUtil;
import com.zzm.springmybatis.dao.IMember;
import com.zzm.springmybatis.entities.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringMybatisApplicationTests {
	@Autowired
	private IMember member;

	@Test
	public void test(){
		Member t = member.findById("0000");
        System.out.println(t);
		String id = t.getPassword();
		System.out.println(PasswordUtil.validPassword("admin", member.findById(t.getStudentId()).getPassword()));
	}

	@Test
	void contextLoads() {
	}

}
