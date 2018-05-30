package com.emirovschi.sm.lab3.components;

import com.emirovschi.sm.lab3.ugens.UGenChain;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import net.beadsproject.beads.analysis.FeatureExtractor;
import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.featureextractors.SpectralPeaks;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.TimeStamp;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Spectrum extends StackPane
{
    public Spectrum(final UGenChain chain)
    {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0, 5, 1);

        final ObservableList<XYChart.Data<String, Number>> dataList = FXCollections.observableArrayList();
        final XYChart.Series<String, Number> series = new XYChart.Series<>(dataList);
        final ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList(series);
        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis, seriesList);

        try
        {
            barChart.getStylesheets().add(Spectrum.class.getResource("/chart.css").toURI().toURL().toExternalForm());
        }
        catch (MalformedURLException | URISyntaxException e)
        {
            e.printStackTrace();
        }

        barChart.setMinHeight(0);
        barChart.setAnimated(false);
        barChart.setLegendVisible(false);

        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMaxWidth(1);
        xAxis.setPrefWidth(1);

        yAxis.setOpacity(0);
        yAxis.setMaxWidth(1);
        yAxis.setPrefWidth(1);

        final ShortFrameSegmenter sfs = new ShortFrameSegmenter(chain.getAudioContext());
        final FFT fft = new FFT();
        final PowerSpectrum ps = new PowerSpectrum();
        final SpectralPeaks sp = new SpectralPeaks(chain.getAudioContext(), 10);

        sfs.addInput(chain.getUGen());
        sfs.addListener(fft);
        fft.addListener(ps);
        ps.addListener(sp);
        sp.addListener(new FeatureExtractor<Object, float[][]>()
        {
            @Override
            public void process(final TimeStamp timeStamp, final TimeStamp timeStamp1, final float[][] floats)
            {
                Platform.runLater(() -> {
                    final List<XYChart.Data<String, Number>> data = IntStream.range(0, 10)
                            .boxed()
                            .sorted(Comparator.comparing(i -> floats[i][0]))
                            .map(i -> new XYChart.Data<String, Number>(Integer.toString(i), floats[i][1]))
                            .collect(toList());
                    dataList.clear();
                    dataList.addAll(data);
                });
            }
        });
        chain.getAudioContext().out.addDependent(sfs);

        setMinHeight(50);
        setPrefHeight(50);
        getChildren().add(barChart);
    }
}
