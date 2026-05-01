package ui_pkg.Learning;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private final StringProperty greeting = new SimpleStringProperty("");
    private final StringProperty name = new SimpleStringProperty("");
    private int click = 0;

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent(),600,400, Color.SKYBLUE);
        Image icon = new Image("ui_pkg/Learning/plague_inc_logo.png");
        //primaryStage.setHeight(1200);
        //primaryStage.setWidth(800);

        primaryStage.setTitle("Plague inc");
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);

        //primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("Hehehe");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.valueOf("q"));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Region createContent(){ 

        
        Text text = new Text();
        text.setText("This is a text");
        text.setX(100);
        text.setY(100);

        VBox results = new VBox(20, text,createBubbleImage(), createGreetingButton(), createInputRow(), createOutputLabel());
        results.setAlignment(Pos.CENTER);

        return results;
    }

    private Node createBubbleImage(){
    Image image = new Image(getClass().getResourceAsStream("/ui_pkg/Learning/Biohazard_bonus_icon.png"));      
    ImageView imageView = new ImageView(image);

        imageView.setX(50);
        imageView.setY(50);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        imageView.setOnMouseClicked(evt -> setGreeting());

        return imageView;
    }

    private Node createGreetingButton(){
        Button m_button = new Button("Hello");
        m_button.setBorder(null);
        m_button.setOnAction(evt -> setGreeting());

        return m_button;
    }

    private Node createOutputLabel(){
        Label outputLabel = new Label("");
        outputLabel.textProperty().bind(greeting);
        return outputLabel;
    }

    private Node createInputRow(){
                TextField textField = new TextField("");
        textField.textProperty().bindBidirectional(name);
        HBox results = new HBox(new Label("Name:"), textField);
                results.setSpacing(6);
        results.setAlignment(Pos.CENTER);
        return results;
    }

    private void setGreeting(){
        this.greeting.set("Hello " + this.name.get() +"\n Clicks: " + click);
        click++;
    }
}