package it.pagopa.interop.signalhub.pull.service.exception;

import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Problem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.UUID;


public class ProblemBuilder {
    private final Problem problem;
    private ExceptionTypeEnum typeEnum;
    private HttpStatus status;
    private String customMessage;

    private ProblemBuilder(){
        this.problem = new Problem();
    }


    public ProblemBuilder addExceptionType(ExceptionTypeEnum typeEnum){
        this.typeEnum = typeEnum;
        return this;
    }

    public ProblemBuilder addStatusCode(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ProblemBuilder setMessage(String message){
        this.customMessage = message;
        return this;
    }

    public Problem build(){
        if (typeEnum == null || status == null) {
            throw new IllegalArgumentException("Required arguments not founded");
        }
        this.problem.setStatus(this.status.value());
        this.problem.setTitle(this.typeEnum.getTitle());
        this.problem.setDetail(this.typeEnum.getMessage());
        if (StringUtils.isNotBlank(this.customMessage)) {
            this.problem.setDetail(this.customMessage);
        }
        this.problem.setCorrelationId(UUID.randomUUID().toString());
        return problem;
    }

    public static ProblemBuilder builder(){
        return new ProblemBuilder();
    }


}
