import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class StegTest {
	
	public static List<String> pixelList = new ArrayList<String>();
	private static final String inputFilename = "/home/shell/a.mp4";
	private static int mVideoStreamIndex = -1, mAudioStreamIndex = -1;
	private static double frameRate;
	private static long SECONDS_TO_RUN_FOR;
	private static final String outputFilename = "/home/shell/new/b.mp4";
	private static IMediaWriter writer;
	private static long startTime;
	private static int frameCount = 0;
	private static int keyFrameCount = 0;
	
	public static void main(String args[])
	{
		IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		
		IContainer container = IContainer.make();
        // we attempt to open up the container
        int result = container.open(inputFilename, IContainer.Type.READ, null);
        // check if the operation was successful
        if (result<0)
            throw new RuntimeException("Failed to open media file");
		
        IStream stream = container.getStream(0);
        IStream audiostream = container.getStream(1);
        
        System.out.println("Num Streams: " + container.getNumStreams());
        
        IRational fps = stream.getFrameRate();
        frameRate = fps.getDouble();
        SECONDS_TO_RUN_FOR = container.getDuration();
        IStreamCoder coder = stream.getStreamCoder();
        IStreamCoder audioCoder = audiostream.getStreamCoder();
        
        System.out.println("Channel Count: " + audioCoder.getChannels());
        System.out.println("Codec Type: " + coder.getCodecType());
        
        writer = ToolFactory.makeWriter(outputFilename);
        writer.addVideoStream(0, 0, coder.getCodecID() ,coder.getWidth(),coder.getHeight());
        writer.addAudioStream(1, 1, audioCoder.getCodecID(), audioCoder.getChannels(), audioCoder.getSampleRate());
        
        startTime =  System.nanoTime();
        mediaReader.addListener(new AudioSampleListener());
        mediaReader.addListener(new ImageSnapListener());
        while (mediaReader.readPacket() == null) ;
        writer.close();
        container.close();
        Alternate alt = new Alternate();
        System.out.println(alt.alternate(pixelList,8,16));
        System.out.println("Key Frame Count: " + keyFrameCount);
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
            
            if(frameCount % 2 == 0)
            {
            	bgrScreen.setRGB(location.width, location.height, 
            			bgrScreen.getRGB(location.width, location.height) | 0x00FF0000 );
            }
            else
            {
            	bgrScreen.setRGB(location.width, location.height, 
            			bgrScreen.getRGB(location.width, location.height) & 0xFF00FFFF );
            }
            
            frameCount ++ ;
            //System.out.println(Integer.toBinaryString(bgrScreen.getRGB(300, 300)));
            System.out.println(frameCount + ". Pixel Change at: " + location.width + " , " + location.height
            		+ " to --> " + Integer.toBinaryString(bgrScreen.getRGB(location.width, location.height)));
            pixelList.add(Integer.toBinaryString(bgrScreen.getRGB(location.width, location.height)));
            //writer.encodeVideo(0, bgrScreen,System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            writer.encodeVideo(0, bgrScreen,event.getTimeStamp(event.getTimeUnit()), event.getTimeUnit());
            
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
            /*try {
                Thread.sleep((long) (1000 / frameRate));
            } 
            catch (InterruptedException e) {
                // ignore
            } */
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
