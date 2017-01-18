package input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

public class changeExtension {

	public static void main(String args[]) throws IOException {

		String examinedFolder = utilities.BrowserDialogs.chooseFolder();

		if (examinedFolder != null) {

			ArrayList<File> files = utilities.ListsFiles.getPaths(new File(examinedFolder), new ArrayList<File>());
			if (files != null) {

				for (int i = 0; i < files.size(); i++) {
					String newExtension = FilenameUtils.getExtension(files.get(i).toString()).toLowerCase();
					
					String newFileName = utilities.fileStringUtilities.getFileName((files.get(i)));
					
					System.out.println(newFileName);
					
					files.get(i).renameTo(new File(newFileName + "." + newExtension));

				}
			}
		}
	}
}
