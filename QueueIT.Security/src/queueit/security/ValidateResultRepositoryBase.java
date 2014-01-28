package queueit.security;

public abstract class ValidateResultRepositoryBase implements IValidateResultRepository{
    private static String SessionQueueId = "QueueITAccepted-SDFrts345E-";
    
    @Override
    public abstract IValidateResult getValidationResult(IQueue queue);
    @Override
    public abstract void setValidationResult(IQueue queue, IValidateResult validationResult);
    
    protected static String generateKey(String customerId, String eventId)
    {
        return SessionQueueId + customerId + "-" + eventId;
    }
}
