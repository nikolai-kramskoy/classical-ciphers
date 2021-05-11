package ru.omsu.imit.cipher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class Alphabet {
    private final char[] alphabet;
    
    public Alphabet() {
        alphabet = new char[26];
        
        char c = 'a';
        for (int i = 0; i < alphabet.length; ++i, ++c) {
            alphabet[i] = c;
        }
    }
    
    public Alphabet(char a, char b) {
        alphabet = new char[b-a+1];
        
        for (int i = 0; i < alphabet.length; ++i, ++a) {
            alphabet[i] = a;
        }
    }
    
    public Alphabet(char[] alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        
        this.alphabet = new char[alphabet.length];
        
        System.arraycopy(alphabet, 0, this.alphabet, 0, alphabet.length);
    }
    
    public Alphabet(Reader alphabet) throws IOException {
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
    
    public char[] getAlphabet() {
        char[] result = new char[alphabet.length];
        
        System.arraycopy(alphabet, 0, result, 0, alphabet.length);
        
        return result;
    }
    
    public char getCharacter(int idx) {
        return alphabet[idx];
    }
    
    public int size() {
        return alphabet.length;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Alphabet alphabet1 = (Alphabet) o;
        return Arrays.equals(alphabet, alphabet1.alphabet);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(alphabet);
    }
}
