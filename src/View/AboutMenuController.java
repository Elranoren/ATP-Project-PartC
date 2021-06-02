package View;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutMenuController implements Initializable {
    public  javafx.scene.image.ImageView elranAndTamarImage;

    public AboutMenuController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(getClass().getResourceAsStream("/images/elranAndTamar.jpeg"));
        elranAndTamarImage.setImage(image);
    }
}
