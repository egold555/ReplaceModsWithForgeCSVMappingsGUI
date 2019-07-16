package org.golde.java.rmwfcsvmg.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.opencsv.CSVReader;

public class FileUtils {

	public static String downloadFile(String url, String extention) {
		try {
			String fName = "data/temp/tempFile." + extention;
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(fName);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return fName;
		}catch(Exception e) {
			System.err.println("Could not find ZIP @: " + url + extention);
			return null;
		}
	}

	public static void unZipIt(File zipFile, File outputFolder) {
		unZipIt(zipFile.getAbsolutePath(), outputFolder.getAbsolutePath());
	}
	public static void unZipIt(String zipFile, File outputFolder) {
		unZipIt(zipFile, outputFolder.getAbsolutePath());
	}
	public static void unZipIt(String zipFile, String outputFolder){

		byte[] buffer = new byte[1024];

		try{

			//create output directory is not exists
			File folder = new File(outputFolder);
			if(!folder.exists()){
				folder.mkdir();
			}

			//get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){
				String fileName = ze.getName();
				if (! fileName.endsWith("/")) {
					File newFile = new File(outputFolder + File.separator + fileName);

					//create all non exists folders
					//else you will hit FileNotFoundException for compressed folder
					new File(newFile.getParent()).mkdirs();

					FileOutputStream fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();


		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public static void listFilesIncludingSub(File directory, ArrayList<File> files) {
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				listFilesIncludingSub(file, files);
			}
		}
	}
	public static void deleteDirectory(File dir) {
		deleteDirectoryNon(dir);
		dir.mkdirs();
	}

	private static boolean deleteDirectoryNon(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles(); 
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectoryNon(children[i]);
				if (!success) {
					return false; 
				} 
			} 
		} 
		return dir.delete(); 
	}

	public static void replaceSelected(File file, ArrayList<Replacement> replacements) {
		replaceSelected(file, file, replacements);
	}
	
	public static void replaceSelected(File oFile, File nFile, ArrayList<Replacement> replacements) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(oFile));
			String line;String input = "";
			while ((line = file.readLine()) != null) input += line + "\r\n";
			file.close();

			for (Replacement repl: replacements) {
				input = input.replace(repl.replaceWhat, repl.replaceWith); 
			}

			FileOutputStream fileOut = new FileOutputStream(nFile);
			fileOut.write(input.getBytes());
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Replacement> gatherReplacementsFromFile(File csv) {
		ArrayList<Replacement> replacements = new ArrayList<Replacement>();

		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csv));
			String[] line;
			while ((line = reader.readNext()) != null) {
				String obf = line[0];
				String forge = line[1];
				replacements.add(new Replacement(obf, forge));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return replacements;
	}

	private static class Replacement {
		public String replaceWhat;
		public String replaceWith;
		public Replacement(String what, String with) {
			this.replaceWhat = what;
			this.replaceWith = with;
		}
	}
	
	public static String getFileExtension(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}

}
