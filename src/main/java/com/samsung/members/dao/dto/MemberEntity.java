package com.samsung.members.dao.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DAO 계층 엔티티 - 회원 테이블 매핑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
    private Long memberId;
    private String memberName;
    private String email;
    private String phoneNumber;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
