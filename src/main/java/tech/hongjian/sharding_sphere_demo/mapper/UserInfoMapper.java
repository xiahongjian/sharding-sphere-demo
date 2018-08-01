package tech.hongjian.sharding_sphere_demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import tech.hongjian.sharding_sphere_demo.entity.UserInfo;

/**
 * @author xiahongjian 
 * @time   2018-07-30 15:09:18
 *
 */
@Mapper
public interface UserInfoMapper {
	
	@Insert("insert into user_info (user_id, user_name, account, password) values (#{userId}, #{userName}, #{account}, #{password})")
	int insert(UserInfo userInfo);
	
	@Select("select * from user_info where user_id=#{userId}")
	UserInfo selectById(Long userId);
	
	@Select("select * from user_info where user_id between #{from} and #{to}")
	List<UserInfo> selectByRange(@Param("from") Long from, @Param("to") Long to);
	
	@Select("select * from user_info where user_id >= #{id}")
	List<UserInfo> selectBigerThan(Long id);
	
	@Select("select * from user_info where user_name = #{username}")
	List<UserInfo> selectByUsername(String username);

}
