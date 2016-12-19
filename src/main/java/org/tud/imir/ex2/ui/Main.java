package org.tud.imir.ex2.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tud.imir.ex2.ui.modal.ModalController;
import org.tud.imir.ex2.ui.modal.ModalStage;
import org.tud.imir.ex2.ui.query.QueryController;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        QueryController queryController = new QueryController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/query.fxml"));
        loader.setControllerFactory(aClass -> queryController);
        Parent root = loader.load();
        queryController.setStage(primaryStage);
        primaryStage.setTitle("Similar image finder");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        ModalStage modal = new ModalStage(new ModalController(), getClass().getResource("/layout/modal.fxml"), primaryStage.getOwner());
        modal.setTitle("Index selection");
        modal.getStyleSheets().add("/style/main.css");
        modal.setOnCloseRequest(event -> {
            // consume event
            event.consume();

            Platform.exit();
        });
        modal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
