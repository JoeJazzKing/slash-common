package com.heycine.slash.common.service.demo.mysql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MysqlDemo {

	@Autowired(required = false)
	UserMapper userMapper;

	@GetMapping("mysql/send")
	public String send() {

		UserEntity userEntity = new UserEntity();
		userEntity.setId("110");
		userEntity.setName("大老公");
		userEntity.setPassword("123456");
		userEntity.setEmail("123@qq.com");
		userEntity.setType(1);
		userEntity.setPhone(18990985032L);
		userEntity.setState("1");
		userEntity.setAvatar("http://test.alezc.com/avatar/5e3c9770c39711ecbfed3fc4cd791916.jpg");
		userMapper.insert(userEntity);
		return "SUCESS";
	}

	@GetMapping("mysql/find")
	public UserEntity find() {
		UserEntity userEntity = userMapper.selectById("110");
		return userEntity;
	}

}
