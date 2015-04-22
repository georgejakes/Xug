import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	public static List<String> pixelList;
	public static int MSBThreshold;
	public static int limit = 1;
	public static Passphrase passphrase;
	public static boolean ClusterSetting; 
	public static String ClusterString;
	
	private static void init(boolean ClusterSet)
	{
		pixelList = new ArrayList<String>();
		mAudioStreamIndex = -1; 
		mVideoStreamIndex = -1;
		frameCount = 0; 
		keyFrameCount = 0;
		ClusterSetting = ClusterSet;
		MSBThreshold = 8;
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
	
	public static String DecodeVideo(String inputFileName, int lim, ArrayList<Integer> clusterList, String password)
	{
		init(false);
		passphrase = new Passphrase(password);
		return null;
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
            	ArrayList<Location> selectedPixels = new PSA().psa(limit, resolution, passphrase,0);
                Iterator<Location> iter = selectedPixels.iterator();
                while(iter.hasNext())
                {
                	Location l = iter.next();
                	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
        					Integer.toBinaryString(image.getRGB(l.width, l.height)));
                	pixelList.add(Integer.toBinaryString(image.getRGB(l.width, l.height)));
                }
            }
            
            frameCount++;
        }
        

    }
}
