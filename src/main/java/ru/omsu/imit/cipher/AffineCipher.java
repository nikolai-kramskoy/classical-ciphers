package ru.omsu.imit.cipher;

import java.io.*;

public class AffineCipher {
    private AffineCipher() {
    }
    
    public static void encrypt(Alphabet alphabet, Writer cipher, Reader plain,
                               int a, int b)
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
        if (gcd(a, alphabet.size()) != 1) {
            throw new IllegalArgumentException("gcd(a, m) != 1");
        }
    
        final char[] ALPHABET = alphabet.getAlphabet();
    
        try (BufferedWriter bufOut = new BufferedWriter(cipher);
                BufferedReader bufIn = new BufferedReader(plain)) {
            int  intChar;
    
            while ((intChar = bufIn.read()) != -1) {
                char c = (char) intChar;
        
                int i = 0;
                for (; i < ALPHABET.length && ALPHABET[i] != c; ++i) {
                    if (i == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the alphabet");
                    }
                }
    
                int newIdx = (a*i + b) % ALPHABET.length;
    
                if (newIdx < 0)
                    newIdx += ALPHABET.length;
    
                bufOut.write(ALPHABET[newIdx]);
            }
        }
    }
    
    public static void decrypt(Alphabet alphabet, Writer plain, Reader cipher,
                               int a, int b)
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
        if (gcd(a, alphabet.size()) != 1) {
            throw new IllegalArgumentException("gcd(a, m) != 1");
        }
        
        final char[] ALPHABET = alphabet.getAlphabet();
        
        final int MUL_INV_IDX = findMulInvIdx(a, ALPHABET.length);
    
        try (BufferedWriter bufOut = new BufferedWriter(plain);
             BufferedReader bufIn = new BufferedReader(cipher)) {
            int  intChar;
        
            while ((intChar = bufIn.read()) != -1) {
                char c = (char) intChar;
    
                int i = 0;
                for (; i < ALPHABET.length && ALPHABET[i] != c; ++i) {
                    if (i == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c + "' is not present in the alphabet");
                    }
                }
    
                int newIdx = (MUL_INV_IDX * (i-b)) % ALPHABET.length;
    
                if (newIdx < 0)
                    newIdx += ALPHABET.length;
                
                bufOut.write(ALPHABET[newIdx]);
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
