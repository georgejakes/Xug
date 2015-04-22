
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;

import java.io.FileNotFoundException;

public class PlayVideo {

    public static void main(String[] args) throws FileNotFoundException {


        IContainer container = IContainer.make();        
        int result = container.open("/home/shell/new/d.mp4", IContainer.Type.READ, null);
        if (result<0)
        {
        	throw new RuntimeException("Failed to open media file");
        }
        IMediaReader mediaReader = ToolFactory.makeReader(container);
        IMediaViewer mediaViewer = ToolFactory.makeViewer(IMediaViewer.Mode.FAST_VIDEO_ONLY, true);

        mediaReader.addListener(mediaViewer);

        while (mediaReader.readPacket() == null) ;

    }
}