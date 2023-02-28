package de.haevn.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class AlertUtils {
    private AlertUtils() {
    }

    public static void showNormal(String title, String text) {
        show(title, text, Alert.AlertType.INFORMATION);
    }

    public static void showWarning(String title, String text) {
        show(title, text, Alert.AlertType.WARNING);
    }

    public static void showError(String title, String text) {
        show(title, text, Alert.AlertType.ERROR);
    }

    public static boolean showConfirmation(String title, String text) {
        var result = show(title, text, Alert.AlertType.CONFIRMATION);
        return !result.getButtonData().isCancelButton();
    }

    public static ButtonType show(String title, String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
        return alert.getResult();
    }
}
