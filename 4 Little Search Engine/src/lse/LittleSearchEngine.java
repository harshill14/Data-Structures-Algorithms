package lse;

import java.io.*;
import java.util.*;

import javax.naming.event.ObjectChangeListener;
import javax.sound.sampled.SourceDataLine;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages
 * in which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the
	 * associated value is an array list of all occurrences of the keyword in
	 * documents. The array list is maintained in DESCENDING order of frequencies.
	 */
	HashMap<String, ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String, ArrayList<Occurrence>>(1000, 2.0f);
		noiseWords = new HashSet<String>(100, 2.0f);
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword
	 * occurrences in the document. Uses the getKeyWord method to separate keywords
	 * from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an
	 *         Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String, Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/


		HashMap<String, Occurrence> hashMap = new HashMap<String, Occurrence>();
		Scanner scanner = new Scanner(new File(docFile));
		if (docFile == null) {
			throw new FileNotFoundException("document file is not found on disk");
		}
		while (scanner.hasNext()) {
			String words = getKeyword(scanner.next());
			if (words != null) {
				if (hashMap.containsKey(words)) { // contains duplicate
					hashMap.get(words).frequency++;
				} else {
					Occurrence occurrence = new Occurrence(docFile, 1);
					hashMap.put(words, occurrence);
				}
			}
		}
		return hashMap;
	}

	// following line is a placeholder to make the program compile
	// you should modify it as needed when you write your code

	/**
	 * Merges the keywords for a single document into the master keywordsIndex hash
	 * table. For each keyword, its Occurrence in the current document must be
	 * inserted in the correct place (according to descending order of frequency) in
	 * the same keyword's Occurrence list in the master hash table. This is done by
	 * calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String, Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for (String next : kws.keySet()) {
			if (!keywordsIndex.containsKey(next)) {
				ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
				occs.add(kws.get(next));
				keywordsIndex.put(next, occs);
			} else {
				keywordsIndex.get(next).add(kws.get(next));
				insertLastOccurrence(keywordsIndex.get(next));
			}
		}
	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of
	 * any trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!' NO
	 * OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be
	 * stripped So "word!!" will become "word", and "word?!?!" will also become
	 * "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		char filter = word.charAt(word.length()-1);
		if(word==null){
			return null;
		}
		word = word.toLowerCase();
		if(noiseWords.contains(word)){
			return null;
		}	
		while((word.length() != 0) && (filter =='.'|| filter ==','|| filter =='?'|| filter ==':'||filter ==';'|| filter =='!'))
		{
			word = word.substring(0, word.length()-1);
			if(word.length() != 0){
				filter = word.charAt(word.length()-1);
			}
		}	
		for(int i = 0; i < word.length(); i++)
		{
				if(!Character.isLetter(word.charAt(i)))
					return null;	
		}
		if(word.equals(""))
		{
			return null;
		}
		
	
		return word;
	}
	// following line is a placeholder to make the program compile
	// you should modify it as needed when you write your code

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in
	 * the list, based on ordering occurrences on descending frequencies. The
	 * elements 0..n-2 in the list are already in the correct order. Insertion is
	 * done by first finding the correct spot using binary search, then inserting at
	 * that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input listed by the binary
	 *         search process, null if the size of the input list is 1. This
	 *         returned array list is only used to test your code - it is not used
	 *         elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if (occs.size() == 1)
			return null;
		int initial = 0;
		int fin = occs.size() - 2;
		int end = occs.get(occs.size() - 1).frequency;
		int mid = 0;
		Occurrence arr = occs.remove(occs.size() - 1);
		ArrayList<Integer> center = new ArrayList<Integer>();
		while (fin >= initial) {                  //we will use a search method and I used binary search
			mid = ((initial + fin) / 2);
			int delta = occs.get(mid).frequency;
			center.add(mid);
			if (delta == end){
				break;
			}	
			else if (delta > end) {
				initial = mid + 1;
				if (fin <= mid){
					mid = mid + 1;
				}		
			}
			else if (delta < end) {
				fin = mid - 1;
			} 
			
		}
		center.add(mid);
		int x = center.get(center.size() - 1);
		occs.add(x, arr);
		return center;
	}

	// following line is a placeholder to make the program compile
	// you should modify it as needed when you write your code

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all
	 * keywords, each of which is associated with an array list of Occurrence
	 * objects, arranged in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile       Name of file that has a list of all the document file
	 *                       names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise
	 *                       word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input
	 *                               files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String, Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2
	 * occurs in that document. Result set is arranged in descending order of
	 * document frequencies.
	 * 
	 * Note that a matching document will only appear once in the result.
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. That is,
	 * if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all,
	 * result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in
	 *         descending order of frequencies. The result size is limited to 5
	 *         documents. If there are no matches, returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Occurrence> list1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> list2 = keywordsIndex.get(kw2);

		if (list1 == null || list2 == null) {
			return null;
		}
		ArrayList<Occurrence> totOccur = new ArrayList<Occurrence>();
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
		for (Occurrence o : list1) {
			maxHeap.add(o.frequency);
			totOccur.add(o);
		}
		for (Occurrence o : list2) {
			maxHeap.add(o.frequency);
			totOccur.add(o);
		}
		
		ArrayList<String> retArr = new ArrayList<String>();
		while (retArr.size() < 5 && maxHeap.size() != 0) {
			int currMax = maxHeap.remove();
			for (Occurrence o : totOccur) {
				if (o.frequency == currMax && !retArr.contains(o.document)) {
					retArr.add(o.document);
					totOccur.remove(o);
					break;
				}
			}
		}

		return retArr;
	}
}

// following line is a placeholder to make the program compile
// you should modify it as needed when you write your code
