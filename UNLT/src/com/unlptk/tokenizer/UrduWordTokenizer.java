package com.unlptk.tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unlptk.driver.POSDriver;
import com.unlptk.util.Dictionary;
import com.unlptk.util.FileOperations;
import com.unlptk.util.TextOperations;
import com.unlptk.util.UNLPTKConstants;

public class UrduWordTokenizer implements Cloneable {

	private static final Logger logger = Logger.getLogger(POSDriver.class.getName());
	
	private static UrduWordTokenizer modelInstance;
	private ArrayList<String> tokens;
	
	private Dictionary dictionary;
	private Map<String, Set<String>> multiWordsDictionary;
	private Map<String, Set<String>> morphemDictionary;
	
	
	private ArrayList<String> keyWordsList;	
	private ArrayList<String> uniGramsList;			
	private ArrayList<String> biGramsList ;
	private ArrayList<String> triGramsList;
	
	private Map<String, Integer> uniGramsOfTrainingData;
	private Map<String, Integer> biGramsOfTrainingData;
	private Map<String, Integer> triGramsOfTrainingData;	
	private long noOfUniqueWordsInCorpus;
	
	
	
	//private replace with public
	public UrduWordTokenizer(){
		System.out.println("\n******************* LOADING REQUIRED RESOURCES **************************************\n");
		tokens = new ArrayList<String>();
		dictionary = Dictionary.getInstance();
		multiWordsDictionary = dictionary.getMultiWordsDictionary();
		morphemDictionary = dictionary.getMorphemsDictionary();	
		
		/*multiWordsDictionary.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));		
		System.out.println(multiWordsDictionary.size());
		System.exit(0);*/
		
		System.out.println("Loading Unigram, Bigram & Trigrams.............");
		try {
			//keyWordsList = FileOperations.fileToArrayList(UNLPTKConstants.KEY_WORDS_LIST_PATH, StandardCharsets.UTF_8);
			keyWordsList = FileOperations.fileToArrayListUsingInputStream(UNLPTKConstants.KEY_WORDS_LIST_PATH);
			//uniGramsList = FileOperations.fileToArrayList(UNLPTKConstants.UNIGRAMS_PATH, StandardCharsets.UTF_8);
			uniGramsList = FileOperations.fileToArrayListUsingInputStream(UNLPTKConstants.UNIGRAMS_PATH);
			//biGramsList = FileOperations.fileToArrayList(UNLPTKConstants.BIGRAMS_PATH, StandardCharsets.UTF_8);
			biGramsList = FileOperations.fileToArrayListUsingInputStream(UNLPTKConstants.BIGRAMS_PATH);
			//triGramsList = FileOperations.fileToArrayList(UNLPTKConstants.TRIGRAMS_PATH, StandardCharsets.UTF_8);
			triGramsList = FileOperations.fileToArrayListUsingInputStream(UNLPTKConstants.TRIGRAMS_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		uniGramsOfTrainingData = getTextGrams(uniGramsList);
		biGramsOfTrainingData = getTextGrams(biGramsList);
		triGramsOfTrainingData = getTextGrams(triGramsList);		
		noOfUniqueWordsInCorpus = uniGramsOfTrainingData.size();
		
		
	}
	
	private static String sentenceBreaker = "۰";
	//Get Instance Method
	public static UrduWordTokenizer getInstance(){
		if(modelInstance==null){
			modelInstance = new UrduWordTokenizer();
		}
		return modelInstance;
	}
	
	private ArrayList<String> getTokens(){
		return tokens;
	}
	private void setTokens(ArrayList<String> tokens){
		this.tokens = tokens;
	}
	
	/**
	 * @param args
	 * @throws IOException
	 */
//	public static void main(String[] args) throws IOException{
//		
//		List<String> fileText = FileOperations.fileToArrayList(UNLPTKConstants.INPUT_FILE, StandardCharsets.UTF_8);
//		String text = String.join(" ", fileText);
//		
//		//String text = "الیکشن کمیشن آف پاکستان مکہ مدینہ پر ایٹم بم مارنے کے ارادوں کے اظہار اور غلام مصطفی جتوئی پاکستان یمن کو الیکشن کمیشن آف پاکستان ڈرون کی نوک پر غلام مصطفی جتوئی رکھنے کے عزم کے باوجود؟";
//		
//		//String text = "جدہ..... محکمہ موسمیات نے خبردار کیا ہے کہ حج ایام کے دوران دوپہر کے وقت منیٰ اور عرفات میں درجہ حرارت 45 سینٹی گریڈ سے زیادہ رہے گا۔10 ذی الحجہ کو رمی کے دنوں میں حجاج سخت موسمی حالات سے دوچار ہوں گے۔ سرد ممالک سے آنے والے عازمین زیادہ متاثر ہوں گے۔ وزارت صحت نے 20برس قبل لو سے متاثر حجاج کو طبی سہولت مہیا کرنے کیلئے عرفات اور منیٰ میں اسپیشلسٹ سینٹر کھولے تھے۔ شدید درجہ حرارت کی وجہ سے حاجیوں کو سخت گرمی سے دوچار ہونا پڑا تھا۔ امسال بھی لو سے متاثر حجاج کی خدمت کیلئے عرفات اور منیٰ میں اسپیشلسٹ سینٹر قائم کر دیئے گئے ہیں۔";
//		
///*		
//		String regex = "[\\u0600-\\u06FF|\\u0750-\\u077F|\\uFB50-\\uFDFF|\\uFE70‌​-\\uFEFF]+\\s*\\-?\\d+\\.?\\d+\\s*[\\u0600-\\u06FF|\\u0750-\\u077F|\\uFB50-\\uFDFF|\\uFE70‌​-\\uFEFF]+";
//		
//		//String regex_date = "[u0600-u06FF][u0600-u06FF]\\۔\\[u0600-u06FF]\\[u0600-u06FF]+\\۔\\[u0600-u06FF]";
//		
//		String regex_date = "([\u0600-\u06ff-0-9]+[-|\u06D4][\u0600-\u06ff-0-9]+)";
//		
//		//String regex_date = "[u0600-u06FF][u0600-u06FF][-][u0600-u06FF][u0600-u06FF]";
//		//String sentence = "۱۲۔۱۱۔۲۰۱۷";
//		//String sentence = "۱۲۔۱۱";
//		//String sentence = "16-12-2018";
//		//String sentence = "16 جنوری 2017";
//		String sentence = "ہیں۔ہیں۔ہیں";
//		
//		
//		Pattern p = Pattern.compile(regex_date);
//		Matcher m = p.matcher(sentence);
//		
//		System.out.println(regex_date);
//		
//		while(m.find()){
//			System.out.println(m.group());
//		}
//
//		
//		System.exit(0);
//		*/
//		String inputTokensFilePath = "tokens.tt";
//		String outputFilePath 	   = "pos_of_tokens.tt";
//		
//		
//		//String text = "الیکشن کمیشن آف پاکستان مکہ مدینہ پر ایٹم بم مارنے کے ارادوں کے اظہار اور غلام مصطفی جتوئی پاکستان یمن کو الیکشن کمیشن آف پاکستان ڈرون کی نوک پر غلام مصطفی جتوئی رکھنے کے عزم کے باوجود؟";
//		//String text = "جدہ..... محکمہ موسمیات نے خبردار کیا ہے کہ حج ایام کے دوران دوپہر کے وقت منیٰ اور عرفات میں درجہ حرارت 45 سینٹی گریڈ سے زیادہ رہے گا۔10 ذی الحجہ کو رمی کے دنوں میں حجاج سخت موسمی حالات سے دوچار ہوں گے۔ سرد ممالک سے آنے والے عازمین زیادہ متاثر ہوں گے۔ وزارت صحت نے 20برس قبل لو سے متاثر حجاج کو طبی سہولت مہیا کرنے کیلئے عرفات اور منیٰ میں اسپیشلسٹ سینٹر کھولے تھے۔ شدید درجہ حرارت کی وجہ سے حاجیوں کو سخت گرمی سے دوچار ہونا پڑا تھا۔ امسال بھی لو سے متاثر حجاج کی خدمت کیلئے عرفات اور منیٰ میں اسپیشلسٹ سینٹر قائم کر دیئے گئے ہیں۔";
//		
//		UrduWordTokenizer uwt = UrduWordTokenizer.getInstance();
//		List<String> tokens = uwt.tokenize(text);
//		System.out.println("Tokens : "+ tokens);
//		System.out.println("No of Tokens : "+tokens.size());
//		
//		List<String> uniqueTokens = uwt.getUniqueTokens(tokens);
//		System.out.println("Unique Tokens : "+ uniqueTokens);
//		System.out.println("No of Unique Tokens : "+ uniqueTokens.size());
//		
//		
//		System.exit(0);
//		
//		//FileOperations.writeListToTextFile(inputTokensFilePath, new ArrayList<String>(tokens));
//		FileOperations.writeListToTextFile(UNLPTKConstants.TNT_TAGGER_INPUT_TOKENS_FILE, new ArrayList<String>(tokens));
//		
//		
//		
//		/*UrduPOSTagger upt = UrduPOSTagger.getInstance();		
//		upt.generatePOSTags(inputTokensFilePath, outputFilePath);*/
//		
//		String tntTaggerCommand;		
//		String workDir = new File(UNLPTKConstants.TNT_TAGGER_DIR).getAbsolutePath();		
//		
//		logger.info("......Training TNT Tagger....");
//		
//		//TODO 1. Train The Tagger
//		tntTaggerCommand = "tnt-para 1train.tt";
//		try {
//			runCommand(tntTaggerCommand, workDir);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("......Finished Training TNT Tagger....");	
//
//		
//		//TODO 2. Generating POS for Testing File
//		//tntTaggerCommand = "tnt 1train 1test.tt > jawad.tts";
//		tntTaggerCommand = "tnt 1train "+new File(UNLPTKConstants.TNT_TAGGER_INPUT_TOKENS_FILE).getAbsolutePath()+" > "+new File(UNLPTKConstants.TNT_TAGGER_OUTPUT_POS_FILE).getAbsolutePath();
//		try {
//			runCommand(tntTaggerCommand, workDir);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("......Finished Generation of POS & Testing File....");		
//		
//				
//	}
	
	public static void runCommand1(String exe, String param1, String param2) throws Exception {
		logger.info("......Inside param1....");
	    String line;
	    OutputStream stdin = null;
	    InputStream stderr = null;
	    InputStream stdout = null;

	      // launch EXE and grab stdin/stdout and stderr
	      Process process = Runtime.getRuntime ().exec (exe);
	      stdin = process.getOutputStream ();
	      stderr = process.getErrorStream ();
	      stdout = process.getInputStream ();

	      // "write" the parms into stdin
	      line = param1 + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();

/*	      line = "param2" + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();

	      line = "param3" + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();*/

	      stdin.close();

	      // clean up if any output in stdout
	      BufferedReader brCleanUp =
	        new BufferedReader (new InputStreamReader (stdout));
	      while ((line = brCleanUp.readLine ()) != null) {
	        //System.out.println ("[Stdout] " + line);
	      }
	      brCleanUp.close();

	      // clean up if any output in stderr
	      brCleanUp =
	        new BufferedReader (new InputStreamReader (stderr));
	      while ((line = brCleanUp.readLine ()) != null) {
	        //System.out.println ("[Stderr] " + line);
	      }
	      brCleanUp.close();
	    
	      logger.info("......end Inside param1....");
		//Process p = Runtime.getRuntime().exec("cmd /c "+command, null , new File(new File(workDir).getAbsolutePath()));
	}
	
	public static void runCommand2(String exe, String param1, String param2, String param3, String param4, String param5) throws Exception {
	    
	    String line;
	    OutputStream stdin = null;
	    InputStream stderr = null;
	    InputStream stdout = null;

	      // launch EXE and grab stdin/stdout and stderr
	      Process process = Runtime.getRuntime ().exec (exe);
	      stdin = process.getOutputStream ();
	      stderr = process.getErrorStream ();
	      stdout = process.getInputStream ();

	      // "write" the parms into stdin
	      line = param1 + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();

	      line = param2 + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();

	      line = param3 + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();
	      
	      line = param4 + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();
	      
	      line = param5 + "\n";
	      stdin.write(line.getBytes() );
	      stdin.flush();

	      stdin.close();

	      // clean up if any output in stdout
	      BufferedReader brCleanUp =
	        new BufferedReader (new InputStreamReader (stdout));
	      while ((line = brCleanUp.readLine ()) != null) {
	        //System.out.println ("[Stdout] " + line);
	      }
	      brCleanUp.close();

	      // clean up if any output in stderr
	      brCleanUp =
	        new BufferedReader (new InputStreamReader (stderr));
	      while ((line = brCleanUp.readLine ()) != null) {
	        //System.out.println ("[Stderr] " + line);
	      }
	      brCleanUp.close();
	    
	    
		//Process p = Runtime.getRuntime().exec("cmd /c "+command, null , new File(new File(workDir).getAbsolutePath()));
	}
	
	
	
	
	public static void runCommand(String command, String workDir) throws Exception {
		logger.info("Run: " + command);
		System.out.println(new File(workDir).getAbsolutePath());
		Process p = Runtime.getRuntime().exec("cmd /c "+command, null , new File(workDir));
		p.waitFor();
	    System.out.println(p.exitValue());
		
		//Process p = Runtime.getRuntime().exec("cmd /c "+command, null , new File(new File(workDir).getAbsolutePath()));
	}
	
	
	public List<String> tokenize(String text) throws IOException {
		System.out.println("\n******************* TOKENIZING TEXT **************************************");
		//TODO Removing hidden characters if any
		text = text.replaceAll("\\p{C}", "");
		//text = text.replaceAll("\\.{1,}", ".");
		
		//TODO Breaking text into sentences
		UrduSentenceTokenizer ust = new UrduSentenceTokenizer(text);
		List<String> sentences = ust.getDocSentences();
		
		/** All tokens of the given text will be available in this list*/
		List<String> allTokens = new ArrayList<String>();
		
		for(String sentence : sentences){
			sentence = sentence.replaceAll("\\p{C}", "");
			
		//	System.out.println("Processing: "+sentence);
			
			sentence = sentence.trim();			
			if(sentence.length()>0){				
				char lastChar = sentence.charAt(sentence.length()-1);				
				if(lastChar != '۔' && lastChar != '؟' && lastChar != '.' && lastChar != '!'){					
					sentence = sentence.trim().concat("۔");
				}
			}
			sentence=sentence.replaceAll("\\s{2,}", " ").trim();
			//System.out.println("Under Processing : "+ sentence);
			List<String>  tempTokensList = preProcessingForTokenizationProcess(sentence);
			
			allTokens.addAll(tempTokensList);
		}
		System.out.println("******************* TOKENIZATION COMPLETED SUCCESSFULLY *******************\n");
//		System.out.println(allTokens);
		return allTokens;
	}

	private List<String> preProcessingForTokenizationProcess(String text) throws IOException{
		
		List<String> inputTextTokens = new ArrayList<String>(Arrays.asList(text.split(" ")));		
		int subStringLength = 4;
		int fromIndex = 0;
		int toIndex   = subStringLength;
		String strWriteToFile="";
		//System.out.println(inputTextTokens.size());
		List<String> subStringsList = new ArrayList<String>();
		
		do{
			
			if(fromIndex < inputTextTokens.size() && toIndex < inputTextTokens.size()){
				List<String> subList = inputTextTokens.subList(fromIndex, toIndex);
				String subString = String.join(" ", subList).concat(sentenceBreaker);
				subStringsList.add(subString);
				//strWriteToFile = strWriteToFile +subString+"\n";			
				fromIndex = toIndex;
				toIndex   = toIndex+subStringLength;
			}
			
			
		}while(fromIndex < inputTextTokens.size() && toIndex < inputTextTokens.size());
		//System.out.println(strWriteToFile);
		
		toIndex = inputTextTokens.size();
		List<String> subList = inputTextTokens.subList(fromIndex, toIndex);
		String subString = String.join(" ", subList);
		subStringsList.add(subString);
		//subString = subString.replaceFirst("۔", "");
		//strWriteToFile = strWriteToFile +subString;
		
		//TextOperations.writeToTextFile(UNLPTKConstants.INPUT_TEXT_SUB_STRINGS, strWriteToFile);
		//FileOperations.writeTextToFileUsingFileOutputStream(UNLPTKConstants.INPUT_TEXT_SUB_STRINGS, strWriteToFile);
		
		//System.exit(0);
		
		//List<String> subStrings = FileOperations.fileToArrayList(UNLPTKConstants.INPUT_TEXT_SUB_STRINGS, StandardCharsets.UTF_8);
		
		//System.out.println(subStringsList);
		
		List<String> allTokens = new ArrayList<String>();
		
		
		
		//UrduWordTokenizer uwt = UrduWordTokenizer.getInstance();
		for(int i=0;i<subStringsList.size();i++){
			String str = subStringsList.get(i);
			//str = str.replaceAll("\\p{C}", "");
			
			//System.out.println("******************* Processing chunk no : "+ i +", Text : "+str+"*******************");
			List<String> strTokens = null;
			try {
				strTokens = wordTokenizer(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			/*if(i<subStrings.size()-1)
				strTokens = uwt.wordTokenizer(str.concat(sentenceBreaker));
			else
				strTokens = uwt.wordTokenizer(str);*/
			
			if(strTokens !=null && strTokens.size()>0)
			allTokens.addAll(strTokens);
		}
		while(allTokens.remove(sentenceBreaker)) {}
		
		
		/*System.out.println("Before Cleaning");
		System.out.println(allTokens.size());
		System.out.println(allTokens);*/		
		try{
			allTokens = finalOperationsToCleanTokenizedList(text,allTokens);
		}catch(Exception e){
			//System.out.println("Exception:preProcessingForTokenizationProcess() ");
		}	
		//System.exit(0);
		
		/*System.out.println("After Cleaning");
		System.out.println(allTokens.size());
		System.out.println(allTokens);*/
		return allTokens;
	}
	
	public List<String> getUniqueTokens(List<String> tokens){		
		Set<String> uniqueTokens = new HashSet<String>(tokens);		
		return new ArrayList<String>(uniqueTokens);
	}
		

	private List<String> finalOperationsToCleanTokenizedList(String inputText,
			List<String> allTokens) throws Exception {
		
		Dictionary dic = Dictionary.getInstance();
		UrduWordTokenizer uwt = UrduWordTokenizer.getInstance();
		// TODO proceed to multi word expression matching			
		//List<String> tokensList = tokenizedRowAfterAllSpaceOmissionOperations.getRowElements();
		
		/**Removing Sentence Breaker*/
		//System.out.println("**************Removing Sentence Breaker**************");	
		for(int i=0;i<allTokens.size();i++){
			String token = allTokens.get(i);
			if(token.contains(sentenceBreaker)){
				token = token.substring(0, token.length()-1);
				allTokens.remove(i);
				allTokens.add(i,token);
			}			
		}
		
		//System.out.println("Final Tokens List: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
		
		//System.out.println("Final Tokens List Before Punctuation Marks: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
				
		/**Handle punctuation marks*/
		//System.out.println("**************Hanlding Punction Marks**************");
		handlePunctuationMarks(allTokens);
		handlePunctuationMarks(allTokens);
		handlePunctuationMarks(allTokens);
		//System.out.println("Final Tokens List After Punctuation Marks: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
		
		/**Handling Terminator if comes with in tokens*/
		//System.out.println("**************Handing Terminator Marker with in Token**************");	
		separateTerminatorWithinToken(allTokens);		
		//System.out.println("Final Tokens List: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
		
		/**Removing Blank Cells*/
		//System.out.println("**************Removing Blank Cells**************");	
		for(int i=0;i<allTokens.size();i++){
			String token = allTokens.get(i);
			if((token.contains(" ") && token.length()==1) || token.length()==0){				
				allTokens.remove(i);				
			}			
		}
		
		//System.out.println("Final Tokens List: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
		
		
		/**Handle Key Words*/
		//System.out.println("**************Hanlding Key Words**************");
		handleCombinedKeyWords(uwt.keyWordsList, allTokens);
		//System.out.println("Final Tokens List: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
		
		
		/**Tokenization of digits in the given input string*/
		//System.out.println("**************Hanlding Digits in Text**************");
		try{
		getFinalTokensListWithDigits(inputText, allTokens);
		}catch(Exception e){
			//System.out.println("Hanlding digits exception");
		}
		//System.out.println("Final Tokens List After Handling Digits: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
		

		/**Handle Space Insertion*/
		//System.out.println("**************Hanlding Space Insertion**************");
		String sentenceFromTokensList = String.join(" ", allTokens);			
		allTokens = getFinalizedTokensListBySpaceInsertion(dic.getMultiWordsDictionary(),allTokens, sentenceFromTokensList);
		//System.out.println("Final Tokens List After Handling Space Insertion: "+allTokens);
		//System.out.println("No. of Tokens : "+allTokens.size());
		
		
		
		/*System.out.println("**************After All Operations**************");
		System.out.println("Final Tokens List: "+allTokens);
		System.out.println("No. of Tokens : "+allTokens.size());*/
		
		
		return allTokens;
	}

	private static void separateTerminatorWithinToken(List<String> allTokens) {
		for(int i=0;i<allTokens.size();i++){
			
			String token = allTokens.get(i);
			if(token.length() > 1)				
				if(token.contains("۔") || token.contains("؟") || token.contains(sentenceBreaker)){
					String lastChar = token.substring(token.length() - 1);
					token = token.substring(0, token.length()-1);										
					allTokens.remove(i);					
					allTokens.add(i,lastChar);
					allTokens.add(i,token);
					i++;
				}			
		}
	}
	
	
	private List<String> wordTokenizer(String inputText) throws IOException {
		// TODO Load all required resources.				
		
				String concatenatedTextOfAllFiles = inputText;
		
		String originalInputText = concatenatedTextOfAllFiles;
		
		String numbersRegex = "-?\\d+\\.?\\d+";
		concatenatedTextOfAllFiles = concatenatedTextOfAllFiles.replaceAll(numbersRegex, "");
		concatenatedTextOfAllFiles = concatenatedTextOfAllFiles.replaceAll("\\s{2,}", " ");
		 
				 
		String textAfterSpaceRemoval = concatenatedTextOfAllFiles.replaceAll(
				" ", "").trim();
		
		String originalTextAfterSpaceRemoval = textAfterSpaceRemoval;
		
		/*System.out.println("Input Text - Original  : "
				+ concatenatedTextOfAllFiles);
		System.out.println("Input Text - After Space Removal "
				+ textAfterSpaceRemoval);
		// TODO To normalize the input string for bigram reading
		// i.e. Making string length Even, if its original length is ODD
		System.out.println("Text Length Before Normalization "
				+ textAfterSpaceRemoval.length());
		textAfterSpaceRemoval = makeTextLengthEven(textAfterSpaceRemoval);
		System.out.println("Text Length After Normalization "
				+ textAfterSpaceRemoval.length());*/

		
		// System.out.println("Dictionary Size : "
			//		+ morphemDictionary.getMorphemsMap().size());

		 /**Making morphems of input text and adding to the existing dictionary*/
		 generateMorphemsOfInputTextAndAddingToTheExistingMorphemsDictionary(
				concatenatedTextOfAllFiles, dictionary);
		
		//morphemDictionary.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
		
		
		// System.out.println("Processing Started.............................");
		/** This will contains all rows of the matrix */
		List<Row> allRows = new ArrayList<Row>();

		// TODO Read Bigram of Input String
		int startIndex = 0;
		int endIndex = startIndex + 2;
		int rowUnderProcessingIndex = 0;
		boolean isBigramExistsInMorphemsDictionary = false;

		do {
			//System.out.println("Input Bigram Index : " + startIndex + "->"
				//	+ endIndex);
			String inputBigram = null;
			String remainingString = null;
			try{
				if(textAfterSpaceRemoval.length()>1){
					inputBigram = textAfterSpaceRemoval.substring(startIndex,endIndex);	
					remainingString = textAfterSpaceRemoval.substring(endIndex);
				}else{
					inputBigram = textAfterSpaceRemoval;	
					remainingString = textAfterSpaceRemoval.substring(endIndex);
				}
			}catch(Exception e){
				//System.out.println("Exception:Wordtokenizer()");
			}
			//if(lastChar != '۔' && lastChar != '؟' && lastChar != '.' && lastChar != '!'){
			if (inputBigram.contains("۔") 
				||	inputBigram.contains("؟") 
				|| 	inputBigram.contains(sentenceBreaker)
				|| 	inputBigram.contains("!")	
				|| 	inputBigram.contains(".")) {
				//System.out
						//.println("****************Sentence Marker Found************************");
				// TODO adding sentence marker to the under processed row
				addSentenceMarkerToTheRowUnderProcess(allRows,
						rowUnderProcessingIndex, inputBigram);
				// printMatrix(allRows);

				// TODO Setting to start next row processing
				rowUnderProcessingIndex++;
				if (allRows.size() > 1
						&& rowUnderProcessingIndex < allRows.size()) {
					Row row = allRows.get(rowUnderProcessingIndex);
					if (row.isFlagBit() == false) {
						/*System.out
								.println("||||||||||||*****************************************************************************|||||||||||||||");
						System.out
								.println("||||||||||||*****************************************************************************|||||||||||||||");
						System.out
								.println("||||||||||||**********************Row No : "
										+ rowUnderProcessingIndex
										+ " Processing Started**************************|||||||||||||||");
						System.out
								.println("||||||||||||*****************************************************************************|||||||||||||||");
						System.out
								.println("||||||||||||*****************************************************************************|||||||||||||||");
*/
						List<String> rowElements = row.getRowElements();
						String allElementsOfRow = String.join("", rowElements);

/*						System.out
								.println("Original Text After Space Removal : "
										+ originalTextAfterSpaceRemoval);
						System.out.println("Last Element of Row : "
								+ allElementsOfRow);*/

						int indexOfLastElementOfRowInOriginalText = originalTextAfterSpaceRemoval
								.indexOf(allElementsOfRow);
						// System.out.println("Index of Last Element of Row : "+indexOfLastElementOfRowInOriginalText);
						indexOfLastElementOfRowInOriginalText = indexOfLastElementOfRowInOriginalText
								+ allElementsOfRow.length();
						// System.out.println("Index of Last Element of Row : "+indexOfLastElementOfRowInOriginalText);

						remainingString = originalTextAfterSpaceRemoval
								.substring(indexOfLastElementOfRowInOriginalText);
						// originalTextAfterSpaceRemoval =
						// originalTextAfterSpaceRemoval.substring(indexOfLastElementOfRowInOriginalText);

						//System.out.println("Remaining Original Text : "+ remainingString);

						textAfterSpaceRemoval = makeTextLengthEven(remainingString);
						// textAfterSpaceRemoval =
						// makeTextLengthEven(originalTextAfterSpaceRemoval);
						//System.out.println("Remaining Text After even length: "
						//		+ textAfterSpaceRemoval);

						startIndex = 0;
						endIndex = startIndex + 2;
						if(textAfterSpaceRemoval.length()>0)
						inputBigram = textAfterSpaceRemoval.substring(startIndex, endIndex);
						//System.out.println("InputBigram: " + inputBigram);
					}

				} else {
					//System.out
							//.println("************All Rows Processed*********");
					break;
				}

			}

			//System.out.println("Input Bigram : " + inputBigram);
			Set<String> possibleMorphems = morphemDictionary.get(inputBigram);
			//System.out.println("Possible Morphems : " + possibleMorphems);

			if (possibleMorphems != null)
				isBigramExistsInMorphemsDictionary = possibleMorphems
						.contains(inputBigram);

			// if (isBigramExistsInMorphemsDictionary) {
			if (possibleMorphems != null) {
				//System.out.println("Bigram " + inputBigram
					//	+ " matchings found in morphems dictionary");

				if (allRows.size() > 0) {
					if (possibleMorphems.size() > 0) {
						List<String> tempList = new ArrayList<String>(
								possibleMorphems);
						for (int i = 1; i < tempList.size(); i++) {
							String nextMorphem = tempList.get(i);
							//System.out.println("Element from Temp List : "
								//	+ nextMorphem);
							if (isMorphemExistsInInputSequence(
									textAfterSpaceRemoval, nextMorphem)) {
								copyExistingRowToNewRowWithElement(allRows,
										rowUnderProcessingIndex, nextMorphem);
							}
						}// end for loop

						// TODO adding input bigram to the row which is under
						// processing now
						if (isMorphemExistsInInputSequence(
								textAfterSpaceRemoval, tempList.get(0)))
							addInputBigramToRowUnderProcess(allRows,
									rowUnderProcessingIndex, tempList.get(0));
					} else {
						// TODO adding input bigram to the row which is under
						// processing now
						addInputBigramToRowUnderProcess(allRows,
								rowUnderProcessingIndex, inputBigram);
					}
										
				} else if (rowUnderProcessingIndex == 0) {
					//System.out.println("***** First Row Processing *****");
					firstInputBigramReadingOfTheProcess(allRows, inputBigram,
							possibleMorphems);					
				}// end if

				startIndex = endIndex;
				endIndex = startIndex + 2;
				// printMatrix(allRows);
			} else {
				// TODO If Input BIGRAM not found in the input string
				/*System.out
						.println("-----------------------Bigram "
								+ inputBigram
								+ " not found in morphems dictionary------------------------");*/
				// TODO Split input bigram to unigram
				// char[] uniGrams = inputBigram.toCharArray();
				String firstChar = inputBigram.substring(0, 1);
				String secondChar = inputBigram.substring(1);
				//System.out.println("first char : " + firstChar);
				//System.out.println("second char : " + secondChar);

				// TODO merge character with the last element of the current row
				// under process
				mergeCharacterWithLastElementOfTheRow(
						originalTextAfterSpaceRemoval, allRows,
						rowUnderProcessingIndex, firstChar);
				// TODO update flag bit of the rowUnderProcessing
				boolean flagBit = true;
				updateFlagBitOfTheRow(allRows, rowUnderProcessingIndex, flagBit);
				// printMatrix(allRows);
				// System.exit(0);
				// TODO Normalizing text to even length.
				if(textAfterSpaceRemoval.length()>0)
				remainingString = textAfterSpaceRemoval.substring(endIndex - 1);
				
				textAfterSpaceRemoval = makeTextLengthEven(remainingString);			
				startIndex = 0;
				endIndex = startIndex + 2;
			}

			 //printMatrix(allRows);
			isBigramExistsInMorphemsDictionary = false;
			//System.out
				//	.println("********************************************************************************");
		} while (endIndex <= textAfterSpaceRemoval.length());
		//printMatrix(allRows);
		//System.out.println("No. of Matrix Rows :" + allRows.size());
		//TODO Filtering rows by comparing original input text with combined tokenized text of each row
		List<Row> filteredRows = new ArrayList<Row>();
		//WordTokenizerV3 wt3 = new WordTokenizerV3();
		filteredRows = getFilteredRows(originalTextAfterSpaceRemoval,allRows);

		//System.out.println("No. of Filtered Rows : "+filteredRows.size());
		//printMatrix(filteredRows);
		// TODO count if there are duplicate values
		
		List<Row> selectedRows =null;
		
		if(filteredRows != null && filteredRows.size()>0){
			//System.out.println("Inside Filtered Rows If");
			sortRowsInAscOrderOfTokens(filteredRows);			
			selectedRows = getTopSortedRowsWithEqualNoOfTokens(filteredRows);
		}else if(allRows !=null && allRows.size()>0){
			//System.out.println("Inside All Rows If");
			sortRowsInDscOrderOfTokens(allRows);			
			selectedRows = getTopSortedRowsWithEqualNoOfTokens(allRows);
		}

		//System.out.println("No. of Selected Rows :" + allRows.size());
		//System.out.println("Selected Rows : "+selectedRows.get(0).getRowElements());
		//printMatrix(selectedRows);
		
		
		
		/************** Finalizing tokenized list ******************/
		List<String> finalizedTokensList = null;
		Row tokenizedRowAfterAllSpaceOmissionOperations = null;
		//ArrayList<String> finalListOfTokens = new ArrayList<String>();

		if(selectedRows != null && selectedRows.size()>0){
			if (selectedRows.size() == 1) {
				//System.out.println("Inside Selected Rows with size 1: ");
				// printMatrix(selectedRows);
				// finalListOfTokens.addAll(selectedRows.get(0).getRowElements());
				tokenizedRowAfterAllSpaceOmissionOperations = selectedRows
						.get(0);
			} else if (selectedRows.size() > 1) {
				//System.out.println("Inside Selected Rows with size > 1: ");
				List<Row> finalRowsWithFlagBitFalse = new ArrayList<Row>();
				for (Row row : selectedRows) {
					if (!row.isFlagBit()) {
						finalRowsWithFlagBitFalse.add(row);
					}
				}
	
				// TODO Checking false bit
				if (finalRowsWithFlagBitFalse.size() == 1) {
					//System.out
						//	.println("Inside finalized Rows with size = 1 and flag bit false: ");
					
					tokenizedRowAfterAllSpaceOmissionOperations = finalRowsWithFlagBitFalse
							.get(0);
				} else if (finalRowsWithFlagBitFalse.size() > 1) {
					/************** apply trigram/bigram MLE along with smoothing ******************/
					// TODO Handling selected rows with all flagbit true
					//System.out
						//	.println("Inside Selected Rows more than 1 rows with flagbit false");
					
					// TODO Extracting final tokens row by tri/bi-gram along with
					// smoothing
					tokenizedRowAfterAllSpaceOmissionOperations = getFinalizedTokensListByMLEAndSmoothing(
							uniGramsOfTrainingData, biGramsOfTrainingData,
							triGramsOfTrainingData, noOfUniqueWordsInCorpus,
							finalRowsWithFlagBitFalse);
	
				} else {
					//System.out
					//.println("Inside Selected Rows more than 1 rows with all flag bit True");
					tokenizedRowAfterAllSpaceOmissionOperations = getFinalizedTokensListByMLEAndSmoothing(
							uniGramsOfTrainingData, biGramsOfTrainingData,
							triGramsOfTrainingData, noOfUniqueWordsInCorpus,
							selectedRows);
				}
			}
		}

		
		/*
		
		// TODO proceed to multi word expression matching			
		List<String> tokensList = tokenizedRowAfterAllSpaceOmissionOperations.getRowElements();
		
		System.out.println("Final Tokens List Before Punctuation Marks: "+tokensList);
		System.out.println("No. of Tokens : "+tokensList.size());
		
		*//**Handle punctuation marks*//*
		System.out.println("**************Hanlding Punction Marks**************");
		handlePunctuationMarks(tokensList);
		handlePunctuationMarks(tokensList);
		System.out.println("Final Tokens List After Punctuation Marks: "+tokensList);
		System.out.println("No. of Tokens : "+tokensList.size());
		
		
		*//**Handle Space Insertion*//*
		System.out.println("**************Hanlding Space Insertion**************");
		String sentenceFromTokensList = String.join(" ", tokensList);			
		finalizedTokensList = getFinalizedTokensListBySpaceInsertion(multiWordsDictionary,
				tokensList, sentenceFromTokensList);
		System.out.println("Final Tokens List After Handling Space Insertion: "+finalizedTokensList);
		System.out.println("No. of Tokens : "+finalizedTokensList.size());
		
		
		*//**Tokenization of digits in the given input string*//*
		System.out.println("**************Hanlding Digits in Text**************");
		getFinalTokensListWithDigits(originalInputText, finalizedTokensList);
		System.out.println("Final Tokens List After Handling Digits: "+finalizedTokensList);
		System.out.println("No. of Tokens : "+finalizedTokensList.size());
		
		
		
		*//**Handle Key Words*//*
		System.out.println("**************Hanlding Key Words**************");
		handleCombinedKeyWords(keyWordsList, finalizedTokensList);
		System.out.println("Final Tokens List: "+finalizedTokensList);
		System.out.println("No. of Tokens : "+finalizedTokensList.size());
		
		
		
		
		System.out.println("**************After All Operations**************");
		System.out.println("Final Tokens List: "+finalizedTokensList);
		System.out.println("No. of Tokens : "+finalizedTokensList.size());*/
		List<String> tokenizedRow = null;
		if(tokenizedRowAfterAllSpaceOmissionOperations != null)
		tokenizedRow = tokenizedRowAfterAllSpaceOmissionOperations.getRowElements();
		
		//return tokenizedRowAfterAllSpaceOmissionOperations.getRowElements();
		return tokenizedRow;
	}// end wordTokenizer()

	
	private static void handleCombinedKeyWords(ArrayList<String> keyWordsList,
			List<String> finalizedTokensList) {
		for(int i=0;i<finalizedTokensList.size();i++){
			String token = finalizedTokensList.get(i);
			for(int j=0;j<keyWordsList.size();j++){
				String keyWord = keyWordsList.get(j);
				String keyWordWithOutSpace = keyWord.replace(" ", "");
				
				if(token.equals(keyWordWithOutSpace)){
					//System.out.println("KeyWord :"+keyWord);
					//System.out.println("KeyWord without space:"+keyWordWithOutSpace);
					String[] words = keyWord.split(" ");
					finalizedTokensList.remove(i);
					if(words.length>1)
					finalizedTokensList.add(i,words[1]);
					finalizedTokensList.add(i,words[0]);
					i++;
					break;
				}
			}
		}
	}

	private static void handlePunctuationMarks(List<String> finalizedTokensList) {
		/** Handling punct marks before token*/
		//String regex = "[\\p{Punct}~`؛)('’‘]،";
		String regex = "ا?[\\p{Punct}~`؛)(،'’‘]";
		
		for(int i=0;i<finalizedTokensList.size();i++){
			String token = finalizedTokensList.get(i);
			//String regex = "[\\p{Punct}~`؛)(،'’‘!]";
			
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(token);

			if(matcher.find()){
				/*System.out.println(token);
				System.out.println("Start Index : "+matcher.start());
				System.out.println("End Index : "+matcher.end());
				System.out.println("Token Length : "+token.length());*/
				
				if(matcher.start()==0 && token.length()>1){
					finalizedTokensList.add(i, token.charAt(0)+"");					
					i++;
					finalizedTokensList.remove(i);
					token = token.substring(1);						
					finalizedTokensList.add(i, token);
				}
			}
		}//end for
		
		/*System.out.println("Final Tokens List: "+finalizedTokensList);
		System.out.println("No. of Tokens : "+finalizedTokensList.size());*/
		
		/** Handling punct marks after token*/
		for(int i=0;i<finalizedTokensList.size();i++){
			String token = finalizedTokensList.get(i);
			
			//String regex = "[\\p{Punct}~`؛)(،'’‘]";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(token);
			
			//if(token.contains("’") || token.contains("(")){
			//if("\\p{Punct}".contains(token.charAt(0)+"")){
			if(matcher.find()){
				/*System.out.println(token);
				System.out.println("Start Index : "+matcher.start());
				System.out.println("End Index : "+matcher.end());
				System.out.println("Token Length : "+token.length());*/
				
				if(matcher.end()==token.length() && token.length()>1){
					//System.out.println("Matched at the End Index : "+matcher.end());
					finalizedTokensList.remove(i);
					finalizedTokensList.add(i,token.trim().charAt(token.length()-1)+"");							
					finalizedTokensList.add(i,token.substring(0,token.trim().length()-1));
					i++;
				}					
			}
		}
		
		//System.out.println("Final Tokens List Before Punctuation Marks: "+finalizedTokensList);
		//System.out.println("No. of Tokens : "+finalizedTokensList.size());
	}

	private static void generateMorphemsOfInputTextAndAddingToTheExistingMorphemsDictionary(
			String concatenatedTextOfAllFiles, Dictionary morphemDictionary) {
		
		morphemDictionary.getMorphemsDictionary().clear();
		//String inputTextAfterRemovingFirstSpace = concatenatedTextOfAllFiles
			//	.substring(1, concatenatedTextOfAllFiles.length() - 1);
		
		String inputTextAfterRemovingFirstSpace = concatenatedTextOfAllFiles;
		
		List<String> inputTextTokens = new ArrayList<String>(
				Arrays.asList(inputTextAfterRemovingFirstSpace.trim()
						.split(" ")));
		for (String word : inputTextTokens) {
			if (word.length() > 1) {
				String key = word.trim().substring(0, 2);
				for (int i = 2; i <= word.length(); i++) {

					String morphem = word.substring(0, i);
					// System.out.println("Key: "+key+", Morpheme"+morphem);
					morphemDictionary.addMorphem(key, morphem);
				}
			} else {
				morphemDictionary.addMorphem(word, word);
			}
			// System.out.println("------------------------------------------------");
		}

		// System.out.println("Dictionary Size : "+
		// morphemDictionary.getMorphemsMap().size());
		//morphemDictionary.getMorphemsMap().forEach(
			//	(k, v) -> System.out.println("Key: " + k + ": Value: " + v));

		// System.exit(0);
	}

	private static void getFinalTokensListWithDigits(String originalInputText,
			List<String> finalizedTokensList) {
		//String regex = "[\\u0600-\\u06FF|\\u0750-\\u077F|\\uFB50-\\uFDFF|\\uFE70‌​-\\uFEFF]+\\s*\\-?\\d+\\s*[\\u0600-\\u06FF|\\u0750-\\u077F|\\uFB50-\\uFDFF|\\uFE70‌​-\\uFEFF]+";
		String regex = "[\\u0600-\\u06FF|\\u0750-\\u077F|\\uFB50-\\uFDFF|\\uFE70‌​-\\uFEFF]+\\s*\\-?\\d+\\.?\\d+\\s*[\\u0600-\\u06FF|\\u0750-\\u077F|\\uFB50-\\uFDFF|\\uFE70‌​-\\uFEFF]+";
		// String regex =
		// "[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF]+\\s{1,}\\-?\\d+\\s{1,}[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF]+ | -?\\d+\\s{1,}[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF]+";
		// String regex =
		// "[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF]+?[,\\s]?-?\\d+[,\\s]?[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF]+";

		//System.out.println("Original Input Text : "+originalInputText);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(originalInputText.replaceAll(
				"\\s{2,}", " "));
		// Check all occurrences
		while (matcher.find()) {
		/*	System.out.print("Start index: " + matcher.start());
			System.out.print(" End index: " + matcher.end());
			System.out.println(" Found: " + matcher.group());
*/
			String[] matchedSubString = matcher.group().split(" ");

			String previousTextPart = null;
			String numberPart = null;
			String nextTextPart = null;

			if (matcher.start() != 0) {
				previousTextPart = matchedSubString[0];
				numberPart = matchedSubString[1];
				nextTextPart = matchedSubString[2];

			} else {
				previousTextPart = "";
				numberPart = matchedSubString[0];
				nextTextPart = matchedSubString[1];
			}

			/*System.out.println(" Part1: " + previousTextPart);
			System.out.println(" Part2: " + numberPart);
			System.out.println(" Part3: " + nextTextPart);*/

			for (int i = 1; i < finalizedTokensList.size(); i++) {
				String currentToken = finalizedTokensList.get(i);
				String previousToken = finalizedTokensList.get(i - 1);
				

				//if (matcher.start() != 0 && nextTextPart.contains(currentToken)
				if (matcher.start() != 0 && currentToken.contains(nextTextPart)
						&& previousTextPart.contains(previousToken)) {
					/*System.out.println("inside start index !0");
					System.out.println(currentToken);
					System.out.println(previousToken);*/
					finalizedTokensList.add(i, numberPart);
					break;
				} else if (matcher.start() == 0) {
					//System.out.println("inside start index 0");
					finalizedTokensList.add(i - 1, numberPart);
					break;
				}
			}

		}

		// System.out.println(finalizedTokensList);

		/*
		 * for(String token:finalizedTokensList){ System.out.println(token); }
		 */
	}

	private static List<Row> getTopSortedRowsWithEqualNoOfTokens(List<Row> rows) {
		List<Row> selectedRows = new ArrayList<Row>();
		int tokens = rows.get(0).getNoOfTokens();
		selectedRows.add(rows.get(0));

		for (int i = 1; i < rows.size(); i++) {
			int nextTokens = rows.get(i).getNoOfTokens();

			if (tokens == nextTokens) {
				selectedRows.add(rows.get(i));
			} else {
				break;
			}
		}
		//System.out.println("No. of Selected Rows :" + selectedRows.size());
		return selectedRows;
	}

	private static void sortRowsInAscOrderOfTokens(List<Row> rows) {
		Collections.sort(rows, new Comparator<Row>() {
			@Override
			public int compare(Row r1, Row r2) {
				return r1.getNoOfTokens() - r2.getNoOfTokens();
			}
		});
	}

	private static void sortRowsInDscOrderOfTokens(List<Row> rows) {
		Collections.sort(rows, new Comparator<Row>() {
			@Override
			public int compare(Row r1, Row r2) {
				return r2.getNoOfTokens() - r1.getNoOfTokens();
			}
		});
	}

	public static List<Row> getFilteredRows(
			String originalTextAfterSpaceRemoval, List<Row> allRows) {
		//System.out.println("No. of Matrix Rows :" + allRows.size());
		List<Row> filteredRows = new ArrayList<Row>();

		for (Row row : allRows) {
			String rowCombinedText = String.join("", row.getRowElements())
					.replace(" ", "");
			// rowCombinedText = rowCombinedText.replace(" ","");

			if (rowCombinedText.equals(originalTextAfterSpaceRemoval)) {
				/*
				 * System.out.println("Original Text :" +
				 * originalTextAfterSpaceRemoval + "," +
				 * originalTextAfterSpaceRemoval.length());
				 */
				/*
				 * System.out.println("Row Combined Text Text :" +
				 * rowCombinedText + "," + rowCombinedText.length());
				 */
				filteredRows.add(row);
				/*
				 * System.out .println("Row No : " + allRows.indexOf(row) +
				 * ", Row:" + row.getRowElements() + ", NoOfTokens : " +
				 * row.getNoOfTokens() + ", FlagBit :" + row.isFlagBit());
				 */
			}
		}
		//System.out.println("No. of Filtered Rows :" + filteredRows.size());

		return filteredRows;
	}
	
	
	private static String findLongestMatch(String regex, String s) {
		//regex = "(" + regex + ")$";
		//System.out.println(regex);
	    Pattern pattern = Pattern.compile("(" + regex + ")$");		
		//Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(s);
	    String longest = null;
	    int longestLength = -1;
	    for (int i = s.length(); i > longestLength; i--) {
	        matcher.region(0, i);
	        if (matcher.find() && longestLength < matcher.end() - matcher.start()) {
	            longest = matcher.group();
	            longestLength = longest.length();
	        }
	    }
	    return longest;
	}
	
	
	
	private static List<String> getFinalizedTokensListBySpaceInsertion(
			Map<String, Set<String>> multiWordsDictionary, List<String> tokensList,
			String sentenceFromTokensList) {
		
		sentenceFromTokensList=sentenceFromTokensList.replaceAll("\\s{2,}", " ").trim();
		
		List<String> finalTokensList = new ArrayList<String>();
		Iterator<String> tokensIterator = tokensList.iterator();
		
		while (tokensIterator.hasNext()) {
			String token = tokensIterator.next();
			
			
			if (!multiWordsDictionary.containsKey(token)) {
				if (sentenceFromTokensList.contains(token))
					finalTokensList.add(token);
			} else {
				//System.out.println("Token "+token +","+token.length()+" found in multiword dictionary");
				boolean isPosWordExistsInInputSentence = false;
				Set<String> possibleWords = multiWordsDictionary.get(token);
				//System.out.println("Possible Words : "+ possibleWords);
				
				//String regex = "(?<m1>(hello|universe))$|(?<m2>(hello world))$|(?<m3>(hello world there))";
				
				
				String regex = "";
				int counter = 1; 	
				/*for(String mw : possibleWords){
					regex = regex+"(?<m";
					regex = regex+counter+">(";
					mw=mw.replaceAll("\\s{2,}", " ").trim();
					regex= regex+mw+"))$|";
					//regex= regex+mw+"))|";
					counter++;
				}*/
				
				for(String mw : possibleWords){
					regex = regex+"(";
					//regex = regex+counter+">(";
					mw=mw.replaceAll("\\s{2,}", " ").trim();
					regex= regex+mw+")|";
					//regex= regex+mw+"))|";
					//counter++;
				}
				
				regex = regex.substring(0, regex.length()-1).trim();
				
				
				
				
				
				String longestMatch = findLongestMatch(regex, sentenceFromTokensList);				
/*				System.out.println(regex);
				System.out.println(sentenceFromTokensList);
				System.out.println("longest match : "+longestMatch);
*/				//System.exit(0);
				
				
				if(longestMatch != null){		 
				        
				        isPosWordExistsInInputSentence = true;
				        finalTokensList.add(longestMatch);
				        
				        
				        Pattern p = Pattern.compile(longestMatch);
					    Matcher m = p.matcher(sentenceFromTokensList);
				        
					    m.find();
					    
					    //while(m.find()) {				        
					    sentenceFromTokensList = sentenceFromTokensList.substring(m.end()).trim();
					    //System.out.println("Remaining Sentence: " + sentenceFromTokensList);
				        
				        List<String> tempTokensList = new ArrayList<String>();
						for (String w : sentenceFromTokensList.trim().split(" ")) {
							tempTokensList.add(w.trim());
						}
						tokensIterator = tempTokensList.iterator();
						tokensList = tempTokensList;			        
				        //break;
				}
				
				
				
				
				
			/*	String regex = "";
				for(String mw : possibleWords){
					mw=mw.replaceAll("\\s{2,}", " ").trim();
					regex= regex+mw+"|";
				}
				regex = regex.substring(0, regex.length()-1);				
				System.out.println(regex);
				System.out.println(sentenceFromTokensList);*/
				
				/*Pattern p = Pattern.compile(regex);
			    Matcher m = p.matcher(sentenceFromTokensList);   // get a matcher object
			    int count = 0;

			  
			    while(m.find()) {
			        count++;
			        System.out.println("Match number "+count);
			        System.out.println("start(): "+m.start());
			        System.out.println("end(): "+m.end());
			        System.out.println("end(): "+m.group());
			        
			       
			        
			        
			        
			        
			        String matchedWord = m.group();
			        
			        isPosWordExistsInInputSentence = true;
			        finalTokensList.add(matchedWord);
			        
			        sentenceFromTokensList = sentenceFromTokensList.substring(m.end()).trim();
			       // System.out.println("Remaining Sentence: " + sentenceFromTokensList);
			        
			        List<String> tempTokensList = new ArrayList<String>();
					for (String w : sentenceFromTokensList.trim().split(" ")) {
						tempTokensList.add(w.trim());
					}
					tokensIterator = tempTokensList.iterator();
					tokensList = tempTokensList;			        
			        break;
			    }*/
			    
			    //If there is no match found in the multiword dictionary according to the input sentence
				if(!isPosWordExistsInInputSentence){
					finalTokensList.add(token);
				}
			   			
			}//end if/else
			
		}//end while
		//System.exit(0);
		return finalTokensList;
		
	}
	
	
/*
	private static List<String> getFinalizedTokensListBySpaceInsertion(
			Map<String, Set<String>> multiWordsDictionary, List<String> tokensList,
			String sentenceFromTokensList) {

		*//** Removing last space from the string *//*
		StringBuilder b = new StringBuilder(sentenceFromTokensList);
		b.replace(sentenceFromTokensList.lastIndexOf(" "),
				sentenceFromTokensList.lastIndexOf(" ") + 1, "");
		sentenceFromTokensList = b.toString();

		
		List<String> finalTokensList = new ArrayList<String>();

		Iterator<String> tokensIterator = tokensList.iterator();
		while (tokensIterator.hasNext()) {
			String token = tokensIterator.next();
			//System.out.println("Token to be processed"+token);
			if (!multiWordsDictionary.containsKey(token)) {
				if (sentenceFromTokensList.contains(token))
					finalTokensList.add(token);
			} else {
				System.out.println("Token "+token+" found in multiword dictionary");
				boolean isPosWordExistsInInputSentence = false;
				Set<String> possibleWords = multiWordsDictionary.get(token);
				System.out.println("Possible Words : "+ possibleWords);
				
				for (String posWord : possibleWords) {
					System.out.println("Possible Word : "+posWord);				
					if (sentenceFromTokensList.contains(posWord)) {
						isPosWordExistsInInputSentence = true;
						
						finalTokensList.add(posWord);
						
						int indexOfWordInSentence = sentenceFromTokensList
								.indexOf(posWord);
						
						sentenceFromTokensList = sentenceFromTokensList
								.substring(
										indexOfWordInSentence
												+ posWord.length()).trim();
						// System.out.println("New org Sentence: "
						// + sentenceFromTokensList);
						List<String> tempTokensList = new ArrayList<String>();

						for (String w : sentenceFromTokensList.trim().split(" ")) {
							tempTokensList.add(w.trim());
						}
						tokensIterator = tempTokensList.iterator();
						tokensList = tempTokensList;
						//System.out.println("New tokens list "+tokensList);
						break;
					}
				}
				//If there is no match found in the multiword dictionary according to the input sentence
				if(!isPosWordExistsInInputSentence){
					finalTokensList.add(token);
				}
			}
		}

		return finalTokensList;
	}

*/	private static Row getFinalizedTokensListByMLEAndSmoothing(
			Map<String, Integer> uniGramsOfTrainingData,
			Map<String, Integer> biGramsOfTrainingData,
			Map<String, Integer> triGramsOfTrainingData,
			long noOfUniqueWordsInCorpus, List<Row> selectedRows) {

		Map<Integer, Float> allRowsProbabilities;
		Row finalTokenizedRow;
		/** Tri-gram comparison and probability calculation */
		allRowsProbabilities = findTrigramProbabilities(biGramsOfTrainingData,
				triGramsOfTrainingData, selectedRows);
		finalTokenizedRow = getFinalizedTokenzRow(selectedRows,
				allRowsProbabilities);
		//System.out.println("Perfomed Trigram MLE");

		/** Bi-gram comparison and probability calculation */
		if (finalTokenizedRow == null) {
			// TODO Move to Bigram Probability checking
			allRowsProbabilities = null;
			allRowsProbabilities = findBigramProbabilities(
					biGramsOfTrainingData, uniGramsOfTrainingData, selectedRows);
			finalTokenizedRow = getFinalizedTokenzRow(selectedRows,
					allRowsProbabilities);
			//System.out.println("Perfomed Bigram MLE");

			/** Moving on SMOOTHING */
			if (finalTokenizedRow == null) {
				allRowsProbabilities = null;
				allRowsProbabilities = findProbabilitiesBySmoothing(
						biGramsOfTrainingData, uniGramsOfTrainingData,
						selectedRows, noOfUniqueWordsInCorpus);
				finalTokenizedRow = getFinalizedTokenzRowAfterSmoothing(
						selectedRows, allRowsProbabilities);
				//System.out.println("Perfomed Smoothing");
			}
		}
		return finalTokenizedRow;
	}

	private static Map<Integer, Float> findProbabilitiesBySmoothing(
			Map<String, Integer> biGramsOfTrainingData,
			Map<String, Integer> uniGramsOfTrainingData,
			List<Row> selectedRows, long noOfUniqueWordsInCorpus) {

		Map<Integer, Float> allRowsProbabilities = new ConcurrentHashMap<Integer, Float>();
		for (Row row : selectedRows) {
			int rowIndexInList = selectedRows.indexOf(row);

			List<String> biGrams = TextOperations.getNGrams(
					row.getRowElements(), 2);
			List<String> uniGrams = TextOperations.getNGrams(
					row.getRowElements(), 1);

			// System.out.println("Bigram:" + biGrams);
			// System.out.println("Unigram:" + uniGrams);

			// Check occurences of trigram
			// if(isGramExistsInTrainingData(biGramsOfTrainingData, biGrams)){
			List<TextGramsProbability> tgProbList = new ArrayList<TextGramsProbability>();
			for (String element : biGrams) {

				int dividendTextOccurrenceCount = 0;
				int divisorTextOccurrenceCount = 0;

				String dividendText = element;
				if (biGramsOfTrainingData.containsKey(dividendText))
					dividendTextOccurrenceCount = biGramsOfTrainingData
							.get(dividendText);

				String divisorText = element.substring(
						element.indexOf(" ") + 1, element.length());
				// System.out.println("Dividend Text:"+dividendText);
				// System.out.println("Divisor Text:"+divisorText);

				if (uniGramsOfTrainingData.containsKey(divisorText))
					divisorTextOccurrenceCount = uniGramsOfTrainingData
							.get(divisorText);

				// System.out.println("Dividend Occurence Count:"+dividendTextOccurrenceCount);
				// System.out.println("Divisor Occurence Count:"+divisorTextOccurrenceCount);
				float pairProbability = 0;
				// if(dividendTextOccurrenceCount != 0 &&
				// divisorTextOccurrenceCount != 0)
				pairProbability = (float) (dividendTextOccurrenceCount + 1)
						/ (divisorTextOccurrenceCount + noOfUniqueWordsInCorpus);
				// System.out.println("Probabliity:"+pairProbability);
				TextGramsProbability tgProb = new TextGramsProbability(
						dividendText, divisorText, dividendTextOccurrenceCount,
						divisorTextOccurrenceCount, pairProbability);
				tgProbList.add(tgProb);
			}
			// TODO Multiply probabilities of each trigram calculated in above
			// step
			float multipliedProb = 1;
			for (TextGramsProbability tgProbObj : tgProbList) {
				// System.out.println(multipliedProb+"*"+tgProbObj.getPairProbability());
				multipliedProb *= tgProbObj.getPairProbability();
				// System.out.println("Resultant Prob:"+multipliedProb);
			}
			// TODO put final probability w.r.t row number in a map
			allRowsProbabilities.put(rowIndexInList, multipliedProb);
			// System.out.println("Exited before last else..it means all bigrams and unigrams are available in training data...and probabilities calculated correctly");
		}
		// allRowsProbabilities.forEach( (k,v) -> System.out.println("Key: " + k
		// + ": Value: " + v));
		return allRowsProbabilities;
	}

	private static Map<Integer, Float> findBigramProbabilities(
			Map<String, Integer> biGramsOfTrainingData,
			Map<String, Integer> uniGramsOfTrainingData, List<Row> selectedRows) {

		Map<Integer, Float> allRowsProbabilities = new ConcurrentHashMap<Integer, Float>();
		for (Row row : selectedRows) {
			int rowIndexInList = selectedRows.indexOf(row);

			List<String> biGrams = TextOperations.getNGrams(
					row.getRowElements(), 2);
			List<String> uniGrams = TextOperations.getNGrams(
					row.getRowElements(), 1);

			// System.out.println("Bigram:" + biGrams);
			// System.out.println("Unigram:" + uniGrams);

			// Check occurences of trigram
			if (isGramExistsInTrainingData(biGramsOfTrainingData, biGrams)) {
				List<TextGramsProbability> tgProbList = new ArrayList<TextGramsProbability>();
				for (String element : biGrams) {

					int dividendTextOccurrenceCount = 0;
					int divisorTextOccurrenceCount = 0;

					String dividendText = element;
					dividendTextOccurrenceCount = biGramsOfTrainingData
							.get(element);

					String divisorText = element.substring(
							element.indexOf(" ") + 1, element.length());
					// System.out.println("Dividend Text:"+dividendText);
					// System.out.println("Divisor Text:"+divisorText);

					if (uniGramsOfTrainingData.containsKey(divisorText)) {
						divisorTextOccurrenceCount = uniGramsOfTrainingData
								.get(divisorText);
					} else {
						// System.out.println("Exited before last else..it means some unigrams are not available in training data");
						allRowsProbabilities.put(rowIndexInList, (float) 0);
						break;
					}

					// System.out.println("Dividend Occurence Count:"+dividendTextOccurrenceCount);
					// System.out.println("Divisor Occurence Count:"+divisorTextOccurrenceCount);
					float pairProbability = 0;
					if (dividendTextOccurrenceCount != 0
							&& divisorTextOccurrenceCount != 0)
						pairProbability = (float) dividendTextOccurrenceCount
								/ divisorTextOccurrenceCount;
					// System.out.println("Probabliity:"+pairProbability);
					TextGramsProbability tgProb = new TextGramsProbability(
							dividendText, divisorText,
							dividendTextOccurrenceCount,
							divisorTextOccurrenceCount, pairProbability);
					tgProbList.add(tgProb);
				}
				// TODO Multiply probabilities of each trigram calculated in
				// above step
				float multipliedProb = 1;
				for (TextGramsProbability tgProbObj : tgProbList) {
					// System.out.println(multipliedProb+"*"+tgProbObj.getPairProbability());
					multipliedProb *= tgProbObj.getPairProbability();
					// System.out.println("Resultant Prob:"+multipliedProb);
				}
				// TODO put final probability w.r.t row number in a map
				allRowsProbabilities.put(rowIndexInList, multipliedProb);
				// System.out.println("Exited before last else..it means all bigrams and unigrams are available in training data...and probabilities calculated correctly");
			} else {
				// System.out.println("Exited from last else..it means some bigrams are not found in training data");
				allRowsProbabilities.put(rowIndexInList, (float) 0);
			}
			// allRowsProbabilities.forEach( (k,v) -> System.out.println("Key: "
			// + k + ": Value: " + v));
		}
		return allRowsProbabilities;
	}

	private static Row getFinalizedTokenzRow(List<Row> selectedRows,
			Map<Integer, Float> allRowsProbabilities) {

		Row finalTokenizedRow = null;
		Entry<Integer, Float> maxEntry = null;
		for (Entry<Integer, Float> entry : allRowsProbabilities.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}

		if (maxEntry.getValue() != 0 && maxEntry.getValue() > 0.0000000001) {
			//System.out.println("Probability value is reasonable");
			finalTokenizedRow = selectedRows.get(maxEntry.getKey());
			// System.out.println(finalTokenizedRow.toString());
		} else {
			//System.out.println("Probability value too small : Row: "
					//+ maxEntry.getKey() + ", Prob: " + maxEntry.getValue());
		}

		return finalTokenizedRow;
	}

	private static Row getFinalizedTokenzRowAfterSmoothing(
			List<Row> selectedRows, Map<Integer, Float> allRowsProbabilities) {

		Row finalTokenizedRow = null;
		Entry<Integer, Float> maxEntry = null;
		for (Entry<Integer, Float> entry : allRowsProbabilities.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}

		finalTokenizedRow = selectedRows.get(maxEntry.getKey());

		/*
		 * if(maxEntry.getValue()!=0 && maxEntry.getValue()>0.0000000001){
		 * System.out.println("Probability value is reasonable");
		 * finalTokenizedRow = selectedRows.get(maxEntry.getKey());
		 * System.out.println(finalTokenizedRow.toString()); }else{
		 * System.out.println
		 * ("Probability value too small : Row: "+maxEntry.getKey
		 * ()+", Prob: "+maxEntry.getValue()); }
		 */

		return finalTokenizedRow;
	}

	private static Map<Integer, Float> findTrigramProbabilities(
			Map<String, Integer> biGramsOfTrainingData,
			Map<String, Integer> triGramsOfTrainingData, List<Row> selectedRows) {

		Map<Integer, Float> allRowsProbabilities = new ConcurrentHashMap<Integer, Float>();
		for (Row row : selectedRows) {
			int rowIndexInList = selectedRows.indexOf(row);

			List<String> triGrams = TextOperations.getNGrams(
					row.getRowElements(), 3);
			List<String> biGrams = TextOperations.getNGrams(
					row.getRowElements(), 2);
			// System.out.println("Trigram:" + triGrams);
			// System.out.println("Bigram:" + biGrams);

			// Check occurences of trigram
			if (isGramExistsInTrainingData(triGramsOfTrainingData, triGrams)) {
				List<TextGramsProbability> tgProbList = new ArrayList<TextGramsProbability>();
				for (String element : triGrams) {

					int dividendTextOccurrenceCount = 0;
					int divisorTextOccurrenceCount = 0;

					String dividendText = element;
					dividendTextOccurrenceCount = triGramsOfTrainingData
							.get(element);

					String divisorText = element.substring(
							element.indexOf(" ") + 1, element.length());
					// System.out.println("Dividend Text:"+dividendText);
					// System.out.println("Divisor Text:"+divisorText);

					if (biGramsOfTrainingData.containsKey(divisorText)) {
						divisorTextOccurrenceCount = biGramsOfTrainingData
								.get(divisorText);
					} else {
						// System.out.println("Exited before last else..it means some bigrams are not available in training data");
						allRowsProbabilities.put(rowIndexInList, (float) 0);
						break;
					}

					// System.out.println("Dividend Occurence Count:"+dividendTextOccurrenceCount);
					// System.out.println("Divisor Occurence Count:"+divisorTextOccurrenceCount);
					float pairProbability = 0;
					if (dividendTextOccurrenceCount != 0
							&& divisorTextOccurrenceCount != 0)
						pairProbability = (float) dividendTextOccurrenceCount
								/ divisorTextOccurrenceCount;
					// System.out.println("Probabliity:"+pairProbability);
					TextGramsProbability tgProb = new TextGramsProbability(
							dividendText, divisorText,
							dividendTextOccurrenceCount,
							divisorTextOccurrenceCount, pairProbability);
					tgProbList.add(tgProb);
				}
				// TODO Multiply probabilities of each trigram calculated in
				// above step
				float multipliedProb = 1;
				for (TextGramsProbability tgProbObj : tgProbList) {
					// System.out.println(multipliedProb+"*"+tgProbObj.getPairProbability());
					multipliedProb *= tgProbObj.getPairProbability();
					// System.out.println("Resultant Prob:"+multipliedProb);
				}
				// TODO put final probability w.r.t row number in a map
				allRowsProbabilities.put(rowIndexInList, multipliedProb);
				// System.out.println("Exited before last else..it means all bigrams and trigrams are available in training data...and probabilities calculated correctly");
			} else {
				// System.out.println("Exited from last else..it means some trigrams are not found in training data");
				allRowsProbabilities.put(rowIndexInList, (float) 0);
			}
			// allRowsProbabilities.forEach( (k,v) -> System.out.println("Key: "
			// + k + ": Value: " + v));
		}
		return allRowsProbabilities;
	}

	private static boolean isGramExistsInTrainingData(
			Map<String, Integer> triGramsOfTrainingData, List<String> triGrams) {
		for (String e : triGrams) {
			if (!triGramsOfTrainingData.containsKey(e)) {
				// System.out.println("Not found:"+e);
				return false;
			}
		}

		return true;
	}

	private static Map getTextGrams(ArrayList<String> uniGramsList) {
		// ArrayList<TextGrams> tgList = new ArrayList<TextGrams>();
		Map<String, Integer> tgMap = new ConcurrentHashMap<String, Integer>();
		for (String text : uniGramsList) {
			// System.out.println(text);
			String[] splitText = text.split("	");
			// TextGrams tg = new TextGrams(splitText[0],
			// Integer.parseInt(splitText[1]));
			// tgList.add(tg);
			tgMap.put(splitText[0], Integer.parseInt(splitText[1]));
		}
		return tgMap;
	}

	private static void updateFlagBitOfTheRow(List<Row> allRows,
			int rowUnderProcessingIndex, boolean flagBit) {
		try{
		
			Row rowUnderProcessing = allRows.get(rowUnderProcessingIndex);
			rowUnderProcessing.setFlagBit(flagBit);
			allRows.remove(rowUnderProcessingIndex);
			allRows.add(rowUnderProcessingIndex, rowUnderProcessing);
		}catch(Exception e){
			//System.out.println("Exception: updateFlagBitOfTheRow()");
		}
	}

	private static void printMatrix(List<Row> aRows) {
		for (Row row : aRows) {
			// if(row.getRowElements().size()==30 && row.isFlagBit()==false){
			// if(row.getRowElements().size()==44){
			// System.out.println("Matrix Row : " + allRows.indexOf(row)
			// + " : " + row.toString());

			System.out.println("Row No : " + aRows.indexOf(row) + ", Row:"
					+ row.getRowElements() + ", NoOfTokens : "
					+ row.getNoOfTokens() + ", FlagBit :" + row.isFlagBit());

			// }
		}
	}

	private static void addSentenceMarkerToTheRowUnderProcess(
			List<Row> allRows, int rowUnderProcessingIndex, String inputBigram) {
		String lastCharacter = inputBigram.substring(0, 1);
		// System.out.println("Last Character : " + lastCharacter);
		String sentenceMarker = inputBigram.substring(1);
		// System.out.println("Sentence Marker : " + sentenceMarker);

		mergeCharacterWithLastElementOfTheRow(allRows, rowUnderProcessingIndex,
				lastCharacter);
		addInputBigramToRowUnderProcess(allRows, rowUnderProcessingIndex,
				sentenceMarker);
	}

	private static void mergeCharacterWithLastElementOfTheRow(
			List<Row> allRows, int rowUnderProcessingIndex, String lastCharacter) {
		
		try{
		Row rowUnderProcessing = allRows.get(rowUnderProcessingIndex);
		List<String> elementsInRow = rowUnderProcessing.getRowElements();
		int lastElementIndex = elementsInRow.size() - 1;
		String lastElement = elementsInRow.get(lastElementIndex);
		elementsInRow.remove(lastElementIndex);
		lastElement = lastElement + lastCharacter;

		// System.out
		// .println("Last Element Concatination : " + lastElement.trim());
		elementsInRow.add(lastElementIndex, lastElement);

		allRows.remove(rowUnderProcessingIndex);
		allRows.add(rowUnderProcessingIndex, rowUnderProcessing);
		
		}catch(Exception e){
			//System.out.println("Exception: mergeCharacterWithLastElementOfTheRow()");
			
		}
	}

	private static void mergeCharacterWithLastElementOfTheRow(
			String textAfterSpaceRemoval, List<Row> allRows,
			int rowUnderProcessingIndex, String lastCharacter) {
		
		try{
		Row rowUnderProcessing = allRows.get(rowUnderProcessingIndex);
		List<String> elementsInRow = rowUnderProcessing.getRowElements();
		int lastElementIndex = elementsInRow.size() - 1;
		String lastElement = elementsInRow.get(lastElementIndex);
		lastElement = lastElement + lastCharacter;

		// System.out.println("Text before Last Element Concatination : "
		// + textAfterSpaceRemoval);

		if (isMorphemExistsInInputSequence(textAfterSpaceRemoval, lastElement)) {
			elementsInRow.remove(lastElementIndex);
			// System.out.println("Last Element Concatination : "
			// + lastElement.trim());
			elementsInRow.add(lastElementIndex, lastElement);

			allRows.remove(rowUnderProcessingIndex);
			allRows.add(rowUnderProcessingIndex, rowUnderProcessing);
		}
		
		}catch(Exception e){
			//System.out.println("Exception: mergeCharacterWithLastElementOfTheRow()");
		}
	}

	private static String makeTextLengthEven(String textAfterSpaceRemoval) {
		if (textAfterSpaceRemoval.length() % 2 != 0) {
			CharSequence lastChar = textAfterSpaceRemoval.subSequence(
					textAfterSpaceRemoval.length() - 1,
					textAfterSpaceRemoval.length());
			textAfterSpaceRemoval = textAfterSpaceRemoval.replace(lastChar, " "
					+ lastChar);
			// System.out.println("After space and Character replacement"+textAfterSpaceRemoval
			// + ","+textAfterSpaceRemoval.length());
			textAfterSpaceRemoval = textAfterSpaceRemoval.replaceAll("\\s{1,}",
					" ");
			// System.out.println("After regex replacement"+textAfterSpaceRemoval
			// + ","+textAfterSpaceRemoval.length());

		}
		return textAfterSpaceRemoval;
	}

	private static void addInputBigramToRowUnderProcess(List<Row> allRows,
			int rowUnderProcessingIndex, String inputBigram) {
		try{
		Row rowUnderProcess = allRows.get(rowUnderProcessingIndex);
		// System.out.println("Row Under Process Before Add New Element : "+
		// rowUnderProcess.getRowElements());
		rowUnderProcess.addRowElement(inputBigram);
		// System.out.println("Row Under Process Before Removal : "+
		// rowUnderProcess.getRowElements());
		allRows.remove(rowUnderProcessingIndex);
		// System.out.println("Row Under Process After Removal : "+
		// rowUnderProcess.getRowElements());
		allRows.add(rowUnderProcessingIndex, rowUnderProcess);
		// System.out.println("Existing Rows : "+
		// rowUnderProcess.getRowElements());
		// System.out.println("Existing Flag Bit : "+
		// rowUnderProcess.isFlagBit());
		// System.out.println("Existing Row Tokens : "+
		// rowUnderProcess.getNoOfTokens());
		}catch(Exception e){
			//System.out.println("Exception: addInputBigramToRowUnderProcess");
		}
	}

	private static void firstInputBigramReadingOfTheProcess(List<Row> allRows,
			String inputBigram, Set<String> possibleMorphems) {

		/** If first bigram has more than one matchings in morphems list **/
		if (possibleMorphems.size() > 0) {
			List<String> tempList = new ArrayList<String>(possibleMorphems);
			for (int i = 0; i < tempList.size(); i++) {
				Row nextRow = new Row();
				String nextMorphem = tempList.get(i);
				nextRow.addRowElement(nextMorphem);
				allRows.add(nextRow);
			}
		}
	}

	private static void insertNewRow(List<Row> allRows,
			int rowUnderProcessingIndex, String element) {

		System.out.println("Inside if");
		Row nextRow = copyExistingRowToNewRow(allRows, rowUnderProcessingIndex);
		System.out.print("----New Row After Element Inserted =>");
		System.out.println(nextRow.toString());
		allRows.add(nextRow);
		// }
	}

	private static Row copyExistingRowToNewRow(List<Row> allRows,
			int rowUnderProcessingIndex) {
		Row rowToBeCopied = allRows.get(rowUnderProcessingIndex);
		// System.out.print("----Row to be copied =>");
		// System.out.println(rowToBeCopied.toString());
		// System.out.println("Element to be Added in New Row :"+element);
		Row nextRow = new Row(rowToBeCopied);
		// System.out.print("----New Row Inserted =>");
		// System.out.println(nextRow.toString());

		return nextRow;
	}

	private static void copyExistingRowToNewRowWithElement(List<Row> allRows,
			int rowUnderProcessingIndex, String element) {
		Row rowToBeCopied = allRows.get(rowUnderProcessingIndex);
		// System.out.print("----Row to be copied =>");
		// System.out.println(rowToBeCopied.toString());
		// System.out.println("Element to be Added in New Row :" + element);
		Row nextRow = new Row(rowToBeCopied);
		nextRow.addRowElement(element);
		// System.out.print("----New Row Inserted =>");
		// System.out.println(nextRow.toString());
		allRows.add(nextRow);
		// return nextRow;
	}

	private static boolean isMorphemExistsInInputSequence(
			String textAfterSpaceRemoval, String morphem) {
		if (textAfterSpaceRemoval.contains(morphem)) {
			// System.out.println(textAfterSpaceRemoval);
			// System.out.println(morphem);
			return true;
		} else
			return false;
	}

}
