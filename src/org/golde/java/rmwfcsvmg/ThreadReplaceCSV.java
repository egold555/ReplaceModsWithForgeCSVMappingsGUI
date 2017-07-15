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

	public ThreadReplaceCSV(EnumForgeVersion selectedForgeVersion, File selectedFile, PanelMain panelMain) {

		this.selectedForgeVersion = selectedForgeVersion;
		this.selectedFile = selectedFile; 
		this.panelMain = panelMain;

		this.start();
	}

	@Override
	public void run() {
		panelMain.btnGo.setEnabled(false);
		panelMain.btnSelectMod.setEnabled(false);
		panelMain.comboBoxForgeVersion.setEnabled(false);

		Mapping mappings = selectedForgeVersion.getMappings();
		if(!selectedForgeVersion.getMappings().hasAlreadyDownloaded()) {
			String zipFile = FileUtils.downloadFile(selectedForgeVersion.getZipLink(), "zip");
			if(zipFile == null) {
				JavaUtils.simpleError("Could not find ZIP @: " + selectedForgeVersion.getZipLink() + ".zip", panelMain);
				reset();
				return;
			}
			FileUtils.unZipIt(zipFile, selectedForgeVersion.getExtractionDirectory());
		}
		updateProgress(0.1);

		try {
			//Delete existing code directory files
			FileUtils.deleteDirectory(Main.FILE_CODE);

			FernFlowerUtils.decompile(selectedFile, Main.FILE_DATA_TEMP, false);
			updateProgress(0.5);
			FileUtils.unZipIt(new File(Main.FILE_DATA_TEMP, selectedFile.getName()), Main.FILE_CODE);
			updateProgress(0.6);

			ArrayList<File> filesToReplace = new ArrayList<File>();
			FileUtils.listFilesIncludingSub(Main.FILE_CODE, filesToReplace);
			for(File file:filesToReplace) {
				FileUtils.replaceSelected(file, FileUtils.gatherReplacementsFromFile(mappings.getCSV_FIELDS()));
				FileUtils.replaceSelected(file, FileUtils.gatherReplacementsFromFile(mappings.getCSV_METHODS()));
				FileUtils.replaceSelected(file, FileUtils.gatherReplacementsFromFile(mappings.getCSV_PARAMS()));
			}
		}catch(Exception e) {
			JavaUtils.simpleError(e, panelMain);
			reset();
			return;
		}
		updateProgress(1.0);
		//End
		reset();
	}

	void reset() {
		FileUtils.deleteDirectory(Main.FILE_DATA_TEMP);
		panelMain.btnGo.setEnabled(true);
		panelMain.btnSelectMod.setEnabled(true);
		panelMain.comboBoxForgeVersion.setEnabled(true);
	}

	void updateProgress(double percentFinished) {
		int percent = (int)Math.round(percentFinished * 100);
		panelMain.progressBar.setString("Processing " + percent + "%");
		panelMain.progressBar.setValue(percent);
	}

}
