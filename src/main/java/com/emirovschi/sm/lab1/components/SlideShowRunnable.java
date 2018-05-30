package com.emirovschi.sm.lab1.components;

import javafx.application.Platform;

public class SlideShowRunnable implements Runnable
{
    private final Workspace workspace;
    private final SlideShow slideShow;
    private final int counter;

    public SlideShowRunnable(final Workspace workspace, final SlideShow slideShow, final int counter)
    {
        this.workspace = workspace;
        this.slideShow = slideShow;
        this.counter = counter;
    }

    @Override
    public void run()
    {
        while (slideShow.getCounter().get() == counter)
        {
            try
            {
                Platform.runLater(workspace::nextImage);
                Thread.sleep(1000);
            }
            catch (final Exception exception)
            {
                slideShow.toggle();
            }
        }
    }
}
