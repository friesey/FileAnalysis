package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import de.schlichtherle.io.FileReader;

public class htmlTextExtraction {

	public static void main(String args[]) throws IOException {

		String examinedFolder = utilities.BrowserDialogs.chooseFolder();

		PrintWriter outputXml = new PrintWriter(new FileWriter(examinedFolder + "\\HtmlTextEctraction.xml"));
		String xmlVersion = "xml version='1.0'";
		String xmlEncoding = "encoding='ISO-8859-1'";
		outputXml.println("<?" + xmlVersion + " " + xmlEncoding + "?>");
		String xmlxslStyleSheet = "<?xml-stylesheet type=\"text/xsl\" href=\"HtmlFindings.xsl\"?>";
		outputXml.println(xmlxslStyleSheet);
		outputXml.println("<HtmlSummary>");

		if (examinedFolder != null) {

			ArrayList<File> files = utilities.ListsFiles.getPaths(new File(examinedFolder), new ArrayList<File>());
			if (files != null) {

				int failed = 0;
				int successful = 0;
				int fontproblems = 0;

				for (int i = 0; i < files.size(); i++) {

					String extension = FilenameUtils.getExtension(files.get(i).toString()).toLowerCase();

					if (extension.equals("html")) {
						outputXml.println("<Finding>");
						outputXml.println("<ID>" + (i + 1) + "</ID>");

						String filename = getHtmlFileName(files.get(i));

						outputXml.println("<Filename>" + filename + "</Filename>");
						List<String> linesHtml = new ArrayList<String>();

						FileReader input = new FileReader(files.get(i).getPath());
						BufferedReader buffRd = new BufferedReader(input);
						String str;
						StringBuffer buff = new StringBuffer();

						while ((str = buffRd.readLine()) != null) {
							linesHtml.add(str);
						}

						buffRd.close();

						for (int j = 0; j < linesHtml.size(); j++) {
							buff.append(linesHtml.get(j) + "\n");

							if (linesHtml.get(j).contains("class=block_problems_problemsdescription")) {
								outputXml.println("<Fehlermeldung>" + linesHtml.get(j + 1) + "</Fehlermeldung>");
							}
						}

						String temp = new String();
						temp = buff.toString();

						if (temp.contains("successful")) {
							successful++;
							outputXml.println("<PdfAKonvertierung>" + "successful" + "</PdfAKonvertierung>");
						}

						if (temp.contains("failed")) {
							failed++;
							outputXml.println("<PdfAKonvertierung>" + "failed" + "</PdfAKonvertierung>");

							if (temp.contains("Font not embedded ")) {
								fontproblems++;
								outputXml.println("<FontProblems>" + "true" + "</FontProblems>");
							}

						}

						else {
							// nothing happens
						}

						outputXml.println("</Finding>");
					}

				}

				outputXml.println("<Summary>");
				outputXml.println("<examined>" + (failed + successful) + "</examined>");
				outputXml.println("<failed>" + failed + "</failed>");
				outputXml.println("<FontProblems>" + fontproblems + "</FontProblems>");
				outputXml.println("<successful>" + successful + "</successful>");
				// int ps;
				// int h = 100;
				// ps = failed/successful*h;
				// System.out.println(ps);
				// outputXml.println("<Prozentsatz>" + ps + "</Prozentsatz>");
				// geht nicht wegen Restwert, muesste komplizierter geloest
				// werden

				outputXml.println("</Summary>");

			}
		}
		outputXml.println("</HtmlSummary>");
		outputXml.close();
		output.XslStyleSheets.HtmlCustomizedXsl(examinedFolder + "//" + "HtmlFindings" + ".xsl");

		// TODO: Stylesheet erstellen
	}

	public static String getHtmlFileName(File file) {
		String filename;
		String[] parts = file.toString().split(Pattern.quote("\\"));
		filename = parts[parts.length - 1]; // filename including extension

		String[] segs = filename.split(Pattern.quote("."));
		filename = segs[segs.length - 3];

		return filename;
	}

}
