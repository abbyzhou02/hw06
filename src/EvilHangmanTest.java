import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class EvilHangmanTest {
    private EvilHangman g;

    @Before
    public void setUp() {
        g = new EvilHangman("testDictionary.txt");
    }

    @Test
    public void testInitialWordLength() {
        String pattern = g.getCurrentPattern();
        assertTrue(pattern.length() > 0);
        assertTrue(pattern.matches("_+"));
    }

    @Test
    public void testPatternUpdates() {
        String initialPattern = g.getCurrentPattern();
        g.makeGuess('e');
        String newPattern = g.getCurrentPattern();
        assertFalse(initialPattern.equals(newPattern) && newPattern.contains("e"));
    }

    @Test
    public void testIncorrectGuesses() {
        g.makeGuess('x');
        Set<Character> incorrect = g.getIncorrectGuesses();
        assertTrue(incorrect.contains('x'));
    }

    @Test
    public void testPreviousGuesses() {
        g.makeGuess('a');
        Set<Character> previous = g.getPreviousGuesses();
        assertTrue(previous.contains('a'));
    }

    @Test
    public void testGameCompletion() {
        assertFalse(g.isSolved());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGuess() {
        g.makeGuess('1');
    }


    private void makeGuess(char c) {
        if (!Character.isLetter(c)) {
            throw new IllegalArgumentException("Must be a letter");
        }

    }
}