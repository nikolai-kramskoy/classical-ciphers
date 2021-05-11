package ru.omsu.imit.cipher;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

public class CaesarCipher {
    private final char[] alphabet;
    
    public CaesarCipher() {
        alphabet = new char[26];
        
        char c = 'a';
        for (int i = 0; i < alphabet.length; ++i, ++c) {
            alphabet[i] = c;
        }
    }
    
    public CaesarCipher(char a, char b) {
        alphabet = new char[b-a+1];
        
        for (int i = 0; i < alphabet.length; ++i, ++a) {
            alphabet[i] = a;
        }
    }
    
    public CaesarCipher(char[] alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        
        this.alphabet = new char[alphabet.length];
        
        System.arraycopy(alphabet, 0, this.alphabet, 0, alphabet.length);
    }
    
    public CaesarCipher(Reader alphabet) throws IOException {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        
        Deque<Character> dequeAlphabet = new LinkedList<>();
        
        BufferedReader bufIn = new BufferedReader(alphabet);
        
        int intChar;
        while ((intChar = bufIn.read()) != -1) {
            dequeAlphabet.addLast((char) intChar);
        }
        
        this.alphabet = new char[dequeAlphabet.size()];
        
        for (int i = 0; i < this.alphabet.length; ++i) {
            this.alphabet[i] = dequeAlphabet.removeFirst();
        }
    }
    
    public CaesarCipher(CaesarCipher caesarCipher) {
        if (caesarCipher == null) {
            throw new IllegalArgumentException("caesarCipher == null");
        }
        
        alphabet = new char[caesarCipher.alphabet.length];
        
        System.arraycopy(caesarCipher.alphabet, 0, alphabet, 0, alphabet.length);
    }
    
    public void encrypt(Writer cipher, Reader plain, int n)
            throws IOException {
        if (cipher == null) {
            throw new IllegalArgumentException("cipher == null");
        }
        if (plain == null) {
            throw new IllegalArgumentException("plain == null");
        }

        try (BufferedWriter bufOut = new BufferedWriter(cipher);
                BufferedReader bufIn = new BufferedReader(plain)) {
            int  intChar;
            char c;
            
            while ((intChar = bufIn.read()) != -1) {
                c = (char) intChar;
    
                int i = 0;
                for (; i < alphabet.length && alphabet[i] != c; ++i) {
                    if (i == alphabet.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the alphabet");
                    }
                }
                
                int newIdx = (i+n) % alphabet.length;
                
                if (newIdx < 0)
                    newIdx += alphabet.length;
                
                bufOut.write(alphabet[newIdx]);
            }
        }
    }
    
    public void decrypt(Writer plain, Reader cipher, int n)
            throws IOException {
        if (plain == null) {
            throw new IllegalArgumentException("plain == null");
        }
        if (cipher == null) {
            throw new IllegalArgumentException("cipher == null");
        }
        
        try (BufferedWriter bufOut = new BufferedWriter(plain);
                BufferedReader bufIn = new BufferedReader(cipher)) {
            int  intChar;
            char c;
            
            while ((intChar = bufIn.read()) != -1) {
                c = (char) intChar;
    
                int i = 0;
                for (; i < alphabet.length && alphabet[i] != c; ++i) {
                    if (i == alphabet.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the alphabet");
                    }
                }
                
                int newIdx = (i-n) % alphabet.length;
    
                if (newIdx < 0)
                    newIdx += alphabet.length;
                
                bufOut.write(alphabet[newIdx]);
            }
        }
    }
}
