package ui_pkg;

import java.util.ArrayList;
import java.util.List;

import game_pkg.country_pkg.Country;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ConceptOfMap extends Application {
    
    private final StringProperty mousePosition = new SimpleStringProperty("");
    private final DoubleProperty xMousePosition = new SimpleDoubleProperty(0.0);
    private final DoubleProperty yMousePosition = new SimpleDoubleProperty(0.0);

    private Image bioBubble = new Image(getClass().getResourceAsStream("/ui_pkg/Assets/Biohazard_bonus_icon.png"));
    private ImageView biohazardBubble = new ImageView(bioBubble);

    private Timeline hazardBubbleAppearAnimationation;
    private Timeline hazardBubblePOPAnimationation;

    private static ConceptOfMap instance;

    public static ConceptOfMap getInstance() {
        return instance;
    }

    AudioClip infect_appear = 
        new AudioClip(getClass().getResource("/ui_pkg/Assets/AudioClip/infect_appear_sfx.wav").toString());
    AudioClip infect_pop =
        new AudioClip(getClass().getResource("/ui_pkg/Assets/AudioClip/infect_pop_sfx.wav").toString());

    public ConceptOfMap(){
        hazardBubbleAppearAnimationation = new Timeline(
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

        hazardBubblePOPAnimationation = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(biohazardBubble.scaleXProperty(), 1.0),
                new KeyValue(biohazardBubble.scaleYProperty(), 1.0)
            ),
            new KeyFrame(Duration.millis(100), 
                new KeyValue(biohazardBubble.scaleXProperty(), 1.1),
                new KeyValue(biohazardBubble.scaleYProperty(), 1.1)
            ),
            new KeyFrame(Duration.millis(200), 
                new KeyValue(biohazardBubble.scaleXProperty(), 0.0),
                new KeyValue(biohazardBubble.scaleYProperty(), 0.0)
            )
        );
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        instance = this;
        Scene scene = new Scene(createContent(),1280 ,720 , Color.BLACK);
        Image icon = new Image("ui_pkg/Assets/plague_inc_logo.png");

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

        biohazardBubble.setOnMousePressed(evt -> {
            System.out.println("pop");
            hazardBubblePOPAnimationation.playFromStart();
            infect_pop.play();

            evt.consume();
        });

        root.setOnMousePressed(evt -> {


            System.out.println(xMousePosition.doubleValue());
            xMousePosition.set(evt.getSceneX());
            yMousePosition.set(evt.getSceneY());
            mousePosition.set("X: " + evt.getSceneX() + " Y: " + evt.getSceneY());
            hazardBubbleAppearAnimationation.playFromStart();
            infect_appear.play();
        });

        return root;
    }

    private Node createWorldMap(){
        Image image = new Image(getClass().getResourceAsStream("/ui_pkg/Assets/MiniMap_Outline_Add_China.png"));      
        ImageView imageView = new ImageView(image);

        imageView.setX(50);
        imageView.setY(50);
        imageView.setFitWidth(1200);
        imageView.setPreserveRatio(true);

        imageView.setPickOnBounds(true);
        return imageView;
    }

    private Pane generateBioBubble(){

        biohazardBubble.setFitWidth(70);

        biohazardBubble.setPreserveRatio(true);

        Pane wrapper = new Pane(biohazardBubble);
        
        biohazardBubble.layoutXProperty().bind(xMousePosition.subtract(35));
        biohazardBubble.layoutYProperty().bind(yMousePosition.subtract(70));

        biohazardBubble.setScaleX(0);
        biohazardBubble.setScaleY(0);

        return wrapper;
    }

    public void showBubbleAt(double x, double y){
        Platform.runLater(() -> {
            infect_appear.play();
            xMousePosition.set(x);
            yMousePosition.set(y);
            hazardBubbleAppearAnimationation.playFromStart();
        });
    }
}
