package preservingfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.pdf.PdfReader;

public class ZbwFilePdf extends ZbwFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PDDocument pdfFile;
	static boolean isEncrypted;
	boolean isPdfA;
	String originalFile;

	public static boolean testFileHeaderPdf(String file) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader(file));
		String FileHeader = fileReader.readLine();
		fileReader.close();
		if (FileHeader != null) {
			if (FileHeader.contains("%PDF")) {
				return true;

			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public static PDDocument toPDDocument(File file) throws IOException {
		try {
			PDDocument pdfFile = PDDocument.load(file);
			return pdfFile;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isEncrypted(PDDocument pdfFile) {
		if (pdfFile.isEncrypted()) {
			isEncrypted = true;
			return true;
		} else {
			isEncrypted = false;
			return false;
		}
	}

	// TODO: int getPdfVersion

	public static boolean isPdfA(String pdfString) {
		System.out.println(pdfString);
		try {
			PdfReader reader = new PdfReader(pdfString);
			if (reader.getMetadata() != null) {
				String xmpMetadata = new String(reader.getMetadata()); // nullpointerException
				reader.close();
				if (xmpMetadata.contains("pdfaid:conformance")) {
					return true;
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			System.out.println("An Exception occured while testing if PDF/A: " + e);
		}
		return false;
	}

	public static boolean testEncryption(File file) throws IOException {
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

	public static boolean checkBrokenPdf(String file) throws IOException {
		try {
			PdfReader reader = new PdfReader(file);
			reader.getMetadata();
			// TODO: One day this function could test more and be more clever.
			return false;
		} catch (Exception e) {
			System.out.println("Broken: " + file);
			return true;
		}
	}

}
