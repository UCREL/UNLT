package com.unlptk.tokenizer;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.unlptk.util.TextOperations;


public class SentenceTokenizer {

	private int numberOfSentences;
	private String delimiters;
	private StringTokenizer stringTokenizer;
	private ArrayList<String> docSentences;
	
	//"(?<=[.!?])"
	public static String sentenceDelimiter = "؟۔";
	//public static String sentenceDelimiter = "(?<=[؟۔])";
	public static void main(String[] args) throws IOException{
		
		
		List<String> fileTextLines = Files.readAllLines(get("C:\\Users\\Rizwan Iqbal\\Desktop\\thesis_revision\\Testing_Data.txt"), StandardCharsets.UTF_8);
		
		String wholeTextOfFile = String.join(" ", fileTextLines);
		String preProcessedText = TextOperations.removeSpecialCharacters(wholeTextOfFile);
		
		System.out.println(preProcessedText);		
		SentenceTokenizer st = new SentenceTokenizer();		
		
		//System.out.println(java.util.Arrays.toString(str.split("(?<=[.!?])");
				
		st.makeSentences(preProcessedText, sentenceDelimiter);				
		System.out.println("No. of Sentences : "+ st.getNumberOfSentences());
		st.printSentences();
		
	}

	public ArrayList<String> getDocSentences() {
		return docSentences;
	}

	public void setDocSentences(ArrayList<String> docSentences) {
		this.docSentences = docSentences;
	}

	public SentenceTokenizer(){
		
	}

	public SentenceTokenizer(String preProcessedText){
		//String delimiter = "؟۔";		
		this.makeSentences(preProcessedText, sentenceDelimiter);				
		//System.out.println("No. of Sentences : "+ this.getNumberOfSentences());
		
	}
	
	public int getNumberOfSentences() {
		return numberOfSentences;
	}


	public void setNumberOfSentences(int numberOfSentences) {
		this.numberOfSentences = numberOfSentences;
	}


	public String getDelimiters() {
		return delimiters;
	}


	public void setDelimiters(String delimiters) {
		this.delimiters = delimiters;
	}


	public StringTokenizer getStringTokenizer() {
		return this.stringTokenizer;
	}


	public void setStringTokenizer(StringTokenizer stringTokenizer) {
		this.stringTokenizer = stringTokenizer;
	}


	/**
	 * Print sentences on console
	 * @return nothing 
	 */
	public void printSentences() {
		long noOfSentences=0;
		StringTokenizer tokens = getStringTokenizer();
		
		if (tokens != null){
			System.out.println("Inside If "+tokens.countTokens());
			while (tokens.hasMoreTokens()) {
				System.out.println("after while ");
				 noOfSentences++;
		         System.out.println("Sentenc No. "+ noOfSentences+" : "+tokens.nextToken());
		     }
		}else {
			System.out.println("Error:> Please make sentences before calling printSentences() function");
			System.exit(0);
		}  
		
	}

	/**
	 * Break the text into sentences according to the delimiters
	 * @param preProcessedText Text which is going to be split into sentences
	 * @param delimiters One or more delimiters, which will split text into sentences
	 * @return tokenizer object of StringTokenizer class, which can be used for additional functionality of Java own 
	 * class StringTokenizer.
	 */

	public void makeSentences(String preProcessedText, String delimiters) {
		//StringTokenizer tokenizer = null;
		
		if(preProcessedText !=null & preProcessedText.length()>0 & delimiters !=null & delimiters.length()>0){
			StringTokenizer tokenizer  = new StringTokenizer(preProcessedText,delimiters);
			
			setStringTokenizer(tokenizer);
			setNumberOfSentences(tokenizer.countTokens());
			setDelimiters(delimiters);
			
			//TODO populate list with actual sentences
			ArrayList<String> sentenceList = new ArrayList<>();			
			
			StringTokenizer tokens = getStringTokenizer();
			if (tokens != null){		
				while (tokens.hasMoreTokens()) {					
					sentenceList.add(tokens.nextToken());
			     }
			}
			this.setDocSentences(sentenceList);			
		}else{
			System.out.println("Error:> Please check text or delimiters length must be greater than 0");
			System.exit(0);
		}
		
		
		//return tokenizer;
	}
}
