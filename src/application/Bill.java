package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Bill {
	
	private LocalDate date;
	private float bill;
	private float tipPercentage;
	private int nbPeople;
	private float tipPerPerson;
	private float totalPerPerson;	
	
	Bill(float bill, float tipPercentage, int nbPeople, LocalDate date)  throws InferiorZeroException
	{
		if(bill <= 0 || tipPercentage <= 0 || nbPeople <= 0)
		{
			throw new InferiorZeroException("Veuillez entrer des valeurs supérieur à zero");
		}
		this.bill = bill;
		this.tipPercentage = tipPercentage;
		this.nbPeople = nbPeople;
		this.date = date;
		
		this.tipPerPerson = (this.bill * (this.tipPercentage/100))/this.nbPeople;
		this.totalPerPerson = ((this.bill)/this.nbPeople) + this.tipPerPerson;
	}
	
	float getTipPerPerson(){
		return this.tipPerPerson;
	}
	
	float getTotalPerPerson(){
		return this.totalPerPerson;
	}
	
	LocalDate getDate(){
		return this.date;
	}
	
	float getTipPercentage(){
		return this.tipPercentage;
	}
	
	float getBill(){
		return this.bill;
	}
	
	int getNbPeople(){
		return this.nbPeople;
	}
	
	String getHistoryLine() {
		return this.date.toString() + ";" + Float.toString(this.bill) + ";" + Float.toString(this.tipPercentage) + ";" + Integer.toString(this.nbPeople) + "\n";
	}
}
