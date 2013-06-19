package webservice;

import java.util.Calendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Simuliert zeitlichen Ablauf in Form eines Datums
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class TimeSimulator {
	private static int secondsPerDay;
	private static Calendar cal;
	
	/**
	 * @param secondsPerDay Wieviele Sekunden pro Tag vergehen
	 * @param startYear 	Startwert
	 * @param startMonth	Startwert
	 * @param startDay		Startwert
	 */
	public TimeSimulator(int secondsPerDay, int startYear, int startMonth, int startDay) {
		this.secondsPerDay = secondsPerDay;
		
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, startYear);
		cal.set(Calendar.MONTH, startMonth);
		cal.set(Calendar.DAY_OF_MONTH, startDay);
	}
	
	/**
	 * Prozess schläft um ein Tag ein, also soviele Sekunden wie in secondsperDay
	 * stehen.
	 */
	public void sleep1Day() {
		try {
			Thread.sleep(secondsPerDay * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("TimeSimulator interrupted during sleep");
		}
		cal.add(Calendar.DAY_OF_MONTH, 1);
	}
	
	/**
	 * @return Liefert das Datum als typisches XML Format
	 */
	public XMLGregorianCalendar getXMLDate() {
		System.out.println(cal.get(Calendar.YEAR) +"-"+ cal.get(Calendar.MONTH) +"-"+ cal.get(Calendar.DAY_OF_MONTH));
		try {
			XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			xmlDate.setDay(cal.get(Calendar.DAY_OF_MONTH));
			xmlDate.setMonth(cal.get(Calendar.MONTH));
			xmlDate.setYear(cal.get(Calendar.YEAR));
			return xmlDate;
			
//			return DatatypeFactory.newInstance().newXMLGregorianCalendar(
//					cal.get(Calendar.YEAR) +"-"+ cal.get(Calendar.MONTH) +"-"+ cal.get(Calendar.DAY_OF_MONTH));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void printCurrentDate() {
		System.out.println(cal.get(Calendar.YEAR) +"."+ cal.get(Calendar.MONTH) +"."+ cal.get(Calendar.DAY_OF_MONTH));
	}
}
