import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.HashSet;

public class demo {

   public static class Polkadot {
      private double myX; // x and y coordinates of center
      private double myY;
      private double myDiameter;
      private Color myColor;
      private double myRadius;
      private String name;

      // constructors
      public Polkadot() // default constructor
      {
         name = "";
         myX = 200;
         myY = 200;
         myDiameter = 25;
         myColor = Color.RED;
         myRadius = myDiameter / 2;
      }

      public Polkadot(double x, double y, double d, Color c) // 4 arg constructor
      {
         name ="";
         myX = x;
         myY = y;
         myDiameter = d;
         myColor = c;
         myRadius = d / 2;
      }

      protected void setName(String name) {
         this.name = name;
      }

      // accessor methods
      public double getX() // 5 accessor methods
      {
         return myX;
      }

      public double getY() {
         return myY;
      }

      public double getDiameter() {
         return myDiameter;
      }

      public Color getColor() {
         return myColor;
      }

      public double getRadius() {
         return myRadius;
      }

      // modifier methods // 5 modifier methods
      public void setX(double x) {
         myX = x;
      }

      public void setY(double y) {
         myY = y;
      }

      public void setColor(Color c) {
         myColor = c;
      }

      public void setDiameter(double d) {
         myDiameter = d;
         myRadius = d / 2;
      }

      public void setRadius(double r) {
         myRadius = r;
         myDiameter = 2 * r;
      }

      // instance methods
      public void jump(int rightEdge, int bottomEdge) {
         // moves location to random (x, y) within the edges
         myX = (Math.random() * (rightEdge - myDiameter) + myRadius);
         myY = (Math.random() * (bottomEdge - myDiameter) + myRadius);
      }

      public void draw(Graphics myBuffer) {
         myBuffer.setColor(myColor);
         myBuffer.fillOval((int) (getX() - getRadius()), (int) (getY() - getRadius()), (int) getDiameter(),
               (int) getDiameter());
         if (name != "") {
            Font font = myBuffer.getFont();
            FontMetrics metrics = myBuffer.getFontMetrics(font);
            int label_x = (int)getX() - metrics.stringWidth(name) / 2;
            int label_y = (int)(getY() - getDiameter());
             myBuffer.setColor(Color.blue);
            myBuffer.drawString(name, label_x, label_y);
         }
      }
   }

   public static class Enemy extends Polkadot {
      private double dx; // pixels to move each time step() is called.
      private double dy;

      // constructors
      public Enemy() // default constructor
      {
         super(200, 200, 50, Color.BLACK);
         dx = dy = 0;
         setName("Student");
      }

      public Enemy(double x, double y, double dia, Color c) {
         super(x, y, dia, c);
         dx = dy = 0;
         setName("Student");
      }

      // modifier methods
      public void setdx(double x) {
         dx = x;
      }

      public void setdy(double y) {
         dy = y;
      }

      // accessor methods
      public double getdx() {
         return dx;
      }

      public double getdy() {
         return dy;
      }

      // instance methods

      public void move(double rightEdge, double bottomEdge) {
         setX(getX() + dx); // move horizontally
         setY(getY() + dy);

         if (getX() >= rightEdge - getRadius()) // hit right edge
         {
            setX(rightEdge - getRadius());
            dx = dx * -1;
         }

         if (getY() >= bottomEdge - getRadius()) // hit bottom edge
         {
            setY(bottomEdge - getRadius());
            dy = dy * -1;
         }

         if (getX() < 0 + getRadius()) {
            setX(0 + getRadius());
            dx = dx * -1;
         }

         if (getY() < 0 + getRadius()) {
            setY(0 + getRadius());
            dy = dy * -1;
         }

      }

   }

   public static class TA extends Enemy {
      private double dx; // pixels to move each time step() is called.
      private double dy;

      public TA(int x, int y, int dia, Color color) {
         super(x, y, dia, color);
         setName("TA");
      }

      public void specialmove(double rightEdge, double bottomEdge, double ballx, double bally) {

         if (ballx > getX()) {
            dx = 0.25;
         } else {
            dx = -0.25;
         }
         if (bally > getY()) {
            dy = 0.25;
         } else {
            dy = -0.25;
         }

         setX(getX() + dx); // move horizontally
         setY(getY() + dy);

         if (getX() >= rightEdge - getRadius()) // hit right edge
         {
            setX(rightEdge - getRadius());
            dx = dx * -1;
         }

         if (getY() >= bottomEdge - getRadius()) // hit bottom edge
         {
            setY(bottomEdge - getRadius());
            dy = dy * -1;
         }

         if (getX() < 0 + getRadius()) {
            setX(0 + getRadius());
            dx = dx * -1;
         }

         if (getY() < 0 + getRadius()) {
            setY(0 + getRadius());
            dy = dy * -1;
         }

      }
   }

   public static class demoPanel extends JPanel {

      private static final int FRAME = 400; // private fields
      private static final Color BACKGROUND = new Color(204, 204, 204);
      HashSet<Integer> pressed_keys;

      private BufferedImage myImage;
      private Graphics myBuffer;
      private Enemy ball;
      private TA pd;
      private Timer t;
      private int hits;

      public demoPanel() // constructor
      {
         pressed_keys = new HashSet<>();
         myImage = new BufferedImage(FRAME, FRAME, BufferedImage.TYPE_INT_RGB);
         myBuffer = myImage.getGraphics();
         myBuffer.setColor(BACKGROUND);
         myBuffer.fillRect(0, 0, FRAME, FRAME);
         int xPos = (int) (Math.random() * (FRAME - 100) + 50);
         int yPos = (int) (Math.random() * (FRAME - 100) + 50);

         int xPos2 = (int) (Math.random() * (FRAME - 100) + 50);
         int yPos2 = (int) (Math.random() * (FRAME - 100) + 50);

         addMouseListener(new Mouse());
         addKeyListener(new Key());
         setFocusable(true);

         ball = new Enemy(xPos, yPos, 50, Color.black);
         pd = new TA(xPos2, yPos2, 20, Color.red);

         t = new Timer(3, new Listener());
         t.start();
         hits = 0;

      }

      public void paintComponent(Graphics g) {

         g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);

      }

      private class Key extends KeyAdapter {
         @Override
         public void keyPressed(KeyEvent e) {
            pressed_keys.add(e.getKeyCode());
         }

         @Override
         public void keyReleased(KeyEvent e) {
            pressed_keys.remove(e.getKeyCode());
         }
      }

      private boolean is_key_pressed(int key_code) {
         return pressed_keys.contains(key_code);
      }

      private class Mouse extends MouseAdapter {
         public void mouseClicked(MouseEvent e) {
            if (!e.isMetaDown() && !e.isShiftDown()) {
               pd.setX(e.getX());
               pd.setY(e.getY());
            }

            else if (e.isMetaDown()) {
               ball.setX(e.getX());
               ball.setY(e.getY());
            } else if (e.isShiftDown()) {
               t.setDelay((int) (897 * Math.random() + 3));
               ball.setdx(-1 * ball.getdx());
            }

         }
      }

      private class Listener implements ActionListener {
         public void actionPerformed(ActionEvent e) {

            myBuffer.setColor(BACKGROUND);
            myBuffer.fillRect(0, 0, FRAME, FRAME);

            int x_velocity, y_velocity;
            boolean w_pressed = is_key_pressed(KeyEvent.VK_W);
            boolean s_pressed = is_key_pressed(KeyEvent.VK_S);
            boolean a_pressed = is_key_pressed(KeyEvent.VK_A);
            boolean d_pressed = is_key_pressed(KeyEvent.VK_D);

            if (w_pressed ^ s_pressed) {
               if (w_pressed) {
                  y_velocity = -1;
               } else {
                  y_velocity = 1;
               }
            } else {
               y_velocity = 0;
            }
            if (a_pressed ^ d_pressed) {
               if (a_pressed) {
                  x_velocity = -1;
               } else {
                  x_velocity = 1;
               }
            } else {
               x_velocity = 0;
            }  
            ball.setdx(x_velocity);
            ball.setdy(y_velocity);

            ball.move(FRAME, FRAME);
            pd.specialmove(FRAME, FRAME, ball.getX(), ball.getY());
            updated(ball, pd);
            collide(ball, pd);

            ball.draw(myBuffer);
            pd.draw(myBuffer);

            myBuffer.setColor(Color.BLACK);
            myBuffer.setFont(new Font("Monospaced", Font.BOLD, 24));
            myBuffer.drawString("Count: " + hits, FRAME - 150, 25);
            repaint();

         }
      }

      private void updated(Enemy b, TA pd) {
         pd.setdx(b.getdx());
         pd.setdy(b.getdy());
      }

      private void collide(Enemy b, TA pd) {
         double ballX = ball.getX();
         double ballY = ball.getY();
         double pdX = pd.getX();
         double pdY = pd.getY();

         double d = distance(ballX, ballY, pdX, pdY);

         if (d <= pd.getRadius() + ball.getRadius()) {
            hits = hits + 1;
            pd.jump(FRAME, FRAME);
         }

      }

      private double distance(double x1, double y1, double x2, double y2) {
         return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
      }

   }

   public static void main(String[] args) {
      JFrame frame = new JFrame("Unit2, Lab10: Polka Dots");
      frame.setSize(400, 400);
      frame.setLocation(0, 0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(new demoPanel());
      frame.setVisible(true);
   }
}
