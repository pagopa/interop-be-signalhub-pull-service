package it.pagopa.interop.signalhub.pull.service.auth;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class PrincipalAgreement {
    private String principalId;
    private String purposeId;
    private String eServiceId;
    private String producerId;
    private String descriptorId;
    private String state;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrincipalAgreement that = (PrincipalAgreement) o;
        return Objects.equals(principalId, that.principalId) && Objects.equals(purposeId, that.purposeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(principalId, purposeId);
    }
}
