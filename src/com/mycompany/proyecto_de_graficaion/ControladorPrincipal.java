package com.mycompany.proyecto_de_graficaion;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.util.FPSAnimator;


public class ControladorPrincipal implements KeyListener {
    
    private static final String TITLE = "PilotoDahs";
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    private static final int FPS = 60;
    
    private Logica logica;
    private Animacion animacion;
    private JFrame frame;
    private FPSAnimator animator;
    
    public ControladorPrincipal() {
        logica = new Logica();
        animacion = new Animacion(logica);
        animacion.addKeyListener(this);
        setupGUI();
    }
    
    private void setupGUI() {
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        animacion.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        frame.add(animacion, BorderLayout.CENTER);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Iniciar animador
        animator = new FPSAnimator(animacion, FPS, true);
        animator.start();
        
        // Timer para actualizar la lógica del juego (60 veces por segundo)
        Timer gameTimer = new Timer(1000 / FPS, e -> {
            if (logica.isGameRunning()) {
                logica.update();
                animacion.repaint();
            } else {
                animacion.repaint(); // Para mostrar pantalla de Game Over
            }
        });
        gameTimer.start();
        
        System.out.println("=== PilotoDahs - INICIADO ===");
        System.out.println("Controles: Flechas para mover | ESC para reiniciar");
    }
    
    public void restartGame() {
        logica.initGame();
        System.out.println("=== JUEGO REINICIADO ===");
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (logica.isGameRunning()) {
            switch(code) {
                case KeyEvent.VK_LEFT:
                    logica.moveLeft();
                    logica.clampPosition();
                    break;
                case KeyEvent.VK_RIGHT:
                    logica.moveRight();
                    logica.clampPosition();
                    break;
                case KeyEvent.VK_UP:
                    logica.moveUp();
                    logica.clampPosition();
                    break;
                case KeyEvent.VK_DOWN:
                    logica.moveDown();
                    logica.clampPosition();
                    break;
            }
        }
        
        if (code == KeyEvent.VK_ESCAPE && !logica.isGameRunning()) {
            restartGame();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ControladorPrincipal();
        });
    }
}