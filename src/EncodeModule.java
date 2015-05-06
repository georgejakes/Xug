import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;


public class EncodeModule {
	public static int mAudioStreamIndex, mVideoStreamIndex, frameCount, keyFrameCount;
	//public static List<String> pixelList;
	public static IMediaWriter writer;
	public static Resolution resolution;
	public static int messageLimitPerFrame;
	public static String bitMessage;
	public static int bitMessageCounter;
	public static Passphrase passphrase;
	public static int clusterLimit;
	public static int clusterCount;
	public static String clusterString; //String to be embedded in corners for cluster identification
	public static int clusterCounter;
	public static int MSBThreshold;
	public static int[] ZeroOne;
	public static boolean messageExhausted;
	public static String endOfMessageText = ""+(char)3+(char)4+(char)5+(char)6;
	
	private static void init()
	{
		mAudioStreamIndex = -1;
		mVideoStreamIndex = -1;
		frameCount = 0;
		keyFrameCount = 0;
		clusterCount = 0;
		clusterLimit = 1;
		bitMessageCounter = 0;
		bitMessage = "";
		MSBThreshold = 0;
		clusterCounter = 0;
		messageExhausted = false;
		//pixelList = new ArrayList<String>();
	}
	
	/**
	 * Encode Video Module
	 * @param inputFileName File Location for input and name of file
	 * @param outputFileName File location for output and name of file
	 * @param message Message that needs to be encoded
	 * @param msgLimitPerFrame Number of bits that needs to be encoded from the message per frame
	 * @param password Password for the pixel selection algorithm
	 * @param clusterNumber The number of repetitions per frame
	 * @param threshold The number of bits changed from the MSB in each color of a pixel
	 * @return 'True' if Encoding was successful.
	 */
	public static boolean EncodeVideo(String inputFileName, String outputFileName, String message, int msgLimitPerFrame, String password, int clusterNumber, int threshold)
	{
		init();
		message += endOfMessageText;
		passphrase = new Passphrase(password);
        messageLimitPerFrame = msgLimitPerFrame;
        clusterLimit = clusterNumber;
        clusterString = Cluster.getClusterStringPattern(clusterNumber);
        bitMessage = StringBinary.toBinary(message);
        MSBThreshold = threshold;
        ZeroOne = ZeroOneGenerator.Generate(MSBThreshold);
        
        if(clusterString.length() != clusterLimit)
        {
        	throw new RuntimeException("Cluster pattern failed to generate properly");
        }
        
        if(bitMessage.length() % 8 != 0)
        {
        	throw new RuntimeException("Faulty Byte generation");
        }
        
		int audioStreamBool = -1, videoStreamBool = -1;
        int streamCount = 0;
		IMediaReader mediaReader = ToolFactory.makeReader(inputFileName);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		IContainer container = IContainer.make();
        // we attempt to open up the container
        int result = container.open(inputFileName, IContainer.Type.READ, null);
        // check if the operation was successful
        streamCount = container.getNumStreams();
        if (result<0)
        {
        	throw new RuntimeException("Failed to open media file");
        }
            
        
        if(streamCount < 2)
        {
        	System.out.println("This is not a proper video!");
        	return false;
        }
        
        //Selecting audio and video streams
        for(int x=0;x<streamCount;x++)
        {
        	if(audioStreamBool == -1 || videoStreamBool == -1 )
        	{
        		IStream randomStream = container.getStream(x);
        		IStreamCoder randomCoder = randomStream.getStreamCoder();
        		if(randomCoder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
        		{
        			
        			videoStreamBool = x;
        		}
        		else if(randomCoder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
        		{
        			audioStreamBool = x;
        		}
        	}
        	else
        	{
        		break;
        	}
        }
        
        if(audioStreamBool == -1|| videoStreamBool == -1)
        {
        	System.out.println("Error in streams");
        	return false;
        }
        IStream stream = container.getStream(videoStreamBool);
        IStream audiostream = container.getStream(audioStreamBool);
        //IRational fps = stream.getFrameRate();
        IStreamCoder coder = stream.getStreamCoder();
        IStreamCoder audioCoder = audiostream.getStreamCoder();
        writer = ToolFactory.makeWriter(outputFileName);
        resolution = new Resolution(coder.getWidth(), coder.getHeight());
        writer.addVideoStream(0, 0, coder.getCodecID() ,coder.getWidth(),coder.getHeight());
        writer.addAudioStream(1, 1, audioCoder.getCodecID(), audioCoder.getChannels(), audioCoder.getSampleRate());
        
        mediaReader.addListener(new AudioSampleListener());
        mediaReader.addListener(new ImageSnapListener());
        while (mediaReader.readPacket() == null) ;
        writer.close();
        container.close();
        System.out.println(message);
		return true;
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
            //Receive selected Pixels
            
            
            //System.out.println("Loop Running");                    
            BufferedImage image = event.getImage();
            IVideoPicture vidPic = event.getMediaData();
            if(vidPic.isKeyFrame())
            {
            	keyFrameCount ++ ;
            }
            BufferedImage bgrScreen = convertToType(image, BufferedImage.TYPE_3BYTE_BGR);
            //BufferedImage bgrScreen = image;
            Dimension siz = new Dimension();
            siz.width = bgrScreen.getWidth();
            siz.height = bgrScreen.getHeight();
            
            if(siz.width != resolution.maxWidth)
            {
            	throw new RuntimeException("Width does not match");
            }
            
            if(clusterCount < clusterLimit && !messageExhausted)
            {
            	if(Character.getNumericValue(clusterString.charAt(clusterCount)) == 1)
            	{
                	bgrScreen.setRGB(0, 0, 
                			bgrScreen.getRGB(0, 0) | ZeroOne[1] );
                	bgrScreen.setRGB(resolution.maxWidth-1, 0, 
                			bgrScreen.getRGB(resolution.maxWidth-1, 0) | ZeroOne[1] );
                	bgrScreen.setRGB(0, resolution.maxHeight-1, 
                			bgrScreen.getRGB(0, resolution.maxHeight-1) | ZeroOne[1] );
                	bgrScreen.setRGB(resolution.maxWidth-1, resolution.maxHeight-1,
                			bgrScreen.getRGB(resolution.maxWidth-1, resolution.maxHeight-1) | ZeroOne[1] );
                }
            	else
            	{
            		bgrScreen.setRGB(0, 0, 
                			bgrScreen.getRGB(0, 0) & ZeroOne[0] );
                	bgrScreen.setRGB(resolution.maxWidth-1, 0, 
                			bgrScreen.getRGB(resolution.maxWidth-1, 0) & ZeroOne[0] );
                	bgrScreen.setRGB(0, resolution.maxHeight-1, 
                			bgrScreen.getRGB(0, resolution.maxHeight-1) & ZeroOne[0] );
                	bgrScreen.setRGB(resolution.maxWidth-1, resolution.maxHeight-1, 
                			bgrScreen.getRGB(resolution.maxWidth-1, resolution.maxHeight-1) & ZeroOne[0] );
            	}
            	clusterCount++;
            }
            else if(!messageExhausted)
            {
            	clusterCount = 0;
            	if(Character.getNumericValue(clusterString.charAt(clusterCount)) == 1)
            	{
                	bgrScreen.setRGB(0, 0, 
                			bgrScreen.getRGB(0, 0) | ZeroOne[1] );
                	bgrScreen.setRGB(resolution.maxWidth-1, 0, 
                			bgrScreen.getRGB(resolution.maxWidth-1, 0) | ZeroOne[1] );
                	bgrScreen.setRGB(0, resolution.maxHeight-1, 
                			bgrScreen.getRGB(0, resolution.maxHeight-1) | ZeroOne[1] );
                	bgrScreen.setRGB(resolution.maxWidth-1, resolution.maxHeight-1,
                			bgrScreen.getRGB(resolution.maxWidth-1, resolution.maxHeight-1) | ZeroOne[1] );
                }
            	else
            	{
            		bgrScreen.setRGB(0, 0, 
                			bgrScreen.getRGB(0, 0) & ZeroOne[0] );
                	bgrScreen.setRGB(resolution.maxWidth-1, 0, 
                			bgrScreen.getRGB(resolution.maxWidth-1, 0) & ZeroOne[0] );
                	bgrScreen.setRGB(0, resolution.maxHeight-1, 
                			bgrScreen.getRGB(0, resolution.maxHeight-1) & ZeroOne[0] );
                	bgrScreen.setRGB(resolution.maxWidth-1, resolution.maxHeight-1, 
                			bgrScreen.getRGB(resolution.maxWidth-1, resolution.maxHeight-1) & ZeroOne[0] );
            	}
            	clusterCount++;
            	bitMessageCounter += messageLimitPerFrame;
            	clusterCounter++;
            }
            else
            {
            	bgrScreen.setRGB(0, 0, 
            			bgrScreen.getRGB(0, 0) & ZeroOne[0] );
            	bgrScreen.setRGB(resolution.maxWidth-1, 0, 
            			bgrScreen.getRGB(resolution.maxWidth-1, 0) & ZeroOne[0] );
            	bgrScreen.setRGB(0, resolution.maxHeight-1, 
            			bgrScreen.getRGB(0, resolution.maxHeight-1) & ZeroOne[0] );
            	bgrScreen.setRGB(resolution.maxWidth-1, resolution.maxHeight-1, 
            			bgrScreen.getRGB(resolution.maxWidth-1, resolution.maxHeight-1) & ZeroOne[0] );
            	bitMessageCounter += messageLimitPerFrame;
            }
            ArrayList<Location> selectedPixels = new PSA().psa(messageLimitPerFrame, resolution, passphrase, clusterCounter);
            //System.out.println(clusterCounter);
            Iterator<Location> iter = selectedPixels.iterator();
            int currentMessageBit = bitMessageCounter;
            String messageToPrint = "Cluster Number: "+ clusterCounter + " Frame Number: " + frameCount;
            while(iter.hasNext())
            {
            	Location l = iter.next();
            	if(bitMessage.length() > currentMessageBit)
            	{
            		if(Character.getNumericValue(bitMessage.charAt(currentMessageBit)) == 1)
                    {
                    	bgrScreen.setRGB(l.width, l.height, 
                    			bgrScreen.getRGB(l.width, l.height) | ZeroOne[1] );
                    }
                    else
                    {
                    	bgrScreen.setRGB(l.width, l.height, 
                    			bgrScreen.getRGB(l.width, l.height) & ZeroOne[0] );
                    }
            		messageToPrint += " Pixel Change at: " + l.width + " , " + l.height + " to --> " + 
							Integer.toBinaryString(bgrScreen.getRGB(l.width, l.height)) + ",";
            	}
            	else
            	{
            		messageExhausted = true;
            	}
            	
            	
                //pixelList.add(Integer.toBinaryString(bgrScreen.getRGB(l.width, l.height)));
                //writer.encodeVideo(0, bgrScreen,System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                
                currentMessageBit++;
            }
            System.out.println(messageToPrint);
            
            writer.encodeVideo(0, bgrScreen,event.getTimeStamp(event.getTimeUnit()), event.getTimeUnit());
            frameCount ++ ;
            //System.out.println(Integer.toBinaryString(bgrScreen.getRGB(300, 300)));
            
            
            /*
            try {
                Thread.sleep((long) (1000 / frameRate));
            } 
            catch (InterruptedException e) {
                // ignore
            } */
        }
        
    }
	
	private static class AudioSampleListener extends MediaListenerAdapter {

        public void onAudioSamples (IAudioSamplesEvent event) {

            if (event.getStreamIndex() != mAudioStreamIndex) {
                // if the selected video stream id is not yet set, go ahead an
                // select this lucky video stream
                if (mAudioStreamIndex == -1)
                	mAudioStreamIndex = event.getStreamIndex();
                // no need to show frames from this video stream
                else
                    return;
            }

            //System.out.println("Loop Running");                    
            IAudioSamples samples = event.getAudioSamples();
            writer.encodeAudio(event.getStreamIndex(), samples);
        }
        
    }
	
	public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        
        BufferedImage image;

        // if the source image is already the target type, return the source image
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        }
        // otherwise create a new image of the target type and draw the new image
        else {
            image = new BufferedImage(sourceImage.getWidth(), 
                 sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;
        
    }
}
