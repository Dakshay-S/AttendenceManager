package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import  java.util.Date;

public class AttendenceTableController implements Initializable
{

    private Stage primaryStage;


    public Label date;
    @FXML
    private Button save;
    @FXML
    private Button saveAndClose;
    @FXML
    private TableView<Course> tableView;
    @FXML
    private TableColumn<Course , String> subject;
    @FXML
    private TableColumn<Course , Integer> noOfPresents;
    @FXML
    private TableColumn<Course , Integer> noOfAbsents;
    @FXML
    private TableColumn<Course , Float> percentage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        save.setOnAction(e->save());
        saveAndClose.setOnAction(e->saveNClose());
        saveAndClose.setStyle("-fx-background-color:red");
        save.setStyle("-fx-background-color:#098812");
        percentage.setResizable(false);
        noOfPresents.setResizable(false);
        noOfAbsents.setResizable(false);
        subject.setResizable(false);
        tableView.getSelectionModel().setCellSelectionEnabled(false);

        {
            Connection conn;
            try {

                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:src/sample/PlayGroundDataBase.db");
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM Date");
                ResultSet  rst = pst.executeQuery();

                rst.next();
                date.setTextFill(Color.web("#ffffff"));
                date.setText(rst.getString(1));
                rst.close();
                pst.close();
                conn.close();



            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }







        tableView.getStylesheets().add(getClass().getResource("cellStyle.css").toExternalForm());
        //tableView.setItems(getCourses());
        subject.setCellValueFactory(cell->cell.getValue().subjectProperty());
        noOfPresents.setCellValueFactory(cell-> cell.getValue().noOfPresentsProperty().asObject());
        noOfAbsents.setCellValueFactory(cell->cell.getValue().noOfAbsentsProperty().asObject());
        percentage.setCellValueFactory(cell->cell.getValue().percentageProperty().asObject());
        subject.setCellFactory(new Callback<TableColumn<Course, String>, TableCell<Course, String>>() {
            @Override
            public TableCell<Course, String> call(TableColumn<Course, String> courseStringTableColumn) {
                return new subjectCell();
            }
        });
        noOfPresents.setCellFactory(new Callback<TableColumn<Course, Integer>, TableCell<Course, Integer>>() {
            @Override
            public TableCell<Course, Integer> call(TableColumn<Course, Integer> courseIntegerTableColumn) {
                return new presentCell();
            }
        });
        noOfAbsents.setCellFactory(new Callback<TableColumn<Course, Integer>, TableCell<Course, Integer>>() {
            @Override
            public TableCell<Course, Integer> call(TableColumn<Course, Integer> courseIntegerTableColumn) {
                return new absentCell();
            }
        });

        ObservableList<Course> list = FXCollections.observableArrayList(getCoursesFromDataBase());
        tableView.setItems(list);




    }

    class subjectCell extends TableCell<Course , String>
    {
        Label label;
        HBox hBox;

        subjectCell() {
            super();
            hBox = new HBox();
            hBox.setPrefSize(10, 50);
            hBox.setAlignment(Pos.CENTER);
            label = new Label();
            HBox.setHgrow(label , Priority.ALWAYS);

            hBox.getChildren().add(label);

            label.setTextFill(Color.WHITESMOKE);
            label.setStyle("-fx-text-weight:bold");
            label.setStyle("-fx-font-size:15px");
            setGraphic(hBox);

        }

        @Override
        protected void updateItem(String s, boolean empty) {
            super.updateItem(s, empty);

            if(!empty)
                label.setText(s);
            else
                label.setText("");
        }
    }

    class presentCell extends  TableCell<Course , Integer>
    {
        HBox hBox;
        Label label;
        Button add;


        public presentCell()
        {
            hBox = new HBox();
            hBox.setPrefSize(10, 50);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            label = new Label(String.valueOf(getItem()));
            label.setStyle("-fx-font-size:14");
            label.setAlignment(Pos.CENTER);

            label.setStyle("-fx-border-color:transparent");

            HBox.setHgrow(label,Priority.ALWAYS);
            add = new Button("+");
            add.setStyle("-fx-text-size:12");
            add.getStylesheets().add(getClass().getResource("plusButton.css").toExternalForm());
            HBox.setMargin(add , new Insets(0,0,0,42));
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert alert = createAlert();
                    Optional<ButtonType> choice = alert.showAndWait();
                    if(choice.get().equals(ButtonType.YES))
                    {
                        Course course = tableView.getItems().get(getIndex());
                        int present = course.getNoOfPresents();
                        updatePresent(course , present+1);
                        label.setText(String.valueOf(course.getNoOfPresents()));
                    }
                }
            });

            hBox.getChildren().addAll(label , add);


            setGraphic(hBox);
        }

        @Override
        public void updateItem(Integer integer, boolean empty) {
            super.updateItem(integer, empty);

            if(!empty)
            {
                setStaticGraphic();
            }

            else
                setGraphic(null);

        }

       /* @Override
        public void cancelEdit() {
            super.cancelEdit();
            setStaticGraphic();
        }

        @Override
        public void commitEdit(Integer integer) {
            super.commitEdit(integer);
            setStaticGraphic();
        }
*/
        void setStaticGraphic()
        {
            label.setText(String.valueOf(getItem()));
            setGraphic(hBox);
        }
    }

    class absentCell extends TableCell<Course , Integer>
    {
        HBox hBox;
        Label label;
        Button add;


        absentCell()
        {
            hBox = new HBox();
            hBox.setPrefSize(10, 50);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            label = new Label(String.valueOf(getItem()));
            label.setStyle("-fx-font-size:14");
            label.setAlignment(Pos.CENTER);


            label.setStyle("-fx-border-color:transparent");

            HBox.setHgrow(label,Priority.ALWAYS);
            add = new Button("+");
            add.setStyle("-fx-text-size:12");
            add.getStylesheets().add(getClass().getResource("plusButton.css").toExternalForm());
            HBox.setMargin(add , new Insets(0,0,0,42));
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert alert = createAlert();
                    Optional<ButtonType> choice = alert.showAndWait();
                    if(choice.get().equals(ButtonType.YES))
                    {
                        Course course = tableView.getItems().get(getIndex());
                        int absent = course.getNoOfAbsents();
                        updateAbsent(course , absent+1);

                        label.setText(String.valueOf(course.getNoOfAbsents()));
                    }
                }
            });

            hBox.getChildren().addAll(label , add);


            setGraphic(hBox);
        }

        @Override
        public void updateItem(Integer integer, boolean empty) {
            super.updateItem(integer, empty);

            if(!empty)
            {
                setStaticGraphic();
            }

            else
                setGraphic(null);
        }

       /* @Override
        public void cancelEdit() {
            super.cancelEdit();
            setStaticGraphic();
        }

        @Override
        public void commitEdit(Integer integer) {
            super.commitEdit(integer);
            setStaticGraphic();
        }*/

        void setStaticGraphic()
        {
            label.setText(String.valueOf(getItem()));
            setGraphic(hBox);
        }
    }

    private ObservableList<Course> getCoursesFromDataBase()
    {
        Connection connection;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:Attendence.sqlite");
            if(connection != null)
            {
                PreparedStatement pst = connection.prepareStatement("SELECT * FROM AttendenceDB");
                ResultSet rst = pst.executeQuery();
                ObservableList<Course> list = FXCollections.observableArrayList();
                while(rst.next())
                {
                    Course course =  new Course(rst.getString(1) , rst.getInt(2),rst.getInt(3),rst.getFloat(4));
                    list.add(course);
                }

                pst.close();
                connection.close();
                rst.close();
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void saveNClose()
    {
        saveToDatabase();
        primaryStage.close();
        System.exit(0);
    }

    private void  save()
    {
        saveToDatabase();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updated");
        alert.setHeaderText("Updated in the data base");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.OK);
        //alert.getDialogPane().
        alert.show();

    }
    private void saveToDatabase() {
       ObservableList<Course> list =  tableView.getItems();
        try {
            //Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Attendence.sqlite");

            Course course;
        for(int i =0;i<list.size();i++)
        {
            course =list.get(i);
           String subject = course.getSubject();
           int present = course.getNoOfPresents();
           int absent = course.getNoOfAbsents();
           float percentage =  course.getPercentage();
           String query = "UPDATE AttendenceDB SET Present = "+present+", Absent ="+absent
                             +", PresentPercentage ="+percentage + " WHERE Subject =  '"+subject+"'";

           PreparedStatement pst = connection.prepareStatement(query);
           pst.executeUpdate();

            pst.close();
        }

        connection = DriverManager.getConnection("jdbc:sqlite:src/sample/PlayGroundDataBase.db");
            Date datetime = new Date();
        PreparedStatement pst = connection.prepareStatement("UPDATE Date SET Date = ' "+datetime +"'");
        date.setText(datetime.toString());
        pst.execute();
        pst.close();


        connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Alert createAlert()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Increment");
        alert.setContentText("Are you sure you want to increment the variable ?");
        alert.setTitle("");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES , ButtonType.CANCEL);

        Button button = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
        button.setDefaultButton(false);

        Button button2 = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        button2.setDefaultButton(true);

        return alert;
    }


    void onCloseRequest()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("SURE EXIT ?");
        alert.setContentText("DO YOU WANT TO SAVE AND EXIT ?");

        alert.getButtonTypes().clear();
        ButtonType back = new ButtonType("BACK");
        ButtonType saveNexit  = new ButtonType("SAVE EXIT");
        ButtonType dontSave = new ButtonType("DON'T SAVE");
        alert.getButtonTypes().addAll(saveNexit , dontSave , back);
        ((Button) alert.getDialogPane().lookupButton(dontSave)).setDefaultButton(true);

        Optional<ButtonType> type = alert.showAndWait();

        if(type.get().equals(dontSave))
        {
            System.exit(0);
        }

        else if(type.get().equals(saveNexit))
        {
            saveNClose();
        }

    }


    void updatePresent(Course course , int noOfPresent)
    {
        course.setNoOfPresents(noOfPresent);
        int total = course.getNoOfAbsents() + course.getNoOfPresents();
        course.setPercentage((float)course.getNoOfPresents()*100/total);
    }

    void updateAbsent(Course course , int noOfAbsent)
    {

        course.setNoOfAbsents(noOfAbsent);
        int total = course.getNoOfAbsents() + course.getNoOfPresents();
        course.setPercentage((float)course.getNoOfPresents()*100/total);
    }

}
