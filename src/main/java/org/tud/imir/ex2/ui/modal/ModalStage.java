package org.tud.imir.ex2.ui.modal;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class ModalStage extends Stage {

    private Scene scene;

    public ModalStage(ModalController controller, URL fxml, Window owner) {
        this(controller, fxml, owner, StageStyle.DECORATED, Modality.APPLICATION_MODAL);
    }

    public ModalStage(final ModalController controller, URL fxml, Window owner, StageStyle style, Modality modality) {
        super(style);
        initOwner(owner);
        initModality(modality);
        FXMLLoader loader = new FXMLLoader(fxml);
        try {
            loader.setControllerFactory(aClass -> controller);
            controller.setDialog(this);
            scene = new Scene(loader.load());
            setScene(scene);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<String> getStyleSheets() {
        return scene.getStylesheets();
    }
}