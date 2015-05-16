import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;


public class ValidateParameters {
	public static boolean EncodeParameterValidate(String coverFile, String message, int messageLimit, int cluster)
	{
		IContainer container = IContainer.make();
		int result = container.open(coverFile, IContainer.Type.READ, null);
		if (result<0)
            return false;
		int numStreams = container.getNumStreams();
		long duration = (long) (container.getDuration()/(1000000.0));
		double fps = 0;
		int messageLength = message.length();
		for (int i=0; i<numStreams; i++) {
            
            // find the stream object
            IStream stream = container.getStream(i);
            
            // get the pre-configured decoder that can decode this stream;
            IStreamCoder coder = stream.getStreamCoder();
            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                fps = coder.getFrameRate().getDouble();
                break;
            }
        }
		double totalFrames = fps * duration;
		int framesForMessage = (int)Math.ceil(((double)messageLength*8)/(double)messageLimit);
//		System.out.println("Total Frames: " + totalFrames);
//		System.out.println("Frames required for single message imprint: " + framesForMessage);
		if(totalFrames >= framesForMessage*cluster)
		{
			return true;
		}
		return false;
		
	}
}
