package com.samsung.members.biz.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 검색 결과 DTO (서비스 계층 출력용)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchResult {
    private List<MemberResponseDTO> members;
    private long totalCount;
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}