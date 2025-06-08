package org.example;

import javax.swing.*;
import java.awt.*;

public class MapVisualizer extends JPanel {
    private final TerrainMap terrainMap;
    private final int REACTOR_SIZE = 15; // Zwiększone z 10
    private final int CITY_SIZE = 12;    // Zwiększone z 8
    private JLabel windLabel;
    private JLabel stepLabel;
    private int currentStep = 0;

    public MapVisualizer(TerrainMap terrainMap) {
        this.terrainMap = terrainMap;
        setPreferredSize(new Dimension(terrainMap.getWidth(), terrainMap.getHeight()));
        setLayout(new BorderLayout());

        // Panel informacyjny na górze
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        stepLabel = new JLabel("Krok: 0");
        windLabel = new JLabel("Wiatr: " + terrainMap.getWindDirection());
        infoPanel.add(stepLabel);
        infoPanel.add(windLabel);
        add(infoPanel, BorderLayout.NORTH);

        // Timer do odświeżania GUI
        Timer timer = new Timer(1000, e -> {
            windLabel.setText("Wiatr: " + terrainMap.getWindDirection());
            repaint();
        });
        timer.start();
    }

    // Metoda do aktualizacji numeru kroku
    public void updateStep(int step) {
        this.currentStep = step;
        stepLabel.setText("Krok: " + step);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.ORANGE);
        for (City city : terrainMap.getCities()) {
            Reactor reactor = city.getReactor();
            if (reactor != null && reactor.checkActivity()) {
                int[] cityPos = city.getPosition();
                int[] reactorPos = reactor.getPosition();

                // Środek miasta
                int cityX = cityPos[0] + CITY_SIZE / 2;
                int cityY = cityPos[1] + CITY_SIZE / 2;
                // Środek reaktora
                int reactorX = reactorPos[0] + REACTOR_SIZE / 2;
                int reactorY = reactorPos[1] + REACTOR_SIZE / 2;

                g2d.drawLine(cityX, cityY, reactorX, reactorY);
            }
        }

        // Rysowanie reaktorów
        for (Reactor reactor : terrainMap.getReactors()) {
            int[] pos = reactor.getPosition();
            g2d.setColor(reactor.checkActivity() ? Color.BLUE : Color.DARK_GRAY);
            g2d.fillRect(pos[0], pos[1], REACTOR_SIZE, REACTOR_SIZE);
        }

        // Rysowanie miast
        for (City city : terrainMap.getCities()) {
            int[] pos = city.getPosition();
            g2d.setColor(Color.GREEN);
            g2d.fillOval(pos[0], pos[1], CITY_SIZE, CITY_SIZE);
        }

        for (Pollution pollution : terrainMap.getPollutions()) {
            int[] pos = pollution.getPosition();
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(pos[0], pos[1], CITY_SIZE, CITY_SIZE);
        }
    }
}