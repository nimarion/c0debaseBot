package de.c0debase.bot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Biosphere
 * @date 23.01.18
 */
public class StringUtils {

    /**
     * Forbidden characters
     */
    private static final List<Character> ESCAPE_CHARACTERS = new ArrayList<Character>() {{
        this.add('*');
        this.add('_');
        this.add('`');
        this.add('~');
    }};

    /**
     * Find all urls in a text
     *
     * @param text
     * @return a list with all urls
     */
    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    /**
     * Replace all forbidden characters
     */
    public static String replaceCharactar(final String input) {
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

}
