package filetools.pdf;

// TODO: next time, the package name should start with a small character, this is the convention

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class PdfAnalysis {

	/*******************************************************
	 * Variables and objects used within the whole package
	 ********************************************************/
	public static BufferedReader PdfHeaderTest;

	// static Logger logger = LoggerFactory.getLogger(PdfAnalysis.class);

	/*********************************************************
	 * Methods used within the whole package
	 *
	 ********************************************************/

	/****************************************************************************
	 * /********************************************************* Checks if a
	 * PDF is ok to work with %PDF Header, Broken PDF & Encryption
	 * 
	 * @param file
	 * @return: boolean true or false
	 * @throws IOException
	 */

	public static boolean testPdfOk(File file) throws IOException {
		if (filetools.GenericFileAnalysis.testFileHeaderPdf(file) == true) {

			try {

				PDDocument testfile = PDDocument.load(file);
				if (!testfile.isEncrypted()) {
					if (!checkBrokenPdf(file.toString())) {
						return true;
					} else {
						System.out.println("Broken Pdf");
						return false;
					}
				} else {
					System.out.println("Is encrypted");
					return false;
				}

			} catch (Exception e) {
				return false;
			}
		}

		else
			System.out.println("No PDF Header");
		return false;
	}

	/**
	 * Determines which PDF version it is. Can also detect PDF/A.
	 * 
	 * @param File
	 *            (should be PDF)
	 * @return: String PDF Version TODO: occasionally throws WARN about log4j
	 *          that I cannot understand or get rid of.
	 * @throws IOException
	 */

	public static String checkIfPdfA(File file) throws IOException {
		String pdfType = "No XMP Metadata";
		String xmpMetadata;
		PdfReader reader;
		try {
			reader = new PdfReader(file.toString());
			if (reader.getPdfVersion() > 3) { // unsure if it gives only values
												// higher than 50
				if (reader.getMetadata() != null) {
					xmpMetadata = new String(reader.getMetadata()); // nullpointerException
					reader.close();
					if (xmpMetadata.contains("pdfaid:conformance")) {
						pdfType = "PDF/A";
					} else {
						pdfType = "PDF 1.4 or higher";
					}
				}

				// falls keine XMP Metadata, bleibt der String bei "No XMP
				// Metadata"

			} else {
				pdfType = "PDF 1.0 - 1.3";
			}
			return pdfType;
		} catch (java.lang.NullPointerException e) {
			System.out.println(e);
			pdfType = "PDF cannot be read by PdfReader";
			// logger.error("Error analyzing " + e);
			return pdfType;
		}
	}

	/**
	 * Checks if a Pdf is too broken to be examined.
	 * 
	 * @param File
	 *            (should be PDF)
	 * @return: boolean
	 * @throws IOException
	 */

	// TODO: This function does not work, e. g. for encrypted files and should
	// not be used until it is fixed.
	public static boolean checkBrokenPdf(String file) throws IOException {

		boolean brokenPdf;
		try {
			PdfReader reader = new PdfReader(file);
			reader.getMetadata();
			// TODO: One day this function could test more and be more clever.
			brokenPdf = false;
			return brokenPdf;
		} catch (Exception e) {
			System.out.println("Broken: " + file);
			brokenPdf = true;
			// logger.error("Error analyzing " + e);
			return brokenPdf;
		}
	}

	/**
	 * Simple Encryption Test without reader, because encryption causes lots of
	 * exceptions.
	 * 
	 * @param PDDocument
	 *            (should be PDF)
	 * @return: boolean
	 * @throws IOException
	 */

	public static boolean testsEncryption(PDDocument file) throws IOException {
		// PDDocumentInformation info =
		// PDDocument.load(file).getDocumentInformation();
		if (file.isEncrypted() == true) {
			System.out.println(file + " is encrypted");
			return true;
		} else {
			return false;
		}
	}

	public static String[] extractsPdfLines(String PdfFile) throws IOException {
		try {
			StringBuffer buff = new StringBuffer();
			String ExtractedText = null;
			PdfReader reader = new PdfReader(PdfFile);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy strategy;

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
				ExtractedText = strategy.getResultantText().toString();
				buff.append(ExtractedText + "\n");
			}

			String[] LinesArray;
			LinesArray = buff.toString().split("\n");
			reader.close();
			return LinesArray;
		} catch (Exception e) {
			return null;
		}
	}

	public static String extractsPdfLinestoString(String PdfFile) throws IOException {
		try {
			StringBuffer buff = new StringBuffer();
			String ExtractedText = null;
			PdfReader reader = new PdfReader(PdfFile);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy strategy;

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
				ExtractedText = strategy.getResultantText().toString();
				buff.append(ExtractedText + "\n");
			}
			return buff.toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static int getPdfVersion(String pdffile) throws IOException {
		BufferedReader fileReader = new BufferedReader(new FileReader(pdffile));
		String fileHeader = fileReader.readLine();
		fileReader.close();

		if (fileHeader.contains("%PDF-1.2")) {
			return 2;
		} else if (fileHeader.contains("%PDF-1.3")) {
			return 3;
		} else if (fileHeader.contains("%PDF-1.4")) {
			return 4;
		} else if (fileHeader.contains("%PDF-1.5")) {
			return 5;
		} else if (fileHeader.contains("%PDF-1.6")) {
			return 6;
		} else if (fileHeader.contains("%PDF-1.7")) {
			return 7;
		} else {
			return 7;
		}
	}

	/**
	 * Checks the size of the Pdf-file, because some big Pdf Files might cause
	 * exceptions. *
	 * 
	 * @param file
	 *            (should be Pdf) @return: boolean @throws
	 */

	/*
	 * I think this method is so complicated because of the test that was build.
	 * Maybe change method in GenericFileAnalysis eventually to embedd those
	 * kinds of tests, too.
	 * 
	 * public static boolean checkPdfSize(File file) { boolean toobig =
	 * isFileTooLong(file, DEFAULT_MAX_FILE_LENGTH); if (toobig) { System.out
	 * .println("File is bigger than 16 MB and therefore cannot be measured"); }
	 * return toobig; }
	 * 
	 * public static boolean checkPdfSize(String filePath) { File toCheck = new
	 * File (filePath); return checkPdfSize(toCheck); }
	 * 
	 * public static boolean isFileTooLong(File toCheck, long maxLength) {
	 * return (toCheck.length() > maxLength); }
	 */
}
