package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class imagemagickAnalysis {

	public static void main(String args[]) throws IOException {

		// JOptionPane.showMessageDialog(null, "Please choose the ImageMagick
		// Findings file to examine","ImageMagick Findings",
		// JOptionPane.QUESTION_MESSAGE);
		// String examinedFile = utilities.BrowserDialogs.chooseFile();

		String examinedFile = "C://Users//Friese Yvonne//ImageMagick-7.0.3-Q16//tiffreport_cleaned.txt";

		JOptionPane.showMessageDialog(null, "Please choose the folder for Xml Findings.", "ImageMagick Findings",
				JOptionPane.QUESTION_MESSAGE);
		String outputFolder = utilities.BrowserDialogs.chooseFolder();

		PrintWriter outputXml = new PrintWriter(new FileWriter(outputFolder + "\\imageMagick.xml"));
		String xmlVersion = "xml version='1.0'";
		String xmlEncoding = "encoding='ISO-8859-1'";
		outputXml.println("<?" + xmlVersion + " " + xmlEncoding + "?>");
		String xmlxslStyleSheet = "<?xml-stylesheet type=\"text/xsl\" href=\"imageMagick.xsl\"?>"; // TODO
		outputXml.println(xmlxslStyleSheet);
		outputXml.println("<imageMagickSummary>");

		FileReader input = new FileReader(examinedFile);
		BufferedReader br = new BufferedReader(input);
		String str;

		ArrayList<String> errormessages = new ArrayList<String>();
		String error;

		int corruptImages = 0;

		int analysedTiff = 0;

		ArrayList<String> lines;
		lines = new ArrayList<String>();

		while ((str = br.readLine()) != null) {
			lines.add(str);
		}

		outputXml.println("<FileAnalysis>");

		boolean valid = false;

		for (int i = 0; i + 1 < lines.size(); i++) {

			if (lines.get(i).startsWith("C:")) {
				outputXml.println("<File>");
				analysedTiff++;

				String filePath = getFilePath(lines.get(i));

				outputXml.println("<Number>" + analysedTiff + "</Number>");

				outputXml.println("<Filename>" + filePath + "</Filename>");

				if (lines.get(i + 1).startsWith("identify")) {
					outputXml.println("<Errors>");
					corruptImages++;
					valid = false;

					int fest = i + 1;

					for (int j = 0; fest + j < lines.size(); j++) {
						if (!lines.get(fest + j).startsWith("C:")) {

							error = lines.get(fest + j);

							error = error.replace("identify: ", "");
							error = error.replace(" @ warning/tiff.c/TIFFWarnings/918.", "");
							error = error.replace(" @ error/tiff.c/TIFFErrors/565.", "");

							outputXml.println("<Message>" + error + "</Message>");

							if (error.contains("Unknown field")) {
								error = "Unknown field encountered.";
							}

							else if (error.contains("Failed to read custom directory at offset")
									&& error.contains("TIFFReadCustomDirectory")) {
								error = "Failed to read custom directory at offset. 'TIFFReadCustomDirectory'";
							}

							else if (error.contains("unexpected end-of-file")) {
								error = "unexpected end-of-file";
							}

							else if (error.contains("improper image header")) {
								error = "improper image header";
							}

							else if (error.contains("compression not supported")) {
								error = "compression not supported";
							}

							else if (error.contains("Incorrect count for")) {
								error = "incorrect count for field";
							}
							
							else if (error.contains("incorrect count for")) {
								error = "incorrect count for field";
							}
					
							else if (error.contains("Wrong data type") && error.contains("TIFFReadCustomDirectory")) {
								error = "Wrong data type for tag; tag ignored. `TIFFReadCustomDirectory'";
							}
							
							else if (error.contains("Sanity check on size") && error.contains("value failed")) {
								error = "Sanity check on size of field value failed.";
							}
										

							else if (error.contains("Failed to read") && error.contains("directory")) {
								error = "Failed to read directory";
							}

							else if (error.contains("Nonstandard tile") && error.contains("convert file")) {
								error = "Nonstandard tile width or length, convert file.";
							}

							else if (error.contains("Sanity check on directory count failed")) {
								error = "Sanity check on directory count failed ";
							}

							else if (error.contains("Incorrect value")) {
								error = "Incorrect value for tag";
							}

							else if (error.contains("Incompatible type")) {
								error = "Incompatible type for tag";
							}

							else if (error.contains("Cannot determine size of unknown tag type")) {
								error = "Cannot determine size of unknown tag";
							}

							else if (error.contains("Not a TIFF file")) {
								error = "Not a (valid) TIFF file";
							}

							else if (error.contains("IO error during reading")) {
								error = "IO error during reading tag";
							}

							else if (error.contains("ASCII value for tag")) {
								error = "ASCII value for tag not valid";
							}

							else if (error.contains("Null count for")) {
								error = "Null count for tag";
							}

							else if (error.contains("Bad value")) {
								error = "Bad value for tag";
							}

							else if (error.contains("TIFF directory is missing required")) {
								error = "TIFF directory is missing required field";
							}
							errormessages.add(error);
						}

						else {
							break;
						}
					}
					outputXml.println("</Errors>");
				}

				else {
					valid = true;
				}

				outputXml.println("<valid>" + valid + "</valid>");
				outputXml.println("</File>");

			}

		}

		outputXml.println("</FileAnalysis>");

		br.close();

		Collections.sort(errormessages);

		int i;

		ArrayList<String> originerrors = new ArrayList<String>();
		for (i = 0; i < errormessages.size(); i++) {
			originerrors.add(errormessages.get(i));
		}
		// get rid of redundant entries
		i = 0;
		while (i < errormessages.size() - 1) {
			if (errormessages.get(i).equals(errormessages.get(i + 1))) {
				errormessages.remove(i);
			} else {
				i++;
			}
		}

		int j = 0;
		int temp1;
		
		outputXml.println("<AllMessages>");
		
		for (i = 0; i < errormessages.size(); i++) {
			temp1 = 0;
			for (j = 0; j < originerrors.size(); j++) {
				if (errormessages.get(i).equals(originerrors.get(j))) {
					temp1++;
				}
			}
			outputXml.println("<Message>");
			outputXml.println("<MessageText>" + errormessages.get(i) + "</MessageText>");
			outputXml.println("<Occurance>" + temp1 + "</Occurance>");
			outputXml.println("</Message>");
		}
		
		outputXml.println("</AllMessages>");

		outputXml.println("<Summary>");

		outputXml.println("<Lines>" + lines.size() + "</Lines>");
		outputXml.println("<analysedTiff>" + analysedTiff + "</analysedTiff>");
		outputXml.println("<corruptTiff>" + corruptImages + "</corruptTiff>");
		outputXml.println("<DifferentMessages>" + errormessages.size() + "</DifferentMessages>");

		outputXml.println("</Summary>");

		outputXml.println("</imageMagickSummary>");

		outputXml.close();

		output.XslStyleSheets.imageMagickXsl(outputFolder + "//" + "imageMagick" + ".xsl");

	}

	private static String getFilePath(String string) {
		String[] parts = string.split(Pattern.quote(" TIFF "));
		return parts[0];
	}
}
