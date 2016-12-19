/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

/**
 * Created by emengjzs on 2016/9/7.
 */
public class LogFileException extends Exception {
    private Type type;
    private Object object;

    public LogFileException(Throwable e) {
        super(e);
    }

    public LogFileException(String msg) {
        super(msg);
    }

    public LogFileException(Type type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }


    public LogFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogFileException(Type errorType) {
        super();
        this.type = errorType;
    }

    public LogFileException(Type errorType , Throwable cause) {
        super(cause);
        this.type = errorType;
    }


    public enum Type {
        UNKNOWN_IO_ERROR,
        UNKNOWN_RECORD_TYPE_ERROR,
        CRC_NOT_MATCH_ERROR,
        RECORD_DATA_ERRPR,
    }


    public <E> E getObject() {
        return (E) object;
    }
}
