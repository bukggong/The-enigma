package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Jenna(dayeon)
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        adRotors = new boolean[numRotors];
        _UsedRotors = new Rotor[numRotors];
        _allRotors = new HashMap<String, Rotor>(allRotors.size());
        for (Rotor rotor: allRotors) {
            _allRotors.put(rotor.name(), rotor);
        }

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;

    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _UsedRotors[k];

    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            Iterator<String> names = _allRotors.keySet().iterator();
            for (int j = 0; j < _allRotors.size(); j++) {
                String availableRotors = names.next();
                if (rotors[i].equals(availableRotors)) {
                    _UsedRotors[i] = (Rotor) _allRotors.get(availableRotors);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {

        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("incorrect setting length");
        }
        for (int i = 0; i < setting.length(); i++) {
            int num = _alphabet.toInt(setting.charAt(i));
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("it doesn't exist");
            }
            _UsedRotors[i + 1].set(num);
        }
    }

    void setRing(String ring) {

        for (int i = 0; i < ring.length(); i++) {
            int num = _alphabet.toInt(ring.charAt(i));
            if (!_alphabet.contains(ring.charAt(i))) {
                throw new EnigmaException("it doesn't exist");
            }
            _UsedRotors[i + 1].setRotorRing(num);
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugBoard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;

    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {


        adRotors[_numRotors - 1] = true;
        for (int i = _numRotors; i > 1; i--) {
            if (_UsedRotors[i - 2].rotates()) {
                if (_UsedRotors[i - 1].atNotch()) {
                    adRotors[i - 1] = true;
                    adRotors[i - 2] = true;
                }
            }
        }
        for (int j = 0; j < _numRotors; j++) {
            if (adRotors[j]) {
                _UsedRotors[j].advance();
                adRotors[j] = false;
            }
        }

    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        for (int i = _numRotors - 1; i > 0; i--) {
            c = _UsedRotors[i].convertForward(c);
        }
        c = _UsedRotors[0].convertForward(c);
        for (int i = 1; i < _numRotors; i++) {
            c = _UsedRotors[i].convertBackward(c);
        }
        return c;

    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {

        char[] lastmsg = msg.toCharArray();
        for (char let : lastmsg) {
            if (!_alphabet.contains(let)) {
                throw new EnigmaException("alphabet does not contain");
            }
        }
        for (int i = 0; i < msg.length(); i++) {
            int inside = _alphabet.toInt(msg.charAt(i));
            char charOut = _alphabet.toChar(convert(inside));

            lastmsg[i] = charOut;
        }
        return new String(lastmsg);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;


    /** Used rotors.*/
    private Rotor[] _UsedRotors;

    /** Number of  pawls.*/
    private int _pawls;

    /** Number of rotors. */
    private int _numRotors;

    /** Permutation of Plugboard. */
    private Permutation _plugBoard;

    /** All rotors in hashmap. */
    private HashMap<String, Rotor> _allRotors;

    /** Rotors array. */
    private boolean[] adRotors;

}

