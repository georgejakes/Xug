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

	public boolean checkContains(ArrayList<Location> list,int height, int width){
		boolean result=false;
		
		Location newloc= new Location(height,width);
		
		result=list.contains(newloc);
		return result;
		
	}
	public ArrayList<Location> psa(int limit, Resolution res)
	{
		ArrayList<Location> result= new ArrayList<Location>();
		int i=0,count=counter;
		
		
		while(i<limit)
		{
			Location l=new Location();
			if(i==0)
			{
				l.width=0;
				l.height=0;
				result.add(l);
				i++;
				count++;
			}
			else{
			l.height=(l.height+(l.width+1)*count*limit)%res.maxHeight;
			l.width=(l.width+(l.height+1)*count*limit)%res.maxWidth;
			if(!checkContains(result,l.height,l.width))
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
