package com.samsung.members.svc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 서비스 계층 입력용 DTO - 회원 검색 요청
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchInputDTO {
    private String keyword;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}
