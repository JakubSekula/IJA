package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private Pane scene;
    @FXML
    private Pane scene2;

    private List<Drawable> elements = new ArrayList<>();

    public void setElements(List<Drawable> elements){
        this.elements = elements;
        for (Drawable drawable : elements){
            scene.getChildren().addAll(drawable.getGUI());
        }
    }
}
