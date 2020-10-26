package org.upgrad.upstac.testrequests;

import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.consultation.*;
import org.upgrad.upstac.testrequests.lab.LabRequestController;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.testrequests.lab.TestStatus;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import javax.print.Doc;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class ConsultationControllerTest {


    @InjectMocks
    ConsultationController consultationController;


    @Mock
    UserLoggedInService userLoggedInService;

    @Mock
    TestRequestUpdateService testRequestUpdateService;

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_assignForConsultation_with_valid_test_request_id_should_update_the_request_status(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_COMPLETED);
        testRequest.setStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);
        //Implement this method
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForConsultation(testRequest.getRequestId(), user)).thenReturn(testRequest);

        //Create another object of the TestRequest method and explicitly assign this object for Consultation using assignForConsultation() method
        // from consultationController class. Pass the request id of testRequest object.
        TestRequest testRequestUpdate = consultationController.assignForConsultation(testRequest.getRequestId());


        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'DIAGNOSIS_IN_PROCESS'
        // make use of assertNotNull() method to make sure that the consultation value of second object is not null
        // use getConsultation() method to get the lab result
        assertThat(testRequest.getRequestId(), equalTo(testRequestUpdate.getRequestId()));
        assertThat(RequestStatus.DIAGNOSIS_IN_PROCESS, equalTo(testRequestUpdate.getStatus()));
        assertNotNull(testRequestUpdate.getConsultation());


    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        CreateTestRequest createTestRequest = createTestRequest();
        TestRequest mockedTestRequest = getMockedResponseFrom(createTestRequest, status);
        return mockedTestRequest;
    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_assignForConsultation_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForConsultation(InvalidRequestId, user)).thenThrow(new AppException("Invalid ID"));

        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForConsultation() method
        // of consultationController with InvalidRequestId as Id
        ResponseStatusException responseStatusException =
            assertThrows(ResponseStatusException.class, () -> {
                consultationController.assignForConsultation(InvalidRequestId);
            });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(responseStatusException.getMessage(), containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_valid_test_request_id_should_update_the_request_status_and_update_consultation_details(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);
        testRequest.setStatus(RequestStatus.COMPLETED);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        CreateConsultationRequest createConsultationRequest = getCreateConsultationRequest(testRequest);
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateConsultation(testRequest.getRequestId(), createConsultationRequest, user)).thenReturn(testRequest);

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'COMPLETED'. Make use of updateConsultation() method from labRequestController class (Pass the previously created two objects as parameters)
        TestRequest updatedTestRequest = consultationController.updateConsultation(testRequest.getRequestId(), createConsultationRequest);

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'COMPLETED'
        // 3. the suggestion of both the objects created should be same. Make use of getSuggestion() method to get the results.
        assertThat(testRequest.getRequestId(), equalTo(updatedTestRequest.getRequestId()));
        assertThat(RequestStatus.COMPLETED, equalTo(updatedTestRequest.getStatus()));
        assertNotNull(updatedTestRequest.getConsultation());


    }


    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_test_request_id_should_throw_exception(){
        Long InvalidRequestId = -2L;
        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object. Pass the above created object as the parameter
        CreateConsultationRequest createConsultationRequest = getCreateConsultationRequest(testRequest);
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateConsultation(InvalidRequestId, createConsultationRequest, user)).thenThrow(new AppException("ConstraintViolationException"));

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        ResponseStatusException responseStatusException =
            assertThrows(ResponseStatusException.class, () -> {
                consultationController.updateConsultation(InvalidRequestId, createConsultationRequest);
            });


        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(responseStatusException.getMessage(), containsString("ConstraintViolationException"));
    }

    @Test
    @WithUserDetails(value = "doctor")
    public void calling_updateConsultation_with_invalid_empty_status_should_throw_exception(){
        Long InvalidRequestId = -2L;
        TestRequest testRequest = getTestRequestByStatus(RequestStatus.DIAGNOSIS_IN_PROCESS);

        //Implement this method

        //Create an object of CreateConsultationRequest and call getCreateConsultationRequest() to create the object.
        // Pass the above created object as the parameter
        // Set the suggestion of the above created object to null.
        CreateConsultationRequest createConsultationRequest = getCreateConsultationRequest(testRequest);
        createConsultationRequest.setSuggestion(null);
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateConsultation(InvalidRequestId, createConsultationRequest, user)).thenThrow(new AppException("Invalid Suggestion"));

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateConsultation() method
        // of consultationController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        ResponseStatusException responseStatusException =
                assertThrows(ResponseStatusException.class, () -> {
                    consultationController.updateConsultation(InvalidRequestId, createConsultationRequest);
                });

        assertThat(responseStatusException.getMessage(), containsString("Invalid Suggestion"));
    }

    public CreateConsultationRequest getCreateConsultationRequest(TestRequest testRequest) {

        //Create an object of CreateLabResult and set all the values
        // if the lab result test status is Positive, set the doctor suggestion as "HOME_QUARANTINE" and comments accordingly
        // else if the lab result status is Negative, set the doctor suggestion as "NO_ISSUES" and comments as "Ok"
        // Return the object
        CreateConsultationRequest createConsultationRequest = new CreateConsultationRequest();
          createConsultationRequest.setComments("Test Comments");
        createConsultationRequest.setSuggestion(DoctorSuggestion.NO_ISSUES);

        return createConsultationRequest; // Replace this line with your code

    }

    public CreateTestRequest createTestRequest() {
        CreateTestRequest createTestRequest = new CreateTestRequest();
        createTestRequest.setAddress("some Addres");
        createTestRequest.setAge(98);
        createTestRequest.setEmail("someone" + "123456789" + "@somedomain.com");
        createTestRequest.setGender(Gender.MALE);
        createTestRequest.setName("someuser");
        createTestRequest.setPhoneNumber("123456789");
        createTestRequest.setPinCode(716768);
        return createTestRequest;
    }
    public TestRequest getMockedResponseFrom(CreateTestRequest createTestRequest) {
        TestRequest testRequest = new TestRequest();
        testRequest.setName(createTestRequest.getName());
        testRequest.setCreated(LocalDate.now());
        testRequest.setRequestId(1L);
        testRequest.setStatus(RequestStatus.INITIATED);
        testRequest.setAge(createTestRequest.getAge());
        testRequest.setEmail(createTestRequest.getEmail());
        testRequest.setPhoneNumber(createTestRequest.getPhoneNumber());
        testRequest.setPinCode(createTestRequest.getPinCode());
        testRequest.setAddress(createTestRequest.getAddress());
        testRequest.setGender(createTestRequest.getGender());
        testRequest.setCreatedBy(createUser());

        return testRequest;
    }

    public TestRequest getMockedResponseFrom(CreateTestRequest createTestRequest, RequestStatus requestStatus) {
        TestRequest testRequest = getMockedResponseFrom(createTestRequest);
        testRequest.setStatus(requestStatus);
        LabResult labResult = new LabResult();
        Consultation drConsultation = new Consultation();
        drConsultation.setComments("Test comments");
        drConsultation.setRequest(testRequest);
        drConsultation.setSuggestion(DoctorSuggestion.NO_ISSUES);
        testRequest.setConsultation(drConsultation);
        testRequest.setLabResult(labResult);
        return testRequest;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("someuser");
        return user;
    }

}