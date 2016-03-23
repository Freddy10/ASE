import static org.junit.Assert.*;

import org.junit.Test;


public class ListOfMenuTest {

	@Test
	public void testEqualsObject() {
		//Multiple instances of MenuItem class for testing
		ListOfMenu t1 = new ListOfMenu("carrots", 2.50, "Side", true, 10);
		ListOfMenu t2 = new ListOfMenu("carrots", 2.50, "Side", true, 10);
		ListOfMenu t3 = new ListOfMenu("carrots", 2.50, "Main", true, 10);
		ListOfMenu t4 = new ListOfMenu("carrots", 2.50, "Side", false, 10);
		ListOfMenu t5 = new ListOfMenu("peas", 2.50, "Side", true, 8);
		ListOfMenu t6 = new ListOfMenu("mash", 3.50, "Side", true, 5);

		boolean result1 = t1.equals(t2);
		assertEquals(true, result1);

		boolean result2 = t1.equals(t3);
		assertEquals(true, result2);

		boolean result3 = t1.equals(t4);
		assertEquals(true, result3);

		boolean result4 = t1.equals(t5);
		assertEquals(false, result4);

		boolean result5 = t1.equals(t6);
		assertEquals(false, result5);

		boolean result6 = t5.equals(t6);
		assertEquals(false, result6);
	}

	@Test
	public void testCompareByNameTo() {
		//Multiple instances of MenuItem class for testing
		ListOfMenu t1 = new ListOfMenu("carrots", 2.50, "Side", true, 10);
		ListOfMenu t2 = new ListOfMenu("cabbage", 2.50, "Side", true, 9);
		ListOfMenu t3 = new ListOfMenu("peas", 2.50, "Side", true, 8);
		ListOfMenu t4 = new ListOfMenu("mash", 3.50, "Side", true, 5);

		int result1 = t1.compareByNameTo(t2);
		assertEquals(16, result1);

		int result2 = t2.compareByNameTo(t4);
		assertEquals(-10, result2);

		int result3 = t4.compareByNameTo(t3);
		assertEquals(-3, result3);
	}

	@Test
	public void testIsVegetarianPrint() {
		//Multiple instances of MenuItem class for testing
		ListOfMenu t1 = new ListOfMenu("carrots", 2.50, "Side", true, 10);
		ListOfMenu t2 = new ListOfMenu("pasta", 5.50, "Main", false, 8);

		String result1 = t1.isVegetarianPrint();
		assertEquals("(V)", result1);

		String result2 = t2.isVegetarianPrint();
		assertEquals("   ", result2);
	}

}
