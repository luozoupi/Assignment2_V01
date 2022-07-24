package Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Listeners implements ActionListener, MouseListener, MouseMotionListener {
    Graphics2D graph;
    JFrame frame;
    public int Init_X=-1, Init_Y=-1, End_X=-1, End_Y=-1;
    public int StrokeSize=1;
    public Object OP="Line";
    public static Color color = Color.BLACK;
    public String RGB = "0!0!0";
    public String Record;
    public ArrayList<String> ListOfRecord= new ArrayList<>();

    public Listeners() {
    }



    public Listeners(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action Performed!");


        if(e.getActionCommand().equals("")){
            JButton b=(JButton)  e.getSource();
            color = b.getBackground();
        } else if (e.getActionCommand()=="ChooseColor") {
            System.out.println("Choose color activated!");
            final JFrame jframe= new JFrame("Color Panel");
            jframe.setSize(350,450);
            jframe.setLocationRelativeTo(null);
            jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Color Cursor_color= JColorChooser.showDialog(frame,"Choose your favourite color please",null);
            if(Cursor_color!=null){
                color=Cursor_color;
            }
        } else {
            OP= e.getActionCommand();
            if ("Pencil".equals(OP) || "Chat".equals(OP)) {
                Cursor c1 = new Cursor(Cursor.DEFAULT_CURSOR);
                frame.setCursor(c1);
            } else if ("Circle".equals(OP) || "Line".equals(OP) || "Rectangular".equals(OP)) {
                Cursor c2 = new Cursor(Cursor.CROSSHAIR_CURSOR);
                frame.setCursor(c2);
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked!");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed!");
        Init_X=e.getX();
        Init_Y=e.getY();
        if(graph.getColor()!=color){
            graph.setColor(color);
        }

        if(OP=="Pencil"){
            RGB= String.valueOf(color.getRed()+"!"+color.getGreen()+"!"+color.getBlue());
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawLine(Init_X,Init_Y,Init_X,Init_Y);
            Record = "Line!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+Init_X+"!"+Init_Y+"!?";
            ListOfRecord.add(Record);

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse Released!");
        End_X = e.getX();
        End_Y = e.getY();
        RGB= String.valueOf(color.getRed()+"!"+color.getGreen()+"!"+color.getBlue());
        if(OP=="Line"){
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawLine(Init_X,Init_Y,End_X,End_Y);
            Record = "Line!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);
            try{
                Networking.Broadcast("Draw!"+Record);
            }catch (Exception ex){
                System.out.println("Wrong in broadcasting line!");
                ex.printStackTrace();
            }
        } else if (OP=="Circle") {
            graph.setStroke(new BasicStroke(StrokeSize));
            int d= Math.min(Math.abs(Init_X-End_X), Math.abs(Init_Y-End_Y));
            graph.drawOval(Math.min(Init_X, End_X), Math.min(Init_Y,End_Y),d,d);
            Record = "Circle!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+Init_X+"!"+Init_Y+"!?";
            ListOfRecord.add(Record);

        } else if (OP=="Chat") {
            String ChatText = JOptionPane.showInputDialog("Please type what you wanna say.");
            if(ChatText!=null){
                Font f= new Font(null, Font.PLAIN, StrokeSize+5);
                graph.setFont(f);
                graph.drawString(ChatText, End_X,End_Y);
                Record = "Chat!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+Init_X+"!"+Init_Y+"!?";
                ListOfRecord.add(Record);
            }

        } else if (OP=="Rectangular"){
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawRect(Init_X,Init_Y,Math.abs(Init_X-End_X),Math.abs(Init_Y-End_Y));
            Record = "Rectangular!"+ StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);
        }else{
            return;
        } try{
            Networking.Broadcast("Draw!"+Record);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse being dragged!");
        End_X=e.getX();
        End_Y=e.getY();
        RGB= String.valueOf(color.getRed()+"!"+color.getGreen()+"!"+color.getBlue());
        if(OP=="Pencil"){
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawLine(Init_X,Init_Y,End_X,End_Y);
            Record="Pencil!"+ StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);
            Init_Y=End_Y;
            Init_X=End_X;

        }else {
            return;
        }try{
            Networking.Broadcast("Draw!"+Record);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public ArrayList<String> LoadRecord(){
        return ListOfRecord;
    }


    public void setGraph(Graphics graph){this.graph=(Graphics2D) graph;}

    public void UpdateRecord(String NewLine){
        ListOfRecord.add(NewLine);
    }

    public void ClearRecord(){
        ListOfRecord.clear();
    }

    public void SetStroke(int Size){
        StrokeSize=Size;
    }

    public int GetStroke(){return StrokeSize;}
}
