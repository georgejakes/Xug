import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ModuleTester {
	public static void main(String args[])
	{
		

		
		//EncodeModule.EncodeVideo("/media/OS/aerounwired.mp4", "/home/shell/new/d.mp4", "Have you met ted recently you fucking whore", 3, "password",3,8);
		//String list = DecodeModule.DecodeVideo("/home/shell/new/d.mp4", 8);
		
		/*System.out.println("1");
	PSA p=new PSA();
	Resolution res=new Resolution(1920,1080);
	ArrayList<Location> res1=p.psa(8, res);*/
	
	
   /* while(itr.hasNext()){
        System.out.println(itr.next().height+"  "+itr.next().width);

	
	}*/
		String list = "101101101101101101101101101101101101101101101101101101101";
		System.out.println(list);
		Cluster temp = new Cluster(5);
		ArrayList<Integer> a = temp.GetCluster(list);
		for(int i:a)
		{
			System.out.println(i);
		}
		String bin = StringBinary.toBinary("Have you met ted");
		System.out.println(StringBinary.toString(bin));
		int[] result = ZeroOneGenerator.Generate(8);
		System.out.println(result[0] + " " + result[1]);
	}
}

