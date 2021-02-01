package ui;

import huffman.Huffman;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author Jacob Gordon
 * @version 1.0
 * @date 6/5/19
 **/
public class MainController {

    /**
     * URL for this project's github src.
     */
    private final String srcURL = "https://github.com/SirTrashyton/Huff4Java";

    /**
     * URL for how Huffman works.
     */
    private final String infoURL = "https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/";

    /**
     * Selected file to compress/decompress.
     */
    private File file;

    /**
     * Chosen directory of out file from directory chooser.
     */
    private String dir;

    /**
     * Name of the new file.
     */
    private String fileNameOut;

    /**
     * Name of our selected file (given from file)
     */
    private String fileName;

    /**
     * Booleans to signify readiness of certain text fields and elements.
     */
    private boolean readyDir = false, readyName = false, readyFile = false;

    /**
     * Booleans to signify readiness of compression or decompression, which
     * depends on the selected file's suffix.
     */
    private boolean readyCompress = false, readyDecompress = false;

    /**
     * Suffix assigned by the choice-box selector.
     * Could be blank.
     */
    private String suffix = "";

    /**
     * Instance of our main class given during 'start'.
     */
    public Main main;

    /**
     * Instance of our primary stage given during 'start'.
     */
    public Stage primaryStage = null;

    /**
     * File and Directory chooser, created during 'start'.
     */
    public FileChooser fc = null;
    public DirectoryChooser dc;

    /**
     * ui.Main AnchorPane for our scene (stretching to both sides)
     */
    @FXML
    private AnchorPane mainPane;

    /**
     * Label above our huffman button that describes waiting for a file before
     * the button can be clicked.
     */
    @FXML
    private Label waitingLabel;

    /**
     * Label above our huffman button that describes how our file will be compressed
     * when the button is clicked.
     */
    @FXML
    private Label compressLabel;

    /**
     * Label above our huffman button that describes how our file will be decompressed
     * when the button is clicked.
     */
    @FXML
    private Label decompressLabel;

    /**
     * Button that will preform huffman on the given file, output dir, and output file name.
     * Will be DISABLED unless: chooseField, outDirField, and fileNameOut SET
     */
    @FXML
    private Button huffButton;

    /**
     * TextField followed by the folder button.
     */
    @FXML
    private TextField chooseField;

    /**
     * Button to read the given file as binary code.
     */
    @FXML
    private Button viewButton;

    /**
     * Our button's to act as clickable folder icon.
     */
    @FXML
    private Button folder1, folder2;

    /**
     * TextField for displaying our output directory path.
     */
    @FXML
    private TextField outDirField;

    /**
     * TextField for typign and displaying our file name.
     * When this field gains focus and has text, erase current text.
     */
    @FXML
    private TextField outNameField;

    /**
     * Button for opening the src on github.
     */
    @FXML
    private Button buttonSrc;

    /**
     * Button for opening info.fxml window.
     */
    @FXML
    private Button buttonInfo;

    /**
     * TextArea on right side of pane to display processing info.
     */
    @FXML
    private TextArea textAreaOut;

    /**
     * MenuButton to choose the file type.
     */
    @FXML
    private ChoiceBox choiceSuffix;

    /**
     * This function is invoked upon the creation of this application.
     */
    @FXML
    private void initialize() {
        //lets have our manePane gain focus when its clicked:
        mainPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainPane.requestFocus();
            }
        });
        mainPane.requestFocus();

        //make sure these labels are not visible yet
        compressLabel.setVisible(false);
        decompressLabel.setVisible(false);

        //setup the huffman button to compress if 'readyCompress'
        huffButton.setDisable(true);
        huffButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (checkReady()) {
                    //compress the file in seperate thread
                    //reset text fields
                    Thread thread = new Thread() {
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            try {
                                if (readyCompress) {
                                    String out = dir + "/" + fileNameOut;
                                    Huffman.compress(file.getAbsolutePath(), out);
                                    long l = file.length();
                                    long l2 = new File(new StringBuilder(out).append(".huff").toString()).length();
                                    long p = l / l2;
                                    printLog(sb, out, l, l2, p, "COMPRESSION");

                                } else if (readyDecompress) {
                                    String out = dir + "/" + fileNameOut;
                                    if (!suffix.equals("")) {
                                        out = new StringBuilder(out).append(suffix).toString();
                                        System.out.println("SUFFIX: " + suffix);
                                    }
                                    String outFile = Huffman.decompress(file.getAbsolutePath(), out);
                                    long l = file.length();
                                    long l2 = new File(outFile).length();
                                    long p = l2 / l;
                                    printLog(sb, out, l, l2, p, "DECOMPRESSION");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            textAreaOut.setText(sb.toString());
                        }
                    };
                    thread.start();
                }
            }
        });

        //set our folder graphic for all the folder buttons
        folder1.setGraphic((ImageView) new ImageView("/img/folder_blue.png"));
        //have it open file browser when clicked
        folder1.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                chooseField.clear();
                file = fc.showOpenDialog(primaryStage);
                if (file != null) {
                    chooseField.setText(file.getAbsolutePath());
                    //update the view file button
                    viewButton.setDisable(false);
                    readyFile = true;
                    fileName = file.getName();
                    if (fileName.endsWith(".huff")) {
                        readyCompress = false;
                        readyDecompress = true;
                    } else {
                        readyCompress = true;
                        readyDecompress = false;
                    }
                } else {
                    file = null;
                    chooseField.setText("...");
                    //update the view file button
                    viewButton.setDisable(true);
                    readyFile = false;
                    fileName = "";
                    readyCompress = false;
                    readyDecompress = false;
                }
                checkReady();
            }
        });

        //set view button to open the view window
        //if ready...
        viewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    String str = Huffman.getFileAsBinary(file);
                    main.openViewWindow(fileName, str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //set our folder graphic for all the folder buttons
        folder2.setGraphic((ImageView) new ImageView("/img/folder_blue.png"));
        //have it open file browser when clicked
        folder2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            //get the directory
            @Override
            public void handle(MouseEvent event) {
                outDirField.clear();
                File f = dc.showDialog(primaryStage);
                if (f != null) {
                    dir = f.getAbsolutePath();
                    outDirField.setText(dir);
                    readyDir = true;
                } else {
                    outDirField.setText("...");
                    readyDir = false;
                }
                checkReady();
            }
        });

        //create a focus listener for our 'outNameField'
        //set text to "" whenever focused.
        outNameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                //out of focus...
                if (!newPropertyValue) {
                    fileNameOut = outNameField.getText();
                    //check if a blank string was submitted...
                    if (fileNameOut.isEmpty()) {
                        outNameField.setText("enter file name");
                        readyName = false;
                        fileNameOut = "";
                    } else {
                        //dont accept  strings with spaces,
                        //or strings that are not letters or digits.
                        boolean valid = true;
                        //remove spaces
                        fileNameOut = fileNameOut.replaceAll(" ", "");
                        if (!fileNameOut.chars().allMatch(Character::isLetterOrDigit)) valid = false;
                        if (valid) {
                            readyName = true;
                        } else {
                            outNameField.setText("enter file name");
                            readyName = false;
                            fileNameOut = "";
                        }
                    }

                    //in focus...
                } else {
                    outNameField.setText("");
                    readyName = false;
                    fileNameOut = "";
                }
                checkReady();
            }
        });
        //allow enter for data submission
        outNameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)) {
                    mainPane.requestFocus();
                }
            }
        });

        //open src website when prompted
        buttonSrc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(new URI(srcURL));
                    } else {
                        throw new NullPointerException();
                    }
                } catch (Exception e) {
                    buttonSrc.setDisable(true);
                    e.printStackTrace();
                }
            }
        });
        //open info website when prompted
        buttonInfo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(new URI(infoURL));
                    } else {
                        throw new NullPointerException();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //make the choiceBox disabled by default.
        choiceSuffix.setDisable(true);
        items = FXCollections.observableArrayList("none", ".bmp", ".rtf", ".txt");
        choiceSuffix.setItems(items);
        choiceSuffix.setOnAction(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                 suffix = (String) choiceSuffix.getValue();
            }
        });
    }

    ObservableList<String> items;

    /**
     * Invoked to reset the states of various text fields for
     * this application.
     */
    private void resetTextFields() {
        //set fields
        chooseField.setText("...");
        outDirField.setText("...");
        outNameField.setText("enter file name");
        //set booleans
        readyName = false;
        readyDir = false;
        readyFile = false;
        readyCompress = false;
        readyDecompress = false;
        file = null;
        fileNameOut = "";
        fileName = "";
        suffix = "";
    }

    /**
     * Checks the various booleans to determine what textFields are ready
     * and will update labels and buttons accordingly.
     * @return true if we are ready, false otherwise.
     */
    public boolean checkReady() {
        //show menu only if decompression may happen
        if (readyName && readyDecompress) {
            choiceSuffix.setDisable(false);
        } else {
            choiceSuffix.setDisable(true);
            suffix = "";
        }
        if (readyDir && readyName && readyFile) {
            huffButton.setDisable(false);
            //set labels
            waitingLabel.setVisible(false);
            if (readyCompress) {
                compressLabel.setVisible(true);
                decompressLabel.setVisible(false);
            } else if (readyDecompress){
                compressLabel.setVisible(false);
                decompressLabel.setVisible(true);
            } else {
                waitingLabel.setVisible(false);
            }
            return true;
        }
        huffButton.setDisable(true);
        //set labels
        waitingLabel.setVisible(true);
        compressLabel.setVisible(false);
        decompressLabel.setVisible(false);
        return false;
    }

    /**
     * Appends the help text to the given string builder.
     * @param sb stringbuilder to add text to.
     */
    public void printLog(StringBuilder sb, String out, long l, long l2, long p, String title) {
        sb.append("---HUFFMAN " + title + "---\n");
        sb.append("File In: " + file.getAbsolutePath() + "\n");
        sb.append("File In Length: " + l + "\n");
        sb.append("File Out: " + out + "\n");
        sb.append("File Out Length: " + l2 + "\n");
        sb.append("Compression Rate (l1/l2): " + p + "\n");
        if (p <= 0) {
            sb.append("TIP: Most files are already compressed to their minimum size.\n");
            sb.append("Some non-compressed file types: .rtf, .bmp");
        }
    }
}
