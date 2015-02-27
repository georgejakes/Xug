import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ModuleTester {
	public static void main(String args[])
	{
		EncodeModule enc = new EncodeModule();

		
		enc.EncodeVideo("/media/OS/aerounwired.mp4", "/home/shell/new/d.mp4", " ", 8);
	
		
		/*System.out.println("1");
	PSA p=new PSA();
	Resolution res=new Resolution(1920,1080);
	ArrayList<Location> res1=p.psa(8, res);*/
	
	
   /* while(itr.hasNext()){
        System.out.println(itr.next().height+"  "+itr.next().width);

	
	}*/
}}

