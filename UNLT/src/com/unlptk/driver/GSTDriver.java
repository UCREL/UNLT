package com.unlptk.driver;

import de.tud.kom.stringmatching.gst.GST;
import de.tud.kom.stringmatching.gst.GSTTile;
import de.tud.kom.stringmatching.gst.utils.GSTHighlighter;


public class GSTDriver {

	public static void main(String[] args){
		String haystack = "This is some text that you want to search through";
		String needle = "some text you want to find";

		/* preparing the GST object */
		GST gst = new GST(haystack);	
		gst.setMinimumTileLength(2);

		/* performing the match operation */
		gst.match(needle);

		/* displaying the results */
		System.out.println("GST found " + gst.getTiles().size() + " tile(es).");
		System.out.println("Containment in the needle " + gst.getContainmentInNeedle());
		System.out.println("Containment in the haystack " + gst.getContainmentInHaystack());

		GSTTile firstTile = gst.getTiles().get(0);
		System.out.println("The first tile consists of " + firstTile.getLength() + " tokens.");
		System.out.println("The matching tokens were: " + firstTile.getText());

		GSTTile secondTile = gst.getTiles().get(1);
		System.out.println("The second tile consists of " + secondTile.getLength() + " tokens.");
		System.out.println("The matching tokens were: " + secondTile.getText());
		
		//GST - Highlighting Matches
		GSTHighlighter highlighter = new GSTHighlighter();
		highlighter.setOpeningDelimiter("[[");
		highlighter.setClosingDelimiter("]]");
		highlighter.setMinimumTileLength(2);

		String highlightedText = highlighter.produceHighlightedText(haystack, needle);
		System.out.println(highlightedText);
		
		//GST - Highlighting Matches in XML Mode
		
	}
	
}
