package ru.omsu.imit.cipher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AffineCipher {
    private final char[] alphabet;
    
    public AffineCipher() {
        alphabet = new char[26];
        
        char c = 'a';
        for (int i = 0; i < alphabet.length; ++i, ++c) {
            alphabet[i] = c;
        }
    }
    
    public AffineCipher(char[] alphabet) {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet == null");
        }
        
        this.alphabet = new char[alphabet.length];
        
        System.arraycopy(alphabet, 0, this.alphabet, 0, alphabet.length);
    }
    
    public AffineCipher(Reader in) throws IOException {
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
        
                int i;
                for (i = 0; i < alphabet.length; ++i) {
                    if (i == alphabet.length - 1 && alphabet[i] != c) {
                        throw new IllegalArgumentException("symbol \'"
                                + c + "\' is not present in the alphabet");
                    }
            
                    if (alphabet[i] == c) {
                        break;
                    }
                }
    
                int newIdx = (a*i + b) % alphabet.length;
    
                if (newIdx < 0)
                    newIdx = alphabet.length - -newIdx;
                
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
        
        int mulInvIdx = findMulInvIdx(a, alphabet.length);
    
        try (BufferedWriter bufOut = new BufferedWriter(plain);
                BufferedReader bufIn = new BufferedReader(cipher)) {
            int  intChar;
            char c;
        
            while ((intChar = bufIn.read()) != -1) {
                c = (char) intChar;
            
                int i;
                for (i = 0; i < alphabet.length; ++i) {
                    if (i == alphabet.length - 1 && alphabet[i] != c) {
                        throw new IllegalArgumentException("symbol \'"
                                + c + "\' is not present in the alphabet");
                    }
                
                    if (alphabet[i] == c) {
                        break;
                    }
                }
    
                int newIdx = (mulInvIdx * (i-b)) % alphabet.length;
    
                if (newIdx < 0)
                    newIdx = alphabet.length - -newIdx;
                
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
