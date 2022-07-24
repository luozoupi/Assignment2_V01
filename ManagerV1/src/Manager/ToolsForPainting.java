package Manager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ToolsForPainting extends JPanel {

    private static final long serialVersionUID=1L;

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


//                        case "Pencil":
//                            Stroke = Integer.parseInt(LoadedRecord[1]);
//                            g.setStroke(new BasicStroke(Stroke));
//                            RGB_r=Integer.parseInt(LoadedRecord[2]);
//                            RGB_g=Integer.parseInt(LoadedRecord[3]);
//                            RGB_b=Integer.parseInt(LoadedRecord[4]);
//                            color = new Color(RGB_r, RGB_g, RGB_b);
//                            g.setColor(color);
//                            Init_X = Integer.parseInt(LoadedRecord[5]);
//                            Init_Y = Integer.parseInt(LoadedRecord[6]);
//                            End_X = Integer.parseInt(LoadedRecord[7]);
//                            End_Y = Integer.parseInt(LoadedRecord[8]);
//                            break;



                        case "Circle":
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
                            int d=Math.min(Math.abs(Init_X-End_X), Math.abs(End_X-End_Y));
                            g.drawOval(Math.min(Init_X,Init_X),Math.min(End_X,End_Y),d,d);
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
                            double theta=Math.PI/3*2;
                            int X2=(int)(Init_X*Math.cos(theta)+Init_Y*Math.sin(theta));
                            int Y2=(int)(Init_X*Math.cos(theta)-Init_Y*Math.sin(theta));
                            int X3=(int)(X2*Math.cos(theta)+Y2*Math.sin(theta));
                            int Y3=(int)(X2*Math.cos(theta)-Y2*Math.sin(theta));
                            Polygon filledPolygon = new Polygon();
                            filledPolygon.addPoint(Init_X,Init_Y);
                            filledPolygon.addPoint(X2,Y2);
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
