package ru.omsu.imit.cipher;

import java.io.*;
import java.util.Deque;
import java.util.LinkedList;

public class AffineCipher {
    private final char[] alphabet;
    
    public AffineCipher() {
        alphabet = new char[26];
        
        char c = 'a';
        for (int i = 0; i < alphabet.length; ++i, ++c) {
            alphabet[i] = c;
        }
    }
    
    public AffineCipher(char a, char b) {
        alphabet = new char[b-a+1];
        
        for (int i = 0; i < alphabet.length; ++i, ++a) {
            alphabet[i] = a;
        }
    }
    
    public AffineCipher(char[] alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        
        this.alphabet = new char[alphabet.length];
        
        System.arraycopy(alphabet, 0, this.alphabet, 0, alphabet.length);
    }
    
    public AffineCipher(Reader alphabet) throws IOException {
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
    
    public AffineCipher(AffineCipher affineCipher) {
        if (affineCipher == null) {
            throw new IllegalArgumentException("affineCipher == null");
        }
        
        alphabet = new char[affineCipher.alphabet.length];
        
        System.arraycopy(affineCipher.alphabet, 0, alphabet, 0, alphabet.length);
    }
    
    public void encrypt(Writer cipher, Reader plain, int a, int b)
            throws IOException {
        if (cipher == null) {
            throw new IllegalArgumentException("cipher == null");
        }
        if (plain == null) {
            throw new IllegalArgumentException("plain == null");
        }
        if (gcd(a, alphabet.length) != 1) {
            throw new IllegalArgumentException("a and m are not coprime");
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
    
                int newIdx = (a*i + b) % alphabet.length;
    
                if (newIdx < 0)
                    newIdx += alphabet.length;
    
                bufOut.write(alphabet[newIdx]);
            }
        }
    }
    
    public void decrypt(Writer plain, Reader cipher, int a, int b)
            throws IOException {
        if (plain == null) {
            throw new IllegalArgumentException("plain == null");
        }
        if (cipher == null) {
            throw new IllegalArgumentException("cipher == null");
        }
        if (gcd(a, alphabet.length) != 1) {
            throw new IllegalArgumentException("a and m are not coprime");
        }
        
        final int MUL_INV_IDX = findMulInvIdx(a, alphabet.length);
    
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
    
                int newIdx = (MUL_INV_IDX * (i-b)) % alphabet.length;
    
                if (newIdx < 0)
                    newIdx += alphabet.length;
                
                bufOut.write(alphabet[newIdx]);
            }
        }
    }
    
    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
    
        if (a == 0 && b == 0)
            return 0;
        if (a == 0)
            return b;
        if (b == 0)
            return a;
        
        while (a != b)
            if (a > b)
                a -= b;
            else
                b -= a;
        
        return a;
    }
    
    private static int findMulInvIdx(int a, int m) {
        while (a < 0)
            a += m;
        
        int idx = 0;
        for (; (a * idx) % m != 1; ++idx)
            ;
        
        return idx;
    }
}
