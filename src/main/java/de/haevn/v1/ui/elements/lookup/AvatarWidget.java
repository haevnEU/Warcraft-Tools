package de.haevn.v1.ui.elements.lookup;

import de.haevn.v1.model.lookup.PlayerLookupModel;
import de.haevn.v1.ui.elements.html.AH1;
import de.haevn.v1.ui.elements.html.H4;
import de.haevn.v1.ui.elements.html.VDIV;
import de.haevn.v1.utils.MathUtils;
import de.haevn.v1.utils.ScoreUtils;
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
        String score = MathUtils.numberToRoundText(ScoreUtils.getInstance().getScoreForCurrentSeason(player));
        imageView.setImage(new Image(player.getThumbnailUrl()));
        lbName.setText(player.getName() + "-" + player.getRealm() + " (" + score + ")");
        lbName.setLink(player.getProfileUrl());
        lbClassInfo.setText(player.getActiveSpecName() + " " + player.getCharacterClass());
        lbFactionInfo.setText(player.getRace() + " " + player.getFaction());

    }
}
