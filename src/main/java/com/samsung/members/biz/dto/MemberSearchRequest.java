package com.samsung.members.biz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 검색 요청 DTO (서비스 계층 입력용)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchRequest {
    private String keyword;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
    
    // 기본값 설정
    public Integer getPage() {
        return page != null ? page : 1;
    }
    
    public Integer getSize() {
        return size != null ? size : 10;
    }
    
    public String getSortBy() {
        return sortBy != null ? sortBy : "memberId";
    }
    
    public String getSortDirection() {
        return "DESC".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";
    }
    
    // 검색 시작 인덱스 계산
    public int getOffset() {
        return (getPage() - 1) * getSize();
    }
}