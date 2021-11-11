package com.unlptk.util;
import static java.nio.file.Paths.get;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class FileOperations {

	/**
	 * Read each file in the given directory and put this file in ArrayList. At each element of list, there will be whole text of each file.
	 * If a file has more than one lines of text then this function joins all these lines with space character.
	 * The space character is of URDU language
	 * @param direcotryPath Path of the directory which contains text files.
	 * @param fileReadingFormat Format in which user want to read text e.g. StandardCharsets.UTF_8 
	 * @return ArrayList of text. Each element of this list contains whole text of a file in the directory.
	 * @throws IOException
	 */
	public static ArrayList<String> directoryToList(String direcotryPath, Charset fileReadingFormat) throws IOException{
		ArrayList<String> allFilesText = new ArrayList<String>();
		
		File filesDirectory = new File(direcotryPath);
		File[] listOfAllTextFiles = filesDirectory.listFiles();
		if(listOfAllTextFiles.length > 0){
			for (int i = 0; i < listOfAllTextFiles.length; i++){
				List<String> docTextLines = Files.readAllLines(get(direcotryPath+File.separator+listOfAllTextFiles[i].getName()), fileReadingFormat);
				String joinedText = String.join(" ", docTextLines);
				allFilesText.add(joinedText);	
			}		
		}
		return allFilesText;
	}

	public static void writeTextToFile(String filePath, String text) throws IOException{
		File file = new File(filePath);
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(text);
		bw.close();	
	}
	
	public static void writeTextToFileUsingFileOutputStream(String filePath, String text) throws IOException{
		FileOutputStream fop = null;
		File file;
		//String content = "This is the text content";

		try {

			file = new File(filePath);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = text.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void writeListToTextFile(String filePath, ArrayList<String> stringList) throws IOException{
		File file = new File(filePath);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
		
		String fileString ="";
		PrintWriter pw = new PrintWriter(new File(filePath), "UTF-8");
		
	    for (String token : stringList)
	    	fileString = fileString+token.trim()+"\n";
	    System.out.println(fileString.trim());	   
	    pw.println(fileString);
	    pw.close();
	    
	   

	}
	
	public static void writeListToTextFileUsingFileOutputStream(String filePath, ArrayList<String> stringList) throws IOException{
		
		FileOutputStream fop = null;
		File file;
		String content = "This is the text content";

		try {

			file = new File(filePath);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			String fileString ="";
		    for (String token : stringList)
		    	fileString = fileString+token.trim()+"\n";
			
			
			// get the content in bytes
			byte[] contentInBytes = fileString.trim().getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		
		//InputStream in=ClassLoader.getSystemResourceAsStream(filePath);
		
		//URL resourceUrl = getClass().getResource(path);
		/*File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		
		//OutputStream output = new FileOutputStream(file);
		
		
		
		
		//File file = new File(filePath);

		
		
		
		
		
		
		String fileString ="";
		PrintWriter pw = new PrintWriter(new File(filePath), "UTF-8");
		
	    for (String token : stringList)
	    	fileString = fileString+token.trim()+"\n";
	    System.out.println(fileString.trim());	   
	    pw.println(fileString);
	    pw.close();*/
	    
	   

	}
	
	/**
	 * Reads the given text file and convert it to arraylist. Each line of the file will
	 * be placed on each element of the arraylist.
	 * @param filePath Complete path of the text file including name and extenssion
	 * @param fileReadingFormat formate of the file e.g. StandardCharset.UTF_8
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> fileToArrayList (String filePath, Charset fileReadingFormat ) throws IOException{
		ArrayList<String> docTextLines = (ArrayList<String>) Files.readAllLines(get(filePath), fileReadingFormat);
		return docTextLines;		
	}
	
	public static ArrayList<String> fileToArrayListUsingInputStream (String filePath) throws IOException{
		//ArrayList<String> docTextLines = (ArrayList<String>) Files.readAllLines(get(filePath), fileReadingFormat);
		ArrayList<String> docTextLines = new ArrayList<>();
		
		
		InputStream in=ClassLoader.getSystemResourceAsStream(filePath);
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            //out.append(line);
        	docTextLines.add(line);
        }
        //System.out.println(out.toString());   //Prints the string content read from input stream
        reader.close();	
		
        //System.out.println("Entries Loaded"+docTextLines.size());
        
        //System.exit(0);
        
		return docTextLines;		
	}
	
	
	
	
	
}
