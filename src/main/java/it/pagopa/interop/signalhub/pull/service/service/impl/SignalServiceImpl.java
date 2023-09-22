package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PnGenericException;
import it.pagopa.interop.signalhub.pull.service.repository.EServiceRepository;
import it.pagopa.interop.signalhub.pull.service.repository.SignalRepository;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.SignalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@AllArgsConstructor
public class SignalServiceImpl implements SignalService {

    @Autowired
    private EServiceRepository eServiceRepository;

    @Autowired
    private SignalRepository signalRepository;



    @Override
    public Mono<Flux<Signal>> pullSignal(String organizationId, String eServiceId, Long indexSignal) {
        return eServiceRepository.findByOrganizationIdAndEServiceId(organizationId, eServiceId)
                .switchIfEmpty(Mono.error(new PnGenericException(ExceptionTypeEnum.CORRESPONDENCE_NOT_FOUND, ExceptionTypeEnum.CORRESPONDENCE_NOT_FOUND.getMessage().concat(eServiceId), HttpStatus.FORBIDDEN)))
                .flatMap(eservice -> signalRepository.findSignal(eServiceId, indexSignal))
                .switchIfEmpty(Mono.error(new PnGenericException(ExceptionTypeEnum.SIGNALID_ALREADY_EXISTS, ExceptionTypeEnum.SIGNALID_ALREADY_EXISTS.getMessage(), HttpStatus.BAD_REQUEST)));
    }
}
