package org.tud.imir.ex2.service;

public class Utils {
    public static String stripExtension(String str) {
        // Handle null case specially.
        if (str == null) {
            return null;
        }

        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) {
            return str;
        }

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    /**
     * Get text between two strings. Passed limiting strings are not
     * included into result.
     *
     * @param text
     *         Text to search in.
     * @param textFrom
     *         Text to start cutting from (exclusive).
     * @param textTo
     *         Text to stop cuutting at (exclusive).
     */
    public static String getBetweenStrings(String text, String textFrom, String textTo) {
        String result;

        // Cut the beginning of the text to not occasionally meet a 'textTo' value in it:
        result = text.substring(
                text.indexOf(textFrom) + textFrom.length(),
                text.length());

        // Cut the excessive ending of the text:
        result = result.substring(
                0,
                result.indexOf(textTo));

        return result;
    }
}
