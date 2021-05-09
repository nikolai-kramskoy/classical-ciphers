package ru.omsu.imit.cipher;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        File alphabetFile = new File("alphabet.txt");
    
        AffineCipher cipher;
        
        try (Reader in = new FileReader(alphabetFile, StandardCharsets.UTF_8)) {
            cipher = new AffineCipher(in);
        }
    
        File cipherFile = new File("cipher.txt");
        File plainFile = new File("plain.txt");
        
        try (Writer out = new FileWriter(cipherFile, StandardCharsets.UTF_8);
                Reader in = new FileReader(plainFile, StandardCharsets.UTF_8)) {
            cipher.encrypt(out, in, 2, -9);
        }
    
        try (Writer out = new FileWriter("plain2.txt", StandardCharsets.UTF_8);
             Reader in = new FileReader(cipherFile, StandardCharsets.UTF_8)) {
            cipher.decrypt(out, in, 2, -9);
        }
    }
}
