package com.unlptk.tokenizer;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.unlptk.util.FileOperations;
import com.unlptk.util.TextOperations;
import com.unlptk.util.UNLPTKConstants;

public class WordTokenizerV2 {

	public static void main(String[] args) throws IOException {

		// TODO Load all required resources. 
		//It will always be the first statement(s) of your application.
		
		ArrayList<String> uniGramsList = FileOperations.fileToArrayList(UNLPTKConstants.UNIGRAMS_PATH, StandardCharsets.UTF_8);
		ArrayList<String> biGramsList = FileOperations.fileToArrayList(
				UNLPTKConstants.BIGRAMS_PATH, StandardCharsets.UTF_8);
		ArrayList<String> triGramsList = FileOperations.fileToArrayList(
				UNLPTKConstants.TRIGRAMS_PATH, StandardCharsets.UTF_8);
		//long noOfUniqueWordsInCorpus = TextOperations.getNoOfUniqueWords(uniGramsList);
		long noOfUniqueWordsInCorpus = 0;
	
		UNLPTKConstants.MORPHEM_LIST = FileOperations.fileToArrayList(
				UNLPTKConstants.MORPHEM_LIST_PATH, StandardCharsets.UTF_8);
		ArrayList<String> textOfAllFiles = FileOperations.directoryToList(
				UNLPTKConstants.DEFAULT_INPUT_DIR, StandardCharsets.UTF_8);
		String concatenatedTextOfAllFiles = String.join(" ", textOfAllFiles);
		String textAfterSpaceRemoval = concatenatedTextOfAllFiles.replaceAll(
				" ", "").trim();
		String copyoftext = textAfterSpaceRemoval;
		System.out.println("******************* REQUIRED RESOURCES HAS BEEN LOADED SUCCESSFULLY *******************");
		System.out.println("Input Text - Original  : " + concatenatedTextOfAllFiles);
		System.out.println("Input Text - After Space Removal " + textAfterSpaceRemoval);
		System.out.println("Input Text - No. of Characters after Space Removal " + textAfterSpaceRemoval.trim().length());

		
		
		//TODO Part-1 Morphem Based Comparison starts here
		List<String> match = new ArrayList<String>();
		List<DynamicArray> list = new ArrayList<DynamicArray>();

		//Iterator<DynamicArray> iterator = list.iterator();

		int row = 0, col = 0;
		int copy = row;
		int count = 0;

		while (row < list.size() || row == 0) {
			
			col = 0;
			textAfterSpaceRemoval = copyoftext;
			
			if (row != 0) {
				String existingText = "";
				for (int l = 0; l < list.get(row).list.size(); l++) {
					existingText = existingText + list.get(row).list.get(l);
					col++;
				}

				if (existingText.length() > textAfterSpaceRemoval.length() - 1) {
					row++;
					System.out.println("let it go:" + existingText);
					continue;
				} else {
					textAfterSpaceRemoval = textAfterSpaceRemoval.replace(
							existingText, "");
					System.out.println("existing text" + existingText
							+ "the remaining text is" + textAfterSpaceRemoval
							+ "  column is " + col);

				}

			}
			for (int i = 1; i < textAfterSpaceRemoval.length(); i++) {

				// char c = textAfterSpaceRemoval.charAt(i);
				// System.out.println(i+" = "+c);
				match.clear();
				count = 0;
				
				if (i == textAfterSpaceRemoval.length() - 1) {

					String doubleCharacters = textAfterSpaceRemoval.substring(
							i, i + 1);
					System.out.println("the single chaacter left is :"
							+ doubleCharacters);

					char[] single = new char[2];
					single = doubleCharacters.toCharArray();
					System.out.println("the single digit is:" + single[0]);
					if (single[0] == 'و') { // Wao 'و' must be here in condition

						String x = Character.toString(single[0]);
						list.get(row).list.add(col, x);
						list.get(row).flag = 0;
						col++;

					} else {
						col--;
						String x = " " + Character.toString(single[0]);
						System.out.println("the single digit is:" + x);
						list.get(row).list.set(col, list.get(row).list.get(col)
								+ x);
						list.get(row).flag = 1;
						col++;

					}

				}

				if (i < textAfterSpaceRemoval.length() - 1) {

					String doubleCharacters = textAfterSpaceRemoval.substring(
							i, i + 2);
					System.out.println(doubleCharacters);

					for (int j = 0; j < UNLPTKConstants.MORPHEM_LIST.size(); j++) {
						// System.out.println(UNLPTKConstants.MORPHEM_LIST.get(j));

						if (UNLPTKConstants.MORPHEM_LIST.get(j).startsWith(
								doubleCharacters)) {
							match.add(count,
									UNLPTKConstants.MORPHEM_LIST.get(j));

							System.out.println(UNLPTKConstants.MORPHEM_LIST
									.get(j) + count);
							count++;
							System.out.println("start double characters:" + doubleCharacters);
						}

					}

					if (!match.isEmpty()) {
						System.out.println(count);

						for (int k = 0; k < match.size(); k++) {

							if (k == 0) {
								if (list.isEmpty()) {
									System.out.println("i came here");

									DynamicArray b = new DynamicArray();
									// System.out.println(match.get(k));

									b.list.add(col, match.get(k));
									list.add(0, b);
								} else {
									list.get(row).list.add(col, match.get(k));

								}
								col++;
							}

							else {
								copy++;
								DynamicArray a = new DynamicArray();
								System.out.println("at row" + copy
										+ match.get(k));

								// a.list=(ArrayList<String>)list.get(row).list;
								for (int h = 0; h < list.get(row).list.size() - 1; h++) {
									String temp;
									temp = list.get(row).list.get(h);
									a.list.add(temp);
								}

								a.list.add(match.get(k));
								a.flag = list.get(row).flag;
								list.add(copy, a);
							}

						}
						i++;
					}// if not empty

					else {
						char[] single = new char[2];
						single = doubleCharacters.toCharArray();
						System.out.println("the single digit is:" + single[0]);
						if (single[0] == 'و') { // Wao 'و' character must be
												// here in condition

							String x = Character.toString(single[0]);
							list.get(row).list.add(col, x);
							list.get(row).flag = 0;
							col++;

						} else {
							col--;
							String x = Character.toString(single[0]);

							list.get(row).list.set(col,
									list.get(row).list.get(col) + x);
							list.get(row).flag = 1;
							col++;

						}
					}

				}

			}// for
				// iterator.next();

			row++;
		}

		System.out.println("\n\n\n-------------OUTPUT-------------\n\n\n");

		int min = 1003516510;
		for (int y = 0; y < list.size(); y++) {
			System.out.print("Row " + y + ":\t");
			for (int u = 0; u < list.get(y).list.size(); u++) {
				System.out.print(list.get(y).list.get(u) + "\t");
			}
			if (list.get(y).list.size() < min) {
				min = list.get(y).list.size();
			}
			System.out.println(list.get(y).flag + "\t"
					+ list.get(y).list.size());
		}

		System.out.println("\n\n\nThe selected rows are :\n\n\n");

		ArrayList<Integer> selectedRows = new ArrayList<Integer>();
		// int selrows=0;
		for (int y = 0; y < list.size(); y++) {
			if ((list.get(y).list.size() == min) && (list.get(y).flag == 0)) {
				System.out.print("Row #" + y + ": \t");
				selectedRows.add(y);
				for (int f = 0; f < list.get(y).list.size(); f++) {
					System.out.print(list.get(y).list.get(f) + "\t");
				}
				System.out.println(list.get(y).flag + "\t"
						+ list.get(y).list.size());
			}
		}

		System.out.println("No. of Selected Rows : " + selectedRows.size());

		System.exit(0);

		//TODO /** Part-2 N-gram with Smoothing starts here
		// TODO Tokens of Selected Row
		for (int selRow : selectedRows) {
			ArrayList<String> selRowTokens = new ArrayList<>();
			// Adding additional space in start
			// selRowTokens.add(" ");
			for (int f = 0; f < list.get(selRow).list.size(); f++) {
				// System.out.print(list.get(selRow).list.get(f) + "\t");
				selRowTokens.add(list.get(selRow).list.get(f));
			}
			

			// Loop to control tri and bi gram operations
			int noOfGrams = 3;
			while (noOfGrams >= 1) {

				ArrayList<Float> sentenceProbabilites = new ArrayList<Float>();
				ArrayList<String> selRowGrams = null;				
				
				// Making tri-gram of these tokens
				if(noOfGrams==3){
					System.out.println("---Making tri gram of the selected row");
					selRowGrams = TextOperations.getNGrams(selRowTokens, 3);
					System.out.println("No. of Selected Rows Trigram : " + selRowGrams.size());
					noOfGrams = 0;
				}else if(noOfGrams==2){	
					// Making bi-gram of these tokens
					System.out.println("---Making bi gram of the selected row");
					selRowGrams = TextOperations.getNGrams(selRowTokens, 2);
					System.out.println("No. of Selected Rows Bigram : " + selRowGrams.size());
					noOfGrams = 0;
				} else if(noOfGrams==1){
					//Now smoothing function code
					// Making bi-gram of these tokens
					System.out.println("---Inside Smoothing Part.......Making bi gram of the selected row");
					selRowGrams = TextOperations.getNGrams(selRowTokens, 2);
					for (String gElement : selRowGrams) {
						int gramOccurrences = checkDividendGramOccurences(
								biGramsList, triGramsList, gElement);
						//Smoothing formula Implementation
						int countOfUnigrams = getDivisorLastGrams(uniGramsList, biGramsList, gElement);
						float prob_occurence = (float) (gramOccurrences+1)/(countOfUnigrams+noOfUniqueWordsInCorpus);
						sentenceProbabilites.add(prob_occurence);						
					}
					Float selectedRowProbability = multiplyAllListElements(sentenceProbabilites);
					System.out.println("Final Selected Row Probability by Smoothing ::" + selectedRowProbability);
					noOfGrams = 0;
					break;
				}//end if smoothing

				
				
				
				// checking occurrences of tri-gram and bi-gram
				// At first it will start from tri-gram and then bi-gram
				
				for (String gElement : selRowGrams) {

					System.out
							.println("-------------------------------------------------------------------------");

					System.out.println("Selected Trigram for this Iteration : "
							+ gElement);
					System.out.println("Selected Trigram length : "
							+ gElement.split(" ").length);

					
					
					
					int gramOccurrences = checkDividendGramOccurences(
							biGramsList, triGramsList, gElement);
					System.out.println("Gram Occurences Count: "
							+ gramOccurrences);

					
					// If we find any occurrence of trigram, then we have to
					// shift for bi gram
					if (gramOccurrences > 0) {
						int lastGramOccurrences = getDivisorLastGrams(
								uniGramsList, biGramsList, gElement);

						// If we find any occurrence of bigram, then we have to
						// calculate probability
						float prob_occurence = 0;
						if (lastGramOccurrences > 0) {
							prob_occurence = (float) gramOccurrences
									/ lastGramOccurrences;
							System.out
									.println("Probability of Occurences Count: "
											+ prob_occurence);
							sentenceProbabilites.add(prob_occurence);
						} else {
							System.out.println("Exit from inner if ");
							sentenceProbabilites.add(prob_occurence);
							noOfGrams=1; // shifting to smoothing
							break;
						}// end if/else
					} else {
						float prob_occurence = 0;
						sentenceProbabilites.add(prob_occurence);
						System.out.println("Exit from outer if ");
						noOfGrams=2; // shifting to bi-gram
						break;
					}// end if/else
				}// end for loop to check tri-gram occurrences
				System.out.println("Sentence Probabilites : "+ sentenceProbabilites.size());

				// Multiplying all probabilities of this one selected row
				Float selectedRowProbability = multiplyAllListElements(sentenceProbabilites);
				System.out.println("Final Selected Row Probability ::" + selectedRowProbability);
			}//end while
		}// end for loop for selected row

	}// end main

	/**
	 * This function check the Upper/Dividend grams occurrences in the
	 * respective tokens list/dictionary
	 * 
	 * @param biGramsList
	 *            Dictionary with word length 2
	 * @param triGramsList
	 *            Dictionary with word length 3
	 * @param gElement
	 *            The element which we want to check in the dictionary
	 * @return no of occurrences this element exist in the list
	 */
	private static int checkDividendGramOccurences(
			ArrayList<String> biGramsList, ArrayList<String> triGramsList,
			String gElement) {
		int gramOccurrences = 0;
		if (gElement.split(" ").length == 3)
			gramOccurrences = Collections.frequency(triGramsList, gElement);
		else if (gElement.split(" ").length == 2)
			gramOccurrences = Collections.frequency(biGramsList, gElement);
		return gramOccurrences;
	}

	/**
	 * This function checks how much time gElement has been occurred in bigram
	 * or unigram tokens list
	 * 
	 * @param uniGramsList
	 *            Dictionary with word length 1
	 * @param biGramsList
	 *            Dictionary with word length 2
	 * @param gElement
	 *            The element which we want to check in the dictionary
	 * @return no of occurrences this element exist in the list
	 */
	private static int getDivisorLastGrams(ArrayList<String> uniGramsList,
			ArrayList<String> biGramsList, String gElement) {
		// Getting last two words of the selected tri-gram
		String[] lastWords = gElement.split(" ");
		String lastGrams = null;
		int lastGramOccurrences = 0;

		if (gElement.split(" ").length == 3) {
			lastGrams = lastWords[lastWords.length - 2] + " "
					+ lastWords[lastWords.length - 1];
			lastGramOccurrences = Collections.frequency(biGramsList, lastGrams);
		} else if (gElement.split(" ").length == 2) {
			lastGrams = lastWords[lastWords.length - 1];
			lastGramOccurrences = Collections
					.frequency(uniGramsList, lastGrams);
		}
		System.out.println("Last Grams : " + lastGrams);
		System.out.println("Last Gram Occurences : " + lastGramOccurrences);
		return lastGramOccurrences;
	}

	private static Float multiplyAllListElements(
			ArrayList<Float> sentenceProbabilites) {
		BigDecimal mult = new BigDecimal(1.0);
		for (Float number : sentenceProbabilites) {
			mult = mult.multiply(BigDecimal.valueOf(number));
		}
		return mult.floatValue();
	}
}// end class
