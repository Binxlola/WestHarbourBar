package main.java.com.updater.ui;

import javafx.scene.control.Skin;

/**
 * Progress indicator showing a circle that fills
 *
 * @author Andrea Vacondio
 *
 */
public class FillProgressIndicator extends ProgressCircleIndicator {

    public FillProgressIndicator() {
        this.getStylesheets().add("fillprogress.css");
        this.getStyleClass().add("fillindicator");
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FillProgressIndicatorSkin(this);
    }
}