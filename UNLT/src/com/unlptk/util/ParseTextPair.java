package com.unlptk.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.unlptk.gst.GreedyStringTiling;
import com.unlptk.tokenizer.SentenceTokenizer;

public class ParseTextPair{
	
	public ParseTextPair(ArrayList<String> sourceText, ArrayList<String> suspeciousText, String pairLabel) {
		super();
		String msg1 = "Source File Not Provided";
		String msg2 = "Suspecious File Not Provided";
		this.sourceFile = msg1;
		this.suspeciousFile = msg2;
		
		this.pairLabel = pairLabel;
		
		setRawSourceText(sourceText);
		setRawSuspeciousText(suspeciousText);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		computeLCS(UNLPTKConstants.SIMPLE_LCS);
		computeLCS(UNLPTKConstants.KEYWORDS_BASED_LCS);
		computeNGramSimilarityScores();
		computeKeyWordsNGramSimilarityScores();		
		computeSentenceRatioSimilarityScore();
		computeTokensRatioSimilarityScore();
		computeTypeTokensRatioSimilarityScore();
		computeGSTSimilarityScore();
	}
	
	public ParseTextPair(String sourceFile, String suspeciousFile, String pairLabel, String sourceDir, Charset fileReadingFormat) {
		super();
		this.sourceFile = sourceFile;
		this.suspeciousFile = suspeciousFile;
		this.pairLabel = pairLabel;
		
		try {			
			setRawSourceText(FileOperations.fileToArrayList(sourceDir+File.separator+sourceFile, fileReadingFormat));
			setRawSuspeciousText(FileOperations.fileToArrayList(sourceDir+File.separator+suspeciousFile, fileReadingFormat));
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/** Name of source file from annotated pair */		 
	private String sourceFile;
	
	/** Name of suspecious file from annotated pair */
	private String suspeciousFile;
	
	/** Label of the annotated pair e.g. Near Copy, Light Revision etc */
	private String pairLabel;
	
	/** Source text in its original form available in the text file. 
	 * If there are more than one lines in the text file, then each 
	 * line will be saved at each element of this list */
	private ArrayList<String> rawSourceText;
	
	/** Suspecious text in its original form available in the text file. 
	 * If there are more than one lines in the text file, then each 
	 * line will be saved at each element of this list */
	private ArrayList<String> rawSuspeciousText;
	
	/** Source text in its original form, but all lines of this text file
	 *  will be merged to form whole text in a single line.*/
	private String rawConcatenatedSourceText;
	
	/** Suspecious text in its original form, but all lines of this text file
	 *  will be merged to form whole text in a single line.*/
	private String rawConcatenatedSuspeciousText;
	
	/** Source text after pre-processing*/
	private String preProcessedSourceText;
	
	/** Suspecious text after pre-processing*/
	private String preProcessedSuspeciousText;
	
	/** Pre-processed source text tokens list*/
	private ArrayList<String> tokensOfSourceText;
	
	/** Pre-processed suspecious text tokens list*/
	private ArrayList<String> tokensOfSuspeciousText;
	
	/** Pre-processed source text after tokenization*/
	private String tokenizedSourceText;
	
	/** Pre-processed suspecious text after tokenization*/
	private String tokenizedSuspeciousText;
	
	/** Unique tokens of Pre-processed source text after tokenization*/
	private ArrayList<String> uniqueTokensSourceText;
	
	/** Unique tokens of Pre-processed suspecious text after tokenization*/
	private ArrayList<String> uniqueTokensSuspeciousText;
	
	/** Number of Unique tokens of Pre-processed source text after tokenization*/
	private int noOfUniqueTokensSourceText;
	
	/** Number of Unique tokens of Pre-processed suspecious text after tokenization*/
	private int noOfUniqueTokensSuspeciousText;

	/** LCS (Longest Common Subsequence) between source and suspecious text*/
	private ArrayList<String> lCS;
	
	/** LCS (Longest Common Subsequence) of KEYWORDS between source and suspecious text*/
	private ArrayList<String> keyWordsLCS;
	
	/** List of KeyWords Source Text*/
	private ArrayList<String> keyWordsOfSourceText;
	
	/** List of KeyWords Suspecious Text*/
	private ArrayList<String> keyWordsOfSuspeciousText;
	
	/** List of StopWords Source Text*/
	private ArrayList<String> stopWordsOfSourceText;
	
	/** List of StopWords Suspecious Text*/
	private ArrayList<String> stopWordsOfSuspeciousText;
		
	/** LCS Similarity Score computed by Jaccard Similarity co-efficient*//*
	private float lCSSimScoreByJaccard;
	
	*//** LCS Similarity Score computed by Dice Similarity co-efficient*//*
	private float lCSSimScoreByDice;
	
	*//** LCS Similarity Score computed by Overlap Similarity co-efficient*//*
	private float lCSSimScoreByOverlap;
	
	*//** LCS Similarity Score computed by Containment Similarity co-efficient*//*
	private float lCSSimScoreByContainment;*/
	
	/** NGram Similarity Score computed by Jaccard, Dice, Overlap and Containment Similarity co-efficient*/
	float lcsSimilarityScore;
	
	/** NGram Similarity Score computed by Jaccard, Dice, Overlap and Containment Similarity co-efficient*/
	private float [][] ngramSimilarityScores;
	
	/** NGram Similarity Score of KEYWords computed by Jaccard, Dice, Overlap and Containment Similarity co-efficient*/
	private float [][] keyWordsNgramSimilarityScores;
	
	/** LCS Similarity Score of KEYWords computed */
	private float keyWordsLCSSimilarityScore;
	
	/** Sentence Ratio Similarity Score*/
	float sentenceRatioSimilarityScore;
	
	/** Token Ratio Similarity Score*/
	float tokensRatioSimilarityScore;

	/** Token Ratio Similarity Score*/
	float typeTokensRatioSimilarityScore;
	
	/** GST (Greedy String Tiling) Similarity Score*/
	float gSTSimilarityScore;
	
	
	
	
	
	
	
	
	/**
	 * @return the sourceFile
	 */
	public String getSourceFile() {
		return sourceFile;
	}

	/**
	 * @param sourceFile the sourceFile to set
	 */
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * @return the suspeciousFile
	 */
	public String getSuspeciousFile() {
		return suspeciousFile;
	}

	/**
	 * @param suspeciousFile the suspeciousFile to set
	 */
	public void setSuspeciousFile(String suspeciousFile) {
		this.suspeciousFile = suspeciousFile;
	}

	/**
	 * @return the pairLabel
	 */
	public String getPairLabel() {
		return pairLabel;
	}

	/**
	 * @param pairLabel the pairLabel to set
	 */
	public void setPairLabel(String pairLabel) {
		this.pairLabel = pairLabel;
	}

	/**
	 * @return the rawSourceText
	 */
	public ArrayList<String> getRawSourceText() {
		return rawSourceText;
	}

	/**
	 * @param rawSourceText the rawSourceText to set
	 */
	public void setRawSourceText(ArrayList<String> rawSourceText) {
		this.rawSourceText = rawSourceText;
		
		String mergedText = String.join(" ", rawSourceText);
		setRawConcatenatedSourceText(mergedText);
		
		String preProcessedText = TextOperations.urduTextPreProcessing(mergedText);
		setPreProcessedSourceText(preProcessedText);
		
		//TODO Getting default tokens of length 1. This will be the input of all greater lenght tokens
		ArrayList<String> tokens = TextOperations.getStringTokens(preProcessedText, UNLPTKConstants.DEFAULT_WORDS_SEPERATOR);		
				
		//TODO Generating finalized tokens of text.
		ArrayList<String> finalTokens = TextOperations.generateTokensFromList(preProcessedText, UNLPTKConstants.MULTI_WORD_DICTIONARY_HASH_CODES, tokens);
		setTokensOfSourceText(finalTokens);
		
		//TODO Setting unique tokens of the source text
		setUniqueTokensSourceText(finalTokens);
		
		//TODO Generating keywords list of source text after removing all stop words from this text.
		setKeyWordsOfSourceText(this.removeCommonTokens(UNLPTKConstants.URDU_STOP_WORDS_LIST, finalTokens));
		
		//TODO Generating stopwords list of source text after removing all keywords from the text
		setStopWordsOfSourceText(this.removeCommonTokens(finalTokens, UNLPTKConstants.URDU_STOP_WORDS_LIST));
	}

	/**
	 * @return the rawSuspeciousText
	 */
	public ArrayList<String> getRawSuspeciousText() {
		return rawSuspeciousText;
	}

	/**
	 * @param rawSuspeciousText the rawSuspeciousText to set
	 */
	public void setRawSuspeciousText(ArrayList<String> rawSuspeciousText) {
		this.rawSuspeciousText = rawSuspeciousText;
		
		String mergedText = String.join(" ", rawSuspeciousText);
		setRawConcatenatedSuspeciousText(mergedText);
		
		String preProcessedText = TextOperations.urduTextPreProcessing(mergedText);
		setPreProcessedSuspeciousText(preProcessedText);
		
		//TODO Getting default tokens of length 1. This will be the input of all greater lenght tokens
		ArrayList<String> tokens = TextOperations.getStringTokens(preProcessedText, UNLPTKConstants.DEFAULT_WORDS_SEPERATOR);		
				
		//TODO Generating finalized tokens of text.
		ArrayList<String> finalTokens = TextOperations.generateTokensFromList(preProcessedText, UNLPTKConstants.MULTI_WORD_DICTIONARY_HASH_CODES, tokens);
		setTokensOfSuspeciousText(finalTokens);
		
		//TODO Setting unique tokens of the suspecious text
		setUniqueTokensSuspeciousText(finalTokens);
		
		//TODO Generating keywords list of source text after removing all stop words from this text.
		setKeyWordsOfSuspeciousText(this.removeCommonTokens(UNLPTKConstants.URDU_STOP_WORDS_LIST, finalTokens));
				
		//TODO Generating stopwords list of source text after removing all keywords from the text
		setStopWordsOfSuspeciousText(this.removeCommonTokens(finalTokens, UNLPTKConstants.URDU_STOP_WORDS_LIST));
	}

	/**
	 * @return the rawConcatenatedSourceText
	 */
	public String getRawConcatenatedSourceText() {
		return rawConcatenatedSourceText;
	}

	/**
	 * @param rawConcatenatedSourceText the rawConcatenatedSourceText to set
	 */
	private void setRawConcatenatedSourceText(String rawConcatenatedSourceText) {
		this.rawConcatenatedSourceText = rawConcatenatedSourceText;
	}

	/**
	 * @return the rawConcatenatedSuspeciousText
	 */
	public String getRawConcatenatedSuspeciousText() {
		return rawConcatenatedSuspeciousText;
	}

	/**
	 * @param rawConcatenatedSuspeciousText the rawConcatenatedSuspeciousText to set
	 */
	private void setRawConcatenatedSuspeciousText(String rawConcatenatedSuspeciousText) {
		this.rawConcatenatedSuspeciousText = rawConcatenatedSuspeciousText;
	}

	/**
	 * @return the preProcessedSourceText
	 */
	public String getPreProcessedSourceText() {
		return preProcessedSourceText;
	}

	/**
	 * @param preProcessedSourceText the preProcessedSourceText to set
	 */
	private void setPreProcessedSourceText(String preProcessedSourceText) {
		this.preProcessedSourceText = preProcessedSourceText;
	}

	/**
	 * @return the preProcessedSuspeciousText
	 */
	public String getPreProcessedSuspeciousText() {
		return preProcessedSuspeciousText;
	}

	/**
	 * @param preProcessedSuspeciousText the preProcessedSuspeciousText to set
	 */
	private void setPreProcessedSuspeciousText(String preProcessedSuspeciousText) {
		this.preProcessedSuspeciousText = preProcessedSuspeciousText;
	}

	/**
	 * @return the tokensOfSourceText
	 */
	public ArrayList<String> getTokensOfSourceText() {
		return tokensOfSourceText;
	}

	/**
	 * @param tokensOfSourceText the tokensOfSourceText to set
	 */
	private void setTokensOfSourceText(ArrayList<String> tokensOfSourceText) {
		this.tokensOfSourceText = tokensOfSourceText;
	}

	/**
	 * @return the tokensOfSuspeciousText
	 */
	public ArrayList<String> getTokensOfSuspeciousText() {
		return tokensOfSuspeciousText;
	}

	/**
	 * @param tokensOfSuspeciousText the tokensOfSuspeciousText to set
	 */
	private void setTokensOfSuspeciousText(ArrayList<String> tokensOfSuspeciousText) {
		this.tokensOfSuspeciousText = tokensOfSuspeciousText;
	}

	/**
	 * @return the tokenizedSourceText
	 */
	public String getTokenizedSourceText() {
		return tokenizedSourceText;
	}

	/**
	 * @param tokenizedSourceText the tokenizedSourceText to set
	 */
	private void setTokenizedSourceText(String tokenizedSourceText) {
		this.tokenizedSourceText = tokenizedSourceText;
	}

	/**
	 * @return the tokenizedSuspeciousText
	 */
	public String getTokenizedSuspeciousText() {
		return tokenizedSuspeciousText;
	}

	/**
	 * @param tokenizedSuspeciousText the tokenizedSuspeciousText to set
	 */
	private void setTokenizedSuspeciousText(String tokenizedSuspeciousText) {
		this.tokenizedSuspeciousText = tokenizedSuspeciousText;
	}

	/**
	 * @return the uniqueTokensSourceText
	 */
	public ArrayList<String> getUniqueTokensSourceText() {
		return uniqueTokensSourceText;
	}

	/**
	 * @param uniqueTokensSourceText the uniqueTokensSourceText to set
	 */
	private void setUniqueTokensSourceText(ArrayList<String> uniqueTokensSourceText) {		
		this.uniqueTokensSourceText = new ArrayList<String>();
		Set<String> uniqueTokens = new HashSet<String>(uniqueTokensSourceText);
		this.uniqueTokensSourceText.addAll(uniqueTokens);
		this.setNoOfUniqueTokensSourceText(uniqueTokens.size());
	}

	/**
	 * @return the uniqueTokensSuspeciousText
	 */
	public ArrayList<String> getUniqueTokensSuspeciousText() {
		return uniqueTokensSuspeciousText;
	}

	/**
	 * @param uniqueTokensSuspeciousText the uniqueTokensSuspeciousText to set
	 */
	private void setUniqueTokensSuspeciousText(ArrayList<String> uniqueTokensSuspeciousText) {
		this.uniqueTokensSuspeciousText = new ArrayList<String>();
		Set<String> uniqueTokens = new HashSet<String>(uniqueTokensSuspeciousText);
		this.uniqueTokensSuspeciousText.addAll(uniqueTokens);
		this.setNoOfUniqueTokensSuspeciousText(uniqueTokens.size());
	}

	/**
	 * @return the noOfUniqueTokensSourceText
	 */
	public int getNoOfUniqueTokensSourceText() {
		return noOfUniqueTokensSourceText;
	}

	/**
	 * @param noOfUniqueTokensSourceText the noOfUniqueTokensSourceText to set
	 */
	private void setNoOfUniqueTokensSourceText(int noOfUniqueTokensSourceText) {
		this.noOfUniqueTokensSourceText = noOfUniqueTokensSourceText;
	}

	/**
	 * @return the noOfUniqueTokensSuspeciousText
	 */
	public int getNoOfUniqueTokensSuspeciousText() {
		return noOfUniqueTokensSuspeciousText;
	}

	/**
	 * @param noOfUniqueTokensSuspeciousText the noOfUniqueTokensSuspeciousText to set
	 */
	private void setNoOfUniqueTokensSuspeciousText(int noOfUniqueTokensSuspeciousText) {
		this.noOfUniqueTokensSuspeciousText = noOfUniqueTokensSuspeciousText;
	}

	
	/**
	 * @return the lCS
	 */
	public ArrayList<String> getlCS() {
		return lCS;
	}

	/**
	 * @param lCS the lCS to set
	 */
	public void setlCS(ArrayList<String> lCS) {
		this.lCS = lCS;
	}

	/**
	 * 
	 * @return sentence ratio similarity score between source and suspecious document
	 */
	public float getSentenceRatioSimilarityScore() {
		return sentenceRatioSimilarityScore;
	}

	/**
	 * Sentence ratio similarity score between source and suspecious document
	 * @param sentenceRatioSimilarityScore
	 */
	public void setSentenceRatioSimilarityScore(float sentenceRatioSimilarityScore) {
		this.sentenceRatioSimilarityScore = sentenceRatioSimilarityScore;
	}

	/**
	 * 
	 * @return Token ratio similarity score between source and suspecious document
	 */
	public float getTokensRatioSimilarityScore() {
		return tokensRatioSimilarityScore;
	}

	/**
	 * Token ratio similarity score between source and suspecious document
	 * @param tokenRatioSimilarityScore 
	 */
	public void setTokensRatioSimilarityScore(float tokensRatioSimilarityScore) {
		this.tokensRatioSimilarityScore = tokensRatioSimilarityScore;
	}

	/**
	 * Type Token ratio similarity score between source and suspecious document
	 * @return Type Token ratio similarity score between source and suspecious document
	 */
	public float getTypeTokensRatioSimilarityScore() {
		return typeTokensRatioSimilarityScore;
	}

	/**
	 * Type Token ratio similarity score between source and suspecious document
	 * @param typeTokenRatioSimilarityScore
	 */
	public void setTypeTokensRatioSimilarityScore(float typeTokensRatioSimilarityScore) {
		this.typeTokensRatioSimilarityScore = typeTokensRatioSimilarityScore;
	}


	/**
	 * It computes the GST similarity score between Source and Suspecious Documents.
	 */
	private void computeGSTSimilarityScore(){
		GreedyStringTiling gst = new GreedyStringTiling();
		float similarityScore = gst.generateTiles(this.getTokensOfSourceText(), this.getTokensOfSuspeciousText(), UNLPTKConstants.MINIMAL_MATCHING_LENGTH, UNLPTKConstants.GST_THRESHOLD);
		this.setGSTSimilarityScore(similarityScore);		
	}

	/**
	 * It computes the sentence ratio similarity score between Source and Suspecious Documents.
	 */
	private void computeSentenceRatioSimilarityScore(){
		String srcPreProcessedText = TextOperations.removeSpecialCharacters(this.getRawSourceText());
		String susPreProcessedText = TextOperations.removeSpecialCharacters(this.getRawSuspeciousText());
		
		SentenceTokenizer stSrc = new SentenceTokenizer(srcPreProcessedText);
		SentenceTokenizer stSus = new SentenceTokenizer(susPreProcessedText);
		
		int numberOfSrcSentences = stSrc.getNumberOfSentences();
		int numberOfSusSentences = stSus.getNumberOfSentences();
		
		float simScore = (float) Math.min(numberOfSrcSentences, numberOfSusSentences)/Math.max(numberOfSrcSentences, numberOfSusSentences);
		this.setSentenceRatioSimilarityScore(simScore);
	}
	
	/**
	 * It computes the token's ratio similarity score between Source and Suspecious Documents.
	 */
	private void computeTokensRatioSimilarityScore(){
		String srcPreProcessedText = TextOperations.removeSpecialCharacters(this.getRawSourceText());
		String susPreProcessedText = TextOperations.removeSpecialCharacters(this.getRawSuspeciousText());
		
		SentenceTokenizer stSrc = new SentenceTokenizer(srcPreProcessedText);
		SentenceTokenizer stSus = new SentenceTokenizer(susPreProcessedText);		
			
		int numberOfTokensInSrcDocument = this.getTokensOfSourceText().size();
		int numberOfTokensInSusDocument = this.getTokensOfSuspeciousText().size();
						
		int numberOfSrcSentences = stSrc.getNumberOfSentences();
		int numberOfSusSentences = stSus.getNumberOfSentences();
		
		
		float avgNoOfTokensInSrcDocument = (float) numberOfTokensInSrcDocument/numberOfSrcSentences;
		float avgNoOfTokensInSusDocument = (float) numberOfTokensInSusDocument/numberOfSusSentences;
				
		float simScore = (float) Math.min(avgNoOfTokensInSrcDocument, avgNoOfTokensInSusDocument)/Math.max(avgNoOfTokensInSrcDocument, avgNoOfTokensInSusDocument);

		this.setTokensRatioSimilarityScore(simScore); 
	}
	
	/**
	 * It computes the type token's ratio similarity score between Source and Suspecious Documents.
	 */
	private void computeTypeTokensRatioSimilarityScore(){				
			
		//TODO Counting unique tokens
		int numberOfUniqueTokensInSrcDocument = this.getUniqueTokensSourceText().size();
		int numberOfUniqueTokensInSusDocument = this.getUniqueTokensSuspeciousText().size();
			
		//TODO Counting Total tokens
		int numberOfTokensInSrcDocument = this.getTokensOfSourceText().size();
		int numberOfTokensInSusDocument = this.getTokensOfSuspeciousText().size();
						
				
		float avgNoOfTokensInSrcDocument = (float) numberOfUniqueTokensInSrcDocument/numberOfTokensInSrcDocument;
		float avgNoOfTokensInSusDocument = (float) numberOfUniqueTokensInSusDocument/numberOfTokensInSusDocument;
				
		float simScore = (float) Math.min(avgNoOfTokensInSrcDocument, avgNoOfTokensInSusDocument)/Math.max(avgNoOfTokensInSrcDocument, avgNoOfTokensInSusDocument);
		this.setTypeTokensRatioSimilarityScore(simScore); 
	}
	
	/**	 
	 * @return 2-D array in which each row contains similarity measure score with different techniques.
	 * This array contains 4 rows, each row contains scores as follows
	 * Row1: contains similarity scores of JACCARD Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row2: contains similarity scores of DICE Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row3: contains similarity scores of CONTAINMENT Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row4: contains similarity scores of OVERLAP Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Colums of this array will be equal to "UNLPTKConstants = NGRAM_LENGTH"
	 */
	
	public float[][] getNgramSimilarityScores() {
		return ngramSimilarityScores;
	}

	/**
	 * @param ngramSimilarityScores the ngramSimilarityScores to set
	 */
	private void setNgramSimilarityScores(float[][] ngramSimilarityScores) {
		this.ngramSimilarityScores = ngramSimilarityScores;
	}

	/**
	 * @return the keyWordsOfSourceText
	 */
	public ArrayList<String> getKeyWordsOfSourceText() {
		return keyWordsOfSourceText;
	}

	/**
	 * @param keyWordsOfSourceText the keyWordsOfSourceText to set
	 */
	public void setKeyWordsOfSourceText(ArrayList<String> keyWordsOfSourceText) {
		this.keyWordsOfSourceText = keyWordsOfSourceText;
	}

	/**
	 * @return the keyWordsOfSuspeciousText
	 */
	public ArrayList<String> getKeyWordsOfSuspeciousText() {
		return keyWordsOfSuspeciousText;
	}

	/**
	 * @param keyWordsOfSuspeciousText the keyWordsOfSuspeciousText to set
	 */
	public void setKeyWordsOfSuspeciousText(
			ArrayList<String> keyWordsOfSuspeciousText) {
		this.keyWordsOfSuspeciousText = keyWordsOfSuspeciousText;
	}

	/**
	 * @return the stopWordsOfSourceText
	 */
	public ArrayList<String> getStopWordsOfSourceText() {
		return stopWordsOfSourceText;
	}

	/**
	 * @param stopWordsOfSourceText the stopWordsOfSourceText to set
	 */
	public void setStopWordsOfSourceText(ArrayList<String> stopWordsOfSourceText) {
		this.stopWordsOfSourceText = stopWordsOfSourceText;
	}

	/**
	 * @return the stopWordsOfSuspeciousText
	 */
	public ArrayList<String> getStopWordsOfSuspeciousText() {
		return stopWordsOfSuspeciousText;
	}

	/**
	 * @param stopWordsOfSuspeciousText the stopWordsOfSuspeciousText to set
	 */
	public void setStopWordsOfSuspeciousText(
			ArrayList<String> stopWordsOfSuspeciousText) {
		this.stopWordsOfSuspeciousText = stopWordsOfSuspeciousText;
	}

	/**
	 * @return the keyWordsNgramSimilarityScores
	 */
	public float[][] getKeyWordsNgramSimilarityScores() {
		return keyWordsNgramSimilarityScores;
	}

	/**
	 * @param keyWordsNgramSimilarityScores the keyWordsNgramSimilarityScores to set
	 */
	public void setKeyWordsNgramSimilarityScores(
			float[][] keyWordsNgramSimilarityScores) {
		this.keyWordsNgramSimilarityScores = keyWordsNgramSimilarityScores;
	}

	/**
	 * @return the keyWordsLCSSimilarityScores
	 */
	public float getKeyWordsLCSSimilarityScore() {
		return keyWordsLCSSimilarityScore;
	}

	/**
	 * @param keyWordsLCSSimilarityScores the keyWordsLCSSimilarityScores to set
	 */
	public void setKeyWordsLCSSimilarityScore(float keyWordsLCSSimilarityScores) {
		this.keyWordsLCSSimilarityScore = keyWordsLCSSimilarityScores;
	}

	/**
	 * @return the keyWordsLCS
	 */
	public ArrayList<String> getKeyWordsLCS() {
		return keyWordsLCS;
	}

	/**
	 * @param keyWordsLCS the keyWordsLCS to set
	 */
	public void setKeyWordsLCS(ArrayList<String> keyWordsLCS) {
		this.keyWordsLCS = keyWordsLCS;
	}

	/**
	 * @return the lcsSimilarityScores
	 */
	public float getLcsSimilarityScore() {
		return lcsSimilarityScore;
	}

	/**
	 * @param lcsSimilarityScores the lcsSimilarityScores to set
	 */
	public void setLcsSimilarityScore(float lcsSimilarityScores) {
		this.lcsSimilarityScore = lcsSimilarityScores;
	}

	
	
	
	
	public float getGSTSimilarityScore() {
		return gSTSimilarityScore;
	}

	public void setGSTSimilarityScore(float gSTSimilarityScore) {
		this.gSTSimilarityScore = gSTSimilarityScore;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\n------------------------------Parsed Text Pair ------------------------\n\n"
				+"sourceFile=" + sourceFile 
				+ "\nsuspeciousFile="	+ suspeciousFile 
				+ "\npairLabel=" + pairLabel
				+ "\nrawSourceText=" + rawSourceText 
				+ "\nrawSuspeciousText=" + rawSuspeciousText 
				+ "\nrawConcatenatedSourceText=" + rawConcatenatedSourceText
				+ "\nrawConcatenatedSuspeciousText=" + rawConcatenatedSuspeciousText 
				+ "\npreProcessedSourceText=" + preProcessedSourceText 
				+ "\npreProcessedSuspeciousText="+ preProcessedSuspeciousText 
				+ "\ntokensOfSourceText=" + tokensOfSourceText 
				+ "\ntokensOfSuspeciousText="	+ tokensOfSuspeciousText 
				+ "\ntokenizedSourceText=" + tokenizedSourceText 
				+ "\ntokenizedSuspeciousText=" + tokenizedSuspeciousText 
				+ "\nuniqueTokensSourceText="	+ uniqueTokensSourceText 
				+ "\nuniqueTokensSuspeciousText=" + uniqueTokensSuspeciousText 
				+ "\nnoOfUniqueTokensSourceText="	+ noOfUniqueTokensSourceText
				+ "\nnoOfUniqueTokensSuspeciousText="	+ noOfUniqueTokensSuspeciousText		
				+ "\nSimple LCS=" + lCS
				+ "\nKey Words LCS=" + keyWordsLCS 
				+ "\nKeyWordsOfSourceText=" + keyWordsOfSourceText 
				+ "\nKeyWordsOfSuspeciousText=" + keyWordsOfSuspeciousText 
				+ "\nStopWordsOfSourceText=" + stopWordsOfSourceText 
				+ "\nStopWordsOfSuspeciousText=" + stopWordsOfSuspeciousText 
				+ "\nSimple LCSSimilarityScores=" + lcsSimilarityScore
				+ "\nNgramSimilarityScores=" + Arrays.toString(ngramSimilarityScores)
				+ "\nKeyWordsNgramSimilarityScores=" + Arrays.toString(keyWordsNgramSimilarityScores)
				+ "\nKeyWordsLCSSimilarityScores=" + keyWordsLCSSimilarityScore;
	}

	private ArrayList<String> computeLCS(String key) {

		String[] text1Words = null;
		String[] text2Words = null;
		ArrayList<String> lcsResultList = null;
		
		if(key.equalsIgnoreCase(UNLPTKConstants.SIMPLE_LCS)){	
			text1Words = this.getTokensOfSourceText().toArray(new String[this.getTokensOfSourceText().size()]);
			text2Words = this.getTokensOfSuspeciousText().toArray(new String[this.getTokensOfSuspeciousText().size()]);			
			lcsResultList = computeLCS(text1Words, text2Words);
			
			this.setlCS(lcsResultList);
			this.setLcsSimilarityScore(this.computeLCSSimilarityScores(lcsResultList,UNLPTKConstants.SIMPLE_LCS));
		}else if(key.equalsIgnoreCase(UNLPTKConstants.KEYWORDS_BASED_LCS)){	
			text1Words = this.getKeyWordsOfSourceText().toArray(new String[this.getKeyWordsOfSourceText().size()]);
			text2Words = this.getKeyWordsOfSuspeciousText().toArray(new String[this.getKeyWordsOfSuspeciousText().size()]);
			
			lcsResultList = computeLCS(text1Words, text2Words);			
			this.setKeyWordsLCS(lcsResultList);
			this.setKeyWordsLCSSimilarityScore(this.computeLCSSimilarityScores(lcsResultList, UNLPTKConstants.KEYWORDS_BASED_LCS));
		}else{
			System.err.println(" Please choose appropriate value of key i.e. lcs or keywords");
			System.exit(0);
		}		
		
		return lcsResultList;
	}
	
	/*
	private ArrayList<String> computeKeyWordsLCS() {

		String[] text1Words = this.getKeyWordsOfSourceText().toArray(new String[this.getKeyWordsOfSourceText().size()]);
		String[] text2Words = this.getKeyWordsOfSuspeciousText().toArray(new String[this.getKeyWordsOfSuspeciousText().size()]);
		
		ArrayList<String> lcsResultList = computeLCS(text1Words, text2Words);
		

		//this.computeLCSSimilarityScores();		
		this.setKeyWordsLCS(lcsResultList);
		this.setKeyWordsLCSSimilarityScores(this.computeLCSSimilarityScores(lcsResultList, "keywords"));
		return lcsResultList;

	}
*/
	/**
	 * This functions take two array's of strings and generate 
	 * longest common subsequence (LCS).
	 * @param text1Words
	 * @param text2Words
	 * @return
	 */
	public ArrayList<String> computeLCS(String[] text1Words, String[] text2Words) {
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
	 * This function compute similarity measure of LCS.  
	 * @param lcs largest sequence in both texts
	 * @param key either you want to compute simple or keywords based lcs. 
	 * it has only two possible values. 
	 * 1- lcs 
	 * 2- keywords
	 * @return similarity measure score between 0 and 1.	 
	 */
	 
	private float computeLCSSimilarityScores(ArrayList<String> lcs,  String key) {
		
		 int srcLength = 0;
		 int susLength = 0;
		 int lcsLength = lcs.size();
			        
	    if(key.equalsIgnoreCase(UNLPTKConstants.SIMPLE_LCS)){	
			srcLength = this.getTokensOfSourceText().size();
		    susLength = this.getTokensOfSuspeciousText().size();
		}else if(key.equalsIgnoreCase(UNLPTKConstants.KEYWORDS_BASED_LCS)){	
			srcLength = this.getKeyWordsOfSourceText().size();
		    susLength = this.getKeyWordsOfSuspeciousText().size();
		}else{
			System.err.println(" Please choose appropriate value of key i.e. lcs or keywords");
			System.exit(0);
		} 
	    	    
	    float lcsScore = (float) lcsLength/Math.min(srcLength, susLength);		
		return lcsScore;
	}
	
	
	/**
	 * This function compute similarity measure on the basis of NGram length.
	 * This function will take length of NGram from this constant "UNLPTKConstants.NGRAM_LENGTH"
	 * @return 2-D array in which each row contains similarity measure score with different techniques.
	 * This array contains 4 rows, each row contains scores as follows
	 * Row1: contains similarity scores of JACCARD Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row2: contains similarity scores of DICE Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row3: contains similarity scores of CONTAINMENT Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row4: contains similarity scores of OVERLAP Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 */
     
	private float[][] computeNGramSimilarityScores() {

		ngramSimilarityScores = new float[5][UNLPTKConstants.NGRAM_LENGTH+1];
		
		for(int nGramCounter=1;nGramCounter<UNLPTKConstants.NGRAM_LENGTH+1;nGramCounter++){
			
			Set<String> srcNGrams = new HashSet<String> (TextOperations.getNGrams(this.getTokensOfSourceText(), nGramCounter));
			Set<String> susNGrams = new HashSet<String> (TextOperations.getNGrams(this.getTokensOfSuspeciousText(), nGramCounter));
			
			Set<String> union = Sets.union(srcNGrams, susNGrams);
			Set<String> intersection = Sets.intersection(srcNGrams, susNGrams);
			    
			int srcLength = srcNGrams.size();
			int susLength = susNGrams.size();
			
			int unionLength = union.size();
			int intersectionLength = intersection.size();

			// Computing similarity co-efficient
			// 1 Jaccard 
			// 2 Dice
			// 3 Containment
			// 4 Overlap
			for(int i=1;i<=4;i++){
				if(i==1){
					//TODO Jaccard Similarity Measure Ref: Dr. Adeel Phd Thesis
					this.ngramSimilarityScores[i][nGramCounter]= computeJaccardSimilarity(unionLength, intersectionLength);
				}else if(i==2){
					//TODO Dice Similarity Measure Ref: Dr. Adeel Phd Thesis
					this.ngramSimilarityScores[i][nGramCounter]= computeDiceSimilarity(srcLength, susLength,
							intersectionLength);
				}else if(i==3){
					//TODO Containment Measure Ref: Dr. Adeel Phd Thesis
					this.ngramSimilarityScores[i][nGramCounter]= computeContainmentSimilarity(srcLength, intersectionLength);
				}else if(i==4){
					//TODO Overlap Similarity Measure Ref: Dr. Adeel Phd Thesis
					this.ngramSimilarityScores[i][nGramCounter]= computeOverlapSimilarity(srcLength, susLength,
							intersectionLength);
				} 
			}//end of Inner for loop
		}//end of outer for loop
		this.setNgramSimilarityScores(ngramSimilarityScores);
		return ngramSimilarityScores;
	}

	/** This function compute similarity measure of KEYWORDS on the basis of NGram length.
	 * This function will take length of NGram from this constant "UNLPTKConstants.KEY_WORDS_NGRAM_LENGTH"
	 * @return 2-D array in which each row contains similarity measure score with different techniques.
	 * This array contains 4 rows, each row contains scores as follows
	 * Row1: contains similarity scores of JACCARD Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row2: contains similarity scores of DICE Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row3: contains similarity scores of CONTAINMENT Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 * Row4: contains similarity scores of OVERLAP Similarity Co-efficient of each length of ngram e.g. n=1,2,3.....
	 */
    
	private float[][] computeKeyWordsNGramSimilarityScores() {

		keyWordsNgramSimilarityScores = new float[5][UNLPTKConstants.KEY_WORDS_NGRAM_LENGTH+1];
		
		for(int nGramCounter=1;nGramCounter<UNLPTKConstants.NGRAM_LENGTH+1;nGramCounter++){
			
			Set<String> srcNGrams = new HashSet<String> (TextOperations.getNGrams(this.getKeyWordsOfSourceText(), nGramCounter));
			Set<String> susNGrams = new HashSet<String> (TextOperations.getNGrams(this.getKeyWordsOfSuspeciousText(), nGramCounter));
			
			Set<String> union = Sets.union(srcNGrams, susNGrams);
			Set<String> intersection = Sets.intersection(srcNGrams, susNGrams);
			    
			int srcLength = srcNGrams.size();
			int susLength = susNGrams.size();
			
			int unionLength = union.size();
			int intersectionLength = intersection.size();

			// Computing similarity co-efficient
			// 1 Jaccard 
			// 2 Dice
			// 3 Containment
			// 4 Overlap
			for(int i=1;i<=4;i++){
				if(i==1){
					//TODO Jaccard Similarity Measure Ref: Dr. Adeel Phd Thesis
					this.keyWordsNgramSimilarityScores[i][nGramCounter]= computeJaccardSimilarity(unionLength, intersectionLength);
				}else if(i==2){
					//TODO Dice Similarity Measure Ref: Dr. Adeel Phd Thesis
					this.keyWordsNgramSimilarityScores[i][nGramCounter]= computeDiceSimilarity(srcLength, susLength,
							intersectionLength);
				}else if(i==3){
					//TODO Containment Measure Ref: Dr. Adeel Phd Thesis
					this.keyWordsNgramSimilarityScores[i][nGramCounter]= computeContainmentSimilarity(srcLength, intersectionLength);
				}else if(i==4){
					//TODO Overlap Similarity Measure Ref: Dr. Adeel Phd Thesis
					this.keyWordsNgramSimilarityScores[i][nGramCounter]= computeOverlapSimilarity(srcLength, susLength,
							intersectionLength);
				} 
			}//end of Inner for loop
		}//end of outer for loop		
		this.setKeyWordsNgramSimilarityScores(this.keyWordsNgramSimilarityScores);
		return this.keyWordsNgramSimilarityScores;
	}

	/**
	 * @param srcLength
	 * @param susLength
	 * @param intersectionLength
	 * @return
	 */
	public float computeOverlapSimilarity(int srcLength, int susLength,
			int intersectionLength) {
		return (float) intersectionLength / Math.min(srcLength, susLength);
	}

	/**
	 * @param srcLength
	 * @param susLength
	 * @param intersectionLength
	 * @return
	 */
	public float computeDiceSimilarity(int srcLength, int susLength,
			int intersectionLength) {
		return (float) Math.multiplyExact(2, intersectionLength) / Math.addExact(srcLength, susLength);
	}

	/**
	 * @param srcLength
	 * @param intersectionLength
	 * @return
	 */
	public float computeContainmentSimilarity(int srcLength,
			int intersectionLength) {
		return (float) intersectionLength / srcLength;
	}

	/**
	 * @param unionLength
	 * @param intersectionLength
	 * @return
	 */
	public float computeJaccardSimilarity(int unionLength,
			int intersectionLength) {
		return (float) intersectionLength/unionLength;
	}
	
	/**
     * This function removes words, return list of key words in the text after removing stop words from the given list
     * @param list1 These are the words which you want to remove from list2
     * @param list2 This is the list from which list1 words will be removed
     * @return return list2 after removing all words of list1

     */
	public ArrayList<String> removeCommonTokens(ArrayList<String> list1, ArrayList<String> list2) {
		ArrayList<String> fileLinesWithOutStopWords = new ArrayList<>(list2);
		fileLinesWithOutStopWords.removeAll(list1);
		return fileLinesWithOutStopWords;
	}
}