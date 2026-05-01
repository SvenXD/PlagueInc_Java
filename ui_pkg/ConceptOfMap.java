package ui_pkg;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ConceptOfMap extends Application {
    
    private final StringProperty mousePosition = new SimpleStringProperty("");
    private final DoubleProperty xMousePosition = new SimpleDoubleProperty(0.0);
    private final DoubleProperty yMousePosition = new SimpleDoubleProperty(0.0);
    private Timeline popAnimation;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent(),1280 ,720 , Color.BLACK);
        Image icon = new Image("ui_pkg/Learning/plague_inc_logo.png");

        stage.setTitle("Plague inc");
        stage.getIcons().add(icon);
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    private Region createContent(){
        Pane root = new Pane(); 
        
        Node map = createWorldMap();
        Pane bubbleLayer = generateBioBubble();
        
        Text text = new Text();
        text.textProperty().bind(mousePosition);
        text.setFill(Color.WHITE);
        text.setLayoutX(50);
        text.setLayoutY(700);

        root.getChildren().addAll(map, bubbleLayer, text);

        root.setOnMousePressed(evt -> {
            System.out.println(xMousePosition.doubleValue());
            xMousePosition.set(evt.getSceneX());
            yMousePosition.set(evt.getSceneY());
            mousePosition.set("X: " + evt.getSceneX() + " Y: " + evt.getSceneY());
            popAnimation.playFromStart();
        });

        return root;
    }

    private Node createWorldMap(){
        Image image = new Image(getClass().getResourceAsStream("/ui_pkg/Learning/MiniMap_Outline_Add_China.png"));      
        ImageView imageView = new ImageView(image);

        imageView.setX(50);
        imageView.setY(50);
        imageView.setFitWidth(1200);
        imageView.setPreserveRatio(true);

        imageView.setPickOnBounds(true);
        return imageView;
    }

    private Pane generateBioBubble(){
        Image bioBubble = new Image(getClass().getResourceAsStream("/ui_pkg/Learning/Biohazard_bonus_icon.png"));
        ImageView biohazardBubble = new ImageView(bioBubble);

        biohazardBubble.setFitWidth(70);

        biohazardBubble.setPreserveRatio(true);
        biohazardBubble.setMouseTransparent(true);

        Pane wrapper = new Pane(biohazardBubble);
        
        biohazardBubble.layoutXProperty().bind(xMousePosition.subtract(35));
        biohazardBubble.layoutYProperty().bind(yMousePosition.subtract(70));

        biohazardBubble.setScaleX(0);
        biohazardBubble.setScaleY(0);

        popAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(biohazardBubble.scaleXProperty(), 0),
                new KeyValue(biohazardBubble.scaleYProperty(), 0)
            ),
            new KeyFrame(Duration.millis(100), 
                new KeyValue(biohazardBubble.scaleXProperty(), 1.1),
                new KeyValue(biohazardBubble.scaleYProperty(), 1.1)
            ),
            new KeyFrame(Duration.millis(300), 
                new KeyValue(biohazardBubble.scaleXProperty(), 1.0),
                new KeyValue(biohazardBubble.scaleYProperty(), 1.0)
            )
        );
        return wrapper;
    }
}
