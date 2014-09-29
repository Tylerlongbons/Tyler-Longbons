import java.util.LinkedList;
public class Term implements Comparable<Term>{

	String name;
	int docFrequency;
	int totalFrequency;
	LinkedList<Occurrence> docsList = new LinkedList<Occurrence>();

	public Term(String name){
		this.name = name.toLowerCase();
		docFrequency = 0;
		totalFrequency = 0;
	}
	public void incFrequency(String document){
		totalFrequency++;

		boolean isNew = true;

		for(Occurrence o: docsList){
			if(o.docName.equals(document)){
				o.termFrequency++;
				isNew = false;
				break;
			}
		}
		if(isNew){
			Occurrence newOcc = new Occurrence(document);
			docsList.add(newOcc);
		}
		docFrequency = docsList.size();


	}
	public int compareTo(Term o){
		if(this.totalFrequency > o.totalFrequency){
			return -1;
		}
		else if(this.totalFrequency < o.totalFrequency){
			return 1;
		}else{
			return 0;
		}
	}

	public int compareWords(Term o){
		if(this.name.compareTo(o.name) == 0){
			return 0;
		}else if(this.name.compareTo(o.name) < 0){
			return -1;
		}else{
			return 1;
		}
	}


}
