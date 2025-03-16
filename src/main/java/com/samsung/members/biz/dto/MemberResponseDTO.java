package com.samsung.members.biz.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 응답 DTO (서비스 계층 출력용)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    private Long memberId;
    private String memberName;
    private String email;
    private String phoneNumber;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    // UI 표시용 추가 정보
    public String getStatusDisplayName() {
        if (status == null) return "";
        
        switch (status) {
            case "ACTIVE": return "활성";
            case "INACTIVE": return "비활성";
            case "SUSPENDED": return "정지";
            default: return status;
        }
    }
    
    public boolean getIsActive() {
        return "ACTIVE".equals(status);
    }
}
