import java.util.ArrayList;
import java.util.List;


public class ModuleTester {
	public static void main(String args[])
	{
		EncodeModule enc = new EncodeModule();
		enc.EncodeVideo("/home/shell/a.mp4", "/home/shell/new/b.mp4", "");
	}
}
