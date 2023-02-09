package de.haevn.debug;

import de.haevn.Launcher;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.widgets.html.A;
import de.haevn.ui.widgets.html.H1;
import de.haevn.ui.widgets.html.H2;
import de.haevn.utils.ExceptionUtils;
import de.haevn.utils.JsonAndStringUtils;
import de.haevn.utils.PropertyHandler;
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

public class ExceptionWidget extends AbstractMessageWindow {
    private final ExceptionJson json = new ExceptionJson();
    private final BorderPane root = new BorderPane();
    private final TextArea stacktrace = new TextArea();
    private final TextArea lbMessage = new TextArea();

    private final Button btClose = new Button("Close");
    private final HBox bottom = new HBox();

    private ExceptionWidget(Throwable throwable) {
        stacktrace.setEditable(false);
        stacktrace.setMaxHeight(Double.MAX_VALUE);
        lbMessage.setMaxWidth(Double.MAX_VALUE);

        VBox top = new VBox();
        top.setSpacing(10);

        top.getChildren().addAll(
                new H1("FATAL EXCEPTION"),
                new Label("An exception occurred.\nPlease open a new BugReport and append the following information."),
                new A("Press here to start a bug report", "https://github.com/nimile/WarcraftTools/issues/new?template=crash-report.md")
        );

        final Button btCopy = Creator.createButton("Copy content", event -> copy());
        final Button btOpen = Creator.createButton("Open BugReport", event -> {
            copy();
            Launcher.openWebsite("https://github.com/nimile/WarcraftTools/issues/new?template=crash-report.md");
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
        json.version = PropertyHandler.getInstance("config").get("app.version") + " (" + PropertyHandler.getInstance("config").get("app.build") + ")";
        json.stacktrace = ExceptionUtils.getStackTrace(throwable);

        stacktrace.setText(json.version + "\n" + json.time + "\nMessage: " + json.message + "\nStacktrace:\n" + json.stacktrace);

        root.setVisible(true);
    }

    public static void show(Throwable throwable) {
        ExceptionWidget widget = new ExceptionWidget(throwable);
        widget.setOnClose(event -> widget.close());
        widget.show();
    }


    private void copy() {
        json.userMessage = lbMessage.getText();
        JsonAndStringUtils.exportJson(json).ifPresent(JsonAndStringUtils::copy);
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
