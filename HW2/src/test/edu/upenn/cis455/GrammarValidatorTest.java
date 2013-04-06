package test.edu.upenn.cis455;

import edu.upenn.cis455.validator.GrammarValidator;
import junit.framework.TestCase;

public class GrammarValidatorTest extends TestCase {

	GrammarValidator validator;
	protected void setUp() throws Exception {
		validator = new GrammarValidator();
	}

	public void testIsValidXpathSet1() {
		assertTrue(validator.isValidXpath("/a/b"));
		assertTrue(validator.isValidXpath("/a"));
		assertTrue(validator.isValidXpath(" /a"));
		assertTrue(validator.isValidXpath("/  a"));
		assertFalse(validator.isValidXpath("//a"));
	}


	public void testIsValidXpathSet2(){
		assertTrue(validator.isValidXpath("/blah[@name=\"gokul\"]"));
		assertTrue(validator.isValidXpath("/a[text() = \"yesno\"]"));
		assertTrue(validator.isValidXpath("/a  [text() = \"yesno\"]"));
		assertFalse(validator.isValidXpath("/a [text()"));
		assertFalse(validator.isValidXpath("/a[text( ) = \"something\""));
		assertFalse(validator.isValidXpath("/a [text( = \"something\"]"));
	}

	public void testIsValidXpathSet3(){
		assertTrue(validator.isValidXpath("/foo-name/bar/xyz"));
		assertTrue(validator.isValidXpath("/foo/bar[x/y]"));
		assertTrue(validator.isValidXpath("/foo/bar[text() = \"gokul\"]"));
		assertTrue(validator.isValidXpath("/foo/bar[contains(text(), \"somesubstring\")]"));
		assertTrue(validator.isValidXpath("/blah[  @name=\"gokul\"]"));
		assertTrue(validator.isValidXpath("/blah[contains(text() , \"bybye\")]"));
		assertFalse(validator.isValidXpath("/a/b[ contains  (), \"what is wrong\"]"));
		assertFalse(validator.isValidXpath("/a/b[contains(text(), \"closebrace\"]"));
	}

	public void testIsValidXpathSet4(){
		assertTrue(validator.isValidXpath("/a/b[text() = \"yes\"]"));
		assertTrue(validator.isValidXpath("/x/y[@name = \"b\"][text() = \"z\"]"));
		assertTrue(validator.isValidXpath("/this/that[foo[text() = \"something\"]][bar]"));
		assertTrue(validator.isValidXpath("/d/e/f[foo[text() = \"\"]][bar]"));
		assertTrue(validator.isValidXpath("/a/ b/c[text() = \"\"][bar[foo/bar[text()=" +
				"\"a\"][contains(text(),\"a\")]]]"));
	}

	public void testIsValidXpathSet5(){
		assertFalse(validator.isValidXpath("/a/b[]][text() = \"yes\"[]"));
		assertFalse(validator.isValidXpath("/a/b/c[text() = \"a]"));
		assertFalse(validator.isValidXpath("/a/b/c[contains(text(),,\"s\")]"));
		assertFalse(validator.isValidXpath("/a/b/c[@abcd == \"s\"]"));
		assertFalse(validator.isValidXpath("/a/b/c[@abcd = \"s\"][]"));
		assertFalse(validator.isValidXpath("/a/b/c[@abcd = \"s\"]/b/"));
	}


	public void testIsValidXpathSet6(){
		assertTrue(validator.isValidXpath("/ab/cd[foo[bar]][fool[bare]]"));
		assertTrue(validator.isValidXpath("/ab/cd[foo][bar][bb]"));
		assertTrue(validator.isValidXpath("/ab/cd[foo[bar[bb]]]"));
		assertTrue(validator.isValidXpath("/ab/cd[a][b[c/d]]"));
		assertFalse(validator.isValidXpath("/"));
	}

	public void testIsValidXpathSet7(){
		assertTrue(validator.isValidXpath("/abcd/xyz[foo[bar[@name = \"s\"]][text() = \"ss\"]]"));
		assertTrue(validator.isValidXpath("/abcd/xyz[foo[bar" +
				"[@name = \"s\"]][text() = \"ss\"]][contains(text(), \"he he\")]"));
	}

	public void testIsValidXpathSet8(){
		assertFalse(validator.isValidXpath("///"));
		assertFalse(validator.isValidXpath("/a/b/c/[@text = \"sss\"]"));
		assertFalse(validator.isValidXpath("/a/b/c[@txt = \"sss\"]]["));
		assertFalse(validator.isValidXpath("/bb/cc/dd[@text() = " +
				"\"sss\"]aa"));
	}

	public void testIsValidXpathSet9(){
		assertFalse(validator.isValidXpath("/a[@a:b = \"c\"]"));
		assertFalse(validator.isValidXpath("/a[contains(text(),\"\")]"));
	}

	public void testIsAxis() {
		assertTrue(validator.isAxis("/  "));
		assertFalse(validator.isAxis(" "));
		assertFalse(validator.isAxis("  /"));
	}

	public void testIsValidStep() {
		assertTrue(validator.isValidTestExpression("blah[@name=\"gokul\"]"));
		assertTrue(validator.isValidTestExpression("this/that[foo[text() = \"something\"]][bar]"));
		assertTrue(validator.isValidTestExpression("d/e/f[foo[text() = \"\"]][bar]"));
		assertTrue(validator.isValidTestExpression("a/ b/c[text() = \"\"][bar[foo/bar[text()=" +
				"\"a\"][contains(text(),\"b\")]]]"));
	}


	public void testIsValidTestExpression() {
		assertTrue(validator.isValidTestExpression("foo-name/bar/xyz"));
		assertFalse(validator.isValidTestExpression("foo/bar/"));
		assertTrue(validator.isValidTestExpression("foo/bar[text() = \"gokul\"]"));
		assertTrue(validator.isValidTestExpression("foo/bar[contains(text(), \"somesubstring\")]"));
		assertTrue(validator.isValidTestExpression("a"));
		assertTrue(validator.isValidTestExpression("a[@att = \"blah\"]"));
		assertTrue(validator.isValidTestExpression("a[text() = \"gokul\"]"));
		assertTrue(validator.isValidTestExpression("a[contains(text(), \"somesubstring\")]"));
		assertTrue(validator.isValidTestExpression("a/b[text() = \"gokul\"]/c"));


	}

	public void testContainsTextForm() {
		assertTrue(validator.containsTextForm("contains(text(),\"gokul\")"));
		assertTrue(validator.containsTextForm("contains (text(),\"gokul\")"));
		assertTrue(validator.containsTextForm("contains(   text(  ) ,  \"gokul\" )"));
	}

	public void testTextEqualsForm() {
		assertTrue(validator.textEqualsForm("text() = \"gokul\""));
		assertFalse(validator.textEqualsForm("text()"));
		assertFalse(validator.textEqualsForm("text() = "));
		assertFalse(validator.textEqualsForm("text() = \""));
		assertTrue(validator.textEqualsForm("text()=\"\""));
	}

	public void testAttributeTextForm() {
		assertTrue(validator.attributeTextForm("@aa =  \"bbb\""));
		assertTrue(validator.attributeTextForm("@aa =  \"gokul\""));
		assertFalse(validator.attributeTextForm("@aa ==  \"gokul\""));
		assertFalse(validator.attributeTextForm("@    =\"aaa\""));
	}

}
