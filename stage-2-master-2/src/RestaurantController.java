import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class RestaurantController{
	
	private RestaurantModel  model;
	private RestaurantView view;
	
	public RestaurantController (RestaurantModel m, RestaurantView v){
		model = m;
		view = v;
		view.kitchenOrderListener(new restaurantController());
		view.orderBillListener(new billController());
		view.closerListener(new closerController());
	}
	
	class restaurantController  implements ActionListener
	{	
	    public void actionPerformed(ActionEvent ae) 
	    { 
	    	//Gets order population method from the GUI.
	    	String popValue = view.getPopulateMethod();
	    	model.setPopulateMethod(popValue);
	    	//Gets the length of time the kitchen will be open from the GUI.
	    	String openTime = "20";
	    	model.setKitchOpenTime(openTime);
			model.start();
			view.disableStartButton();
	    }
	 }
	
	class billController  implements ActionListener
	{	
	    public void actionPerformed(ActionEvent ae) 
	    { 
	    	javax.swing.UIManager.put("OptionPane.messageFont", new Font(Font.MONOSPACED, Font.PLAIN, 12));
	    	try{
				String numberText = view.tables.getSelectedItem().toString().substring(1);
				String discountText = "10";
				if(!discountText.equals("")){
					if(Integer.parseInt(discountText) < 0 || Integer.parseInt(discountText) > 100){
						String error = "discount  not  correct";
						JOptionPane.showMessageDialog(view, error, "Bill could not be generated", JOptionPane.ERROR_MESSAGE);
					}else
						JOptionPane.showMessageDialog(view, model.generateBill(numberText, discountText), "Bill for TABLE " + numberText, JOptionPane.PLAIN_MESSAGE);
				}else
					JOptionPane.showMessageDialog(view, model.generateBill(numberText, discountText), "Bill for TABLE " + numberText, JOptionPane.PLAIN_MESSAGE);
	    	}
			catch (NumberFormatException nfe) {
				String error = "error.\nPlease use number as discount value";
				JOptionPane.showMessageDialog(view, error, "Bill not generated", JOptionPane.ERROR_MESSAGE);
			}
	    }
	 }
	
	class closerController  implements ActionListener
	{	
	    public void actionPerformed(ActionEvent ae) 
	    { 
	    	model.writer("writeToReport.txt", model.reporter());
			System.exit(0);
	    }
	 }
}
