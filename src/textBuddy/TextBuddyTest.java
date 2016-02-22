package textBuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.not;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class TextBuddyTest {
	
	private static final String TEST_FILE = "mytestfile.txt";
	private static final String EMPTY_FILE = "emptyfile.txt";
	private static final String DUMMY_FILE = "dummyfile.txt";
	
	private static final String TEST_ADD = "add ";
	private static final String TEST_DELETE = "delete ";
	private static final String TEST_CLEAR = "clear";
	
	private static final String TEST_LINE_1 = "The quick brown fox";
	private static final String TEST_LINE_2 = "jumps over the lazy dog";
	private static final String TEST_LINE_3 = "this is a line";
	private static final String TEST_LINE_4 = "asdfasdfasdf";
	
	private static final String TEST_DISPLAY_OUTPUT = "1. The quick brown fox\n2. jumps over the lazy dog\n3. this is a line\n4. asdfasdfasdf\n";
	private static final String TEST_SEARCH_OUTPUT = "found in mytestfile.txt:\n1. The quick brown fox\n2. jumps over the lazy dog\n";
	private static final String TEST_SORTED_OUTPUT = "mytestfile.txt sorted alphabetically";
	
	private static CommandListener cl = new CommandListener();
	
	@Test
	public void runCommandTest() {
		File testFile = new File(TEST_FILE);
		CommandListener.clear(testFile);
		assertEquals("added to " + TEST_FILE + ": \"" + TEST_LINE_1 + "\"", TextBuddy.runCommand(cl, testFile, TEST_ADD + TEST_LINE_1));
		assertEquals("deleted from " + TEST_FILE + ": \"" + TEST_LINE_1 + "\"", TextBuddy.runCommand(cl, testFile, TEST_DELETE + "1"));
		assertEquals("no line deleted.", TextBuddy.runCommand(cl, testFile, TEST_DELETE + "1"));
		assertEquals("all content deleted from " + TEST_FILE, TextBuddy.runCommand(cl, testFile, TEST_CLEAR));

	}
	
	@Test
	public void addTest() {
		File testFile = new File(TEST_FILE);
		assertEquals("added to " + TEST_FILE + ": \"" + TEST_LINE_1 + "\"", CommandListener.addLine(testFile, TEST_LINE_1));
		assertEquals("added to " + TEST_FILE + ": \"" + TEST_LINE_2 + "\"", CommandListener.addLine(testFile, TEST_LINE_2));
		assertEquals("added to " + TEST_FILE + ": \"" + TEST_LINE_3 + "\"", CommandListener.addLine(testFile, TEST_LINE_3));
		assertEquals("added to " + TEST_FILE + ": \"" + TEST_LINE_4 + "\"", CommandListener.addLine(testFile, TEST_LINE_4));
	}
	
	@Test
	public void deleteTest() {
		File testFile = new File(TEST_FILE);
		try {
			populateFile(testFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("deleted from " + TEST_FILE + ": \"" + TEST_LINE_1 + "\"",CommandListener.deleteLine(testFile, "1"));
		assertEquals("deleted from " + TEST_FILE + ": \"" + TEST_LINE_2 + "\"",CommandListener.deleteLine(testFile, "1"));
		assertEquals("no line deleted.",CommandListener.deleteLine(testFile, "3"));
		assertEquals("deleted from " + TEST_FILE + ": \"" + TEST_LINE_4 + "\"",CommandListener.deleteLine(testFile, "2"));	
	}
	
	@Test
	public void clearTest() {
		File testFile = new File(TEST_FILE);
		File empty = new File(EMPTY_FILE);
		try {
			populateFile(testFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertThat(testFile.length(),not(empty.length()));
		assertEquals("all content deleted from " + TEST_FILE, CommandListener.clear(testFile));
		assertEquals(testFile.length(), empty.length());
		
	}
	
	@Test
	public void displayTest() {
		File testFile = new File(TEST_FILE);
		try {
			populateFile(testFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(TEST_DISPLAY_OUTPUT, CommandListener.display(testFile));
	}
	
	@Test
	public void searchTest() {
		File testFile = new File(TEST_FILE);
		try {
			populateFile(testFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(TEST_SEARCH_OUTPUT, CommandListener.searchKeyword(testFile, "the"));		
	}
	
	@Test
	public void sortTest() throws IOException {
		File testFile = new File(TEST_FILE);
		File sortedFile = new File(DUMMY_FILE);
		try {
			populateFile(testFile);
			populateSortedFile(sortedFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(TEST_SORTED_OUTPUT, CommandListener.sort(testFile));
		assertEquals(CommandListener.collectLines(testFile), CommandListener.collectLines(sortedFile));
	}
	private void populateFile(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.append(TEST_LINE_1 + "\n");
		writer.append(TEST_LINE_2 + "\n");
		writer.append(TEST_LINE_3 + "\n");
		writer.append(TEST_LINE_4 + "\n");
		writer.close();
	}
	
	private void populateSortedFile(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.append(TEST_LINE_4 + "\n");;
		writer.append(TEST_LINE_2 + "\n");
		writer.append(TEST_LINE_1 + "\n");
		writer.append(TEST_LINE_3 + "\n");
		writer.close();
	}
}

