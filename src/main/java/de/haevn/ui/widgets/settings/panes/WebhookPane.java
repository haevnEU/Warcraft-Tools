package de.haevn.ui.widgets.settings.panes;

import de.haevn.api.DiscordApi;
import de.haevn.utils.AlertUtils;
import de.haevn.utils.NetworkUtils;
import de.haevn.utils.PropertyHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class WebhookPane extends GridPane {

    private final StringProperty webhookLogs = new SimpleStringProperty();
    private final StringProperty webhookRecord = new SimpleStringProperty();
    private final StringProperty webhookPlayerLookup = new SimpleStringProperty();

    public WebhookPane() {
        PropertyHandler.getInstance("config").getOptional(DiscordApi.LOG_KEY).ifPresent(webhookLogs::set);
        PropertyHandler.getInstance("config").getOptional(DiscordApi.RECORDING_KEY).ifPresent(webhookRecord::set);
        PropertyHandler.getInstance("config").getOptional(DiscordApi.PLAYER_LOOKUP_KEY).ifPresent(webhookPlayerLookup::set);

        setHgap(10);
        setVgap(5);

        final TextField tfWebhookLogs = new TextField();
        GridPane.setHgrow(tfWebhookLogs, javafx.scene.layout.Priority.ALWAYS);
        add(new Label("Webhook url"), 0, 0);
        add(tfWebhookLogs, 1, 0);

        tfWebhookLogs.textProperty().bindBidirectional(webhookLogs);
        tfWebhookLogs.textProperty().bindBidirectional(webhookRecord);
        tfWebhookLogs.textProperty().bindBidirectional(webhookPlayerLookup);
        tfWebhookLogs.setOnAction(e -> updateLogWebhook());
    }

    private void updateLogWebhook() {
        if (NetworkUtils.isUrl(webhookLogs.get()) || webhookLogs.get().isEmpty()) {
            PropertyHandler.getInstance("config").set(DiscordApi.LOG_KEY, webhookLogs.get());
        } else {
            AlertUtils.showError("Invalid URL", "The URL you entered is not valid.");
        }
    }

    private void updateRecordWebhook() {
        if (NetworkUtils.isUrl(webhookRecord.get()) || webhookRecord.get().isEmpty()) {
            PropertyHandler.getInstance("config").set(DiscordApi.RECORDING_KEY, webhookRecord.get());
        } else {
            AlertUtils.showError("Invalid URL", "The URL you entered is not valid.");
        }
    }


    private void updatePlayerLookupWebhook() {
        if (NetworkUtils.isUrl(webhookPlayerLookup.get()) || webhookPlayerLookup.get().isEmpty()) {
            PropertyHandler.getInstance("config").set(DiscordApi.PLAYER_LOOKUP_KEY, webhookPlayerLookup.get());
        } else {
            AlertUtils.showError("Invalid URL", "The URL you entered is not valid.");
        }
    }
}
