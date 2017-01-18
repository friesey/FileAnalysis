package preservingfiles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.pdf.PdfReader;

import filetools.pdf.PdfAnalysis;

public class PdfAnalysator {

	static String examinedFolder;
	String pathwriter;
	static PrintWriter xmlSimpleWriter;

	static int MAXIMUM_SIZE_PDF = 15500;

	// TODO: If a PDF is bigger than a certain size, do not do certain tests,
	// that will fail the Heap Space. The same is true for the number of pages.
	// It is too difficult to determine exactly where the borders are, that is
	// why I have chosen something arbitrary which puts the program on the save
	// side.

	public static void main(String args[]) throws IOException {

		changecolor();

		JOptionPane.showMessageDialog(null, "Please choose the folder with files to analyse.", "File Analysis",
				JOptionPane.QUESTION_MESSAGE);
		examinedFolder = utilities.BrowserDialogs.chooseFolder();

		if (examinedFolder != null) {

			JFrame f = new JFrame();
			JButton but = new JButton("... Program is running ... ");
			f.add(but, BorderLayout.PAGE_END);
			f.pack();
			f.setVisible(true);

			xmlSimpleWriter = new PrintWriter(new FileWriter(examinedFolder + "\\FileAnalysis.xml"));

			String xsltLocation = (examinedFolder + "\\Analyst.xsl");
			output.XslStyleSheets.fileAnalysis(xsltLocation);
			startingXmlOutput();

			ArrayList<File> files = utilities.ListsFiles.getPaths(new File(examinedFolder), new ArrayList<File>());
			ArrayList<ZbwFile> findings = new ArrayList<ZbwFile>();

			xmlSimpleWriter.println("<FileAnalysisSummary>");

			for (int i = 0; i < files.size(); i++) {

				if (ZbwFilePdf.testFileHeaderPdf(files.get(i).toString())) {

					ZbwFile testfile = new ZbwFile();
					testfile.fileName = testfile.getName(files.get(i).toString());

					testfile.path = files.get(i).toString();
					testfile.zbwFile = testfile.toFile(testfile.path);
					testfile.mimetype = testfile.getFileMimeType(testfile.zbwFile);

					testfile.checksumMD5 = testfile.getMD5Checksum(testfile.zbwFile);
					testfile.size = testfile.getSizeinKB(testfile.toFile(testfile.path));
					testfile.mimetype = testfile.getFileMimeType(testfile.toFile(testfile.path));
					testfile.fileExtension = testfile.getFileExtension(testfile.path);
					testfile.isEncrypted = testfile.isEncrypted(files.get(i));

					// Aktionen von unten noch einbauen und auch aus alten
					// Programmen. Ggf. Summary aus allem erstellen. Siehe
					// andere Programme

					findings.add(testfile);

					System.out.println(i); // nur um zu sehen dass das Programm
											// noch arbeitet
				}
			}

			for (int i = 0; i < findings.size(); i++) {
				xmlSimpleWriter.println("<File>");
				xmlSimpleWriter.println("<FileName><![CDATA[" + findings.get(i).fileName + "]]></FileName>");
				xmlSimpleWriter.println("<MD5Checksum>" + findings.get(i).checksumMD5 + "</MD5Checksum>");
				xmlSimpleWriter.println("<FileSizeKB>" + findings.get(i).size + "</FileSizeKB>");
				xmlSimpleWriter.println("<Mimetype>" + findings.get(i).mimetype + "</Mimetype>");
				xmlSimpleWriter
						.println("<FileExtension>" + findings.get(i).fileExtension.toLowerCase() + "</FileExtension>");
				findings.get(i);
				xmlSimpleWriter.println("<PdfEncryption>" + findings.get(i).isEncrypted + "</PdfEncryption>");

				// alle Methoden von hier unten nach oben verlegen

				if (findings.get(i).mimetype != null) {
					if (findings.get(i).mimetype.equals("application/pdf")) {
						ZbwFilePdf testfilePdf = new ZbwFilePdf();
						System.out.println(findings.get(i).fileName);
						System.out.println(findings.get(i).size);
						if (findings.get(i).size < MAXIMUM_SIZE_PDF) {
							
							//wenn encryption, dann keine weiteren Metadaten

							if (PdfAnalysis.testPdfOk(findings.get(i).zbwFile)) {
								PDDocument analyzefile = PDDocument.load(findings.get(i).zbwFile);

								String PdfType = PdfAnalysis.checkIfPdfA(findings.get(i).zbwFile);

								if (PdfType.equals("PDF/A")) {
									// PDMetadata metadata = new
									// PDMetadata(analyzefile);
									String level = "no";

									int pdfaVersion = 0;

									BufferedReader in = new BufferedReader(new FileReader(findings.get(i).zbwFile));
									String line;
									while ((line = in.readLine()) != null) {
										if (line.contains("pdfaid:part>1<")) {
											pdfaVersion = 1;
										} else if (line.contains("pdfaid:part='1'")) {
											pdfaVersion = 1;
										} else if (line.contains("pdfaid:part=\"1\"")) {
											pdfaVersion = 1;
										} else if (line.contains("pdfaid:part>2<")) {
											pdfaVersion = 2;
										} else if (line.contains("pdfaid:part='2'")) {
											pdfaVersion = 2;
										} else if (line.contains("pdfaid:part=\"2\"")) {
											pdfaVersion = 2;
										} else if (line.contains("pdfaid:part") && line.contains("1")) {
											pdfaVersion = 1;
										} else if (line.contains("pdfaid:part") && line.contains("2")) {
											pdfaVersion = 2;
										}
									}

									xmlSimpleWriter.println("<PdfA>" + PdfType + "-" + pdfaVersion + "</PdfA>");
								}

								else
									xmlSimpleWriter.println("<PdfA>" + PdfType + "</PdfA>");

								String ProducerType;

								PdfReader reader = new PdfReader(findings.get(i).zbwFile.toString());
								Map<String, String> metadata = reader.getInfo();
								if (metadata.get("Producer") != null) {
									ProducerType = (metadata.get("Producer").toString());

									xmlSimpleWriter
											.println("<PdfCreationSoftware>" + ProducerType + "</PdfCreationSoftware>");
								}

								if (metadata.get("CreationDate") != null) {
									if (metadata.get("CreationDate").length() > 10) {
										String creationYear = getYear(metadata.get("CreationDate"));
										// int creationYearInt =
										// Integer.parseInt(creationYear);
										xmlSimpleWriter.println("<CreationYear>" + creationYear + "</CreationYear>");
									}
								}

								char pdfVersion = reader.getPdfVersion();

								xmlSimpleWriter.println("<PdfVersion>" + "PDF 1." + pdfVersion + "</PdfVersion>");

							}
						}
					}

				}

				// Test PDF/A

				// Baujahr
				// PDF Version
				// Herstellungssoftware

				xmlSimpleWriter.println("</File>");

			}
			xmlSimpleWriter.println("</FileAnalysisSummary>");
			xmlSimpleWriter.close();
			f.dispose();
		}

	}

	private static void startingXmlOutput() {
		String xmlVersion = "xml version='1.0'";
		String xmlEncoding = "encoding='ISO-8859-1'";
		xmlSimpleWriter.println("<?" + xmlVersion + " " + xmlEncoding + "?>");
		xmlSimpleWriter.println("<?xml-stylesheet type=\"text/xsl\" href=\"Analyst.xsl\"?>");
	}

	private static void changecolor() {
		UIManager.put("OptionPane.background", Color.white);
		UIManager.put("Panel.background", Color.white);
	}

	private static String getYear(String creationYear) {
		creationYear = creationYear.replace("D:", "");
		String year = creationYear.substring(0, 4);
		return year;
	}

}
