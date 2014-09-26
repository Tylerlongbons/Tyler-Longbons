import java.util.LinkedList;
public class Term {

	String name;
	int docFrequency;
	int totalFrequency;
	LinkedList<Occurrence> docsList = new LinkedList<Occurrence>();
	
	public Term(String name){
		this.name = name.toLowerCase();
		docFrequency = 0;
	}
	public void incFrequency(String document){
		totalFrequency++;
		int i = 0;
		boolean found = false;
			if(docsList.size() > 0){
				while(docsList.get(i) != null){
					
					if(docsList.get(i).equals(document)){
						found = true;
						break;
					}
					i++;
				}
				if(!found){
					Occurrence occur = new Occurrence(document);
					docsList.addFirst(occur);
				}else{
					docsList.get(i).incFrequency();
					docFrequency++;
				}
			}else{
				Occurrence occur = new Occurrence(document);
				docsList.addFirst(occur);
			}
			
	}
	public int compareTo(Term o){
		if(this.totalFrequency < o.totalFrequency){
			return -1;
		}
		else if(this.totalFrequency > o.totalFrequency){
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
