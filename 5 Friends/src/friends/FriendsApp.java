package friends;

import java.io.*;
import java.util.*;

// import javax.sound.sampled.SourceDataLine;

// Testing client for Friends
public class FriendsApp {

    public static void main (String[] args) {

		if ( args.length < 1 ) {
			System.out.println("Expecting graph text file as input");
			return;
		}

		String filename = args[0];
		try {
			Graph g = new Graph(new Scanner(new File(filename)));

			testShortestChains(g);

			testCliques(g);

			testConnectors(g); 

			// ADD test for Friends.connectors() here
		} catch (FileNotFoundException e) {
			System.out.println(filename + " not found");
		}
    }
	private static void testShortestChains(Graph g) {
		// Update p1 and p2 to refer to people on Graph g
		// sam and sergei are from sample graph
		String p1 = "p1";
		String p2 = "p50";
		ArrayList<String> shortestChain = Friends.shortestChain(g, p1, p2);

		// Testing Friends.shortestChain
		System.out.println("Shortest chain from " + p1 + " to " + p2);
		for ( String s : shortestChain ) {
			System.out.println(s);
		}
	} 

	private static void testCliques(Graph g) {
		String school = "rutgers";
		System.out.println();
		System.out.println("Cliques for " + school);
		ArrayList<ArrayList<String>> cliques = Friends.cliques(g, school);
		for (ArrayList<String> clique : cliques) {
			for (String name : clique) {
				System.out.println(name);
			}
			System.out.println();
		}
	}

	private static void testConnectors(Graph g) {
		System.out.println();
		System.out.println("Connectors:");
		ArrayList<String> connectors = Friends.connectors(g);
		for (String connnector : connectors) {
			System.out.println(connnector);
		}
	} 

}
