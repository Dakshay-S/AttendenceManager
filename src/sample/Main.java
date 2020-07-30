package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AttendenceTable.fxml"));

        Parent root =  loader.load();
        AttendenceTableController  controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setOnCloseRequest(e->{e.consume();
        controller.onCloseRequest();
        });



        primaryStage.setTitle("Attendance Book");
        Scene scene = new Scene(root);
        scene.getStylesheets().add((getClass().getResource("SceneCSS.css")).toExternalForm());
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Scholar.png")));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
