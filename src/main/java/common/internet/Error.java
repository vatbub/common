package common.internet;

/**
 * A class to represent error messages that can be sent over http in json
 */
@SuppressWarnings("unused")
public class Error {
    String error;
    String stacktrace;

    public Error(String error) {
        this(error, "");
    }

    public Error(String error, String stacktrace) {
        this.error = error;
        this.stacktrace = stacktrace;
    }
}
