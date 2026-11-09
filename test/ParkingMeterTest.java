/*
 * ParkingMeterTest.java -- unit tests for the Lesson 15 exercise
 * ICS 4U0 - Lesson 15 Exercise: ParkingMeter
 *
 * Two kinds of tests:
 *   - Output tests run MeterApp.main() exactly as it will be graded: they type
 *     the input lines on System.in and check the lines it prints. MeterApp only
 *     works if your addPayment does.
 *   - Javadoc tests read src/ParkingMeter.java as text and check that every
 *     constructor and method has a Javadoc comment with the right tags.
 * You don't need to change this file -- just run the tests (see README.md)
 * and fix ParkingMeter.java until they all pass.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParkingMeterTest {

    private InputStream originalIn;
    private PrintStream originalOut;
    private Locale originalLocale;

    @BeforeEach
    public void redirectIoAndPinLocale() {
        originalIn = System.in;
        originalOut = System.out;
        originalLocale = Locale.getDefault();
        // printf follows the default locale -- pin it so "%.2f" prints
        // 2.25, not 2,25, on every machine.
        Locale.setDefault(Locale.CANADA);
    }

    @AfterEach
    public void restoreIoAndLocale() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        Locale.setDefault(originalLocale);
    }

    /**
     * Feeds {@code input} to MeterApp.main() on System.in and returns
     * everything it printed to System.out.
     */
    private String run(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));

        MeterApp.main(new String[0]);

        return captured.toString(StandardCharsets.UTF_8);
    }

    /**
     * Compares output line by line, trimming trailing whitespace on each
     * line and ignoring one blank line at the very end (println's final
     * newline). Everything else -- including every space, colon, and dollar
     * sign -- is compared exactly.
     */
    private void assertOutputEquals(String expected, String actual) {
        String[] expectedLines = expected.stripTrailing().split("\n", -1);
        String[] actualLines = actual.stripTrailing().split("\n", -1);

        assertEquals(expectedLines.length, actualLines.length,
                "Expected " + expectedLines.length + " line(s) of output, got " + actualLines.length
                        + ".\n--- expected ---\n" + expected + "--- actual ---\n" + actual);

        for (int i = 0; i < expectedLines.length; i++) {
            assertEquals(expectedLines[i].stripTrailing(), actualLines[i].stripTrailing(),
                    "Line " + (i + 1) + " didn't match.\n--- expected ---\n" + expected
                            + "--- actual ---\n" + actual);
        }
    }

    // ------------------------------------------------------------------
    // Output tests: addPayment
    // ------------------------------------------------------------------

    @Test
    public void exampleFromReadme() {
        String input = "20\n120\n4\npay 2.50\nwait 30\npay 6.00\nwait 45\n";
        String expected = "Paid $2.50: 50 min added, 50 min left\n"
                + "Waited 30 min: 20 min left\n"
                + "Paid $6.00: 100 min added, 120 min left\n"
                + "Waited 45 min: 75 min left\n"
                + "Expired: no\n"
                + "Cost to fill: $2.25\n";
        assertOutputEquals(expected, run(input));
    }

    @Test
    public void paymentsAddToTimeAlreadyOnTheMeter() {
        // A second payment adds to what's there. minutesLeft = bought would
        // throw away the first payment's 20 minutes.
        String input = "20\n120\n2\npay 1.00\npay 1.00\n";
        String expected = "Paid $1.00: 20 min added, 20 min left\n"
                + "Paid $1.00: 20 min added, 40 min left\n"
                + "Expired: no\n"
                + "Cost to fill: $4.00\n";
        assertOutputEquals(expected, run(input));
    }

    @Test
    public void paymentBuysWholeMinutesRoundedDown() {
        // $1.03 at 20 min per dollar is 20.6 minutes: it buys 20, not 21.
        // $0.04 is 0.8 minutes: it buys 0. Cast to int, don't use Math.round.
        String input = "20\n120\n2\npay 1.03\npay 0.04\n";
        String expected = "Paid $1.03: 20 min added, 20 min left\n"
                + "Paid $0.04: 0 min added, 20 min left\n"
                + "Expired: no\n"
                + "Cost to fill: $5.00\n";
        assertOutputEquals(expected, run(input));
    }

    @Test
    public void paymentNeverFillsPastTheMaximum() {
        // $5.00 buys 100 minutes, but this meter only holds 60.
        // The postcondition says getMinutesLeft() is at most getMaxMinutes().
        String input = "20\n60\n2\npay 5.00\npay 1.00\n";
        String expected = "Paid $5.00: 60 min added, 60 min left\n"
                + "Paid $1.00: 0 min added, 60 min left\n"
                + "Expired: no\n"
                + "Cost to fill: $0.00\n";
        assertOutputEquals(expected, run(input));
    }

    @Test
    public void returnsOnlyTheMinutesActuallyAdded() {
        // The second $2.00 buys 40 minutes, but only 20 fit. @return says
        // "the number of minutes actually added", so it returns 20, not 40.
        String input = "20\n60\n2\npay 2.00\npay 2.00\n";
        String expected = "Paid $2.00: 40 min added, 40 min left\n"
                + "Paid $2.00: 20 min added, 60 min left\n"
                + "Expired: no\n"
                + "Cost to fill: $0.00\n";
        assertOutputEquals(expected, run(input));
    }

    // ------------------------------------------------------------------
    // Javadoc tests: these read ParkingMeter.java as text
    // ------------------------------------------------------------------

    /** One public constructor or method found in the source file. */
    private static class Member {
        String name;
        String returnType;          // null for a constructor
        List<String> params = new ArrayList<String>();
        String javadoc;             // null if there isn't one
        int line;

        String label() {
            return name + "(" + String.join(", ", params) + ") on line " + line;
        }
    }

    private static final Pattern HEADER = Pattern.compile(
            "^\\s*public\\s+(?:static\\s+)?(?:([\\w<>\\[\\]]+)\\s+)?(\\w+)\\s*\\(([^)]*)\\)\\s*\\{?\\s*$");

    private List<String> sourceLines() {
        Path path = Path.of("src", "ParkingMeter.java");
        if (!Files.exists(path)) {
            path = Path.of("..", "src", "ParkingMeter.java");
        }
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail("Couldn't read src/ParkingMeter.java: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns the Javadoc comment directly above line {@code index}
     * (skipping blank lines and annotations), or null if there isn't one.
     * A plain block comment that starts with a single star doesn't count.
     */
    private String javadocAbove(List<String> lines, int index) {
        int end = index - 1;
        while (end >= 0 && (lines.get(end).trim().isEmpty() || lines.get(end).trim().startsWith("@"))) {
            end--;
        }
        if (end < 0 || !lines.get(end).trim().endsWith("*/")) {
            return null;
        }
        for (int start = end; start >= 0; start--) {
            String t = lines.get(start).trim();
            if (t.startsWith("/**")) {
                return String.join("\n", lines.subList(start, end + 1));
            }
            if (t.startsWith("/*")) {
                return null;
            }
        }
        return null;
    }

    private List<Member> members() {
        List<String> lines = sourceLines();
        List<Member> found = new ArrayList<Member>();
        for (int i = 0; i < lines.size(); i++) {
            Matcher m = HEADER.matcher(lines.get(i));
            if (m.matches()) {
                Member member = new Member();
                member.returnType = m.group(1);
                member.name = m.group(2);
                member.line = i + 1;
                for (String p : m.group(3).split(",")) {
                    String[] words = p.trim().split("\\s+");
                    if (words.length >= 2) {
                        member.params.add(words[words.length - 1]);
                    }
                }
                member.javadoc = javadocAbove(lines, i);
                found.add(member);
            }
        }
        assertTrue(found.size() >= 7, "Expected at least 7 public constructors/methods in ParkingMeter.java, found "
                + found.size() + ". Don't change the method headers.");
        return found;
    }

    /** The comment's text with the slash-stars and leading stars removed, one entry per line. */
    private List<String> textLines(String javadoc) {
        String body = javadoc.trim();
        body = body.substring(3, body.length() - 2);    // drop /** and */
        List<String> result = new ArrayList<String>();
        for (String line : body.split("\n")) {
            String t = line.trim();
            if (t.startsWith("*")) {
                t = t.substring(1).trim();
            }
            result.add(t);
        }
        return result;
    }

    /** Each @tag with the text after it, including any lines it wraps onto. */
    private List<String> tags(String javadoc) {
        List<String> result = new ArrayList<String>();
        for (String line : textLines(javadoc)) {
            if (line.startsWith("@")) {
                result.add(line);
            } else if (!result.isEmpty() && !line.isEmpty()) {
                int last = result.size() - 1;
                result.set(last, result.get(last) + " " + line);
            }
        }
        return result;
    }

    private String summary(String javadoc) {
        String text = "";
        for (String line : textLines(javadoc)) {
            if (line.startsWith("@")) {
                break;
            }
            text = text + line;
        }
        return text.trim();
    }

    @Test
    public void classHasJavadoc() {
        List<String> lines = sourceLines();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().startsWith("public class ParkingMeter")) {
                String doc = javadocAbove(lines, i);
                assertNotNull(doc, "The class needs a Javadoc comment (/** ... */) directly above "
                        + "'public class ParkingMeter'. The /* Name: ... */ header doesn't count: it starts with /* not /**.");
                assertTrue(!summary(doc).isEmpty(), "The class Javadoc needs a sentence describing what a ParkingMeter is.");
                return;
            }
        }
        fail("Couldn't find 'public class ParkingMeter' in src/ParkingMeter.java.");
    }

    @Test
    public void everyConstructorAndMethodHasJavadoc() {
        for (Member m : members()) {
            assertNotNull(m.javadoc, m.label() + " has no Javadoc comment. Put /** ... */ directly above it "
                    + "(a // or /* comment doesn't count).");
            assertTrue(!summary(m.javadoc).isEmpty(), m.label()
                    + ": the Javadoc needs a description sentence before the @ tags.");
        }
    }

    @Test
    public void paramTagsMatchTheParameters() {
        for (Member m : members()) {
            if (m.javadoc == null) {
                fail(m.label() + " has no Javadoc comment yet.");
            }
            List<String> names = new ArrayList<String>();
            for (String tag : tags(m.javadoc)) {
                if (tag.startsWith("@param")) {
                    String[] words = tag.split("\\s+", 3);
                    assertTrue(words.length == 3 && !words[2].trim().isEmpty(), m.label()
                            + ": '" + tag + "' needs a description after the parameter name.");
                    names.add(words[1]);
                }
            }
            assertEquals(m.params, names, m.label()
                    + ": needs one @param for each parameter, in the same order, using the parameter's exact name.");
        }
    }

    @Test
    public void returnTagOnlyWhenSomethingIsReturned() {
        for (Member m : members()) {
            if (m.javadoc == null) {
                fail(m.label() + " has no Javadoc comment yet.");
            }
            String returnTag = null;
            for (String tag : tags(m.javadoc)) {
                if (tag.startsWith("@return")) {
                    returnTag = tag;
                }
            }
            boolean returnsSomething = m.returnType != null && !m.returnType.equals("void");
            if (returnsSomething) {
                assertNotNull(returnTag, m.label() + " returns " + m.returnType + ", so it needs an @return tag.");
                assertTrue(returnTag.trim().length() > "@return".length(), m.label()
                        + ": @return needs a description of what's returned.");
            } else {
                String kind = (m.returnType == null) ? "a constructor" : "a void method";
                assertTrue(returnTag == null, m.label() + " is " + kind + ", so it shouldn't have an @return tag.");
            }
        }
    }

    @Test
    public void timePassesStatesItsPrecondition() {
        for (Member m : members()) {
            if (m.name.equals("timePasses")) {
                assertNotNull(m.javadoc, "timePasses has no Javadoc comment yet.");
                assertTrue(m.javadoc.toLowerCase().contains("precondition"), "timePasses only works for "
                        + "minutes >= 0. Its Javadoc needs a 'Precondition:' line saying so.");
                return;
            }
        }
        fail("Couldn't find timePasses in src/ParkingMeter.java. Don't change the method headers.");
    }
}
