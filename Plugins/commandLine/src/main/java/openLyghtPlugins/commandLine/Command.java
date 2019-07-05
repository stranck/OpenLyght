package openLyghtPlugins.commandLine;

public abstract class Command {
	private static final String COMMANDS_PACKAGE = "openLyghtPlugins.commandLine.commands."; //Package in cui sono contenute le classi dei comandi

	protected String[] args; //Argomenti del comando
	/**
	 * Metodo da estendere nelle classi comando
	 * @param packet	Pacchetto con cui il messaggio è arrivato (Contiene informazioni sul client)
	 * @param data		Stringa contenente il pacchetto originale (Comando + payload)
	 * @return Stringa contenente una descrizione sul risultato dell'esecuzione del comando
	 */
	protected abstract String execCommand(String fullCommand);
	/**
	 * Interfaccia esterna da chiamare per eseguire i comandi.
	 * Eventuali conversioni, controlli e, in generale, operazione da eseguire in anticipo per tutti i comandi vanno fatte qui
	 * (Come ad esempio convertire il buffer in stringa)
	 * @param packet	Pacchetto con cui il messaggio è arrivato (Contiene informazioni sul client)
	 * @param data		Stringa contenente il pacchetto originale (Comando + payload)
	 * @return Stringa contenente una descrizione sul risultato dell'esecuzione del comando
	 */
	public final String execute(String fullCommand){
		return execCommand(fullCommand); //Esegue effettivamente il comando, chiamando execCommand() nelle classi dei comandi
	}

	private static String previousText = "", previousCmd;
	private static final char PREVIOUS_TEXT_TRIGGER = '.';
	private static final char PREVIOUS_CMD_TRIGGER = ',';
	/**
	 * Ritorna l'oggetto comando con gli argomenti impostati, da una stringa data (Le classi dei comandi sono in clientServerUDP.commands)
	 * Esempio Stringa:
	 * 			  v Argomenti del comando separati da spazi. Ogni "parola" viene messa in una posizione diversa dell'array
	 * 		Send ciao come stai?
	 * 		 ^ Comando. è il nome della classe comando da instanziare
	 * @param text	Stringa contenente comando + payload con cui si vuole invocare il comando
	 * @return	L'oggetto comando instanziato con argomenti impostati
	 */
	public static Command getCommand(String text){
		Command c = null;
		if(text.charAt(0) == PREVIOUS_TEXT_TRIGGER)
			text = previousText;
		else
			previousText = text;
		String originalArgs[] = text.split("\\s+"); //Divide la stringa in argomenti separati da spazi
		if(text.charAt(0) == PREVIOUS_CMD_TRIGGER)
			originalArgs[0] = previousCmd;
		else
			previousCmd = originalArgs[0];
		String command = getText(originalArgs[0]); //Si ottiene il nome del comando da invocare
		//System.out.println("ASDASDASD\t" + text + "\t" + command);
		//if(text.equals("")) {int asd = 1/0;}
		c = getInternalCommand(command); //Ottiene l'oggetto comando dal suo nome
		if(c != null) { //Se il comando è stato trovato:
			String newArgs[] = new String[originalArgs.length - 1];
			for(int i = 1; i < originalArgs.length; i++)
				newArgs[i - 1] = originalArgs[i];

			c.args = newArgs; //Assegna gli argomenti, rimuovendo la prima posizione dell'array (In origine occupata dal nome del comando)
		}// else Main.LOGGER.warning(command + ": Command not found");
		return c;
	}
	
	/**
	 * Crea e restituisce un'istanza del comando in base al nome, usando java reflection
	 * @param command	Il nome del comando
	 * @return	L'istanza dell'oggetto Comando
	 */
	private static Command getInternalCommand(String command){
		Command c = null;
		try {
			c = (Command) Class.forName(COMMANDS_PACKAGE + command).newInstance();
		} catch (Exception e){
			e.printStackTrace();
		}
		return c;
	}
	
	/**
	 * Ritorna il nome del comando, scritto in modo da rispettare le convenzioni java dei nomi delle classi
	 * (Ovvero, niente caratteri speciali, prima lettera maiuscola e le altre minuscole)
	 * @param arg	Nome da convertire
	 * @return	Stringa senza caratteri speciali, formattata in modo da rispettare le convenzioni java dei nomi delle classi
	 */
	private static String getText(String arg){
		arg = arg.replaceAll("[^A-Za-z0-9]", ""); //Rimuove tutti i caratteri non alfanumerici
		if(arg.length() > 1)
			arg = arg.substring(0, 1).toUpperCase() + arg.substring(1).toLowerCase(); //Corregge eventuali caps che non rispettano le convenzioni
		return arg;
	}
	
	/*
	 * NON RIMUOVERE GRAZIE CIAO VI VOGLIO BENE
	 * ThE game
	 */
	@SuppressWarnings("unused")
	private static void copyrightTM(){
		System.out.println("Command engine made by Stranck (c)"
						 + ""
						 + "Need help? Contact me!"
						 + "Telegram channel: https://t.me/Stranck"
						 + "Telegram user: https://t.me/LucaStranck"
						 + "YouTube channel: https://www.youtube.com/channel/UCmMWUz0QZ7WhIBx-1Dz-IGg"
						 + "Twitter: https://twitter.com/LStranck https://twitter.com/stranckV2"
						 + "Instagram: https://www.instagram.com/stranck/"
						 + "Github: https://github.com/stranck"
						 + "Pastebin: https://pastebin.com/u/Stranck");
	}
}
