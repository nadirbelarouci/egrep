package fr.sorbonne.daar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KMP {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("60390-0.txt"));
        String pattern = "The";
        String line = reader.readLine();
        int i = 1;
        while (line != null) {
            System.out.println(i + " : " + match(pattern.toCharArray(), kmpTable(pattern), line.toCharArray()));
            i++;
            line = reader.readLine();
        }
        reader.close();
    }


    public static int match(char[] facteur, int[] retenue, char[] text) {
        int i = 0, j = 0;
        while (i < text.length) {
            if (j == facteur.length) return i - facteur.length;
            if (facteur[j] == text[i]) {
                i++;
                j++;
            } else {
                if (retenue[j] == -1) {
                    i++;
                    j = 0;
                } else {
                    j = retenue[j];
                }
            }
        }
        if (j == facteur.length) return i - j;
        return -1;
    }

    public static int[] kmpTable(String pattern) {
        int pos = 1, cnd = 0;
        int[] table = new int[pattern.length() + 1];

        table[0] = -1;

        while (pos < pattern.length()) {
            if (pattern.charAt(pos) == pattern.charAt(cnd)) {
                table[pos] = table[cnd];
            } else {
                table[pos] = cnd;
                cnd = table[cnd];
                while (cnd >= 0 && pattern.charAt(pos) != pattern.charAt(cnd)) {
                    cnd = table[cnd];
                }
            }
            pos = pos + 1;
            cnd = cnd + 1;
        }
        table[pos] = cnd;

        return table;
    }

    private static class Couple<T, U> {
        protected T key;
        protected U value;

        public Couple(T key, U value) {
            this.key = key;
            this.value = value;
        }
    }
}