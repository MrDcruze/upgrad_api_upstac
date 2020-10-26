package org.upgrad.upstac.testrequests.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;
import sun.misc.Request;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class LabResultService {

    @Autowired
    private LabResultRepository labResultRepository;


    private static Logger logger = LoggerFactory.getLogger(LabResultService.class);



    private LabResult createLabResult(User tester, TestRequest testRequest) {
        //Implement this method to create the lab result module service
        // create object of LabResult class and use the setter methods to set tester and testRequest details
        // make use of saveLabResult() method to return the LabResult object
        LabResult labResult = new LabResult();
        labResult.setRequest(testRequest);
        labResult.setTester(tester);
        return saveLabResult(labResult); // replace this line with your code
    }

    @Transactional
    LabResult saveLabResult(@Valid LabResult labResult) {
        return labResultRepository.save(labResult);
    }



    public LabResult assignForLabTest(TestRequest testRequest, User tester) {

        return createLabResult(tester, testRequest);


    }


    public LabResult updateLabTest(TestRequest testRequest, CreateLabResult createLabResult) {

        //Implement this method to update the lab test
        // create an object of LabResult and make use of setters to set Blood Pressure, Comments,
        // HeartBeat, OxygenLevel, Temperature, Result and UpdatedOn values
        // make use of the saveLabResult() method to return the object of LabResult

        validLabResultInputForRequiredDetails(createLabResult);
        LabResult updateLabResult = labResultRepository.findByRequest(testRequest).orElseThrow(()-> new AppException("Invalid ID or State"));
        updateLabResult.setRequest(testRequest);
        updateLabResult.setBloodPressure(createLabResult.getBloodPressure());
        updateLabResult.setHeartBeat(createLabResult.getHeartBeat());
        updateLabResult.setComments(createLabResult.getComments());
        updateLabResult.setOxygenLevel(createLabResult.getOxygenLevel());
        updateLabResult.setTemperature(createLabResult.getTemperature());
        updateLabResult.setResult(createLabResult.getResult());
        return saveLabResult(updateLabResult); // replace this line with your code
    }
    public void validLabResultInputForRequiredDetails (CreateLabResult createLabResult) {
        if (createLabResult.getBloodPressure().trim().isEmpty() ||
        createLabResult.getHeartBeat().trim().isEmpty() ||
        createLabResult.getOxygenLevel().trim().isEmpty() ||
        createLabResult.getTemperature().trim().isEmpty()) {
            throw new AppException("Please fill the required fields");
        }
    }
}
