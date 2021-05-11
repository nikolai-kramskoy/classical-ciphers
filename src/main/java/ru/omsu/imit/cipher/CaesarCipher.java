package ru.omsu.imit.cipher;

import java.io.*;

public class CaesarCipher {
    private CaesarCipher() {
    }
    
    public static void encrypt(Alphabet alphabet,
                               Writer cipher, Reader plain, int n)
            throws IOException {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        if (cipher == null) {
            throw new IllegalArgumentException("cipher == null");
        }
        if (plain == null) {
            throw new IllegalArgumentException("plain == null");
        }
    
        final char[] ALPHABET = alphabet.getAlphabet();

        try (BufferedWriter bufOut = new BufferedWriter(cipher);
                BufferedReader bufIn = new BufferedReader(plain)) {
            int  intChar;
            char c;
            
            while ((intChar = bufIn.read()) != -1) {
                c = (char) intChar;
    
                int i = 0;
                for (; i < ALPHABET.length && ALPHABET[i] != c; ++i) {
                    if (i == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the ALPHABET");
                    }
                }
                
                int newIdx = (i+n) % ALPHABET.length;
                
                if (newIdx < 0)
                    newIdx += ALPHABET.length;
                
                bufOut.write(ALPHABET[newIdx]);
            }
        }
    }
    
    public static void decrypt(Alphabet alphabet,
                               Writer plain, Reader cipher, int n)
            throws IOException {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        if (plain == null) {
            throw new IllegalArgumentException("plain == null");
        }
        if (cipher == null) {
            throw new IllegalArgumentException("cipher == null");
        }
    
        final char[] ALPHABET = alphabet.getAlphabet();
        
        try (BufferedWriter bufOut = new BufferedWriter(plain);
                BufferedReader bufIn = new BufferedReader(cipher)) {
            int  intChar;
            char c;
            
            while ((intChar = bufIn.read()) != -1) {
                c = (char) intChar;
    
                int i = 0;
                for (; i < ALPHABET.length && ALPHABET[i] != c; ++i) {
                    if (i == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the ALPHABET");
                    }
                }
                
                int newIdx = (i-n) % ALPHABET.length;
    
                if (newIdx < 0)
                    newIdx += ALPHABET.length;
                
                bufOut.write(ALPHABET[newIdx]);
            }
        }
    }
}
