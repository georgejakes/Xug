import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;


public class DecodeModule {
	
	public static int mAudioStreamIndex = -1, mVideoStreamIndex = -1, frameCount = 0, keyFrameCount = 0;
	public static Resolution resolution;
	public static int limit = 1;
	public static List<String> pixelList = new ArrayList<String>();
	public static int MSBLimit = 8;
	
	public static String DecodeVideo(String inputFileName, int lim, String password)
	{
		IMediaReader mediaReader = ToolFactory.makeReader(inputFileName);
		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		IContainer container = IContainer.make();
		int result = container.open(inputFileName, IContainer.Type.READ, null);
        if (result<0)
        {
        	throw new RuntimeException("Failed to open media file");
        }
        limit = lim;
        IStream stream = container.getStream(0);
        IStreamCoder coder = stream.getStreamCoder();
        resolution = new Resolution(coder.getWidth(), coder.getHeight());
        mediaReader.addListener(new ImageSnapListener());
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
            ArrayList<Location> selectedPixels = new PSA().psa(limit, resolution, "");
            Iterator<Location> iter = selectedPixels.iterator();
            while(iter.hasNext())
            {
            	Location l = iter.next();
            	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
    					Integer.toBinaryString(image.getRGB(l.width, l.height)));
            	pixelList.add(Integer.toBinaryString(image.getRGB(l.width, l.height)));
            }
            
            frameCount++;
        }
        

    }
}
