package openLyghtPlugins.DMXUtils.ext;

import java.io.OutputStream;
import java.io.PrintStream;

public class Printer extends PrintStream {
	private Widow widow;
	
	public Printer(OutputStream out, Widow widow) {
		super(out, true);
		this.widow = widow;
	}
	
	@Override
	public void print(String s){
		try {
			widow.log(s);
		} catch (Exception e) {}
		//super.print(s);
	}
	@Override
	public void println(String s){
		s += "\n";
		try {
			widow.log(s);
		} catch (Exception e) {}
		//super.println(s);
	}
}
