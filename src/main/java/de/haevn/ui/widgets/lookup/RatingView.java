package de.haevn.ui.widgets.lookup;

import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.ui.widgets.html.H1;
import de.haevn.utils.ScoreUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class RatingView extends BorderPane {

    private PlayerLookupModel model;
    private final TextField tfKeystoneLevel = new TextField();
    private final Label lblRating = new Label();
    private final Label lblRatingDescription = new Label();
    private final Label lbFormula = new Label();
    public RatingView() {
        setTop(new H1("Home"));
        final GridPane root = new GridPane();

        root.setHgap(10);
        root.setVgap(10);
        final ColumnConstraints col1 = new ColumnConstraints();
        final ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(20);
        col2.setPercentWidth(80);
        root.getColumnConstraints().addAll(col1, col2);

        root.add(new Label("Desired keystone level:"), 0, 0);
        root.add(tfKeystoneLevel, 1, 0);

        root.add(new Label("Rating:"), 0, 1);
        root.add(lblRating, 1, 1);

        root.add(new Label("Description:"), 0, 2);
        root.add(lblRatingDescription, 1, 2);

        root.add(new Label("Formula:"), 0, 3);
        root.add(lbFormula, 1, 3);

        tfKeystoneLevel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfKeystoneLevel.setText(newValue.replaceAll("\\D", ""));
            } else {
                calculate();
            }
        });
        setCenter(root);
    }


    private void calculate() {
        if (tfKeystoneLevel.getText().isEmpty() || null == model) {
            return;
        }

        int level = Integer.parseInt(tfKeystoneLevel.getText());

        ScoreUtils.RatingResult result = ScoreUtils.getInstance().rateScoreForCurrentSeason(model, level);

        lbFormula.setText(result.getFormula());
        lblRatingDescription.setText(result.getDescription());
        lblRating.setText(String.valueOf(result.getRating()));
    }

    public void setPlayer(PlayerLookupModel player) {
        this.model = player;
        calculate();
    }
}
