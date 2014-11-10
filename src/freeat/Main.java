/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPLACE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import static org.newdawn.slick.opengl.renderer.SGL.GL_COLOR_BUFFER_BIT;

/**
 *
 * @author otso
 */
public class Main {

    private static final String MAP_FILE = "mapsquare";

    static int vboHandle;
    static int texHandle;
    static int vertices = 4;
    static int vertexSize = 2;
    static int texSize = 2;
    static boolean left = true;
    public static final int WINDOW_WIDTH = 1368;
    public static final int WINDOW_HEIGHT = 1000;

    public static Texture map;

    public static void main(String[] args) {
        try {
            Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT));
            Display.setTitle("FreeAT");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        initTextures();
        gameLoop();
    }

    private static void gameLoop() {

        glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT,0,1,-1);
        glMatrixMode(GL_MODELVIEW);
        long start;
        long end;
        long total = 0;
        int fps = 0;
        while (!Display.isCloseRequested()) {
            start = System.nanoTime();
            glClear(GL_COLOR_BUFFER_BIT);
            checkUserInput();
            render();
            Display.update();
            Display.sync(60);
            end = System.nanoTime();
            total += end - start;
            fps++;
            if (total > 1000000000) {
                Display.setTitle("FPS: " + fps);
                fps = 0;
                total = 0;
            }
        }
    }

    private static void render() {
        map.bind();
        //GL11.glScalef(1*1.4f, 1*1.4f, 1);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);
        
        glTexCoord2f(1, 0);
        glVertex2f(WINDOW_WIDTH,0);
       
        glTexCoord2f(1, 1);
        glVertex2f(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        glTexCoord2f(0, 1);
        glVertex2f(0, WINDOW_HEIGHT);
        glEnd();
        //GL11.glScalef(11.4f, 1/1.4f, 1);
    }

    private static void checkUserInput() {
        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
            }
        }
    }


    private static void initTextures() {
        glEnable(GL_TEXTURE_2D);
        map = loadTexture(MAP_FILE);
        map.bind();
        glTexEnvi( GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        
    }

    public static Texture loadTexture(String key) {
        InputStream resourceAsStream = Main.class
                .getClassLoader().getResourceAsStream("res/textures/" + key + ".png");

        try {
            return TextureLoader.getTexture("png", resourceAsStream);

        } catch (IOException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
