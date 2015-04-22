


public class ZeroOneGenerator {
	public static int[] Generate(int threshold)
	{
		String binaryZero = "";
		String binaryOne = "";
		int i;
		int[] ZeroOne = new int[2];
		for(i=0;i<8;i++)
		{
			if(i < threshold)
			{
				binaryZero += '0';
				binaryOne += '1';
			}
			else
			{
				binaryZero += '1';
				binaryOne += '0';
			}
		}
		binaryZero = "00000000" + binaryZero + binaryZero + binaryZero;
		binaryOne = "00000000" + binaryOne + binaryOne + binaryOne;
		//System.out.println(binaryZero);
		//System.out.println(binaryOne);
		ZeroOne[0] = Integer.parseInt(binaryZero,2);
		ZeroOne[1] = Integer.parseInt(binaryOne,2);
		return ZeroOne;
	}
	
}
