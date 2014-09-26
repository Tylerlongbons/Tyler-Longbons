import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class WebPages {
	static ArrayList<Term> termIndex;
	public static int mergeSortCount;
	//P1 p1 = new P1();

	public WebPages(){
		termIndex = new ArrayList<Term>();
	}
	public void addPage(String filename){

		ReadDoc docReader = new ReadDoc(filename);

		int counter = 0;
		int wordsSize = docReader.words.size();
		while(counter < wordsSize){
			String word = docReader.words.get(counter);
			Term newTerm = new Term(word);
			newTerm.incFrequency(filename);
			termIndex.add(newTerm);
			counter++;
		}
	}
	public static void printTerms(){
		System.out.println("WORDS");
		for(Term t: termIndex){
			System.out.println(t.name);
		}
	}
	//Prichard and Carrano pgs (529-531)
	private static void mergeSort(ArrayList<Term> theArray, int first, int last, sortType type){

		if(first < last){
			int mid = (first + last) / 2;
			mergeSort(theArray,first, mid, type);
			mergeSort(theArray, mid + 1, last, type);

			merge(theArray, first, mid, last, type);
		}


	}
	//Prichard and Carrano pgs (529-531)
	private static void merge(ArrayList<Term> theArray,int first, int mid, int last, sortType type){
		int first1 = first;
		int last1 = mid;
		int first2 = mid + 1;
		int last2 = last;
		int index = first1;
		int counter = 0; //this needs to be in output
		ArrayList<Term> tempArray = new ArrayList<>();

		while((first <= last1) && (first2 <= last2)){
			if(type == sortType.totalFrequency){
				
					if(theArray.get(first1).compareTo(theArray.get(first2)) <= 0){ 
						tempArray.add(index, theArray.get(first1));
						first1++;
						counter++;  //this needs to be in output
					}else{
						tempArray.add(index, theArray.get(first2));
						first2++;
					}
					index++;
				}else{
					
					if(theArray.get(first1).compareWords(theArray.get(first2)) <= 0){ 
						tempArray.add(index, theArray.get(first1));
						first1++;
						counter++;  //this needs to be in output
					}else{
						tempArray.add(index, theArray.get(first2));
						first2++;
					}
					index++;
				}
		}

		while(first1 <= last1){
			tempArray.set(index, theArray.get(first));
			first1++;
			index++;
		}
		while(first1 <= last2){
			tempArray.set(index, theArray.get(first2));
			first2++;
			index++;
		}
		for(index = first; index <= last; ++index){
			theArray.set(index, tempArray.get(index));
		}
		mergeSortCount += counter;
	} //end merge


	public void pruneStopWords(int n){
		//TODO

		mergeSort(termIndex, 0, termIndex.size(),sortType.totalFrequency);
		for(int i = 0; i < n; i++){
			termIndex.remove(i);
		}
		mergeSort(termIndex, 0, termIndex.size(),sortType.words);
	}
	//	public String[] whichPages(String word){
	//		ArrayList<String> s = new ArrayList<String>();
	//		for(Term t: termIndex){
	//			if(t.name.equals(word)){
	//				for(Occurrence o: t.occurrences){
	//					s.add(o.docName);
	//				}
	//				return (String[]) s.toArray();
	//			}
	//		}
	//		return null;
	//	}

	public class ReadDoc {

		public ArrayList<String> words = new ArrayList<String>();
		private ArrayList<String> tag = new ArrayList<String>();
		private boolean inTag =false;

		public ReadDoc(String filename){
			readFile(filename);
		}

		public void readFile(String filename) {
			String w = "";
			try {
				Scanner scanner = new Scanner(new File(filename));
				while (scanner.hasNext()) {
					w = scanner.next().toLowerCase();
					if(w.contains("-->")){
						@SuppressWarnings("unused")
						int x=2;
					}
					if (w.contains("<") || w.contains(">")) {// checks if tag
						tagWord(w);
						for (String s : tag) {
							s = punct(s);
							if (binarySearch(s, 0, words.size() - 1)>-1
									&& s.length() > 0) {
								words.add(binarySearch(s,0,words.size()-1),s);
							}
						}
						tag = new ArrayList<String>();
					} else if(!inTag){
						w = punct(w);
						if (binarySearch(w, 0, words.size() - 1)>-1 && w.length() > 0) {
							words.add(binarySearch(w,0,words.size()-1),w);
						}
					}
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				// TODO How do they want errors
				e.printStackTrace();
			}
		}

		private String punct(String w) {
			char[] s = w.toCharArray();
			if(s.length<=0){//handle empty case later
				return "";
			}
			if (!Character.isAlphabetic(s[0]) && !Character.isDigit(s[0])) {
				return punct(w.substring(1));
			} else if (!Character.isAlphabetic(s[s.length - 1])
					&& !Character.isDigit(s[s.length - 1])) {
				return punct(w.substring(0, w.length() - 1));
			}
			return w;
		}
		private void tagWord(String w) {
			if (w.contains("<") && !w.contains(">")) {// ###<####
				tag.add(w.substring(0, w.indexOf('<')));
				inTag =true;
			}
			else if (w.contains(">") && !w.contains("<")) {// ###>###
				tag.add(w.substring(w.indexOf('>')+1));
				inTag=false;
			}
			else if (!w.contains(">") && !w.contains("<")) {//
				tag.add(w);
			} else {// ##<###>--
				if (w.indexOf('<') < w.indexOf('>')) {// ##<###>--
					tag.add(w.substring(0, w.indexOf('<')));// ##
				}
				tagWord(w.substring(w.indexOf('>') + 1));// --
			}
		}
		private int binarySearch(String s, int first, int last) {
			int result =0;
			if (first > last)//not found but will push from there
				result= first;
			else {
				int mid = (first + last) / 2;
				if (s.equals(words.get(mid)))
					result= -1;//implies found
				else if (s.compareTo(words.get(mid)) < 0)
					result= binarySearch(s, first, mid - 1);
				else if (s.compareTo(words.get(mid)) > 0)
					result= binarySearch(s, mid + 1, last);
			}
			return result;
		}
	}  //end readDoc class


	public static void main(String[] args){
		WebPages page = new WebPages();
		ArrayList<String> list = new ArrayList<>();
		Scanner scanFile = null;
		try {
			scanFile = new Scanner(new File(args[0]));
			while (scanFile.hasNextLine()){
				list.add(scanFile.nextLine());
			}
			int i = 0;

			while(!list.get(i).equals("*EOFs*")){
				page.addPage(list.get(i));
				i++;
			}
			printTerms();
			int stopWords = Integer.parseInt(list.get(i + 1));
			page.pruneStopWords(stopWords);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
