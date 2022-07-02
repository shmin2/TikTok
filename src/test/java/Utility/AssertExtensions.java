package Utility;

public class AssertExtensions {
    public static void aggregate(Runnable[] assertActions) {
        StringBuilder assertionResult = new StringBuilder();

        for (Runnable action : assertActions) {
            try {
                action.run();
            } catch (AssertionError er) {
                assertionResult.append(System.lineSeparator());
                assertionResult.append(er.getMessage());
                assertionResult.append(System.lineSeparator());
            }
        }

        if (assertionResult.length() > 0) {
            throw new AssertionError(assertionResult.toString());
        }
    }
}
