import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class Test_Cluster{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Cluster base = new Cluster(6);
		int[] t1 = {6,6,6,5,1,5,3,6};
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i:t1){
			temp.add(i);
			System.out.print(i);
		}
		System.out.println("....");
		temp = base.ClearCorruption(temp);
		for(int i:temp){
			System.out.print(i);
		}

	}

}
