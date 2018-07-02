package server.utilities;

import org.junit.Test;

public class DistanceTests {

	@Test
	public void TestSimilarStrings() {
		int d1 = Distance.Levenshtein("StackOverflow", "SmackUverflow");
		int d2 = Distance.Levenshtein("Dimitris", "Panagiotis");
		System.out.println(d1 + " " + d2);
	}
	
}
