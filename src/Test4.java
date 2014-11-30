

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IVideoPicture;

public class Test4{
    
    public static final double SECONDS_BETWEEN_FRAMES = 10;
    public static int frameCount = 0;
    private static final String inputFilename = "/home/shell/new/b.mp4";
    public static List<String> pixelList = new ArrayList<String>();
    public static int keyFrameCount = 0;
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
        
        mediaReader.addListener(new ImageSnapListener());

        // read out the contents of the media file and
        // dispatch events to the attached listener
        while (mediaReader.readPacket() == null) ;
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
            //image.setRGB(0, 0, image.getRGB(0, 0) & 0xFFFFFFFE);
            System.out.println(frameCount + ". Pixel At " + location.width + "," + location.height + " --> " + 
            					Integer.toBinaryString(image.getRGB(location.width, location.height)));
            pixelList.add(Integer.toBinaryString(image.getRGB(location.width, location.height)));
            frameCount++;
        }
        

    }

}