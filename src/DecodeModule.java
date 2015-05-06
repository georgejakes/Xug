import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;


public class DecodeModule {
	
	public static int mAudioStreamIndex, mVideoStreamIndex, frameCount, keyFrameCount;
	public static Resolution resolution;
	public static ArrayList<Integer> pixelList;
	public static int MSBThreshold;
	public static int MessageLimitPerFrame;
	public static Passphrase passphrase;
	public static boolean ClusterSetting; 
	public static String ClusterString;
	public static ArrayList<Integer> Cluster;
	public static int ClusterCount;	//Counts current cluster being focused
	public static int ClusterCounter; //Counts individual cluster volume
	public static int ClusterSize;
	public static ArrayList<ZeroOne> ZeroOneCounter;
	public static String finalMessage;
	public static int shortCluster; // 0 - First Short Cluster found, 1 - second short cluster found, 2 - Actual cluster size match
	public static int currentClusterSet;
	public static boolean byClustList;
	public static String endOfMessageText = ""+(char)3+(char)4;
	
	private static void init(boolean ClusterSet)
	{
		pixelList = new ArrayList<Integer>();
		mAudioStreamIndex = -1; 
		mVideoStreamIndex = -1;
		frameCount = 0; 
		keyFrameCount = 0;
		ClusterSetting = ClusterSet;
		MSBThreshold = 8;
		ClusterCounter = 0;
		ClusterCount = 0;
		MessageLimitPerFrame = 0;
		finalMessage = "";
		ZeroOneCounter = new ArrayList<ZeroOne>();
		shortCluster = 2;
		currentClusterSet = 0;
	}
	
	public static String DecodeVideo(String inputFileName, int threshold)
	{
		init(true);
		ClusterString = "";
		MSBThreshold = threshold;
		IMediaReader mediaReader = ToolFactory.makeReader(inputFileName);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		IContainer container = IContainer.make();
		
		int result = container.open(inputFileName, IContainer.Type.READ, null);
        if (result<0)
        {
        	throw new RuntimeException("Failed to open media file");
        }
        
        IStream stream = container.getStream(0);
        IStreamCoder coder = stream.getStreamCoder();
        if(coder.getCodecType() != ICodec.Type.CODEC_TYPE_VIDEO)
        {
        	return null;
        }
        resolution = new Resolution(coder.getWidth(), coder.getHeight());
        
        mediaReader.addListener(new ImageSnapListener());
        while (mediaReader.readPacket() == null) ;
		return ClusterString;
	}
	
	public static String DecodeVideo(String inputFileName, int MsgLimitPerFrame, String password, ArrayList<Integer> clusterList, int clusterSize, int threshold, boolean byClusterList)
	{
		init(false);
		passphrase = new Passphrase(password);
		Cluster = clusterList;
		MSBThreshold = threshold;
		MessageLimitPerFrame = MsgLimitPerFrame; 
		ClusterSize = clusterSize;
		byClustList = byClusterList;
		
		IMediaReader mediaReader = ToolFactory.makeReader(inputFileName);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		IContainer container = IContainer.make();
		int result = container.open(inputFileName, IContainer.Type.READ, null);
        if (result<0)
        {
        	throw new RuntimeException("Failed to open media file");
        }
        IStream stream = container.getStream(0);
        IStreamCoder coder = stream.getStreamCoder();
        if(coder.getCodecType() != ICodec.Type.CODEC_TYPE_VIDEO)
        {
        	return null;
        }
        resolution = new Resolution(coder.getWidth(), coder.getHeight());
        mediaReader.addListener(new ImageSnapListener());
        while (mediaReader.readPacket() == null) ;
		return StringBinary.toString(finalMessage.split(endOfMessageText)[0]);
	}
	
	private static class ImageSnapListener extends MediaListenerAdapter {

        public void onVideoPicture(IVideoPictureEvent event) {

            if (event.getStreamIndex() != mVideoStreamIndex) {
                // if the selected video stream id is not yet set, go ahead an
                // select this lucky video stream
                if (mVideoStreamIndex == -1)
                    mVideoStreamIndex = event.getStreamIndex();
                // no need to show frames from this video stream
                else
                    return;
            }
                                
            BufferedImage image = event.getImage();
            IVideoPicture vidPic = event.getMediaData();
            if(vidPic.isKeyFrame())
            {
            	keyFrameCount ++ ;
            }
            Dimension siz = new Dimension();
            siz.width = image.getWidth();
            siz.height = image.getHeight();
            if(siz.width != resolution.maxWidth)
            {
            	throw new RuntimeException("Width Does Not MAtch");
            }
            
            if(ClusterSetting)
            {
            	System.out.println(frameCount);
            	int corner1 = MajorityBitFinder.MajorityBitSearch(Integer.toBinaryString(image.getRGB(0, 0)), MSBThreshold);
            	int corner2 = MajorityBitFinder.MajorityBitSearch(Integer.toBinaryString(image.getRGB(resolution.maxWidth-1, 0)), MSBThreshold);
            	int corner3 = MajorityBitFinder.MajorityBitSearch(Integer.toBinaryString(image.getRGB(0, resolution.maxHeight-1)), MSBThreshold);
            	int corner4 = MajorityBitFinder.MajorityBitSearch(Integer.toBinaryString(image.getRGB(resolution.maxWidth-1, resolution.maxHeight-1)), MSBThreshold);
            	int[] zeroCounter = {0,0,0};
            	zeroCounter[corner1]++;
            	zeroCounter[corner2]++;
            	zeroCounter[corner3]++;
            	zeroCounter[corner4]++;
            	if(zeroCounter[0] > zeroCounter[1])
            	{
            		ClusterString += 0;
            	}
            	else
            	{
            		ClusterString += 1;
            	}
            }
            else
            {
            	currentClusterSet = frameCount/ClusterSize;
            	if(byClustList)
            	{
            		
                	if(ClusterCount < Cluster.size() && ClusterCounter >= Cluster.get(ClusterCount))
                	{
                		if(ZeroOneCounter.size() > 0)
                		{
                			String messageTemp = Accumulate(Cluster.get(ClusterCount),ZeroOneCounter,MessageLimitPerFrame);
                    		finalMessage += messageTemp;
                		}
                		
                		ClusterCount++;
                		ClusterCounter = 0;
                		//pixelList.clear();
                		if(ClusterCount < Cluster.size())
                		{
                			//System.out.println(ZeroOneCounter.size() + "1");
                			ZeroOneCounter.clear();
                    		ArrayList<Location> selectedPixels = new PSA().psa(MessageLimitPerFrame, resolution, passphrase,currentClusterSet);
                            Iterator<Location> iter = selectedPixels.iterator();
                            while(iter.hasNext())
                            {
                            	Location l = iter.next();
                            	String rgb = Integer.toBinaryString(image.getRGB(l.width, l.height));
                            	ZeroOne zeroOneTemp = new ZeroOne();
                            	zeroOneTemp.var[MajorityBitFinder.MajorityBitSearch(rgb, MSBThreshold)]++;
                            	ZeroOneCounter.add(zeroOneTemp);
                            	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
                    					rgb);
                            	//pixelList.add(image.getRGB(l.width, l.height));
                            }
                            
                    		ClusterCounter ++;
                		}
                	}
                	else if(ClusterCount == 0 && ClusterCounter == 0)
                	{
                		//System.out.println(ZeroOneCounter.size() + "3");
            			ZeroOneCounter.clear();
                		ArrayList<Location> selectedPixels = new PSA().psa(MessageLimitPerFrame, resolution, passphrase,currentClusterSet);
                        Iterator<Location> iter = selectedPixels.iterator();
                        while(iter.hasNext())
                        {
                        	Location l = iter.next();
                        	String rgb = Integer.toBinaryString(image.getRGB(l.width, l.height));
                        	ZeroOne zeroOneTemp = new ZeroOne();
                        	zeroOneTemp.var[MajorityBitFinder.MajorityBitSearch(rgb, MSBThreshold)]++;
                        	ZeroOneCounter.add(zeroOneTemp);
                        	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
                					rgb);
                        	//pixelList.add(image.getRGB(l.width, l.height));
                        }
                        
                		ClusterCounter ++;
                	}
                	else if(ClusterCount < Cluster.size())
                	{
                		ArrayList<Location> selectedPixels = new PSA().psa(MessageLimitPerFrame, resolution, passphrase,currentClusterSet);
                        Iterator<Location> iter = selectedPixels.iterator();
                        int i=0;
                        while(iter.hasNext())
                        {
                        	//System.out.println(ZeroOneCounter.size() + "2");
                        	Location l = iter.next();
                        	String rgb = Integer.toBinaryString(image.getRGB(l.width, l.height));
                        	ZeroOne zeroOneTemp = ZeroOneCounter.get(i);
                        	zeroOneTemp.var[MajorityBitFinder.MajorityBitSearch(rgb, MSBThreshold)]++;
                        	ZeroOneCounter.set(i, zeroOneTemp);
                        	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
                					rgb);
                        	i++;
                        	//pixelList.add(image.getRGB(l.width, l.height));
                        }
                        
                		ClusterCounter++;
                	}
                	else
                	{
                		ZeroOneCounter.clear();
                	}
            	}
            	else
            	{
                	if(ClusterCount < Cluster.size() && ClusterCounter >= ClusterSize)
                	{
                		String messageTemp = Accumulate(ClusterSize,ZeroOneCounter,MessageLimitPerFrame);
                    	finalMessage += messageTemp;
                		ClusterCount++;
                		ClusterCounter = 0;
                		//pixelList.clear();
                		if(ClusterCount < Cluster.size())
                		{
                			//System.out.println(ZeroOneCounter.size() + "1");
                			ZeroOneCounter.clear();
                    		ArrayList<Location> selectedPixels = new PSA().psa(MessageLimitPerFrame, resolution, passphrase,currentClusterSet);
                            Iterator<Location> iter = selectedPixels.iterator();
                            while(iter.hasNext())
                            {
                            	Location l = iter.next();
                            	String rgb = Integer.toBinaryString(image.getRGB(l.width, l.height));
                            	ZeroOne zeroOneTemp = new ZeroOne();
                            	zeroOneTemp.var[MajorityBitFinder.MajorityBitSearch(rgb, MSBThreshold)]++;
                            	ZeroOneCounter.add(zeroOneTemp);
                            	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
                    					rgb);
                            	//pixelList.add(image.getRGB(l.width, l.height));
                            }
                            
                    		ClusterCounter ++;
                		}
                	}
                	else if(ClusterCount == 0 && ClusterCounter == 0)
                	{
                		//System.out.println(ZeroOneCounter.size() + "3");
            			ZeroOneCounter.clear();
                		ArrayList<Location> selectedPixels = new PSA().psa(MessageLimitPerFrame, resolution, passphrase,currentClusterSet);
                        Iterator<Location> iter = selectedPixels.iterator();
                        while(iter.hasNext())
                        {
                        	Location l = iter.next();
                        	String rgb = Integer.toBinaryString(image.getRGB(l.width, l.height));
                        	ZeroOne zeroOneTemp = new ZeroOne();
                        	zeroOneTemp.var[MajorityBitFinder.MajorityBitSearch(rgb, MSBThreshold)]++;
                        	ZeroOneCounter.add(zeroOneTemp);
                        	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
                					rgb);
                        	//pixelList.add(image.getRGB(l.width, l.height));
                        }
                        
                		ClusterCounter ++;
                	}
                	else if(ClusterCount < Cluster.size())
                	{
                		ArrayList<Location> selectedPixels = new PSA().psa(MessageLimitPerFrame, resolution, passphrase,currentClusterSet);
                        Iterator<Location> iter = selectedPixels.iterator();
                        int i=0;
                        while(iter.hasNext())
                        {
                        	//System.out.println(ZeroOneCounter.size() + "2");
                        	Location l = iter.next();
                        	String rgb = Integer.toBinaryString(image.getRGB(l.width, l.height));
                        	ZeroOne zeroOneTemp = ZeroOneCounter.get(i);
                        	zeroOneTemp.var[MajorityBitFinder.MajorityBitSearch(rgb, MSBThreshold)]++;
                        	ZeroOneCounter.set(i, zeroOneTemp);
                        	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
                					rgb);
                        	i++;
                        	//pixelList.add(image.getRGB(l.width, l.height));
                        }
                        
                		ClusterCounter++;
                	}
                	else
                	{
                		ZeroOneCounter.clear();
                	}
            	}
            }
            
            frameCount++;
        }
        

    }
	
	private static String Accumulate(int ClusterSize, ArrayList<ZeroOne> ZeroOneCount, int MsgLimitPerFrame)
	{
		String message = "";
		for(int i=0;i<MsgLimitPerFrame;i++)
		{
			ZeroOne temp  = ZeroOneCount.get(i);
			if(temp.var[0] > temp.var[1])
			{
				message+='0';
			}
			else
			{
				message+='1';
			}
		}
		return message;
	}
}
