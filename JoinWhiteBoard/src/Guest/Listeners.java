package Guest;

import Guest.Networking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class Listeners implements ActionListener, MouseListener, MouseMotionListener {
    private Graphics2D graph;
    private  JFrame frame;
    private int Init_X=-1, Init_Y=-1, End_X=-1, End_Y=-1;
    private int StrokeSize=1;
    private Object OP="Line";
    static Color color = Color.BLACK;
    private String RGB = "0!0!0";
    private String Record;

    private String Paint;
    ArrayList<String> ListOfRecord= new ArrayList<String>();

    public Listeners() {
    }

    public Listeners(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("")){
            JButton b=(JButton)  e.getSource();
            color = b.getBackground();
        } else if (e.getActionCommand()=="ChooseColor") {
            final JFrame jframe= new JFrame("Color Panel");
            jframe.setSize(350,450);
            jframe.setLocationRelativeTo(null);
            jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Color Cursor_color= JColorChooser.showDialog(jframe,"Choose your favourite color please",null);
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

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Init_X=e.getX();
        Init_Y=e.getY();
        if(graph.getColor()!=color){
            graph.setColor(color);
        }

        if(OP=="Pencil"){
            RGB= String.valueOf(color.getRed()+"!"+color.getGreen()+"!"+color.getBlue());
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawLine(Init_X,Init_Y,Init_X,Init_Y);
            Record = "Pencil!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+Init_X+"!"+Init_Y+"!?";
            ListOfRecord.add(Record);
            //drawObject(color, x1,y1,this.thickness)

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        End_X = e.getX();
        End_Y = e.getY();
        RGB= String.valueOf(color.getRed()+"!"+color.getGreen()+"!"+color.getBlue());
        if(OP=="Line"){//drawObject(color, x1,y1,this.thickness)
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawLine(Init_X,Init_Y,End_X,End_Y);
            Record = "Line!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);

        } else if (OP=="Circle") {
            graph.setStroke(new BasicStroke(StrokeSize));
            int d= Math.min(Math.abs(Init_X-End_X), Math.abs(Init_Y-End_Y));
            graph.drawOval(Math.min(Init_X, End_X), Math.min(Init_Y,End_Y),d,d);
            Record = "Circle!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);

        } else if (OP=="Chat") {
            String ChatText = JOptionPane.showInputDialog("Please type what you wanna say.");
            if(ChatText!=null){
                Font f= new Font(null, Font.PLAIN, StrokeSize+5);
                graph.setFont(f);
                graph.drawString(ChatText, End_X,End_Y);
                Record = "Chat!"+StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+ChatText+"!?";
                ListOfRecord.add(Record);
            }

        } else if (OP=="Rectangular"){
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawRect(Init_X,Init_Y,Math.abs(Init_X-End_X),Math.abs(Init_Y-End_Y));
            Record = "Rectangular!"+ StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);
        }else if (OP=="Triangle") {
            graph.setStroke(new BasicStroke(StrokeSize));
            int length= (int)Math.min(Math.abs(Init_X-End_X), Math.abs(End_X-End_Y));
            int X3=End_X;
            int Y3=End_Y+length;
            Polygon filledPolygon = new Polygon();
            filledPolygon.addPoint(Init_X,Init_Y);
            filledPolygon.addPoint(End_X,End_Y);
            filledPolygon.addPoint(X3,Y3);
            graph.drawPolygon(filledPolygon);
            Record = "Triangle!"+ StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);
        } else{
            return;
        }
        SendRecord();

    }
    public void SendRecord(){
        try{
            Paint="Draw!"+ Record;
            GuestGUI.JoinWhiteboard.Node.Dout.writeUTF(Paint);
            GuestGUI.JoinWhiteboard.Node.Dout.flush();


        }catch (IOException ee){
            ee.printStackTrace();
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

        }else if (OP=="Eraser") {
            graph.setColor(Color.WHITE);
            graph.setStroke(new BasicStroke(StrokeSize));
            graph.drawLine(Init_X,Init_Y,End_X,End_Y);
            RGB="255!255!255";
            Record="Pencil!"+ StrokeSize+"!"+RGB+"!"+Init_X+"!"+Init_Y+"!"+End_X+"!"+End_Y+"!?";
            ListOfRecord.add(Record);
            Init_Y=End_Y;
            Init_X=End_X;
        }else{
            return;
        }SendRecord();
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
