import java.util.ArrayList;


public class Cluster {
	int PL;
	String Pattern;
	public Cluster(int patternLength){
		
		
		this.Pattern=Cluster.getClusterStringPattern(patternLength);
		this.PL = this.Pattern.length();
	}
	
	public static String getClusterStringPattern(int patternLength){
		String pattern = "1";
		for(int i=0;i<patternLength-2;i++)
		{
			pattern += "0";
		}
		pattern += "1";
		return pattern;
	}
	
	public Boolean IsPattern(String bp, int i){
		String pattern = "";
		
		for(int k=0;k<this.PL;k++){
			pattern= pattern + bp.charAt(i+k);
		}
		if(pattern.equals(this.Pattern)){
			return true;
		}
		return false;
		
	}
	
	public ArrayList<Integer> GetCluster(String bp ){
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		int i=0;

		int counter =0;
		int last_suc =0;
		while(i<bp.length()){
			
			if((i<bp.length()-this.PL+1) && IsPattern(bp,i)){
				if(counter > 0){
					result.add(counter);
					counter=0;
					last_suc=i-1;
				}
				
				result.add(this.PL);
				i=i+this.PL;
				last_suc = i-1;
				
			}
			else{
				i++;
				counter++;
				if(counter>=this.PL-1){
					result.add(this.PL-1);
					counter=counter-(this.PL-1);
					last_suc=last_suc+this.PL-1;
				}
			}
			
		}
		if(counter>0)  {result.add(counter);}
		return result;
		
	}

}
