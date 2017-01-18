package preservingfiles;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class countFiles {

	public static void main(String args[]) throws Exception {

		String folder;

		JOptionPane.showMessageDialog(null, "Please choose the folder with files to count.", "Files count",
				JOptionPane.QUESTION_MESSAGE);
		folder = utilities.BrowserDialogs.chooseFolder();

		ArrayList<File> files = utilities.ListsFiles.getPaths(new File(folder), new ArrayList<File>());

		int findings = files.size();

		ArrayList<Long> fileSizePdf;
		fileSizePdf = new ArrayList<Long>();
		
		int jpeg = 0;
		int pdf = 0;
		int tiff = 0;

		for (int i = 0; i < files.size(); i++) {

			long size = files.get(i).length();
			size = size / 1024;

			String extension = (utilities.fileStringUtilities.getExtension(files.get(i)));
			
			extension = extension.toLowerCase();

			if (extension.equals("pdf")) {
				fileSizePdf.add(size);
			//	System.out.println(size + " KB");
				pdf++;
			}
			
			if (extension.equals("jpg") || extension.equals("jpeg")){
				jpeg++;
			}
			
			if (extension.equals("tif")) {	
			tiff++;
			}
		}
		System.out.println(jpeg + " JPEG Files");
		System.out.println(pdf + " PDF Files");
		System.out.println(tiff + " TIFF Files");
		
		long sizePdf=0;
		
		for (int i = 0; i < fileSizePdf.size(); i++) {
			sizePdf = sizePdf + fileSizePdf.get(i);
		//	System.out.println(sizePdf + " KB");		
					}
		
	System.out.println(sizePdf + " KB");	
	if (pdf != 0){
		System.out.println("Durchschnittliche Größe PDF Datei: " + sizePdf / pdf + " KB");
	}

		// TODO: alle Elemente der ArrayList addieren und durch Anzahl der
		// Elemente teilen

		JOptionPane.showMessageDialog(null, findings, "Files", JOptionPane.INFORMATION_MESSAGE);

		System.out.println(findings);

	}
}
