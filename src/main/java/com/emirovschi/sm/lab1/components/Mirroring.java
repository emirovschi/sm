package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;

public class Mirroring extends Component<VBox>
{
    private static final String TOP = "Top";
    private static final String BOTTOM = "Bottom";
    private static final String LEFT = "Left";
    private static final String RIGHT = "Right";

    private final Workspace workspace;
    private final RadioButton firstSide;
    private final RadioButton secondSide;
    private final RadioButton vertical;
    private final RadioButton horizontal;
    private final Slider cut;

    public Mirroring(final Workspace workspace)
    {
        super("Mirroring", new VBox());
        this.workspace = workspace;

        final ToggleGroup orientationGroup = new ToggleGroup();
        final ToggleGroup sideGroup = new ToggleGroup();

        firstSide = new RadioButton(LEFT);
        firstSide.setToggleGroup(sideGroup);
        firstSide.setSelected(true);
        
        secondSide = new RadioButton(RIGHT);
        secondSide.setToggleGroup(sideGroup);

        vertical = new RadioButton("Vertical");
        vertical.setToggleGroup(orientationGroup);
        vertical.setSelected(true);
        vertical.setOnAction(event -> {firstSide.setText(LEFT); secondSide.setText(RIGHT);});

        horizontal = new RadioButton("Horizontal");
        horizontal.setToggleGroup(orientationGroup);
        horizontal.setOnAction(event -> {firstSide.setText(TOP); secondSide.setText(BOTTOM);});

        cut = new Slider(0, 1, 0);

        final Button button = new Button("Mirror");
        button.setOnAction(event -> mirror());

        add(vertical);
        add(horizontal);
        add(firstSide);
        add(secondSide);
        add(cut);
        add(button);
        getContent().setSpacing(10);
    }

    private void mirror()
    {
        final BufferedImage original = SwingFXUtils.fromFXImage(workspace.getCurrentImage().getImage(), null);
        final BufferedImage mirrored = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        int w = original.getWidth();
        int h = original.getHeight();
        int cw = w / 2;
        int ch = h / 2;
        int sw = new Double(cut.getValue() * cw).intValue();
        int sh = new Double(cut.getValue() * ch).intValue();

        int dx1 = horizontal.isSelected() ? 0 : firstSide.isSelected() ? 0 : cw + sw;
        int dy1 = vertical.isSelected() ? 0 : firstSide.isSelected() ? 0 : ch + sh;
        int dx2 = horizontal.isSelected() ? w : firstSide.isSelected() ? cw - sw : w;
        int dy2 = vertical.isSelected() ? h : firstSide.isSelected() ? ch - sh : h;

        int sx1 = horizontal.isSelected() ? 0 : firstSide.isSelected() ? w - 2 * sw : cw + sw;
        int sy1 = vertical.isSelected() ? 0 : firstSide.isSelected() ? h - 2 * sh : ch + sh;
        int sx2 = horizontal.isSelected() ? w : firstSide.isSelected() ? cw - sw : 2 * (cw + sw) - w;
        int sy2 = vertical.isSelected() ? h : firstSide.isSelected() ? ch - sh : 2 * (ch + sh) - h;

        mirrored.getGraphics().drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
        mirrored.getGraphics().drawImage(original, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

        workspace.getCurrentImage().setImage(SwingFXUtils.toFXImage(mirrored, null));
    }
}
