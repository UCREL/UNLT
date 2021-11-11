package com.unlptk.tokenizer;


public class TextGramsProbability {

	private String dividendText;
	private String divisorText;
	private int dividendTextOccurrenceCount;
	private int divisorTextOccurrenceCount;
	private float pairProbability;
	
	
	
	
	
	public TextGramsProbability(String dividendText, String divisorText,
			int dividendTextOccurrenceCount, int divisorTextOccurrenceCount,
			float pairProbability) {
		super();
		this.dividendText = dividendText;
		this.divisorText = divisorText;
		this.dividendTextOccurrenceCount = dividendTextOccurrenceCount;
		this.divisorTextOccurrenceCount = divisorTextOccurrenceCount;
		this.pairProbability = pairProbability;
	}
	public String getDividendText() {
		return dividendText;
	}
	public void setDividendText(String dividendText) {
		this.dividendText = dividendText;
	}
	public String getDivisorText() {
		return divisorText;
	}
	public void setDivisorText(String divisorText) {
		this.divisorText = divisorText;
	}
	public int getDividendTextOccurrenceCount() {
		return dividendTextOccurrenceCount;
	}
	public void setDividendTextOccurrenceCount(int dividendTextOccurrenceCount) {
		this.dividendTextOccurrenceCount = dividendTextOccurrenceCount;
	}
	public int getDivisorTextOccurrenceCount() {
		return divisorTextOccurrenceCount;
	}
	public void setDivisorTextOccurrenceCount(int divisorTextOccurrenceCount) {
		this.divisorTextOccurrenceCount = divisorTextOccurrenceCount;
	}
	public float getPairProbability() {
		return pairProbability;
	}
	public void setPairProbability(float pairProbability) {
		this.pairProbability = pairProbability;
	}
	
	
	
	
	
}
