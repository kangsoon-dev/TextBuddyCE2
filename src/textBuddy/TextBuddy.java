/**
 * This class is a simple text editor as part of the CE2 assignment for CS2103. 
 * The available commands in this program are add, delete, clear, display, search, sort and exit.
 * 
 * This program assumes that the input will be from the keyboard or a redirected text file. 
 * If input comes from a redirected text file, the displayed messages might not look 
 * exactly like the given example below as the commands entered will not be shown.
 * 
 * It is also assumed that the argument passed into the program will be a valid plain
 * text format file. 
 * 
 * The command format is given by the example interaction below:
 * Welcome to TextBuddy. mytextfile.txt is ready for use
 * command: add little brown fox
 * added to mytextfile.txt: "little brown fox"
 * command: display
 * 1. little brown fox
 * command: add jumped over the moon
 * added to mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * 2. jumped over the moon
 * command: delete 2
 * deleted from mytextfile.txt: "jumped over the moon"
 * command: display
 * 1. little brown fox
 * command: clear
 * all content deleted from mytextfile.txt
 * command: display
 * mytextfile.txt is empty
 * command: exit
 * 
 * @author A0131857B Ang Kang Soon
 */
package textBuddy;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TextBuddy {

	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_COMMAND_REQUEST = "command: ";
	private static final String MESSAGE_INVALID_COMMAND_LINE = "%1$s is not a valid command!";
	private static final String MESSAGE_ERROR_OPEN_FILE = "error opening/creating file!";

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String args[]) {
		String fileName = args[0], command, feedback;
		File currentFile = openFile(fileName);

		printWelcomeMessage(args[0]);

		while (true) {
			System.out.print(MESSAGE_COMMAND_REQUEST);
			command = scanner.nextLine();
			CommandListener cl = new CommandListener();
			feedback = runCommand(cl,currentFile, command);
			printMessage(feedback);
		}
	}

	private static void printWelcomeMessage(String fileName) {
		printMessage(String.format(MESSAGE_WELCOME, fileName));
	}

	private static void printMessage(String message) {
		System.out.println(message);
	}

	/**
	 * This operation checks whether target file exists or not, if not new file
	 * with parameter as name is created
	 * 
	 * @param fileName
	 *            is the name of the file to check for
	 * @return File object with specified file name
	 */
	private static File openFile(String fileName) {
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			printMessage(MESSAGE_ERROR_OPEN_FILE);
			e.printStackTrace();
			System.exit(1);
		}
		return file;
	}

	/**
	 * This operation determines whether command line passed follows any of the
	 * supported command types, and performs specified command indicated if
	 * valid
	 * 
	 * @param commandLine
	 *            is the command line to perform
	 * @return feedback to be shown to user
	 */
	public static String runCommand(CommandListener cl, File currentFile, String commandLine) {
		String commandWord = getFirstWord(commandLine);

		switch (commandWord) {
		case "add":
			return CommandListener.addLine(currentFile, removeFirstWord(commandLine));
		// fall-through
		case "delete":
			return CommandListener.deleteLine(currentFile, removeFirstWord(commandLine));
		// fall-through
		case "display":
			if (removeFirstWord(commandLine).equals("")) {
				return CommandListener.display(currentFile);
			}
			// fall-through
		case "clear":
			if (removeFirstWord(commandLine).equals("")) {
				return CommandListener.clear(currentFile);
			}
			// fall-through
		case "search":
			return CommandListener.searchKeyword(currentFile, removeFirstWord(commandLine).trim());
		// fall-through
		case "sort":
			if (removeFirstWord(commandLine).equals("")) {
				return CommandListener.sort(currentFile);
			}
			// fall-through
		case "exit":
			if (removeFirstWord(commandLine).equals("")) {
				System.exit(0);
			}
			// fall-through
		default:
			return (String.format(MESSAGE_INVALID_COMMAND_LINE, commandLine));
		}
	}

	/**
	 * This operation removes the first word from the parameter string
	 * 
	 * @return parameter string without the first word and no whitespace in
	 *         front
	 */
	private static String removeFirstWord(String inputString) {
		return inputString.replace(getFirstWord(inputString), "").trim();
	}

	/**
	 * This operation extracts the first word from the parameter string
	 * 
	 * @return first word of parameter string
	 */
	public static String getFirstWord(String inputString) {
		String[] splitWords = inputString.split("\\s");
		return splitWords[0];
	}
}
