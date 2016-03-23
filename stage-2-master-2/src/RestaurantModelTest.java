import static org.junit.Assert.*;

import org.junit.Test;
/**
* <h1>OrderGeneratorTest</h1>
* This class consists of different JUnit cases to test several methods of Aggregator class
*
*/
public class RestaurantModelTest {

	private static final double DELTA = 1e-15;
	
	@Test
	public void testGetTableTotal() {
		RestaurantModel collections = new RestaurantModel();
		//collections.populate();
		assertEquals(0,collections.getTableTotal(-1),DELTA);
		double total1 = collections.getTableTotal(1);
		assertEquals(total1, collections.getTableTotal(1),DELTA);
		collections.updateDiscounts(1, 40);
		assertEquals(total1, collections.getTableTotal(1),DELTA);
		collections.deleteDiscount(1);
		assertEquals(total1, collections.getTableTotal(1),DELTA);
	}

	@Test
	public void testGetTableDiscountedTotal() {
		RestaurantModel collections = new RestaurantModel();
		//collections.populate();
		assertEquals(0,collections.getTableDiscountedTotal(-1),DELTA);
		double total1 = collections.getTableDiscountedTotal(1);
		assertEquals(total1, collections.getTableDiscountedTotal(1),DELTA);
		collections.updateDiscounts(1, 40);
		assertNotEquals(total1, collections.getTableDiscountedTotal(1),DELTA);
		collections.deleteDiscount(1);
		assertEquals(total1, collections.getTableDiscountedTotal(1),DELTA);
	}

}
