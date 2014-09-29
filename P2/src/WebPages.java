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

		ReadDoc docReader = new ReadDoc(termIndex);
		docReader.readFile(filename);
	}
	public class ReadDoc {

		public ArrayList<Term> words;
		private ArrayList<String> tag = new ArrayList<String>();
		private boolean inTag =false;

		public ReadDoc(ArrayList<Term> terms){
			this.words = terms;
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
							int srchRslt = binarySearch(s, 0, words.size() - 1);
							if(srchRslt >-1 && s.length() > 0){
								Term t = new Term(s);
								t.incFrequency(filename);;
								words.add(srchRslt, t);
							}else
								if(srchRslt == -1){
									Term t = getTerm(s);
									t.incFrequency(filename);;
								}
						}
						tag = new ArrayList<String>();
					} else if(!inTag){
						w = punct(w);
						int srchRslt = binarySearch(w, 0, words.size() - 1);
						if(srchRslt >-1 && w.length() > 0) {
							Term t = new Term(w);
							t.incFrequency(filename);;
							words.add(srchRslt, t);
						}else{
							if(srchRslt == -1){
							Term t = getTerm(w);
							t.incFrequency(filename);
							}
						}
					}
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				// TODO How do they want errors
				e.printStackTrace();
			}
		}
		private Term getTerm(String s){
			Term term = null;
			for(Term t: words){
				if(t.name.equals(s)){
					term = t;
					break;
				}
			}
			return term;
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
				if (s.equals(words.get(mid).name))
					result= -1;//implies found
				else if (s.compareTo(words.get(mid).name) < 0)
					result= binarySearch(s, first, mid - 1);
				else if (s.compareTo(words.get(mid).name) > 0)
					result= binarySearch(s, mid + 1, last);
			}
			return result;
		}
	}  //end readDoc class
	
	public static void printTerms(){
		  System.out.println("WORDS");
		  for(Term t: termIndex){
		   String msg;
		   if (t.name.length() <= 2) {
		    msg = "term:"+t.name+"\t\tdoc_freq:"+t.docFrequency+"\ttotal_freq:"+t.totalFrequency;
		   } else {
		    msg = "term:"+t.name+"\tdoc_freq:"+t.docFrequency+"\ttotal_freq:"+t.totalFrequency;
		   }
		    
		   System.out.println(msg);
		  }
		
		//		System.out.println("WORDS");
//		for(Term t: termIndex){
//			System.out.println(t.name);
//		}
	}
	//Prichard and Carrano pgs (529-531)
	private static void mergeSort(ArrayList<Term> theArray,ArrayList<Term> tempArray, int first, int last, sortType type){
		
		if(first < last){
			int mid = (first + last) / 2;
			mergeSort(theArray,tempArray, first, mid, type);
			mergeSort(theArray,tempArray, mid + 1, last, type);

			merge(theArray,tempArray, first, mid, last, type);
		}
		

	}
	//Prichard and Carrano pgs (529-531)
	private static void merge(ArrayList<Term> theArray,ArrayList<Term> tempArray,int first, int mid, int last, sortType type){
		int first1 = first;
		int last1 = mid;
		int first2 = mid + 1;
		int last2 = last;
		int index = first1;
		int counter = 0; //this needs to be in output

		while((first1 <= last1) && (first2 <= last2)){
			Term f1 = theArray.get(first1);
			Term f2 = theArray.get(first2);
			if(type == sortType.totalFrequency){
				int cmp = f1.compareTo(f2);
				if(cmp <= 0){ 
					tempArray.add(index, f1);
					first1++;
					counter++;  //this needs to be in output
				}else{
					tempArray.set(index, f2);
					first2++;
				}
				index++;
			}else{
				int cmp = f1.compareWords(f2);
				if(cmp <= 0){ 
					tempArray.add(index, f1);
					first1++;
					counter++;  //this needs to be in output
				}else{
					tempArray.set(index, f2);
					first2++;
				}
				index++;
			}
			
		}

		while(first1 <= last1){
			Term f1 = theArray.get(first1);
			tempArray.set(index, f1);
			first1++;
			index++;
		}
		while(first2 <= last2){
			Term f2 = theArray.get(first2);
			tempArray.add(index, f2);
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
		ArrayList<Term> tempArray = new ArrayList<Term>();
		mergeSortCount = 0;
		mergeSort(termIndex,tempArray, 0, termIndex.size() - 1,sortType.totalFrequency);
		System.out.println("Copies: "+ mergeSortCount);
		for(int i = 0; i < n; i++){
			termIndex.remove(0);
		}
		mergeSortCount = 0;
		mergeSort(termIndex,tempArray, 0, termIndex.size() - 1,sortType.words);
		System.out.println("Copies: " + mergeSortCount);
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

	
		public static void main(String[] args){
		WebPages page = new WebPages();
		ArrayList<String> list = new ArrayList<>();
		String inputfile = "input2.txt";
		//String inputFile = args[0];
		Scanner scanFile = null;
		try {
			scanFile = new Scanner(new File(inputfile));
			while (scanFile.hasNextLine()){
				list.add(scanFile.nextLine());
			}
			int i = 0;

			while(!list.get(i).equals("*EOFs*")){
				String pageName = list.get(i);
				page.addPage(pageName);
				i++;
			}
			printTerms();
			
			i++;
			int stopWords = Integer.parseInt(list.get(i));
			page.pruneStopWords(stopWords);
			
			printTerms();
			
			scanFile.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
