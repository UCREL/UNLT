package com.unlptk.driver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeTesting {

	 public static void main(String[] args) {
	     
		 String inputText = "ان کا کہنا تھا کہ سنہ 2014 کے دھرنے میں 62  اور 63 کا سبق پوری اہل کیا ہے۔";
		 
		 //String regexPart1 = "[\\u0600-\\u06FF|\\u0750-\\u077F|\\uFB50-\\uFDFF|\\uFE70‌​-\\uFEFF]+?[\\s]?";
		 String regexPartRight = "[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF]+([,\\s])?";
		 String regexPart2 = "-?\\d+";
		 String regexPartLeft = "([,\\s])?[\\u0600-\\u06FF\\u0750-\\u077F\\uFB50-\\uFDFF\\uFE70‌​-\\uFEFF]+";
		 //String regexPart3 = "";
		 
		 String regex = regexPartRight+regexPart2+regexPartLeft;
			
			Pattern pattern = Pattern.compile(regex);
			//Matcher matcher = pattern.matcher(originalInputText.replaceAll("\\s{2,}", " "));
			Matcher matcher = pattern.matcher(inputText);
			// Check all occurrences
			while (matcher.find()) {
			    System.out.print("Start index: " + matcher.start());
			    System.out.print(" End index: " + matcher.end());
			    System.out.println(" Found: " + matcher.group());
			}    
		 
		 

	 }
}
