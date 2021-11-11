package com.unlptk.tokenizer;

import static java.nio.file.Paths.get;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.unlptk.gst.GreedyStringTiling;
import com.unlptk.util.FileOperations;
import com.unlptk.util.ParseTextPair;
import com.unlptk.util.TextOperations;
import com.unlptk.util.UNLPTKConstants;

public class WordTokenizer {
	
	

	public static void main(String[] args) throws IOException {
		
		//TODO Load Multi Word Dictionary, Stop Words list Here. It will always be first statement of your application.
		UNLPTKConstants.MULTI_WORD_DICTIONARY = FileOperations.fileToArrayList(UNLPTKConstants.MULTI_WORD_DICTIONARY_PATH, StandardCharsets.UTF_8);
		UNLPTKConstants.MULTI_WORD_DICTIONARY_HASH_CODES = TextOperations.getWordsHashCodes(UNLPTKConstants.MULTI_WORD_DICTIONARY);
		UNLPTKConstants.URDU_STOP_WORDS_LIST = FileOperations.fileToArrayList(UNLPTKConstants.URDU_STOP_WORDS_LIST_PATH, StandardCharsets.UTF_8);
		UNLPTKConstants.URDU_STOP_WORDS_LIST_HASH_CODES = TextOperations.getWordsHashCodes(UNLPTKConstants.URDU_STOP_WORDS_LIST);		
		//
		
		// ****************************Hafiz Rizwan Code for Annotations File to Tokenized Files // here*********************************//
	/*	ArrayList<String> annotatedPairList = FileOperations.fileToArrayList(UNLPTKConstants.CORPUS_ANNOTATION_FILE, StandardCharsets.UTF_8);
		UNLPTKConstants.ANNOTATED_FILE_PAIR_SPLITTER=",";
		ArrayList<ParseTextPair> parsedTextPairsList = getParsedTextPairs(annotatedPairList, UNLPTKConstants.ANNOTATED_FILE_PAIR_SPLITTER,  UNLPTKConstants.CORPUS_SOURCE_DIR, StandardCharsets.UTF_8);*/
		
		
		//for(ParseTextPair pt : parsedTextPairsList){
			//System.out.println(pt.getLcsSimilarityScore());
			//System.out.println(pt.getKeyWordsLCSSimilarityScore());
			
			//TODO This code generates tokenized corpus directory.
			/*ArrayList<String> srcTokens = pt.getTokensOfSourceText();
			ArrayList<String> susTokens = pt.getTokensOfSuspeciousText();			
			String srcFilePath = UNLPTKConstants.TOKENIZED_CORPUS_DIR+File.separator+pt.getSourceFile();
			String susFilePath = UNLPTKConstants.TOKENIZED_CORPUS_DIR+File.separator+pt.getSuspeciousFile();			
			TextOperations.writeListToTextFile(srcFilePath, srcTokens);
			TextOperations.writeListToTextFile(susFilePath, susTokens);*/
			
		//}
		
		//System.exit(0);
		
		
		//TODO Generate Arff file of NGram Sim Scores		
		/*String plagDetectionTechnique = UNLPTKConstants.KEYWORDS_BASED_NGRAM; //i.e. KeyWords_based_ngram, Simple_based_ngram		
		String simMeasureTechnique = UNLPTKConstants.JACCARD; //i.e. Jaccard, Dice, Containment, Overlap		
		writeNGramARFF(parsedTextPairsList, simMeasureTechnique, plagDetectionTechnique);
		simMeasureTechnique=UNLPTKConstants.DICE;		
		writeNGramARFF(parsedTextPairsList,  simMeasureTechnique, plagDetectionTechnique);
		simMeasureTechnique=UNLPTKConstants.CONTAINMENT;		
		writeNGramARFF(parsedTextPairsList,  simMeasureTechnique, plagDetectionTechnique);
		simMeasureTechnique=UNLPTKConstants.OVERLAP;		
		writeNGramARFF(parsedTextPairsList,  simMeasureTechnique, plagDetectionTechnique);*/
	
		
		//TODO Generate Arff file of single attributed similarity scores
		/*String plagDetectionTechnique=UNLPTKConstants.KEYWORDS_BASED_LCS;
		writeSingleAttributeARFF(parsedTextPairsList, plagDetectionTechnique);
		plagDetectionTechnique=UNLPTKConstants.SIMPLE_LCS;
		writeSingleAttributeARFF(parsedTextPairsList, plagDetectionTechnique);*/
		/*String plagDetectionTechnique=UNLPTKConstants.SENTENCE_RATIO;
		writeSingleAttributeARFF(parsedTextPairsList, plagDetectionTechnique);
		plagDetectionTechnique=UNLPTKConstants.TOKENS_RATIO;
		writeSingleAttributeARFF(parsedTextPairsList, plagDetectionTechnique);
		plagDetectionTechnique=UNLPTKConstants.TYPE_TOKENS_RATIO;
		writeSingleAttributeARFF(parsedTextPairsList, plagDetectionTechnique);*/
	
		/*String plagDetectionTechnique=UNLPTKConstants.GREEDY_STRING_TILING;
		writeSingleAttributeARFF(parsedTextPairsList, plagDetectionTechnique);
		
		
		System.exit(0);*/
		
		// ****************************Hafiz Rizwan Code Starting from // here*********************************/
		ArrayList<String> textOfAllFiles = FileOperations.directoryToList(UNLPTKConstants.CORPUS_SOURCE_DIR, StandardCharsets.UTF_8);
		String concatenatedTextOfAllFiles = String.join(" ", textOfAllFiles);
		String preProcessedText = TextOperations.urduTextPreProcessing(concatenatedTextOfAllFiles);
		
		ArrayList<String> multiWords = FileOperations.fileToArrayList(UNLPTKConstants.MULTI_WORD_DICTIONARY_PATH, StandardCharsets.UTF_8);
		ArrayList<Integer> hashCodesOfDictonary = TextOperations.getWordsHashCodes(multiWords);
		
		//TODO Getting default tokens of length 1. This will be the input of all greater length tokens
		ArrayList<String> tokens = TextOperations.getStringTokens(preProcessedText, UNLPTKConstants.DEFAULT_WORDS_SEPERATOR);		
		
		//TODO Generating finalized tokens of text.
		ArrayList<String> finalTokens = generateTokensFromList(preProcessedText, hashCodesOfDictonary, tokens);
		
		System.out.println("Tokens Generated");
		
		//TODO Writing final tokens list to text file
		TextOperations.writeListToTextFile(UNLPTKConstants.OUTPUT_FILE+".txt", finalTokens);
		
		System.out.println("File Written");
		
		//TODO Generating Bigram tokens list
		ArrayList<String> bigramList = TextOperations.getNGrams(finalTokens, 2);
		
		System.out.println("Bigram Generated");		
				
		//TODO writing bigram tokens list to text file
		TextOperations.writeListToTextFile(UNLPTKConstants.OUTPUT_FILE+"_bigram.txt", bigramList);
		
		System.out.println("Bigram File Written");
				
		//TODO Generating trigram tokens list
		ArrayList<String> trigramList = TextOperations.getNGrams(finalTokens, 3);
		
		System.out.println("Trigram Generated");
		
		//TODO writing trigram tokens list to text file
		TextOperations.writeListToTextFile(UNLPTKConstants.OUTPUT_FILE+"_trigram.txt", trigramList);
		
		System.out.println("Trigram File Written");
		
		System.out.println("Its DONE !");
	}

	
	
	
	
	
	/**
	 * This function will generate ARFF file for Single attribute based techniques, similarity scores with respect to
	 * technique i.e. Keywords based, Simple LCS etc.
	 * @param parsedTextPairsList List of all documents pair in the corpus.
	 * @param lcsTechnique It can have only these possible values 
	 * 1- SIMPLE_LCS_SIM_SCORE
	 * 2- KEYWORDS_BASED_LCS_SIM_SCORE
	 * 3- SENTENCE_RATIO_SIM_SCORE
	 * 4- TOKENS_RATIO_SIM_SCORE
	 * 5- TYPE_TOKENS_RATIO_SIM_SCORE
	 * @throws IOException
	 */
	
	public static void writeSingleAttributeARFF(ArrayList<ParseTextPair> parsedTextPairsList, String plagDetectionTechnique)
			throws IOException {
		String arffInstances=UNLPTKConstants.ARFF_RELATION+" "
			+plagDetectionTechnique+ "\n\n\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+plagDetectionTechnique+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+UNLPTKConstants.ARFF_CLASS+"\n"
			+"\n\n\n"
			+UNLPTKConstants.ARFF_DATA
			+"\n";
		
			
		for(ParseTextPair pt : parsedTextPairsList){
			String pairLabel = pt.getPairLabel();
			
			//TODO SIMPLE LCS Similarity Scores preparation for ARFF
			float simScore = 0;
			if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.SIMPLE_LCS))
				simScore = pt.getLcsSimilarityScore();
			else if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.KEYWORDS_BASED_LCS))
				simScore = pt.getKeyWordsLCSSimilarityScore();
			else if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.SENTENCE_RATIO))
				simScore = pt.getSentenceRatioSimilarityScore();
			else if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.TOKENS_RATIO))
				simScore = pt.getTokensRatioSimilarityScore();
			else if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.TYPE_TOKENS_RATIO))
				simScore = pt.getTypeTokensRatioSimilarityScore();
			else if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.GREEDY_STRING_TILING))
				simScore = pt.getGSTSimilarityScore();
			else {
				System.err.println("Wrong value in plagDetectionTechnique attribute. Please use one the available choices \n"
						+ "1- "+UNLPTKConstants.SIMPLE_LCS+"\n"
						+ "2- "+UNLPTKConstants.KEYWORDS_BASED_LCS+"\n"
						+ "3- "+UNLPTKConstants.SENTENCE_RATIO+"\n"
						+ "4- "+UNLPTKConstants.TOKENS_RATIO+"\n"
						+ "5- "+UNLPTKConstants.TYPE_TOKENS_RATIO);
				System.exit(0);
			}
			
			String singleArffInstance = UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore)+","+pairLabel;
			arffInstances = arffInstances+singleArffInstance+"\n";
			
		}
		
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+plagDetectionTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), arffInstances.trim().getBytes());			
		System.out.println(plagDetectionTechnique+"---------------- Similarity Score Arff has been Written to Default Output Directory");
	}
	
	public static void writeNGramARFF(ArrayList<ParseTextPair> parsedTextPairsList, String simMeasureTechnique, String plagDetectionTechnique)
			throws IOException {
		String nGramInstances=UNLPTKConstants.ARFF_RELATION+" "
			+plagDetectionTechnique+"_"	
			+simMeasureTechnique+"_NGram_Similarity_Scores"+ "\n\n\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+simMeasureTechnique+"_"+UNLPTKConstants.UNI_GRAM+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+simMeasureTechnique+"_"+UNLPTKConstants.BI_GRAM+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+simMeasureTechnique+"_"+UNLPTKConstants.TRI_GRAM+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+simMeasureTechnique+"_"+UNLPTKConstants.FOUR_GRAM+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+simMeasureTechnique+"_"+UNLPTKConstants.FIVE_GRAM+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+UNLPTKConstants.ARFF_CLASS+"\n"
			+"\n\n\n"
			+UNLPTKConstants.ARFF_DATA
			+"\n";
		
		
				
		for(ParseTextPair pt : parsedTextPairsList){
			String pairLabel = pt.getPairLabel();
			
			//TODO SIMPLE NGram Similarity Scores preparation for ARFF
			float simScore[][] = null;
			if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.SIMPLE_NGRAM_BASED)){
				simScore = pt.getNgramSimilarityScores();
			}else if(plagDetectionTechnique.equalsIgnoreCase(UNLPTKConstants.KEYWORDS_BASED_NGRAM)){
				simScore = pt.getKeyWordsNgramSimilarityScores();
			}else{
				System.err.println("Please choose correct option i.e. KeyWords or Simple");
				System.exit(0);
			}
			
			/*if(lcsTechnique.equalsIgnoreCase(UNLPTKConstants.JACCARD))
				simScore = pt.getNgramSimilarityScores();*/

			if(simMeasureTechnique.equalsIgnoreCase(UNLPTKConstants.JACCARD)){
				nGramInstances= nGramInstances+
								UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1][1])+","+
								UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1][2])+","+
								UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1][3])+","+
								UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1][4])+","+
								UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1][5])+","+
								pairLabel+"\n";
			}else if(simMeasureTechnique.equalsIgnoreCase(UNLPTKConstants.DICE)){
				nGramInstances= nGramInstances+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2][1])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2][2])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2][3])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2][4])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2][5])+","+
						pairLabel+"\n";
			}else if(simMeasureTechnique.equalsIgnoreCase(UNLPTKConstants.CONTAINMENT)){
				nGramInstances= nGramInstances+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3][1])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3][2])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3][3])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3][4])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3][5])+","+
						pairLabel+"\n";
			}else if(simMeasureTechnique.equalsIgnoreCase(UNLPTKConstants.OVERLAP)){
				nGramInstances= nGramInstances+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[4][1])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[4][2])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[4][3])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[4][4])+","+
						UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[4][5])+","+
						pairLabel+"\n";
			}	
		}
		
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+plagDetectionTechnique+"_"+simMeasureTechnique+"_NGram_Similarity_Scores"+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), nGramInstances.trim().getBytes());
		/*Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_JACCARD_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSJaccardInstance.trim().getBytes());
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_DICE_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSDiceInstance.trim().getBytes());
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_CONTAINMENT_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSContainmentInstance.trim().getBytes());
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_OVERLAP_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSContainmentInstance.trim().getBytes());*/
		
	
		System.out.println(plagDetectionTechnique+"_"+simMeasureTechnique+ "_N_Gram_Similarity_Score File has been Wrritten to Default Output Directory");
	}

/*	public static void writeNGramARFF(ArrayList<ParseTextPair> parsedTextPairsList, String lcsTechnique)
			throws IOException {
		String simpleLCSAllInstances=UNLPTKConstants.ARFF_RELATION+" "
			+lcsTechnique+"_LCS"+ "\n\n\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.JACCARD+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.DICE+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.CONTAINMENT+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.OVERLAP+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"
			+UNLPTKConstants.ARFF_ATTRIBUTE+" "+UNLPTKConstants.ARFF_CLASS+"\n"
			+"\n\n\n"
			+UNLPTKConstants.ARFF_DATA
			+"\n";
		
		
		String simpleLCSJaccardInstance=UNLPTKConstants.ARFF_RELATION+" "
				+lcsTechnique+"_"+UNLPTKConstants.JACCARD+"\n\n\n"
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.JACCARD+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"				
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+UNLPTKConstants.ARFF_CLASS+"\n"
				+"\n\n\n"
				+UNLPTKConstants.ARFF_DATA
				+"\n";
		String simpleLCSDiceInstance=UNLPTKConstants.ARFF_RELATION+" "
				+lcsTechnique+"_"+UNLPTKConstants.DICE+"\n\n\n"
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.DICE+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"				
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+UNLPTKConstants.ARFF_CLASS+"\n"
				+"\n\n\n"
				+UNLPTKConstants.ARFF_DATA
				+"\n";
		String simpleLCSContainmentInstance=UNLPTKConstants.ARFF_RELATION+" "
				+lcsTechnique+"_"+UNLPTKConstants.CONTAINMENT+"\n\n\n"
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.CONTAINMENT+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"				
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+UNLPTKConstants.ARFF_CLASS+"\n"
				+"\n\n\n"
				+UNLPTKConstants.ARFF_DATA
				+"\n";
		String simpleLCSOverlapInstance=UNLPTKConstants.ARFF_RELATION+" "
				+lcsTechnique+"_"+UNLPTKConstants.OVERLAP+"\n\n\n"
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+lcsTechnique+"_"+UNLPTKConstants.OVERLAP+" "+UNLPTKConstants.ARFF_NUMERIC_DATA_TYPE+"\n"				
				+UNLPTKConstants.ARFF_ATTRIBUTE+" "+UNLPTKConstants.ARFF_CLASS+"\n"
				+"\n\n\n"
				+UNLPTKConstants.ARFF_DATA
				+"\n";
		
		for(ParseTextPair pt : parsedTextPairsList){
			String pairLabel = pt.getPairLabel();
			
			//TODO SIMPLE LCS Similarity Scores preparation for ARFF
			float simScore = 0;
			if(lcsTechnique.equalsIgnoreCase("combined"))
				simScore = pt.getLcsSimilarityScore();
			else if(lcsTechnique.equalsIgnoreCase("keywords"))
				simScore = pt.getKeyWordsLCSSimilarityScore();
			simpleLCSJaccardInstance=UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[0])+"";
			simpleLCSDiceInstance=UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1])+"";
			simpleLCSContainmentInstance=UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2])+"";
			simpleLCSOverlapInstance=UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3])+"";
			
			String combinedLCSArffInstance = UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[0])+","+UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1])+","+UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2])+","+UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3])+","+pairLabel;
			simpleLCSAllInstances = simpleLCSAllInstances+combinedLCSArffInstance+"\n";
			
			simpleLCSJaccardInstance = simpleLCSJaccardInstance+UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[0])+","+pairLabel+"\n";
			simpleLCSDiceInstance = simpleLCSDiceInstance+UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[1])+","+pairLabel+"\n";
			simpleLCSContainmentInstance = simpleLCSContainmentInstance+UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[2])+","+pairLabel+"\n";
			simpleLCSOverlapInstance = simpleLCSOverlapInstance+UNLPTKConstants.DEFAULT_DECIMAL_FORMATTER.format(simScore[3])+","+pairLabel+"\n";
			
			//TODO Key Words LCS Similarity Scores preparation for ARFF
			simScore = pt.getKeyWordsLCSSimilarityScores();
			arffInstance = simScore[0]+","+simScore[1]+","+simScore[2]+","+simScore[3]+","+pt.getPairLabel();
			keyWordsLCSInstances = keyWordsLCSInstances+arffInstance+"\n";		
			
		}
		
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_All_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSAllInstances.trim().getBytes());
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_JACCARD_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSJaccardInstance.trim().getBytes());
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_DICE_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSDiceInstance.trim().getBytes());
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_CONTAINMENT_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSContainmentInstance.trim().getBytes());
		Files.write(get(UNLPTKConstants.DEFAULT_OUTPUT_DIR+File.separator+"LCS_SIM_SCORE_OVERLAP_"+lcsTechnique+UNLPTKConstants.ARFF_OUTPUT_EXTENTION), simpleLCSContainmentInstance.trim().getBytes());
		
	
		System.out.println("Files have been Written in Output Directory");
	}*/
	
	
	/**
	 * @param annotatedPairList
	 * @param parsedPairsList
	 */
	public static ArrayList<ParseTextPair> getParsedTextPairs(ArrayList<String> annotatedPairList, String filePairSplitter, String corpusSourceDirectory, Charset fileReadingFormat) {		
		ArrayList<ParseTextPair> parsedPairsList = new ArrayList<ParseTextPair>();
		
		for(String pair : annotatedPairList){
			String[] splitedPair = pair.split(filePairSplitter);
			String sourceFile = splitedPair[0];
			String suspeciousFile = splitedPair[1];
			String pairLabel      = splitedPair[2];			
			
			ParseTextPair parseTextPair = new ParseTextPair(sourceFile, suspeciousFile, pairLabel, corpusSourceDirectory, fileReadingFormat);
			parsedPairsList.add(parseTextPair);
			System.out.println("System Parsed :  Pair No = "+ parsedPairsList.size() +", Source File = "+sourceFile+", Suspecious File = "+suspeciousFile+", Pair Label = "+pairLabel);
			
		}		
		return parsedPairsList;
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
}
