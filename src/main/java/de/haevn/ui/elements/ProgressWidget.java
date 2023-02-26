package de.haevn.ui.elements;

import de.haevn.ui.elements.html.H1;
import de.haevn.ui.elements.html.H3;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProgressWidget extends VBox {

    public ProgressWidget() {
        this("An operation is currently ongoing", "This may take a few seconds.");
    }

    public ProgressWidget(String title, String text) {
        ProgressBar pb = new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS);
        pb.setMaxWidth(400);
        pb.setPrefWidth(400);
        getChildren().addAll(new H1(title),
                new H3(text),
                pb);

        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
    }

    public static Stage show() {
        final Stage stage = new Stage();
        stage.setScene(new Scene(new ProgressWidget()));
        stage.show();
        return stage;
    }

    public static Stage show(String title, String text) {
        final Stage stage = new Stage();
        stage.setScene(new Scene(new ProgressWidget(title, text)));
        stage.show();
        return stage;
    }
}
