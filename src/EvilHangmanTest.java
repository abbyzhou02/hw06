import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class EvilHangmanTest {
    private EvilHangman game;

    @Before
    public void setUp() {
        game = new EvilHangman("test_dictionary.txt");
    }

    @Test
    public void testInitialWordLength() {
        String pattern = game.getCurrentPattern();
        assertTrue(pattern.length() > 0);
        assertTrue(pattern.matches("_+"));
    }

    @Test
    public void testPatternUpdates() {
        String initialPattern = game.getCurrentPattern();
        game.makeGuess('e');
        String newPattern = game.getCurrentPattern();
        assertFalse(initialPattern.equals(newPattern) && newPattern.contains("e"));
    }

    @Test
    public void testIncorrectGuesses() {
        game.makeGuess('x');
        Set<Character> incorrect = game.getIncorrectGuesses();
        assertTrue(incorrect.contains('x'));
    }

    @Test
    public void testPreviousGuesses() {
        game.makeGuess('a');
        Set<Character> previous = game.getPreviousGuesses();
        assertTrue(previous.contains('a'));
    }

    @Test
    public void testGameCompletion() {
        assertFalse(game.isSolved());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGuess() {
        game.makeGuess('1');
    }


    private void makeGuess(char c) {
        if (!Character.isLetter(c)) {
            throw new IllegalArgumentException("Must be a letter");
        }

    }
}