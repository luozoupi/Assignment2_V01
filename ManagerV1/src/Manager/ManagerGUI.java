package Manager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerGUI {

    static ManagerGUI CreateWhiteBoard;
    public JPanel Panel0;


    private JPanel ManagerPanel;
    private JLabel ManagerLabel;
    private JComboBox comboBox1;
    private JButton lineButton;
    private JButton chatButton;
    private JButton circleButton;
    private JButton rectangularButton;
    private JButton pencilButton;
    private JButton triangleButton;
    public JTextPane ChatBox;
    private JButton expelTheUserButton;
    private JScrollPane ListOfUser;
    private JButton quitButton;
    public JList List1;
    private JButton moreColorButton;
    private JPanel Canvas0;
    private ToolsForPainting toolsForPainting1;

    static int Port=3005;
    static String UserName="Default Manager's Name";
    static String Address="localhost";

    static String File="./save/white_board";

    public static List<Networking> Network = new ArrayList<>();
    public static List<String> Usernames= new ArrayList<>();

    public static int CursorX, CursorY;

    public static int Width=-1;

    public static int Length=-1;
    private static int CID=0;

    public static ToolsForPainting Canvas;

    static Listeners PaintListener;

    Graphics2D graph0;

    static int gay=0;

    public ManagerGUI() {

        //Setup(Port,UserName);




        comboBox1.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem().toString()=="New"){
                    Canvas.removeAll();
                    Canvas.updateUI();
                    PaintListener.ClearRecord();
                    try{
                        Networking.Broadcast("New");
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }System.out.println("New whiteboard successfully created!");
                }
                else if(comboBox1.getSelectedItem().toString()=="Save"){
                    SaveIt();
                } else if (comboBox1.getSelectedItem().toString()=="SaveAs") {
                    SaveAsDialog SaveAs= new SaveAsDialog();

                } else if (comboBox1.getSelectedItem().toString()=="Open") {
                    OpenDialog OpenIt = new OpenDialog();

                }
            }


        });

        moreColorButton.setActionCommand("ChooseColor");
        lineButton.setActionCommand("Line");
        rectangularButton.setActionCommand("Rectangular");
        circleButton.setActionCommand("Circle");
        pencilButton.setActionCommand("Pencil");
        triangleButton.setActionCommand("Triangle");
        chatButton.setActionCommand("Chat");
        quitButton.setActionCommand("Quit");

        moreColorButton.addActionListener(PaintListener);
        lineButton.addActionListener(PaintListener);
        rectangularButton.addActionListener(PaintListener);
        circleButton.addActionListener(PaintListener);
        pencilButton.addActionListener(PaintListener);
        triangleButton.addActionListener(PaintListener);
        chatButton.addActionListener(PaintListener);
        quitButton.addActionListener(PaintListener);

        Canvas=new ToolsForPainting();
        Width=Canvas.getWidth();
        Length=Canvas.getHeight();

        Canvas.setBackground(Color.BLUE);
        Canvas.LoadList(PaintListener.LoadRecord());

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Quiting the ManagerGUI");
                System.exit(1);
            }
        });
        Panel0.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                Component comp=e.getComponent();
                CursorX=comp.getX()+166;
                CursorY=comp.getY()+74;
            }
        });

        PaintListener.setGraph(Canvas.getGraphics());
        Canvas.addMouseListener(PaintListener);
        Canvas.addMouseMotionListener(PaintListener);



        List1 = new JList<>();
        String username= UserName;
        String[] names={username};
        List1.setListData(names);
        ManagerLabel.setText(UserName+"'s Window");
        ListOfUser.add(List1);


        expelTheUserButton.addActionListener(e->{
            String oUser=List1.getSelectedValue().toString();
            if (username==oUser){
                return;
            }
            for(int i=0; i<Network.size();i++){
                Networking baddie=Network.get(i);
                if(oUser==baddie.NID){
                    baddie.Oust=true;
                    try{
                        baddie.Dout.writeUTF("Kick!" + baddie.NID);
                        baddie.Dout.flush();
                    }catch(Exception ex){
                        System.out.println("ManagerGUI goes wrong in ousting a persona non grata!");
                        ex.printStackTrace();
                    }
                    try{
                        baddie.socket.close();
                    }catch(Exception ex){
                        System.out.println("ManagerGUI goes wrong in closing a user's socket!");
                        ex.printStackTrace();
                    }
                    Network.remove(i);
                    Usernames.remove(oUser);
                    JOptionPane.showMessageDialog(Panel0, oUser+ " has been ousted from your whiteboard!");
                }
            }

            for(String UID: Usernames){
                oUser += "!"+UID;
            }

            for(int i=0; i<Network.size();i++){
                Networking tk=Network.get(i);
                try{
                    tk.Dout.writeUTF("Delete!"+ oUser);
                    tk.Dout.flush();
                }catch(Exception ex){
                    System.out.println("Error in sending Delete UTF message!");
                    ex.printStackTrace();
                }
            }
            String[] List2=oUser.split("!",2);
            String[] List3=List2[1].split("!");
            List1.setListData(List3);

        });

        Canvas0.addMouseListener(new MouseAdapter() {
            @Override

            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse Clicked "+gay+" times!");
                super.mouseClicked(e);
            }
        });
    }

    protected  static void Setup(int Port, String Username){
        Networking n1 =null;
        ServerSocket s1=null;
        Usernames.add(Username);
        try{
            s1 = new ServerSocket(Port);
            Socket c1;
            System.out.println("Setup function is listening via a socket!");
            while(true){
                System.out.println("ServerSocket Listening!");
                c1 = s1.accept();
                System.out.println("Client Accepted!");
                CID++;
                System.out.println("ID: "+CID+" wants the access to you!");
                n1 = new Networking(c1);
                Network.add(n1);
                n1.start();
            }


        }catch (Exception e){
            System.out.println("Linking Failure! Please check the ManagerGUI class, Setup method!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static int RequestFromGuest(String NameOfClient){
        int Judgment=JOptionPane.showConfirmDialog(null, NameOfClient + " wants to use your shared whiteboard, agree?", "Yes, go forward",JOptionPane.INFORMATION_MESSAGE);

        return Judgment;

    }

    void setPanel(ManagerGUI Whiteboard){
        this.CreateWhiteBoard=Whiteboard;
    }

    public void SaveIt(String filename){
        File="src/"+filename;
        SaveIt();
    }
    public void SaveIt(){
        PrintWriter outputStream =null;
        try{
            outputStream = new PrintWriter(new FileOutputStream(File));

        }catch (IOException ex){
            System.out.println("Errors in opening the file "+File);
            return;
        }
        ArrayList<String> ListOfRecord = PaintListener.LoadRecord();
        for(String line : ListOfRecord){
            outputStream.println(line);
        }
        outputStream.flush();
        outputStream.close();
        System.out.println("Save complete!");
    }

//    public void SavePic(String filename){
//        BufferedImage Pic=new BufferedImage(Width,Length, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g=Pic.createGraphics();
//        g.setColor(Color.WHITE);
//        g.fillRect(0,0,Width,Length);
//        ArrayList<String> ListOfRecord=PaintListener.LoadRecord();
//        Canvas.Draw(g,ListOfRecord);
//
//        try{
//            ImageIO.write(Pic, "JPEG", new File("src/"+filename));
//
//        }catch (IOException ex){
//            System.out.println("Error! In saving JPEG file!");
//        }
//
//    }

    public void OpenIt(String filename){
        Scanner inputStream=null;
        try{
            inputStream=new Scanner(new FileInputStream(filename));

        }catch (IOException ex){
            System.out.println("Error when opening files!");
            return;
        }
        PaintListener.ClearRecord();
        while(inputStream.hasNextLine()){
            String line = inputStream.nextLine();
            PaintListener.UpdateRecord(line);
        }

        try{
            Networking.Broadcast("New");
        }catch(IOException e2){
            e2.printStackTrace();
        }

        ArrayList<String> rl=PaintListener.LoadRecord();
        try{
            Networking.Broadcast_B(rl);
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        Canvas.repaint();
        inputStream.close();
    }

    public static void main(String[] args) {
        if(args.length>=2){
            try{
                Address=args[0];
                Port= Integer.parseInt(args[1]);
                if(args.length==3)
                    UserName=args[2];
            } catch (Exception e){
                System.out.println("The argument you typed in is wrong!");
                e.printStackTrace();
                System.exit(1);
            }


        }
        else{
            System.out.println("Booted by default.");
        }


        EventQueue.invokeLater(()->{
            try {
        JFrame frame = new JFrame("ManagerGUI");
        System.out.println("Window initialized");
        PaintListener= new Listeners(frame);
        frame.setContentPane(new ManagerGUI().Panel0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getContentPane().add(Canvas);
        frame.pack();
        frame.setVisible(true);

            }catch (Exception e){
                System.out.println("Error in initializing ManagerGUI!");
                e.printStackTrace();
            }
        });

        //Setup(Port,UserName);
    }



}
