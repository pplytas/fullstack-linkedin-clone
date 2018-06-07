package server.classification;

public enum Categories {
	
	SOFTWARE("Software"), TELECOMMUNICATIONS("Telecommunications"), HR("HR");
	
	private String category;
	
	private Categories(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}
	
}
