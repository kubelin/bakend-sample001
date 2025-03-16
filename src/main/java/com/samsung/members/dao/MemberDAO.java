package com.samsung.members.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.samsung.members.dao.dto.MemberEntity;

/**
 * 회원 정보 DAO 인터페이스
 */
@Mapper
public interface MemberDAO {
    
    /**
     * 회원 ID로 회원 정보 조회
     */
    Optional<MemberEntity> findById(@Param("memberId") Long memberId);
    
    /**
     * 이름 키워드로 회원 검색 (페이징 포함)
     */
    List<MemberEntity> findByNameContaining(
            @Param("keyword") String keyword,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit,
            @Param("sortBy") String sortBy,
            @Param("sortDirection") String sortDirection);
    
    /**
     * 이름 키워드로 검색된 회원 수 조회
     */
    int countByNameContaining(@Param("keyword") String keyword);
    
    /**
     * 전체 회원 목록 조회 (페이징 포함)
     */
    List<MemberEntity> findAll(
            @Param("offset") Integer offset,
            @Param("limit") Integer limit,
            @Param("sortBy") String sortBy,
            @Param("sortDirection") String sortDirection);
    
    /**
     * 전체 회원 수 조회
     */
    int countAll();
    
    /**
     * 회원 정보 저장
     */
    int save(MemberEntity entity);
    
    /**
     * 회원 정보 수정
     */
    int update(MemberEntity entity);
    
    /**
     * 회원 정보 삭제
     */
    int deleteById(@Param("memberId") Long memberId);
}