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
		//System.out.println("num clients is " + numClients);
		for(int i = 0; i < numClients; i++) {
			int clientId = theater.getClientNum();
			theater.buyTicket(id, clientId);
			
		}
	}

}
