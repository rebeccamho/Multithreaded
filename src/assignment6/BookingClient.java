// BookingClient.java 
/*
 * EE422C Project 6 (Multithread) submission by
 * Rebecca Ho
 * rh29645
 * Slip days used: 0
 * Fall 2016
 */
package assignment6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Thread;

public class BookingClient {
  
	private Map<String, Integer> office;
	private Theater theater;
	private List<Thread> threads;
	
	/*
	 * @param office maps box office id to number of customers in line
	 * @param theater the theater where the show is playing
	 */
	public BookingClient(Map<String, Integer> office, Theater theater) {
		this.office = office;
		this.theater = theater;
		threads = new ArrayList<Thread>();
	}

  
	public static void main(String[] args) {
		Map<String, Integer> o = new HashMap<String, Integer>();
		
		o.put("BX1", 3);
		o.put("BX3", 3);
		o.put("BX2", 4); 
		o.put("BX5", 3);
		o.put("BX4", 3);	
		Theater t = new Theater(3, 5, "Ouija");
		
		BookingClient booker = new BookingClient(o, t);
		booker.simulate();
	
	}
	
	/*
	 * Starts the box office simulation by creating (and starting) threads
	 * for each box office to sell tickets for the given theater
	 *
	 * @return list of threads used in the simulation,
	 *         should have as many threads as there are box offices
	 */
	public List<Thread> simulate() {	
		for(Map.Entry s : office.entrySet()) { // create a thread for each box office
			Runnable bo = new BoxOffice(theater, (String) s.getKey(), (Integer) s.getValue());
			Thread boThread = new Thread(bo);
			threads.add(boThread);
			boThread.start();
			
		}

		return threads;
	}

}