import java.util.ArrayList;
import java.util.Collections;


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
		return ClearCorruption(result);
		
	}
	
	
	public ArrayList<Integer> ClearCorruption(ArrayList<Integer> r1 ){
		int counter=0;
		ArrayList<Integer> f1 = new ArrayList<Integer>() ;
		for(int i=0;i<r1.size();i++){
			if(r1.get(i)==this.PL){
			
				f1.add(r1.get(i));
				//System.out.print(r1.get(i));
				
			}
			else{int j=0;
			//System.out.println("i="+(i+1));
				for(j=i+1;j<r1.size();j++){
					if(r1.get(j) == this.PL){
						//System.out.println("j="+(j+1));
						break;
			
						}
					else{
						//System.out.print(r1.get(i));
						counter++;
					}
				}
//				System.out.println("counter="+counter);
				ArrayList<Integer> left = new ArrayList<Integer>() ;
				ArrayList<Integer> right = new ArrayList<Integer>() ;
				//System.out.println("i="+i);
				left = Left_Clean(i,j,r1);
				right = Reft_Clean(i,j,r1);
				if(Cluster_count(left)>=Cluster_count(right)){
					f1.addAll(left);
				}
				else{
					f1.addAll(right);
				}
				i=j-1;
			}
		}
		
		return f1;
		
	}
	

	public ArrayList<Integer> Left_Clean(int i, int j ,ArrayList<Integer> r1){
		int sum=0;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		while(i<j){
			sum = Sum(i,j, r1);
			if(sum%this.PL==0){
				int k = sum/this.PL;
				while(k>0){
					//temp.add(0);
					temp.add(this.PL);
					k--;
				}
				break;
			}
			else{
				temp.add(r1.get(i));
				i++;
			}
		}
		return temp;
		
	}
	

	public ArrayList<Integer> Reft_Clean(int i, int j ,ArrayList<Integer> r1){
		int sum=0;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		while(j>i){
			sum = Sum(i,j, r1);
			if(sum%this.PL==0){
				int k = sum/this.PL;
				while(k!=0){
					temp.add(this.PL);
					k--;
					
				}
				break;
			}
			else{
				temp.add(r1.get(j-1));
				j--;
			}
		}
		Collections.reverse(temp);
		return temp;
		
	}	
	int Sum(int i,int j,ArrayList<Integer> r1){
		int sum=0;
		while(i<j){
			sum=sum+r1.get(i);
			i++;
		}
		return sum;
		
	}
	int Cluster_count(ArrayList<Integer> temp){
		int count=0;
		for(int i:temp){
			if(i==this.PL){count++;}
		}
		return count;
		
	}
	
}
