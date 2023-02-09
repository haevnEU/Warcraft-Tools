package de.haevn.ui.views;

import de.haevn.abstraction.IView;
import de.haevn.api.GitHubApi;
import de.haevn.ui.utils.CheckBoxGroup;
import de.haevn.ui.utils.Creator;
import de.haevn.ui.widgets.html.A;
import de.haevn.ui.widgets.html.ClearInput;
import de.haevn.ui.widgets.html.ErrorLabel;
import de.haevn.ui.widgets.html.H1;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class PremadeGroupFilterView extends BorderPane implements IView {

    private final Label lbTimeError = new ErrorLabel("");
    private final TextField tfMaxTime = new TextField();
    private final ClearInput tfQuery = new ClearInput();
    private final CheckBox cbDeclined = new CheckBox("Not declined");
    private final CheckBoxGroup dungeonGroup = new CheckBoxGroup();
    private final ToggleGroup groupMode = new ToggleGroup();
    private final ToggleGroup groupDifficulty = new ToggleGroup();
    private final TextField customQuery = new TextField();

    public PremadeGroupFilterView() {
        VBox root = new VBox();
        final FlowPane dungeonPane = new FlowPane();
        final FlowPane modePane = new FlowPane();
        final FlowPane difficultyPane = new FlowPane();

        root.getChildren().addAll(
                new H1("Premade Group Filter"),
                tfQuery,
                Creator.generateTitledPane("Premade Group Filter", dungeonPane, true),
                Creator.generateTitledPane("Max age in minutes", new FlowPane(tfMaxTime, lbTimeError), true),
                Creator.generateTitledPane("Other", new FlowPane(cbDeclined, new HBox(new A("Custom Query(click for doc)", "https://github.com/0xbs/premade-groups-filter/wiki/Keywords#difficulty"), customQuery)), true),
                Creator.generateTitledPane("Mode", modePane, false),
                Creator.generateTitledPane("Difficulty", difficultyPane, false)
        );

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        setCenter(scrollPane);

        customQuery.setMaxWidth(Double.MAX_VALUE);

        GitHubApi.getInstance().getDungeons().addListener((observable, oldValue, newValue) ->
                newValue.forEach((shortName, name) -> {
                    CheckBox cb = new CheckBox(name);
                    cb.setUserData(shortName);
                    dungeonGroup.addCheckBox(cb);
                    dungeonPane.getChildren().add(cb);
                }));

        modePane.getChildren().addAll(FXCollections.observableArrayList(
                generateRadioButton("Normal", "", groupMode, true),
                generateRadioButton("Push", "playstyle == 3", groupMode, false),
                generateRadioButton("Weekly", "playstyle == 2", groupMode, false)
        ));

        difficultyPane.getChildren().addAll(FXCollections.observableArrayList(
                generateRadioButton("none", "", groupDifficulty, true),
                generateRadioButton("normal", "normal", groupDifficulty, false),
                generateRadioButton("heroic", "heroic", groupDifficulty, false),
                generateRadioButton("mythic", "mythic", groupDifficulty, false),
                generateRadioButton("mythic+", "mythicplus", groupDifficulty, false),
                generateRadioButton("2v2", "arena2v2", groupDifficulty, false),
                generateRadioButton("3v3", "arena3v3", groupDifficulty, false)
        ));

        groupMode.selectedToggleProperty().addListener((observable, ignore, value) -> generate());
        tfMaxTime.textProperty().addListener((observable, ignore, value) -> generate());
        cbDeclined.setOnAction(e -> generate());
        dungeonGroup.bind((observable, ignore, value) -> generate());
        groupDifficulty.selectedToggleProperty().addListener((observable, ignore, value) -> generate());
        customQuery.textProperty().addListener((observable, ignore, value) -> generate());

        tfMaxTime.setText("2");
        cbDeclined.setSelected(true);
        tfQuery.addOnClearClicked(e -> reset());
        generate();
    }

    private void reset() {
        tfMaxTime.setText("");
        cbDeclined.setSelected(false);
        dungeonGroup.reset();
        groupMode.selectToggle(groupMode.getToggles().get(0));
        groupDifficulty.selectToggle(groupDifficulty.getToggles().get(0));
        customQuery.setText("");
    }

    private void generate() {
        final List<String> query = new ArrayList<>(List.of("partyfit"));
        final List<String> dungeons = dungeonGroup.getSelectedUserData().stream().map(Object::toString).toList();
        final String mode = groupMode.getSelectedToggle().getUserData().toString();
        final String difficulty = groupDifficulty.getSelectedToggle().getUserData().toString();


        if (!dungeons.isEmpty()) {
            query.add("(" + String.join(" or ", dungeons) + ")");
        }

        if (!tfMaxTime.getText().isEmpty()) {
            if (tfMaxTime.getText().matches("\\d+")) {
                query.add("age < " + tfMaxTime.getText());
                lbTimeError.setText("");
            } else {
                lbTimeError.setText("Invalid time, must be a number");
            }
        }

        if (cbDeclined.isSelected()) {
            query.add("not declined");
        }

        if (!mode.isEmpty()) {
            query.add(mode);
        }

        if (!difficulty.isEmpty()) {
            query.add(difficulty);
        }

        if (!customQuery.getText().isEmpty()) {
            query.add("(" + customQuery.getText() + ")");
        }

        tfQuery.setText(String.join(" and ", query));
    }

    private RadioButton generateRadioButton(String title, String userData, ToggleGroup group, boolean isDefault) {
        RadioButton rb = new RadioButton(title);
        rb.setSelected(isDefault);
        rb.setUserData(userData);
        group.getToggles().add(rb);
        return rb;
    }
}
