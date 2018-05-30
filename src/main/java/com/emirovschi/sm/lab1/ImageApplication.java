package com.emirovschi.sm.lab1;

import com.emirovschi.sm.lab1.components.ColorAdjustment;
import com.emirovschi.sm.common.Component;
import com.emirovschi.sm.lab1.components.FileManager;
import com.emirovschi.sm.lab1.components.Mirroring;
import com.emirovschi.sm.lab1.components.RotationControl;
import com.emirovschi.sm.lab1.components.SizeControl;
import com.emirovschi.sm.lab1.components.SlideShow;
import com.emirovschi.sm.lab1.components.LensDistort;
import com.emirovschi.sm.lab1.components.Workspace;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ImageApplication extends Application
{
    @Override
    public void start(final Stage primaryStage)
    {
        final Workspace workspace = new Workspace(primaryStage);

        final List<Component> toolboxComponents = new ArrayList<>();
        toolboxComponents.add(new FileManager(workspace, primaryStage));
        toolboxComponents.add(new ColorAdjustment(workspace));
        toolboxComponents.add(new RotationControl(workspace));
        toolboxComponents.add(new SlideShow(workspace, primaryStage));
        toolboxComponents.add(new SizeControl(workspace));
        toolboxComponents.add(new LensDistort(workspace));
        toolboxComponents.add(new Mirroring(workspace));

        final ImageApplicationView imageView = new ImageApplicationView(workspace, toolboxComponents);
        final Scene scene = new Scene(imageView, 800, 600);

        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Image Controller");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}