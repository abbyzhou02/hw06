import java.io.*;
import java.util.*;

public class EvilHangmanRunner {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> dictionary = loadDictionary("engDictionary.txt");

        System.out.print("Enter the length of the word to guess: ");
        int wordLength = Integer.parseInt(reader.readLine());

        EvilHangman game = new EvilHangman(dictionary, wordLength);
        int remainingGuesses = 6;

        while (remainingGuesses > 0 && !game.isGameWon()) {
            System.out.println("\nCurrent word: " + game.getCurrentPattern());
            System.out.println("Guessed letters: " + game.getGuessedLetters());
            System.out.println("Remaining guesses: " + remainingGuesses);

            System.out.print("Enter your guess: ");
            String input = reader.readLine();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Invalid input. Please guess a single letter.");
                continue;
            }

            char guess = input.charAt(0);
            boolean correctGuess = game.processGuess(guess);

            if (!correctGuess) {
                remainingGuesses--;
                System.out.println("Wrong guess!");
            }
        }

        if (game.isGameWon()) {
            System.out.println("Congratulations! You guessed the word: " + game.getCurrentPattern());
        } else {
            System.out.println("Game over! The word was: " + game.wordSet.iterator().next());
        }
    }

    private static List<String> loadDictionary(String fileName) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        }
        return words;
    }

}
