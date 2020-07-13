package de.c0debase.bot.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final List<Character> ESCAPE_CHARACTERS;
    private static final String URL_REGEX;

    static {
        ESCAPE_CHARACTERS = Arrays.asList('*', '_', '`', '~');
        URL_REGEX = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
    }

    /**
     * Replace all forbidden characters
     */
    public static String replaceCharacter(final String input) {
        final StringBuilder stringBuilder = new StringBuilder(input);
        for (int i = 0; i < stringBuilder.length(); i++) {
            final char c = stringBuilder.charAt(i);
            if (ESCAPE_CHARACTERS.contains(c)) {
                stringBuilder.replace(i, i + 1, "\\" + c);
                i += 1;
            }
        }
        return stringBuilder.toString();
    }

    /**
     *
     * @param content The message to check
     * @return If the given message contains a link
     */
    public static boolean containtsURL(final String content) {
        final Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(content);
        return m.find();
    }

    public static boolean isInteger(final String input) {
        try {
            Integer.valueOf(input);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
