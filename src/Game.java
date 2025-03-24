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

        // Zelda-themed button panel with parchment background
        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                // Parchment/scroll background color
                g2d.setPaint(new Color(222, 208, 175));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Create Zelda-styled buttons
        JButton spawn1000Button = createZeldaButton("Spawn 1_000", "Add 1_000 enemies to the game");
        spawn1000Button.addActionListener(e -> spawnEnemies(1_000));
        buttonPanel.add(spawn1000Button);

        JButton spawn10000Button = createZeldaButton("Spawn 10_000", "Add 10_000 enemies to the game");
        spawn10000Button.addActionListener(e -> spawnEnemies(10_000));
        buttonPanel.add(spawn10000Button);

        JButton spawn100000Button = createZeldaButton("Spawn 100_000", "Add 100_000 enemies to the game");
        spawn100000Button.addActionListener(e -> spawnEnemies(100_000));
        buttonPanel.add(spawn100000Button);
        
        add(buttonPanel, BorderLayout.NORTH);

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

    // Add method to spawn enemies
    private void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            world.spawnEnemy();
        }
        System.out.println("Spawned " + count + " enemies. Total: " + world.getEntities().size());
        memoryView.repaint();
    }

    // Helper method to create Zelda-themed buttons
    private JButton createZeldaButton(String text, String tooltip) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw button background
                Color goldColor = new Color(218, 165, 32);
                Color darkGoldColor = new Color(184, 134, 11);
                
                if (getModel().isPressed()) {
                    g2d.setColor(darkGoldColor.darker());
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else if (getModel().isRollover()) {
                    g2d.setColor(goldColor.brighter());
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g2d.setColor(goldColor);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                
                // Draw pixel-like border (classic NES look)
                g2d.setColor(Color.BLACK);
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.setColor(new Color(139, 69, 19)); // Brown border
                g2d.drawRect(2, 2, getWidth() - 5, getHeight() - 5);
                
                // Add bevel effect
                g2d.setColor(new Color(255, 215, 0)); // Light gold for top/left edges
                g2d.drawLine(1, 1, getWidth() - 2, 1); // Top
                g2d.drawLine(1, 1, 1, getHeight() - 2); // Left
                
                g2d.setColor(new Color(139, 101, 8)); // Dark gold for bottom/right edges
                g2d.drawLine(1, getHeight() - 2, getWidth() - 2, getHeight() - 2); // Bottom
                g2d.drawLine(getWidth() - 2, 1, getWidth() - 2, getHeight() - 2); // Right
                
                // Draw text with pixel-like font
                g2d.setColor(new Color(72, 61, 139)); // Dark blue-purple for text (classic Zelda)
                g2d.setFont(new Font("Courier New", Font.BOLD, 14)); // Monospaced font for pixel-like look
                
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                
                g2d.drawString(getText(), (getWidth() - textWidth) / 2, 
                              (getHeight() + textHeight / 2) / 2);
                
                g2d.dispose();
            }
        };
        
        // Configure button appearance
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
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
                ((Enemy) entity).move(world);
            }
        }
    }

    private synchronized void render() {
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
