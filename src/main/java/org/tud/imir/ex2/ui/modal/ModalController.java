package org.tud.imir.ex2.ui.modal;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.tud.imir.ex2.service.ConfigurationHolder;
import org.tud.imir.ex2.service.ImageMatchingService;

import java.io.File;

public class ModalController {

    private ModalStage dialog;

    @FXML
    private TextField indexName;
    @FXML
    private TextField resourcePath;
    @FXML
    private Button browseButton;
    @FXML
    private Button submitButton;
    @FXML
    private ProgressBar progressBar;

    public void setDialog(ModalStage dialog) {
        this.dialog = dialog;
    }

    @FXML
    public void initialize() {
        resourcePath.focusedProperty().addListener((observableValue, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                browseButton.requestFocus();
                onBrowseForResource();
            }
        });
    }

    @FXML
    public void onBrowseForResource() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open resource directory");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File directory = chooser.showDialog(dialog);
        if (directory != null && directory.isDirectory()) {
            resourcePath.setText(directory.toString());
        }
    }

    @FXML
    public void onSubmit() {
        if (indexName.getText().length() == 0 || resourcePath.getText().length() == 0) {
            return;
        }
        ConfigurationHolder configurationHolder = ConfigurationHolder.getInstance();
        configurationHolder.setIndexName(indexName.getText());
        configurationHolder.setResourcePath(resourcePath.getText());
        ImageMatchingService imageMatchingService = new ImageMatchingService();
        if (!imageMatchingService.isIndexFilled(configurationHolder.getIndexName())) {
            submitButton.setText("Indexing...");
            submitButton.setDisable(true);
            browseButton.setDisable(true);
            resourcePath.setDisable(true);
            indexName.setDisable(true);
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    imageMatchingService.indexResources(configurationHolder.getIndexName(), configurationHolder.getResourcePath(), this::updateProgress);
                    return null;
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    dialog.close();
                }
            };
            progressBar.setProgress(0);
            progressBar.setVisible(true);
            progressBar.progressProperty().bind(task.progressProperty());
            new Thread(task).start();
        }
        else {
            dialog.close();
        }
    }
}