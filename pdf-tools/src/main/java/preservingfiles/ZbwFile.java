package preservingfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

public class ZbwFile implements Serializable { // TODO: does not go quite
												// smoothly. An ID seems to be
												// missing.
	/**
	 * 
	 */
	private static final long serialVersionUID = 4089068952340983707L;
	String path;
	String fileName;
	String checksumMD5;
	String mimetype;
	String fileExtension;
	File zbwFile;
	boolean isEncrypted;
	long size;

	// size
	public ZbwFile() {
	}

	public String getPath() {
		return path;
	}

	public String getMD5Checksum(File file) {
		try {
			checksumMD5 = DigestUtils.md5Hex(new FileInputStream(file));
			return checksumMD5;
		} catch (Exception e) {
			return null;
		}
	}

	public void setPath(String newPath) {
		path = newPath;
	}

	public String getName(String path) {
		String[] parts = path.toString().split(Pattern.quote("\\"));
		fileName = parts[parts.length - 1]; // filename including extension
		String[] segs = fileName.split(Pattern.quote("."));
		if (segs.length > 1) {
			fileName = segs[segs.length - 2];
		} else {
			fileName = segs[segs.length - 1]; // some file names do not have an
												// extension
		}
		return fileName;
	}

	public File toFile(String newPath) { // get a file Object from String path
		File file = new File(newPath);
		return file;
	}

	public long getSizeinKB(File file) {
		long size = file.length();

		size = size / 1024;

		return size;
	}

	public String getFileMimeType(File file) throws IOException {
		mimetype = Files.probeContentType(file.toPath());
		return mimetype;
	}

	public String getFileExtension(String path) {

		String[] segs = path.split(Pattern.quote("\\"));
		segs = segs[segs.length - 1].split(Pattern.quote("."));
		if (segs.length > 1) {
			return segs[segs.length - 1];
		} else {
			return fileExtension = "none"; // some file names do not have an
											// extension
		}
	}

	
	public boolean isEncrypted (File file) throws IOException {
		// try {
		PDDocument testfile = PDDocument.load(file);
		if (!testfile.isEncrypted()) {
			isEncrypted = false;
			return false;
		} else {
			isEncrypted = true;
			System.out.println ("Encrypted");
			return true;
			
		}
			// } catch (Exception e) {return false;return isEncrypted; }

	}

	// TODO: Formaterkennungstool wie DROID einbinden

}
