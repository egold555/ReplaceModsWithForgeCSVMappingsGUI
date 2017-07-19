package org.golde.java.rmwfcsvmg;

import java.io.File;
import java.util.ArrayList;

import org.golde.java.rmwfcsvmg.utils.FernFlowerUtils;
import org.golde.java.rmwfcsvmg.utils.FileUtils;
import org.golde.java.rmwfcsvmg.utils.JavaUtils;
import org.golde.java.rmwfcsvmg.windows.PanelMain;

public class ThreadReplaceCSV extends Thread{

	private EnumForgeVersion selectedForgeVersion;
	private File selectedFile;
	private PanelMain panelMain;
	private long startTime;

	public ThreadReplaceCSV(EnumForgeVersion selectedForgeVersion, File selectedFile, PanelMain panelMain) {

		this.selectedForgeVersion = selectedForgeVersion;
		this.selectedFile = selectedFile; 
		this.panelMain = panelMain;
		startTime = System.nanoTime();
		this.start();
	}
	
	void log(String msg) {
		System.out.println(msg);
	}

	@Override
	public void run() {
		panelMain.btnGo.setEnabled(false);
		panelMain.btnSelectMod.setEnabled(false);
		panelMain.comboBoxForgeVersion.setEnabled(false);

		
		Mapping mappings = selectedForgeVersion.getMappings();
		log("Downloading CSV mappings...");
		if(!selectedForgeVersion.getMappings().hasAlreadyDownloaded()) {
			String zipFile = FileUtils.downloadFile(selectedForgeVersion.getZipLink(), "zip");
			if(zipFile == null) {
				JavaUtils.simpleError("Could not find ZIP @: " + selectedForgeVersion.getZipLink() + ".zip", panelMain);
				reset();
				return;
			}
			log("Unzipping CSV Mappings....");
			FileUtils.unZipIt(zipFile, selectedForgeVersion.getExtractionDirectory());
		}
		updateProgress(0.1);

		try {
			//Delete existing code directory files
			log("Deleting code directory...");
			FileUtils.deleteDirectory(Main.FILE_CODE);

			log("====FernFlower Decomile Start====");
			FernFlowerUtils.decompile(selectedFile, Main.FILE_DATA_TEMP, false);
			log("====FernFlower Decomile End====");
			updateProgress(0.5);
			log("unziping FernFlower output to code folder...");
			FileUtils.unZipIt(new File(Main.FILE_DATA_TEMP, selectedFile.getName()), Main.FILE_CODE);
			updateProgress(0.6);

			ArrayList<File> filesToReplace = new ArrayList<File>();
			FileUtils.listFilesIncludingSub(Main.FILE_CODE, filesToReplace);
			log("====Replace CSV Start====");
			for(File file:filesToReplace) {
				log("Replacing " + file.getName());
				FileUtils.replaceSelected(file, FileUtils.gatherReplacementsFromFile(mappings.getCSV_FIELDS()));
				FileUtils.replaceSelected(file, FileUtils.gatherReplacementsFromFile(mappings.getCSV_METHODS()));
				FileUtils.replaceSelected(file, FileUtils.gatherReplacementsFromFile(mappings.getCSV_PARAMS()));
			}
			log("====Replace CSV End====");
		}catch(Exception e) {
			JavaUtils.simpleError(e, panelMain);
			reset();
			return;
		}
		updateProgress(1.0);
		//End
		JavaUtils.successMessage("Successfully decompiled mod in " + elapsedTime(startTime) + " seconds!", panelMain);
		reset();
	}

	private void reset() {
		FileUtils.deleteDirectory(Main.FILE_DATA_TEMP);
		panelMain.btnGo.setEnabled(true);
		panelMain.btnSelectMod.setEnabled(true);
		panelMain.comboBoxForgeVersion.setEnabled(true);
	}

	private void updateProgress(double percentFinished) {
		int percent = (int)Math.round(percentFinished * 100);
		panelMain.progressBar.setString("Processing " + percent + "%");
		panelMain.progressBar.setValue(percent);
	}
	
	private double elapsedTime(long startTime) {
		return (double)(System.nanoTime() - startTime) / 1000000000.0;
	}

}
