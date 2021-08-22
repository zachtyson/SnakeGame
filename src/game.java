import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

//SNAKE GAME
//ALMOST COMPLETELY FUNCTIONAL

public class game {

    JFrame frame;
    Action upMovement;
    Action downMovement;
    Action leftMovement;
    Action rightMovement;

    JTextArea endGame;
    final int MAP_SIZE = 400;
    final int NUM_TILES = 20;
    Timer timer;
    int movementDirection = 0;
    int score = 0;
    JLabel foodTile;
    //I decided to do AbstractAction instead of ActionListener for movements.
    //I don't remember why I chose that honestly

    ArrayList<snakeBody> body = new ArrayList<>(); //Each square on the snake's body is going to be a label, which is part of an array of each square

    game() {

        frame = new JFrame("Snake game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(MAP_SIZE,MAP_SIZE));
        frame.pack();
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        foodTile = new JLabel();
        foodTile.setBackground(Color.RED);
        foodTile.setOpaque(true);
        foodTile.setBounds(MAP_SIZE/2,MAP_SIZE/2,MAP_SIZE/NUM_TILES,MAP_SIZE/NUM_TILES);

        body.add(new snakeBody(0,0,MAP_SIZE/NUM_TILES, MAP_SIZE/NUM_TILES)); //Creates the "head" of the snake
        upMovement = new UpMovement();
        downMovement = new DownMovement();
        leftMovement = new LeftMovement();
        rightMovement = new RightMovement();
        body.get(0).getInputMap().put(KeyStroke.getKeyStroke("UP"), "upMovement");
        body.get(0).getActionMap().put("upMovement", upMovement);
        body.get(0).getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "downMovement");
        body.get(0).getActionMap().put("downMovement", downMovement);
        body.get(0).getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "leftMovement");
        body.get(0).getActionMap().put("leftMovement", leftMovement);
        body.get(0).getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "rightMovement");
        body.get(0).getActionMap().put("rightMovement", rightMovement);

        //Keybindings for the movement, up down left and right
        frame.getContentPane().setBackground(Color.black);
        frame.add(foodTile);
        frame.add(body.get(0));
        frame.setVisible(true);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                moveSnake(movementDirection, MAP_SIZE/NUM_TILES);
            }
        }, 0, 100);
    }
    

    //All of these are just keybindings for movement, and are identical otherwise
    //Optimization very possible, but I wanna get this to work
    public class UpMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(movementDirection != 2 || body.size() == 1) { //Can't change directions on a time, unless the size is 0
                movementDirection =1;
            }

        }
    }
    public class DownMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(movementDirection != 1 || body.size() == 1) {
                movementDirection =2;
            }
        }
    }
    public class LeftMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(movementDirection != 4 || body.size() == 1) {
                movementDirection =3;
            }
        }
    }
    public class RightMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(movementDirection != 3 || body.size() == 1) {
                movementDirection =4;
            }
        }
    }
    public void addBodyPart(int a) { //Adds additional squares behind the snake, adding each one to the body ArrayList
        int x = 0; //X coordinate
        int y = 0; //Y coordinate
                    // both defaulted to zero
        //1 = up, 2 = down, 3 = left, 4 = right for int a
        if(a < 3) {
            y = MAP_SIZE/NUM_TILES;  //if a = 1 or 2 then it means vertical
            if(a == 1) { //1 means upwards movement
                y = -y;
            }
        }
        if(a > 2) {
            x = -MAP_SIZE/NUM_TILES;//if a = 3 or 4 then it means horizontal movement
            if(a == 3) { //3 means left movement
                x = -x;
            }
        }

        body.add(new snakeBody(body.get(body.size()-1).getX() + x , body.get(body.size()-1).getY() + y, MAP_SIZE/NUM_TILES, MAP_SIZE/NUM_TILES));
        frame.add(body.get(body.size() -1));
    }
    public boolean checkIfSameTile(snakeBody labelCheck){
        if(labelCheck.getX() == foodTile.getX() && labelCheck.getY() == foodTile.getY()) { //Method checks to see if the food is in the same location as the snake head, if it is, then move the food to random spot
            moveFood(); //returns true which would increase snake size
            return true;
        }
        return false;
    }
    public void moveSnake(int direction, int distance) {
        //1 = up, 2 = down, 3 = left, 4 = right
        int x = 0;
        int y = 0;
        if(direction > 2) {
            x = distance;
            if(direction == 3) {
                x = -x;
            }
        } else if(direction != 0) {
            y = distance;
            if(direction == 1) {
                y = -y;
            }
        }
        if(checkIfDead(body.get(0).getX() + x, body.get(0).getY() + y)) {
            return;
        }
        //this for loop moves each body part relative to where the part ahead of it (in the ArrayList) was previously, and goes all the way up to the 0th index
        if(body.size() > 1) {
            for (int i = body.size() -1; i > 0; i--) {
                int y2 = body.get(i-1).getY();
                int x2 = body.get(i-1).getX();
                body.get(i).setLocation(x2,y2);
            }
        }
            body.get(0).setCoordinates(x,y);
        if(checkIfSameTile(body.get(0))) {
            addBodyPart(direction);
        }
        checkIfDead(body.get(0));
    }
    public void moveFood() { //Literally just moves the red panel to a random spot on the frame
        int randX = (int)(Math.random()*MAP_SIZE/NUM_TILES)*MAP_SIZE/NUM_TILES;
        int randY = (int)(Math.random()*MAP_SIZE/NUM_TILES)*MAP_SIZE/NUM_TILES;
        foodTile.setLocation(randX,randY);
        score++;

    }
    public void checkIfDead(snakeBody head) { //Checks to see if the player has lost, either by eating themself or touching the outer boundaries of the map
        if(body.size() > 1) {
            for (int i = 1; i < body.size(); i++) {
                if (head.getX() == body.get(i).getX() && head.getY() == body.get(i).getY()) {
                    timer.cancel();
                    lostGame();
                    return;
                }
            }
        }
        if(head.getX() < 0 || head.getX() >= MAP_SIZE || head.getY() < 0 || head.getY() >= MAP_SIZE) {
            timer.cancel();
            lostGame();
        }
    }
    public boolean checkIfDead(int x, int y) {
        if(x < 0 ||x >= MAP_SIZE || y < 0 || y >= MAP_SIZE) {
            timer.cancel();
            lostGame();
            return true;
        }
        return false;
    }
    public void lostGame(){  //You just lost The Game
        Timer timerEnd = new Timer();


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (snakeBody snakeBody : body) {
                    snakeBody.setVisible(false);
                }
                foodTile.setVisible(false);
                endGame = new JTextArea();
                endGame.setFont(new Font("Trebuchet MS", Font.BOLD, MAP_SIZE/10));
                endGame.setForeground(Color.green);
                endGame.setSize(new Dimension(MAP_SIZE, MAP_SIZE));
                endGame.setBackground(Color.BLACK);
                endGame.setText("GAME OVER \n" + "SCORE: " + score);
                frame.repaint();
                frame.add(endGame);
            } //Removes snake and food, just puts a game over screen
        };

        timerEnd.schedule(task, 2000); //Does this after 2 seconds
    }

}


class snakeBody extends JLabel{ //Again, each snake part is a JLabel, with coordinates attached to it under this class
    snakeBody(int x, int y, int a, int b) {
        this.setBackground(Color.GREEN);
        this.setBounds(x, y, a, b);
        this.setOpaque(true);
    }
    public void setCoordinates(int x, int y) { //this is just for the head of the snake, to move it relative to the direction pressed
        this.setLocation(this.getX() + x, this.getY() + y);
    }
}



