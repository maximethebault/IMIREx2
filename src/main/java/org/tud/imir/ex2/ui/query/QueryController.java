package org.tud.imir.ex2.ui.query;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.tud.imir.ex2.service.ConfigurationHolder;
import org.tud.imir.ex2.service.ImageMatchingService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryController {
    private ConfigurationHolder configurationHolder;

    private ImageMatchingService imageMatchingService;

    @FXML
    private TextField path;
    @FXML
    private Button browse;
    @FXML
    private ScrollPane resultPane;
    @FXML
    private ImageView queryObject;
    @FXML
    private Button search;

    private Stage stage;

    @FXML
    public void initialize() {
        configurationHolder = ConfigurationHolder.getInstance();
        imageMatchingService = new ImageMatchingService();

        path.focusedProperty().addListener((observableValue, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                browse.requestFocus();
                onBrowseClicked();
            }
        });

        browse.requestFocus();
    }

    public void onBrowseClicked() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open image");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JPEG images", "*.jpg", "*.jpeg");
        chooser.getExtensionFilters().add(extensionFilter);
        chooser.setSelectedExtensionFilter(extensionFilter);
        if (configurationHolder.getResourcePath() != null) {
            chooser.setInitialDirectory(new File(configurationHolder.getResourcePath()));
        }
        else {
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }
        File image = chooser.showOpenDialog(stage);
        if (image != null && image.isFile()) {
            path.setText(image.toString());
        }
    }

    public void onSearchClicked() {
        if (path.getText().length() == 0) {
            return;
        }

        File file = new File(path.getText());
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            queryObject.setImage(image);
        }

        List<String> similarImages = new ArrayList<>();
        try {
            similarImages = imageMatchingService.findSimilarImages(path.getText(), 20);
        }
        catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input file");
            alert.setHeaderText(null);
            alert.setContentText("The JPEG file given as input is not a valid image");

            alert.showAndWait();
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Could not read the file");
            alert.setHeaderText(null);
            alert.setContentText("There was an error while trying to read the input file");

            alert.showAndWait();
        }
        displayResults(similarImages);
    }

    private void displayResults(List<String> paths) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));

        int x = 0, y = 0;

        for (int i = 0; i < paths.size(); i++) {
            if ((i % 5 == 0) && (i != 0)) {
                x = 0;
                y++;
            }
            File file = new File(paths.get(i));
            Image image = new Image(file.toURI().toString());
            ImageView iv = new ImageView();
            iv.setImage(image);
            iv.setFitWidth(100);
            iv.setFitHeight(300);
            iv.setPreserveRatio(true);

            grid.add(iv, x, y);
            x++;
        }
        resultPane.setContent(grid);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
