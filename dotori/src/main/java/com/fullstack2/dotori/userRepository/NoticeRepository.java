package com.fullstack2.dotori.userRepository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack2.dotori.userEntity.UserRole;
import com.fullstack2.dotori.userEntity.notice.NoticeEntity;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
	 Page<NoticeEntity> findAll(Pageable pageable); 
	   Page<NoticeEntity> findByTitleContaining(String searchKeyword, Pageable pageable);
	   Page<NoticeEntity> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
	   List<NoticeEntity> findByTitle(String title);
}
