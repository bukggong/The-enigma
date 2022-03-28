package enigma;

import java.util.HashMap;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Jenna(dayeon)
 */
class Alphabet {


    /** String of alpha. */
    private String alpha;

    /** Saving alphabet to map. */
    private HashMap<Character, Integer> map = new HashMap<Character, Integer>();

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        alpha = chars;

        for (int i = 0; i < chars.length(); i++) {
            char check = chars.charAt(i);
            if (map.containsKey(check)) {
                throw new EnigmaException("Error: it does already contain");
            } else if (chars.charAt(i) == '*') {
                throw new EnigmaException("Error : it contains *");
            } else if (chars.charAt(i) == ')') {
                throw new EnigmaException("Error : it contains )");
            } else if (chars.charAt(i) == '(') {
                throw new EnigmaException("Erro : it contains (");
            } else if (chars.charAt(i) == ' ') {
                throw new EnigmaException("Error : it contains blank");
            } else {
                map.put(check, i);
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }


    /** Returns the size of the alphabet. */
    int size() {
        int alphaSize = alpha.length();
        return alphaSize;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        if (map.containsKey(ch)) {
            return true;
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return alpha.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return map.get(ch);
    }

}
