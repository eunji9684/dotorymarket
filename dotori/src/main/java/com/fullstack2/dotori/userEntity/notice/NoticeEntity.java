package com.fullstack2.dotori.userEntity.notice;

import java.time.LocalDateTime;
import java.util.List;

import com.fullstack2.dotori.userEntity.BaseEntity;
import com.fullstack2.dotori.userEntity.User;
import com.fullstack2.dotori.userEntity.UserRole;
import com.fullstack2.dotori.userEntity.Product.ReplyEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "notice")
public class NoticeEntity extends BaseEntity {

    public void incrementLikeCount() {
        if (this.likeCount == null) {
            this.likeCount = 1L;
        } else {
            this.likeCount++;
        }
    }
    public void incrementViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 1L;
        } else {
            this.viewCount++;
        }
    }
    public void decrementLikeCount() {
        if (this.likeCount != null && this.likeCount > 0) {
            this.likeCount--;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "author") // 새로운 author 열 추가
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "notice_id")
    private List<ReplyEntity> replies;
  
    
    @Builder
    public NoticeEntity(Long id, String title, String content, Long views, Long likes, User user, String author, List<ReplyEntity> replies) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = views;
        this.likeCount = likes;
        this.user = user;
        this.replies = replies;
        this.author = author;
    }


    
    // 다른 메서드 등을 추가할 수 있습니다.
}

