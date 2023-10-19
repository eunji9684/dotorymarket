// NoticeService.java
package com.fullstack2.dotori.userService.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fullstack2.dotori.userDTO.notice.NoticeDTO;
import com.fullstack2.dotori.userDTO.notice.NoticeRequestDTO;
import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userEntity.Product.ProductEntity;
import com.fullstack2.dotori.userEntity.notice.NoticeEntity;

public interface NoticeService {
	
	NoticeEntity getNoticeById(Long id);
    void updateNotice(NoticeEntity noticeEntity, NoticeRequestDTO noticeRequestDTO); // 추가

    void deleteNotice(NoticeEntity noticeEntity); // 추가
    
    default NoticeDTO entityToDTO(NoticeEntity entity) {
        if (entity == null) {
            return null;
        }
        return NoticeDTO.builder()
                .id(entity.getId())
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .content(entity.getContent())
                .views(entity.getViewCount())
                .likes(entity.getLikeCount())
                .createDate(entity.getCreatedAt())
                .build();
    }

    default NoticeEntity dtoToEntity(NoticeDTO dto) {
        if (dto == null) {
            return null;
        }
        return NoticeEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(dto.getAuthor())
                .build();
    }
    
    
Page<NoticeEntity> noticeSearchList(String searchKeyword, Pageable pageable);	
	
	Page<LikeEntity> noticeLikeList(Long userId, Pageable pageable);
	
	 Page<NoticeDTO> getNotices(Pageable pageable);
	 
	
}
