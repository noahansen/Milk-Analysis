/**
 * Project: aTeam
 *
 * Authors: Noah Hansen {nphansen@wisc.edu}, Gunnar Schmitz {goschmitz@wisc.edu}
 * 
 * Date: 4/21/2020
 *
 * Course: CS400 Semester: Spring 2020 Lecture: 001
 *
 * IDE: Eclipse IDE for Java Developers Version: 2019-12 (14.4.0)
 *
 * Device: OS: Windows 10 Version: OS Build:
 * 
 * Note: We chose not to implement the charts, as they are optional. If we have time, we may add
 * them in the future. Also, There are some redundant parts with the table.
 * 
 * Note: Code is a bit messy. It will be split into methods before a3.
 *
 */
package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Main - Drives a milk analysis application that displays milk weight measurements in a table
 * 
 * TODO: The UI must provide sufficient instructions and help tips for new users to navigate.
 * 
 * TODO: how will we edit the table? have a create table for each method?
 * 
 * @author Noah Hansen (2020)
 * @author Gunnar Schmitz
 */
public class Main extends Application {

  private static final int WINDOW_WIDTH = 1000;
  private static final int WINDOW_HEIGHT = 500;
  private static final String APP_TITLE = "Milk Analysis";
  private static final FileChooser fileChooser = new FileChooser();
  private Stage primaryStage = null;
  private MilkStore ds = new MilkStore();

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;

    // Main layout is Border Pane example (top,left,center,right,bottom)
    BorderPane root = new BorderPane();

    // drop down list of reports
    String report_types[] = {"Farm Report", "Monthly Report", "Annual Report", "Date Range Report"};
    ComboBox<String> report_menu =
        new ComboBox<String>(FXCollections.observableArrayList(report_types));

    // TODO when these are selected, get to see input values somehow... global variables.
    // maybe don't diplay table until apply button is pressed
    // only display buttons based on what thing is set
    // pressing a button sets a global variable?
    // is globals the best way to do this
    // apply event handler will call displayReport with the necessary arguments set.


    report_menu.setPromptText("Select a Report");

    Button edit = new Button("Edit Table");
    Button confirm = new Button("Confirm");

    VBox edit_table = new VBox(edit, confirm);
    edit_table.setPadding(new Insets(40, 10, 10, 10));


    VBox left_box = new VBox(getFilters(), edit_table);
    VBox center = new VBox(report_menu, create_table());

    Button json = new Button("Download JSON");
    Button csv = new Button("Download CSV");

    GridPane grid = new GridPane();
    Text input = new Text("Input File(s):");

    // Set to only show csv files
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    Button browse = new Button("Browse");
    browse.setOnAction(new BrowseHandler()); // allows file selection


    grid.addRow(0, input);
    grid.addRow(1, csv);
    grid.add(browse, 1, 0);
    grid.add(json, 1, 1);
    // spacing
    grid.setVgap(30);
    grid.setHgap(5);

    VBox right = new VBox(grid);
    right.setPadding(new Insets(10, 10, 10, 50));

    root.setLeft(left_box);
    root.setCenter(center);
    root.setRight(right);


    Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

    // Add the stuff and set the primary stage
    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  /**
   * BrowseHandler - displays file search dialogue. Reads selected file(s) into the DS. Populates
   * table with monthly report by default.
   */
  class BrowseHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent arg0) {
      List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
      // TODO populate data.
      // TODO input validation... Just catch an exception?
      if (files == null) {
        return;
      }

      int skipCount = 0;
      String line = "";

      // Parse though each file, adding the items to the DS
      for (File file : files) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

          while ((line = br.readLine()) != null) {
            String[] row = line.split(",");
            String date = row[0];
            String farmId = row[1];
            int weight = Integer.parseInt(row[2]);
            String[] dateParts = date.split("-");
            if (dateParts.length < 3) { // invalid, ignore
              skipCount++;
              continue;
            } // TODO handle invalid data
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);

            ds.insert(farmId, year, month, day, weight);

          }

        } catch (Exception e) {
          dispMessage("There was a problem with the file.");
          ds = new MilkStore(); // Ditch the data read in before the fail
        }
      }
    }
  }

  /**
   * make another window with the given message
   * 
   * @param message
   */
  private void dispMessage(String message) {
    Stage stage = new Stage();
    stage.setTitle("Message");
    Text text = new Text(message);
    text.setFont(Font.font("Avenir", FontWeight.BOLD, 20));
    text.setFill(Color.CADETBLUE);
    text.setX(10);
    text.setY(50);

    Group root = new Group(text);
    Scene scene = new Scene(root);// 300, 100
    stage.setScene(scene);
    stage.show();
  }


  @SuppressWarnings({"rawtypes", "unchecked"})
  private TableView create_table() {
    final TableView table = new TableView();

    // TODO: Populate with values from csv?
    final ObservableList<Month> data =
        FXCollections.observableArrayList(new Month("5", "%", "1"), new Month("59", "%", "2"),
            new Month("9", "%", "3"), new Month("95", "%", "4"), new Month("59", "%", "5"));

    table.setEditable(true);

    TableColumn weightCol = new TableColumn("Weight");
    TableColumn pctCol = new TableColumn("Percent");
    TableColumn monthCol = new TableColumn("Month");

    weightCol.setMinWidth(100);
    pctCol.setMinWidth(100);
    monthCol.setMinWidth(100);

    weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
    pctCol.setCellValueFactory(new PropertyValueFactory<>("percent"));
    monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));

    table.setItems(data);
    table.getColumns().addAll(weightCol, pctCol, monthCol);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    return table;
  }

  // TODO have keep track of all the filter values and only show relevant ones
  private GridPane getFilters() {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setMinSize(300, 300);
    grid.setVgap(5);
    grid.setHgap(5);

    Text heading = new Text("Filters");
    heading.setUnderline(true);
    grid.add(heading, 0, 0);

    Text farmId = new Text("Farm id:");
    grid.add(farmId, 0, 1);
    Text year = new Text("Year:");
    grid.add(year, 0, 2);
    Text month = new Text("Month:");
    grid.add(month, 0, 3);
    Text start = new Text("Start Date:");
    grid.add(start, 0, 4);
    Text end = new Text("End Date:");
    grid.add(end, 0, 5);
    Text asc = new Text("Ascending:");
    grid.add(asc, 0, 6);
    Text percent = new Text("Sort by percent:");
    grid.add(percent, 0, 7);
    Text display = new Text("Display:");
    grid.add(display, 0, 8);
    Text byMonth = new Text("Display by Month:");
    grid.add(byMonth, 0, 9);

    Button apply = new Button("Apply");
    grid.add(apply, 0, 10);


    TextField text; // TODO will have to keep track of the text in the future
    for (int i = 1; i < 6; i++) {
      text = new TextField();
      text.setPrefColumnCount(10);
      grid.add(text, 1, i);
    }

    RadioButton rb1 = new RadioButton();
    RadioButton rb2 = new RadioButton();
    RadioButton rb3 = new RadioButton();
    grid.add(rb1, 1, 6);
    grid.add(rb2, 1, 7);
    grid.add(rb3, 1, 9);

    String[] dispOptns = {"Minimum", "Maximum", "Average"};
    ComboBox<String> disp = new ComboBox<String>(FXCollections.observableArrayList(dispOptns));
    disp.setPromptText("Min/Max/Avg");
    grid.add(disp, 1, 8);

    return grid;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    launch(args);
  }

}
