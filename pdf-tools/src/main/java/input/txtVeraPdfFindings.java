package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class txtVeraPdfFindings {

	public static void main(String args[]) throws IOException {

		JOptionPane.showMessageDialog(null, "Please choose the veraPDF file to examine", "veraPDF Findings",
				JOptionPane.QUESTION_MESSAGE);
		String examinedFile = utilities.BrowserDialogs.chooseFile();
		JOptionPane.showMessageDialog(null, "Please choose the folder for Xml Findings.", "veraPDF Findings",
				JOptionPane.QUESTION_MESSAGE);
		String outputFolder = utilities.BrowserDialogs.chooseFolder();

		PrintWriter outputXml = new PrintWriter(new FileWriter(outputFolder + "\\veraPDF.xml"));
		String xmlVersion = "xml version='1.0'";
		String xmlEncoding = "encoding='ISO-8859-1'";
		outputXml.println("<?" + xmlVersion + " " + xmlEncoding + "?>");
		String xmlxslStyleSheet = "<?xml-stylesheet type=\"text/xsl\" href=\"veraPDF.xsl\"?>"; // TODO
		outputXml.println(xmlxslStyleSheet);
		outputXml.println("<veraPDFSummary>");

		FileReader input = new FileReader(examinedFile);
		BufferedReader br = new BufferedReader(input);
		String str;

		int pass = 0;
		int fail = 0;

		while ((str = br.readLine()) != null) {

			if (str.startsWith("FAIL")) {
				fail++;

				String failPdf = utilities.fileStringUtilities.getFileNameFromString(str);

				outputXml.println("<item>");
				outputXml.println("<veraPDFExamination>" + "failed" + "</veraPDFExamination>");
				outputXml.println("<Filename>" + failPdf + "</Filename>");
				outputXml.println("</item>");
			}

			else if (str.startsWith("PASS")) {
				pass++;
				
				String passPdf = utilities.fileStringUtilities.getFileNameFromString(str);

				outputXml.println("<item>");
				outputXml.println("<veraPDFExamination>" + "passed" + "</veraPDFExamination>");
				outputXml.println("<Filename>" + passPdf + "</Filename>");
				outputXml.println("</item>");

			}

		}

		br.close();

		outputXml.println("<PdfAValidity>");
		outputXml.println("<Fail>" + fail + "</Fail>");
		outputXml.println("<Pass>" + pass + "</Pass>");
		outputXml.println("</PdfAValidity>");

		outputXml.println("</veraPDFSummary>");

		outputXml.close();
		
		output.XslStyleSheets.veraPdfXsl(outputFolder + "//" + "veraPDF" + ".xsl");

	}

}
