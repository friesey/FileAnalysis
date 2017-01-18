package input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Libtiffanalysis {

	static String SEPARATOR = ";";
	static String filename;

	public static void main(String args[]) throws IOException {

		String examinedFile = "C://Users//Friese Yvonne//FileSamples//libTiff_cleaned.txt";
		PrintWriter outputCsv = new PrintWriter(new FileWriter("C://Users//Friese Yvonne//FileSamples//libTiff.csv"));

		FileReader input = new FileReader(examinedFile);
		BufferedReader br = new BufferedReader(input);
		String str;

		ArrayList<String> lines;
		lines = new ArrayList<String>();

		while ((str = br.readLine()) != null) {
			lines.add(str);
		}

		outputCsv.println("File" + SEPARATOR + "Findings");

		int len = 45;
		String findings = "valid";
		int test= 0;
		boolean findingsbool = true;

		for (int i = 0; i + 1 < lines.size(); i++) {

			if (!(lines.get(i).startsWith("Warning"))) {

				filename = lines.get(i);
				test++;
				System.out.println(test);

				if (lines.get(i + 1).startsWith("Warning")) {
					findings = "invalid";
					findingsbool = false;
				}

				else {
					findings = "valid";
					findingsbool = true;
				}

				if (findingsbool == true) {
					outputCsv.println(filename + SEPARATOR + findings + SEPARATOR);
			}

		
		}
		}

		outputCsv.close();
	}
}
