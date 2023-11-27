package it.pagopa.interop.signalhub.pull.service.exception;

import lombok.Getter;

@Getter
public enum ExceptionTypeEnum {
    JWT_NOT_VALID("JWT_NOT_VALID", "Il voucher passato non è valido"),
    JWT_TYPE_INCORRECT("JWT_TYPE_INCORRECT", "Il voucher passato non è del tipo atteso"),
    JWT_EMPTY("JWT_EMPTY", "Il voucher passato è vuoto"),
    JWT_NOT_PRESENT("JWT_NOT_PRESENT", "Il voucher non è stato passato"),
    NO_AUTH_FOUNDED("NO_AUTH_FOUNDED", "Errore interno. Consumatore non trovato"),
    GENERIC_ERROR("GENERIC_ERROR", "Si è verificato un errore interno"),
    DETAIL_AGREEMENT_ERROR("DETAIL_AGREEMENT_ERROR", "Si è verificato un errore durante il recupero del dettaglio del purpose"),
    AGREEMENT_NOT_VALID("AGREEMENT_NOT_VALID", "Non si dispone delle autorizzazioni necessarie per utilizzare il sistema"),
    JWT_PARSER_ERROR("JWT_PARSER_ERROR", "Non è stato possibile decodificare il voucher."),
    UNAUTHORIZED("NOT_AUTHORIZED", "User not authorized"),
    SIZE_NOT_VALID("SIZE_NOT_VALID", "size non può essere maggiore di 100"),
    SIGNALID_ALREADY_EXISTS("SIGNALID_NOT_EXISTS", "non esiste nessun segnale per il signalId selezionato");

    private final String title;
    private final String message;


    ExceptionTypeEnum(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
