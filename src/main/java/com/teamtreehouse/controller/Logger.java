package com.teamtreehouse.controller;
// This class is used as mocked class when all other methods
// sent different messages. It is also executes almost all System.out.print
// calls
// e.g.: it is invoked in protected constructor with any method
// than mocked in class. And used with verify(logger).setMessage
// to check message
// Also used for decoration purposes
public class Logger {
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLACK = "\u001B[30m";
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final String ANSI_YELLOW = "\u001B[33m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    protected static final String ANSI_PURPLE = "\u001B[35m";
    protected static final String ANSI_CYAN = "\u001B[36m";
    protected static final String ANSI_WHITE = "\u001B[37m";

    private String mWelcomeMessage;
    private String mChoiceMessage;
    private String mSimpleMessage;
    private String mErrorMessage;
    private String mHintMessage;
    private String mSuccessMessage;
    private String mExceptionMessage;

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
        System.out.println(ANSI_RED + "[Error]: " + errorMessage + ANSI_RESET);
    }
    public void setHintMessage(String hintMessage) {
        mHintMessage = hintMessage;
        System.out.println(ANSI_YELLOW + "[Hint]: " + hintMessage + ANSI_RESET);
    }
    public void setSuccessMessage(String successMessage) {
        mSuccessMessage = successMessage;
        System.out.println(ANSI_GREEN + "[Success]: " + successMessage + ANSI_RESET);
    }
    public void setExceptionMessage(String exceptionMessage) {
        mExceptionMessage = exceptionMessage;
        System.out.println("[Exception]: " + exceptionMessage);
    }
    public void setWelcomeMessage(String message) {
        mWelcomeMessage = message;
        System.out.println(message);
    }
    public void setChoiceMessage(String choiceMessage) {
        mChoiceMessage = choiceMessage;
        System.out.println(ANSI_CYAN + "[Input]: " + choiceMessage + ANSI_RESET);
    }
    public void setSimpleMessage(String simpleMessage) {
        mSimpleMessage = simpleMessage;
        System.out.println(simpleMessage);
    }
    // default constructor: nothing is here
    public Logger() {
    }
}
