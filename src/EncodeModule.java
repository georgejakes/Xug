import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;


public class EncodeModule {
	public static int mAudioStreamIndex = -1, mVideoStreamIndex = -1, frameCount = 0, keyFrameCount = 0;
	public static List<String> pixelList = new ArrayList<String>();
	public static IMediaWriter writer;
	public static Resolution resolution;
	public static int limit;
	
	
	public static boolean EncodeVideo(String inputFileName, String outputFileName, String message, int lim)
	{
		int audioStreamBool = -1, videoStreamBool = -1;
        int streamCount = 0;
        limit = lim;
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
        IRational fps = stream.getFrameRate();
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
        System.out.println(keyFrameCount);
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
            ArrayList<Location> selectedPixels = new PSA().psa(limit, resolution);
            
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
            Alternate alt = new Alternate();
            Dimension location = alt.pixelSelector(siz, frameCount);
            Iterator<Location> iter = selectedPixels.iterator();
            while(iter.hasNext())
            {
            	Location l = iter.next();
            	if(frameCount % 2 == 0)
                {
                	bgrScreen.setRGB(l.width, l.height, 
                			bgrScreen.getRGB(l.width, l.height) | 0xFFFFFFFF );
                }
                else
                {
                	bgrScreen.setRGB(l.width, l.height, 
                			bgrScreen.getRGB(l.width, l.height) & 0x0 );
                }
            	System.out.println(frameCount + ". Pixel Change at: " + l.width + " , " + l.height
                		+ " to --> " + Integer.toBinaryString(bgrScreen.getRGB(l.width, l.height)));
                pixelList.add(Integer.toBinaryString(bgrScreen.getRGB(l.width, l.height)));
                //writer.encodeVideo(0, bgrScreen,System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                writer.encodeVideo(0, bgrScreen,event.getTimeStamp(event.getTimeUnit()), event.getTimeUnit());
            }
            
            
            
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
