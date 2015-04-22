
public class MajorityBitFinder {
	public static int MajorityBitSearch(String color, int limit)
	{
		int[] tally = {0,0,0};
		if (color.length() == 32)
		{
			for(int i=1;i<=3;i++)
			{
				tally[MajorityBit(color,8*i,(8*i)+limit)]++;
			}
			if(tally[0] > tally[1])
			{
				return 0;
			}
			else
			{
				return 1;
			}
			
		}
		else
			return 2;
	}
	
	private static int MajorityBit(String color, int start, int finish)
	{
		int[] tally = {0,0};
		for(int i=start;i<finish;i++)
		{
			tally[Character.getNumericValue(color.charAt(i))]++;
		}
		if(tally[0] > tally[1])
		{
			return 0;
		}
		else if (tally[1] > tally[0])
		{
			return 1;
		}
		else
		{
			return 2;
		}
	}
}
