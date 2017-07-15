package org.golde.java.rmwfcsvmg;

import java.io.File;

public class Mapping {

	private final boolean hasAlreadyDownloaded;
	private final File CSV_FIELDS;
	private final File CSV_METHODS;
	private final File CSV_PARAMS;
	
	public Mapping(File extractionDirecxtory) {
		hasAlreadyDownloaded = extractionDirecxtory.exists();
		CSV_FIELDS = new File(extractionDirecxtory, "fields.csv");
		CSV_METHODS = new File(extractionDirecxtory, "methods.csv");
		CSV_PARAMS = new File(extractionDirecxtory, "params.csv");
	}
	
	public boolean hasAlreadyDownloaded() {
		return hasAlreadyDownloaded;
	}
	
	public File getCSV_FIELDS() {
		return CSV_FIELDS;
	}
	
	public File getCSV_METHODS() {
		return CSV_METHODS;
	}
	
	public File getCSV_PARAMS() {
		return CSV_PARAMS;
	}
	
	
}
