package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Harshil Patel
 *
 */
public class Trie {
	
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	
	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null); 
		TrieNode ptr = root;
	// new classification
		short start = convShort(0);
		short end = convShort(allWords[0].length() - 1);
		int num1 = 0;
		int num2 = allWords[0].length() - 1;
		Indexes sub = new Indexes(0, start, end);
		ptr.firstChild = new TrieNode(sub, null, null);
		ptr = root.firstChild;
// different distinction
		Integer number = 0;
		short shortnum = number.shortValue();
		// short conversions
		for(int i = 1; i < allWords.length; i++) {
			String word = allWords[i];
			boolean finished = false;
			ptr = root.firstChild; 
			
			while (!finished) {
				
				
				while (contains(word, ptr, allWords) ) {
					ptr = ptr.firstChild;
				}
	
				// ptr is at the first node 
				
				
				int dif = findDifference(word, ptr, allWords);
				
				
				while (dif == 0) {
					if (ptr.sibling == null) {
						sub = new Indexes(i, ptr.substr.startIndex, convShort(allWords[i].length() - 1));
						addSibling(sub, ptr);
						finished = true;
						break;
					}
					else {
						ptr = ptr.sibling;
					}
					
					dif = findDifference(word, ptr, allWords);
					if (dif > ptr.substr.endIndex) {
						break;
					}
				}
				TrieNode add = new TrieNode(sub, null, null);
		add.firstChild = ptr.firstChild;
		
				
				if (finished) {
					break;
				}
				
				if (dif > ptr.substr.endIndex) {
					continue;
				}

								
				if (dif != -1 || dif != 0) {

					
					short difShort = convShort(dif); 
					sub = new Indexes(ptr.substr.wordIndex, difShort, ptr.substr.endIndex);

					
					addChild(sub, ptr);
					ptr.substr.endIndex = convShort(dif - 1);
					ptr = ptr.firstChild;
					
					
					sub = new Indexes(i, difShort, convShort(allWords[i].length()-1) );
					addSibling(sub, ptr); 

					
					finished = true;
				}
			}
		}
		
		return root;
	}
	private static void addSibling(Indexes sub, TrieNode location) {
		TrieNode add = new TrieNode(sub, null, null);
		location.sibling = add;
		return;
	}
	
	private static void addChild(Indexes sub, TrieNode location) {
		TrieNode add = new TrieNode(sub, null, null);
		add.firstChild = location.firstChild;
		location.firstChild = add;
		return;
	}
	
	private static short convShort(int num) {
		Integer number = num;
		short shortnum = number.shortValue();
		return shortnum;
	}
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nofs in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
	
		TrieNode ptr = root.firstChild; 
		boolean finished = false;
		int dif;
		
		
		while(!finished) {
			while(contains(prefix, ptr, allWords)) {	
				dif = findDifference(prefix, ptr, allWords);
				if (dif == prefix.length()) {
					finished = true;
					break;
				}
				if (dif > ptr.substr.endIndex && ptr.firstChild == null) {
					return null;
				}
				ptr = ptr.firstChild;
			}
			if (finished) {
				break;
			}
			
			Indexes nodeSubstring = ptr.substr;
			int start = nodeSubstring.startIndex; 
			int end = nodeSubstring.endIndex + 1; 
			String check = allWords[ptr.substr.wordIndex]; 

			dif = findDifference(prefix, ptr, allWords);
			
			while (dif == 0) { 
				
				// when sibling present result will be different
				if (ptr.sibling == null) { 
					return null; 
					// no sibling will result in a different config
				}
				else {
					ptr = ptr.sibling; 
				}
				
				dif = findDifference(prefix, ptr, allWords);
			}
			
			if (dif > ptr.substr.endIndex) {
				continue;
			}
			else if (dif >= prefix.length()) {
				finished = true;
			}
			else if (dif < prefix.length()) {
				return null;
			}
			
			
		}
		
		ArrayList<TrieNode> listOut = new ArrayList<TrieNode>();
		if (ptr.firstChild == null) {
			listOut.add(ptr);
			return listOut;
		}
		listOut = makeList(ptr.firstChild, listOut);
		
		return listOut;
	}
	
	
	
	private static boolean contains(String word, TrieNode ptr, String[] allWords) {
		
		if (ptr != null) {
			int start = ptr.substr.startIndex; 
			int end = ptr.substr.endIndex+1; 
			String check = allWords[ptr.substr.wordIndex].substring(start, end); 
			
			if (word.length() < end) {
				return false;
			}
			else if (word.substring(start, end).contentEquals(check)) {
				return true;
			}
		}
		return false;
	}
	
	private static int findDifference(String word, TrieNode ptr, String[] allWords) {
		int match = -1; 
		// -1 indicates null node
		
		if (ptr != null) {
			Indexes nodeSubstring = ptr.substr;
			int start = nodeSubstring.startIndex; 
			int end = nodeSubstring.endIndex + 1; 
			String check = allWords[ptr.substr.wordIndex]; 

			
			if (word.length() < end) {
				end = word.length(); 
			}
			
			for (int i = start; i <end; i++) {
				if(word.substring(start, i+1).contentEquals(check.substring(start, i+1))) {
					match = i; 

				}
				else {
					break;
					//break incase extraneous
				}
			}
			int differenceChar = match+1;//3
			return differenceChar;
		}
		return -1; 
		//null node
	}
	
	private static ArrayList<TrieNode> makeList(TrieNode ptr, ArrayList<TrieNode> listOut) {
		if (ptr == null) {
			return null;
		}
		if (ptr.firstChild == null) {
			listOut.add(ptr);
		}
		makeList(ptr.firstChild, listOut);
		makeList(ptr.sibling, listOut);
		return listOut;
	}
	
 	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
