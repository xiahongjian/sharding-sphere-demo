package tech.hongjian.sharding_sphere_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tech.hongjian.sharding_sphere_demo.entity.UserInfo;
import tech.hongjian.sharding_sphere_demo.mapper.UserInfoMapper;


/**
 * @author xiahongjian 
 * @time   2018-07-30 14:29:54
 *
 */
@RestController
public class IndexController {

	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@GetMapping("/")
	public int index() {
		return 0;
	}
	
	@PostMapping("/insert")
	public String insert(@RequestBody UserInfo userInfo) {
		if (userInfo != null) {
			userInfoMapper.insert(userInfo);
			return "done";
		}
		return "fail";
	}
	
	@GetMapping("/user/{id}")
	public UserInfo findById(@PathVariable Long id) {
		return userInfoMapper.selectById(id);
	}
	
	@GetMapping("/user/range")
	public List<UserInfo> findRange(Long from, Long to) {
		if (from != null && to != null) {
			return userInfoMapper.selectByRange(from, to);
		}
		return null;
	}
	
	@GetMapping("/user/bigger")
	public List<UserInfo> findBiggerThan(Long id) {
		if (id == null)
			return null;
		return userInfoMapper.selectBigerThan(id);
	}
	
	@GetMapping("/user/username")
	public List<UserInfo> findByUsername(String username) {
		if (username == null)
			return null;
		return userInfoMapper.selectByUsername(username);
	}
}
