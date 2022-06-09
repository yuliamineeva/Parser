package ru.mineeva.parser.exception;

public class FileStorageException extends RuntimeException {

    private final String msg;

    public FileStorageException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
