import java.io.*;
import java.util.*;

public class EvilHangman {
    Set<String> wordSet; // Current possible words
    private Set<Character> guessedLetters; // Letters guessed by the player
    private String currentPattern; // Current revealed pattern (e.g., -e--)

    // Constructor to initialize the game with a given word length and dictionary
    public EvilHangman(List<String> dictionary, int wordLength) {
        wordSet = new HashSet<>();
        guessedLetters = new HashSet<>();
        currentPattern = "-".repeat(wordLength);

        for (String word : dictionary) {
            if (word.length() == wordLength) {
                wordSet.add(word);
            }
        }

        if (wordSet.isEmpty()) {
            throw new IllegalArgumentException("No words of the specified length in the dictionary.");
        }
    }

    // Process a guessed letter and update the state
    public boolean processGuess(char guess) {
        if (guessedLetters.contains(guess)) {
            System.out.println("You've already guessed that letter. Try again.");
            return false;
        }

        guessedLetters.add(guess);
        Map<String, Set<String>> wordFamilies = partitionWords(guess);
        chooseLargestFamily(wordFamilies);


        return currentPattern.contains(String.valueOf(guess));
    }

    // Partition the wordSet into families based on the guessed letter's placement
    private Map<String, Set<String>> partitionWords(char guess) {
        Map<String, Set<String>> families = new HashMap<>();

        for (String word : wordSet) {
            StringBuilder patternBuilder = new StringBuilder(currentPattern);

            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    patternBuilder.setCharAt(i, guess);
                }
            }

            String pattern = patternBuilder.toString();
            families.computeIfAbsent(pattern, k -> new HashSet<>()).add(word);
        }

        return families;
    }

    // Choose the largest family and update the wordSet and current pattern
    private void chooseLargestFamily(Map<String, Set<String>> families) {
        int maxSize = 0;
        String bestPattern = currentPattern;
        for (Map.Entry<String, Set<String>> entry : families.entrySet()) {
            if (entry.getValue().size() > maxSize) {
                maxSize = entry.getValue().size();
                bestPattern = entry.getKey();
            }
        }

        wordSet = families.get(bestPattern);
        currentPattern = bestPattern;
    }

    // Check if the game is won
    public boolean isGameWon() {
        return !currentPattern.contains("-");
    }

    // Check if the game is lost (no words left)
    public boolean isGameLost() {
        return wordSet.isEmpty();
    }

    // Get the current revealed pattern
    public String getCurrentPattern() {
        return currentPattern;
    }

    // Get guessed letters
    public Set<Character> getGuessedLetters() {
        return guessedLetters;
    }
}