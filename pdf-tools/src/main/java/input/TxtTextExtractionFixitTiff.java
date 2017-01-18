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

public class TxtTextExtractionFixitTiff {

	public static String examinedFolder;
	static PrintWriter csvsummary;
	static String SEPARATOR = ";";

	public static void main(String args[]) throws IOException {

		examinedFolder = "C://Users//Friese Yvonne//FileSamples//Findings Google Image Test Suite TIFF//tiflog_checkit_tiff";

		String outputCsv = examinedFolder + "//" + "fixitTiffFindings" + ".csv";
		csvsummary = new PrintWriter(new FileWriter(outputCsv));

		ArrayList<File> files = utilities.ListsFiles.getPaths(new File(examinedFolder), new ArrayList<File>());

		if (files != null) {

			int failed = 0;
			int successful = 0;
			int fontproblems = 0;

			csvsummary.println("Filename" + SEPARATOR + "Fixit TIFF Findings" + SEPARATOR + "Number of Errors");

			for (int i = 0; i < files.size(); i++) {

				String filename = getTiffFileName(files.get(i));
			
				List<String> linesLog = new ArrayList<String>();

				FileReader input = new FileReader(files.get(i).getPath());
				BufferedReader buffRd = new BufferedReader(input);
				String str;
				StringBuffer buff = new StringBuffer();

				String errornumber = null;

				while ((str = buffRd.readLine()) != null) {
					linesLog.add(str);
				}

				buffRd.close();

				for (int j = 0; j < linesLog.size(); j++) {
					buff.append(linesLog.get(j) + "\n");

					if (linesLog.get(j).contains("Found")) {
						errornumber = linesLog.get(j);
					}
				}

				String temp = new String();
				temp = buff.toString();

				if (temp.contains("TIFF Offset read error2")) {
					csvsummary.println(filename + SEPARATOR + "Offset read error" + SEPARATOR + "cannot read offset adress");

				}

				if (temp.contains("No, the given tif is not valid")) {
					csvsummary.println(filename + SEPARATOR + "TIFF invalid" + SEPARATOR + errornumber);

				}

				if (temp.contains("TIFF Header read error3")) {
					csvsummary.println(filename + SEPARATOR + "Header read error" + SEPARATOR + "cannot read Header information");


				}

				else {
					// nothing happens
				}

			}

			csvsummary.close();

		}
	}

	private static String getTiffFileName(File file) {
		String filename;
		String[] parts = file.toString().split(Pattern.quote("\\"));
		filename = parts[parts.length - 1]; // filename including extension

		String[] segs = filename.split(Pattern.quote("."));
		filename = segs[0];

		return filename;
	}
}
