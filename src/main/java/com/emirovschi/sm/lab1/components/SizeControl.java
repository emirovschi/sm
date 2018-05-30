package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import javafx.util.converter.BigIntegerStringConverter;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

public class SizeControl extends Component<GridPane>
{
    private final Workspace workspace;
    private final StringConverter<BigInteger> numberConverter;

    private final TextField scaleWidth;
    private final TextField scaleHeight;
    private final TextField cropTop;
    private final TextField cropRight;
    private final TextField cropBottom;
    private final TextField cropLeft;

    public SizeControl(final Workspace workspace)
    {
        super("Size", new GridPane());
        this.workspace = workspace;
        numberConverter = new BigIntegerStringConverter();

        scaleWidth = createTextField("Width");
        scaleHeight = createTextField("Height");
        final Button scale = new Button("Scale");
        scale.setMaxWidth(Double.MAX_VALUE);
        scale.setOnAction(event -> scale());

        cropTop = createTextField("Top");
        cropRight = createTextField("Right");
        cropBottom = createTextField("Bottom");
        cropLeft = createTextField("Left");
        final Button crop = new Button("Crop");
        crop.setMaxWidth(Double.MAX_VALUE);
        crop.setOnAction(event -> crop());

        GridPane.setHgrow(scaleWidth, Priority.ALWAYS);
        GridPane.setHgrow(scaleHeight, Priority.ALWAYS);

        getContent().add(scaleWidth, 0, 0, 2, 1);
        getContent().add(scaleHeight, 2, 0, 2, 1);
        getContent().add(scale, 0, 1, 4, 1);
        getContent().add(cropTop, 0, 2);
        getContent().add(cropRight, 1, 2);
        getContent().add(cropBottom, 2, 2);
        getContent().add(cropLeft, 3, 2);
        getContent().add(crop, 0, 3, 4, 1);
        getContent().setVgap(5);
        getContent().setHgap(10);

        workspace.getImageObservable().addObserver((observable, value) -> updateValues());
    }

    private TextField createTextField(final String prompt)
    {
        final TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setMinWidth(20);
        textField.setPrefWidth(20);
        GridPane.setHgrow(textField, Priority.ALWAYS);
        return textField;
    }

    private TextFormatter<BigInteger> createFormatter(final Predicate<BigInteger> validator)
    {
        return new TextFormatter<>(numberConverter, BigInteger.ZERO, change -> filter(change, validator));
    }

    private void updateValues()
    {
        if (workspace.getCurrentImage() != null)
        {
            scaleWidth.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) > 0));
            scaleHeight.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) > 0));
            cropTop.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) >= 0 && value.compareTo(height().subtract(cropBottomValue())) < 0));
            cropRight.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) >= 0 && value.compareTo(width().subtract(cropLeftValue())) < 0));
            cropBottom.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) >= 0 && value.compareTo(height().subtract(cropTopValue())) < 0));
            cropLeft.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) >= 0 && value.compareTo(width().subtract(cropRightValue())) < 0));

            scaleWidth.setText(width().toString());
            scaleHeight.setText(height().toString());
            cropTop.setText("0");
            cropRight.setText("0");
            cropBottom.setText("0");
            cropLeft.setText("0");
        }
    }

    private BigInteger width()
    {
        return BigInteger.valueOf(((Double) workspace.getCurrentImage().getImage().getWidth()).longValue());
    }

    private BigInteger height()
    {
        return BigInteger.valueOf(((Double) workspace.getCurrentImage().getImage().getHeight()).longValue());
    }

    private BigInteger widthValue()
    {
        return getValue(scaleWidth);
    }

    private BigInteger heightValue()
    {
        return getValue(scaleHeight);
    }

    private BigInteger cropTopValue()
    {
        return getValue(cropTop);
    }

    private BigInteger cropRightValue()
    {
        return getValue(cropRight);
    }

    private BigInteger cropBottomValue()
    {
        return getValue(cropBottom);
    }

    private BigInteger cropLeftValue()
    {
        return getValue(cropLeft);
    }

    private BigInteger getValue(final TextField textField)
    {
        return getValue(textField.getText());
    }

    private BigInteger getValue(final String value)
    {
        return ofNullable(value)
                .filter(text -> text.length() > 0)
                .map(this::fromString)
                .orElse(BigInteger.ZERO);
    }

    private BigInteger fromString(final String value)
    {
        try
        {
            return numberConverter.fromString(value);
        }
        catch (final NumberFormatException exception)
        {
            return null;
        }
    }

    private TextFormatter.Change filter(final TextFormatter.Change change, final Predicate<BigInteger> validator)
    {
        final String newText = change.getControlNewText();

        if (newText.isEmpty())
        {
            return change;
        }

        final BigInteger value = getValue(newText);
        return validator.test(value) ? change : null;
    }

    private void scale()
    {
        redraw(workspace,
                original -> new BufferedImage(widthValue().intValue(), heightValue().intValue(), original.getType()),
                (original, newImage) -> newImage.getGraphics().drawImage(original,
                        0, 0, newImage.getWidth(), newImage.getHeight(),
                        0, 0, original.getWidth(),original.getHeight(), null)
                );
    }

    private void crop()
    {
        redraw(workspace,
                original -> new BufferedImage(
                        width().subtract(cropLeftValue()).subtract(cropRightValue()).intValue(),
                        height().subtract(cropTopValue()).subtract(cropBottomValue()).intValue(), original.getType()),
                (original, newImage) -> newImage.getGraphics().drawImage(original,
                        0, 0, newImage.getWidth(), newImage.getHeight(),
                        cropLeftValue().intValue(), cropTopValue().intValue(), width().subtract(cropRightValue()).intValue(), height().subtract(cropBottomValue()).intValue(),
                        null)
        );
    }

    private void redraw(
            final Workspace workspace,
            final Function<BufferedImage, BufferedImage> copy,
            final BiConsumer<BufferedImage, BufferedImage> transform)
    {
        final BufferedImage original = SwingFXUtils.fromFXImage(workspace.getCurrentImage().getImage(), null);
        final BufferedImage flipped = copy.apply(original);
        transform.accept(original, flipped);
        workspace.getCurrentImage().setImage(SwingFXUtils.toFXImage(flipped, null));
        workspace.setImageViewSize();
        updateValues();
    }
}
