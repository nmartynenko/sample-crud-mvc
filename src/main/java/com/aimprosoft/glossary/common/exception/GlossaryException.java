package com.aimprosoft.glossary.common.exception;

import com.aimprosoft.glossary.ApplicationException;

public class GlossaryException extends ApplicationException {
    public GlossaryException() {
        super();
    }

    public GlossaryException(String message) {
        super(message);
    }

    public GlossaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlossaryException(Throwable cause) {
        super(cause);
    }
}
