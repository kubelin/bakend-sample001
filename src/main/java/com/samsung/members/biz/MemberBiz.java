package com.samsung.members.biz;

import java.util.Optional;

import com.samsung.members.biz.dto.MemberRequestDTO;
import com.samsung.members.biz.dto.MemberResponseDTO;
import com.samsung.members.biz.dto.MemberSearchRequest;
import com.samsung.members.biz.dto.MemberSearchResult;
import com.samsung.members.biz.dto.MemberUpdateRequestDTO;

/**
 * 회원 서비스 인터페이스
 */
public interface MemberBiz {
    
    /**
     * 회원 ID로 회원 정보 조회
     */
    Optional<MemberResponseDTO> getMemberById(Long memberId);
    
    /**
     * 이름 키워드로 회원 검색
     */
    MemberSearchResult searchMembers(MemberSearchRequest request);
    
    /**
     * 전체 회원 목록 조회
     */
    MemberSearchResult getAllMembers(MemberSearchRequest request);
    
    /**
     * 회원 정보 저장
     */
    MemberResponseDTO saveMember(MemberRequestDTO requestDTO);
    
    /**
     * 회원 정보 수정
     */
    MemberResponseDTO updateMember(Long memberId, MemberUpdateRequestDTO requestDTO);
    
    /**
     * 회원 정보 삭제
     */
    boolean deleteMember(Long memberId);
}
