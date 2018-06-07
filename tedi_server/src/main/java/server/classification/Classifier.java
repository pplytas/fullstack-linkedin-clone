package server.classification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classifier {
	
	private final int k = 5;
	
	public List<Neighbor> UpdateNeighbors(List<Neighbor> neighbors, 
			Categories category, int distance) {
		
		if (neighbors.size() < k) {	
			neighbors.add(new Neighbor(distance, category));
			neighbors.sort(new Neighbor.DistanceComp());
		}
		else {
			if (neighbors.get(neighbors.size()-1).getDistance() > distance) {
				neighbors.set(neighbors.size()-1, new Neighbor(distance, category));
				neighbors.sort(new Neighbor.DistanceComp());
			}
		}
		
		return neighbors;
	}

	public Map<Categories, Integer> countNeighborCategories(List<Neighbor> neighbors) {
		
		Map<Categories, Integer> count = new HashMap<>();
		for (Neighbor n : neighbors) {
			if (count.containsKey(n.getCategory())) {
				count.put( n.getCategory(), count.get(n.getCategory())+1 );
			}
			else {
				count.put( n.getCategory(), 1);
			}	
		}
		return count;
		
	}
	
	public Categories findMaxCategory(Map<Categories, Integer> count) {
		
		int max = 0;
		Categories maxCat = null; //realistically it will never stay null
		for (Map.Entry<Categories, Integer> entry : count.entrySet()) {
			if (entry.getValue()>max) {
				max = entry.getValue();
				maxCat = entry.getKey();
			}
		}
		return maxCat;
		
	}
	
}
