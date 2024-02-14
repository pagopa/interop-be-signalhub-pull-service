package it.pagopa.interop.signalhub.pull.service.exception;


import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Problem;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.ProblemError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ProblemBuilder {
    private final Problem problem;
    private HttpStatus status;
    private List<ProblemError> errors;
    private String customMessage;

    private ProblemBuilder(){
        this.problem = new Problem();
    }


    public ProblemBuilder addStatusCode(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ProblemBuilder setMessage(String message){
        this.customMessage = message;
        return this;
    }

    public ProblemBuilder addProblemError(String code, String detail){
        if(errors==null) errors= new ArrayList<>();
        ProblemError problemError= new ProblemError(code,detail);
        errors.add(problemError);
        return this;
    }

    public Problem build(){
        if (status == null) {
            throw new IllegalArgumentException("Required arguments not founded");
        }
        this.problem.setStatus(this.status.value());
        this.problem.setTitle(this.status.name());
        this.problem.setDetail(this.status.getReasonPhrase());
        if(errors!=null) this.problem.setErrors(errors);
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
