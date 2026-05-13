package ui_pkg;

import java.util.ArrayList;
import java.util.List;

import game_pkg.Main;
import javafx.animation.AnimationTimer;
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
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui_pkg.Enums.ScreenState;

public class ConceptOfMap extends Application {

    private static ConceptOfMap instance;

    private final StringProperty mousePosition;
    private final DoubleProperty xMousePosition;
    private final DoubleProperty yMousePosition;

    private final Image bioBubble;
    private final Image hexGrid;

    private final AudioClip infectAppear;
    private final AudioClip infectPop;

    private final Text totalInfected;
    private final Text totalADN;

    private final List<ImageView> activeBubbles;

    private Pane root;
    private Pane worldLayer;

    private final AnimationTimer gameLoop;
    private ScreenState screenState;

    private Rectangle optionsButton;

    public ConceptOfMap() {

        mousePosition = new SimpleStringProperty("");
        xMousePosition = new SimpleDoubleProperty(0.0);
        yMousePosition = new SimpleDoubleProperty(0.0);
        activeBubbles = new ArrayList<>();

        optionsButton = new Rectangle();

        optionsButton.setWidth(200);
        optionsButton.setHeight(40);
        optionsButton.setX(50);
        optionsButton.setY(650);

        optionsButton.setFill(Color.BEIGE);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now){
                
            }
        };

        bioBubble = new Image(getClass().getResourceAsStream("/ui_pkg/Assets/Biohazard_bonus_icon.png"));
        hexGrid = new Image(getClass().getResourceAsStream("/ui_pkg/Assets/Hex_Trim_02.png"));

        infectAppear = new AudioClip(getClass().getResource("/ui_pkg/Assets/AudioClip/infect_appear_sfx.wav").toString());
        infectPop = new AudioClip(getClass().getResource("/ui_pkg/Assets/AudioClip/infect_pop_sfx.wav").toString());

        infectAppear.setVolume(0.5);
        infectPop.setVolume(0.5);

        totalInfected = new Text("0");
        totalInfected.setFill(Color.WHITE);
        totalInfected.setLayoutX(600);
        totalInfected.setLayoutY(700);

        totalADN = new Text("DNA: 0");
        totalADN.setFill(Color.WHITE);
        totalADN.setLayoutX(600);
        totalADN.setLayoutY(500);

        infectAppear.play();
        infectPop.play();

        screenState = ScreenState.WORLD_MAP;
    }

    public static ConceptOfMap getInstance() {
        return instance;
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
        root = new Pane(); 
        worldLayer = new Pane();        

        Text text = new Text();
        text.textProperty().bind(mousePosition);
        text.setFill(Color.WHITE);
        text.setLayoutX(50);
        text.setLayoutY(700);

        worldLayer.getChildren().addAll(createWorldMap(), text, totalInfected, totalADN,optionsButton);

        root.getChildren().add(worldLayer);
        root.setOnMousePressed(evt -> {

            System.out.println(xMousePosition.doubleValue());
            xMousePosition.set(evt.getSceneX());
            yMousePosition.set(evt.getSceneY());
            mousePosition.set("X: " + evt.getSceneX() + " Y: " + evt.getSceneY());
        });

        optionsButton.setOnMouseClicked(e -> {
            worldLayer.setVisible(false);
            screenState = ScreenState.PLAGUE_MENU;
            showHexTrim();
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

    public void showBubbleAt(double x, double y){
        Platform.runLater(() -> {
            infectAppear.play();

            ImageView bubble = new ImageView(bioBubble);
            bubble.setFitWidth(70);
            bubble.setPreserveRatio(true);
            bubble.setLayoutX(x - 35);
            bubble.setLayoutY(y - 70);
            bubble.setScaleX(0);
            bubble.setScaleY(0);

        Timeline appear = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(bubble.scaleXProperty(), 0),
                new KeyValue(bubble.scaleYProperty(), 0)
            ),
            new KeyFrame(Duration.millis(100), 
                new KeyValue(bubble.scaleXProperty(), 1.1),
                new KeyValue(bubble.scaleYProperty(), 1.1)
            ),
            new KeyFrame(Duration.millis(300), 
                new KeyValue(bubble.scaleXProperty(), 1.0),
                new KeyValue(bubble.scaleYProperty(), 1.0)
            )
        );

        Timeline pop = new Timeline(
            new KeyFrame(Duration.ZERO, 
                new KeyValue(bubble.scaleXProperty(), 1.0),
                new KeyValue(bubble.scaleYProperty(), 1.0)
            ),
            new KeyFrame(Duration.millis(100), 
                new KeyValue(bubble.scaleXProperty(), 1.1),
                new KeyValue(bubble.scaleYProperty(), 1.1)
            ),
            new KeyFrame(Duration.millis(200), 
                new KeyValue(bubble.scaleXProperty(), 0.0),
                new KeyValue(bubble.scaleYProperty(), 0.0)
            )
        );

        bubble.setOnMousePressed(evt -> {
            infectPop.play();
            pop.playFromStart();
            Main.popBioBubble();
            evt.consume();
        });
        
        pop.setOnFinished(e -> {
            root.getChildren().remove(bubble);
            activeBubbles.remove(bubble);
        });
        
        worldLayer.getChildren().add(bubble);
        activeBubbles.add(bubble);
        appear.playFromStart();

        });
    }
    
    public void showHexTrim(){
        Platform.runLater(()->{
            ImageView fullHex = new ImageView(hexGrid);
            
            fullHex.setFitWidth(750);
            fullHex.setPreserveRatio(true);
            fullHex.setLayoutX(250);
            fullHex.setLayoutY(40);
            fullHex.setBlendMode(BlendMode.SCREEN);

            root.getChildren().add(fullHex);
            root.setBackground(Background.fill(Paint.valueOf("black")));

        });
    }

    public void updateTotalInfected(int infected){
        this.totalInfected.setText("Total infected: " + infected);
    }

    public void updateTotalDna(int dna){
        this.totalADN.setText("DNA: " + dna);
    }
}
