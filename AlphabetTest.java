package enigma;

import org.junit.Assert;
import org.junit.Test;

public class AlphabetTest {
    @Test
    public void test1() {
        Alphabet test = new Alphabet("ABCD");
        Assert.assertEquals(4, test.size());
        Assert.assertTrue(test.contains('A'));
        Assert.assertFalse(test.contains('G'));
        Assert.assertEquals(0, test.toInt('A'));
        Assert.assertEquals(1, test.toInt('B'));
        Assert.assertEquals('A', test.toChar(0));
        Assert.assertEquals('B', test.toChar(1));
    }

    @Test(expected = EnigmaException.class)
    public void test4() {
        Alphabet test = new Alphabet("  ");
        Alphabet test1 = new Alphabet("(");
        Alphabet test2 = new Alphabet(")");
    }


}
