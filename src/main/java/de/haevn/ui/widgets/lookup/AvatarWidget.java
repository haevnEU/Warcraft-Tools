package de.haevn.ui.widgets.lookup;

import de.haevn.model.lookup.PlayerLookupModel;
import de.haevn.ui.widgets.html.AH1;
import de.haevn.ui.widgets.html.H4;
import de.haevn.ui.widgets.html.VDIV;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AvatarWidget extends HBox {
    private final ImageView imageView = new ImageView();
    private final AH1 lbName = new AH1();
    private final Label lbClassInfo = new H4();
    private final Label lbFactionInfo = new H4();

    public AvatarWidget() {
        getChildren().addAll(imageView, new VDIV(lbName, lbClassInfo, lbFactionInfo));
    }


    public void setPlayer(PlayerLookupModel player) {
        imageView.setImage(new Image(player.getThumbnailUrl()));
        lbName.setText(player.getName() + "-" + player.getRealm());
        lbName.setLink(player.getProfileUrl());
        lbClassInfo.setText(player.getActiveSpecName() + " " + player.getCharacterClass());
        lbFactionInfo.setText(player.getRace() + " " + player.getFaction());
    }
}
