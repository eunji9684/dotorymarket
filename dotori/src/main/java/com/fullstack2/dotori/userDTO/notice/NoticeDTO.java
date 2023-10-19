package com.fullstack2.dotori.userDTO.notice;

import java.time.LocalDateTime;

import com.fullstack2.dotori.userEntity.notice.NoticeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    private Long id;
    private String author;
    private String title;
    private String content;
    private Long views;
    private Long likes;
    private LocalDateTime createDate;
    private Long userId;
    
    

}
