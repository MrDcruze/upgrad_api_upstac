package org.upgrad.upstac.testrequests.consultation;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateConsultationRequest {

    public DoctorSuggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(DoctorSuggestion suggestion) {
        this.suggestion = suggestion;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @NotNull
    private DoctorSuggestion suggestion;

    private String comments;
}
