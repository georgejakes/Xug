import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
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
	
	public static boolean EncodeVideo(String inputFileName, String outputFileName, String message)
	{
		IMediaReader mediaReader = ToolFactory.makeReader(inputFileName);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		IContainer container = IContainer.make();
        // we attempt to open up the container
        int result = container.open(inputFileName, IContainer.Type.READ, null);
        // check if the operation was successful
        if (result<0)
            throw new RuntimeException("Failed to open media file");
		
        IStream stream = container.getStream(0);
        IStream audiostream = container.getStream(1);
        IRational fps = stream.getFrameRate();
        IStreamCoder coder = stream.getStreamCoder();
        IStreamCoder audioCoder = audiostream.getStreamCoder();
        writer = ToolFactory.makeWriter(outputFileName);
        writer.addVideoStream(0, 0, coder.getCodecID() ,coder.getWidth(),coder.getHeight());
        writer.addAudioStream(1, 1, audioCoder.getCodecID(), audioCoder.getChannels(), audioCoder.getSampleRate());
        mediaReader.addListener(new AudioSampleListener());
        mediaReader.addListener(new ImageSnapListener());
        while (mediaReader.readPacket() == null) ;
        writer.close();
        container.close();
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
