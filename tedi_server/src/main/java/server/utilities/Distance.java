package server.utilities;

public class Distance {

	//returns the number of differences between the strings
	public static int Hamming(String s1, String s2) {
		
		char[] s1Array = s1.toLowerCase().toCharArray();
		char[] s2Array = s2.toLowerCase().toCharArray();
		int differences = Math.abs(s1Array.length-s2Array.length);
		int size;
		if (s1Array.length > s2Array.length) {
			size = s2Array.length;
		}
		else {
			size = s1Array.length;
		}
		for (int i=0;i<size;i++) {
			if (s1Array[i] != s2Array[i]) {
				differences++;
			}
		}
		return differences;
		
	}
	
}
