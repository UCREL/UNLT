package com.unlptk.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UNLPTKConstants {

	
	
	//TODO ARFF FILES CONFIGURATION
	public static final String DEFAULT_OUTPUT_DIR =".\\Output";
	//public static final String DEFAULT_INPUT_DIR =".\\input";
	public static final String DEFAULT_INPUT_DIR ="input";
	public static final String DEFAULT_TOKENIZED_FILES_DIR =".\\TokenizedFiles";
	public static final String DEFAULT_OUTPUT_EXTENTION =".txt";
	public static final String ARFF_OUTPUT_EXTENTION =".arff";
	public static final  DecimalFormat DEFAULT_DECIMAL_FORMATTER = new DecimalFormat("#0.00");
	//public static final String DEFAULT_TEMP_DIR =".\\temp";
	public static final String DEFAULT_TEMP_DIR ="temp";
	
	
	public static final String ARFF_RELATION ="@RELATION";
	public static final String ARFF_ATTRIBUTE ="@ATTRIBUTE";
	public static final String ARFF_DATA ="@DATA";
	public static final String ARFF_CLASS ="CLASS {"+"NC,"+"LR,"+"HR,"+"NP"+"}";
	
	public static final String ARFF_NUMERIC_DATA_TYPE ="NUMERIC";
	
	
	public static final String LCS_COMBINED_ARFF = DEFAULT_OUTPUT_DIR+File.separator+"LCS_COMBINED"+ARFF_OUTPUT_EXTENTION;
	public static final String LCS_JACCARD_ARFF = DEFAULT_OUTPUT_DIR+File.separator+"LCS_JACCARD"+ARFF_OUTPUT_EXTENTION;
	public static final String LCS_DICE_ARFF = DEFAULT_OUTPUT_DIR+File.separator+"LCS_DICE"+ARFF_OUTPUT_EXTENTION;
	public static final String LCS_CONTAINMENT_ARFF = DEFAULT_OUTPUT_DIR+File.separator+"LCS_CONTAINMENT"+ARFF_OUTPUT_EXTENTION;
	public static final String LCS_OVERLAP_ARFF = DEFAULT_OUTPUT_DIR+File.separator+"LCS_OVERLAP"+ARFF_OUTPUT_EXTENTION;
	
	//TODO DEFAULT NAMES OF TECHNIQUES
	public static final String JACCARD = "JACCARD";
	public static final String DICE = "DICE";	
	public static final String CONTAINMENT = "CONTAINMENT";
	public static final String OVERLAP = "OVERLAP";
	public static final String UNI_GRAM = "UNI_GRAM";
	public static final String BI_GRAM = "BI_GRAM";	
	public static final String TRI_GRAM = "TRI_GRAM";
	public static final String FOUR_GRAM = "FOUR_GRAM";
	public static final String FIVE_GRAM = "FIVE_GRAM";
	public static final String SIMPLE_NGRAM_BASED = "SIMPLE_NGRAM_SIM_SCORE";
	public static final String KEYWORDS_BASED_NGRAM = "KEYWORDS_BASED_NGRAM_SIM_SCORE";
	
	public static final String KEYWORDS_BASED_LCS = "KEYWORDS_BASED_LCS_SIM_SCORE";
	public static final String SIMPLE_LCS = "SIMPLE_LCS_SIM_SCORE";
	public static final String SENTENCE_RATIO = "SENTENCE_RATIO_SIM_SCORE";
	public static final String TOKENS_RATIO = "TOKENS_RATIO_SIM_SCORE";
	public static final String TYPE_TOKENS_RATIO = "TYPE_TOKENS_RATIO_SIM_SCORE";
	public static final String GREEDY_STRING_TILING = "GREEDY_STRING_TILING";
	
	
	
	public static String ANNOTATED_FILE_PAIR_SPLITTER = "";
	public static ArrayList<String> MULTI_WORDS_LIST = null;
	public static ArrayList<String> MULTI_WORD_DICTIONARY = null;	
	public static ArrayList<String> MORPHEM_LIST = null;
	public static ArrayList<Integer> MULTI_WORD_DICTIONARY_HASH_CODES = null;
	public static ArrayList<String> URDU_STOP_WORDS_LIST = null;
	public static ArrayList<Integer> URDU_STOP_WORDS_LIST_HASH_CODES = null;
	
	
	public static int NGRAM_LENGTH = 5;
	public static int KEY_WORDS_NGRAM_LENGTH = 5;
	public static int STOP_WORDS_NGRAM_LENGTH = 5;
	public static int MINIMAL_MATCHING_LENGTH = 5; // USED for GST
	public static float GST_THRESHOLD = (float) 0.5; // USED for GST
	
	
	public static String[] N_GRAM_ATTRIBUTES = {"UNI-GRAM", "BI-GRAM", "TRI-GRAM", "FOUR_GRAM", "FIVE-GRAM"};
	public static String[] LCS_ATTRIBUTES = {"SIMPLE-LCS", "KEY-WORDS-LCS"};
	public static String[] ARFF_RELATIONS = {"UNI-GRAM", "BI-GRAM", "TRI-GRAM", "FOUR_GRAM", "FIVE-GRAM", "COMBINED-N-GRAMS", "SIMPLE-LCS", "KEY-WORDS-LCS"};
	
	//TODO DEFAULT PATH's
	public static final String DEFAULT_WORDS_SEPERATOR =" ";	
	public static final String MULTI_WORD_DICTIONARY_PATH = ".\\resources\\multiwords_dictionary.txt";
//	public static final String MULTI_WORD_DICTIONARY_PATH = "resources//multiwords_dictionary.txt";
	public static final String URDU_STOP_WORDS_LIST_PATH = ".\\resources\\stopwords.txt";
	public static final String MORPHEM_LIST_PATH = "resources/morphem_list.txt";
	//public static final String MORPHEM_LIST_PATH = ".\\morphem_list.txt";
	//public static final String KEY_WORDS_LIST_PATH = ".\\resources\\key_words_list.txt";
	public static final String KEY_WORDS_LIST_PATH = "resources/key_words_list.txt";
	public static final String OUTPUT_FILE = DEFAULT_OUTPUT_DIR+File.separator+"tokens";
	public static final String TOKENIZED_FILE = DEFAULT_TOKENIZED_FILES_DIR+File.separator+"tokens.txt";
	//public static final String UNIGRAMS_PATH = ".\\Output\\tokens.txt";
	//public static final String BIGRAMS_PATH = ".\\Output\\tokens_bigram.txt";
	//public static final String TRIGRAMS_PATH = ".\\Output\\tokens_trigram.txt";
	public static final String UNIGRAMS_PATH = "Output/tokens.txt";
	public static final String BIGRAMS_PATH = "Output/tokens_bigram.txt";
	public static final String TRIGRAMS_PATH = "Output/tokens_trigram.txt";
	public static final String DOC_SENTENCES_OUTPUT_FILE_PATH = ".\\Output\\sentences.txt";
	
	
	//TODO CORPUS Related Path's
	//Cross lingual path - tanveer corpus

	//read file after pre processing text  for_pos
//	public static final String CORPUS_SOURCE_DIR = "F:\\Games\\prog\\Jawad\\nlp\\for_pos.tt";
	
	
	
//	public static final String CORPUS_SOURCE_DIR = "tokens.tt";
	
	public static final String CORPUS_SOURCE_DIR = "E:\\Jawad\\Data";
//	public static final String CORPUS_SOURCE_DIR = "D:\\Rizwan_Drive\\Comsats\\Research\\jawadShafi_Work\\NLE_paper\\Final_Word_Tokenized_Corpus_Automatic_Manuall_data\\Final_Word_Tokenized_Corpus_Automatic_Manuall_data\\toknized_corpus";
	//public static final String CORPUS_SOURCE_DIR = "D:\\Rizwan_Drive\\Comsats\\Research\\MS_Projects\\Tanveer Data\\Final_automatic_Corpus_(26-08-14)";
	public static final String CORPUS_ANNOTATION_FILE = "D:\\Rizwan_Drive\\Comsats\\Research\\MS_Projects\\Tanveer Data\\all-plagiarised-files.txt";
	public static final String TOKENIZED_CORPUS_DIR = "D:\\Rizwan_Drive\\Comsats\\Research\\MS_Projects\\Tanveer Data\\Tokenized_Corpus";
	
	//MONO Lingual corpus path - ahmad corpus
	/*public static final String CORPUS_SOURCE_DIR = "D:\\Rizwan_Drive\\Comsats\\Research\\MS_Projects\\ahmad bilal\\CORPUS";
	public static final String CORPUS_ANNOTATION_FILE = "D:\\Rizwan_Drive\\Comsats\\Research\\MS_Projects\\ahmad bilal\\all-plagiarised-files.txt";
	public static final String TOKENIZED_CORPUS_DIR = "D:\\Rizwan_Drive\\Comsats\\Research\\MS_Projects\\ahmad bilal\\Tokenized_Corpus";
	*/
	//TNT Tagger Config
	//public static final String TNT_TAGGER_DIR = "Library_Projects\\TNTTagger";
	public static final String TNT_TAGGER_DIR = ".\\Library_Projects\\TNTTagger";
	//public static final String TNT_TAGGER_DIR = "UrduPOSTagger";
	//public static final String TNT_TAGGER_DIR =new File(".").getAbsolutePath()+"\\"+"src\\TNTTagger";
//	public static final String TNT_TAGGER_DIR =new File(".").getAbsolutePath();   
	
	
	public static final String TNT_TAGGER_INPUT_TOKENS_FILE = "tokens.tt";
	//public static final String TNT_TAGGER_INPUT_TOKENS_FILE = TNT_TAGGER_DIR+"\\"+"tokens.tt";
	//public static final String TNT_TAGGER_INPUT_TOKENS_FILE = new File(".").getAbsolutePath()+"\\"+"tokens.tt";
//	public static final String TNT_TAGGER_INPUT_TOKENS_FILE = new File("tokens.tt").getAbsolutePath();
//	public static final String TNT_TAGGER_INPUT_TOKENS_FILE = new File("tokens.tt").getAbsolutePath(); 
	
	//public static final String TNT_TAGGER_INPUT_TOKENS_FILE = "UrduPOSTagger/tokens.tt";
	public static final String TNT_TAGGER_OUTPUT_POS_FILE = "pos_of_tokens.tt";
	//public static final String TNT_TAGGER_OUTPUT_POS_FILE = TNT_TAGGER_DIR+"\\"+"pos_of_tokens.tt";
	//public static final String TNT_TAGGER_OUTPUT_POS_FILE = new File(".").getAbsolutePath()+"\\"+"pos_of_tokens.tt";
//	public static final String TNT_TAGGER_OUTPUT_POS_FILE = new File("pos_of_tokens.tt").getAbsolutePath() 
	
	
	//Word Tokenizer Constants
	public static long NUMBER_OF_UNIQUE_WORDS_IN_CORPUS = 0;
	public static final String INPUT_TEXT_SUB_STRINGS = "temp/SubStrings.txt";
	public static final String INPUT_FILE = "input/org-text.txt";
}
