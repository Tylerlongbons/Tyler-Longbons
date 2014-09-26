
public class Occurrence {
	
	public String docName;
	public int termFrequency;
	
	public Occurrence(String name){
		docName = name;
		termFrequency = 1;
	}
	public void incFrequency(){
		termFrequency++;
	}

}
