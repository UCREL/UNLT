package com.unlptk.tokenizer;

import java.io.IOException;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
		
		
		UrduWordTokenizer uwt = UrduWordTokenizer.getInstance();
		String text = "الیکشن کمیشن آف پاکستان مکہ مدینہ پر ایٹم بم مارنے کے ارادوں کے اظہار اور غلام مصطفی جتوئی پاکستان یمن کو الیکشن کمیشن آف پاکستان ڈرون کی نوک پر غلام مصطفی جتوئی رکھنے کے عزم کے باوجود؟";
		
		List<String> tokens = uwt.tokenize(text);
		System.out.println("Tokens : "+ tokens);
		System.out.println("No of Tokens : "+tokens.size());
		
		List<String> uniqueTokens = uwt.getUniqueTokens(tokens);
		System.out.println("Unique Tokens : "+ uniqueTokens);
		System.out.println("No of Unique Tokens : "+ uniqueTokens.size());
		

	}

}
