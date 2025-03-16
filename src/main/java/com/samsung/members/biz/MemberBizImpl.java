package com.samsung.members.biz;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samsung.members.biz.dto.MemberRequestDTO;
import com.samsung.members.biz.dto.MemberResponseDTO;
import com.samsung.members.biz.dto.MemberSearchRequest;
import com.samsung.members.biz.dto.MemberSearchResult;
import com.samsung.members.biz.dto.MemberUpdateRequestDTO;
import com.samsung.members.dao.MemberDAO;
import com.samsung.members.dao.dto.MemberEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 서비스 구현체
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberBizImpl implements MemberBiz {

	private final MemberDAO memberDAO;

	@Override
	@Transactional(readOnly = true)
	public Optional<MemberResponseDTO> getMemberById(Long memberId) {
		log.info("Getting member with ID: {}", memberId);

		return memberDAO.findById(memberId).map(this::convertToResponseDTO);
	}
 
	@Override
	@Transactional(readOnly = true)
	public MemberSearchResult searchMembers(MemberSearchRequest request) {
		log.info("Searching members with keyword: {}", request.getKeyword());

		// 검색 실행
		List<MemberEntity> entities = memberDAO.findByNameContaining(request.getKeyword(), request.getOffset(),
				request.getSize(), request.getSortBy(), request.getSortDirection());

		// 전체 건수 조회
		int totalCount = memberDAO.countByNameContaining(request.getKeyword());

		// 결과 변환
		return createSearchResult(entities, totalCount, request);
	}

	@Override
	@Transactional(readOnly = true)
	public MemberSearchResult getAllMembers(MemberSearchRequest request) {
		log.info("Getting all members with pagination");

		// 모든 회원 조회
		List<MemberEntity> entities = memberDAO.findAll(request.getOffset(), request.getSize(), request.getSortBy(),
				request.getSortDirection());

		// 전체 건수 조회
		int totalCount = memberDAO.countAll();

		// 결과 변환
		return createSearchResult(entities, totalCount, request);
	}

	@Override
	@Transactional
	public MemberResponseDTO saveMember(MemberRequestDTO requestDTO) {
		log.info("Saving new member: {}", requestDTO.getMemberName());

		// 요청 DTO를 Entity로 변환
		MemberEntity entity = MemberEntity.builder().memberName(requestDTO.getMemberName()).email(requestDTO.getEmail())
				.phoneNumber(requestDTO.getPhoneNumber()).status(requestDTO.getStatus()).build();

		// 데이터 저장
		memberDAO.save(entity);

		// 저장된 Entity를 응답 DTO로 변환하여 반환
		return convertToResponseDTO(entity);
	}

	@Override
	@Transactional
	public MemberResponseDTO updateMember(Long memberId, MemberUpdateRequestDTO requestDTO) {
		log.info("Updating member with ID: {}", memberId);

		// 기존 회원 정보 조회
		Optional<MemberEntity> existingMember = memberDAO.findById(memberId);

		if (!existingMember.isPresent()) {
			log.warn("Member not found with ID: {}", memberId);
			return null;
		}

		// 기존 엔티티에 요청 DTO 값 적용
		MemberEntity entity = existingMember.get();
		entity.setMemberName(requestDTO.getMemberName());
		entity.setEmail(requestDTO.getEmail());
		entity.setPhoneNumber(requestDTO.getPhoneNumber());
		entity.setStatus(requestDTO.getStatus());

		// 데이터 업데이트
		memberDAO.update(entity);

		// 업데이트된 Entity를 응답 DTO로 변환하여 반환
		return convertToResponseDTO(entity);
	}

	@Override
	@Transactional
	public boolean deleteMember(Long memberId) {
		log.info("Deleting member with ID: {}", memberId);

		// 기존 회원 정보 조회
		Optional<MemberEntity> existingMember = memberDAO.findById(memberId);

		if (!existingMember.isPresent()) {
			log.warn("Member not found with ID: {}", memberId);
			return false;
		}

		// 데이터 삭제
		int result = memberDAO.deleteById(memberId);

		return result > 0;
	}

	/**
	 * 검색 결과 DTO 생성
	 */
	private MemberSearchResult createSearchResult(List<MemberEntity> entities, int totalCount,
			MemberSearchRequest request) {
		List<MemberResponseDTO> memberDTOs = entities.stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());

		int totalPages = (int) Math.ceil((double) totalCount / request.getSize());

		return MemberSearchResult.builder().members(memberDTOs).totalCount(totalCount).currentPage(request.getPage())
				.totalPages(totalPages).hasNext(request.getPage() < totalPages).hasPrevious(request.getPage() > 1)
				.build();
	}

	/**
	 * Entity를 응답 DTO로 변환
	 */
	private MemberResponseDTO convertToResponseDTO(MemberEntity entity) {
		return MemberResponseDTO.builder().memberId(entity.getMemberId()).memberName(entity.getMemberName())
				.email(entity.getEmail()).phoneNumber(entity.getPhoneNumber()).status(entity.getStatus())
				.createdDate(entity.getCreatedDate()).updatedDate(entity.getUpdatedDate()).build();
	}
}
