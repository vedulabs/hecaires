package com.hecaires.rest.exceptions;

public class DuplicateResourceException  extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String msg) {
        super(msg);
    }
}