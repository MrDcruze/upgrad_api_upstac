package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.lab.*;
import org.upgrad.upstac.users.User;
import org.upgrad.upstac.users.models.Gender;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class LabRequestControllerTest {


    @InjectMocks
    LabRequestController labRequestController;


    @Mock
    UserLoggedInService userLoggedInService;

    @Mock
    TestRequestQueryService testRequestQueryService;

    @Mock
    TestRequestUpdateService testRequestUpdateService;

    @Mock
    LabResultService labResultService;


    @Test
    @WithUserDetails(value = "testerv")
    public void calling_assignForLabTest_with_valid_test_request_id_should_update_the_request_status(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.INITIATED);
        //Implement this method
        Long validId = 1L;
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForLabTest(validId, user)).thenReturn(testRequest);


        //Create another object of the TestRequest method and explicitly assign this object for Lab Test using assignForLabTest() method
        // from labRequestController class. Pass the request id of testRequest object.
        TestRequest testRequest2 = labRequestController.assignForLabTest(validId);

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'INITIATED'
        // make use of assertNotNull() method to make sure that the lab result of second object is not null
        // use getLabResult() method to get the lab result
        assertThat(testRequest.getRequestId(), equalTo(testRequest2.getRequestId()));
        assertThat(RequestStatus.INITIATED, equalTo(testRequest2.getStatus()));
        assertNotNull(testRequest2.getLabResult());


    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        CreateTestRequest createTestRequest = createTestRequest();
        TestRequest mockedTestRequest = getMockedResponseFrom(createTestRequest, status);
        return mockedTestRequest;
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_assignForLabTest_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -32L;
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.assignForLabTest(InvalidRequestId, user)).thenThrow(new AppException("Invalid ID"));
        //Implement this method

        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForLabTest() method
        // of labRequestController with InvalidRequestId as Id
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> {
            labRequestController.assignForLabTest(InvalidRequestId);
        });;
        assertNotNull(responseStatusException);
        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(responseStatusException.getMessage(), containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "testerv")
    public void calling_updateLabTest_with_valid_test_request_id_should_update_the_request_status_and_update_test_request_details(){
        Long id = 1L;
        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        CreateLabResult createLabResult = getCreateLabResult();
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateLabTest(id, createLabResult, user)).thenReturn(testRequest);


        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)
        TestRequest updateTestRequest = labRequestController.updateLabTest(1L, createLabResult);

        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.
        assertThat(testRequest.getRequestId(), equalTo(updateTestRequest.getRequestId()));
        assertThat(RequestStatus.LAB_TEST_IN_PROGRESS, equalTo(updateTestRequest.getStatus()));
        assertNotNull(updateTestRequest.getLabResult());


    }


    @Test
    @WithUserDetails(value = "testerv")
    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
        Long InvalidRequestId= -3L;

        //Implement this method


        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        CreateLabResult createLabResult = getCreateLabResult();
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateLabTest(InvalidRequestId, createLabResult, user)).thenThrow(new AppException("Invalid ID"));
        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()-> {
            labRequestController.updateLabTest(InvalidRequestId, createLabResult);
        });
        assertNotNull(responseStatusException);

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"
        assertThat(responseStatusException.getMessage(), containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_empty_status_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        // Set the result of the above created object to null.
        CreateLabResult createLabResult = getCreateLabResult();
        User user = createUser();
        Mockito.when(userLoggedInService.getLoggedInUser()).thenReturn(user);
        Mockito.when(testRequestUpdateService.updateLabTest(testRequest.getRequestId(), createLabResult, user)).thenThrow(new AppException("ConstraintViolationException"));

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method
        ResponseStatusException result = assertThrows(ResponseStatusException.class, ()-> {
            labRequestController.updateLabTest(testRequest.getRequestId(), createLabResult);
        });
        assertNotNull(result);

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "ConstraintViolationException"
        assertThat(result.getMessage(), containsString("ConstraintViolationException"));
    }

    public CreateLabResult getCreateLabResult() {

        //Create an object of CreateLabResult and set all the values
        // Return the object
        CreateLabResult createLabResult = new CreateLabResult();
        createLabResult.setBloodPressure("102");
        createLabResult.setHeartBeat("88");
        createLabResult.setOxygenLevel("98");
        createLabResult.setTemperature("99");
        createLabResult.setResult(TestStatus.NEGATIVE);

        return createLabResult; // Replace this line with your code
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