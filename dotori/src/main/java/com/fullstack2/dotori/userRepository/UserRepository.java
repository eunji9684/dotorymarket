package com.fullstack2.dotori.userRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.fullstack2.dotori.userEntity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	  // 이름과 이메일을 기반으로 사용자를 검색하는 메서드 정의
    User findByUnameAndUemail(String uname, String uemail);

	//중복체크시 필요
	boolean existsByUid(String uid);
	boolean existsByUemail(String uemail);
	
	Optional<User> findByUid(String uid);
	
	
	@Query("SELECT u FROM User u "
			+ "WHERE u.uid = :uid")
	Object getUserByUId(@Param("uid") String uid);
	
	//Page<User> findBy
	
	
}
