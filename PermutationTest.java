package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Jenna(dayeon) Jang
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testInvertChar() {
        String cycles = "(bacd) (jeny)";
        Alphabet alpha2 = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Permutation test = new Permutation(cycles, alpha2);
        assertEquals('b', test.invert('a'));
        assertEquals('c', test.invert('d'));
        assertEquals('d', test.invert('b'));
        assertEquals('a', test.invert('c'));
        assertEquals('e', test.invert('n'));
        assertEquals('y', test.invert('j'));
        assertEquals(26, test.size());


    }

    @Test
    public void testInvertInt() {
        String cycles = "(bacd) (euntk)";
        Alphabet alpha3 = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Permutation test = new Permutation(cycles, alpha3);
        assertEquals(1, test.invert(0));
        assertEquals(2, test.invert(3));
        assertEquals(3, test.invert(1));
        assertEquals(0, test.invert(2));
        assertEquals(4, test.invert(20));
        assertEquals(19, test.invert(10));
    }

    @Test
    public void testPermuteChar() {
        String cycles = "(bacd) (ulqz)";
        Alphabet alpha4 = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Permutation test = new Permutation(cycles, alpha4);
        assertEquals('a', test.permute('b'));
        assertEquals('b', test.permute('d'));
        assertEquals('c', test.permute('a'));
        assertEquals('d', test.permute('c'));
        assertEquals('u', test.permute('z'));
        assertEquals('q', test.permute('l'));
        assertEquals(26, test.size());
    }

    @Test
    public void testPermuteInt() {
        String cycles = "(bacd) (ulqz)";
        Alphabet alpha5 = new Alphabet("abcdefghijklmnopqrstuvwxyz");
        Permutation test = new Permutation(cycles, alpha5);
        assertEquals(0, test.permute(1));
        assertEquals(1, test.permute(3));
        assertEquals(2, test.permute(0));
        assertEquals(3, test.permute(2));
        assertEquals(20, test.permute(25));
        assertEquals(16, test.permute(11));
        assertEquals(26, test.size());
    }

}
