package application;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IndexController implements Initializable {
	
	@FXML
	private TextField dateField;
	
	@FXML
	private TextField billField;
	
	@FXML
	private TextField tipField;
	
	@FXML
	private TextField peopleField;
	
	@FXML
	private Text resTip;
	
	@FXML
	private Text resTotal;
	
	@FXML
	private Text errorMessage;
	
	@FXML
	private Button btnCalculate;
	
	@FXML
	private Button btnFill;
	
	private ArrayList<Bill> historyList;
	
	private HashMap<String, Bill> history = new HashMap<>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.history = this.getHistory();
		System.out.println(this.history);
		
	}
	
	public void fillValueHistory(MouseEvent e)
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dateBill = LocalDate.parse(dateField.getText(), formatter);
		Bill bill = this.history.get(dateBill.toString());
		if(bill != null)
		{
			billField.setText(Float.toString(bill.getBill()));
			peopleField.setText(Integer.toString(bill.getNbPeople()));
			tipField.setText(Float.toString(bill.getTipPercentage()));			
			errorMessage.setText("");
		}
		else
		{
			errorMessage.setText("Pas d'entrée pour cette date");
		}
	}
	
	public void calculate(MouseEvent e) throws IOException
	{
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dateBill = LocalDate.parse(dateField.getText(), formatter);
			
			float bill = Float.parseFloat(billField.getText());
			int nbPeople = Integer.parseInt(peopleField.getText());
			float tipPercentage = Float.parseFloat(tipField.getText());
			
			Bill totalBill = new Bill(bill, tipPercentage, nbPeople, dateBill);
			
			resTip.setText(Float.toString(totalBill.getTipPerPerson()));
			resTotal.setText(Float.toString(totalBill.getTotalPerPerson()));			
			
			errorMessage.setText("");
			
			this.writeHistory(totalBill);
			this.history = this.getHistory();
		}
		catch (NumberFormatException error){
			errorMessage.setText("Entrée incorrecte, veuillez saisir des nombres");
			resTip.setText("error");
			resTotal.setText("error");	
		}
		catch (InferiorZeroException error){
			errorMessage.setText(error.getMessage());
			resTip.setText("error");
			resTotal.setText("error");	
		}
		catch (DateTimeParseException error){
			errorMessage.setText(error.getMessage());
			resTip.setText("error");
			resTotal.setText("error");	
		}
		
	}
	
	private void writeHistory(Bill totalBill)
	{
		String historyLine = totalBill.getHistoryLine();
		
		boolean isPresent = this.checkExistingDate(totalBill.getDate());
		
		if(isPresent)
		{	
			this.history.remove(totalBill.getDate().toString());				
			this.history.put(totalBill.getDate().toString(), totalBill);
			
			System.out.println(this.history);
			
			String result = "";
			 for (Map.Entry res : this.history.entrySet()) {
				 result = result + ((Bill) res.getValue()).getHistoryLine();
			 }
			 this.writeHistoryLine(result, isPresent);
		}
		else
		{
			this.writeHistoryLine(historyLine, isPresent);
		}			
	}
	
	public HashMap<String, Bill> getHistory()
	{
		try {
			HashMap<String, Bill> resHistory = new HashMap<>();
			FileReader fileReader = new FileReader("history.txt");
			BufferedReader reader = new BufferedReader(fileReader);
			while (reader.ready()) {
				String[] line = reader.readLine().split("\n");
				for (String s : line) {
					
					String[] values = s.split(";");
					
					float bill = Float.parseFloat(values[1].replace(";", ""));
					float tipPercentage = Float.parseFloat(values[2].replace(";", ""));
					int nbPeople = Integer.parseInt(values[3].replace(";", ""));
					LocalDate date = LocalDate.parse(values[0].replace(";", ""));
					
					Bill billHistory = new Bill(bill, tipPercentage, nbPeople, date);
					
					resHistory.put(date.toString(), billHistory);
				}
			}
			reader.close();
			fileReader.close();
			
			return resHistory;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InferiorZeroException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	private boolean checkExistingDate(LocalDate date)
	{
		String dateString = date.toString();
		
		if(this.history.get(dateString) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void writeHistoryLine(String historyLine, boolean isPresent)
	{		
		try {
			File history = new File("history.txt");
			FileOutputStream flux = new FileOutputStream(history, !isPresent);
			
			for (int i = 0; i < historyLine.length(); i++) {
				flux.write(historyLine.charAt(i));
			}				
			flux.close();			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
