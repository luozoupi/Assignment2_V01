package Manager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

    public JTextArea Chat0;

    //public JTextArea ChatBox;
    //public JTextArea ChatBox;
    private JButton expelTheUserButton;
    //private JScrollPane ListOfUser;
    private JButton quitButton;
    public JList List1;
    private JButton moreColorButton;
    private JPanel Canvas0;
    private JScrollPane ScrollpaneList;
    private JButton pushChatButton;

    static int Port=3005;
    static String UserName="Default Manager's Name";
    static String Address="localhost";

    static String FileName=".\\white_board.txt";

    public static List<Networking> Network = new ArrayList<>();
    public static List<String> Usernames= new ArrayList<>();

    public static int CursorX, CursorY;

    public static int Width=200,Length=400;

    public static int CID=0;

    public static ToolsForPainting Canvas;

    static Listeners PaintListener;

    public SaveAsDialog SaveAs;
    public OpenDialog OpenIt;

    public static String To_replace="Chat area:";


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
                    SaveAsDialog SaveAs= new SaveAsDialog(CreateWhiteBoard);
                    SaveAs.setSize(Width,Length);
                    SaveAs.setVisible(true);

                } else if (comboBox1.getSelectedItem().toString()=="Open") {
                    OpenDialog OpenIt = new OpenDialog(CreateWhiteBoard);
                    OpenIt.setSize(Width,Length);
                    OpenIt.setVisible(true);

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


        Canvas.setBackground(Color.WHITE);
        Canvas.LoadList(PaintListener.LoadRecord());

        Canvas.addMouseListener(PaintListener);
        Canvas.addMouseMotionListener(PaintListener);
        Canvas0.add(Canvas);
        Width=Canvas0.getWidth();
        Length=Canvas0.getHeight();
        PaintListener.setGraph(Canvas.getGraphics());
        PaintListener.graph=(Graphics2D)(Canvas0.getGraphics());


        String username= UserName;
        String[] names={username};
        List1.setListData(names);

        System.out.println("List's size: "+names.length);
        ManagerLabel.setText(UserName+"'s Window");




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


        expelTheUserButton.addActionListener(e->{
            String oUser=List1.getSelectedValue().toString();
            if (username.equals(oUser)){
                return;
            }
            for(int i=0; i<Network.size();i++){
                Networking baddie=Network.get(i);
                if(oUser.equals(baddie.NID)){
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


        List1.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });
        Chat0.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
             String Talking =CreateWhiteBoard.Chat0.getText().toString();
//             try{
//                 Networking.Broadcast("Chat!"+Talking);
//             }catch (IOException e){
//                 System.out.println("Error in broadcasting chat!");
//                 e.printStackTrace();
//             }

            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {

            }
        });
        Chat0.addComponentListener(new ComponentAdapter() {
        });
        pushChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Talking =CreateWhiteBoard.Chat0.getText().toString();
                Talking=Talking.replace(To_replace,"");
                if(Talking.contains("\r\n")){
                    Talking = Talking.replace("\r\n"," ");
                }

                Chat0.append("\r\n");
                To_replace = Chat0.getText().toString();

                if (Talking.equals("")) {
                    JOptionPane.showMessageDialog(null, "Your input is null! Pleas type in something!", "Error when the typein is null", JOptionPane.INFORMATION_MESSAGE);
                }else{
                try{
                    Networking.Broadcast("Chat!"+Talking+"!"+UserName);
                }catch (IOException ee){
                    System.out.println("Error in broadcasting chat!");
                    ee.printStackTrace();
                }
                }

            }
        });
    }

    void setThisFrame(ManagerGUI WB){
        CreateWhiteBoard=WB;
    }

    protected  static void SetupNetwork(int Port, String Username){
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
        FileName=".\\"+filename;
        SaveIt();
    }
    public void SaveIt(){
        System.out.println(PaintListener.LoadRecord());
        //PrintWriter outputStream =null;
        File record0=new File(FileName);
        try{
            record0.createNewFile(); System.out.println("File created:"+FileName);
            //outputStream = new PrintWriter(new FileOutputStream(FileName));
        }catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Errors in saving the file "+FileName);
            return;
        }
        ArrayList<String> ListOfRecord = PaintListener.LoadRecord();
        if(record0.exists()){
            try{
                FileWriter output= new FileWriter(record0);

                for(String line : ListOfRecord){
                    line=line+"\r\n";
                    output.write(line);
                    //outputStream.println(line);
                }
                output.close();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Error in writing files!");

            }

        }
        //outputStream.flush();
        //outputStream.close();
        System.out.println("Save complete!");
    }

    public void SavePic(String filename){
        System.out.println(PaintListener.LoadRecord());
        BufferedImage Pic=new BufferedImage(Width,Length, BufferedImage.TYPE_INT_RGB);
        Graphics2D g=Pic.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0,0,Width,Length);
        ArrayList<String> ListOfRecord=PaintListener.LoadRecord();

        Canvas.Draw(g,ListOfRecord);

        try{
            ImageIO.write(Pic, "JPEG", new File(".\\"+filename));

        }catch (IOException ex){
            System.out.println("Error! In saving JPEG file!");
        }

    }

    public void OpenFile(String filename){
        Scanner inputStream=null;
        try{
            inputStream=new Scanner(new FileInputStream(filename));

        }catch (FileNotFoundException ex){
            System.out.println("Error when opening files!");
            return;
        }
        PaintListener.ClearRecord();
        while(inputStream.hasNextLine()){
            String line = inputStream.nextLine();
            System.out.println("CMD line: "+line);
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
        CreateWhiteBoard=new ManagerGUI();
        frame.setContentPane(CreateWhiteBoard.Panel0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


        PaintListener.setGraph(Canvas.getGraphics());
        Width=Canvas.getWidth();
        Length=Canvas.getHeight();
        CreateWhiteBoard.Chat0.append("\r\n");

            }catch (Exception e){
                System.out.println("Error in initializing ManagerGUI!");
                e.printStackTrace();
            }
        });

        SetupNetwork(Port,UserName);
    }


}
