package filetools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import filetools.pdf.PdfAnalysis;

public class MoveFiles {

	public static void main(String args[]) throws IOException {

		JOptionPane.showMessageDialog(null, "Please choose the folder with PDF files to move.", "Move PDFs",
				JOptionPane.QUESTION_MESSAGE);
		String fileFolder = utilities.BrowserDialogs.chooseFolder();

		// Liste mit Filenamen erstellen

		/*
		 * JOptionPane.showMessageDialog(null,
		 * "Please choose the folder with PDF file with String Informations",
		 * "String Information", JOptionPane.QUESTION_MESSAGE); String pdfFile =
		 * utilities.BrowserDialogs.chooseFile();
		 */

		String pdfFile = "C://Users//Friese Yvonne//Desktop//Callas_Ganz//test//HtmlTextEctraction.pdf";

		String[] linesPdf = PdfAnalysis.extractsPdfLines(pdfFile);
		List<String> nichtKonvertierbar = new ArrayList<String>();

		int lenPdf = linesPdf.length;

		for (int j = 0; j < lenPdf; j++) {

			if ((linesPdf[j]).contains("failed")) {

				String[] parts = linesPdf[j].split(Pattern.quote(" "));

				// System.out.println("Hinzugefuegt zur Array List:" +
				// parts[1]);
				nichtKonvertierbar.add(parts[1]);
			}
		}

		if (fileFolder != null) {

			ArrayList<File> files = utilities.ListsFiles.getPaths(new File(fileFolder), new ArrayList<File>());
			if (files != null) {

				for (int j = 0; j < nichtKonvertierbar.size(); j++) {
					System.out.println("Teil der Array Liste: " + nichtKonvertierbar.get(j));
				}

				for (int i = 0; i < files.size(); i++) {

					String filename = utilities.fileStringUtilities.getFileName(files.get(i));

					// System.out.println("PDF im Ordner:" +
					// files.get(i).toString());
					// System.out.println("Filename" + filename);

					if (nichtKonvertierbar.contains(filename)) {

						System.out.println(filename);

						Path copySourcePath = Paths.get(
								"C://Users//Friese Yvonne//Desktop//Callas_Ganz//Originale" + "//" + filename + ".pdf");
						Path copyTargetPath = Paths
								.get("C://Users//Friese Yvonne//Desktop//Callas_Ganz//NichtKonvertiert_Korpus" + "//"
										+ filename + ".pdf");
						// if file already exists in Target, an error is thrown
						Files.copy(copySourcePath, copyTargetPath);

					}

					else {
						// nothing happens
					}

				}
			}
		}
	}
}
