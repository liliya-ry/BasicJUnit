package exceptions;

import java.util.List;

public class MultipleFailuresError extends Error {
    private static final String DEFAULT_HEADING = "Multiple Failures";

    private final String message;
    private String heading;

    public MultipleFailuresError(String heading, List<Throwable> throwableList) {
        this.heading = heading != null ? heading : DEFAULT_HEADING;
        this.message = createMessage(throwableList);
        for (Throwable throwable : throwableList) {
            this.addSuppressed(throwable);
        }
    }

    private String createMessage(List<Throwable> throwableList) {
        String message = heading + " ("+ throwableList.size() + " failures)";
        for (Throwable throwable : throwableList) {
            message += "\n   " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
        }
        return message;
    }

    public String getMessage() {
        return message;
    }
}
