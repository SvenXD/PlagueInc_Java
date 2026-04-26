package ui_pkg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MainUI extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create a simple circle
        Circle circle = new Circle(50, Color.RED);
        
        // Put it in a layout
        StackPane root = new StackPane();
        root.getChildren().add(circle);
        
        // Create scene (800x600)
        Scene scene = new Scene(root, 800, 600);
        
        // Set window title
        primaryStage.setTitle("Plague Inc Clone");
        
        // Add scene to window
        primaryStage.setScene(scene);
        
        // Show window
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}