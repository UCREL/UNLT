package com.unlptk.tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Row{
	
	private List<String> rowElements = new ArrayList<String>();
	private int noOfTokens;
	private boolean flagBit;
	
	public Row(){
		this.rowElements = this.getRowElements();
		this.noOfTokens = this.getNoOfTokens();
		this.flagBit = this.isFlagBit();
	}
	
	public Row(Row row) {		
		/*this.rowElements = row.rowElements;
		this.noOfTokens = row.noOfTokens;
		this.flagBit = row.flagBit;*/
		this.rowElements = new ArrayList<String>(row.getRowElements());
		this.noOfTokens = new Integer(row.getNoOfTokens());
		this.flagBit = new Boolean(row.isFlagBit());
	}
	
	
	public List<String> getRowElements() {
		return rowElements;
	}
	public void addRowElement(String rowElement) {
		this.rowElements.add(rowElement);
	}
	public int getNoOfTokens() {
		this.noOfTokens=this.rowElements.size();
		return noOfTokens;
	}
	
	public boolean isFlagBit() {
		return flagBit;
	}
	public void setFlagBit(boolean flagBit) {
		this.flagBit = flagBit;
	}

	@Override
	public String toString() {
		return "Row [rowElements=" + rowElements + ", noOfTokens=" + noOfTokens
				+ ", flagBit=" + flagBit + "]";
	}
	
	
}
