package ru.omsu.imit.cipher;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 1 && args[0].equals("--help")) {
            printHelp();
        }
        else if (inputIsValid(args)) {
            try (Reader alphabetReader = new FileReader(args[2], StandardCharsets.UTF_8);
                 Writer out = new FileWriter(args[3], StandardCharsets.UTF_8);
                 Reader in = new FileReader(args[4], StandardCharsets.UTF_8)) {
                Alphabet alphabet = new Alphabet(alphabetReader);
    
                if ("caesar".equals(args[0])) {
                    int n = Integer.parseInt(args[5]);
    
                    if ("encrypt".equals(args[1])) {
                        CaesarCipher.encrypt(alphabet, out, in, n);
                    }
                    else if ("decrypt".equals(args[1])) {
                        CaesarCipher.decrypt(alphabet, out, in, n);
                    }
                }
                else if ("affine".equals(args[0])) {
                    int a = Integer.parseInt(args[5]);
                    int b = Integer.parseInt(args[6]);
    
                    if ("encrypt".equals(args[1])) {
                        AffineCipher.encrypt(alphabet, out, in, a, b);
                    }
                    else if ("decrypt".equals(args[1])) {
                        AffineCipher.decrypt(alphabet, out, in, a, b);
                    }
                }
                else if ("hill".equals(args[0])) {
                    int[][] key = { {Integer.parseInt(args[5]), Integer.parseInt(args[6])},
                                    {Integer.parseInt(args[7]), Integer.parseInt(args[8])} };
    
                    if ("encrypt".equals(args[1])) {
                        HillCipher.encrypt(alphabet, out, in, key);
                    }
                    else if ("decrypt".equals(args[1])) {
                        HillCipher.decrypt(alphabet, out, in, key);
                    }
                }
            }
        }
        else {
            System.out.println("Wrong usage, see --help");
        }
    }
    
    private static boolean inputIsValid(String[] args) {
        boolean isValid = false;
        
        // Caesar cipher args check
        isValid |= args.length == 6
                && "caesar".equals(args[0])
                && ("encrypt".equals(args[1]) || "decrypt".equals(args[1]));
    
        // Affine cipher args check
        isValid |= args.length == 7
                && "affine".equals(args[0])
                && ("encrypt".equals(args[1]) || "decrypt".equals(args[1]));
    
        // Hill cipher args check
        isValid |= args.length == 9
                && "hill".equals(args[0])
                && ("encrypt".equals(args[1]) || "decrypt".equals(args[1]));
        
        return isValid;
    }
    
    private static void printHelp() {
        System.out.println("Usage:");
        System.out.print("java Main cipher_name encryption_option alphabet_file_name ");
        System.out.println("output_file input_file keys");
        System.out.println();
        System.out.println("cipher_name:");
        System.out.println("    caesar - E(x) = (x + n) mod m (alphabet size),");
        System.out.println("             D(x) = (x - n) mod m (alphabet size);");
        System.out.println("    affine - E(x) = (a*x + b) mod m (alphabet size),");
        System.out.println("             D(x) = a^(-1) * (x - b) mod m (alphabet size);");
        System.out.println("    hill - E(p_1, p_2) = c_i = (k_i_1 * p_1 + k_i_2 * p_2) mod m (alphabet size),");
        System.out.println("           D(c_1, c_2) = p_i = ((k_i_1)^-1 * c_1 + (k_i_2)^-1 * c_2) mod m (alphabet size);");
        System.out.println();
        System.out.println("encryption_option:");
        System.out.println("    encrypt;");
        System.out.println("    decrypt;");
        System.out.println();
        System.out.println("keys:");
        System.out.println("    caesar cipher keys:");
        System.out.println("        n - any integer;");
        System.out.println("    affine cipher keys:");
        System.out.println("        a - any coprime to the size of the alphabet integer,");
        System.out.println("        b - any integer;");
        System.out.println("    hill cipher keys:");
        System.out.println("        key - 2x2 integer matrix, |key| != 0 and GCD(|key|, m) = 1;");
    }
}
