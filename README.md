## CSV Parser GUI

This is a simple bonus GUI implementation with Java Swing for the csv parser application that allows the user to select a CSV file, which is then parsed and converted into either CSV or XML files based on the contents of the original file.

### Requirements

- The user must be able to select a CSV file from their local filesystem.
- The application must parse the CSV file and separate the data by "buyer".
- For each "buyer", a separate CSV file or XML document must be created that includes all rows containing their data.
- The CSV and XML files must be saved to the user's local filesystem.
- The application must be able to handle CSV files that contain illegal XML characters.
- The application must have a graphical user interface (GUI) that displays the name of the selected file and allows the user to choose whether to output the data as CSV or XML.
- The GUI must also display a message indicating whether the operation was successful or not.

### Implementation

The CSVParserGUI class contains the main method that launches the application. The GUI is implemented using Swing components and consists of a JFrame with a JLabel to display the selected file name, two JButtons to output the data as either CSV or XML, and a JLabel to display the status message.

The outputCSV and outputXML methods handle the parsing and conversion of the CSV file. The CSV file is read line-by-line and split by "buyer". A Map is used to keep track of which rows belong to which "buyer". For each "buyer", a separate CSV file or XML document is created that includes all rows containing their data. The CSV and XML files are saved to the user's local filesystem.

The escapeXML and escapeIllegalXmlChars methods are used to handle CSV files that contain illegal XML characters. These methods replace illegal XML characters with their corresponding XML entities.

### Usage

To use the CSV Parser GUI, simply run the CSVParserGUI class. The user can then select a CSV file by clicking the "Choose File" button. Once a file is selected, the name of the file will be displayed in the "File Name" JLabel and the "CSV output" and "XML output" buttons will be enabled. Clicking one of these buttons will parse the CSV file and output the data as either CSV or XML files. The status of the operation will be displayed in the "Message" JLabel.