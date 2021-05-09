package ru.omsu.imit.cipher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        alphabet = new char[b-a];
        
        char c = a;
        for (int i = 0; i < alphabet.length; ++i, ++c) {
            alphabet[i] = c;
        }
    }
    
    public CaesarCipher(char[] alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        
        this.alphabet = new char[alphabet.length];
        
        System.arraycopy(alphabet, 0, this.alphabet, 0, alphabet.length);
    }
    
    public CaesarCipher(Reader in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("in == null");
        }
        
        List<Character> listAlphabet = new ArrayList<>();
        
        BufferedReader bufIn = new BufferedReader(in);
        
        int intChar;
        while ((intChar = bufIn.read()) != -1) {
            listAlphabet.add((char) intChar);
        }
        
        alphabet = new char[listAlphabet.size()];
        
        for (int i = 0; i < alphabet.length; ++i) {
            alphabet[i] = listAlphabet.get(i);
        }
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
                
                int i;
                for (i = 0; i < alphabet.length; ++i) {
                    if (i == alphabet.length - 1 && alphabet[i] != c) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the alphabet");
                    }
                    
                    if (alphabet[i] == c) {
                        break;
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
                
                int i;
                for (i = 0; i < alphabet.length; ++i) {
                    if (i == alphabet.length - 1 && alphabet[i] != c) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the alphabet");
                    }
                    
                    if (alphabet[i] == c) {
                        break;
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
