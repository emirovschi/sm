package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.common.Component;
import com.emirovschi.sm.lab3.components.Controls;
import com.emirovschi.sm.lab3.components.Equalizer;
import com.emirovschi.sm.lab3.components.FileManager;
import com.emirovschi.sm.lab3.components.MultiEventHandler;
import com.emirovschi.sm.lab3.components.SampleEffects;
import com.emirovschi.sm.lab3.components.SampleEffectsController;
import com.emirovschi.sm.lab3.components.Spectrum;
import com.emirovschi.sm.lab3.ugens.UGenChain;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import net.beadsproject.beads.core.AudioContext;

public class AdvancedSamplePlayer extends Component<VBox>
{
    public AdvancedSamplePlayer(final String title, final AudioContext audioContext, final MultiEventHandler onClose, final Window primaryStage)
    {
        super(title, new VBox());

        final UGenChain chain = new UGenChain(audioContext);
        final FileManager fileManager = new FileManager(chain, primaryStage);
        final Spectrum spectrum = new Spectrum(chain);
        final Controls controls = new Controls(fileManager, onClose);
        final SampleEffectsController sampleEffectsController = new SampleEffectsController(fileManager, chain);
        final SampleEffects sampleEffects = new SampleEffects(fileManager, sampleEffectsController);
        final Equalizer equalizer = new Equalizer(fileManager, chain);

        chain.getPrevUGens().forEach(audioContext.out::addInput);

        add(fileManager);
        add(controls);
        add(spectrum);
        add(equalizer);
        add(sampleEffects);

        VBox.setVgrow(spectrum, Priority.ALWAYS);
        VBox.setVgrow(equalizer, Priority.ALWAYS);

        getContent().setSpacing(10);
    }
}
