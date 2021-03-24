/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author LENOVO
 */
public class NotePad extends Application {

    private BorderPane notepadLayout;
    private TextArea notepadBody;
    private MenuBar topMenuBar;
    private Menu file, edit, format, view, help;
    private MenuItem[] fileMenuItems, editMenuItems, formatMenuItems, viewMenuItems, helpMenuItems;
    private Label footer;
    private Scene scene;
    private FileChooser fileChooser;
    private final Desktop desktop = Desktop.getDesktop();

    @Override
    public void init() {
        //File Menu Items: new, open, save as [LINE_SEPARATOR] exit
        fileMenuItems = new MenuItem[]{new MenuItem("New"), new MenuItem("Open"), new MenuItem("Save As"),
            new SeparatorMenuItem(), new MenuItem("Exit")};
        //Edit Menu Items: undo [LINE_SEPARATOR] cut, copy, paste, delete [LINE_SEPARATOR] select all
        editMenuItems = new MenuItem[]{new MenuItem("Undo"), new SeparatorMenuItem(), new MenuItem("Cut"),
            new MenuItem("Copy"), new MenuItem("Paste"), new MenuItem("Delete"),
            new SeparatorMenuItem(), new MenuItem("Select All")};
        //Help Menu Items: About Notepad
        helpMenuItems = new MenuItem[]{new MenuItem("About Notepad")};

        //File Menu
        file = new Menu("File");
        //Adding File Menu items to the File Menu
        file.getItems().addAll(Arrays.asList(fileMenuItems));

        //Edit Menu
        edit = new Menu("Edit");
        //Adding Edit Menu items to the Edit Menu
        edit.getItems().addAll(Arrays.asList(editMenuItems));

        //Format Menu
        format = new Menu("Format");

        //View Menu
        view = new Menu("View");

        //Help Menu
        help = new Menu("Help");
        //Adding Help Menu item to the Help Menu
        help.getItems().add(helpMenuItems[0]);

        //Menu Bar
        topMenuBar = new MenuBar();
        //Adding menus to our menu bar
        topMenuBar.getMenus().addAll(file, edit, format, view, help);

        //Notepad Text Area
        notepadBody = new TextArea();

        //Notepad Footer
        footer = new Label("Copyright© 2020-2021, ITI Intake 41, Shehab El-Deen Alalkamy. All Rights Reserved.");

        //Notepad layout 
        notepadLayout = new BorderPane();
        //setting the notepadLayout top to the top bar menu
        notepadLayout.setTop(topMenuBar);
        //setting the notepadLayout center to the text area field
        notepadLayout.setCenter(notepadBody);
        //setting the notepadLayout bottom to the label field
        notepadLayout.setBottom(footer);

        //Creating object of FileChooser
        fileChooser = new FileChooser();
    }

    @Override
    public void start(Stage primaryStage) {
        //F I L E  M E N U        
        //Assigning shortcuts
        fileMenuItems[0].setAccelerator(KeyCombination.keyCombination("Ctrl+n"));       //new item
        fileMenuItems[1].setAccelerator(KeyCombination.keyCombination("Ctrl+o"));       //open item
        fileMenuItems[2].setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+s")); //save as item

        //Handling Events
        //1-New Item
        fileMenuItems[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (notepadBody.getText().isEmpty() == false) {
                    saveChanges(primaryStage, false);
                }
                /*   
                //create new stage for the new notepad
                Stage newNotepad = new Stage();
                //setting the newNotepad scene to the current scene
                newNotepad.setScene(scene);               
                //newNotepad.initOwner(primaryStage);            This line makes the newNotepad a child of the current primaryStage
                newNotepad.setTitle("Untitled - Notepad");
                newNotepad.show();
                 */
            }
        });
        //2-Open Item
        fileMenuItems[1].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                configureFileChooserOpenDialog(fileChooser);
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile(file);
                }
            }
        });
        //3-Save As Item
        fileMenuItems[2].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                configureFileChooserSaveDialog(fileChooser);
                File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    saveTextToFile(notepadBody.getText(), file);
                }
            }
        });
        //4-Exit Item
        fileMenuItems[4].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (notepadBody.getText().isEmpty() == false) {
                    saveChanges(primaryStage, true);
                } else {
                    primaryStage.close();
                }
            }
        });

        //E D I T  M E N U
        //Assigning shortcuts
        editMenuItems[2].setAccelerator(KeyCombination.keyCombination("Ctrl+x"));       //cut item
        editMenuItems[3].setAccelerator(KeyCombination.keyCombination("Ctrl+c"));       //copy item
        editMenuItems[4].setAccelerator(KeyCombination.keyCombination("Ctrl+v"));       //paste item
        editMenuItems[7].setAccelerator(KeyCombination.keyCombination("Ctrl+a"));       //select all item

        //Handling Events
        //1-Undo Item
        editMenuItems[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notepadBody.undo();
            }
        });
        //2-Cut Item
        editMenuItems[2].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notepadBody.cut();
            }
        });
        //3-Copy Item
        editMenuItems[3].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notepadBody.copy();
            }
        });
        //4-Paste Item
        editMenuItems[4].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notepadBody.paste();
            }
        });
        //5-Delete Item
        editMenuItems[5].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //assigning selection range
                int startPosition = notepadBody.getAnchor();
                int endPosition = notepadBody.getCaretPosition();
                //if the user select text from right to left
                if (startPosition > endPosition) {
                    notepadBody.deleteText(endPosition, startPosition);
                } else {
                    notepadBody.deleteText(startPosition, endPosition);
                }
            }
        });
        //6-Select All Item
        editMenuItems[7].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notepadBody.selectAll();
            }
        });

        //H E L P  M E N U
        //Handling Events
        //About Notepad item
        helpMenuItems[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("FX Notepad");
                alert.setHeaderText("FX Notepad using JavaFX");
                alert.setContentText("Copyright© 2020-2021 ITI Intake 41.\n\n\nAuthor: Shehab El-Deen Alalkamy.");

                alert.showAndWait();
            }
        });

        //scene preparing
        scene = new Scene(notepadLayout, 900, 500);

        //stage preparing
        primaryStage.setTitle("FX Notepad");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    final private void openFile(File file) {
        //Function to open the file chosen by the user
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    NotePad.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    final private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(NotePad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    final private void configureFileChooserOpenDialog(FileChooser fileChooser) {
        //Setting title of fileChooser Dialogue
        fileChooser.setTitle("Open");
        //Setting the initial directory to the user directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        //Adding filters to our fileChooser Dialogue
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Documents(*.txt)", "*.txt")
        );
    }

    final private void configureFileChooserSaveDialog(FileChooser fileChooser) {
        //Setting title of fileChooser Dialogue
        fileChooser.setTitle("Save As");
        //Setting the initial directory to the user directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        //Adding filters to our fileChooser Dialogue
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Documents(*.txt)", "*.txt")
        );
    }

    final private void saveChanges(Stage stage, boolean exit) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("FX Notepad");
        alert.setHeaderText("Do you want to save changes?");

        ButtonType save = new ButtonType("Save");
        ButtonType doNotSave = new ButtonType("Don't Save");
        ButtonType cancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(save, doNotSave, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == save) {
            // ... user chose "Save"
            configureFileChooserSaveDialog(fileChooser);
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                saveTextToFile(notepadBody.getText(), file);
            }
        } else if (result.get() == doNotSave) {
            // ... user chose "Don't Save"
            if (exit == true) {
                stage.close();
            } else {
                notepadBody.clear();
            }
        } else if (result.get() == cancel) {
            // ... user chose "Cancel"
        }
    }
}
