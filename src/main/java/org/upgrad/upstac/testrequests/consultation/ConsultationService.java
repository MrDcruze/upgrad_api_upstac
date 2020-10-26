package org.upgrad.upstac.testrequests.consultation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.users.User;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@Validated
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    private static Logger logger = LoggerFactory.getLogger(ConsultationService.class);


    @Transactional
    public Consultation assignForConsultation( TestRequest testRequest, User doctor) {
        //Implement this method to assign the consultation service
        // create object of Consultation class and use the setter methods to set doctor and testRequest details
        // make use of save() method of consultationRepository to return the Consultation object
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setRequest(testRequest);
        return saveConsultations(consultation);
    }

    @Transactional
    Consultation saveConsultations(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    public Consultation updateConsultation(TestRequest testRequest , CreateConsultationRequest createConsultationRequest) {
        //Implement this method to update the consultation
        // create an object of Consultation and make use of setters to set Suggestion, Comments, and UpdatedOn values
        // make use of save() method of consultationRepository to return the Consultation object
        Consultation updateConsultation = consultationRepository.findByRequest(testRequest).orElseThrow(()-> new AppException("Invalid ID or State"));;
        updateConsultation.setRequest(testRequest);
        updateConsultation.setComments(createConsultationRequest.getComments());
        updateConsultation.setSuggestion(createConsultationRequest.getSuggestion());
        updateConsultation.setUpdatedOn(LocalDate.now());
        return saveConsultations(updateConsultation); // replace this line with your code


    }


}
