package com.fullstack2.dotori.userService.notice;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fullstack2.dotori.userDTO.notice.NoticeDTO;
import com.fullstack2.dotori.userDTO.notice.NoticeRequestDTO;
import com.fullstack2.dotori.userEntity.Product.LikeEntity;
import com.fullstack2.dotori.userEntity.notice.NoticeEntity;
import com.fullstack2.dotori.userRepository.NoticeRepository;

@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {

	private final NoticeRepository noticeRepository;

	
    
    @Override
    public NoticeEntity getNoticeById(Long id) {
        Optional<NoticeEntity> noticeOptional = noticeRepository.findById(id);
        return noticeOptional.orElse(null);
    }


    @Autowired
    public NoticeServiceImpl(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }
    


    @Override
    public Page<NoticeEntity> noticeSearchList(String searchKeyword, Pageable pageable) {
        // 제목 또는 내용에 검색 키워드가 포함된 공지사항을 가져옵니다.
        return noticeRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword, pageable);
    }


    @Override
    public Page<LikeEntity> noticeLikeList(Long userId, Pageable pageable) {
        // 여기에 사용자의 좋아요 목록을 가져오는 로직을 추가하십시오
        // 예: likeRepository.findByUserId(userId, pageable);
        return null;
    }

    @Override
    public Page<NoticeDTO> getNotices(Pageable pageable) {
        // 페이지네이션된 NoticeEntity를 가져와서 NoticeDTO로 변환하여 반환합니다.
        Page<NoticeEntity> noticeEntities = noticeRepository.findAll(pageable);
        return noticeEntities.map(this::entityToDTO);
    }
    
    
    
    @Transactional
    public Long createNotice(NoticeEntity noticeEntity) {
        try {
            // 공지사항을 저장
            noticeRepository.save(noticeEntity);
            return noticeEntity.getId();
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace(); // 또는 로그에 기록
            return null; // 또는 예외 처리에 따른 반환 값 설정
        }
    }
    
    @Transactional
    @Override
    public void updateNotice(NoticeEntity noticeEntity, NoticeRequestDTO noticeRequestDTO) {
        // 공지사항을 업데이트하는 로직을 구현하세요.
        noticeEntity.setTitle(noticeRequestDTO.getTitle());
        noticeEntity.setContent(noticeRequestDTO.getContent());
        // 필요한 다른 업데이트 로직을 추가하세요.
        //noticeRepository.save(noticeEntity);
        
    }

    @Transactional
    @Override
    public void deleteNotice(NoticeEntity noticeEntity) {
        // 공지사항을 삭제하는 로직을 구현하세요.
        noticeRepository.delete(noticeEntity);
    }


    // NoticeEntity를 NoticeDTO로 변환하는 메서드v
    public NoticeDTO entityToDTO(NoticeEntity entity) {
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

    // DTO를 Entity로 변환하는 메서드
    public NoticeEntity dtoToEntity(NoticeRequestDTO noticeRequestDTO) {
        if (noticeRequestDTO == null) {
            return null;
        }
        return NoticeEntity.builder()
                .title(noticeRequestDTO.getTitle())
                .content(noticeRequestDTO.getContent())
                .author(noticeRequestDTO.getAuthor())
                .build();
    }
}
