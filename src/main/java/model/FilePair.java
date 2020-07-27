package model;

public class FilePair {
	private final String filename;
	private final Integer linesOfCode;

	public FilePair(String filename, Integer linesOfCode) {
		this.filename = filename;
		this.linesOfCode = linesOfCode;
	}

	public String getFilename() {
		return filename;
	}

	public Integer getLinesOfCode() {
		return linesOfCode;
	}

	@Override
	public String toString() {
		return String.join(" : ", filename, linesOfCode.toString());
	}
}
