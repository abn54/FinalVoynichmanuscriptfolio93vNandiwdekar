/**
 * Project: Voynich Cipher Analyzer
 * Purpose Details: This program performs frequency analysis and attempts to decode a text possibly written in the Voynich manuscript style.
 * It applies various cipher techniques (Caesar cipher, monoalphabetic substitution, Vigenère cipher, transposition cipher) to decipher encrypted text.
 * The analysis is optimized for Akkadian, Arabic, Aramaic, Egyptian, Etruscan, French, Greek, Hebrew, Italian, Latin, Portuguese, Proto-Romance, and Sumerian using dictionaries to validate words.
 * Future extensions could include support for additional cipher techniques and more advanced frequency analyses.
 * Course: IST 242
 * Author: Aayudh Nandiwdekar
 * Date Developed: December 3, 2024
 * Last Date Changed: December 13, 2024
 * Revision: 1.6
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

    private static final String[] LANGUAGES = {"akkadian", "anglosaxxon", "arabic", "aramaic", "celtic","chinese","egyptian", "etruscan","farsi","french", "german", "greek", "hebrew", "italian", "latin", "portuguese", "protoroman", "russian","sanskrit", "spanish", "sumerian"};
    private static final String DICTIONARY_PATH = "./dictionary/";  // Path to your dictionary files
    private static final String VIGENERE_KEY = "VOYNICH";
    private static final Map<Character, Character> MONOALPHABETIC_KEY = createMonoalphabeticKey();

    public static void main(String[] args) {
        System.out.println("Welcome to the Voynich Cipher Analyzer!");

        // Loop through supported languages and perform analysis
        for (String language : LANGUAGES) {
            String dictionaryFile = DICTIONARY_PATH + language + "_dictionary.txt"; // Correct path for dictionary files
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
        String encryptedText = "possheody qoteeo qosho cphy opchody opor opchy otchdal or shodaiin"; // Correct sample encrypted text

        // Clean the encrypted text by removing non-textual elements (page numbers, references, etc.)
        encryptedText = cleanText(encryptedText);

        System.out.println("\nAnalyzing encrypted text:");
        System.out.println(encryptedText);

        // Perform the various cipher analyses
        analyzeFrequency(encryptedText);
        analyzeCaesarCipher(encryptedText, validWords);
        analyzeMonoalphabeticSubstitutionCipher(encryptedText, validWords);
        analyzeVigenereCipher(encryptedText, validWords);
        analyzeTranspositionCipher(encryptedText, validWords);

        // Perform dictionary-based analysis for the language
        analyzeTextWithDictionary(encryptedText, validWords, language);
    }

    private static Set<String> loadDictionary(File dictionaryFile) {
        Set<String> validWords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            while ((word = br.readLine()) != null) {
                validWords.add(word.toLowerCase().trim()); // Add each word in lower case for case-insensitive matching
            }
        } catch (IOException e) {
            System.err.println("Error reading dictionary: " + e.getMessage());
        }
        return validWords;
    }

    private static String cleanText(String text) {
        // Remove any non-alphabetical characters, page numbers, or references like 'foli 93 v'
        return text.replaceAll("[^a-zA-Z\\s]", "").toLowerCase().trim();
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

        // Check dictionary validity after Caesar decryption
        analyzeTextWithDictionary(bestDecryptedText, validWords, "Caesar Decryption");
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

        // Check dictionary validity after monoalphabetic decryption
        analyzeTextWithDictionary(decryptedText, validWords, "Monoalphabetic Substitution");
    }

    private static void analyzeVigenereCipher(String encryptedText, Set<String> validWords) {
        System.out.println("\n=== Vigenère Cipher Analysis ===");
        String decryptedText = vigenereCipherDecrypt(encryptedText, VIGENERE_KEY);
        int validWordCount = countValidWords(decryptedText, validWords);
        System.out.println("Keyword: " + VIGENERE_KEY + " | Valid Words: " + validWordCount);
        System.out.println("Decrypted Text: " + decryptedText);

        // Check dictionary validity after Vigenere decryption
        analyzeTextWithDictionary(decryptedText, validWords, "Vigenère Cipher");
    }

    private static void analyzeTranspositionCipher(String encryptedText, Set<String> validWords) {
        System.out.println("\n=== Transposition Cipher Analysis ===");
        String reversed = new StringBuilder(encryptedText).reverse().toString();
        int validWordCount = countValidWords(reversed, validWords);
        System.out.println("Reversed Text: " + reversed + " | Valid Words: " + validWordCount);

        // Check dictionary validity after transposition decryption
        analyzeTextWithDictionary(reversed, validWords, "Transposition Cipher");
    }

    private static void analyzeTextWithDictionary(String encryptedText, Set<String> validWords, String cipherType) {
        System.out.println("\n=== Dictionary-Based Analysis for " + cipherType + " ===");
        String[] words = encryptedText.split("\\s+");
        int validWordCount = 0;
        Set<String> matchedWords = new HashSet<>();

        for (String word : words) {
            if (validWords.contains(word.toLowerCase())) {
                validWordCount++;
                matchedWords.add(word.toLowerCase());
            }
        }

        System.out.println("Valid Words: " + validWordCount);
        System.out.println("Matched Words: " + matchedWords);
    }

    private static int countValidWords(String text, Set<String> validWords) {
        String[] words = text.split("\\s+");
        int validWordCount = 0;
        for (String word : words) {
            if (validWords.contains(word.toLowerCase())) {
                validWordCount++;
            }
        }
        return validWordCount;
    }

    private static String caesarCipherDecrypt(String text, int shift) {
        StringBuilder decryptedText = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                decryptedText.append((char) ((ch - base - shift + 26) % 26 + base));
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }

    private static String applyMonoalphabeticCipher(String encryptedText) {
        StringBuilder decryptedText = new StringBuilder();
        for (char ch : encryptedText.toCharArray()) {
            if (Character.isLetter(ch)) {
                decryptedText.append(MONOALPHABETIC_KEY.getOrDefault(Character.toLowerCase(ch), ch));
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }

    private static String vigenereCipherDecrypt(String encryptedText, String key) {
        StringBuilder decryptedText = new StringBuilder();
        int keyIndex = 0;
        for (char ch : encryptedText.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                decryptedText.append((char) ((ch - base - (key.charAt(keyIndex % key.length()) - base) + 26) % 26 + base));
                keyIndex++;
            } else {
                decryptedText.append(ch);
            }
        }
        return decryptedText.toString();
    }

    private static Map<Character, Character> createMonoalphabeticKey() {
        Map<Character, Character> key = new HashMap<>();
        // Example: simple substitution for testing
        key.put('a', 'z');
        key.put('b', 'y');
        key.put('c', 'x');
        key.put('d', 'w');
        // Continue for the rest of the alphabet...
        return key;
    }
}
