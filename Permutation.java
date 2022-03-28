package enigma;


import java.util.Arrays;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jenna(dayeon)
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;

        String cutting = cycles.replace("(", "");
        cutting = cutting.replace(")", " ");
        _cycles = new ArrayList<>(Arrays.asList(cutting.split(" ")));
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {

        if (cycle.isEmpty()) {
            throw new EnigmaException("empty cycle cannot use");
        }
        _cycles.add(cycle);

    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int wrapOfp = wrap(p);
        int index;
        char charOfp = _alphabet.toChar(wrapOfp);
        char character;

        for (String s : _cycles) {
            if (s.indexOf(charOfp) != -1) {
                index = s.indexOf(charOfp) + 1;
                if (index >= s.length()) {
                    index = 0;
                }
                character = s.charAt(index);
                return _alphabet.toInt(character);
            }
        }
        return wrapOfp;

    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {

        int wrapOfp = wrap(c);
        int index;
        char charOfp = _alphabet.toChar(wrapOfp);
        char character;

        for (String s : _cycles) {
            if (s.indexOf(charOfp) != -1) {
                index = s.indexOf(charOfp) - 1;
                if (index < 0) {
                    index = s.length() - 1;
                }
                character = s.charAt(index);
                return _alphabet.toInt(character);
            }
        }
        return wrapOfp;

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int place = permute(_alphabet.toInt(p));
        return _alphabet.toChar(place);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int place = invert(_alphabet.toInt(c));
        return _alphabet.toChar(place);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int len = 0;
        for (String s : _cycles) {
            len += s.length();
            if (s.length() == 1) {
                return false;
            }
        }
        if (len != _alphabet.size()) {
            return false;
        }
        return true;


    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles array. */
    private ArrayList<String> _cycles;
}
