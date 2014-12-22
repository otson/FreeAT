/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package freeat;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_ALL_ATTRIB_BITS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
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
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvi;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import static org.newdawn.slick.opengl.renderer.SGL.GL_COLOR_BUFFER_BIT;

/**
 *
 * @author otso
 */
public class Main {

    private static final String MAP_FILE = "mapsquare";
    public static final String LOCATIONS_FILE = "src\\res\\coordinates\\coordinates.txt";
    public static final String CONNECTIONS_FILE = "src\\res\\coordinates\\connections.txt";
    public static final String PLANE_CONNECTIONS_FILE = "src\\res\\coordinates\\planeConnections.txt";
    public static final String NODE_NAMES = "src\\res\\coordinates\\nodeNames.txt";

    static int vboHandle;
    static int texHandle;
    static int vertices = 4;
    static int vertexSize = 2;
    static int texSize = 2;
    static boolean left = true;
    public static final int WINDOW_WIDTH = 1368;
    public static final int WINDOW_HEIGHT = 1000;
    static int id = 101;
    static PrintWriter writer = null;
    private static Game game;

    public static Texture map;

    private static UnicodeFont font;

    public static void main(String[] args) {
        initDisplay();
        initFont();
        initTextures();
        game = new Game();
        gameLoop();
    }

    private static void initFont() {
        Font awtFont = new Font("Calibri", Font.PLAIN, 18); //name, style (PLAIN, BOLD, or ITALIC), size

        font = new UnicodeFont(awtFont.deriveFont(0, 18));

        font.addAsciiGlyphs();
        ColorEffect e = new ColorEffect();

        //e.setColor(java.awt.Color.white);
        font.getEffects()
                .add(e);
        try {
            font.loadGlyphs();
        } catch (SlickException e1) {
            e1.printStackTrace();
        }
    }

    private static void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT));
            Display.setTitle("FreeAT");
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
//        try {
//            writer = new PrintWriter("src\\res\\coordinates\\test.txt", "UTF-8");
//        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private static void gameLoop() {

        glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        long start;
        long end;
        long total = 0;
        int fps = 0;
        long sinceUpdate = System.nanoTime();
        while (!Display.isCloseRequested()) {
            start = System.nanoTime();
            glClear(GL_COLOR_BUFFER_BIT);
            checkUserInput();
            game.processTurn();

            //if (System.nanoTime()- sinceUpdate > 1000000*17) {
            render();
            renderDebugText();
            Display.update();
            Display.sync(60);
            sinceUpdate = System.nanoTime();
            fps++;
            //}
            end = System.nanoTime();
            total += end - start;

            if (total > 1000000000) {
                Display.setTitle("FPS: " + fps);
                fps = 0;
                total = 0;
            }
        }
        //writer.close();
    }

    private static void render() {
        map.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);

        glTexCoord2f(1, 0);
        glVertex2f(WINDOW_WIDTH, 0);

        glTexCoord2f(1, 1);
        glVertex2f(WINDOW_WIDTH, WINDOW_HEIGHT);

        glTexCoord2f(0, 1);
        glVertex2f(0, WINDOW_HEIGHT);
        glEnd();

        game.renderTreasures();
        game.renderPlayers();
    }

    private static void checkUserInput() {
        while (Mouse.next() && Mouse.isButtonDown(0)) {
            if (Mouse.isButtonDown(0)) {
                if (Mouse.isButtonDown(0)) {
//                System.out.println("Wrote id: "+id);
//                writer.println(id+" "+Mouse.getX()+" "+-Mouse.getY());
//                id++;
                }
            }
        }
    }

    private static void initTextures() {
        glEnable(GL_TEXTURE_2D);
        map = loadTexture(MAP_FILE);
        map.bind();
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

    }

    public static Texture loadTexture(String key) {
        InputStream resourceAsStream = null;// = Main.class
        //.getClassLoader().getResourceAsStream("res/textures/" + key + ".png");

        try {
            resourceAsStream = new FileInputStream("C:\\Users\\otso\\Documents\\NetBeansProjects\\FreeAT\\src\\res\\textures\\mapSquare.png");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return TextureLoader.getTexture("png", resourceAsStream);

        } catch (IOException ex) {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static void renderDebugText() {
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        glLoadIdentity();
        glDisable(GL_LIGHTING);
        glDisable(GL_LIGHT0);
        glEnable(GL_BLEND);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_TEXTURE_2D);

        float x = 5f;
        float y = 5f;
        for (int i = 0; i < PublicInformation.PLAYER_COUNT; y+=20f, i++) {
            font.drawString(x, y, "Player " + i + " cash: " + PublicInformation.getBalance(i) + " Debug: " + game.getPlayers().get(i).getDebugString());
        }

        glEnable(GL_TEXTURE_2D);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glDisable(GL_BLEND);

        glLoadIdentity();
        map.bind();
        glPopAttrib();
    }

}
