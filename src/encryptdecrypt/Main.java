package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    interface Codigo {
        void encryptOrDecrypt(String word, int key, String inf);

        void encryptOrDecryptFile(int key, String in, String out, String inf);
    }

    static class Unicode implements Codigo {

        @Override
        public void encryptOrDecrypt(String word, int key, String inf) {
            char[] cadena = word.toCharArray();
            int letra;
            char enc;
            for (char c : cadena) {
                if (inf.equals("enc")) {
                    letra = c + key;
                } else {
                    letra = c - key;
                }
                enc = (char) letra;
                System.out.print(enc);
            }
        }

        @Override
        public void encryptOrDecryptFile(int key, String in, String out, String inf) {
            File file = new File(in);
            char[] cadena = new char[0];
            int letra;
            char enc;
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    cadena = scanner.nextLine().toCharArray();
                }
            } catch (FileNotFoundException e) {
                System.out.print("Error");
            }

            try (FileWriter fileWriter = new FileWriter(out, false)) {
                for (char c : cadena) {
                    if (inf.equals("enc")) {
                        letra = c + key;
                    } else {
                        letra = c - key;
                    }
                    enc = (char) letra;
                    fileWriter.write(enc);
                }
            } catch (IOException e) {
                System.out.print("Error");
            }

        }
    }

    static class Shift implements Codigo {

        @Override
        public void encryptOrDecrypt(String word, int key, String inf) {
            char[] encrip = word.toCharArray();
            int originalAlphabetPosition;
            String salida = "";
            if (inf == "dec") {
                key = 26 - (key % 26);
            }

            for (char character : encrip) {
                if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
                    originalAlphabetPosition = character >= 'a' && character <= 'z' ? character - 'a' : character - 'A';
                    int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                    char newCharacter = character >= 'a' && character <= 'z' ? (char) ('a' + newAlphabetPosition) : (char) ('A' + newAlphabetPosition);
                    salida += newCharacter;
                } else {
                    salida += character;
                }
            }

            System.out.println(salida.trim());
        }

        @Override
        public void encryptOrDecryptFile(int key, String in, String out, String inf) {
            File file = new File(in);
            char[] cadena = new char[0];
            int originalAlphabetPosition;
            String salida = "";
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    cadena = scanner.nextLine().toCharArray();
                }
            } catch (FileNotFoundException e) {
                System.out.print("Error");
            }

            try (FileWriter fileWriter = new FileWriter(out, false)) {
                if (inf == "dec") {
                    key = 26 - (key % 26);
                }

                for (char character : cadena) {
                    if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')) {
                        originalAlphabetPosition = character >= 'a' && character <= 'z' ? character - 'a' : character - 'A';
                        int newAlphabetPosition = (originalAlphabetPosition + key) % 26;
                        char newCharacter = character >= 'a' && character <= 'z' ? (char) ('a' + newAlphabetPosition) : (char) ('A' + newAlphabetPosition);
                        salida += newCharacter;
                    } else {
                        salida += character;
                    }
                }
                fileWriter.write(salida.trim());
            } catch (IOException e) {
                System.out.print("Error");
            }

        }
    }

    static class Eleccion {
        private Codigo codigo;
        private String word;
        private int key;
        private String inf;
        private String in;
        private String out;

        public Eleccion(Codigo codigo, String word, int key, String inf) {
            this.codigo = codigo;
            this.word = word;
            this.key = key;
            this.inf = inf;
            this.encriptacion();
        }

        public Eleccion(Codigo codigo, int key, String in, String out, String inf) {
            this.codigo = codigo;
            this.in = in;
            this.out = out;
            this.key = key;
            this.inf = inf;
            this.encriptacionFile();
        }

        public void encriptacion() {
            this.codigo.encryptOrDecrypt(word, key, inf);
        }

        public void encriptacionFile() {
            this.codigo.encryptOrDecryptFile(key, in, out, inf);
        }
    }

    public static void main(String[] args) {
        String inf = "enc";
        String word = "";
        int key = 0;
        String in = null;
        String out = null;
        String algoritmo = "shift";
        Eleccion eleccion;


        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-mode")) {
                inf = args[i + 1];
            } else if (args[i].equals("-key")) {
                key = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-data")) {
                word = args[i + 1];
            } else if (args[i].equals("-in")) {
                in = args[i + 1];
            } else if (args[i].equals("-out")) {
                out = args[i + 1];
            } else if (args[i].equals("-alg")) {
                algoritmo = args[i + 1];
            }
        }
        if (in != null && out != null) {
            if (algoritmo == "shift") {
                eleccion = new Eleccion(new Shift(), key, in, out, inf);
                eleccion.encriptacionFile();
            } else {
                eleccion = new Eleccion(new Unicode(), key, in, out, inf);
                eleccion.encriptacionFile();
            }

        } else {
            if (algoritmo == "shift") {
                eleccion = new Eleccion(new Shift(), key, in, out, inf);
                eleccion.encriptacion();
            } else {
                eleccion = new Eleccion(new Unicode(), key, in, out, inf);
                eleccion.encriptacion();
            }
        }
    }
}
