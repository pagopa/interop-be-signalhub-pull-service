package it.pagopa.interop.signalhub.pull.service.exception;

import lombok.Getter;

@Getter
public enum ExceptionTypeEnum {
    JWT_NOT_VALID("JWT_NOT_VALID", "Il vaucher passato non è valido"),
    JWT_TYPE_INCORRECT("JWT_TYPE_INCORRECT", "Il vaucher passato non è del tipo atteso"),
    JWT_EMPTY("JWT_EMPTY", "Il vaucher passato è vuoto"),
    JWT_NOT_PRESENT("JWT_NOT_PRESENT", "Il vaucher non è stato passato"),
    NO_AUTH_FOUNDED("NO_AUTH_FOUNDED", "Errore interno. Consumatore non trovato"),
    CORRESPONDENCE_NOT_FOUND("CORRESPONDENCE_NOT_FOUND", "Non risulta corrispondenza tra l'erogatore e l'id del servizio: "),
    ESERVICE_STATUS_NOT_VALID("ESERVICE_STATUS_NOT_VALID", "Lo stato del servizio è diverso da archiviato. EServiceId: "),
    SIGNALID_ALREADY_EXISTS("SIGNALID_NOT_EXISTS", "non esiste nessun segnale per il signalId selezionato");

    private final String title;
    private final String message;


    ExceptionTypeEnum(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
