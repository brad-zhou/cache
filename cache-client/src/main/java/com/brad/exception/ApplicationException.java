package com.brad.exception;


import com.brad.utils.rest.Result;

/**
 * 通用异常
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1311708438878595435L;
    private int code;
    private Object data;

    public ApplicationException(String message) {
        super(message, null, true, true);
        this.code = Result.RECODE_VALIDATE_ERROR;
    }

    public ApplicationException(int code, String message) {
        super(message, null, true, true);
        this.code = code;
    }

    public ApplicationException(String message, Object data) {
        super(message, null, true, true);
        this.code = Result.RECODE_VALIDATE_ERROR;
        this.data = data;
    }

    public ApplicationException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
