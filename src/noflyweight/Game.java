package noflyweight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class Game extends JFrame implements Runnable {
    // Adjusted window dimensions
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TILE_SIZE = 16;

    private final GameWorld world;
    private final Canvas gameCanvas;
    private final MemoryUsageView memoryView;
    private Timer updateTimer;

    private boolean running = false;
    private Thread gameThread;

    public Game() {
        setTitle("Zelda-like Game with Flyweight Pattern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());

        gameCanvas = new Canvas();
        gameCanvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        gameCanvas.setFocusable(true);
        add(gameCanvas, BorderLayout.CENTER);

        // Initialize game world with new dimensions
        world = new GameWorld(WIDTH/TILE_SIZE, HEIGHT/TILE_SIZE);

        memoryView = new MemoryUsageView(world);
        add(memoryView, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

        // Add keyboard input for player movement
        gameCanvas.addKeyListener(new KeyAdapter() {
            // In the KeyAdapter:
            @Override
            public void keyPressed(KeyEvent e) {
                Player player = world.getPlayer();
                int speed = 5;

                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        player.move(0, -speed, world);
                        break;
                    case KeyEvent.VK_DOWN:
                        player.move(0, speed, world);
                        break;
                    case KeyEvent.VK_LEFT:
                        player.move(-speed, 0, world);
                        break;
                    case KeyEvent.VK_RIGHT:
                        player.move(speed, 0, world);
                        break;
                }
            }


        });

        gameCanvas.createBufferStrategy(2);

        updateTimer = new Timer(1000, e -> memoryView.repaint());
        updateTimer.start();

        setVisible(true);
    }

    public synchronized void start() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Game loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                update();
                delta--;
            }

            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    // In the update method:
    private void update() {
        // Update game logic here
        for (GameObject entity : world.getEntities()) {
            if (entity instanceof Enemy) {
                ((Enemy) entity).move();
            }
        }
    }

    private void render() {
        BufferStrategy bs = gameCanvas.getBufferStrategy();
        if (bs == null) {
            gameCanvas.createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw tiles
        Tile[][] map = world.getMap();
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                map[y][x].draw(g);
            }
        }

        // Draw entities and player
        for (GameObject entity : world.getEntities()) {
            entity.draw(g);
        }
        world.getPlayer().draw(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.start();
        });
    }
}