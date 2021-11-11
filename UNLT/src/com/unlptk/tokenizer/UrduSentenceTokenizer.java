package com.unlptk.tokenizer;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.unlptk.util.TextOperations;
import com.unlptk.util.UNLPTKConstants;


public class UrduSentenceTokenizer {

	
	
	private int numberOfSentences;
	private String delimiters;	
	private ArrayList<String> docSentences;
	private static String sentenceDelimiter = "(?<=[!؟۔])";
	//public static String sentenceDelimiter = "(!)";
	
	public static void main(String[] args) throws IOException{
	
		
		//Matcher matcher = pattern.matcher());
		
		
		
		
		//List<String> fileTextLines = Files.readAllLines(get("C:\\Users\\Rizwan Iqbal\\Desktop\\thesis_revision\\Testing_Data.txt"), StandardCharsets.UTF_8);	
		List<String> fileTextLines = Files.readAllLines(get("C:\\Users\\Rizwan Iqbal\\Desktop\\thesis_revision\\test.txt"), StandardCharsets.UTF_8);
		String wholeTextOfFile = String.join(" ", fileTextLines);
		String preProcessedText = TextOperations.removeSpecialCharacters(wholeTextOfFile);
		
		UrduSentenceTokenizer ust = new UrduSentenceTokenizer(preProcessedText, sentenceDelimiter);		
		System.out.println(ust.getDocSentences().size());
		TextOperations.writeListToTextFile(UNLPTKConstants.DOC_SENTENCES_OUTPUT_FILE_PATH, ust.getDocSentences());
		System.out.println("File Successfully Written");
	}

	public ArrayList<String> getDocSentences() {
		return docSentences;
	}

	public void setDocSentences(ArrayList<String> docSentences) {
		this.docSentences = docSentences;
	}

	public UrduSentenceTokenizer(String preProcessedText, String sentenceDelimiter2){
		ArrayList<String> sentencesList = new ArrayList<String>(Arrays.asList(preProcessedText.split(sentenceDelimiter2))); 
		this.setDocSentences(sentencesList);
		this.setNumberOfSentences(sentencesList.size());
		this.setDelimiters(sentenceDelimiter2);
	}

	public UrduSentenceTokenizer(String preProcessedText){
		ArrayList<String> sentencesList = new ArrayList<String>(Arrays.asList(preProcessedText.split(sentenceDelimiter))); 
		this.setDocSentences(sentencesList);
		this.setNumberOfSentences(sentencesList.size());
		this.setDelimiters(sentenceDelimiter);
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

}
