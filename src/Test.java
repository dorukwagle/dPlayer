import com.doruk.dplayer.seekbar.SeekBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Test extends Application  {


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ComboBox Experiment 1");

        ComboBox comboBox = new ComboBox();

        comboBox.getItems().add("Choice 1");
        comboBox.getItems().add("Choice 2");
        comboBox.getItems().add("Choice 3");


        HBox hbox = new HBox(comboBox);

        SeekBar seekBar = new SeekBar();
        seekBar.setMax(100);
        seekBar.setMin(0);
        seekBar.setValue(20);
        HBox hhbox = new HBox(seekBar);

        VBox vbox = new VBox(hbox, hhbox);
        vbox.setFillWidth(true);


        Scene scene = new Scene(vbox);
        scene.getStylesheets().add(this.getClass().getResource("/assets/dark_theme_adv.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}