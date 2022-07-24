package Guest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class GuestGUI {

    public static JFrame frame;

    static GuestGUI JoinWhiteboard;
    private JButton lineButton;
    private JButton pencilButton;
    private JButton circleButton;
    private JButton rectangularButton;
    private JButton triangleButton;
    private JButton chatButton;
    private JComboBox comboBox1;
    private JPanel Canvas0;
    public JPanel GuestPanel;
    public JTextArea ChatArea;
    private JButton moreColorButton;
    private JButton quitButton;
    private JScrollPane JScrollPane1;
    private JScrollPane JScrollpane1;

    static Listeners Listener;

    static ToolsForPainting GuestPainter;

    public JList List;
    private JButton pushChatButton;
    private JLabel GuestLabel;

    public static Networking Node;

    public String GuestName;

    static String Address;
    static int Port;
    static String UserName;
    public static Socket Guestsocket;

    public static int wd,hgh;
    public  static int x,y;

    public static String To_waive="Chat area:";

    public GuestGUI(Networking Node){
        this.Node=Node;
        INIT();

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                try{
//                    Node.Dout.writeUTF("ClientGoAway!"+UserName);
//                    Node.Dout.flush();
//                }catch(IOException ee){
//                    ee.printStackTrace();
//                }

                System.out.println("Quiting the Guest UI!");
                System.exit(1);
            }
        });
        ChatArea.addInputMethodListener(new InputMethodListener() {
            @Override
            public void inputMethodTextChanged(InputMethodEvent event) {
//                String talking = ChatArea.getText().toString();
//
//                try{
//                    Node.Dout.writeUTF("Chat!"+talking+"!"+GuestName);
//                    Node.Dout.flush();
//                    System.out.println("Chat message sent from "+GuestName);
//                }catch (IOException e){
//                    System.out.println("Fail to send chat message to the server!");
//                    e.printStackTrace();
//                }

            }

            @Override
            public void caretPositionChanged(InputMethodEvent event) {

            }
        });
        pushChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String talking = ChatArea.getText().toString();
                talking = talking.replace(To_waive, "");
                if(talking.contains("\r\n")){
                    talking = talking.replace("\r\n"," ");
                }

                ChatArea.append("\r\n");
                To_waive = ChatArea.getText().toString();

                if (talking.equals("")) {
                    JOptionPane.showMessageDialog(null, "Your input is null! Pleas type in something!", "Error when the typein is null", JOptionPane.INFORMATION_MESSAGE);
                }else
                {
                try {
                    Node.Dout.writeUTF("Chat!" + talking + "!" + UserName);
                    Node.Dout.flush();
                    System.out.println("Chat message sent from " + UserName);
                } catch (IOException ee) {
                    System.out.println("Fail to send chat message to the server!");
                    ee.printStackTrace();
                }
            }


            }
        });
    }
    public GuestGUI(Networking Node, String GuestName){
        this.Node=Node;
        this.GuestName=GuestName;
        INIT();
    }


    private void INIT() {

        moreColorButton.setActionCommand("ChooseColor");
        lineButton.setActionCommand("Line");
        rectangularButton.setActionCommand("Rectangular");
        circleButton.setActionCommand("Circle");
        pencilButton.setActionCommand("Pencil");
        triangleButton.setActionCommand("Triangle");
        chatButton.setActionCommand("Chat");
        quitButton.setActionCommand("Quit");

        lineButton.addActionListener(Listener);
        circleButton.addActionListener(Listener);
        chatButton.addActionListener(Listener);
        rectangularButton.addActionListener(Listener);
        pencilButton.addActionListener(Listener);
        triangleButton.addActionListener(Listener);
        moreColorButton.setActionCommand("ChooseColor");
        moreColorButton.addActionListener(Listener);


        GuestPainter=new ToolsForPainting();
        wd=GuestPainter.getWidth();
        hgh=GuestPainter.getHeight();
        GuestPainter.setBackground(Color.WHITE);
        GuestPainter.LoadList(Listener.LoadRecord());
        Canvas0.add(GuestPainter);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                Component comp=e.getComponent();
                x=comp.getX()+166;
                y=comp.getY()+74;

            }
        });
        GuestPainter.addMouseListener(Listener);
        GuestPainter.addMouseMotionListener(Listener);
        Listener.setGraph(GuestPainter.getGraphics());
        GuestLabel.setText(UserName+"'s Window");
         //List = new JList();
         String ID=UserName;
         GuestName=ID;
         String[] Users={ID};
         List.setListData(Users);
        try{
            Node.Dout.writeUTF("Start!11");
            Node.Dout.flush();
        }catch(Exception e){
            e.printStackTrace();
        }


    }




    public static void main(String[] args) {
        if(args.length==3){
            try{
                Address=args[0];
                Port=Integer.parseInt(args[1]);
                UserName=args[2];
            }catch (Exception e){
                System.out.println("CMD error!");
                System.exit(1);
            }
        }else{
            System.out.println("Boot by default");
            Address="localhost";
            Port=3005;//3005
            UserName="YueboGuest";
        }

        try{
            Guestsocket = new Socket(Address,Port);

        }catch (IOException e){
            System.out.println("Network blows up!");
            e.printStackTrace();
            System.exit(1);
        }
        Node = new Networking(Guestsocket);
        System.out.println("Node is set!");
        EventQueue.invokeLater(()->{
            try{
                Listener = new Listeners();

                System.out.println("Guest go!");
                try{
                    Node.Dout.writeUTF("Request!"+UserName);
                    System.out.println("Request sent!");
                    int t=0;
                    while(Node.GetState().equals("Waiting")&&t<10000){
                        TimeUnit.MILLISECONDS.sleep(100);
                        t+=100;
                    }
                    //String str1=Node.Din.readUTF();
                   // String[] Reply=str1.split("!",2);
                    //Node.GuestGo();
                    String Response=Node.GetState();
                     System.out.println("Current state: "+Node.GetState());
                    //String Response=Reply[1];
                    //System.out.println("Reply from the manager: "+Reply[0]+" "+Reply[1]);

                        if(Response.equals("No")){
                            Node.State="No";
                            JOptionPane.showMessageDialog(null,"Sorry, your request has been declined!");
                            Node.Reset();
                            Guestsocket.close();
                            System.exit(1);
                        } else if (Response.equals("Rejected")) {
                            Node.State="Rejected";
                            JOptionPane.showMessageDialog(null,"Sorry, you are rejected by the manager!");
                            try{
                                Node.Dout.writeUTF("Over");
                                Node.Dout.flush();
                                Guestsocket.close();
                                System.exit(1);
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }else if (Response.equals("Yes")){
                            Node.State="Yes";
                            try{
                                frame = new JFrame("GuestGUI");
                                Listener=new Listeners(frame);
                                JoinWhiteboard = new GuestGUI(Node);
                                frame.setContentPane(JoinWhiteboard.GuestPanel);
                                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                frame.pack();
                                frame.setVisible(true);
                                Listener.setGraph(GuestPainter.getGraphics());
                                JoinWhiteboard.ChatArea.append("\r\n");
                                wd=GuestPainter.getWidth();
                                hgh=GuestPainter.getHeight();


                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }




                }catch(Exception e){

                }
                        }catch (Exception e){
                e.printStackTrace();
            }
        });
       // Node.GuestGo();
        System.out.println("Guest go!");
        Node.GuestGo();



    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
