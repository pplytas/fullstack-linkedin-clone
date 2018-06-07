package server.classification;

import java.util.Comparator;

public class Neighbor {
	
	private int distance;
	private Categories category;
	
	public Neighbor(int distance, Categories category) {
		this.distance = distance;
		this.category = category;
	}

	public int getDistance() {
		return distance;
	}

	public Categories getCategory() {
		return category;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setCategory(Categories category) {
		this.category = category;
	}
	
	public static class DistanceComp implements Comparator<Neighbor> {
		
		public int compare(Neighbor a, Neighbor b) {
			return a.distance - b.distance;
		}
		
	}

}
