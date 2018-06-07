package server.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Classifier<T> {
	
	private int k;
	
	public Classifier() {
		this.k = 5;
	}
	
	public Classifier(int k) {
		this.k = k;
	}
	
	public abstract int calculateDistance(T item1, T item2);
	
	//this is created to enable getCategory in unknown class structure
	//protected because we do not want it seen outside of package/extended classes
	protected abstract Categories getItemCategory(T item);
	
	public Categories classify(T nItem, List<T> dataset) {
		
		if (k > dataset.size()) {
			k = dataset.size();
		}
		
		List<Neighbor> neighbors = new ArrayList<>();
		for (T dataitem : dataset) {
			int distance = calculateDistance(nItem, dataitem);
			neighbors = updateNeighbors(neighbors, getItemCategory(dataitem), distance);
		}
		
		Map<Categories, Integer> count = countNeighborCategories(neighbors);
		return findMaxCategory(count);
		
	}
	
	public List<Neighbor> updateNeighbors(List<Neighbor> neighbors, 
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
