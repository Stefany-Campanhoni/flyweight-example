package noflyweight;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class MemoryUsageView extends JPanel {
    private final GameWorld world;
    private final Runtime runtime = Runtime.getRuntime();
    private final NumberFormat format = NumberFormat.getInstance();

    public MemoryUsageView(GameWorld world) {
        this.world = world;
        setPreferredSize(new Dimension(300, 150));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 12));

        int totalObjects = world.getWidth() * world.getHeight() + 1 + world.getEntities().size();

        // Calculate memory usage
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();

        int loadedSprites = this.world.getSpriteCount();

        // Display statistics
        int y = 20;
        g.drawString("FLYWEIGHT PATTERN MEMORY ECONOMY", 10, y);
        y += 20;
        g.drawString("Total game objects: " + totalObjects, 10, y);
        y += 15;
        g.drawString("Unique sprite objects: " + loadedSprites, 10, y);
        y += 20;
        g.drawString("Current memory: " + (usedMemory / 1024 / 1024) + " MB", 10, y);

        // Draw memory bar
        y += 15;
        int barWidth = getWidth() - 20;
        int barHeight = 15;
        double memoryRatio = (double)usedMemory / runtime.totalMemory();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(10, y, barWidth, barHeight);

        g.setColor(memoryRatio > 0.8 ? Color.RED : Color.GREEN);
        g.fillRect(10, y, (int)(barWidth * memoryRatio), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(10, y, barWidth, barHeight);
    }
}
