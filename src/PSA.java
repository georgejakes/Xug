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

	public boolean checkContains(ArrayList<Location> list,int height, int width,Resolution res){
		boolean result=false;
		
		Location newloc= new Location(height,width);
		Location l1 = new Location(0,0);
		Location l2 = new Location(0,res.maxWidth-1);
		Location l3 = new Location(res.maxHeight-1,0);
		Location l4 = new Location(res.maxHeight-1,res.maxWidth-1);
		
		if(newloc==l1||newloc==l2||newloc==l3||newloc==l4){
			result = true;
		}
		else{
		result=list.contains(newloc);}
		
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
				l.width=passphrase.getcode()*(counter+1)%res.maxWidth;
				l.height=passphrase.getcode()*(counter+1)%res.maxHeight;
				result.add(l);
				i++;
				count++;
			}
			else{
			l.height=(l.height+(l.width+1)*count*limit)%res.maxHeight;
			l.width=(l.width+(l.height+1)*count*limit)%res.maxWidth;
			if(!checkContains(result,l.height,l.width,res))
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
