package queueit.security;

import java.util.Date;

public interface IValidateResultRepository {
    IValidateResult getValidationResult(IQueue queue);
    void setValidationResult(IQueue queue, IValidateResult validationResult);
    void setValidationResult(IQueue queue, IValidateResult validationResult, Date expirationTime);
    void cancel(IQueue queue, IValidateResult validationResult);
}
