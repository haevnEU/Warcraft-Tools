package de.haevn.ui.windows;

import de.haevn.ui.elements.html.A;
import de.haevn.ui.elements.html.H1;
import de.haevn.ui.elements.html.H2;
import de.haevn.ui.utils.Creator;
import de.haevn.utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

public class CrashReport extends AbstractWindow {
    private final ExceptionJson json = new ExceptionJson();
    private final BorderPane root = new BorderPane();
    private final TextArea stacktrace = new TextArea();
    private final TextArea lbMessage = new TextArea();

    private final Button btClose = new Button("Close");
    private final HBox bottom = new HBox();

    private CrashReport(Throwable throwable) {
        stacktrace.setEditable(false);
        stacktrace.setMaxHeight(Double.MAX_VALUE);
        lbMessage.setMaxWidth(Double.MAX_VALUE);

        VBox top = new VBox();
        top.setSpacing(10);

        top.getChildren().addAll(
                new H1("FATAL EXCEPTION"),
                new Label("An exception occurred.\nPlease open a new BugReport and append the following information."),
                new A("Press here to start a bug report", "https://github.com/nimile/Warcraft-Tools/issues/new?template=crash-report.md")
        );

        final Button btCopy = Creator.createButton("Copy content", event -> copy());
        final Button btOpen = Creator.createButton("Open BugReport", event -> {
            copy();
            NetworkUtils.openWebsite("https://github.com/nimile/Warcraft-Tools/issues/new?template=crash-report.md");
        });

        root.setTop(top);


        bottom.getChildren().addAll(btCopy, btOpen);
        root.setBottom(bottom);

        VBox center = new VBox(new H2("Message:"), lbMessage, new H2("Information"), stacktrace);
        HBox.setHgrow(lbMessage, Priority.ALWAYS);
        VBox.setVgrow(stacktrace, Priority.ALWAYS);
        root.setCenter(center);
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf("#1e1e1e"), CornerRadii.EMPTY, Insets.EMPTY)));
        if (null != throwable) {
            setException(throwable);
        }

        super.initialize(root);
    }

    public static void show(Throwable throwable) {
        CrashReport widget = new CrashReport(throwable);
        widget.showAndWait();
        Platform.exit();
    }

    public void setOnClose(EventHandler<ActionEvent> event) {
        btClose.setOnAction(event);
        bottom.getChildren().add(btClose);
    }

    public void setException(Throwable throwable) {
        if (null == throwable) {
            return;
        }

        json.message = throwable.getMessage();
        json.time = Date.from(Instant.now()).toString();
        json.version = PropertyHandler.getInstance("config").get(PropertyKeys.APP_VERSION) + " (" + PropertyHandler.getInstance("config").get(PropertyKeys.APP_BUILD) + ")";
        json.stacktrace = ExceptionUtils.getStackTrace(throwable);

        stacktrace.setText(json.version + "\n" + json.time + "\nMessage: " + json.message + "\nStacktrace:\n" + json.stacktrace);

        root.setVisible(true);
    }

    private void copy() {
        json.userMessage = lbMessage.getText();
        SerializationUtils.exportJson(json).ifPresent(CustomStringUtils::copy);
    }


    @Data
    public static final class ExceptionJson {
        private String userMessage;
        private String message;
        private String time;
        private String version;
        private String stacktrace;
    }
}
