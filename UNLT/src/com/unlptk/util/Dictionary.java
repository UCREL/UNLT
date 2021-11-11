package com.unlptk.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {

	private Map<String, Set<String>> morphemsMap;	
	private Map<String, Set<String>> multiWordsMap;
	
	private static Dictionary modelInstance;
	
	
	private Dictionary(){
		morphemsMap = new ConcurrentHashMap<String, Set<String>>();	
		multiWordsMap = new ConcurrentHashMap<String, Set<String>>();
	}
	
	//Get Instance method
	public static Dictionary getInstance(){
		if(modelInstance == null){
			modelInstance = new Dictionary();
			System.out.println("Loading Multiword & Morphem Dictionaries.........");
			loadMorphemsDictionary();			
			loadMultiWordsDictionary();
		}
		return modelInstance;
	}
	
	
	public void addMorphem(String key, String morphem){
		Set<String> morphemSet = morphemsMap.get(key);
		if(morphemSet == null){
			morphemSet = new LinkedHashSet<String>();
		}
		morphemSet.add(morphem);				
		morphemsMap.put(key, morphemSet);		
	}
	
	public void addMultiWord(String key, String word){
		Set<String> wordSet = multiWordsMap.get(key);
		if(wordSet == null){
			wordSet = new LinkedHashSet<String>();
		}
		wordSet.add(word.trim());
		//System.out.println("Key :"+key+", Word :"+word);
		//morphemSet.stream().sorted().collect(Collectors.toList());		
		multiWordsMap.put(key.trim(), wordSet);		
	}
	
	private static void loadMultiWordsDictionary(){
		//Dictionary multiWordsDictionary = new Dictionary();
	
		try {
			//UNLPTKConstants.MULTI_WORDS_LIST = FileOperations.fileToArrayList(UNLPTKConstants.MULTI_WORD_DICTIONARY_PATH, StandardCharsets.UTF_8);
			UNLPTKConstants.MULTI_WORDS_LIST = FileOperations.fileToArrayListUsingInputStream(UNLPTKConstants.MULTI_WORD_DICTIONARY_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("MultiWords list Size:"+UNLPTKConstants.MULTI_WORDS_LIST.size());
		
		for(String multiWord : UNLPTKConstants.MULTI_WORDS_LIST){
			String firstWord = multiWord.substring(0, multiWord.indexOf(" "));
			//System.out.println("First word :"+firstWord);
			
			for(String word : UNLPTKConstants.MULTI_WORDS_LIST){
				//System.out.println("List word :"+word);
				if(word.startsWith(firstWord)){
					//System.out.println("Start with List word :"+word);
					//multiWordsDictionary.addMultiWord(firstWord, word);
					modelInstance.addMultiWord(firstWord, word);
				}
			}					
		}
		
		//morphemDictionary.getMorphemsMap().forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
		//return multiWordsDictionary;
	}
	

	public Map<String, Set<String>> getMorphemsDictionary() {
		return morphemsMap;
	}
	
	public Map<String, Set<String>> getMultiWordsDictionary() {
		return multiWordsMap;
	}
	
	
	private static void loadMorphemsDictionary(){
		//Dictionary morphemDictionary = new Dictionary();
	
		try {
		//	UNLPTKConstants.MORPHEM_LIST = FileOperations.fileToArrayList(UNLPTKConstants.MORPHEM_LIST_PATH, StandardCharsets.UTF_8);
			UNLPTKConstants.MORPHEM_LIST = FileOperations.fileToArrayListUsingInputStream(UNLPTKConstants.MORPHEM_LIST_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(String morphem : UNLPTKConstants.MORPHEM_LIST){
			String firstTwoCharacters = morphem.substring(0, 2);
			//System.out.println("First Two Characters :"+firstTwoCharacters);
			
			for(String word : UNLPTKConstants.MORPHEM_LIST){
				if(word.startsWith(firstTwoCharacters)){
					//morphemDictionary.addMorphem(firstTwoCharacters, word);
					modelInstance.addMorphem(firstTwoCharacters, word);
				}
			}					
		}
		
		//morphemDictionary.getMorphemsMap().forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
		//return morphemDictionary;
	}
	
	
	
}
