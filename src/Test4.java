

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;

public class Test4{
    
    public static final double SECONDS_BETWEEN_FRAMES = 10;
    public static int frameCount = 0;
    private static final String inputFilename = "/home/shell/new/d.mp4";
    public static List<String> pixelList = new ArrayList<String>();
    public static int keyFrameCount = 0;
    public static int limit = 8; //Set limit manual
    public static Resolution resolution;
    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private static int mVideoStreamIndex = -1;
    
    // Time of last frame write
    private static long mLastPtsWrite = Global.NO_PTS;
    
    public static final long MICRO_SECONDS_BETWEEN_FRAMES = 
        (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

    public static void main(String[] args) {

        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);

        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        IContainer container = IContainer.make();
        int result = container.open(inputFilename, IContainer.Type.READ, null);
        if (result<0)
        {
        	throw new RuntimeException("Failed to open media file");
        }
        IStream stream = container.getStream(0);
        IStreamCoder coder = stream.getStreamCoder();
        resolution = new Resolution(coder.getWidth(), coder.getHeight());
        mediaReader.addListener(new ImageSnapListener());

        // read out the contents of the media file and
        // dispatch events to the attached listener
        while (mediaReader.readPacket() == null) ;
        Alternate alt = new Alternate();
        System.out.println(alt.alternate(pixelList,8,32,limit));
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
                                
            BufferedImage image = event.getImage();
            IVideoPicture vidPic = event.getMediaData();
            if(vidPic.isKeyFrame())
            {
            	keyFrameCount ++ ;
            }
            Dimension siz = new Dimension();
            siz.width = image.getWidth();
            siz.height = image.getHeight();
            Alternate alt = new Alternate();
            Dimension location = alt.pixelSelector(siz, frameCount);
            ArrayList<Location> selectedPixels = new PSA().psa(limit, resolution);
            Iterator<Location> iter = selectedPixels.iterator();
            while(iter.hasNext())
            {
            	Location l = iter.next();
            	System.out.println(frameCount + ". Pixel At " + l.width + "," + l.height + " --> " + 
    					Integer.toBinaryString(image.getRGB(l.width, l.height)));
            	pixelList.add(Integer.toBinaryString(image.getRGB(l.width, l.height)));
            }
            //image.setRGB(0, 0, image.getRGB(0, 0) & 0xFFFFFFFE);
            
            
            frameCount++;
        }
        

    }

}