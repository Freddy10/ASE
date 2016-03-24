
public class InvalidPositiveInteger extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidPositiveInteger(int number) {
		super("'" + number + "' is not a valid positive integer.");
	}

}
