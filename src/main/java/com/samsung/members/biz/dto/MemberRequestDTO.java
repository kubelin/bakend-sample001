package com.samsung.members.biz.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 생성 요청 DTO (서비스 계층 입력용)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {
    private String memberName;
    private String email;
    private String phoneNumber;
    private String status;
}