package com.mycompany.proyecto_de_graficaion;

import java.awt.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;         
import com.jogamp.opengl.util.texture.TextureIO;       
import java.io.File;                                    
import java.io.IOException;                             
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_BLEND;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL2.*;

public class Animacion extends GLJPanel implements GLEventListener {
    
    private GLU glu;
    private GLUT glut;
    private float aspect;
    private Logica logica;
    private Texture obstacleTexture; 
    
    public Animacion(Logica logica) {
        this.logica = logica;
        this.addGLEventListener(this);
        this.setFocusable(true);
        glu = new GLU();
        glut = new GLUT();
    }
    
    // Dibujar sombra en el agua (Sin cambios)
    private void drawShadow(GL2 gl, float x, float z, float size) {
        gl.glDisable(GL_LIGHTING);
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.35f);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glPushMatrix();
        gl.glTranslatef(x, -2.48f, z);
        gl.glScalef(size * 1.3f, 0.02f, size * 1.3f);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();
        
        gl.glDisable(GL_BLEND);
    }
    
    // Dibujar avión COLORIDO (Sin cambios)
    private void drawPlane(GL2 gl) {
        float planeX = logica.getPlaneX();
        float planeY = logica.getPlaneY();
        boolean invincible = logica.isInvincible();
        int invFrames = logica.getInvincibilityFrames();
        
        gl.glDisable(GL_LIGHTING);
        
        gl.glPushMatrix();
        gl.glTranslatef(planeX, planeY, -2);
        
        if (invincible && (invFrames / 5) % 2 == 0) {
            gl.glColor3f(1.0f, 1.0f, 1.0f);
        } else {
            gl.glColor3f(0.0f, 0.4f, 0.9f);
        }
        
        gl.glPushMatrix();
        gl.glScalef(0.9f, 0.35f, 0.45f);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();
        
        gl.glColor3f(0.2f, 0.7f, 1.0f);
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.2f, 0);
        gl.glScalef(1.3f, 0.1f, 0.55f);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.1f, -0.45f);
        gl.glScalef(0.5f, 0.15f, 0.3f);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();
        
        gl.glColor3f(1.0f, 0.6f, 0.0f);
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.18f, 0.2f);
        gl.glScalef(0.45f, 0.2f, 0.35f);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();
        
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glPushMatrix();
        gl.glTranslatef(0.7f, 0.2f, 0);
        gl.glScalef(0.2f, 0.08f, 0.4f);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        gl.glTranslatef(-0.7f, 0.2f, 0);
        gl.glScalef(0.2f, 0.08f, 0.4f);
        glut.glutSolidCube(1.0f);
        gl.glPopMatrix();
        
        gl.glPopMatrix();
        
        drawShadow(gl, planeX, -2.0f, 0.8f);
        
        gl.glEnable(GL_LIGHTING);
    }
    
    // Dibujar obstáculo MODIFICADO PARA SOPORTAR TEXTURA
    private void drawObstacle(GL2 gl, Logica.ObstacleData obs) {
    gl.glDisable(GL_LIGHTING);
    
    gl.glPushMatrix();
    gl.glTranslatef(obs.x, obs.y, obs.z);
    gl.glRotatef(obs.z * 20, 0, 1, 0);
    
    float r = obs.size / 2.0f;
    
    // ACTIVAR TEXTURA 
    if (obstacleTexture != null) {
        gl.glEnable(GL_TEXTURE_2D);
        obstacleTexture.bind(gl);
        
        gl.glDisable(GL_TEXTURE_GEN_S);
        gl.glDisable(GL_TEXTURE_GEN_T);
    }
    
    //  color blanco neutro

    gl.glColor3f(1.0f, 1.0f, 1.0f); 
    
    //  DIBUJAR EL CUBO CARA POR CARA 
    gl.glBegin(GL2.GL_QUADS);
    
    // --- Cara Frontal ---
    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-r, -r,  r);
    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( r, -r,  r);
    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( r,  r,  r);
    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-r,  r,  r);
    
    // --- Cara Trasera ---
    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-r, -r, -r);
    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-r,  r, -r);
    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( r,  r, -r);
    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( r, -r, -r);
    
    // --- Cara Superior ---
    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-r,  r, -r);
    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-r,  r,  r);
    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( r,  r,  r);
    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( r,  r, -r);
    
    // --- Cara Inferior ---
    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-r, -r, -r);
    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( r, -r, -r);
    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( r, -r,  r);
    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-r, -r,  r);
    
    // --- Cara Derecha ---
    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( r, -r, -r);
    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( r,  r, -r);
    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( r,  r,  r);
    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( r, -r,  r);
    
    // --- Cara Izquierda ---
    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-r, -r, -r);
    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-r, -r,  r);
    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-r,  r,  r);
    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-r,  r, -r);
    
    gl.glEnd();
    
    //  DESACTIVAR TEXTURA 
    if (obstacleTexture != null) {
        gl.glDisable(GL_TEXTURE_2D);
    }
    
    gl.glPopMatrix();
    
    drawShadow(gl, obs.x, obs.z, obs.size);
    
    gl.glEnable(GL_LIGHTING);
}
    // Dibujar el MAR 
    private void drawWater(GL2 gl) {
        gl.glDisable(GL_LIGHTING);
        gl.glColor3f(0.05f, 0.2f, 0.5f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-30, -2.5f, -40);
        gl.glVertex3f(30, -2.5f, -40);
        gl.glVertex3f(30, -2.5f, 30);
        gl.glVertex3f(-30, -2.5f, 30);
        gl.glEnd();
        gl.glEnable(GL_LIGHTING);
    }
    
    // Dibujar HUD 
    private void drawHUD(GL2 gl, int width, int height) {
        gl.glDisable(GL_LIGHTING);
        gl.glDisable(GL_DEPTH_TEST);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, width, 0, height);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        
        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.55f);
        gl.glEnable(GL_BLEND);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2f(0, height - 100);
        gl.glVertex2f(width, height - 100);
        gl.glVertex2f(width, height);
        gl.glVertex2f(0, height);
        gl.glEnd();
        gl.glDisable(GL_BLEND);
        
        gl.glColor3f(0.3f, 0.8f, 1.0f);
        gl.glRasterPos2f(width/2 - 65, height - 30);
        String title = "PilotoDahs";
        for (char c : title.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
        
        gl.glColor3f(1.0f, 0.9f, 0.0f);
        gl.glRasterPos2f(20, height - 50);
        String scoreText = "PUNTAJE: " + logica.getScore();
        for (char c : scoreText.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
        
        gl.glColor3f(1.0f, 0.2f, 0.2f);
        gl.glRasterPos2f(20, height - 80);
        String livesText = "VIDAS: " + logica.getLives();
        for (char c : livesText.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
        
        gl.glColor3f(0.5f, 0.8f, 1.0f);
        gl.glRasterPos2f(width - 150, height - 50);
        String controls = "← → ↑ ↓";
        for (char c : controls.toCharArray()) {
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_12, c);
        }
        
        if (logica.isInvincible() && logica.isGameRunning()) {
            gl.glColor3f(1.0f, 1.0f, 0.0f);
            gl.glRasterPos2f(width/2 - 55, 70);
            String invMsg = "INVENCIBLE!";
            for (char c : invMsg.toCharArray()) {
                glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
            }
        }
        
        if (!logica.isGameRunning()) {
            gl.glColor3f(1.0f, 0.2f, 0.2f);
            gl.glRasterPos2f(width/2 - 55, height/2);
            String gameOver = "GAME OVER";
            for (char c : gameOver.toCharArray()) {
                glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
            }
            
            gl.glColor3f(1.0f, 0.8f, 0.2f);
            gl.glRasterPos2f(width/2 - 75, height/2 - 45);
            String finalScore = "PUNTAJE FINAL: " + logica.getScore();
            for (char c : finalScore.toCharArray()) {
                glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
            }
            
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glRasterPos2f(width/2 - 120, height/2 - 85);
            String restart = "PRESIONA ESC PARA REINICIAR";
            for (char c : restart.toCharArray()) {
                glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_12, c);
            }
        }
        
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        gl.glEnable(GL_LIGHTING);
        gl.glEnable(GL_DEPTH_TEST);
    }
    
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        
        gl.glClearColor(0.3f, 0.6f, 0.9f, 1.0f);
        
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glDisable(GL_LIGHTING);
        
        //  CARGAR LA IMAGEN DE TEXTURA 
        try {
            File file = new File("C:/Users/ronis/OneDrive/Documentos/NetBeansProjects/PilotoDahs/textura_bloque.png"); 
            obstacleTexture = TextureIO.newTexture(file, true);
            
           
            obstacleTexture.setTexParameteri(gl, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            obstacleTexture.setTexParameteri(gl, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            
            System.out.println("Textura cargada correctamente.");
        } catch (IOException e) {
            System.err.println("Error al cargar la textura del obstáculo: " + e.getMessage());
        }
        
        System.out.println("=== Juego iniciado - Colores brillantes ===");
    }
    
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        if (height == 0) height = 1;
        aspect = (float) width / height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(55.0f, aspect, 0.3f, 50.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();
        
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        glu.gluLookAt(0, 2.5f, 12, 0, 0, -2, 0, 1, 0);
        
        gl.glDisable(GL_LIGHTING);
        
        drawWater(gl);
        
        for (Logica.ObstacleData obs : logica.getObstacles()) {
            drawObstacle(gl, obs);
        }
        
        drawPlane(gl);
        
        drawHUD(gl, width, height);
        
        gl.glFlush();
    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {}
}