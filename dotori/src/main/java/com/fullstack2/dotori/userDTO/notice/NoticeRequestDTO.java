package com.fullstack2.dotori.userDTO.notice;

import com.fullstack2.dotori.userEntity.User;

import com.fullstack2.dotori.userEntity.notice.NoticeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor // 생성자 추가

public class NoticeRequestDTO {

    private Long id; // id 속성 추가
    private String author;
    private String title;
    private String content;
    private User user;

    // 필요에 따라 다른 속성을 추가할 수 있습니다.

    public NoticeRequestDTO(String author, String title, String content, User user) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public NoticeEntity toEntity() {
        return NoticeEntity.builder()
                .id(id) // id 속성 설정
                .author(author)
                .title(title)
                .content(content)
                .user(user)
                .build();
    }

}
