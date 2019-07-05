package dmx.OpenLyght;

public abstract class Attribute {
	protected Channel c;
	protected int min, max;
	protected String[] args;
	
	public static Attribute getAttribute(String text){
		Attribute c = null;
		String originalArgs[] = text.split("\\s+");
		String command = getText(originalArgs[0]);
		//c = getInternalCommand(command, updateType, checkStoredCommand);
		if(c != null) {
			int n = 0;
			String newArgs[] = new String[n + originalArgs.length - 1];
			for(int i = (1 - n); i < originalArgs.length; i++)
				newArgs[i - (1 - n)] = originalArgs[i];

			c.args = newArgs;
		}
		return c;
	}
	private static String getText(String arg){
		arg = arg.split("@")[0].replaceAll("[^A-Za-z0-9]", "");
		if(arg.length() > 1)
			arg = arg.substring(0, 1).toUpperCase() + arg.substring(1).toLowerCase();
		return arg;
	}
	
	public Channel getChannel(){
		return c;
	}
	public abstract String getName();
}
