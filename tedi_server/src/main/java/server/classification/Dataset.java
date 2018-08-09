package server.classification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Dataset {
	
	public static final List<String> softwareDataset =
			Arrays.asList("software", "junior", "senior", "developer", "programmer", "frontend", "backend", "development", "system", "administrator", "deploy", "version control", "passionate");
	
	public static final List<String> telecommunicationsDataset =
			Arrays.asList("cisco", "network", "wireless", "communication", "mobile", "it", "maintenance", "systems", "telecommunication");
	
	public static final List<String> hrDataset =
			Arrays.asList("proactive", "adaptable", "openminded", "friendly", "positive", "recruit", "talk", "communicate", "handle");

	public static final List<String> researchDataset =
			Arrays.asList("research", "paper", "reveals", "examines", "unknown", "information", "generation", "researchers", "scientists", "data", "people", "discovery", "previously", "science", "cannabis", "opioids");
	
	public static final List<String> aiDataset =
			Arrays.asList("neural", "networks", "gate", "future", "algorithms", "deep", "learning", "big", "data", "AI", "new", "google", "microsoft", "games", "intelligence", "predict", "improve");
	
	public static final List<String> biologyDataset =
			Arrays.asList("mitochondria", "cell", "human", "animals", "lab", "tissue", "testing", "experiment", "kind", "cancer", "virus", "remedee", "medicine", "biologists", "diseases", "opioids");

	public static final List<String> gossipDataset =
			Arrays.asList("queen", "event", "king", "prince", "princess", "party", "orgy", "prison", "vacation", "trip", "death", "shocking", "news", "greek", "singer", "actor", "actress", "photograph", "island", "oscar", "ceremony", "awards", "mikonos");
	
	public static List<String> getRandomWords(List<String> dataset, int limit) {
		List<String> randomList = new ArrayList<>();
		while (randomList.size()<limit) {
			randomList.add(dataset.get((int)Math.floor(Math.random()*dataset.size())));
		}
		return randomList;
	}
}
