import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author blaze
 *
 */
public class PSA {
static int counter=0;
public boolean IsBoundry(int width, int height, Resolution res){
//	System.out.println(width+","+ height);
	boolean result=false;
	Location newloc= new Location(width,height);
	Location l1 = new Location(0,0);
	Location l2 = new Location(0,res.maxHeight);
	Location l3 = new Location(res.maxWidth,0);
	Location l4 = new Location(res.maxWidth,res.maxHeight);
	ArrayList<Location> list = new ArrayList<Location>();
	list.add(l1);
	list.add(l2);
	list.add(l3);
	list.add(l4);
	
//	System.out.println("ARRAY LIST:");
//	for(Location temp:list){
//		System.out.println(temp.width+","+temp.height);
//	}
	
//	if(checkContains(list,height,width)){
//		result = true;
//	}
	return CheckContains(list,width,height);
	
}
	

public boolean CheckContains(ArrayList<Location> list,int width,int height ){
	boolean result=false;
//	System.out.println(width+","+ height);
	Location newloc= new Location(width,height);
	

//	result=list.contains(newloc);
	for(Location temp:list){
		if(newloc.height==temp.height && newloc.width==temp.width){
			result = true;
			
		}
//		System.out.println(temp.width+","+temp.height);
	}
//	System.out.println("Result = " + result);
	
	return result;
	
}
	public ArrayList<Location> psa(int limit, Resolution res, Passphrase passphrase,int counter)
	{
		
		ArrayList<Location> result= new ArrayList<Location>();
		int i=0,count=counter;
	
		while(i<limit)
		{
			Location l=new Location();
			if(i==0)
			{	
//				l.width=passphrase.getcode()*(counter+1)%res.maxWidth;
//				l.height=passphrase.getcode()*(counter+1)%res.maxHeight;
				l.width=1920;
				l.height=1080;
				if(IsBoundry(l.width,l.height,res)){
					
					
					l.width = (l.width  + 10) % res.maxWidth;
					l.height=( l.height + 10) % res.maxHeight;
				
				}
				result.add(l);
				
				i++;
				count++;
			}
			else{
			l.height=(l.height+(l.width+1)*count*limit)%res.maxHeight;
			l.width=(l.width+(l.height+1)*count*limit)%res.maxWidth;
			if(!CheckContains(result,l.width,l.height)&&!IsBoundry(l.width,l.height, res))
			{
				result.add(l);
				i++;
			}
			/*System.out.println(result.get(i).height+"  "+result.get(i).width);*/
			count++;
		}}
		//counter++;
		/*for(int j=0;j<8;j++)
			System.out.println(result.get(j).height+"  "+result.get(j).width); */
	
		return result;
	}
	
}
