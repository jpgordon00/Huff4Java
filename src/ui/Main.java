package ui;

import huffman.Huffman;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.ViewController;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Jacob Gordon
 * @version 1.0
 * @date 6/5/19
 **/
public class Main extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/main.fxml"));
        MainController mc = new MainController();
        mc.primaryStage = primaryStage;
        mc.fc = new FileChooser();
        mc.dc = new DirectoryChooser();
        mc.main = this;
        loader.setController(mc);
        Parent root = loader.load();
        primaryStage.setTitle("Huff4Java - File Encoder/Decoder");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        //final init step
        primaryStage.show();
    }

    /**
     * Invoked to open our secondary 'view' window given a window title and
     * text to fill our view's textArea.
     * @param title of the new window.
     * @param str string to fill our new view's textArea.
     * @throws IOException
     */
    public void openViewWindow(String title, String str) throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/view.fxml"));
                ViewController vc = new ViewController();
                vc.text = str;
                loader.setController(vc);
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage primaryStage = new Stage();
              //  primaryStage
                primaryStage.setTitle(title);
                primaryStage.setScene(new Scene(root, 600, 400));
                primaryStage.setResizable(true);
                primaryStage.show();
    }

    /**
     * Opens the web page given the operation is suported.
     * @param uri to send to the web (taken from a URL)
     * @return true if succesfully opened the page.
     */
    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Opens the web page given the operation is suported.
     * @param url to send to the web (constructor takes string)
     * @return true if succesfully opened the page.
     */
    public static boolean openWebpage(URL url) {
        try {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(url.toURI());
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
