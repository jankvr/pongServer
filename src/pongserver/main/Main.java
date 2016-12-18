/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pongserver.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Hlavná trieda servera
 * @author Jan Kovář, Jaroslav Fedorčák
 */
public class Main extends Application implements IGui {

    private TextArea textArea;
    private static final int PORT = 5000;
    
    /**
     * Hlavni metoda pro vypis okna a samotne vytvoreni serveru
     * @param stage 
     */
    @Override
    public void start(Stage stage) {
        textArea = new TextArea();
        textArea.setEditable(false);

        Server server = new Server(PORT, this);
        
        StackPane root = new StackPane();
        root.getChildren().add(textArea);

        Scene scene = new Scene(root, 720, 500);

        stage.setTitle("Pong Server");
        stage.setScene(scene);
        stage.show();
            
        stage.setOnCloseRequest((WindowEvent e) -> {
            Platform.exit();
            System.exit(0);
        });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void appendMessage(String message) {
        textArea.appendText(message + "\n");
    }
    
}
