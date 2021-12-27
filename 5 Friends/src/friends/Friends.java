package friends;

import java.util.ArrayList;
import java.util.*;
import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 * Harshil Patel
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		if (g == null || p1 == null || p2 == null || p1.length() == 0 || p2.length() == 0) {
            return null;
        }
		
		Boolean dead = false;
		int a = 0;
		ArrayList<String> small = new ArrayList<String>();
		ArrayList<String> finSmall = new ArrayList<String>();
		boolean[] prevVisit = new boolean[g.members.length];
		ArrayList<String> pairs = new ArrayList<String>();
		Queue<Friend> queue = new Queue<Friend>();
		
		int p1index = g.map.get(p1);
		
		Boolean b = false;
		if( p1.equals(p2)){
			finSmall.add(p1);
			System.out.println(finSmall);
			return finSmall;
		}
		prevVisit[p1index] = true;
		Friend initial = new Friend( p1index, g.members[p1index].first);
		queue.enqueue(initial);
		String before = g.members[p1index].name;
		small.add(g.members[p1index].name);
		while (!queue.isEmpty()) {
			if (b) {
				break;
			}
			Friend pivots = queue.dequeue();
			if(!pairs.contains(before + " + " + g.members[pivots.fnum].name) && a == 0)
			{
				if( !(before.contains(g.members[pivots.fnum].name))){
					pairs.add(before + " + " + g.members[pivots.fnum].name);
					small.add(g.members[pivots.fnum].name);
				}
			}
			before = g.members[pivots.fnum].name;
			prevVisit[pivots.fnum] = true;
			String hwi = g.members[pivots.fnum].name;
			if(hwi.contentEquals(p2)){
			
				break;
			}
			for (Friend v = g.members[pivots.fnum].first; v != null; v = v.next) {
				if (!prevVisit[v.fnum]){
					queue.enqueue(v);
					a++;
					pairs.add(g.members[pivots.fnum].name + " + " + g.members[v.fnum].name );
					if( !small.contains(g.members[v.fnum].name)){
						small.add(g.members[v.fnum].name);
					}
					prevVisit[v.fnum] = true;
					if (g.members[v.fnum].name.equals(p2)) {
						b = true;
						break;
					}
					}
					}
					}
		if( !small.contains(p2)){
			return null;
		}
		int initialize = small.size() -1;
			for( int i  = small.size() -1; i > 0; i-- ){
				int previous = i -1;
				String recover = ((small.get(initialize) + " + " + small.get(previous)));
				String recovers = (small.get(previous) + " + " + (small.get(initialize)));
				if( pairs.contains(recover)){
					if(!finSmall.contains(small.get(initialize))){
						finSmall.add(small.get(initialize));
						initialize = previous;
						previous = initialize -1;
						if( previous == -1){
						
							finSmall.add(p1);
						}
					}
					
				}
								else if(pairs.contains(recovers)){
									if( !finSmall.contains(small.get(initialize))){
										finSmall.add(small.get(initialize));
										initialize = previous;
										previous = initialize -1;
										if( previous == -1){
											finSmall.add(p1);
										}
									}
								}
				else{
					previous = previous -1;
				}
			}
		Collections.reverse(finSmall);
		System.out.println(finSmall);
		return finSmall;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		ArrayList<ArrayList<String>> answer = new ArrayList<>();

		if (g == null || school == null || school.length() == 0) {
		   return null;
		}
		school = school.toLowerCase();
		boolean[] visited = new boolean[g.members.length];
		for (Person member : g.members) {
			if (!visited[g.map.get(member.name)] && member.student && member.school.equals(school)) {
				ArrayList<String> currClique = bfs(g, member, visited, school);
				answer.add(currClique); 
			}
		}
		return answer;
		
	}

	private static ArrayList<String> bfs(Graph g, Person p, boolean[] visit, String school) {
		ArrayList<String> clique = new ArrayList<>();
		Queue<Integer> queue = new Queue<>();
		int start = g.map.get(p.name);
		queue.enqueue(start);
		while (!queue.isEmpty()) {
			int curr = queue.dequeue();
			Person currP = g.members[curr];
			visit[curr] = true;
			clique.add(currP.name);
			Friend nextP = currP.first;
			while (nextP != null) {
				Person currFriend = g.members[nextP.fnum];
				if (!visit[nextP.fnum] && currFriend.student && currFriend.school.equals(school)) {
					visit[nextP.fnum] = true;
					queue.enqueue(nextP.fnum);
				}
				nextP = nextP.next;
			}
		}
		return clique;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		if (g == null) {
			return null;
		}
		boolean[] connectors = new boolean[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		int[] dfsnums = new int[g.members.length];
		int[] backs = new int[g.members.length];
		boolean[] returned = new boolean[g.members.length];
		for (Person p : g.members) {
			if (!visited[g.map.get(p.name)]) {
				dfsConnectors(g, connectors, visited, returned, dfsnums, backs, p, null, p);
			}
		}
		ArrayList<String> retAL = new ArrayList<String>();
		for (int i = 0; i < connectors.length; i++) {
			if (connectors[i]) {
				retAL.add(g.members[i].name);
			}
		}
		return retAL;
	}

	private static void dfsConnectors(Graph g, boolean[] connectors, boolean[] visited, boolean[] returned, int[] dfsnums, int[] backs, Person currP, Person prev, Person start) {
		int currNum = g.map.get(currP.name);
		int prevNum;
		if (prev == null) {
			dfsnums[currNum] = 1;
			backs[currNum] = 1;
		} else {
			prevNum = g.map.get(prev.name);
			dfsnums[currNum] = dfsnums[prevNum] + 1;
			backs[currNum] = dfsnums[currNum];
		}
		visited[currNum] = true;
		Friend nextP = currP.first;
		while (nextP != null) {
			Person currFriend = g.members[nextP.fnum];
			if (visited[nextP.fnum]) {
				backs[currNum] = Math.min(backs[currNum], dfsnums[nextP.fnum]);
			} else {
				dfsConnectors(g, connectors, visited, returned, dfsnums, backs, currFriend, currP, start);
				if (dfsnums[currNum] > backs[nextP.fnum]) {
					backs[currNum] = Math.min(backs[currNum], backs[nextP.fnum]);
				} else {
					if (currNum != g.map.get(start.name)) {
						connectors[currNum] = true;
					} else {
						if (returned[currNum]) {
							connectors[currNum] = true;
						}
					}
				}
				returned[currNum] = true;
			}
			nextP = nextP.next;
		}
	}
}