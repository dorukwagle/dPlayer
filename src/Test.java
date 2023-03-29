import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.Group;
import javafx.scene.shape.*;

public class Test extends Application {

    // launch the application
    public void start(Stage stage)
    {

        try {

            // set title for the stage
            stage.setTitle("StackPane");

            // create a label
            Label label = new Label("this is StackPane example");

            // set Font for label
            label.setFont(new Font(30));

            // create a circle
            Circle circle = new Circle(100, 100, 70);

            // set fill for  the circle
            circle.setFill(Color.RED);

            // create Rectangle
            Rectangle rectangle = new Rectangle(100, 100, 180, 160);

            // set fill for rectangle
            rectangle.setFill(Color.BLUE);

            // create a stack pane
            StackPane stack_pane = new StackPane(rectangle, circle, label);

            // set alignement for the stack pane
            stack_pane.setAlignment(rectangle, Pos.TOP_CENTER);
            stack_pane.setAlignment(circle, Pos.BASELINE_RIGHT);

            // create a scene
            Scene scene = new Scene(stack_pane, 400, 300);

            // set the scene
            stage.setScene(scene);

            stage.show();
        }

        catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    // Main Method
    public static void main(String args[])
    {

        // launch the application
        launch(args);
    }
}