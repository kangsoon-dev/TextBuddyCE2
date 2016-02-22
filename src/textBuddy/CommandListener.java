package textBuddy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

public class CommandListener {
	
	private static final String MESSAGE_INVALID_LINE = "line %1$s does not exist! ";
	private static final String MESSAGE_UNSUCCESSFUL_DELETE = "no line deleted.";
	private static final String MESSAGE_INVALID_NUMBER_FORMAT = "\"%1$s\" is not a valid number!";
	private static final String MESSAGE_ADD_LINE = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEAR_FILE = "all content deleted from %1$s";
	private static final String MESSAGE_SEARCH_FILE_SUCCESS = "found in %1$s:";
	private static final String MESSAGE_SEARCH_FILE_FAIL = "\"%1$s\" not found!";
	private static final String MESSAGE_SORT_FILE = "Sorted alphabetically: \"%1$s\"";
	private static final String MESSAGE_ERROR_WRITE_TO_FILE = "error writing to file!";
	private static final String MESSAGE_ERROR_DISPLAY_FILE = "error, file to display not found!";
	private static final String MESSAGE_ERROR_DELETE_LINE = "error deleting line!";
	private static final String MESSAGE_ERROR_CLEAR_FILE = "error clearing file!";
	private static final String MESSAGE_ERROR_SEARCH_FILE = "error searching file!";
	private static final String MESSAGE_ERROR_SORT_FILE = "error sorting file!";
	
	private static final int emptyFileIndicator = 0;

	private static void printMessage(String message) {
		System.out.println(message);
	}
	/**
	 * This operation searches for a particular keyword in the text file
	 * 
	 * @param searchKey
	 *            is the keyword to search for
	 * @return feedback whether the search is successful or not
	 */
	public static String searchKeyword(File file, String searchKey) {
		ArrayList<String> lines;
		StringBuilder feedbackString = new StringBuilder();
		try {
			lines = collectLines(file);
			Vector<String> searchResult = new Vector<String>();
			boolean found = false;
			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).toLowerCase().contains(searchKey.toLowerCase())) {
					found = true;
					searchResult.addElement(lines.get(i));
					System.out.println("test" + lines.get(i));
				}
			}
			if(found) {
				feedbackString.append(String.format(MESSAGE_SEARCH_FILE_SUCCESS, file.getName()) + "\n");
				while (!searchResult.isEmpty()) {
					feedbackString.append(searchResult.firstElement() + "\n");
					searchResult.remove(searchResult.firstElement());
				}
			} else {
				feedbackString.append(String.format(MESSAGE_SEARCH_FILE_FAIL + "\n", searchKey));
			}
		} catch (FileNotFoundException e) {
			printMessage(MESSAGE_ERROR_SEARCH_FILE);
			e.printStackTrace();
		}

		return feedbackString.toString();

	}

	/**
	 * This operation performs the sort function
	 * 
	 * @return feedback on sort
	 */
	public static String sort(File file) {
		ArrayList<String> lines;
		try {
			lines = collectLines(file);
			Collections.sort(lines, String.CASE_INSENSITIVE_ORDER);
			clear(file);
			for (int i = 0; i < lines.size(); i++) {
				addLine(file, lines.get(i));
			}
		} catch (FileNotFoundException e) {
			printMessage(MESSAGE_ERROR_SORT_FILE);
			e.printStackTrace();
		}
		return String.format(MESSAGE_SORT_FILE, file.getName());
	}

	/**
	 * This operation collects all lines of text in a file
	 * 
	 * @param file
	 * 			is the file to collect text lines from
	 * @return list of lines from file
	 */
	public static ArrayList<String> collectLines(File file) throws FileNotFoundException {
		ArrayList<String> lines;
		Scanner sc;
		lines = new ArrayList<String>(numberOfLines(file));
		sc = new Scanner(file);
		while (sc.hasNext()) {
			lines.add(sc.nextLine());
		}
		sc.close();
		return lines;
	}

	private static int numberOfLines(File file) throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		int counter = 0;
		while (sc.hasNextLine()) {
			sc.nextLine();
			counter++;
		}
		sc.close();
		return counter;
	}

	/**
	 * This operation performs the add command
	 * 
	 * @return feedback to be shown to user
	 */
	public static String addLine(File file, String inputText) {
		writeToFile(file, inputText);
		return (String.format(MESSAGE_ADD_LINE, file.getName(), inputText));
	}

	/**
	 * This operation appends parameter string to the file as a single newline.
	 * 
	 * @param inputText
	 *            is the text to be appended to the file
	 * 
	 * @return feedback for successful add execution to be shown to user
	 */
	private static void writeToFile(File file, String inputText) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			if (!isEmpty(file)) {
				writer.newLine();
			}
			writer.append(inputText);
			writer.close();
		} catch (IOException e) {
			printMessage(MESSAGE_ERROR_WRITE_TO_FILE);
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static boolean isEmpty(File file) {
		return file.length() <= emptyFileIndicator;
	}

	/**
	 * This operation builds the string to be shown to the user which contains
	 * the text file content
	 * 
	 * @return feedback to be shown to user
	 */
	public static String display(File file) {
		StringBuilder feedbackString = new StringBuilder();
		int i = 0;

		if (isEmpty(file)) {
			return (file.getName() + " is empty");
		} else {
			try {
				Scanner sc = new Scanner(file);
				while (sc.hasNext()) {
					i++;
					feedbackString.append(i + ". " + sc.nextLine() + "\n");
				}
				sc.close();
			} catch (FileNotFoundException e) {
				printMessage(MESSAGE_ERROR_DISPLAY_FILE);
			}
		}
		return feedbackString.toString();
	}

	/**
	 * This operation verifies whether parameter is a valid integer, and
	 * performs removing of line
	 * 
	 * @return feedback of successful/failed delete execution to be shown to
	 *         user
	 */
	public static String deleteLine(File file, String lineNumber) {
		String deletedString = "";
		try {
			int lineNumberInt = Integer.parseInt(lineNumber);
			deletedString = removeLine(file, lineNumberInt);

			// returns empty string if no string is deleted in removeLine
			// function
			if (deletedString == null) {
				return (MESSAGE_UNSUCCESSFUL_DELETE);
			}

		} catch (NumberFormatException e) {
			return (String.format(MESSAGE_INVALID_NUMBER_FORMAT, lineNumber));
		}
		return ("deleted from " + file.getName() + ": \"" + deletedString + "\"");
	}

	/**
	 * This operation removes text line by line number if indicated line is
	 * found in the file
	 * 
	 * @return feedback of successful/failed line deletion to be shown to user
	 */
	private static String removeLine(File file, int lineToDelete) {
		String deletedString = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Vector<String> linesNotDeleted = new Vector<String>();
			int j = 0;
			boolean lineIsFound = false;

			String currentLine = reader.readLine();
			while (currentLine != null) {
				j++;
				if (j == lineToDelete) {
					deletedString = currentLine;
					lineIsFound = true;
				} else {
					linesNotDeleted.add(currentLine);
				}
				currentLine = reader.readLine();
			}
			reader.close();

			if (lineIsFound) {
				BufferedWriter writer = writeToFile(file, linesNotDeleted, j);
				writer.close();
			} else {
				System.out.print(String.format(MESSAGE_INVALID_LINE, lineToDelete));
			}
		} catch (Exception e) {
			printMessage(MESSAGE_ERROR_DELETE_LINE);
			e.printStackTrace();
		}
		// null value is returned if no line was deleted from file
		return deletedString;
	}

	private static BufferedWriter writeToFile(File file, Vector<String> linesNotDeleted, int j) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
		for (int k = 0; k < j - 1; k++) {
			writer.write(linesNotDeleted.elementAt(k) + "\n");
		}
		return writer;
	}

	/**
	 * This operation performs the clear command
	 * 
	 * @return feedback of successful clear execution to be shown to user
	 */
	public static String clear(File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName(), false));
			writer.write("");
			writer.close();

		} catch (IOException e) {
			printMessage(MESSAGE_ERROR_CLEAR_FILE);
			e.printStackTrace();
			return ("");
		}
		return (String.format(MESSAGE_CLEAR_FILE, file.getName()));
	}
}
