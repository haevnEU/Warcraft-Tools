package de.haevn.ui.widgets.settings.panes;

import de.haevn.ui.elements.html.A;
import de.haevn.ui.elements.html.H3;
import de.haevn.utils.PropertyHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class HelpPane extends GridPane {
    public HelpPane() {
        setHgap(10);
        setVgap(5);
        setPadding(new Insets(10));

        final String repositoryUrl = PropertyHandler.getInstance("urls").get("settings.repo");
        final String issueUrl = PropertyHandler.getInstance("urls").get("settings.support.issue");
        final String featureUrl = PropertyHandler.getInstance("urls").get("settings.support.feature");


        add(new H3("Support"), 0, 0, 2, 1);

        add(new A("Repository", repositoryUrl), 0, 1, 2, 1);

        add(new A("BugReport", issueUrl), 0, 2, 2, 1);
        add(new A("Feature Request", featureUrl), 0, 3, 2, 1);


        add(new H3("Author"), 0, 4);
        add(new Label("Haevn"), 1, 4);

        add(new Label("Discord"), 0, 5);
        add(new A("Discord server", "https://discord.gg/pTnNVYv4Wd"), 1, 5);

        add(new Label("Twitter"), 0, 6);
        add(new A("@haevneu", "https://twitter.com/haevneu"), 1, 6);

        add(new Label("Twitch"), 0, 7);
        add(new A("haevneu", "https://www.twitch.tv/haevneu"), 1, 7);

        add(new Label("Youtube"), 0, 8);
        add(new A("@haevneu", "https://www.youtube.com/@haevneu"), 1, 8);
    }
}
