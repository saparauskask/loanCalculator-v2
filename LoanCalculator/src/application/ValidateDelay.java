package application;

import java.time.LocalDate;
import java.time.Period;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ValidateDelay extends Validate{
	
	public ValidateDelay(TextField numberAmount, TextField numberInterest, DatePicker dateStart, DatePicker dateEnd) {
		super(numberAmount, numberInterest, dateStart, dateEnd);
		// TODO Auto-generated constructor stub
	}

	private DatePicker delayStart; // from FXML
	private TextField delayDuration;
	private TextField delayInterest;
	
	private Label delayStartLabel; // labels
	private Label delayDurationLabel;
	private Label delayInterestLabel;
	
	private int delayDurationNumber; // converted from FXML
	private double delayInterestNumber;
	private int delayStartMonth;
	private LocalDate delayStartDate;
	private LocalDate loanStartDate;
	private LocalDate loanEndDate;
	
	private int timeResult1 = 0;
	private int timeResult2 = 0;
	
	boolean checkStart;
	boolean checkDuration;
	boolean checkInterest;
	boolean checkDelay;

	
	public void setDelayInfo(DatePicker delayStart, TextField delayDuration, TextField delayInterest, DatePicker dateStart, DatePicker dateEnd) {
		this.delayStart = delayStart;
		this.delayDuration = delayDuration;
		this.delayInterest = delayInterest;
		this.dateStart = dateStart;
			if (dateStart.getValue() != null) {
				loanStartDate = dateStart.getValue();
			} else {
				//System.out.println("Beda cia: dateStart");
			}
			if (dateEnd.getValue() != null) {
				loanEndDate = dateEnd.getValue();
			} else {
				//System.out.println("Beda cia: dateEnd");
			}
		setVariables();
	}
	
	public void setDelayLabels(Label delayStartLabel, Label delayDurationLabel, Label delayInterestLabel) {
		this.delayStartLabel = delayStartLabel;
		this.delayDurationLabel = delayDurationLabel;
		this.delayInterestLabel = delayInterestLabel;
	}
	
	private void setVariables() { // OVERRIDE?
		try {
			delayDurationNumber = Integer.parseInt(delayDuration.getText());
		} catch (NumberFormatException e) {
			//handle the exception
		}
		
		try {
			delayInterestNumber = Double.parseDouble(delayInterest.getText());
		} catch(NumberFormatException e) {
			//handle the exception
		}
		
		if (delayStart.getValue() != null) {
			delayStartDate = delayStart.getValue();
			//dateStartLabel.setText("");
		}
		if (dateStart.getValue() != null) {
			loanStartDate = dateStart.getValue();
		}
		if (dateEnd.getValue() != null) {
			loanEndDate = dateEnd.getValue();
		}
	}
	
	public void setDelayFormula(Formula formula) {
		formula.setDelayInfo(delayDurationNumber, delayInterestNumber, delayStartMonth);
	}
	
	public int getDelayDuration() {
		return delayDurationNumber;
	}
	
	public LocalDate getDelayStart() {
		return delayStartDate;
	}
	
	public int getDelayStartMonth() {
		return delayStartMonth;
	}
	
	public boolean validateDelayInfo() {
				if (delayDurationNumber > 0 && delayDurationNumber < 13) {
					delayDurationLabel.setText("");
					checkDuration = true;
				} else {
					delayDurationLabel.setText("Klaida!");
				}
			
				if (delayInterestNumber > 0 && delayInterestNumber < 100) {
					checkInterest = true;
					delayInterestLabel.setText("");
				} else {
					delayInterestLabel.setText("Klaida!");
				}
				if (loanStartDate != null && loanEndDate != null && delayStartDate != null) {
					timeResult1 = delayStartDate.compareTo(loanStartDate);
					timeResult2 = loanEndDate.compareTo(delayStartDate);
				} else {
					timeResult1 = -1;
					timeResult2 = -1;
				}
				if (timeResult1 >= 0 && timeResult2 >=0) {
					checkStart = true;
					//calculate delayStartMonth
					//Period period = Period.between(loanStartDate.withDayOfMonth(1), delayStartDate.withDayOfMonth(1));
					Period period = Period.between(loanStartDate, delayStartDate);
					delayStartMonth = period.getYears() * 12 + period.getMonths();
					delayStartLabel.setText("");
					++delayStartMonth;
				} else {
					delayStartLabel.setText("Klaida!");
				}
				
				if(checkDuration && checkInterest && checkStart) {
					checkDelay = true;
				} else {
					checkDelay = false;
				}
				return checkDelay;
	}
	
	public void setDates(Payment[] test, int duration) {
		for (int i = 0; i < duration; ++i) {
			test[i].setDate(loanStartDate.plusMonths(i+1).withDayOfMonth(1));
		}
}
	

}
