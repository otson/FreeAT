/* 
 * Copyright (C) 2017 Otso Nuortimo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package freeat;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
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
public class Main
{

    private static final String MAP_FILE = "mapsquare";
    public static final String NEW_LOCATIONS_FILE = "coordinates.txt";
    public static final String LOCATIONS_FILE = "coordinates.txt";
    public static final String CONNECTIONS_FILE = "connections.txt";
    public static final String PLANE_CONNECTIONS_FILE = "planeConnections.txt";
    public static final String NODE_NAMES = "nodeNames.txt";
    public static final String NODE_TYPES = "types.txt";
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
    static final float[] array = new float[6];

    public static Texture map;

    private static UnicodeFont font;
    

    public static void main(String[] args)
    {
        initDisplay();
        initFont();
        initTextures();
        game = new Game();
        gameLoop();
    }

    private static void initFont()
    {
        Font awtFont = new Font("Calibri", Font.PLAIN, 18); //name, style (PLAIN, BOLD, or ITALIC), size

        font = new UnicodeFont(awtFont.deriveFont(0, 18));

        font.addAsciiGlyphs();
        ColorEffect e = new ColorEffect();

        //e.setColor(java.awt.Color.white);
        font.getEffects()
                .add(e);
        try
        {
            font.loadGlyphs();
        } catch (SlickException e1)
        {
            e1.printStackTrace();
        }
    }

    private static void initDisplay()
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT));
            Display.setTitle("FreeAT");
            Display.create();
        } catch (LWJGLException e)
        {
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

    private static void gameLoop()
    {

        glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();
        glOrtho(0, WINDOW_WIDTH, WINDOW_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        long start;
        Float f;
        long end;
        long total = 0;
        int fps = 0;
        long sinceUpdate = System.nanoTime();

        game.resetGame();
        boolean paused = false;

        while (!Display.isCloseRequested())
        {
            while (Keyboard.next()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_P) {
                paused = true;
                }
                if (Keyboard.getEventKey() == Keyboard.KEY_O) {
                paused = false;
                }
            }
            start = System.nanoTime();
            
            //checkUserInput();
            if(!paused)
                game.processTurn();

            //if (System.nanoTime()- sinceUpdate > 1000000*17) {
            if (Globals.RENDER)
            {
                glClear(GL_COLOR_BUFFER_BIT);
                render();
                renderDebugText();
                Display.update();
                Display.sync(Globals.DISPLAY_FRAME_RATE);

            }
            sinceUpdate = System.nanoTime();
            fps++;
            //}
            end = System.nanoTime();
            total += end - start;

            if (total > 1000000000)
            {
                Display.update();
                Display.setTitle("FPS: " + fps);
                fps = 0;
                total = 0;
            }
        }
        //writer.close();
    }

    private static void render()
    {
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
        game.renderAINodes();
    }

    private static void checkUserInput()
    {
        while (Mouse.next() && Mouse.isButtonDown(0))
        {
            if (Mouse.isButtonDown(0))
            {
                if (Mouse.isButtonDown(0))
                {
//                System.out.println("Wrote id: "+id);
//                writer.println(id+" "+Mouse.getX()+" "+-Mouse.getY());
//                id++;
                }
            }
        }
    }

    private static void initTextures()
    {
        glEnable(GL_TEXTURE_2D);
        map = loadTexture(MAP_FILE);
        map.bind();
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

    }

    public static Texture loadTexture(String key)
    {
        InputStream resourceAsStream = null;// = Main.class
        //.getClassLoader().getResourceAsStream("res/textures/" + key + ".png");

        try
        {
            resourceAsStream = new FileInputStream("C:\\Users\\otso\\Documents\\NetBeansProjects\\FreeAT\\src\\res\\textures\\mapSquare.png");
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            return TextureLoader.getTexture("png", resourceAsStream);

        } catch (IOException ex)
        {
            Logger.getLogger(Main.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private static void renderDebugText()
    {
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
        for (int i = 0; i < PublicInformation.PLAYER_COUNT; y += 20f, i++)
        {
            font.drawString(x, y, "Player " + i + " " + PublicInformation.getName(i) + " cash: " + PublicInformation.getBalance(i) + " Debug: " + game.getPlayers().get(i).getDebugString());
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
