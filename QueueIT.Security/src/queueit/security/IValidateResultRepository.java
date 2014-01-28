package queueit.security;

public interface IValidateResultRepository {
    IValidateResult getValidationResult(IQueue queue);
    void setValidationResult(IQueue queue, IValidateResult validationResult);
}
