package com.mycompany.proyecto_de_graficaion;

import java.util.ArrayList;
import java.util.Random;


public class Logica {
    
    private Random random;
    
    // Variables del avión
    private float planeX;
    private float planeY;
    private float planeSpeed;
    
    // Variables del juego
    private int score;
    private int lives;
    private boolean gameRunning;
    private float obstacleSpeed;
    
    // Lista de obstáculos
    private ArrayList<ObstacleData> obstacles;
    
    // Variables para invencibilidad
    private int invincibilityFrames;
    private boolean invincible;
    private int spawnTimer;
    
    // Clase interna para datos del obstáculo
    public static class ObstacleData {
        public float x, y, z;
        public float size;
        public float[] color;
        
        public ObstacleData(float x, float y, float z, float[] color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.size = 0.5f;
            this.color = color;
        }
    }
    
    public Logica() {
        random = new Random();
        obstacles = new ArrayList<>();
        initGame();
    }
    
    public void initGame() {
        planeX = 0;
        planeY = 0;
        planeSpeed = 0.3f;
        score = 0;
        lives = 3;
        gameRunning = true;
        obstacleSpeed = 0.1f;
        obstacles.clear();
        invincible = false;
        invincibilityFrames = 0;
        spawnTimer = 0;
    }
    
    public void spawnObstacle() {
        float x = (random.nextFloat() * 5) - 2.5f;
        float y = (random.nextFloat() * 4) - 2;
        float z = -12.0f;
        
        float[] color = {
            (random.nextInt(200) + 55) / 255.0f,
            (random.nextInt(200) + 55) / 255.0f,
            (random.nextInt(200) + 55) / 255.0f
        };
        
        obstacles.add(new ObstacleData(x, y, z, color));
    }
    
    public void update() {
        if (!gameRunning) return;
        
        // Actualizar spawn de obstáculos
        spawnTimer++;
        if (spawnTimer > 40 && obstacles.size() < 15) {
            spawnObstacle();
            spawnTimer = 0;
        }
        
        // Actualizar invencibilidad
        if (invincible) {
            invincibilityFrames--;
            if (invincibilityFrames <= 0) {
                invincible = false;
            }
        }
        
        // Actualizar posición de obstáculos
        for (int i = 0; i < obstacles.size(); i++) {
            ObstacleData obs = obstacles.get(i);
            obs.z += obstacleSpeed;
            
            if (obs.z > 10) {
                obstacles.remove(i);
                score++;
                i--;
            }
        }
        
        // Verificar colisiones
        checkCollisions();
    }
    
    private void checkCollisions() {
        if (!gameRunning || invincible) return;
        
        for (int i = 0; i < obstacles.size(); i++) {
            ObstacleData obs = obstacles.get(i);
            float dx = planeX - obs.x;
            float dy = planeY - obs.y;
            float dz = -2 - obs.z;
            float distance = (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
            
            if (distance < 0.8f) {
                lives--;
                if (lives <= 0) {
                    gameRunning = false;
                } else {
                    invincible = true;
                    invincibilityFrames = 60;
                }
                obstacles.remove(i);
                break;
            }
        }
    }
    
    public void moveLeft() { planeX -= planeSpeed; }
    public void moveRight() { planeX += planeSpeed; }
    public void moveUp() { planeY += planeSpeed; }
    public void moveDown() { planeY -= planeSpeed; }
    
    public void clampPosition() {
        planeX = Math.max(-3.2f, Math.min(3.2f, planeX));
        planeY = Math.max(-2.0f, Math.min(2.0f, planeY));
    }
    
    // Getters
    public float getPlaneX() { return planeX; }
    public float getPlaneY() { return planeY; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public boolean isGameRunning() { return gameRunning; }
    public boolean isInvincible() { return invincible; }
    public int getInvincibilityFrames() { return invincibilityFrames; }
    public ArrayList<ObstacleData> getObstacles() { return obstacles; }
}