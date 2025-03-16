package com.samsung.stepdefs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.samsung.common.ApiResponse;
import com.samsung.members.biz.MemberBiz;
import com.samsung.members.biz.dto.MemberResponseDTO;
import com.samsung.members.biz.dto.MemberSearchResult;
import com.samsung.members.svc.MemberService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MemberServiceStepDefs {

    @Mock
    private MemberBiz memberBiz;

    @InjectMocks
    private MemberService memberController;

    private ResponseEntity<ApiResponse<MemberResponseDTO>> memberResponse;
    private ResponseEntity<ApiResponse<MemberSearchResult>> searchResponse;
    private List<MemberResponseDTO> testMembers = new ArrayList<>();
    private MemberResponseDTO testMember;
    private Long testMemberId;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("a member exists with ID {long}")
    public void aMemberExistsWithID(Long memberId) {
        testMemberId = memberId;
        testMember = new MemberResponseDTO(
            memberId,
            "Test User " + memberId,
            "test" + memberId + "@example.com",
            "123-456-7890",
            "ACTIVE"
        );
        
        when(memberBiz.getMemberById(memberId)).thenReturn(Optional.of(testMember));
    }

    @Given("no member exists with ID {long}")
    public void noMemberExistsWithID(Long memberId) {
        when(memberBiz.getMemberById(memberId)).thenReturn(Optional.empty());
    }

    @Given("the following members exist in the system:")
    public void theFollowingMembersExistInTheSystem(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        
        testMembers = rows.stream()
            .map(row -> new MemberResponseDTO(
                Long.parseLong(row.get("memberId")),
                row.get("memberName"),
                row.get("email"),
                row.get("phoneNumber"),
                row.get("status")
            ))
            .collect(Collectors.toList());
        
        
        // Mock the search functionality
//        when(memberBiz.searchMembersByName(anyString(), anyLong(), anyLong()))
//            .thenAnswer(invocation -> {
//                String keyword = invocation.getArgument(0);
//                return testMembers.stream()
//                    .filter(member -> member.getMemberName().contains(keyword))
//                    .collect(Collectors.toList());
//            });
//        
//        when(memberBiz.countMembersByName(anyString()))
//            .thenAnswer(invocation -> {
//                String keyword = invocation.getArgument(0);
//                return (int) testMembers.stream()
//                    .filter(member -> member.getMemberName().contains(keyword))
//                    .count();
//            });
    }

    @Given("I have valid member information")
    public void iHaveValidMemberInformation() {
        testMember = new MemberResponseDTO(
            null, // ID will be assigned by the system
            "New Test User",
            "newuser@example.com",
            "987-654-3210",
            "ACTIVE"
        );
        
        // Mock the create functionality
//        when(memberBiz.createMember(testMember))
//            .thenReturn(new MemberResponseDTO(
//                10L, // Assign a new ID
//                testMember.getMemberName(),
//                testMember.getEmail(),
//                testMember.getPhoneNumber(),
//                testMember.getStatus()
//            ));
    }

    @When("I request member information with ID {long}")
    public void iRequestMemberInformationWithID(Long memberId) {
        memberResponse = memberController.getMember(memberId);
    }

    @When("I search for members with name {string}")
    public void iSearchForMembersWithName(String keyword) {
        searchResponse = memberController.searchMembers(keyword, 0, 10, null, null);
    }

    @When("I submit a request to create a new member")
    public void iSubmitARequestToCreateANewMember() {
        // This would typically involve calling the controller's create method
        // We'll mock this for now
    }

    @When("I update the member's email to {string}")
    public void iUpdateTheMembersEmailTo(String newEmail) {
        // Update the test member with new email
        testMember.setEmail(newEmail);
        
        // Mock the update functionality
//        when(memberBiz.updateMember(testMember.getMemberId(), testMember))
//            .thenReturn(Optional.of(testMember));
        
        // Call the controller's update method (would need to be implemented)
    }

    @Then("the member details should be returned")
    public void theMemberDetailsShouldBeReturned() {
        assertNotNull(memberResponse.getBody());
        assertTrue(memberResponse.getBody().isSuccess());
        assertNotNull(memberResponse.getBody().getData());
        
        MemberResponseDTO returnedMember = memberResponse.getBody().getData();
        assertEquals(testMemberId, returnedMember.getMemberId());
    }

    @Then("the member should not be found")
    public void theMemberShouldNotBeFound() {
        assertNotNull(memberResponse.getBody());
        assertFalse(memberResponse.getBody().isSuccess());
        assertNull(memberResponse.getBody().getData());
//        assertTrue(memberResponse.getBody().getError().contains("not found"));
    }

    @Then("the response status should be {word}")
    public void theResponseStatusShouldBe(String status) {
        HttpStatus expectedStatus = HttpStatus.valueOf(status);
        assertEquals(expectedStatus, memberResponse.getStatusCode());
    }

    @Then("the search result should contain {int} members")
    public void theSearchResultShouldContainMembers(Integer count) {
        assertNotNull(searchResponse.getBody());
        assertTrue(searchResponse.getBody().isSuccess());
        assertNotNull(searchResponse.getBody().getData());
        
        MemberSearchResult result = searchResponse.getBody().getData();
//        assertEquals(count, result.getTotalCount());
        assertEquals(count, result.getMembers().size());
    }

    @Then("the search result should include members with IDs {int} and {int}")
    public void theSearchResultShouldIncludeMembersWithIDsAnd(Integer id1, Integer id2) {
        MemberSearchResult result = searchResponse.getBody().getData();
        List<Long> memberIds = result.getMembers().stream()
            .map(MemberResponseDTO::getMemberId)
            .collect(Collectors.toList());
        
        assertTrue(memberIds.contains(id1.longValue()));
        assertTrue(memberIds.contains(id2.longValue()));
    }

    @Then("a new member should be created successfully")
    public void aNewMemberShouldBeCreatedSuccessfully() {
        // Implement assertion for successful member creation
    }

    @Then("the response should include the new member ID")
    public void theResponseShouldIncludeTheNewMemberID() {
        // Implement assertion for checking new member ID
    }

    @Then("the member's email should be updated successfully")
    public void theMembersEmailShouldBeUpdatedSuccessfully() {
        // Implement assertion for successful email update
    }
}