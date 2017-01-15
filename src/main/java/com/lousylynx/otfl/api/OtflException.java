package com.lousylynx.otfl.api;

public class OtflException extends Exception {

    public OtflException(String message) {
        this(message, "");
    }

    public OtflException(String format, Object... replacements) {
        super(String.format(format, replacements));
    }
}
