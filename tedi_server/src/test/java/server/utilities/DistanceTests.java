package server.utilities;

import org.junit.Assert;
import org.junit.Test;

public class DistanceTests {

	@Test
	public void TestSimilarStrings() {
		int d1 = Distance.Levenshtein("The queen and the prince of England photographed together in vacation! Shocking news in Hollywood, as greek singer is invited to sing in the oscar awards ceremony", "The queen and the prince of England together in vacation! ");
		int d2 = Distance.Levenshtein("Dimitris", "Panagiotis");
		System.out.println(d1);
		Assert.assertTrue(d1 < d2);
	}
	
}
