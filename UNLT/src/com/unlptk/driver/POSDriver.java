package com.unlptk.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import com.unlptk.util.FileOperations;
import com.unlptk.util.TextOperations;
import com.unlptk.util.UNLPTKConstants;

public class POSDriver {

private static final Logger logger = Logger.getLogger(POSDriver.class.getName());


public static void fileCalling() throws IOException {
	System.out.println("File Path :"+UNLPTKConstants.MULTI_WORD_DICTIONARY_PATH);
	String fPath = UNLPTKConstants.MULTI_WORD_DICTIONARY_PATH;
	UNLPTKConstants.MULTI_WORD_DICTIONARY = FileOperations.fileToArrayList(fPath, StandardCharsets.UTF_8);
	System.out.println("Its Done");
}

public static void main(String[] args) throws Exception
// public static void pos() throws Exception
{
	
		//TODO Load Multi Word Dictionary, Stop Words list Here. It will always be first statement of your application.
		UNLPTKConstants.MULTI_WORD_DICTIONARY = FileOperations.fileToArrayList(UNLPTKConstants.MULTI_WORD_DICTIONARY_PATH, StandardCharsets.UTF_8);
		UNLPTKConstants.MULTI_WORD_DICTIONARY_HASH_CODES = TextOperations.getWordsHashCodes(UNLPTKConstants.MULTI_WORD_DICTIONARY);
		System.out.println(".....MultiWords Dictionary has been loaded.....");
		UNLPTKConstants.URDU_STOP_WORDS_LIST = FileOperations.fileToArrayList(UNLPTKConstants.URDU_STOP_WORDS_LIST_PATH, StandardCharsets.UTF_8);
		UNLPTKConstants.URDU_STOP_WORDS_LIST_HASH_CODES = TextOperations.getWordsHashCodes(UNLPTKConstants.URDU_STOP_WORDS_LIST);		
		System.out.println(".....StopWords List has been loaded.....");
		
		// ****************************Tokens generation code Starting from // here*********************************//
		ArrayList<String> textOfAllFiles = FileOperations.directoryToList(UNLPTKConstants.CORPUS_SOURCE_DIR, StandardCharsets.UTF_8);
		System.out.println(".....Text Files Directory has been read.....");
		
		String concatenatedTextOfAllFiles = String.join(" ", textOfAllFiles);
		System.out.println(".....All files data has been merged.....");
		
		String preProcessedText = TextOperations.urduTextPreProcessing(concatenatedTextOfAllFiles);			
		System.out.println(".....Text has been Pre-Processed.....");
		
		
		
		//TODO Getting default tokens of length 1. This will be the input of all greater lenght tokens
		ArrayList<String> tokens = TextOperations.getStringTokens(preProcessedText, UNLPTKConstants.DEFAULT_WORDS_SEPERATOR);		
		System.out.println(".....Basic Tokens of Text have been generated.....");
		System.out.println(".....Now Generating Multi Words Tokens.....");
		System.out.println(".....Pleae Wait...................................");
		//TODO Generating finalized tokens of text.
		ArrayList<String> finalTokens = TextOperations.generateTokensFromList(preProcessedText, UNLPTKConstants.MULTI_WORD_DICTIONARY_HASH_CODES, tokens);
		System.out.println(".....Final list of text tokens has been generated.....");
		
		//TODO Writing final tokens in text file at directory TokenizedFiles
		TextOperations.writeListToTextFile(UNLPTKConstants.TOKENIZED_FILE, finalTokens);
		
		//TODO Writing final tokens in .tt for TNT tagger input
		//String tokensForTNTTagger = UNLPTKConstants.TNT_TAGGER_DIR+File.separator+"tokens.tt";
		TextOperations.writeListToTextFile(UNLPTKConstants.TNT_TAGGER_DIR+File.separator+UNLPTKConstants.TNT_TAGGER_INPUT_TOKENS_FILE, finalTokens);		
		System.out.println(".....Final tokens have been written to text/.tt file.....");
	
		
		// ****************************TNT-Tagger calling & generation of POS code Starting from here*********************************//
		
		String tntTaggerCommand;		
		String workDir = UNLPTKConstants.TNT_TAGGER_DIR;		
		
		logger.info("......Training TNT Tagger....");
		
		//TODO 1. Train The Tagger
		tntTaggerCommand = "tnt-para 1train.tt";
		POSDriver.runCommand(tntTaggerCommand, workDir);
		logger.info("......Finished Training TNT Tagger....");	

		
		//TODO 2. Generating POS for Testing File
		//tntTaggerCommand = "tnt 1train 1test.tt > jawad.tts";
		tntTaggerCommand = "tnt 1train "+UNLPTKConstants.TNT_TAGGER_INPUT_TOKENS_FILE+" > "+UNLPTKConstants.TNT_TAGGER_OUTPUT_POS_FILE;
		POSDriver.runCommand(tntTaggerCommand, workDir);
		
		logger.info("......Finished Generation of POS & Testing File....");		
	}

	
	/**
	 * This function run the command using Command Prompt.
	 * @param command Command which you want to run
	 * @param workDir Working directory in which you want to run script or command
	 * @throws Exception
	 */
	public static void runCommand(String command, String workDir) throws Exception {
		logger.info("Run: " + command);	
		Process p = Runtime.getRuntime().exec("cmd /c "+command, null , new File(workDir));		
	}
	

}
