package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;
import javafx.util.converter.BigIntegerStringConverter;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;

public class LensDistort extends Component<GridPane>
{
    private final Workspace workspace;
    private final StringConverter<BigInteger> numberConverter;

    private TextField centerX;
    private TextField centerY;
    private TextField radius;
    private Slider power;

    public LensDistort(final Workspace workspace)
    {
        super("Lens Distort", new GridPane());
        this.workspace = workspace;
        numberConverter = new BigIntegerStringConverter();

        centerX = createTextField("X");
        centerY = createTextField("Y");
        radius = createTextField("Radius");
        power = new Slider(1, 10, 2);

        final Button fishEyeButton = new Button("Distort");
        fishEyeButton.setMaxWidth(Double.MAX_VALUE);
        fishEyeButton.setOnAction(event -> apply(workspace));

        getContent().add(centerX, 0, 0);
        getContent().add(centerY, 1, 0);
        getContent().add(radius, 2, 0);
        getContent().add(power, 0, 1, 3, 1);
        getContent().add(fishEyeButton, 0, 2, 3, 1);

        getContent().setVgap(5);
        getContent().setHgap(10);

        workspace.getImageObservable().addObserver((observable, value) -> updateValues());
    }

    private void updateValues()
    {
        if (workspace.getCurrentImage() != null)
        {
            centerX.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) >= 0 && value.compareTo(width()) < 0));
            centerY.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) >= 0 && value.compareTo(height()) < 0));
            radius.setTextFormatter(createFormatter(value -> value.compareTo(BigInteger.ZERO) >= 0 && value.compareTo(height().max(width())) < 0));

            centerX.setText(width().divide(BigInteger.valueOf(2)).toString());
            centerY.setText(height().divide(BigInteger.valueOf(2)).toString());
            radius.setText(height().max(width()).divide(BigInteger.valueOf(2)).toString());
            power.setValue(2);
        }
    }

    private TextFormatter<BigInteger> createFormatter(final Predicate<BigInteger> validator)
    {
        return new TextFormatter<>(numberConverter, BigInteger.ZERO, change -> filter(change, validator));
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

    private BigInteger width()
    {
        return BigInteger.valueOf(((Double) workspace.getCurrentImage().getImage().getWidth()).longValue());
    }

    private BigInteger height()
    {
        return BigInteger.valueOf(((Double) workspace.getCurrentImage().getImage().getHeight()).longValue());
    }

    private int centerX()
    {
        return getValue(centerX);
    }

    private int centerY()
    {
        return getValue(centerY);
    }

    private int radius()
    {
        return getValue(radius);
    }

    private double power()
    {
        return power.getValue();
    }

    private int getValue(final TextField textField)
    {
        return getValue(textField.getText()).intValue();
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

    private void apply(final Workspace workspace)
    {
        final BufferedImage original = SwingFXUtils.fromFXImage(workspace.getCurrentImage().getImage(), null);
        final BufferedImage distorted = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        distorted.getGraphics().drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);

        int[] pixelDistances = IntStream.range(centerX() - radius(), centerX() + radius())
                .flatMap(x -> getPixel(original, x))
                .toArray();

        int[] fishEyeDistances = fishEye(pixelDistances, radius() * 2, radius() * 2, power());

        IntStream.range(0, radius() * 2)
                .forEach(x -> putPixel(distorted, fishEyeDistances, x));

        workspace.getCurrentImage().setImage(SwingFXUtils.toFXImage(distorted, null));
    }

    private IntStream getPixel(final BufferedImage original, final int x)
    {
        return IntStream.range(centerY() - radius(), centerY() + radius())
                .map(y -> getPixel(original, x, y));
    }

    private int getPixel(final BufferedImage original, final int x, final int y)
    {
        return x >= 0 && x < original.getWidth() && y >=0 && y < original.getHeight() ? original.getRGB(x, y) : 0;
    }

    private void putPixel(final BufferedImage distorted, int[] fishEye, final int x)
    {
        IntStream.range(0, radius() * 2)
                .forEach(y -> putPixel(distorted, fishEye, x, y));
    }

    private void putPixel(final BufferedImage distorted, int[] fishEye, final int x, final int y)
    {
        int position = y * radius() * 2 + x;
        int newX = centerX() - radius() + x;
        int newY = centerY() - radius() + y;

        if (fishEye[position] != Integer.MAX_VALUE && newX >=0 && newX < distorted.getWidth() && newY >= 0 && newY < distorted.getHeight())
        {
            distorted.setRGB(newX, newY, fishEye[position]);
        }
    }

    private static int[] fishEye(int[] srcpixels, double w, double h, double power)
    {
        /*
         *    Fish eye effect
         *    tejopa, 2012-04-29
         *    http://popscan.blogspot.com
         *    http://www.eemeli.de
         */

        // create the result data
        int[] dstpixels = new int[(int) (w * h)];
        // for each row
        for (int y = 0; y < h; y++)
        {
            // normalize y coordinate to -1 ... 1
            double ny = ((2 * y) / h) - 1;
            // pre calculate ny*ny
            double ny2 = ny * ny;
            // for each column
            for (int x = 0; x < w; x++)
            {
                // normalize x coordinate to -1 ... 1
                double nx = ((2 * x) / w) - 1;
                // pre calculate nx*nx
                double nx2 = nx * nx;
                // calculate distance from center (0,0)
                // this will include circle or ellipse shape portion
                // of the image, depending on image dimensions
                // you can experiment with images with different dimensions
                double r = Math.sqrt(nx2 + ny2);
                // discard pixels outside from circle!
                dstpixels[(int) (y * w + x)] = Integer.MAX_VALUE;
                if (0.0 <= r && r <= 1.0)
                {
                    double nr = Math.sqrt(1 - Math.pow(r, power));
                    // new distance is between 0 ... 1
                    nr = (r + (1.0 - nr)) / 2.0;
                    // discard radius greater than 1.0
                    if (nr <= 1.0)
                    {
                        // calculate the angle for polar coordinates
                        double theta = Math.atan2(ny, nx);
                        // calculate new x position with new distance in same angle
                        double nxn = nr * Math.sin(theta);
                        // calculate new y position with new distance in same angle
                        double nyn = nr * Math.cos(theta);
                        // map from -1 ... 1 to image coordinates
                        int x2 = (int) (((nxn + 1) * w) / 2.0);
                        // map from -1 ... 1 to image coordinates
                        int y2 = (int) (((nyn + 1) * h) / 2.0);
                        // find (x2,y2) position from source pixels
                        int srcpos = (int) (y2 * w + x2);
                        // make sure that position stays within arrays
                        if (srcpos >= 0 & srcpos < w * h)
                        {
                            // get new pixel (x2,y2) and put it to target array at (x,y)
                            dstpixels[(int) (y * w + x)] = srcpixels[srcpos];
                        }
                    }
                }
            }
        }
        //return result pixels
        return dstpixels;
    }
}
