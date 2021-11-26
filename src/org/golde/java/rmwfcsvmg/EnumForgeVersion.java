package org.golde.java.rmwfcsvmg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.golde.java.rmwfcsvmg.utils.JavaUtils;

public enum EnumForgeVersion {

	v1_7_10("http://export.mcpbot.golde.org/mcp_stable/12-1.7.10/mcp_stable-12-1.7.10.zip", "1.7.10"),
	v1_8("http://export.mcpbot.golde.org/mcp_stable/18-1.8/mcp_stable-18-1.8.zip", "1.8"),
	v1_8_8("http://export.mcpbot.golde.org/mcp_stable/20-1.8.8/mcp_stable-20-1.8.8.zip", "1.8.8"),
	v1_8_9("http://export.mcpbot.golde.org/mcp_stable/22-1.8.9/mcp_stable-22-1.8.9.zip", "1.8.9"),
	v1_9("http://export.mcpbot.golde.org/mcp_stable/24-1.9/mcp_stable-24-1.9.zip", "1.9"),
	v1_9_4("http://export.mcpbot.golde.org/mcp_stable/26-1.9.4/mcp_stable-26-1.9.4.zip", "1.9.4"),
	v1_10_2("http://export.mcpbot.golde.org/mcp_stable/29-1.10.2/mcp_stable-29-1.10.2.zip", "1.10.2"),
	v1_11("http://export.mcpbot.golde.org/mcp_stable/32-1.11/mcp_stable-32-1.11.zip", "1.11"),
	v1_12("http://export.mcpbot.golde.org/mcp_stable/39-1.12/mcp_stable-39-1.12.zip", "1.12"),
	v1_13_2("http://export.mcpbot.golde.org/mcp_stable/47-1.13.2/mcp_stable-47-1.13.2.zip", "1.13.2"),
	v1_14_2("http://export.mcpbot.golde.org/mcp_stable/53-1.14.2/mcp_stable-53-1.14.2.zip", "1.14.2"),
	Snapshot("", "Snapshot"),
	;

	private String zipLink;
	private File extractionDirectory;
	private String goodName;
	EnumForgeVersion(String zipLink, String goodName) {
		this.zipLink = zipLink;
		this.goodName = goodName;
		if(!JavaUtils.isStringEmpty(goodName)) {
			extractionDirectory = new File(Main.FILE_DATA_MAPPINGS_STABLE, goodName);
		}
	}
	
	public String getZipLink() {
		return zipLink;
	}
	
	public Mapping getMappings() {
		return new Mapping(extractionDirectory);
	}
	
	public void setCustom(String snapshotNum, String versionNum) {
		snapshotNum = snapshotNum.toLowerCase();
		if(snapshotNum.startsWith("snapshot_")) {
			snapshotNum = snapshotNum.replace("snapshot_", "");
		}
		extractionDirectory = new File(Main.FILE_DATA_MAPPINGS_SNAPSHOT, snapshotNum);
		zipLink = "http://export.mcpbot.bspk.rs/mcp_snapshot/" + snapshotNum + "-" + versionNum + "/mcp_snapshot-" + snapshotNum + "-" + versionNum + ".zip";
	}

	public static String[] valuesNice() {
		List<String> toReturn = new ArrayList<String>();
		for(EnumForgeVersion value:values()) {
			toReturn.add(value.goodName);
		}
		return toReturn.toArray(new String[0]);
	}

	public static EnumForgeVersion get(String variable) {
		for(EnumForgeVersion value:values()) {
			if(variable.equalsIgnoreCase(value.goodName)) {
				return value;
			}
		}
		System.out.println("This wasnt suppost to happen!");
		return null;
	}
	
	public File getExtractionDirectory() {
		return extractionDirectory;
	}

}
