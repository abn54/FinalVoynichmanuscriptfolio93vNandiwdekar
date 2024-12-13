# Voynich Cipher Analyzer

## Project Overview
The **Voynich Cipher Analyzer** is a Java-based program designed to analyze and potentially decode texts written in the mysterious Voynich Manuscript. The manuscript, written in an unknown script and language, has perplexed cryptographers and linguists for centuries. This tool attempts to decipher the manuscript's cipher using a range of common cipher decryption techniques, such as Caesar cipher, monoalphabetic substitution cipher, Vigenère cipher, and transposition cipher. 

The project also integrates language validation by checking decrypted texts against word dictionaries for various languages. The goal of the program is to provide possible decryptions of encrypted texts by applying multiple techniques, measuring their validity, and suggesting the most likely cipher used.

## Purpose of the Project
The purpose of this project is to:
- Analyze encrypted texts using common cipher techniques.
- Apply frequency analysis to determine character patterns.
- Decrypt the text using multiple cipher methods: Caesar cipher, monoalphabetic substitution cipher, Vigenère cipher, and transposition cipher.
- Validate decrypted text using language-specific dictionaries.
- Explore the decryption of the Voynich Manuscript-like texts by utilizing cryptographic techniques.

## Features
- **Frequency Analysis:** Measures the frequency of characters in the encrypted text, which is useful for cipher decryption techniques like the Caesar cipher and monoalphabetic substitution.
- **Caesar Cipher:** Decrypts the text by trying all 25 possible shifts (since the Caesar cipher is a shift cipher with a fixed key length of 26 letters).
- **Monoalphabetic Substitution Cipher:** Attempts to decode the text by replacing each letter with another based on a pre-defined substitution key.
- **Vigenère Cipher:** Decrypts the text using a Vigenère cipher with a fixed key.
- **Transposition Cipher:** Checks for a possible transposition cipher by reversing the encrypted text and analyzing the result.
- **Dictionary Validation:** Checks the decrypted text for validity by comparing the words against language dictionaries.

## Languages Supported
The cipher analyzer supports the following languages for validation using external dictionary files:
- **Italian**
- **Latin**
- **Portuguese**
- **French**
- **Malay**
- **Proto-Romance**
- **Arabic**
- **Greek**
- **Hebrew**

Each language has a corresponding dictionary file, which is required for the validation process.

## How to Use
### Prerequisites
Before running the program, ensure you have the following:
1. **Java Development Kit (JDK) 8 or higher**: Ensure that Java is installed and the environment variable `JAVA_HOME` is set up properly.
2. **Dictionary Files**: The dictionary files for the supported languages must be downloaded and placed in the `./dictionary/` directory. Each file should be named `<language>_dictionary.txt` (for example: `italian_dictionary.txt`, `latin_dictionary.txt`).

### Steps to Set Up the Project:
1. **Clone the Repository:**
   Clone this repository to your local machine using the following command:

   ```bash
   git clone https://github.com/abn54/FinalVoynichmanuscriptfolio93vNandiwdekar.git
