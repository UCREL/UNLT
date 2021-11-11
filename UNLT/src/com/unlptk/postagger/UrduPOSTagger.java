package com.unlptk.postagger;

import java.awt.image.BufferedImageFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.unlptk.driver.POSDriver;
import com.unlptk.util.UNLPTKConstants;

public class UrduPOSTagger {

	private static final Logger logger = Logger.getLogger(POSDriver.class.getName());
	private static UrduPOSTagger modelInstance;
	
	
	public static void main(String[] args) throws IOException{
		
		UrduPOSTagger upt = UrduPOSTagger.getInstance();
		String inputTokensFilePath ="E:\\Jawad\\Data\\tokens.tt";
		String  outputFilePath = "E:\\Jawad\\Data\\umaar.txt";
		
		upt.generatePOSTags(inputTokensFilePath, outputFilePath);
//		generate_POS_Tagged_Corpus(outputFilePath, new File(inputTokensFilePath));
		tag("G://", new File("G://_ttttt.txt"));
	}
	public static void tag(String outputDir, File inFile) throws IOException
	{
		File outFile= new File(outputDir+"/__"+inFile.getName());
		
		Path outPath = Paths.get(outFile.toURI());
		try
		{
			UrduPOSTagger upt = UrduPOSTagger.getInstance();
			upt.generatePOSTags(inFile, outFile);
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}	
	}
	//------------------------------- a helper methods :@ Saba Anwar
	public void generatePOSTags(File inFile, File outFile)
	{
		generatePOSTags(inFile.getAbsolutePath(), outFile.getAbsolutePath());
	}
	//--------------------------------
	public static UrduPOSTagger getInstance(){
		
		if(modelInstance == null){
			
			System.out.println("Inside getInstance");
			
			modelInstance = new UrduPOSTagger();
			
			System.out.println("Inside 2 getInstance");
		}
		
		return modelInstance;
	}

	
	
	public UrduPOSTagger() {
		// TODO Auto-generated constructor stub
		trainTagger();		
	}

	public void generatePOSTags(String inputTokensFilePath, String outputFilePath){
		//TODO 2. Generating POS for Testing File
		//tntTaggerCommand = "tnt 1train 1test.tt > jawad.tts";
		String workDir = UNLPTKConstants.TNT_TAGGER_DIR;
		String tntTaggerCommand = "tnt 1train "+inputTokensFilePath+" > "+outputFilePath;
		//String tntTaggerCommand = "tnt 1train "+new File(inputTokensFilePath).getAbsolutePath()+" > "+new File(outputFilePath).getAbsolutePath();
		try {
			POSDriver.runCommand(tntTaggerCommand, workDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//logger.info("......Finished Generation of POS & Testing File....");
	}
	
	private static void trainTagger(){
		String tntTaggerCommand;		
		String workDir = UNLPTKConstants.TNT_TAGGER_DIR;
		//String workDir = ".";
		
		logger.info("......Training Urdu POS Tagger....");
		
		//TODO 1. Train The Tagger
		tntTaggerCommand = "tnt-para 1train.tt";
		try {
			runCommand(tntTaggerCommand, workDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("......Finished Training Urdu POS Tagger....");	

	}
	
	
	private static void runCommand(String command, String workDir) throws Exception {
		logger.info("Run: " + command);
		System.out.println(new File(workDir).getAbsolutePath());
		//Process p = Runtime.getRuntime().exec("cmd /c "+command, null , new File(workDir));
		Process p = Runtime.getRuntime().exec("cmd /c "+command, null , new File(new File(workDir).getAbsolutePath()));
	}
	
}
