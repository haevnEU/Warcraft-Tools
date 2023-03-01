package de.haevn.ui.widgets.settings.panes;

import de.haevn.api.GitHubApi;
import de.haevn.api.RaiderIOApi;
import de.haevn.logging.LoggerHandler;
import de.haevn.ui.elements.RefreshButton;
import de.haevn.ui.utils.Creator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.Instant;
import java.util.Date;

public class UpdatePane extends GridPane {
    private final Label lbLastRaiderIOUpdate = new Label();
    private final Label lbLastGitHubUpdate = new Label();

    public UpdatePane() {
        setHgap(10);
        setVgap(5);
        setPadding(new Insets(10));

        add(new Label("RaiderIO"), 0, 1);
        add(lbLastRaiderIOUpdate, 1, 1);
        add(new RefreshButton(e -> RaiderIOApi.getInstance().update()), 2, 1);

        add(new Label("GitHub"), 0, 2);
        add(lbLastGitHubUpdate, 1, 2);
        add(new RefreshButton(e -> GitHubApi.getInstance().update()), 2, 2);

        add(Creator.createButton("Flush logs", e -> LoggerHandler.flush()), 0, 3);
    }


    public void onRaiderIOUpdate(Date date) {
        Platform.runLater(() -> {
            if (date == null || date.before(Date.from(Instant.EPOCH))) {
                lbLastRaiderIOUpdate.setText("failed");
            } else {
                lbLastRaiderIOUpdate.setText(date.toString());
            }
        });
    }

    public void onGitHubApiUpdate(Date date) {
        Platform.runLater(() -> {
            if (date == null || date.before(Date.from(Instant.EPOCH))) {
                lbLastGitHubUpdate.setText("failed");
            } else {
                lbLastGitHubUpdate.setText(date.toString());
            }
        });
    }
}
