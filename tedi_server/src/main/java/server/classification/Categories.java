package server.classification;

public enum Categories {
	
	//categories for users and ads
	SOFTWARE("Software"), TELECOMMUNICATIONS("Telecommunications"), HR("HR"),
	//categories for articles
	RESEARCH("Research"), AI("AI"), BIOLOGY("Biology"), GOSSIP("Gossip");
	
	private String category;
	
	private Categories(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}
	
}
