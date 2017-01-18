package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class KostValBadPeggySearch {

	public static String file;
	static String searchedString;
	static PrintWriter outputfile;
	static int stringfound;
	static String extension;

	// implemented non-context-sensitive for methods
	// searchforStringinSimpleFiles and searchforStringinPdfFiles

	public static void main(String args[]) throws IOException {

		int invalid = 0;
		int soi = 0;
		int ype = 0;
		int header = 0;
		int twosoi = 0;
		int premature = 0;
		int extraneous = 0;
		int eoi = 0;
		int progressive = 0;
		int sof = 0;
		int huffman = 0;
		int huffmantable = 0;
		int precision = 0;
		int jfif = 0;
		int markerlength = 0;
		int image = 0;
		int sos = 0;
		int table = 0;
		int unsup = 0;
		int empty = 0;
		int sampling = 0;
		int jfifnot = 0;
		int missingSOS = 0;
		int badHuffman = 0;
		int bogusSampling = 0;
		int rst0 = 0;
		int toomany = 0;
		int legal = 0;
		int bogusDQT = 0;

		file = "C://Users//Friese Yvonne//Desktop//Kost//KOST.txt";

		BufferedReader txtreader = new BufferedReader(new FileReader(file));
		String line;

		while (null != (line = txtreader.readLine())) {
			if (line.contains("BadPeggy validation has failed")) {
				invalid++;
			}

			else if (line.contains("Missing SOI between two EOI thumbnail markers.")) {
				soi++;
			}

			else if (line.contains("ype.")) {
				ype++;
			}

			else if (line.contains("The file is not a JPEG (header).")) {
				header++;
			}

			else if (line.contains("two SOI markers.")) {
				twosoi++;
			}

			else if (line.contains("premature end of data segment.")) {
				premature++;

			} else if (line.contains("extraneous bytes before marker")) {
				extraneous++;
			}

			else if (line.contains("Missing EOI marker")) {
				eoi++;
			}

			else if (line.contains("Invalid progressive parameters")) {
				progressive++;
			}

			else if (line.contains("two SOF markers")) {
				sof++;
			}

			else if (line.contains("Bogus Huffman table definition")) {
				huffman++;
			}

			else if ((line.contains("Huffman table")) && (line.contains("was not defined"))) {
				huffmantable++;
			}

			else if (line.contains("Unsupported JPEG data precision")) {
				precision++;

			} else if (line.contains("Warning: unknown JFIF revision number")) {
				jfif++;
			}

			else if (line.contains("Bogus marker length")) {
				markerlength++;
			}

			else if (line.contains("Image Format Error.")) {
				image++;
			}

			else if (line.contains("SOS before SOF")) {
				sos++;
			}

			else if ((line.contains("Quantization table")) && (line.contains("was not defined"))) {
				table++;
			}

			else if (line.contains("Unsupported JPEG process: ")) {
				unsup++;
			}

			else if (line.contains("Empty JPEG image")) {
				empty++;
			}

			else if (line.contains("Sampling factors too large for interleaved scan.")) {
				sampling++;
			}

			else if (line.contains("JFIF not permitted in stream metadata")) {
				jfifnot++;
			}

			else if (line.contains("missing SOS marker")) {
				missingSOS++;

			} else if (line.contains("bad Huffman code")) {
				badHuffman++;
			}

			else if (line.contains("Bogus sampling factors")) {
				bogusSampling++;
			}

			else if (line.contains("instead of RST0")) {
				rst0++;
			}

			else if (line.contains("Too many color components")) {
				toomany++;
			}

			else if (line.contains("there are legal restrictions on arithmetic coding")) {
				legal++;
			}

			else if (line.contains("Bogus DQT index")) {
				bogusDQT++;
			}

			else if (line.contains("Friese Yvonne")) {
			}

			else if (line.length() < 1) {
			}

			else {
				System.out.println(line);
			}

		}
		System.out.println(invalid + "Files");

		System.out.println(soi + " invalid file structure	Missing SOI between two EOI thumbnail markers");
		System.out.println(ype + " ype");
		System.out.println(header + " recognition and BadPeggy	The file is not a JPEG (header).");
		System.out.println(twosoi + " invalid file structure	two SOI markers.");
		System.out.println(premature + "corrupt data	premature end of data segment");
		System.out.println(extraneous + " corrupt data	16 extraneous bytes before marker 0xe0");
		System.out.println(eoi + " corrupt data	Truncated File - Missing EOI marker");
		System.out.println(progressive + "other problems	Invalid progressive parameters Ss=227 Se=63 Ah=1 Al=0");
		System.out.println(sof + " invalid file structure	two SOF markers.");
		System.out.println(huffman + " other problems	Bogus Huffman table definition");
		System.out.println(huffmantable + " invalid file structure	Huffman table 0x00 was not defined");
		System.out.println(precision + " other problems	Unsupported JPEG data precision 9");
		System.out.println(jfif + " other problems	Warning: unknown JFIF revision number 148.195.");
		System.out.println(markerlength + " other problems	Bogus marker length");
		System.out.println(image + " other problems	Image Format Error");
		System.out.println(sos + " invalid file structure	SOS before SOF.");
		System.out.println(table + " other problems	Quantization table 0x00 was not defined.");
		System.out.println(unsup + " other problems	Unsupported JPEG process: SOF type 0xc3.");
		System.out.println(empty + " invalid file structure	Empty JPEG image (DNL not supported).");
		System.out.println(sampling + " other problems	Sampling factors too large for interleaved scan.");
		System.out.println(jfifnot + " other problems	JFIF not permitted in stream metadata.");
		System.out.println(missingSOS + " invalid file structure	missing SOS marker.");
		System.out.println(badHuffman + " corrupt data	bad Huffman code.");
		System.out.println(bogusSampling + "other problems	Bogus sampling factors.");
		System.out.println(rst0 + "corrupt data	found marker 0xf7 instead of RST0.");
		System.out.println(toomany + " other problems	Too many color components: 17, max 10.");
		System.out.println(legal + " other problems	Sorry, there are legal restrictions on arithmetic coding.");
		System.out.println(bogusDQT + "other problems	Bogus DQT index 10.");

		txtreader.close();
	}
}
