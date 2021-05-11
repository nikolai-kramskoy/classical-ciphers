package ru.omsu.imit.cipher;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 1 && args[0].equals("--help")) {
            printHelp();
        }
        else if (args.length == 6 && args[0].equals("caesar")
                && args[1].equals("encrypt")) {
            CaesarCipher caesarCipher = new CaesarCipher(
                    new FileReader(args[2], StandardCharsets.UTF_8));
            
            int n = Integer.parseInt(args[5]);
            
            try (Writer out = new FileWriter(args[3], StandardCharsets.UTF_8);
                    Reader in = new FileReader(args[4], StandardCharsets.UTF_8)) {
                caesarCipher.encrypt(out, in, n);
            }
        }
        else if (args.length == 6 && args[0].equals("caesar")
                && args[1].equals("decrypt")) {
            CaesarCipher caesarCipher = new CaesarCipher(
                    new FileReader(args[2], StandardCharsets.UTF_8));
            
            int n = Integer.parseInt(args[5]);
    
            try (Writer out = new FileWriter(args[3], StandardCharsets.UTF_8);
                    Reader in = new FileReader(args[4], StandardCharsets.UTF_8)) {
                caesarCipher.decrypt(out, in, n);
            }
        }
        else if (args.length == 7 && args[0].equals("affine")
                && args[1].equals("encrypt")) {
            AffineCipher affineCipher = new AffineCipher(
                    new FileReader(args[2], StandardCharsets.UTF_8));
    
            int a = Integer.parseInt(args[5]);
            int b = Integer.parseInt(args[6]);
    
            try (Writer out = new FileWriter(args[3], StandardCharsets.UTF_8);
                    Reader in = new FileReader(args[4], StandardCharsets.UTF_8)) {
                affineCipher.encrypt(out, in, a, b);
            }
        }
        else if (args.length == 7 && args[0].equals("affine")
                && args[1].equals("decrypt")) {
            AffineCipher affineCipher = new AffineCipher(
                    new FileReader(args[2], StandardCharsets.UTF_8));
    
            int a = Integer.parseInt(args[5]);
            int b = Integer.parseInt(args[6]);
    
            try (Writer out = new FileWriter(args[3], StandardCharsets.UTF_8);
                    Reader in = new FileReader(args[4], StandardCharsets.UTF_8)) {
                affineCipher.decrypt(out, in, a, b);
            }
        }
        else {
            System.out.println("Wrong usage, see --help");
        }
    }
    
    private static void printHelp() {
        System.out.println("Usage:");
        System.out.print("java Main cipher_name encryption_option alphabet_file_name ");
        System.out.println("output_file input_file keys");
        System.out.println();
        System.out.println("cipher_name:");
        System.out.println("    caesar - E(x) = (x + n) mod m (alphabet size),");
        System.out.println("             D(x) = (x - n) mod m (alphabet size)");
        System.out.println("    affine - E(x) = (a*x + b) mod m (alphabet size),");
        System.out.println("             D(x) = a^(-1) * (x - b) mod m (alphabet size)");
        System.out.println();
        System.out.println("encryption_option:");
        System.out.println("    encrypt");
        System.out.println("    decrypt");
        System.out.println();
        System.out.println("keys:");
        System.out.println("    caesar cipher keys:");
        System.out.println("        n - int shift");
        System.out.println();
        System.out.println("    affine cipher keys:");
        System.out.println("        a - any coprime to the size of the alphabet integer");
        System.out.println("        b - any integer number");
    }
}
