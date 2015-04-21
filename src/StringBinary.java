
public class StringBinary {
	
	static String[] zeroString = {"","0","00","000","0000","00000","000000","0000000","00000000"};
	public static String toBinary(String message)
	{
		int length = message.length();
		int i;
		String tempString = "";
		String messageInBinaryString;
		for(i=0,messageInBinaryString = "";i<length;i++)
		{
			tempString = Integer.toBinaryString((int)message.charAt(i));
			tempString = zeroString[8-tempString.length()] + tempString;
			messageInBinaryString += tempString;
		}
		return messageInBinaryString;
	}
	
	public static String toString(String binaryMessage)
	{
		int length = binaryMessage.length()/8;
		int i,j,k,temp;
		String message = "";
		for(i=0;i<length;i++)
		{
			temp = 0;
			for(j=i*8,k=7;j<(i*8)+8;j++,k--)
			{
				temp += Math.pow(2, k) * Character.getNumericValue(binaryMessage.charAt(j));
			}
			message += (char)temp;
		}
		return message;
	}
}
