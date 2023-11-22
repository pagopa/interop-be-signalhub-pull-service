package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.SignalMapper;
import it.pagopa.interop.signalhub.pull.service.repository.SignalRepository;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.ConsumerService;
import it.pagopa.interop.signalhub.pull.service.service.OrganizationService;
import it.pagopa.interop.signalhub.pull.service.service.SignalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@AllArgsConstructor
public class SignalServiceImpl implements SignalService {
    private ConsumerService consumerService;
    private SignalRepository signalRepository;
    private SignalMapper signalMapper;
    private OrganizationService organizationService;


    @Override
    public Flux<Signal> pullSignal(String consumerId, String eServiceId, Long signalId, Long size) {
        long finalSignalId = signalId;
        long start = finalSignalId+1;
        long end = finalSignalId+size;
        return consumerService.getConsumerEservice(eServiceId, consumerId)
                .switchIfEmpty(Mono.error(new PDNDGenericException(ExceptionTypeEnum.CORRESPONDENCE_NOT_FOUND, ExceptionTypeEnum.CORRESPONDENCE_NOT_FOUND.getMessage().concat(eServiceId), HttpStatus.FORBIDDEN)))
                .flatMap(consumerEService -> organizationService.getEService(eServiceId, consumerEService.getDescriptorId()))
                .doOnNext(eService -> log.info("Searching signals from {} to {}", start, end))
                .flatMapMany(eservice -> signalRepository.findSignal(eServiceId, start, end))
                .map(signalMapper::toDto);
    }


}
