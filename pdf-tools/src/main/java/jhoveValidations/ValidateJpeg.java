package jhoveValidations;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.OutputHandler;
import edu.harvard.hul.ois.jhove.handler.XmlHandler;
import edu.harvard.hul.ois.jhove.module.JpegModule;

public class ValidateJpeg {

	static App jhoveapp;
	static JhoveBase jhoveBase;
	static Module jpegmodule;
	static OutputHandler handler;
	public static String folder;

	public static void JhoveJpegValidator() {

		String pathwriter;
		
		try {
			folder = JhoveGuiStarterDialog.jhoveExaminationFolder;
			if (folder != null) {

				JFrame f = new JFrame();
				JButton but = new JButton("... Program is running ... ");
				f.add(but, BorderLayout.PAGE_END);
				f.pack();
				f.setVisible(true);

				JhoveBase jb = new JhoveBase();

				String configFilePath = JhoveBase.getConfigFileFromProperties();
				jb.init(configFilePath, null);

				jb.setEncoding("UTF-8");// UTF-8 does not calculate checksums,
										// which saves time
				jb.setBufferSize(131072);
				jb.setChecksumFlag(false);
				jb.setShowRawFlag(false);
				jb.setSignatureFlag(false);

				String appName = "Customized JHOVE";
				String version = "1.0";

				int[] date = utilities.GenericUtilities.getDate();
				String usage = "Call JHOVE via own Java";
				String rights = "Copyright nestor Format Working Group";
				App app = new App(appName, version, date, usage, rights);

				Module module = new JpegModule();

				OutputHandler handler = new XmlHandler();
				ArrayList<File> files = utilities.ListsFiles.getPaths(new File(folder), new ArrayList<File>());

				pathwriter = (folder + "//" + "JhoveExamination.xml");

				PrintWriter writer = new PrintWriter(new FileWriter(pathwriter));
				handler.setWriter(writer);
				handler.setBase(jb);
				module.init("");
				module.setDefaultParams(new ArrayList<String>());

				String xmlVersion = "xml version='1.0'";
				String xmlEncoding = "encoding='ISO-8859-1'";

				writer.println("<?" + xmlVersion + " " + xmlEncoding + "?>");
				writer.println("<JhoveFindings>");

				// To handle one file after the other
				for (int i = 0; i < files.size(); i++) {
					if (utilities.GenericFileAnalysis.testFileHeaderJpeg(files.get(i)) == true) {
						writer.println("<item>");
						if (files.get(i).toString().contains("&")) {
							String substitute = utilities.GenericUtilities.normaliseToUtf8(files.get(i).toString());
							writer.println("<filename>" + substitute + "</filename>");
						} else {
							writer.println("<filename>" + files.get(i).toString() + "</filename>");
						}
						jb.process(app, module, handler, files.get(i).toString());
						writer.println("</item>");
					}
				}
				writer.println("</JhoveFindings>");
				writer.close();
				output.XmlParserJhove.parseXmlFile(pathwriter);

				f.dispose();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e, "error message", JOptionPane.ERROR_MESSAGE);

		}
	}
}
