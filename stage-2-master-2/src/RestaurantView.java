import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RestaurantView extends JFrame implements Observer {

	private JTextArea kitchen;
	private JTextArea hatch;
	private JTextArea[] tableRow;
	protected JTextField discountField;
	private JScrollPane scrollBar;
	private JButton getReceipt, startProgram, closeProgram;
	private JComboBox<String> dishes;
	private JComboBox<String> kitchOpen;
	protected JComboBox<String> tables;

	private RestaurantModel model;
	private int numOfTables;

	public RestaurantView(RestaurantModel model) {

		setTitle("Restaurant Ordering system");

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.model = model;
		model.addObserver(this);
		numOfTables = 6;

		setSize(100, 500);
		setLocation(10, 20);
		
		//Panels 
		JPanel centrePanel = new JPanel();
		JPanel tablePanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		//view content pane
		Container contentPane = getContentPane();
		contentPane.add(centrePanel, BorderLayout.CENTER);
		contentPane.add(northPanel, BorderLayout.NORTH);
		contentPane.add(tablePanel, BorderLayout.SOUTH);
		//contentPane.add(southPanel, BorderLayout.SOUTH);
		tablePanel.add(customTabDisplay(), BorderLayout.EAST);
		
		//start bit
		kitchen = new JTextArea(10, 30);
		kitchen.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.LIGHT_GRAY));

		hatch = new JTextArea(10, 30);
		hatch.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.LIGHT_GRAY));

		kitchen.setEditable(false);
		kitchen.setText("KITCHEN ORDERS LIST");

		hatch.setEditable(false);
		hatch.setText("HATCH ORDERS LIST");
		centrePanel.add(kitchen);
		centrePanel.add(hatch);

		
		scrollBar = new JScrollPane();
		centrePanel.add(scrollBar, BorderLayout.CENTER);
		startProgram = new JButton("Start Simulation");
		northPanel.add(startProgram);

		kitchOpen = new JComboBox<String>();
		dishes = new JComboBox<String>();
		dishes.addItem("from a textfile");
		dishes.addItem("at random");
		
		northPanel.add(new JLabel("Table:"));
		
		tables = new JComboBox<String>();
		for (int i = 1; i < (numOfTables + 1); i++) {
			tables.addItem("#" + i);
		}

		northPanel.add(tables, BorderLayout.BEFORE_FIRST_LINE);		
		discountField = new JTextField(3);
		getReceipt = new JButton("Bill");
		getReceipt.setEnabled(false);
		northPanel.add(getReceipt);
		//getReport = new JButton("Print Report");
		//getReport.setEnabled(false); 
		//northPanel.add(getReport);
		closeProgram = new JButton("Close");
		northPanel.add(closeProgram);
		
		pack();
		setVisible(true);
	}

	private JPanel customTabDisplay() {

		JPanel customTablePanel = new JPanel(new GridLayout(1, 6));
		tableRow = new JTextArea[6];

		for (int i = 0; i < 6; i++) {
			tableRow[i] = new JTextArea(10, 15);
			tableRow[i].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.LIGHT_GRAY));
			tableRow[i].setLineWrap(true);
			customTablePanel.add(tableRow[i]);
		}
		return customTablePanel;

	}

	public void kitchenOrderListener(ActionListener a) {
		startProgram.addActionListener(a);
	}
	


	public void orderBillListener(ActionListener a) {
		getReceipt.addActionListener(a);
	}

	public void closerListener(ActionListener a) {
		closeProgram.addActionListener(a);
	}

	public String getPopulateMethod() {
		String value = dishes.getSelectedItem().toString();
		return value;
	}

	public String getKitchOpenTime() {
		String value = kitchOpen.getSelectedItem().toString();
		return value;
	}

	public void enableGetButton() {
		getReceipt.setEnabled(true);
	}

	public void disableStartButton() {
		startProgram.setEnabled(true);
	}

	public synchronized void update(Observable o, Object args) {
		this.kitchen.setText(model.getKitchenReport());
		this.hatch.setText(model.getHatchReport());

		for (int i = 0; i < model.getListOfTables().size(); i++) {
			String report = model.getOrderList(i);
			this.tableRow[i].setText(report);
		}

		if (model.getThreadFinished()) {
			getReceipt.setEnabled(true);
		}
	}
}
