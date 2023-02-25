package de.haevn.ui.elements.currentweek;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CutoffWidget extends GridPane {
    private Label lbScore = new Label();
    private Label lbTotalAchieved = new Label();

    public CutoffWidget() {
        setId("cutoff-widget");

        add(new Label("Score"), 0, 0);
        add(lbScore, 1, 0);
        add(new Label("Total achieved"), 0, 1);
        add(lbTotalAchieved, 1, 1);
    }


    public void setData(String score, String totalPlayer){
        Platform.runLater(() -> {
            lbScore.setText(score);
            lbTotalAchieved.setText(totalPlayer);
        });
    }
}
