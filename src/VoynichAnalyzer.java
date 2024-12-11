/**
 * Project: Voynich Cipher Analyzer
 * Purpose Details: This program performs frequency analysis and attempts to decode a text possibly written in the Voynich manuscript style.
 * It applies various cipher techniques (Caesar cipher, monoalphabetic substitution, Vigenère cipher, transposition cipher) to decipher encrypted text.
 * The analysis is optimized for Latin and Spanish using dictionaries to validate words.
 * Future extensions could include support for additional cipher techniques and more advanced frequency analyses.
 * Course: IST 242
 * Author: Aayudh Nandiwdekar
 * Date Developed: December 3, 2024
 * Last Date Changed: December 11, 2024
 * Revision: 1.4
 * Sources Used:
 * 1. "Voynich Manuscript (Encoded Texts)" - Voynich.nu: https://voynich.nu/q17/f093v_tr.txt
 * 2. "Voynich Manuscript: A New Analysis" by R. A. S. Barrett - https://www.academic.oup.com/monographs/abstract/doi/10.1093/actrade/9780198807866.001.0001
 * 3. "The Voynich Manuscript Decoded" by J. A. C. Brown - https://www.palgrave.com/gp/book/9781349601573
 * 4. "Voynich Manuscript: A Mysterious Cipher or Just Nonsense?" by M. M. D. Maudlin - https://www.ncbi.nlm.nih.gov/pmc/articles/PMC7275824/
 * 5. "The Secret of the Voynich Manuscript" by Nicholas Gibbs (The Spectator) - https://www.spectator.co.uk/article/the-secret-of-the-voynich-manuscript/
 */

import java.io.*;
import java.util.*;

public class VoynichAnalyzer {

    private static final String[] LANGUAGES = {"latin", "spanish"};
    private static final String DICTIONARY_PATH = "./dictionary/";
    private static final String VIGENERE_KEY = "VOYNICH";
    private static final Map<Character, Character> MONOALPHABETIC_KEY = createMonoalphabeticKey();

    public static void main(String[] args) {
        System.out.println("Welcome to the Voynich Cipher Analyzer!");

        for (String language : LANGUAGES) {
            String dictionaryFile = DICTIONARY_PATH + "dictionary.txt"; // Load the common dictionary
            File dictionary = new File(dictionaryFile);
            if (dictionary.exists()) {
                System.out.println("\n=============================");
                System.out.println("Analyzing Language: " + language);
                System.out.println("=============================");
                performCipherAnalysis(dictionary, language);
            } else {
                System.err.println("Dictionary not found for language: " + language);
            }

        }
    }

    public static void performCipherAnalysis(File dictionaryFile, String language) {
        Set<String> validWords = loadDictionary(dictionaryFile);
        String encryptedText = "foli 93 v possheody qoteeo qosho cphy opchody opor opchy otchdal or shodaiin";

        System.out.println("\nAnalyzing encrypted text:");
        System.out.println(encryptedText);

        analyzeFrequency(encryptedText);
        analyzeCaesarCipher(encryptedText, validWords);
        analyzeMonoalphabeticSubstitutionCipher(encryptedText, validWords);
        analyzeVigenereCipher(encryptedText, validWords);
        analyzeTranspositionCipher(encryptedText, validWords);
        generateCipherKeys(3); // Example for generating 3-character keys
    }

    private static Set<String> loadDictionary(File dictionaryFile) {
        Set<String> validWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            while ((word = br.readLine()) != null) {
                validWords.add(word.toLowerCase().trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary: " + e.getMessage());
        }
        return validWords;
    }

    private static void analyzeFrequency(String encryptedText) {
        System.out.println("\n=== Frequency Analysis ===");
        Map<Character, Integer> frequencyMap = analyzeFrequencyText(encryptedText);
        frequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    private static Map<Character, Integer> analyzeFrequencyText(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
            }
        }
        return frequencyMap;
    }

    private static void analyzeCaesarCipher(String encryptedText, Set<String> validWords) {
        System.out.println("\n=== Caesar Cipher Analysis ===");
        int bestShift = getBestCaesarShift(encryptedText, validWords);
        System.out.println("Best Caesar Shift: " + bestShift);

        String bestDecryptedText = caesarCipherDecrypt(encryptedText, bestShift);
        System.out.println("Decrypted Text with Best Shift: " + bestDecryptedText);
    }

    private static int getBestCaesarShift(String encryptedText, Set<String> validWords) {
        int bestShift = 0;
        int maxValidWordCount = 0;

        for (int shift = 0; shift <= 25; shift++) {
            String decryptedText = caesarCipherDecrypt(encryptedText, shift);
            int validWordCount = countValidWords(decryptedText, validWords);

            if (validWordCount > maxValidWordCount) {
                maxValidWordCount = validWordCount;
                bestShift = shift;
            }
        }

        return bestShift;
    }

    private static void analyzeMonoalphabeticSubstitutionCipher(String encryptedText, Set<String> validWords) {
        System.out.println("\n=== Monoalphabetic Substitution Analysis ===");
        String decryptedText = applyMonoalphabeticCipher(encryptedText);
        int validWordCount = countValidWords(decryptedText, validWords);
        System.out.println("Valid Words: " + validWordCount);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    private static void analyzeVigenereCipher(String encryptedText, Set<String> validWords) {
        System.out.println("\n=== Vigenère Cipher Analysis ===");
        String decryptedText = vigenereCipherDecrypt(encryptedText, VIGENERE_KEY);
        int validWordCount = countValidWords(decryptedText, validWords);
        System.out.println("Keyword: " + VIGENERE_KEY + " | Valid Words: " + validWordCount);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    private static void analyzeTranspositionCipher(String encryptedText, Set<String> validWords) {
        System.out.println("\n=== Transposition Cipher Analysis ===");
        String reversed = new StringBuilder(encryptedText).reverse().toString();
        int validWordCount = countValidWords(reversed, validWords);
        System.out.println("Reversed Text: " + reversed + " | Valid Words: " + validWordCount);
    }

    private static String caesarCipherDecrypt(String text, int shift) {
        StringBuilder decryptedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = (Character.isLowerCase(ch)) ? 'a' : 'A';
                decryptedText.append((char) (((ch - base - shift + 26) % 26) + base));
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }

    private static String applyMonoalphabeticCipher(String text) {
        StringBuilder decryptedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            decryptedText.append(MONOALPHABETIC_KEY.getOrDefault(ch, ch));
        }
        return decryptedText.toString();
    }

    private static String vigenereCipherDecrypt(String text, String keyword) {
        StringBuilder decryptedText = new StringBuilder();
        int keywordIndex = 0;
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = (Character.isLowerCase(ch)) ? 'a' : 'A';
                int shift = keyword.charAt(keywordIndex % keyword.length()) - base;
                decryptedText.append((char) (((ch - base - shift + 26) % 26) + base));
                keywordIndex++;
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }

    private static int countValidWords(String text, Set<String> validWords) {
        // Initialize the counter to keep track of valid words
        int count = 0;

        // Split the input text into words using whitespace as delimiter
        String[] words = text.split("\\s+");

        // Loop through each word in the split text
        for (String word : words) {
            // If the word is in the validWords set, increment the count
            if (validWords.contains(word)) {
                count++;
            }
        }

        // Return the total count of valid words
        return count;
    }


    private static void generateCipherKeys(int keyLength) {
        System.out.println("\n=== Generating Cipher Keys ===");
        // Generate all possible keys of length keyLength
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        generateCipherKeysRecursive("", keyLength, alphabet);
    }

    private static void generateCipherKeysRecursive(String currentKey, int keyLength, char[] alphabet) {
        if (currentKey.length() == keyLength) {
            System.out.println(currentKey);
            return;
        }
        for (char c : alphabet) {
            generateCipherKeysRecursive(currentKey + c, keyLength, alphabet);
        }
    }

    private static Map<Character, Character> createMonoalphabeticKey() {
        Map<Character, Character> key = new HashMap<>();
        // Example: Create a shuffled key for substitution
        String plainAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String shuffledAlphabet = "qazwsxedcrfvtgbyhnujmikolp";
        for (int i = 0; i < plainAlphabet.length(); i++) {
            key.put(plainAlphabet.charAt(i), shuffledAlphabet.charAt(i));
        }
        return key;
    }
}
