package it.pagopa.interop.signalhub.pull.service.exception;

import lombok.Getter;

@Getter
public enum ExceptionTypeEnum {
    JWT_NOT_VALID("JWT_NOT_VALID", "Il vaucher passato non è valido"),
    JWT_TYPE_INCORRECT("JWT_TYPE_INCORRECT", "Il vaucher passato non è del tipo atteso"),
    JWT_EMPTY("JWT_EMPTY", "Il vaucher passato è vuoto"),
    JWT_NOT_PRESENT("JWT_NOT_PRESENT", "Il vaucher non è stato passato"),
    NO_AUTH_FOUNDED("NO_AUTH_FOUNDED", "Errore interno. Consumatore non trovato"),
    GENERIC_ERROR("GENERIC_ERROR", "Si è verificato un errore interno"),
    DETAIL_AGREEMENT_ERROR("DETAIL_AGREEMENT_ERROR", "Si è verificato un errore durante il recupero del dettaglio del purpose"),
    AGREEMENT_NOT_VALID("AGREEMENT_NOT_VALID", "Non si dispone delle autorizzazioni necessarie per utilizzare il sistema"),

    CORRESPONDENCE_NOT_FOUND("CORRESPONDENCE_NOT_FOUND", "Non risulta corrispondenza tra l'erogatore e l'id del servizio: "),
    ESERVICE_STATUS_IS_NOT_ACTIVE("ESERVICE_STATUS_IS_NOT_ACTIVE", "Lo stato del servizio è diverso da ACTIVE. EServiceId: "),
    ESERVICE_NOT_FOUND("ESERVICE_NOT_FOUND", "L'eservice non è presente a sistema - eserviceId: "),
    SIGNALID_ALREADY_EXISTS("SIGNALID_NOT_EXISTS", "non esiste nessun segnale per il signalId selezionato");

    private final String title;
    private final String message;


    ExceptionTypeEnum(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
