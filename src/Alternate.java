import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Alternate {
	public List<Integer> maxInt(List<String> pixels){
		Iterator<String> iter=pixels.iterator();
		List<Integer> maxBit=new ArrayList<Integer>();
		
		String pixel;
		while(iter.hasNext()){
			pixel=iter.next();
			pixel=pixel.substring(8);
			//System.out.println(pixel);
			int count1=0,count0=0;
			for(int i=0;i<24;i++){
				char c=pixel.charAt(i);
				if(c=='0')
					count0+=1;
				else if(c=='1')
					count1+=1;
			}
			if(Math.max(count0, count1)==count0){
				maxBit.add(0);
			}
			else{
				maxBit.add(1);
				
			}
		}
		return maxBit;
	}
	
	public List<Integer> maxIntRange(List<String> pixels, int start, int end){
		Iterator<String> iter=pixels.iterator();
		List<Integer> maxBit=new ArrayList<Integer>();
		
		String pixel;
		while(iter.hasNext()){
			pixel=iter.next();
			pixel=pixel.substring(start,end);
			//System.out.println(pixel);
			int count1=0,count0=0;
			for(int i=0;i<pixel.length();i++){
				char c=pixel.charAt(i);
				if(c=='0')
					count0+=1;
				else if(c=='1')
					count1+=1;
			}
			if(Math.max(count0, count1)==count0){
				maxBit.add(0);
			}
			else{
				maxBit.add(1);
				
			}
		}
		return maxBit;
	}

	public String alternate(List<String> pixels, int start, int end, int limit){
		List<Integer> maxBits= maxIntRange(pixels, start, end);
		String s = "";
		int errorCount = 0, tempError = 0, i = 0;
		int startValue = 1;
		while (i < maxBits.size())
		{
			if(startValue == 0)
			{
				if((i % 2 == 0 && maxBits.get(i) != 0) || (i % 2 == 1 && maxBits.get(i) != 1))
				{
					s += "Error at frame no: " + i + "\n";
					errorCount ++;
				}
			}
			else
			{
				if((i % 2 == 0 && maxBits.get(i) != 1) || (i % 2 == 1 && maxBits.get(i) != 0))
				{
					s += "Error at frame no: " + i + "\n";
					errorCount ++;
				}
			}
			
			i++;
		}
		if(s.length() == 0)
		{
			return "True";
		}
		else
		{
			return s + "Error Count: " + errorCount;
		}
	}
	
	public Dimension pixelSelector(Dimension siz, int count)
	{
		int width = siz.width;
		int height = siz.height;
		Dimension process = new Dimension();
		process.width = count % width;
		process.height = count % height;
		if(count % 2 == 0)
		{
			//process.width = 0;
		}
		else
		{
			//process.height = 0;
		}
		return process;
	}

	

}