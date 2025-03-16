package com.samsung.members.svc.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 서비스 계층 출력용 DTO - 회원 정보 응답
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchOutputDTO {
	  private Long memberId;
	    private String memberName;
	    private String email;
	    private String phoneNumber;
	    private String status;
	    private LocalDateTime createdDate;
	    private LocalDateTime updatedDate;
	    
	    // 추가 정보 (UI 표시용)
	    private String statusDisplayName;
	    private boolean isActive;
	    
	    // 상태 표시명 계산 메소드
	    public String getStatusDisplayName() {
	        if (status == null) return "";
	        
	        switch (status) {
	            case "ACTIVE": return "활성";
	            case "INACTIVE": return "비활성";
	            case "SUSPENDED": return "정지";
	            default: return status;
	        }
	    }
	    
	    // 활성 상태 여부 계산 메소드
	    public boolean getIsActive() {
	        return "ACTIVE".equals(status);
	    }
	
}
