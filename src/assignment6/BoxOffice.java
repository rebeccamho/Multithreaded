// BoxOffice.java 
/*
 * EE422C Project 6 (Multithread) submission by
 * Rebecca Ho
 * rh29645
 * Slip days used: 0
 * Fall 2016
 */

package assignment6;

public class BoxOffice implements Runnable {

	private Theater theater;
	private Integer numClients;
	private String id;
	
	public BoxOffice(Theater theater, String id, Integer numClients) {
		this.id = id;
		this.theater = theater;
		this.numClients = numClients;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < numClients; i++) {
			int clientId = theater.getClientNum(); // assign ID to client
			
			try {
				Thread.sleep(50); // give other box offices a chance to buy tickets
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			theater.buyTicket(id, clientId); // process client buying ticket
						
		}

	}
}
