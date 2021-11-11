package com.unlptk.postagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		File inFile = new File("D://file.txt");
		String outputDir = "D://";
		
		tag(outputDir, inFile);
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
	/*
	public static void reGenerate_POS_Tagged_Corpus_AfterRemovingExtraText(String path,String cs) throws IOException
	{
		String outputDir; 
		int count = 0;
		boolean flag= true;
		int datasetIndex = 0; 	
		

		File inFile1 = new File(path+"Corpus_Word/"+cs);
		String inFile2 = path+"pos_tagged_data/"+cs;
		outputDir = path+ "Corpus_POS/"+cs;
	
		String gender[]={"male","female"};
		Path target_Directories[]=new Path[2];
		
		target_Directories[0]=Files.createDirectories(Paths.get(outputDir+"/"+gender[0]));
		target_Directories[1]=Files.createDirectories(Paths.get(outputDir+"/"+gender[1]));
		
		Path target_Directory;
		
		if(inFile1.isDirectory())
		{
			File gender_Directories[]=inFile1.listFiles();//gender wise directories
			for(File gd:gender_Directories )
			{	
				if(gd.isDirectory())
				{
					//--------------------------------------------
					if(gd.getName().equalsIgnoreCase(gender[0]))
						target_Directory=target_Directories[0];
					else
						target_Directory=target_Directories[1];
					//--------------------------------------------
					File files[]=gd.listFiles();//individual files of one gender
					for(File infile1:files )
					{
						File outFile= new File(target_Directory+"/"+infile1.getName());
						File infile2=new File(inFile2+infile1.getParent().substring(infile1.getParent().lastIndexOf("\\"),infile1.getParent().length())+"/"+infile1.getName());
						//System.out.println(infile2.getAbsolutePath());
						
						Path inPath1 = Paths.get(infile1.toURI());
						Path inPath2 = Paths.get(infile2.toURI());
						
						Path outPath = Paths.get(outFile.toURI());
						datasetIndex = 0; 	
						
						try(BufferedReader br1=Files.newBufferedReader(inPath1, StandardCharsets.UTF_8))
						{
							try(BufferedReader br2=Files.newBufferedReader(inPath2, StandardCharsets.UTF_8))
							{
								try(BufferedWriter bw=Files.newBufferedWriter(outPath, StandardCharsets.UTF_8))
								{
									String strLine_Dataset=null;
									String strLine_POS_Dataset=null;
									int removed_lines=1;
									
									while ((strLine_Dataset = br1.readLine()) != null 	&& 	(strLine_POS_Dataset = br2.readLine()) != null) 
									{
										//-------------------------------skip the extra text
										while(removed_lines!=13)
										{	
											strLine_POS_Dataset = br2.readLine();
											removed_lines++;
										}
										//-------------------------------
										datasetIndex++;
										
										String dataset = strLine_Dataset;
										String tempDatasetWord= dataset.trim();
									//	System.out.println("String::"+strLine_Dataset+"\t"+strLine_POS_Dataset);
															
										String[] lexicon = strLine_POS_Dataset.split("\t+");
										String tempWord=lexicon[0].trim();
										String tempPOS= lexicon[1].trim();
										
										
										if(!tempWord.equals(tempDatasetWord))//System.out.println("String::"+tempWord+"\t"+tempPOS);
											System.out.println(infile1.getParent()+"/"+infile1.getName()+"-->"+datasetIndex);
										
										bw.write(tempDatasetWord+ "/" + tempPOS);
										bw.newLine();
									}
								}
							
							}
						}
						catch(Exception ex)
						{
							System.out.println(ex+":"+infile1);

						}
						//break;
					}
				}
			}
		}
		

		}
	//---------------------------------
	public static void generate_POS_Tagged_Corpus(String outputDir, File inFile) throws IOException
	{
		//UrduPOSTagger upt = UrduPOSTagger.getInstance();
		
		String gender[]={"male","female"};
		Path target_Directories[]=new Path[2];
		
		target_Directories[0]=Files.createDirectories(Paths.get(outputDir+"/"+gender[0]));
		target_Directories[1]=Files.createDirectories(Paths.get(outputDir+"/"+gender[1]));
		
		Path target_Directory;
		
		if(inFile.isDirectory())
		{
			File gender_Directories[]=inFile.listFiles();//gender wise directories
			for(File gd:gender_Directories )
			{	
				if(gd.isDirectory())
				{
					//--------------------------------------------
					if(gd.getName().equalsIgnoreCase(gender[0]))
						target_Directory=target_Directories[0];
					else
						target_Directory=target_Directories[1];
					//--------------------------------------------
					File files[]=gd.listFiles();//individual files of one gender
					for(File file:files )
					{
						String infile=file.getAbsolutePath();
						String outFile= target_Directory.toAbsolutePath()+"/"+file.getName();
						try
						{
							UrduPOSTagger upt = UrduPOSTagger.getInstance();
							upt.generatePOSTags(infile, outFile);
						}
						catch(Exception ex)
						{
							System.out.println(file.toString());
							continue;
						}
					}
				}
			}
		}
	}
	*/


}
