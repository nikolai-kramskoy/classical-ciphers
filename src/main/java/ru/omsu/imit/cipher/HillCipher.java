package ru.omsu.imit.cipher;

import java.io.*;

public class HillCipher {
    private HillCipher() {
    }
    
    public static void encrypt(Alphabet alphabet, Writer cipher, Reader plain,
                               int[][] key)
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
        if (key == null || key[0] == null || key[1] == null) {
            throw new IllegalArgumentException(
                    "key == null || key[0] == null || key[1] == null");
        }
        if (key.length != 2 || key[0].length != 2 || key[1].length != 2) {
            throw new IllegalArgumentException("only 2x2 key matrices are allowed");
        }
        
        final int D = key[0][0] * key[1][1] - key[0][1] * key[1][0];
        
        if (D == 0 || gcd(D, alphabet.size()) != 1) {
            throw new IllegalArgumentException(
                    "|key matrix| == 0 or gcd(|key matrix|, m) != 1");
        }
    
        final char[] ALPHABET = alphabet.getAlphabet();
    
        try (BufferedWriter bufOut = new BufferedWriter(cipher);
             BufferedReader bufIn = new BufferedReader(plain)) {
            int  intChar1, intChar2;
    
            while ((intChar1 = bufIn.read()) != -1) {
                if ((intChar2 = bufIn.read()) == -1)
                    break;
                
                char c1 = (char) intChar1;
                char c2 = (char) intChar2;
        
                int i1 = 0;
                int i2 = 0;
                for (; i1 < ALPHABET.length && ALPHABET[i1] != c1; ++i1) {
                    if (i1 == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c1 + "' is not present in the alphabet");
                    }
                }
                for (; i2 < ALPHABET.length && ALPHABET[i2] != c2; ++i2) {
                    if (i2 == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c2 + "' is not present in the alphabet");
                    }
                }
    
                int newIdx1 = (key[0][0] * i1 + key[0][1] * i2) % ALPHABET.length;
                int newIdx2 = (key[1][0] * i1 + key[1][1] * i2) % ALPHABET.length;
        
                if (newIdx1 < 0)
                    newIdx1 += ALPHABET.length;
                if (newIdx2 < 0)
                    newIdx2 += ALPHABET.length;
        
                bufOut.write(ALPHABET[newIdx1]);
                bufOut.write(ALPHABET[newIdx2]);
            }
        }
    }
    
    public static void decrypt(Alphabet alphabet, Writer plain, Reader cipher,
                               int[][] key)
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
        if (key == null || key[0] == null || key[1] == null) {
            throw new IllegalArgumentException(
                    "key == null || key[0] == null || key[1] == null");
        }
        if (key.length != 2 || key[0].length != 2 || key[1].length != 2) {
            throw new IllegalArgumentException("only 2x2 key matrices are allowed");
        }
    
        final int D = key[0][0] * key[1][1] - key[0][1] * key[1][0];
    
        if (D == 0 || gcd(D, alphabet.size()) != 1) {
            throw new IllegalArgumentException(
                    "|key matrix| == 0 or gcd(|key matrix|, m) != 1");
        }
        
        final char[] ALPHABET = alphabet.getAlphabet();
        
        final int[][] invKey = { { (key[1][1] / D) % ALPHABET.length,  (-key[0][1] / D) % ALPHABET.length},
                                 { (-key[1][0] / D) % ALPHABET.length, (key[0][0] / D) % ALPHABET.length} };
        
        try (BufferedWriter bufOut = new BufferedWriter(plain);
             BufferedReader bufIn = new BufferedReader(cipher)) {
            int  intChar1, intChar2;
            
            while ((intChar1 = bufIn.read()) != -1) {
                if ((intChar2 = bufIn.read()) == -1)
                    break;
                
                char c1 = (char) intChar1;
                char c2 = (char) intChar2;
                
                int i1 = 0;
                int i2 = 0;
                for (; i1 < ALPHABET.length && ALPHABET[i1] != c1; ++i1) {
                    if (i1 == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c1 + "' is not present in the alphabet");
                    }
                }
                for (; i2 < ALPHABET.length && ALPHABET[i2] != c2; ++i2) {
                    if (i2 == ALPHABET.length - 1) {
                        throw new IllegalArgumentException("symbol '"
                                + c2 + "' is not present in the alphabet");
                    }
                }
                
                int newIdx1 = (invKey[0][0] * i1 + invKey[0][1] * i2) % ALPHABET.length;
                int newIdx2 = (invKey[1][0] * i1 + invKey[1][1] * i2) % ALPHABET.length;
                
                if (newIdx1 < 0)
                    newIdx1 += ALPHABET.length;
                if (newIdx2 < 0)
                    newIdx2 += ALPHABET.length;
                
                bufOut.write(ALPHABET[newIdx1]);
                bufOut.write(ALPHABET[newIdx2]);
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
}
