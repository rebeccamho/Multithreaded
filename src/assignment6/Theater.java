// Theater.java 
/*
 * EE422C Project 6 (Multithread) submission by
 * Rebecca Ho
 * rh29645
 * Slip days used: 0
 * Fall 2016
 */package assignment6;

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
			// TODO: Implement this method to return the full Seat location ex: A1
			// A is 65
			int ascii = rowNum; 
			String seatName = new String();
	
			ascii = ascii % 26;
			if(ascii == 0) { ascii = 26; }
			seatName = seatName + new Character((char) (ascii + 64)).toString();
			
			while(ascii > 0) {
				ascii = ascii / 26;
				if(ascii <= 0) { break; } 
				ascii = ascii % 26;
				//ascii = ascii + 64; // convert ascii to ASCII
				seatName = seatName + new Character((char) (ascii + 64)).toString();
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
			ticket = ticket + "|\n Box Office ID: " + boxOfficeId;
			for(int i = 0; i < 14-boxOfficeId.length(); i++) { // add variable # spaces after box office id
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
	}

	/*
	 * Calculates the best seat not yet reserved
	 *
 	 * @return the best seat or null if theater is full
 	 */
	public Seat bestAvailableSeat() {
		synchronized(this) {
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
		Ticket clientTicket = new Ticket(show, boxOfficeId, seat, client);
		synchronized(this) {
			System.out.println(clientTicket.toString());
			
			
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			log.add(clientTicket);
		}
		return clientTicket;	

	}

	/*
	 * Lists all tickets sold for this theater in order of purchase
	 *
	 * @return list of tickets sold
	 */
	public List<Ticket> getTransactionLog() {
		//TODO: Implement this method
		return log;
	}
	
	public int getClientNum() {
		synchronized(this) {
			int current = clientNum;
			clientNum++;
			return current;
		}
	}
	
	public Ticket buyTicket(String boxOfficeId, int client) {
		Ticket purchasedTicket;
		synchronized(this) { 
			Seat purchasedSeat = bestAvailableSeat();
			
			if(purchasedSeat == null) { // all seats already sold
				System.out.println("Sorry, we are sold out!");
				return null;
			}
			
			purchasedTicket = printTicket(boxOfficeId, purchasedSeat, client);
		}
		return purchasedTicket;
			
	}
}
