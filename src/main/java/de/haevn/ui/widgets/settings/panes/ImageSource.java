package de.haevn.ui.widgets.settings.panes;

import de.haevn.ui.elements.html.A;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class ImageSource extends VBox {
    public ImageSource() {
        setSpacing(10);
        setPadding(new Insets(10));

        final String toolIcon = "https://www.flaticon.com/de/kostenlose-icons/werkzeuge";
        final String crossIcon = "https://www.flaticon.com/free-icons/trash";
        final String recordingIcon = "https://www.flaticon.com/free-icons/record";
        final String newsPaperIcon = "https://www.flaticon.com/free-icons/paper";
        final String editIcon = "https://www.flaticon.com/free-icons/edit";

        getChildren().add(new A("Werkzeuge Icons erstellt von juicy_fish", toolIcon));
        getChildren().add(new A("Trash icons created by Freepik", crossIcon));
        getChildren().add(new A("Record icons created by Hilmy Abiyyu A.", recordingIcon));
        getChildren().add(new A("Paper icons created by Freepik", newsPaperIcon));
        getChildren().add(new A("Edit icons created by Kiranshastry", editIcon));
    }
}
