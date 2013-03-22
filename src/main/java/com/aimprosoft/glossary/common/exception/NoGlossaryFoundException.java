package com.aimprosoft.glossary.common.exception;

public class NoGlossaryFoundException extends GlossaryException{

    public NoGlossaryFoundException() {
    }

    public NoGlossaryFoundException(String message) {
        super(message);
    }

    public NoGlossaryFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoGlossaryFoundException(Throwable cause) {
        super(cause);
    }

    public NoGlossaryFoundException(long modelId) {
        this.modelId = modelId;
    }

    public NoGlossaryFoundException(Throwable cause, long modelId) {
        super(cause);
        this.modelId = modelId;
    }

    private long modelId;

    public long getModelId() {
        return modelId;
    }


}
