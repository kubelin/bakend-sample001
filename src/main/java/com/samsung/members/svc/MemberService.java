package com.samsung.members.svc;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samsung.common.ApiResponse;
import com.samsung.members.biz.MemberBiz;
import com.samsung.members.biz.dto.MemberRequestDTO;
import com.samsung.members.biz.dto.MemberResponseDTO;
import com.samsung.members.biz.dto.MemberSearchRequest;
import com.samsung.members.biz.dto.MemberSearchResult;
import com.samsung.members.biz.dto.MemberUpdateRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 정보 컨트롤러
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	
	  private final MemberBiz memberBiz;
	    
	    /**
	     * 회원 ID로 회원 정보 조회
	     */
	    @GetMapping("/{memberId}")
	    public ResponseEntity<ApiResponse<MemberResponseDTO>> getMember(@PathVariable("memberId") Long memberId) {
	        log.info("API - Get member with ID: {}", memberId);
	        
	        Optional<MemberResponseDTO> member = memberBiz.getMemberById(memberId);
	        
	        if (member.isPresent()) {
	            return ResponseEntity.ok(ApiResponse.success(member.get()));
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(ApiResponse.error("Member not found with ID: " + memberId));
	        }
	    }
	    
	    /**
	     * 이름 키워드로 회원 검색
	     */
	    @GetMapping("/search")
	    public ResponseEntity<ApiResponse<MemberSearchResult>> searchMembers(
	    	    @RequestParam("keyword") String keyword,
	    	    @RequestParam(name = "page", required = false) Integer page,
	    	    @RequestParam(name = "size", required = false) Integer size,
	    	    @RequestParam(name = "sortBy", required = false) String sortBy,
	    	    @RequestParam(name = "sortDirection", required = false) String sortDirection) {
	        
	        log.info("API - Search members with keyword: {}", keyword);
	        
	        MemberSearchRequest request = MemberSearchRequest.builder()
	                .keyword(keyword)
	                .page(page)
	                .size(size)
	                .sortBy(sortBy)
	                .sortDirection(sortDirection)
	                .build();
	        
	        MemberSearchResult result = memberBiz.searchMembers(request);
	        
	        return ResponseEntity.ok(ApiResponse.success(result));
	    }
	    
	    /**
	     * 전체 회원 목록 조회
	     */
	    @GetMapping("/searchwholeMembers")
	    public ResponseEntity<ApiResponse<MemberSearchResult>> getAllMembers(
	            @RequestParam(required = false) Integer page,
	            @RequestParam(required = false) Integer size,
	            @RequestParam(required = false) String sortBy,
	            @RequestParam(required = false) String sortDirection) {
	        
	        log.info("API - Get all members with pagination");
	        
	        MemberSearchRequest request = MemberSearchRequest.builder()
	                .page(page)
	                .size(size)
	                .sortBy(sortBy)
	                .sortDirection(sortDirection)
	                .build();
	        
	        MemberSearchResult result = memberBiz.getAllMembers(request);
	        
	        return ResponseEntity.ok(ApiResponse.success(result));
	    }
	    
	    /**
	     * 회원 정보 등록
	     */
	    @PostMapping
	    public ResponseEntity<ApiResponse<MemberResponseDTO>> createMember(@RequestBody MemberRequestDTO requestDTO) {
	        log.info("API - Create new member: {}", requestDTO.getMemberName());
	        
	        MemberResponseDTO savedMember = memberBiz.saveMember(requestDTO); 
	        
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body(ApiResponse.success("Member created successfully", savedMember));
	    }
	    
	    /**
	     * 회원 정보 수정
	     */
	    @PutMapping("/{memberId}")
	    public ResponseEntity<ApiResponse<MemberResponseDTO>> updateMember(
	            @PathVariable Long memberId, 
	            @RequestBody MemberUpdateRequestDTO requestDTO) {
	        
	        log.info("API - Update member with ID: {}", memberId);
	        
	        MemberResponseDTO updatedMember = memberBiz.updateMember(memberId, requestDTO);
	        
	        if (updatedMember != null) {
	            return ResponseEntity.ok(ApiResponse.success("Member updated successfully", updatedMember));
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(ApiResponse.error("Member not found with ID: " + memberId));
	        }
	    }
	    
	    /**
	     * 회원 정보 삭제
	     */
	    @DeleteMapping("/{memberId}")
	    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long memberId) {
	        log.info("API - Delete member with ID: {}", memberId);
	        
	        boolean result = memberBiz.deleteMember(memberId);
	        
	        if (result) {
	            return ResponseEntity.ok(ApiResponse.success("Member deleted successfully", null));
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(ApiResponse.error("Member not found with ID: " + memberId));
	        }
	    }
}
