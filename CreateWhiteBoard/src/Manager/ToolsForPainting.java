package Manager;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ToolsForPainting extends JPanel {

    private ArrayList<String> PaintRecord= new ArrayList<String>();

    public void LoadList (ArrayList<String> RecordToBeLoaded){
        PaintRecord=RecordToBeLoaded;
    }

    public void paint(Graphics graph){
        super.paint(graph);
        Draw((Graphics2D) graph, this.PaintRecord);

    }
    public void Draw(Graphics2D g, ArrayList<String> PaintRecord){
            try{
                System.out.println("Draw of ToolsForPainting activated!");
                String[] Record = PaintRecord.toArray(new String[PaintRecord.size()]);
                for (String line: Record){
                    String[] LoadedRecord= line.split("!");
                    int Init_X=-1, Init_Y=-1, End_X=-1, End_Y=-1, Stroke=-1, RGB_r=-1, RGB_g=-1, RGB_b=-1;
                    Color color;

                    if(LoadedRecord[1]=="?"){
                        continue;
                    }

                    switch (LoadedRecord[0]){
                        case "Line":


                        case "Pencil":
                            Stroke = Integer.parseInt(LoadedRecord[1]);
                            g.setStroke(new BasicStroke(Stroke));
                            RGB_r=Integer.parseInt(LoadedRecord[2]);
                            RGB_g=Integer.parseInt(LoadedRecord[3]);
                            RGB_b=Integer.parseInt(LoadedRecord[4]);
                            color = new Color(RGB_r, RGB_g, RGB_b);
                            g.setColor(color);
                            Init_X = Integer.parseInt(LoadedRecord[5]);
                            Init_Y = Integer.parseInt(LoadedRecord[6]);
                            End_X = Integer.parseInt(LoadedRecord[7]);
                            End_Y = Integer.parseInt(LoadedRecord[8]);
                            g.drawLine(Init_X,Init_Y,End_X,End_Y);
                            break;


                        case "Circle":
                            System.out.println("Start drawing circle!");
                            Stroke = Integer.parseInt(LoadedRecord[1]);
                            g.setStroke(new BasicStroke(Stroke));
                            RGB_r=Integer.parseInt(LoadedRecord[2]);
                            RGB_g=Integer.parseInt(LoadedRecord[3]);
                            RGB_b=Integer.parseInt(LoadedRecord[4]);
                            color = new Color(RGB_r, RGB_g, RGB_b);
                            g.setColor(color);
                            Init_X = Integer.parseInt(LoadedRecord[5]);
                            Init_Y = Integer.parseInt(LoadedRecord[6]);
                            End_X = Integer.parseInt(LoadedRecord[7]);
                            End_Y = Integer.parseInt(LoadedRecord[8]);
                            int d=Math.min(Math.abs(Init_X-End_X), Math.abs(Init_Y-End_Y));
                            g.drawOval(Math.min(Init_X, End_X), Math.min(Init_Y,End_Y),d,d);
                            System.out.println("Circle printed!");
                            break;

                        case "Rectangular":
                            Stroke = Integer.parseInt(LoadedRecord[1]);
                            g.setStroke(new BasicStroke(Stroke));
                            RGB_r=Integer.parseInt(LoadedRecord[2]);
                            RGB_g=Integer.parseInt(LoadedRecord[3]);
                            RGB_b=Integer.parseInt(LoadedRecord[4]);
                            color = new Color(RGB_r, RGB_g, RGB_b);
                            g.setColor(color);
                            Init_X = Integer.parseInt(LoadedRecord[5]);
                            Init_Y = Integer.parseInt(LoadedRecord[6]);
                            End_X = Integer.parseInt(LoadedRecord[7]);
                            End_Y = Integer.parseInt(LoadedRecord[8]);
                            g.drawRect(Init_X,Init_Y,Math.abs(Init_X-End_X),Math.abs(Init_Y-End_Y));
                            break;


                        case "Triangle":
                            Stroke = Integer.parseInt(LoadedRecord[1]);
                            g.setStroke(new BasicStroke(Stroke));
                            RGB_r=Integer.parseInt(LoadedRecord[2]);
                            RGB_g=Integer.parseInt(LoadedRecord[3]);
                            RGB_b=Integer.parseInt(LoadedRecord[4]);
                            color = new Color(RGB_r, RGB_g, RGB_b);
                            g.setColor(color);
                            Init_X = Integer.parseInt(LoadedRecord[5]);
                            Init_Y = Integer.parseInt(LoadedRecord[6]);
                            End_X = Integer.parseInt(LoadedRecord[7]);
                            End_Y = Integer.parseInt(LoadedRecord[8]);
                            int length= (int)Math.min(Math.abs(Init_X-End_X), Math.abs(End_X-End_Y));
                            int X3=End_X;
                            int Y3=End_Y+length;
                            Polygon filledPolygon = new Polygon();
                            filledPolygon.addPoint(Init_X,Init_Y);
                            filledPolygon.addPoint(End_X,End_Y);
                            filledPolygon.addPoint(X3,Y3);
                            g.drawPolygon(filledPolygon);
                            break;

                        case "Chat":
                            Stroke = Integer.parseInt(LoadedRecord[1]);
                            g.setStroke(new BasicStroke(Stroke));
                            RGB_r=Integer.parseInt(LoadedRecord[2]);
                            RGB_g=Integer.parseInt(LoadedRecord[3]);
                            RGB_b=Integer.parseInt(LoadedRecord[4]);
                            color = new Color(RGB_r, RGB_g, RGB_b);
                            g.setColor(color);
                            Init_X = Integer.parseInt(LoadedRecord[5]);
                            Init_Y = Integer.parseInt(LoadedRecord[6]);
                            String Chat=LoadedRecord[7];
                            g.drawString(Chat,Init_X,Init_Y);
                            break;

                    }
                }
            } catch (Exception e){
                e.printStackTrace();

            }

    }
}
