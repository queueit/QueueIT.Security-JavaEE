package queueit.security;

public abstract class ValidateResultBase implements IValidateResult{
    private IQueue queue;
    
    public IQueue getQueue() {
        return this.queue;
    }

    ValidateResultBase(IQueue queue)
    {
        this.queue = queue;
    }
}