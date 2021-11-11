package com.unlptk.util;


import static java.nio.file.Paths.get;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;



public class TextOperations {

	public static ArrayList<String> getStringTokens(String text, String delim) {
		ArrayList<String> tokens = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(text, delim);
		while (st.hasMoreElements()) {
			String sequence = (String) st.nextElement();
			tokens.add(sequence);			
		}
		return tokens;
	}
	
	public static List<String> removeStopWords(List<String> listOfStopWords, List<String> textTokensList) throws IOException{        
        ArrayList<String> textTokensWithOutStopWords = new ArrayList<>(textTokensList);
        textTokensWithOutStopWords.removeAll(listOfStopWords);
		return textTokensWithOutStopWords;
	}
	
	public static String removeSpecialCharacters(ArrayList<String> orgFileLines) {
		return orgFileLines.toString().replace("Ã›â€?", "").replace("-", "").replace("  ", " ").replace("\"", "").replace("Ã˜Å’", "");
	}
	
	public static String removeSpecialCharactersFromURDUText(String text) {
		return text.replace("Ã›â€?", "").replace("Û”", "").replace("  ", " ").replace("\"", "").replace("Ã˜Å’", "").replace("ØŒ", "").replace(")", "").replace("(", "").replace("\\", "").replace(":", " ").replace("â€˜", "").replace("â€™", "");
	}
	
	public static String urduTextPreProcessing(String text) {
		String tempText=text.replaceAll(",", "");
		return tempText.replace("’’", " ’’ ").replace("‘‘", " ‘‘ ").replace(" ویں", "ویں").replace("،"," ، ").replace("۔", " ۔ ").replace("،", " ، ").replace("،", " ،  ").replace("‘"," ‘ ").replace("\"", " \" ").replace("\\", " \\ ").replace("/", " / ").replace("@", " @ ").replace("+", " + ").replace("”", " ” ").replace("“", " “ ").replace(":"," : ").replace("(", " ( ").replace(")", " ) ").replaceAll("\\s+"," ");
	}
	
	/*public static long getNoOfUniqueWords(ArrayList<TextGrams> gramsList){
		
		Set<String> uniqueWordsSet = new HashSet<String>();
		for(TextGrams tg : gramsList){
			uniqueWordsSet.add(tg.getText());
		}
		
		return uniqueWordsSet.size();	
	}*/
	
	/**
	 * This function breaks the text on the basis of given delimeter and returns number of words in the given text.
	 * @param text
	 * @param delimeter
	 * @return Number of Words in given text based on delimeter
	 */
	public static long getNoOfWords(String text, String delimeter){			
		return text.split(delimeter).length;	
	}
	
	
	public static long getNoOfSentences(String text, String delimeter){			
		return text.split(delimeter).length;	
	}
	
	public static void writeToTextFile(String filePath, String text) throws IOException{
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
	
	public static String readTextFromFile(String fileName) {
		String fileText=null;
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			fileText = br.readLine();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileText;
	}
	
	public static void writeListToTextFile(String filePath, ArrayList<String> stringList) throws IOException{
		File file = new File(filePath);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		
		String fileTextToWrite ="";		 
		for(String str: stringList) {
			fileTextToWrite = fileTextToWrite+str.trim()+"\n";
		  
		}
		FileWriter writer = new FileWriter(filePath);
		writer.write(fileTextToWrite.trim());		  
		writer.close();
		

	}
	


	public static ArrayList<String> getStringTokens(String orgPreProcessedText) {
		return new ArrayList<String>(Arrays.asList(orgPreProcessedText.split(" ")));
	}
	
	public static String removeSpecialCharacters(List<String> orgFileLines) {
		return orgFileLines.toString().replace("Ã›â€?", "").replace("-", "").replace("  ", " ").replace("\"", "").replace("Ã˜Å’", "");
	}



	public static String removeSpecialCharacters(String text) {
		return text.replace("Ã›â€?", "").replace("-", "").replace("Û”", " ").replace("  ", " ").replace("   ", " ").replace("	", " ").replace("\"", "").replace("Ã˜Å’", "");
	}

	public static void generateAndWriteTokensToFile(String srcDir,
			String targetDir) throws IOException {
		String pathOfOriginalTextDirectory = srcDir;
		File originalTextDirectory = new File(pathOfOriginalTextDirectory);
		File[] listOfOriginalTextFiles = originalTextDirectory.listFiles();
		if(listOfOriginalTextFiles.length > 0){
			for (int i = 0; i < listOfOriginalTextFiles.length; i++){
				List<String> docTextLines = Files.readAllLines(get(srcDir+File.separator+listOfOriginalTextFiles[i].getName()), StandardCharsets.UTF_8);
				System.out.println("No. of Lines : "+docTextLines.size());
				String joinedText = String.join(" ", docTextLines);    				
				String preProcessedText = removeSpecialCharacters(joinedText);
				//Files.write(get(targetDir+File.separator+listOfOriginalTextFiles[i].getName()), docTextLines.toString().trim().getBytes());
				//TextOperations.saveOutput(preProcessedText, targetDir+File.separator+listOfOriginalTextFiles[i].getName());
				//System.exit(0);
				
				ArrayList<String> fileTokens = getStringTokens(preProcessedText);
				TextOperations.writeListToTextFile(targetDir+File.separator+listOfOriginalTextFiles[i].getName(), fileTokens);
				
			}
			
			System.out.println("Directory Tokenized");
		}
	}
	
	public static ArrayList<Integer> getWordsHashCodes(ArrayList<String> wordsList) {
		ArrayList<Integer> codes = new ArrayList<Integer>();
		for(String word : wordsList){
			codes.add(word.hashCode());
		}		
		return codes;
	}
	
	public static ArrayList<String> getTokensOfLength2(ArrayList<String> tokens){
		ArrayList<String> wordsListOfLength2 = new ArrayList<String>();
		
		String[] singleWordArray = new String[tokens.size()];		
		singleWordArray = (String[]) tokens.toArray(singleWordArray);
		
		int sequence = 0;
		//System.out.println("Single Words List : "+ singleWordArray);
		while (sequence < 2) {
			for (int i = sequence; i < singleWordArray.length; i = i + 2) {
				int j = i + 1;
				if (j < singleWordArray.length) {
					wordsListOfLength2.add(singleWordArray[i] + " " + singleWordArray[j]);
					//System.out.println("\n Double Words = " + singleWordArray[i] + " "+ singleWordArray[j] + "\n");
				}

			}
			
			//System.out.println("Double Words List Size : "+ wordsListOfLength2.size());
			sequence++;
		}
		return wordsListOfLength2;
	}
	
	public static ArrayList<String> getTokensOfLength3(ArrayList<String> tokens){
		ArrayList<String> wordsListOfLength3 = new ArrayList<String>();
		
		String[] trippleWordArray = new String[tokens.size()];		
		trippleWordArray = (String[]) tokens.toArray(trippleWordArray);
		
		int sequence = 0;
		while (sequence < 3) {
			for (int i = sequence; i < trippleWordArray.length; i = i + 3) {
				int j = i + 1;
				int k = i + 2;
				if (j < trippleWordArray.length && k < trippleWordArray.length) {
					wordsListOfLength3.add(trippleWordArray[i] + " " + trippleWordArray[j] + " " + trippleWordArray[k]);
					//System.out.println("\n Tripple Words = " + trippleWordArray[i] + " "+ trippleWordArray[j] + " " + trippleWordArray[k] + "\n");
				}

			}

			sequence++;
		}				
		return wordsListOfLength3;
	}
	
	public static ArrayList<String> getTokensOfLength4(ArrayList<String> tokens){
		ArrayList<String> wordsListOfLength4 = new ArrayList<String>();
		
		String[] fourWordArray = new String[tokens.size()];		
		fourWordArray = (String[]) tokens.toArray(fourWordArray);
		
		int sequence = 0;
		while (sequence < 4) {
			for (int i = sequence; i < fourWordArray.length; i = i + 4) {
				int j = i + 1;
				int k = i + 2;
				int l = i + 3;
				if (j < fourWordArray.length && k < fourWordArray.length && l < fourWordArray.length) {
					wordsListOfLength4.add(fourWordArray[i] + " " + fourWordArray[j] + " " + fourWordArray[k] + " "
							+ fourWordArray[l]);

					//System.out.println("\n Fourth Words = " + fourWordArray[i] + " "+ fourWordArray[j] + " " + fourWordArray[k] + " " + fourWordArray[l] + "\n");
				} else if (j < fourWordArray.length && k < fourWordArray.length
						&& l > fourWordArray.length) {
					wordsListOfLength4.add(fourWordArray[i] + " " + fourWordArray[j] + " " + fourWordArray[k]);

					//System.out.println("\n Fourth Words = " + fourWordArray[i] + " "+ fourWordArray[j] + " " + fourWordArray[k] + "\n");
				}

			}

			sequence++;
		}
		
			
		return wordsListOfLength4;
	}
	
	
	public static ArrayList<String> searchWordsFromDictionary(ArrayList<String> tokens, ArrayList<Integer> dictionaryInHashCodes){
		ArrayList<String> founded_words = new ArrayList<String>();

		for (int i = 0; i < tokens.size(); i++) {
			for (int j = 0; j < dictionaryInHashCodes.size(); j++) {
				if (tokens.get(i).hashCode() == dictionaryInHashCodes.get(j)) {
					founded_words.add(tokens.get(i));
					//System.out.println("\nSuccessfully founded words = "+ tokens.get(i) + "\n");

				}

			}

		}

		return founded_words;
	}
	
	/**
	 * This method generate tokens of the whole text i.e. It receives 
	 * @param preProcessedText This is the text after removing all punctuation marks from urdu text
	 * @param hashCodesOfDictonary This the list of hashcodes of words in dictionary.
	 * @param tokens This is the list of tokens generated by default delimeter i.e. space delimeter
	 * @return Returns a list of tokens of the given text. These tokens will used for further processing of text.
	 */
	public static ArrayList<String> generateTokensFromList(
			String preProcessedText, ArrayList<Integer> hashCodesOfDictonary,
			ArrayList<String> tokens) {
		//TODO Getting tokens of length 2 and then searching these tokens into dictionary
		ArrayList<String> tokensOfLenght2 = TextOperations.getTokensOfLength2(tokens);		
		ArrayList<String> foundedWordsOfLength2 = TextOperations.searchWordsFromDictionary(tokensOfLenght2, hashCodesOfDictonary);
		
		//TODO Getting tokens of length 3 and then searching these tokens into dictionary
		ArrayList<String> tokensOfLenght3 = TextOperations.getTokensOfLength2(tokens);		
		ArrayList<String> foundedWordsOfLength3 = TextOperations.searchWordsFromDictionary(tokensOfLenght3, hashCodesOfDictonary);
		
		//TODO Getting tokens of length 4 and then searching these tokens into dictionary
		ArrayList<String> tokensOfLenght4 = TextOperations.getTokensOfLength2(tokens);		
		ArrayList<String> foundedWordsOfLength4 = TextOperations.searchWordsFromDictionary(tokensOfLenght4, hashCodesOfDictonary);
		
		
		//TODO Now replacing all founded words of length 2 or more with the original text.
		String replacedText  = replaceWords(preProcessedText, foundedWordsOfLength2, foundedWordsOfLength3, foundedWordsOfLength4);
		//System.out.println("Replaced Text : "+replacedText);
		//TODO Getting final tokens list which will be used for further processing
		ArrayList<String> finalTokens = TextOperations.getStringTokens(replacedText, "|");
		//System.out.println("Final Tokens List : "+finalTokens);
		return finalTokens;
	}
	
	public static String replaceWords(String preProcessedText, ArrayList<String> foundedWordsOfLength2, ArrayList<String> foundedWordsOfLength3, ArrayList<String> foundedWordsOfLength4) {
		String tokenizedText = preProcessedText.replaceAll(" ", "|");
		
		//TODO Matching of length 4 words
		for (int i = 0; i < foundedWordsOfLength4.size(); i++) {
			String w = foundedWordsOfLength4.get(i).replaceAll(" ", "|");
			tokenizedText = tokenizedText.replace(w, foundedWordsOfLength4.get(i));			
		}
		
		//TODO Matching of length 3 words
		for (int i = 0; i < foundedWordsOfLength3.size(); i++) {
			String w = foundedWordsOfLength3.get(i).replaceAll(" ", "|");
			tokenizedText = tokenizedText.replace(w, foundedWordsOfLength3.get(i));			
		}	
		
		//TODO Matching of length 2 words
		for (int i = 0; i < foundedWordsOfLength2.size(); i++) {
			String w = foundedWordsOfLength2.get(i).replaceAll(" ", "|");			
			tokenizedText = tokenizedText.replace(w, foundedWordsOfLength2.get(i));			
		}
		
		return tokenizedText;
	}

	/**
	 * 
	 * @param sourceText
	 * @param suspeciousText
	 * @return
	 */
	public static ArrayList<String> longestCommonSubsequence(ArrayList<String> sourceText,ArrayList<String> suspeciousText) {

		String[] text1Words = sourceText.toArray(new String[sourceText.size()]);
		String[] text2Words = suspeciousText.toArray(new String[suspeciousText.size()]);
		
		int text1WordCount = text1Words.length;

		int text2WordCount = text2Words.length;

		int[][] solutionMatrix = new int[text1WordCount + 1][text2WordCount + 1];

		for (int i = text1WordCount - 1; i >= 0; i--) {

			for (int j = text2WordCount - 1; j >= 0; j--) {

				if (text1Words[i].equals(text2Words[j])) {

					solutionMatrix[i][j] = solutionMatrix[i + 1][j + 1] + 1;

				}

				else {

					solutionMatrix[i][j] = Math.max(solutionMatrix[i + 1][j],

					solutionMatrix[i][j + 1]);

				}

			}

		}

		int i = 0, j = 0;

		ArrayList<String> lcsResultList = new ArrayList<String>();

		while (i < text1WordCount && j < text2WordCount) {

			if (text1Words[i].equals(text2Words[j])) {

				lcsResultList.add(text2Words[j]);

				i++;

				j++;

			}

			else if (solutionMatrix[i + 1][j] >= solutionMatrix[i][j + 1]) {

				i++;

			}

			else {

				j++;

			}

		}

		return lcsResultList;

	}
	
	
	/**
	 * Compute NGrams of the given text
	 * @param tlokens This is the text which will be divided into grams
	 * @param noOfNGrams No of NGrams e.g. 1,2,3........
	 * @return ArrayList At each index of this array there will be a gram
	 */
	public static ArrayList<String> getNGrams(List<String> tokens, int noOfNGrams) {
		
		if (noOfNGrams < 1 || tokens == null || tokens.size() == 0)
			throw new IllegalArgumentException(
					"Check the input Parameters : String Should not be null or empty and Ngram value should be > 1");
		
		
		String[] parts = tokens.toArray(new String[tokens.size()]);
		String[] nGrams = new String[parts.length - noOfNGrams + 1];
		
		for (int i = 0; i < parts.length - noOfNGrams + 1; i++) {
			StringBuilder sb = new StringBuilder();
			for (int k = 0; k < noOfNGrams; k++) {
				if (k > 0)
					sb.append(' ');
				sb.append(parts[i + k].trim());
				
			}
			nGrams[i] = sb.toString().trim();
		}
		
		
		return new ArrayList<String>(Arrays.asList(nGrams));
	}
	
	
	


	
}
