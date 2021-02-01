package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * @author Jacob Gordon
 * @version 1.0
 * @date 6/8/19
 **/
public class ViewController {

    /**
     * TextArea to display our binary for the file.
     */
    @FXML
    private TextArea textArea;

    /**
     * Text to represent the binary strings.
     */
    public String text;

    /**
     * This function is called upon this controller's initiation.
     * Sets up the given TextFields.
     */
    @FXML
    private void initialize() {
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setWrapText(true);
    }
}
