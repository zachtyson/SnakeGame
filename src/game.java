import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

//SNAKE GAME
//CURRENTLY THE ONLY WORKING FUNCTION IS ADDING BODY PARTS


public class game {

    JFrame frame;
    Action upMovement;
    Action downMovement;
    Action leftMovement;
    Action rightMovement;

    //I decided to do AbstractAction instead of ActionListener for movements.
    //I don't remember why I chose that honestly

    ArrayList<snakeBody> body = new ArrayList<>(); //Each square on the snake's body is going to be a label, which is part of an array of each square

    int parts = 0; //Number of body parts, used for the addBodyPart function
    game() {
        frame = new JFrame("Snake game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(400,400));
        frame.pack();
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);


        body.add(new snakeBody(0,0)); //Creates the "head" of the snake
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

        frame.add(body.get(0));
        frame.setVisible(true);
    }

    //All of these are just keybindings for movement, and are identical otherwise
    //Optimization very possible, but I wanna get this to work
    public class UpMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveSnake(1, 20);
            System.out.println(body.get(0).getX() + " " + body.get(0).getY());
            if(checkIfSameTile(body.get(0))) {
                body.get(0).setBackground(Color.RED);
                addBodyPart(1);

            }else {
                body.get(0).setBackground(Color.GREEN);
            }
        }
    }
    public class DownMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveSnake(2, 20);
            System.out.println(body.get(0).getX() + " " + body.get(0).getY());
            if(checkIfSameTile(body.get(0))) {
                body.get(0).setBackground(Color.RED);
                addBodyPart(2);
            } else {
                body.get(0).setBackground(Color.GREEN);
            }
        }
    }
    public class LeftMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveSnake(3, 20);
            System.out.println(body.get(0).getX() + " " + body.get(0).getY());
            if(checkIfSameTile(body.get(0))) {
                body.get(0).setBackground(Color.RED);
                addBodyPart(3);
            }else {
                body.get(0).setBackground(Color.GREEN);
            }
        }
    }
    public class RightMovement extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveSnake(4, 20);
            System.out.println(body.get(0).getX() + " " + body.get(0).getY());
            if(checkIfSameTile(body.get(0))) {
                body.get(0).setBackground(Color.RED);
                addBodyPart(4);
            }else {
                body.get(0).setBackground(Color.GREEN);
            }
        }
    }
    public void addBodyPart(int a) { //Adds additional squares behind the snake, adding each one to the body ArrayList
        int x = 0; //X coordinate
        int y = 0; //Y coordinate
                    // both defaulted to zero
        //1 = up, 2 = down, 3 = left, 4 = right for int a
        if(a < 3) {
            y = 20;  //if a = 1 or 2 then it means vertical
            if(a == 1) { //1 means upwards movement
                y = -y;
            }
        }
        if(a > 2) {
            x = -20;//if a = 3 or 4 then it means horizontal movement
            if(a == 3) { //3 means left movement
                x = -x;
            }
        }

        body.add(new snakeBody(body.get(body.size()-1).getX() + x , body.get(body.size()-1).getY() + y));
        parts++;
        frame.add(body.get(parts));
    }
    public boolean checkIfSameTile(snakeBody labelCheck){
        return labelCheck.getX() == 200 && labelCheck.getY() == 200; //This is just for testing to see if the body part movements work, at 200,200 it counts as an "apple" or food source like the original game
    }
    public void moveSnake(int direction, int distance) {
        //1 = up, 2 = down, 3 = left, 4 = right
        int x = 0;
        int y = 0;
        if(direction < 3) {
            y = distance;
            if(direction == 1) {
                y = -y;
            }
        }
        if(direction > 2) {
            x = distance;
            if(direction == 3) {
                x = -x;
            }
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
    }
}


class snakeBody extends JLabel{ //Again, each snake part is a JLabel, with coordinates attached to it under this class
    snakeBody(int x, int y) {
        this.setBackground(Color.GREEN);
        this.setBounds(x, y, 20, 20);
        this.setOpaque(true);
    }
    public void setCoordinates(int x, int y) { //this is just for the head of the snake, to move it relative to the direction pressed
        this.setLocation(this.getX() + x, this.getY() + y);
    }
    public void moveUp(int x, int y) { //This moves the rest of the snake body parts, honestly a little unnecessary since you can just directly do it, but I did this for sake of readability
        this.setLocation(x,y);
    }
}


