package com.emirovschi.sm.common;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Component<T extends Pane> extends StackPane
{
    private static final String TITLE_STYLE =
            "-fx-background-color: #f4f4f4;\n" +
            "-fx-translate-y: -10;\n";

    private static final String COMPONENT_STYLE =
            "-fx-content-display: top;\n" +
            "-fx-border-insets: 10 0 0 0;\n" +
            "-fx-border-color: #909090;\n" +
            "-fx-border-radius: 3;\n" +
            "-fx-border-width: 1;\n";

    private static final String PARENT_STYLE =
            "-fx-padding: 10 10 10 10;";

    private final T parent;

    public Component(final String title, final T parent)
    {
        this.parent = parent;

        final Label titleLabel = new Label(" " + title + " ");
        titleLabel.setStyle(TITLE_STYLE);
        StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);

        final StackPane contentPane = new StackPane();
        contentPane.setStyle(PARENT_STYLE);
        contentPane.getChildren().add(parent);

        setStyle(COMPONENT_STYLE);
        getChildren().addAll(titleLabel, contentPane);
    }

    protected void add(final Node node)
    {
        parent.getChildren().add(node);
    }

    protected T getContent()
    {
        return parent;
    }
}
