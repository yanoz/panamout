package fr.panamout;

import com.google.common.io.Files;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yann on 3/29/15.
 */
public class CleanFiles {


    private static final String BARS = ".*<DT><H3 ADD_DATE=\".*\">Bars</H3>";
    private static final String RESTAURANTS = ".*<DT><H3 ADD_DATE=\".*\">Restos</H3>";
    private static final String INDEX = ".*<DT><H3 ADD_DATE=\".*\">(.*)</H3>";
    private static final String ENTRY = "^\\s+<DT><A HREF=\".*\"\\sADD_DATE=\".*\"\\sICON=\".*\">(.*)</A>$";

    public static void main(String args[]) throws IOException {
        List<String> lines = Files.readLines(new File("/home/yann/Documents/bookmarks_3_29_15.html"), Charset.defaultCharset());
        Map<String, List<String>> bars = new HashMap();
        Map<String, List<String>> rests = new HashMap();

        boolean getBars = false;
        boolean getRest = false;
        for (String line : lines) {
            if (isBarLine(line)) {
                getBars = true;
                getRest = false;
                continue;
            }
            if (isRestLine(line)) {
                getBars = false;
                getRest = true;
                continue;
            }
            if (isIndexLine(line)) {
                if (getBars) {
                    bars.put(extractIndex(line), new ArrayList<String>());
                }
                if (getRest) {
                    rests.put(extractIndex(line), new ArrayList<String>());
                }
                continue;
            }
            if (iEntryLine(line)) {
                if (getBars) {
                    bars.get(bars.size() - 1).add(extractIndex(line));
                }
                if (getRest) {
                    rests.get(rests.size() - 1).add(extractIndex(line));
                }
                continue;
            }
        }
        System.out.print(bars);
    }

    private static boolean isBarLine(String line) {
        return checkLineType(line, BARS);
    }

    private static boolean isRestLine(String line) {
        return checkLineType(line, RESTAURANTS);
    }

    private static boolean isIndexLine(String line) {
        return checkLineType(line, INDEX);
    }

    private static boolean iEntryLine(String line) {
        return checkLineType(line, ENTRY);
    }

    private static boolean checkLineType(String line, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        return m.matches();
    }

    private static String extractIndex(String line) {
        return extractData(line, INDEX);
    }

    private static String extractEntry(String line) {
        return extractData(line, ENTRY);
    }

    private static String extractData(String line, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(line);
        if (m.find()) {
            return StringEscapeUtils.unescapeHtml(m.group(1));
        }
        return null;
    }
}
