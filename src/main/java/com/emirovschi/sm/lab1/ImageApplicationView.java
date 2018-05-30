package com.emirovschi.sm.lab1;

import com.emirovschi.sm.common.Component;
import com.emirovschi.sm.lab1.components.FileManager;
import com.emirovschi.sm.lab1.components.Workspace;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ImageApplicationView extends HBox
{

    public ImageApplicationView(
            final Workspace workspace,
            final List<Component> toolboxComponents)
    {
        final List<Component> disabledTools = toolboxComponents.stream()
                .filter(component -> !(component instanceof FileManager))
                .collect(toList());

        final VBox toolbox = new VBox();
        toolbox.setSpacing(10);
        toolbox.getChildren().addAll(toolboxComponents);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(toolbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(0, 10, 0, 0));
        scrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        scrollPane.setMinWidth(250);
        scrollPane.setPrefWidth(250);

        setHgrow(scrollPane, Priority.NEVER);
        setHgrow(workspace, Priority.ALWAYS);

        setSpacing(10);
        setPadding(new Insets(10));
        getChildren().add(scrollPane);
        getChildren().add(workspace);

        workspace.getImageObservable().addObserver((observable, value) -> toggleToolBox(workspace, disabledTools));
        toggleToolBox(workspace, disabledTools);
    }

    private void toggleToolBox(final Workspace workspace, final List<Component> disabledTools)
    {
        disabledTools.forEach(component -> component.setDisable(workspace.getCurrentImage() == null));
    }
}
