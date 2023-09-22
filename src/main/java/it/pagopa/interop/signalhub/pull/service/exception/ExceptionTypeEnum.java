package it.pagopa.interop.signalhub.pull.service.exception;

import lombok.Getter;

@Getter
public enum ExceptionTypeEnum {

    CORRESPONDENCE_NOT_FOUND("CORRESPONDENCE_NOT_FOUND", "Non risulta corrispondenza tra l'erogatore e l'id del servizio: "),
    SIGNALID_ALREADY_EXISTS("SIGNALID_ALREADY_EXISTS", "Esiste già un signalId per il determinato servizio");

    private final String title;
    private final String message;


    ExceptionTypeEnum(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
