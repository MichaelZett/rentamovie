package de.zettsystems.rentamovie.common.adapter;

import javafx.scene.control.DialogPane;

import java.util.Objects;

/**
 * Applies the application stylesheet to dialogs.
 *
 * <p>Dialogs open in their own window and therefore do not inherit the stylesheets of the main
 * scene; every dialog pane has to attach the stylesheet itself.</p>
 */
public final class Theme {

    /** Classpath location of the application stylesheet. */
    public static final String STYLESHEET = "/stylesheets/rentamovie.css";

    private Theme() {
    }

    /**
     * Attaches the application stylesheet to the given dialog pane.
     *
     * @param dialogPane the pane of the dialog to style
     */
    public static void apply(DialogPane dialogPane) {
        dialogPane.getStylesheets()
                .add(Objects.requireNonNull(Theme.class.getResource(STYLESHEET)).toExternalForm());
    }

}
