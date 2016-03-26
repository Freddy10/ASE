import java.text.DecimalFormat;
import java.text.NumberFormat;


public class OrderID {

	private static OrderID instance;

	private String lastID;

	private OrderID(){
		lastID = "00";
	}

	public static synchronized OrderID getInstance(){
		if(instance==null){
			instance = new OrderID();
		}
		return instance;
	}

	public synchronized String getNext(){
		NumberFormat myFormat = new DecimalFormat("00");
		int i = Integer.parseInt(lastID.substring(1)) + 1;
		lastID = "O" + myFormat.format(i);
		return lastID;
	}

}