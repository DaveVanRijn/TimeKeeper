/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

import Exception.CharNotSupportedException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dave van Rijn, Student 500714558, Klas IS202
 */
public class EncryptionKey {

    private Map<Integer, String> alphabet;
    private Map<String, Integer> binary;
    private Map<String, Integer> counters;

    public EncryptionKey() {
        initMaps();
    }

    /**
     * Encrypt given String to an encrypted code
     *
     * @param s The String that must be encrypted
     * @return The encrypted version of the String s
     * @throws Exception.CharNotSupportedException
     */
    public String encrypt(String s) throws CharNotSupportedException {
        String isSupported = isSupported(s);
        if (isSupported == null) {
            String[] letters = s.split("");
            String encryption = "";
            for (String letter : letters) {
                int worth = 0;
                for (int i : alphabet.keySet()) {
                    if (alphabet.get(i).equals(letter)) {
                        worth = i;
                        break;
                    }
                }
                String temp = "";
                for (String encLetter : binary.keySet()) {
                    int value;
                    if ((value = binary.get(encLetter)) <= worth) {
                        temp += encLetter;
                        worth -= value;
                        if (worth == 0) {
                            break;
                        }
                    }
                }
                int usedLetters = temp.length();
                String letterCounter = "";
                for (String count : counters.keySet()) {
                    if (counters.get(count) == usedLetters) {
                        letterCounter = count;
                    }
                }
                encryption += letterCounter + temp;
            }
            encryption += "0";
            return encryption;
        } else {
            throw new CharNotSupportedException(isSupported.charAt(0));
        }
    }

    /**
     * Decrypt the given encryption code to a normal String
     *
     * @param s The encryption code
     * @return The encryption code decrypted to a normal String
     * @throws Exception.CharNotSupportedException If the encryption code contains
     * unsupported characters
     */
    public String decrypt(String s) throws CharNotSupportedException {
        String isSupported = isEncryptSupported(s.substring(0, s.length() - 1));
        if (isSupported == null) {
            String result = "";
            String[] letters = s.split("");
            int letterCount = 0;
            String countLetter = letters[letterCount];

            while (!countLetter.equals("0")) {
                if (counters.containsKey(countLetter)) {
                    letterCount++;
                    int letterAmount = counters.get(countLetter);
                    int worth = 0;

                    for (int i = letterCount; letterAmount > 0; i++, letterAmount--, letterCount++) {
                        String encLetter = letters[i];
                        for (int j : alphabet.keySet()) {
                            if (alphabet.get(j).equals(encLetter)) {
                                worth += binary.get(encLetter);
                                break;
                            }
                        }
                    }
                    countLetter = letters[letterCount];
                    if (alphabet.containsKey(worth)) {
                        result += alphabet.get(worth);
                    } else {
                        throw new CharNotSupportedException("Letter value " + worth + " is not supported by this key.");
                    }
                } else {
                    throw new CharNotSupportedException(countLetter.charAt(0));
                }
            }
            return result;
        } else {
            throw new CharNotSupportedException("Character " + isSupported + " is not supported by the decryptor of this key.");
        }
    }

    /**
     * Check if the given String is supported by this encryption key by checking
     * each character
     *
     * @param s The String that must be checked
     * @return The String (character) that is not supported, null if fully
     * supported
     */
    public String isSupported(String s) {
        if (s.isEmpty()) {
            return null;
        }
        String[] array = s.split("");
        for (String str : array) {
            if (!alphabet.containsValue(str)) {
                return str;
            }
        }
        return null;
    }

    private String isEncryptSupported(String s) {
        String[] array = s.split("");
        for (String str : array) {
            if (!binary.containsKey(str)) {
                return str;
            }
        }
        return null;
    }

    private void initMaps() {
        alphabet = new HashMap<>();
        binary = new HashMap<>();
        counters = new HashMap<>();

        String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", ",", "-",
            "_", "!", "?", " "};

        int i = 1;
        for (String s : letters) {
            alphabet.put(i, s);
            i++;
        }

        int devider = 1;
        int binaryCount = 0;

        while (devider < alphabet.size()) {
            binaryCount++;
            devider *= 2;
        }

        int bin = devider / 2;

        int capital = 65;
        int normal = 97;
        while (binaryCount > 0) {
            String s = "";
            s += (char) capital;
            capital++;
            binary.put(s, bin);
            binaryCount--;

            if (binaryCount > 0) {
                bin /= 2;
                s = "";
                s += (char) normal;
                normal++;
                binary.put(s, bin);
                binaryCount--;

                if (binaryCount > 0) {
                    bin /= 2;
                }
            }
        }

        for (int k = 1, j = 26; k <= binary.size(); k++, j++) {
            counters.put(letters[j], k);
        }

    }
}
