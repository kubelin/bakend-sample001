package com.samsung.members.svc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.samsung.members.biz.MemberBizImpl;
import com.samsung.members.biz.dto.MemberResponseDTO;
import com.samsung.members.dao.MemberDAO;
import com.samsung.members.dao.dto.MemberEntity;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member Service")
public class MemberServiceNestedTest {

	@Mock
	private MemberDAO memberDAO;

	@InjectMocks
	private MemberBizImpl memberBiz;

	private MemberEntity testMemberEntity;
	private MemberResponseDTO testMemberDTO;

	@BeforeEach
	void setUp() {
		// Initialize test data
		testMemberEntity = new MemberEntity();
		testMemberEntity.setMemberId(1L);
		testMemberEntity.setMemberName("Test User");
		testMemberEntity.setEmail("test@example.com");
		testMemberEntity.setPhoneNumber("123-456-7890");
		testMemberEntity.setStatus("ACTIVE");
		testMemberEntity.setCreatedDate(LocalDateTime.now());
		testMemberEntity.setUpdatedDate(LocalDateTime.now());

		testMemberDTO = new MemberResponseDTO(testMemberEntity.getMemberId(), testMemberEntity.getMemberName(),
				testMemberEntity.getEmail(), testMemberEntity.getPhoneNumber(), testMemberEntity.getStatus());
	}

	@Nested
	@DisplayName("Given a member exists")
	
	class GivenMemberExists {
		@BeforeEach
		void setUp() {
		    when(memberDAO.findById(1L)).thenReturn(Optional.of(testMemberEntity));
		}

		@Nested
		@DisplayName("When getting the member by ID")
		class WhenGettingMemberById {

			private Optional<MemberResponseDTO> result;

			@BeforeEach
			void setUp() {
				result = memberBiz.getMemberById(1L);
			}

			@Test
			@DisplayName("Then the member details should be returned")
			void thenMemberDetailsShouldBeReturned() {
				assertTrue(result.isPresent());
				assertEquals(testMemberDTO.getMemberId(), result.get().getMemberId());
				assertEquals(testMemberDTO.getMemberName(), result.get().getMemberName());
				assertEquals(testMemberDTO.getEmail(), result.get().getEmail());
				verify(memberDAO, times(1)).findById(1L);
			}
		}

		@Nested
		@DisplayName("When updating the member")
		class WhenUpdatingMember {
			private MemberResponseDTO updateDTO;
			private MemberEntity updatedEntity;

			@BeforeEach
			void setUp() {
				updateDTO = new MemberResponseDTO(1L, "Updated Name", "updated.email@example.com", "555-555-5555",
						"INACTIVE");

				updatedEntity = new MemberEntity();
				updatedEntity.setMemberId(1L);
				updatedEntity.setMemberName(updateDTO.getMemberName());
				updatedEntity.setEmail(updateDTO.getEmail());
				updatedEntity.setPhoneNumber(updateDTO.getPhoneNumber());
				updatedEntity.setStatus(updateDTO.getStatus());

				when(memberDAO.update(any(MemberEntity.class))).thenReturn(1);
				when(memberDAO.findById(1L)).thenReturn(Optional.of(testMemberEntity), Optional.of(updatedEntity));
			}

			@Test
			@DisplayName("Then the member should be updated successfully")
			void thenMemberShouldBeUpdatedSuccessfully() {
				Optional<MemberResponseDTO> result = memberBiz.updateMember(1L, updateDTO);

				assertTrue(result.isPresent());
				assertEquals(updateDTO.getMemberName(), result.get().getMemberName());
				assertEquals(updateDTO.getEmail(), result.get().getEmail());
				verify(memberDAO, times(2)).findById(1L);
				verify(memberDAO, times(1)).update(any(MemberEntity.class));
			}
		}
	}

	@Nested
	@DisplayName("Given no member exists")
	class GivenNoMemberExists {

		@BeforeEach
		void setUp() {
			when(memberDAO.findById(999L)).thenReturn(null);
		}

		@Nested
		@DisplayName("When getting the member by ID")
		class WhenGettingMemberById {

			private Optional<MemberResponseDTO> result;

			@BeforeEach
			void setUp() {
				result = memberBiz.getMemberById(999L);
			}

			@Test
			@DisplayName("Then empty result should be returned")
			void thenEmptyResultShouldBeReturned() {
				assertFalse(result.isPresent());
				verify(memberDAO, times(1)).findById(999L);
			}
		}

		@Nested
		@DisplayName("When updating the member")
		class WhenUpdatingMember {

			@Test
			@DisplayName("Then the update should fail")
			void thenUpdateShouldFail() {
				MemberResponseDTO updateDTO = new MemberResponseDTO(999L, "Updated Name", "updated.email@example.com",
						"555-555-5555", "INACTIVE");

				Optional<MemberResponseDTO> result = memberBiz.updateMember(999L, updateDTO);

				assertFalse(result.isPresent());
				verify(memberDAO, times(1)).findById(999L);
				verify(memberDAO, never()).update(any(MemberEntity.class));
			}
		}
	}

	@Nested
	@DisplayName("Given multiple members exist")
	class GivenMultipleMembersExist {

		private List<MemberEntity> mockMembers;

		@BeforeEach
		void setUp() {
			MemberEntity member1 = new MemberEntity();
			member1.setMemberId(1L);
			member1.setMemberName("John Doe");
			member1.setEmail("john.doe@example.com");
			member1.setStatus("ACTIVE");

			MemberEntity member2 = new MemberEntity();
			member2.setMemberId(2L);
			member2.setMemberName("John Smith");
			member2.setEmail("john.smith@example.com");
			member2.setStatus("ACTIVE");

			mockMembers = Arrays.asList(member1, member2);

			when(memberDAO.findByNameContaining(eq("John"), anyInt(), anyInt(), any(), any())).thenReturn(mockMembers);
			when(memberDAO.countByNameContaining("John")).thenReturn(2);
		}

		@Nested
		@DisplayName("When searching for members by name")
		class WhenSearchingForMembersByName {

			private List<MemberResponseDTO> results;

			@BeforeEach
			void setUp() {
				results = memberBiz.searchMembersByName("John", 0L, 10L);
			}

			@Test
			@DisplayName("Then matching members should be returned")
			void thenMatchingMembersShouldBeReturned() {
				assertEquals(2, results.size());
				assertEquals("John Doe", results.get(0).getMemberName());
				assertEquals("John Smith", results.get(1).getMemberName());
				verify(memberDAO, times(1)).findByNameContaining(eq("John"), anyInt(), anyInt(), any(), any());
			}

			@Test
			@DisplayName("Then total count should be correct")
			void thenTotalCountShouldBeCorrect() {
				int count = memberBiz.countMembersByName("John");
				assertEquals(2, count);
				verify(memberDAO, times(1)).countByNameContaining("John");
			}
		}
	}
}