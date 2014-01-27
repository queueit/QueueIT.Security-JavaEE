package queueit.security;

public interface IValidateResultRepository {
    IValidateResult GetValidationResult(IQueue queue);
    void SetValidationResult(IQueue queue, IValidateResult validationResult);
}
