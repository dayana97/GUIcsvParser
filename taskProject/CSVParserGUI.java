package taskProject;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CSVParserGUI {

	private static String csvFile;
	
	private JFrame frameCSV;
	private JLabel lbl;
	private static JLabel lblMessage;
	private JButton btnCSV;
	private JButton btnXML;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				CSVParserGUI window = new CSVParserGUI();
				window.frameCSV.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CSVParserGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frameCSV = new JFrame();
		frameCSV.setTitle("CSV Parser");
		frameCSV.setBounds(100, 100, 450, 300);
		frameCSV.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameCSV.getContentPane().setLayout(null);
		frameCSV.setSize(230,230);
		frameCSV.setResizable(false);
		frameCSV.setLocationRelativeTo(null);
		
		lbl = new JLabel("File Name");
		lbl.setBounds(10, 40, 217, 23);
		frameCSV.getContentPane().add(lbl);
		

		lblMessage = new JLabel("Message");
		lblMessage.setBounds(10, 164, 217, 23);
		frameCSV.getContentPane().add(lblMessage);
		
		JButton btnChoose = new JButton("Choose File");
		btnChoose.addActionListener(arg0 -> {
			JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

			//add restrictions on shown file types
			FileNameExtensionFilter restrict = new FileNameExtensionFilter(".csv", "csv");
			fileChooser.addChoosableFileFilter(restrict);

			// invoke the showsOpenDialog function to show the save dialog
			int check = fileChooser.showOpenDialog(null);

			// if the user selects a file
			if (check == JFileChooser.APPROVE_OPTION)
			{
				// set the label to the file name
				csvFile = fileChooser.getSelectedFile().getAbsolutePath();
				int point_split = csvFile.lastIndexOf('.');
					if(point_split > 0) {
						if(csvFile.substring(point_split + 1).equals("csv")) {
							lbl.setText(fileChooser.getSelectedFile().getName());
							btnCSV.setEnabled(true);
							btnXML.setEnabled(true);
						}else{
							lbl.setText("Incorrect file path format!");
						}
					}
			}
			// if the user cancelled the operation
			else
				lbl.setText("the user cancelled the operation");
		});
		btnChoose.setBounds(10, 11, 120, 23);
		frameCSV.getContentPane().add(btnChoose);
		
		btnCSV = new JButton("CSV output");
		btnCSV.addActionListener(arg0 -> outputCSV(csvFile));
		btnCSV.setEnabled(false);
		btnCSV.setBounds(10, 100, 142, 23);
		frameCSV.getContentPane().add(btnCSV);
		
		btnXML = new JButton("XML output");
		btnXML.addActionListener(e -> outputXML(csvFile));
		btnXML.setEnabled(false);
		btnXML.setBounds(10, 130, 142, 23);
		frameCSV.getContentPane().add(btnXML);
		
	}
	
	private static void outputCSV(String filePath){
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read the header row
            String headerLine = br.readLine();
            Map<String, PrintWriter> csvWriters = new HashMap<>();
            
            // Read each row and split by 'buyer'
            String row;
            while ((row = br.readLine()) != null) {
                String[] values = row.split(",");
                String buyer = values[0];
                
                // Create a new CSV file or XML document for this buyer if it doesn't exist
                if (!csvWriters.containsKey(buyer)) {
                    String csvFilePath = buyer + ".csv";
                    PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFilePath));
                    csvWriter.println(headerLine);
                    csvWriters.put(buyer, csvWriter);
                }
                
                // Write the row to the CSV file or XML document for this buyer
                String[] outputValues = Arrays.copyOf(values, values.length - 1);
                String outputLine = String.join(",", outputValues);
                
                PrintWriter csvWriter = csvWriters.get(buyer);
                csvWriter.println(outputLine);
            }
            
            // Close the CSV writers and write the XML documents to files
            for (PrintWriter csvWriter : csvWriters.values()) {
                csvWriter.close();
            }
            
            lblMessage.setText("Output CSV files created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private static void outputXML(String filePath){
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			// Read the header row
			String headerLine = br.readLine();
			String[] headers = headerLine.split(",");
			StringBuilder normalizer = new StringBuilder();
			for(int i=0; i<headers[0].length();i++){
				if(!(headers[0].charAt(i)>='a' && headers[0].charAt(i)>='z')){
					normalizer.append(headers[0].charAt(i));
				}
			}
			headers[0] = normalizer.toString();
			Map<String, Document> xmlDocuments = new HashMap<>();

			// Read each row and split by 'buyer'
			String row;
			while ((row = br.readLine()) != null) {
				String[] values = row.split(",");
				String buyer = values[0];

				if (!xmlDocuments.containsKey(buyer)) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document xmlDocument = builder.newDocument();
					Element rootElement = xmlDocument.createElement("buyer");
					xmlDocument.appendChild(rootElement);
					xmlDocuments.put(buyer, xmlDocument);
				}

				// Write the row to XML document for this buyer
				String[] outputValues = Arrays.copyOf(values, values.length - 1);


				Document xmlDoc = xmlDocuments.get(buyer);
				Element invoiceElement = xmlDoc.createElement("invoice");
				for (int i = 0; i < outputValues.length; i++) {
					Element element = xmlDoc.createElement(escapeXML(headers[i]));
					element.appendChild(xmlDoc.createTextNode(outputValues[i]));
					invoiceElement.appendChild(element);
				}
				xmlDoc.getDocumentElement().appendChild(invoiceElement);

			}

			// write the XML documents to files
			for (Map.Entry<String, Document> entry : xmlDocuments.entrySet()) {
				String xmlFilePath = entry.getKey() + ".xml";
				Document xmlDocument = entry.getValue();
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(xmlDocument);
				StreamResult result = new StreamResult(new File(xmlFilePath));
				transformer.transform(source, result);
			}

			lblMessage.setText("Output XML files created successfully.");
		} catch (IOException | ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	private static String escapeXML(String str) {
        String escapedStr = str.replaceAll("&", "&amp;")
                              .replaceAll("<", "&lt;")
                              .replaceAll(">", "&gt;")
                              .replaceAll("\"", "&quot;")
                              /*.replaceAll("ï", "")
                              .replaceAll("»", "")
                              .replaceAll("¿", "")*/;
        return escapeIllegalXmlChars(escapedStr);
    }

    private static String escapeIllegalXmlChars(String str) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
		for (; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 0x9 || c == 0xA || c == 0xD || c >= 0x20 && c <= 0xD7FF || c >= 0xE000 && c <= 0xFFFD) {
                sb.append(c);
                
            }
        }
        return sb.toString();
    }
}
