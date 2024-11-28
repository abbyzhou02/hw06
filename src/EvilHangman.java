import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EvilHangman {
    private ArrayList<String> wordList;
    private HashSet<Character> previousGuesses;
    private TreeSet<Character> incorrectGuesses; // behaves like a hash set, but orders the entries!
    private String currentPattern;
    private Scanner inputScanner;
    private int wordLength;

    public EvilHangman() {
        this("engDictionary.txt");
    }

    public EvilHangman(String filename) {
        try {
            wordList = dictionaryToList(filename);
        } catch (IOException e) {
            System.out.printf(
                    "Couldn't read from the file %s. Verify that you have it in the right place and try running again.",
                    filename);
            e.printStackTrace();
            System.exit(0);
        }

        previousGuesses = new HashSet<>();
        incorrectGuesses = new TreeSet<>();

        int randomIndex = new Random().nextInt(wordList.size());
        wordLength = wordList.get(randomIndex).length();
        wordList = new ArrayList<>(filterWordsByLength());

        currentPattern = "_".repeat(wordLength);
        inputScanner = new Scanner(System.in);
    }

    public void start() {
        while (!isSolved()) {
            char guess = promptForGuess();
            recordGuess(guess);
        }
        printVictory();
    }

    private char promptForGuess() {
        while (true) {
            System.out.println("Guess a letter.\n");
            printProgress();
            System.out.println("Incorrect guesses:\n" + incorrectGuesses.toString());
            String input = inputScanner.next();
            if (input.length() != 1) {
                System.out.println("Please enter a single character.");
            } else if (previousGuesses.contains(input.charAt(0))) {
                System.out.println("You've already guessed that.");
            } else {
                return input.charAt(0);
            }
        }
    }

    private void recordGuess(char guess) {
        if (previousGuesses.add(guess)) {
            Map<String, List<String>> families = createWordFamilies(guess);
            updateGameState(families);
            if (!currentPattern.contains(String.valueOf(guess))) {
                incorrectGuesses.add(guess);
            }
        }
    }

    private List<String> filterWordsByLength() {
        return wordList.stream()
                .filter(word -> word.length() == wordLength)
                .collect(Collectors.toList());
    }

    private Map<String, List<String>> createWordFamilies(char guess) {
        Map<String, List<String>> families = new HashMap<>();
        for (String word : wordList) {
            StringBuilder pattern = new StringBuilder(currentPattern);
            for (int i = 0; i < wordLength; i++) {
                if (word.charAt(i) == guess) {
                    pattern.setCharAt(i, guess);
                }
            }
            String familyKey = pattern.toString();
            families.computeIfAbsent(familyKey, k -> new ArrayList<>()).add(word);
        }
        return families;
    }

    private void updateGameState(Map<String, List<String>> families) {
        List<String> largestFamily = families.values().stream()
                .max(Comparator.comparingInt(List::size))
                .orElseThrow();

        String newPattern = families.entrySet().stream()
                .filter(e -> e.getValue().equals(largestFamily))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();

        if (newPattern.equals(currentPattern)) {
            incorrectGuesses.add(previousGuesses.iterator().next());
        }

        currentPattern = newPattern;
        wordList = new ArrayList<>(largestFamily);
    }

    private void printProgress() {
        System.out.println(String.join(" ", currentPattern.split("")));
    }

    private void printVictory() {
        System.out.printf("Congrats! The word was %s%n", currentPattern);
    }

    private static ArrayList<String> dictionaryToList(String filename) throws IOException {
        ArrayList<String> wordList = new ArrayList<>();
        try (FileInputStream fs = new FileInputStream(filename);
             Scanner scnr = new Scanner(fs)) {
            while (scnr.hasNext()) {
                wordList.add(scnr.next());
            }
        }
        return wordList;
    }

    public static void main(String[] args) {
        EvilHangman game = new EvilHangman();
        game.start();
    }

    public String getCurrentPattern() {
        return currentPattern;
    }

    public Set<Character> getIncorrectGuesses() {
        return Collections.unmodifiableSet(incorrectGuesses);
    }

    public Set<Character> getPreviousGuesses() {
        return Collections.unmodifiableSet(previousGuesses);
    }

    public void makeGuess(char guess) {
        if (!Character.isLetter(guess)) {
            throw new IllegalArgumentException("Must be a letter");
        }
        if (previousGuesses.contains(guess)) {
            return;
        }
        recordGuess(guess);
    }

    public boolean isSolved() {
        return !currentPattern.contains("_");
    }
}
