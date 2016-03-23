import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/*
 * This JUnit Suite joins all the test cases created
 */

@RunWith(Suite.class)
@SuiteClasses({ RestaurantModelTest.class, listofMenuTest.class, tableOrdersTest.class, listOfOrdersTest.class })
public class AllTests {

}
