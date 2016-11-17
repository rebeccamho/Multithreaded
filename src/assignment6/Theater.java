// Theater.java 
/*
 * EE422C Project 6 (Multithread) submission by
 * Rebecca Ho
 * rh29645
 * Slip days used: 0
 * Fall 2016
 */

package assignment6;

import java.util.ArrayList;
import java.util.List;

public class Theater {
	
	public String show;
	private List<Ticket> log;
	private int currentSeat;
	private int maxSeat;
	private int numRows;
	private int numSeats;
	private int clientNum;
	
	private Object lockClientNum;
	private Object lockBestSeat;
	private Object lockPrintTicket;
	public Object lockBuyTicket;

	boolean soldOut;
	
	/*
	 * Represents a seat in the theater
	 * A1, A2, A3, ... B1, B2, B3 ...
	 */
	static class Seat {
		private int rowNum;
		private int seatNum;

		public Seat(int rowNum, int seatNum) {
			this.rowNum = rowNum;
			this.seatNum = seatNum;
		}

		public int getSeatNum() {
			return seatNum;
		}

		public int getRowNum() {
			return rowNum;
		}

		@Override
		public String toString() {
			int ascii = rowNum; 
			int rem;
			String seatName = new String();
	
			rem = ascii % 26;
			if(rem == 0) { // ascii equals 26
				int rem_sub = 26; 
				seatName = seatName + new Character((char) (rem_sub + 64)).toString();
			} else {
				seatName = seatName + new Character((char) (rem + 64)).toString();
			}
			
			while(ascii > 0) {
				ascii = (ascii-1) / 26; // ascii-1 bc if ascii = 26, don't want to print sth again
				if(ascii <= 0) { break; } 
				rem = ascii % 26;
				if(rem == 0) { // ascii equals 26
					int rem_sub = 26; 
					seatName = seatName + new Character((char) (rem_sub + 64)).toString();
				} else {
					seatName = seatName + new Character((char) (rem + 64)).toString();
				}
			}
			seatName = new StringBuilder(seatName).reverse().toString(); // reverse rowName
			seatName = seatName + Integer.toString(seatNum);
			return seatName;
		}
	}

	/*
	 * Represents a ticket purchased by a client
	 */
	static class Ticket {
		private String show;
		private String boxOfficeId;
		private Seat seat;
		private int client;

		public Ticket(String show, String boxOfficeId, Seat seat, int client) {
			this.show = show;
			this.boxOfficeId = boxOfficeId;
			this.seat = seat;
			this.client = client;
		}

		public Seat getSeat() {
			return seat;
		}

		public String getShow() {
			return show;
		}

		public String getBoxOfficeId() {
			return boxOfficeId;
		}

		public int getClient() {
			return client;
		}

		@Override
		public String toString() {
			String ticket = new String("-------------------------------\n| Show: " +
				show);
			for(int i = 0; i < 22-show.length(); i++) { // add variable # spaces after show
				ticket = ticket + " ";
			}
			ticket = ticket + "|\n| Box Office ID: " + boxOfficeId;
			for(int i = 0; i < 13-boxOfficeId.length(); i++) { // add variable # spaces after box office id
				ticket = ticket + " ";
			}
			ticket = ticket + "|\n| Seat: " + seat.toString();
			for(int i = 0; i < 22-seat.toString().length(); i++) { // add variable # spaces after seat
				ticket = ticket + " ";
			}
			ticket = ticket + "|\n| Client: " + Integer.toString(client);
			for(int i = 0; i < 20-Integer.toString(client).length(); i++) { // add variable # spaces after client
				ticket = ticket + " ";
			}
			ticket = ticket + "|\n-------------------------------\n";
			return ticket;
			
		}
	}

	public Theater(int numRows, int seatsPerRow, String show) {
		currentSeat = 0;
		maxSeat = numRows*seatsPerRow;
		this.show = show;
		clientNum = 1;
		this.numRows = numRows;
		this.numSeats = seatsPerRow;
		log = new ArrayList<Ticket>();
		lockClientNum = new Object();
		lockBestSeat = new Object();
		lockBuyTicket = new Object();
		lockPrintTicket = new Object();
		soldOut = false;
	}

	/*
	 * Calculates the best seat not yet reserved
	 *
 	 * @return the best seat or null if theater is full
 	 */
	public Seat bestAvailableSeat() {
		synchronized(lockBestSeat) {
			if(currentSeat == maxSeat) { // all seats are reserved
				return null;
			}
			int rowNum = (currentSeat / numSeats) + 1;
			int seatNum = (currentSeat % numSeats) + 1;
			Seat best = new Seat(rowNum, seatNum);
			currentSeat++;
			return best;
		}
	}

	/*
	 * Prints a ticket for the client after they reserve a seat
	 * Also prints the ticket to the console
	 *
	 * @param seat a particular seat in the theater
	 * @return a ticket or null if a box office failed to reserve the seat
	 */
	public Ticket printTicket(String boxOfficeId, Seat seat, int client) {
		synchronized(lockPrintTicket) {
			if(soldOut) { return null; } // already printed sold out
			if(seat == null) { // all seats already sold
				System.out.println("Sorry, we are sold out!");
				soldOut = true;
				return null;
			}
			
			Ticket clientTicket = new Ticket(show, boxOfficeId, seat, client);
			System.out.println(clientTicket.toString()); // print ticket to console	
			
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				
			log.add(clientTicket); // add ticket to list of all tickets sold
	
			return clientTicket;	
		}

	}

	/*
	 * Lists all tickets sold for this theater in order of purchase
	 *
	 * @return list of tickets sold
	 */
	public List<Ticket> getTransactionLog() {
		return log;
	}
	
	/*
	 * Gives the client the next available ID.
	 *
	 * @return current available ID number
	 */
	public int getClientNum() {
		synchronized(lockClientNum) {
			int current = clientNum;
			clientNum++;
			return current;
		}
	}
	
}
