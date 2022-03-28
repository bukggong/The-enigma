package enigma;


import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Jenna(dayeon)
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._notches = notches;
        _perm = perm;
    }


    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int i = 0; i < _notches.length(); i++) {
            int put = alphabet().toInt(_notches.charAt(i)) - _ring;
            int newNotch = permutation().wrap(put);
            if (newNotch == setting()) {
                return true;
            }
        }
        return false;

    }

    @Override
    void advance() {
        _setting = permutation().wrap(_setting + 1);

    }

    @Override
    String notches() {
        return _notches;
    }

    /** String of notches. */
    private String _notches;

    /** Permutation. */
    private Permutation _perm;


}
