package de.c0debase.bot.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

    private static final List<Character> ESCAPE_CHARACTERS;

    static {
        ESCAPE_CHARACTERS = Arrays.asList('*', '_', '`', '~');
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

    public static String replaceLast(final String text, final String regex, final String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

}
