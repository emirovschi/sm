package com.emirovschi.sm.lab1.components;

import com.emirovschi.sm.common.Component;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SlideShow extends Component<StackPane>
{
    private static final String START = "Start";
    private static final String STOP = "Stop";

    private final Workspace workspace;
    private final Button button;
    private final ExecutorService executorService;
    private final AtomicInteger counter;

    private boolean playing;

    public SlideShow(final Workspace workspace, final Window window)
    {
        super("Slideshow", new StackPane());
        this.workspace =  workspace;
        window.setOnCloseRequest(event -> stop());

        executorService = Executors.newSingleThreadExecutor();
        counter = new AtomicInteger();

        button = new Button(START);
        button.setOnAction(event -> toggle());
        button.setMaxWidth(Double.MAX_VALUE);
        add(button);
    }

    private void stop()
    {
        if (playing)
        {
            toggle();
            executorService.shutdown();
        }
    }

    public void toggle()
    {
        if (playing)
        {
            counter.incrementAndGet();
            button.setText(START);
        }
        else
        {
            executorService.execute(new SlideShowRunnable(workspace, this, counter.get()));
            button.setText(STOP);
        }

        playing = !playing;
    }

    public AtomicInteger getCounter()
    {
        return counter;
    }
}
