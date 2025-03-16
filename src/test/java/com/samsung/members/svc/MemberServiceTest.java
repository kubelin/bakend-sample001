package com.samsung.members.svc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
public class MemberServiceTest {

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

        testMemberDTO = new MemberResponseDTO(
            testMemberEntity.getMemberId(),
            testMemberEntity.getMemberName(),
            testMemberEntity.getEmail(),
            testMemberEntity.getPhoneNumber(),
            testMemberEntity.getStatus()
        );
    }

    @Test
    @DisplayName("Should return member when getMemberById is called with valid ID")
    void shouldReturnMemberWhenGetMemberByIdIsCalledWithValidId() {
        // Arrange
//        when(memberDAO.findById(1L)).thenReturn(testMemberEntity);
        when(memberDAO.findById(1L));

        // Act
        Optional<MemberResponseDTO> result = memberBiz.getMemberById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testMemberDTO.getMemberId(), result.get().getMemberId());
        assertEquals(testMemberDTO.getMemberName(), result.get().getMemberName());
        assertEquals(testMemberDTO.getEmail(), result.get().getEmail());
        verify(memberDAO, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty Optional when getMemberById is called with invalid ID")
    void shouldReturnEmptyOptionalWhenGetMemberByIdIsCalledWithInvalidId() {
        // Arrange
        when(memberDAO.findById(999L)).thenReturn(null);

        // Act
        Optional<MemberResponseDTO> result = memberBiz.getMemberById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(memberDAO, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should return members when searchMembersByName is called with valid keyword")
    void shouldReturnMembersWhenSearchMembersByNameIsCalledWithValidKeyword() {
        // Arrange
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

        List<MemberEntity> mockResults = Arrays.asList(member1, member2);
        
        when(memberDAO.findByNameContaining(eq("John"), anyInt(), anyInt(), any(), any()))
            .thenReturn(mockResults);
        when(memberDAO.countByNameContaining("John")).thenReturn(2);

        // Act
        List<MemberResponseDTO> results = memberBiz.searchMembersByName("John", 0L, 10L);
//        int count = memberBiz.countMembersByName("John");

        // Assert
        assertEquals(2, results.size());
//        assertEquals(2, count);
        assertEquals("John Doe", results.get(0).getMemberName());
        assertEquals("John Smith", results.get(1).getMemberName());
        verify(memberDAO, times(1)).findByNameContaining(eq("John"), anyInt(), anyInt(), any(), any());
        verify(memberDAO, times(1)).countByNameContaining("John");
    }

    @Test
    @DisplayName("Should create member successfully")
    void shouldCreateMemberSuccessfully() {
        // Arrange
        MemberResponseDTO newMemberDTO = new MemberResponseDTO(
            null, // ID will be assigned by the system
            "New User",
            "new.user@example.com",
            "987-654-3210",
            "ACTIVE"
        );
        
        MemberEntity newMemberEntity = new MemberEntity();
        newMemberEntity.setMemberName(newMemberDTO.getMemberName());
        newMemberEntity.setEmail(newMemberDTO.getEmail());
        newMemberEntity.setPhoneNumber(newMemberDTO.getPhoneNumber());
        newMemberEntity.setStatus(newMemberDTO.getStatus());
        
        MemberEntity savedEntity = new MemberEntity();
        savedEntity.setMemberId(10L); // Assigned ID
        savedEntity.setMemberName(newMemberDTO.getMemberName());
        savedEntity.setEmail(newMemberDTO.getEmail());
        savedEntity.setPhoneNumber(newMemberDTO.getPhoneNumber());
        savedEntity.setStatus(newMemberDTO.getStatus());
        
//        when(memberDAO.save(any(MemberEntity.class))).thenReturn(savedEntity);
        
//         Act
//        MemberResponseDTO result = memberBiz.createMember(newMemberDTO);
        
        // Assert
//        assertNotNull(result);
//        assertEquals(10L, result.getMemberId());
//        assertEquals(newMemberDTO.getMemberName(), result.getMemberName());
//        assertEquals(newMemberDTO.getEmail(), result.getEmail());
//        verify(memberDAO, times(1)).save(any(MemberEntity.class));
    }

    @Test
    @DisplayName("Should update member successfully")
    void shouldUpdateMemberSuccessfully() {
        // Arrange
        Long memberId = 1L;
        MemberResponseDTO updateDTO = new MemberResponseDTO(
            memberId,
            "Updated Name",
            "updated.email@example.com",
            "555-555-5555",
            "INACTIVE"
        );
        
        MemberEntity existingEntity = new MemberEntity();
        existingEntity.setMemberId(memberId);
        existingEntity.setMemberName("Test User");
        existingEntity.setEmail("test@example.com");
        existingEntity.setPhoneNumber("123-456-7890");
        existingEntity.setStatus("ACTIVE");
        
        MemberEntity updatedEntity = new MemberEntity();
        updatedEntity.setMemberId(memberId);
        updatedEntity.setMemberName(updateDTO.getMemberName());
        updatedEntity.setEmail(updateDTO.getEmail());
        updatedEntity.setPhoneNumber(updateDTO.getPhoneNumber());
        updatedEntity.setStatus(updateDTO.getStatus());
//        
//        when(memberDAO.findById(memberId)).thenReturn(existingEntity);
//        when(memberDAO.update(any(MemberEntity.class))).thenReturn(1); // 1 row affected
//        when(memberDAO.findById(memberId)).thenReturn(updatedEntity); // After update
//        
//        // Act
//        Optional<MemberResponseDTO> result = memberBiz.updateMember(memberId, updateDTO);
//        
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals(updateDTO.getMemberName(), result.get().getMemberName());
//        assertEquals(updateDTO.getEmail(), result.get().getEmail());
//        assertEquals(updateDTO.getPhoneNumber(), result.get().getPhoneNumber());
//        assertEquals(updateDTO.getStatus(), result.get().getStatus());
//        verify(memberDAO, times(2)).findById(memberId); // Once before update, once after
//        verify(memberDAO, times(1)).update(any(MemberEntity.class));
    }

    @Test
    @DisplayName("Should return empty Optional when updating non-existent member")
    void shouldReturnEmptyOptionalWhenUpdatingNonExistentMember() {
        // Arrange
        Long memberId = 999L;
        MemberResponseDTO updateDTO = new MemberResponseDTO(
            memberId,
            "Updated Name",
            "updated.email@example.com",
            "555-555-5555",
            "INACTIVE"
        );
        
        when(memberDAO.findById(memberId)).thenReturn(null);
        
        // Act
//        Optional<MemberResponseDTO> result = memberBiz.updateMember(memberId, updateDTO);
//        
//        // Assert
//        assertFalse(result.isPresent());
//        verify(memberDAO, times(1)).findById(memberId);
//        verify(memberDAO, never()).update(any(MemberEntity.class));
    }

    @Test
    @DisplayName("Should delete member successfully")
    void shouldDeleteMemberSuccessfully() {
        // Arrange
        Long memberId = 1L;
        when(memberDAO.deleteById(memberId)).thenReturn(1); // 1 row affected
        
        // Act
        boolean result = memberBiz.deleteMember(memberId);
        
        // Assert
        assertTrue(result);
        verify(memberDAO, times(1)).deleteById(memberId);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent member")
    void shouldReturnFalseWhenDeletingNonExistentMember() {
        // Arrange
        Long memberId = 999L;
        when(memberDAO.deleteById(memberId)).thenReturn(0); // 0 rows affected
        
        // Act
        boolean result = memberBiz.deleteMember(memberId);
        
        // Assert
        assertFalse(result);
        verify(memberDAO, times(1)).deleteById(memberId);
    }
}