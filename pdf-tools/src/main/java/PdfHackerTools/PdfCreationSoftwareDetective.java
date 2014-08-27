package PdfHackerTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.pdf.PdfReader;

public class PdfCreationSoftwareDetective {

	public static void main(String args[]) throws IOException {

		String ExaminedFolder;
		String extension;

		ArrayList<String> ProducerType;

		PdfReader reader;
		PrintWriter outputfile;

		try {
			ExaminedFolder = PdfUtilities.chooseFolder();

			if (ExaminedFolder != null) {
				ProducerType = new ArrayList<String>();
				ArrayList<File> files = PdfUtilities.getPaths(new File(
						ExaminedFolder), new ArrayList<File>());

				outputfile = new PrintWriter(new FileWriter(ExaminedFolder
						+ "//" + "CreationSoftwareDetective.txt"));
				for (int i = 0; i < files.size(); i++) {
					if (files.get(i) != null) { // maybe not necessary
						// prints out only files and not the subdirectories
						// as
						// well
						if (!files.get(i).isDirectory()) {
							// null pointer exception
							extension = Files.probeContentType(files.get(i)
									.toPath());
							if (extension != null) {
								if (extension.equals("application/pdf")) {
									if (PdfUtilities.testPdfOk(files.get(i))) {
										System.out.println(files.get(i));
										try {
											PDDocument testfile = PDDocument
													.load(files.get(i));

											reader = new PdfReader(files.get(i)
													.toString());
											Map info = reader.getInfo();
											if (info.get("Producer") != null) {
												System.out.println(info.get(
														"Producer").toString());
												ProducerType.add(info.get(
														"Producer").toString());
											}
											reader.close();
											testfile.close();
										} catch (IOException e) {
											outputfile
													.println(files.get(i)
															+ " is so damaged it cannot be parsed: "
															+ e);

										}
									}
								}
							}
						}
					}
				}

				Collections.sort(ProducerType);
				int i;

				ArrayList<String> AllProducerTypes = new ArrayList<String>();

				for (i = 0; i < ProducerType.size(); i++) { // There might
															// be a
					// pre-defined
					// function for this
					AllProducerTypes.add(ProducerType.get(i));
				}
				// get rid of redundant entries
				for (i = 0; i < ProducerType.size() - 1;) {
					if (ProducerType.get(i).equals(ProducerType.get(i + 1))) {
						ProducerType.remove(i);
					} else {
						i++;
					}
				}

				// how often does each Producer occur?
				int j = 0;
				int temp;

				for (i = 0; i < ProducerType.size(); i++) {
					temp = 0;
					for (j = 0; j < AllProducerTypes.size(); j++) {
						if (ProducerType.get(i).equals(AllProducerTypes.get(j))) {
							temp++;
						}
					}
					System.out.println((i + 1) + ": " + temp + " x "
							+ ProducerType.get(i));

					outputfile.println((i + 1) + ": " + temp + " x "
							+ ProducerType.get(i));
				}

				// in case no PDF-files are found, the outputfile comes
				// out empty.
				// Is this intended?
				outputfile.close();
			}
		} catch (FileNotFoundException e) {
		}
	}
}