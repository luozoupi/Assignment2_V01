package Manager;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Networking extends Thread {

    public Socket socket;
    public String NID;

    public DataInputStream Din;

    public DataOutputStream Dout;
    public static boolean Oust=false;

    public Networking(Socket socket){
        this.socket=socket;
    }

    public static synchronized int Turnitin(String ID){
        int lame = ManagerGUI.CreateWhiteBoard.RequestFromGuest(ID);
        return  lame;
    }

    public static void Broadcast(String newRecord) throws IOException{
        for(int i=0; i<ManagerGUI.Network.size();i++){
            Networking Nwk=ManagerGUI.Network.get(i);
            Nwk.Dout.writeUTF(newRecord);
            Nwk.Dout.flush();
        }
    }

    public static void LoadAllUser(String[] CIDs){
        ManagerGUI.CreateWhiteBoard.List1.setListData(CIDs);
    }

    public static void OustClient(String CIDoust){
        System.out.println("Client ousted!");
        String[] OldClientList=CIDoust.split("!",2);
        JOptionPane.showMessageDialog(ManagerGUI.CreateWhiteBoard.Panel0,"The user "+OldClientList[0]+" has left your whiteboard!");
        String[] newClientList = OldClientList[1].split("!");
        ManagerGUI.CreateWhiteBoard.List1.setListData(newClientList);

    }

    public static void RepaintCanvas(String RecordOfOp){
        ManagerGUI.PaintListener.UpdateRecord(RecordOfOp);
        ManagerGUI.CreateWhiteBoard.Canvas.repaint();
    }

    public static void Broadcast_B(ArrayList<String> ListOfRecord)throws IOException{
        String[] ArrayOfRecord= ListOfRecord.toArray(new String[ListOfRecord.size()]);
        for(String msg: ArrayOfRecord){
            for(int i=0; i<ManagerGUI.Network.size(); i++){ //should "ManagerGUI" be excluded?
                Networking Nwk=ManagerGUI.Network.get(i);
                Nwk.Dout.writeUTF("Draw!"+msg);
                Nwk.Dout.flush();
            }
        }
    }

    public void run(){

        try{
            InputStream ips=socket.getInputStream();
            OutputStream ops=socket.getOutputStream();
            Din = new DataInputStream(ips);
            Dout= new DataOutputStream(ops);
            String str1;

            while((str1=Din.readUTF())!=null){
                System.out.println("Message from the guest!: "+ManagerGUI.CreateWhiteBoard.CID+" "+str1);
                String[] output=str1.split("!",2);
                System.out.println("Message from the guest is: "+output[0]+".");
                //System.out.println(output[0]);
                if(output[0].equals("Start")){//Note that it is sent from the client/guest
                    System.out.println("Message:Start!");
                    ArrayList<String> rl=ManagerGUI.PaintListener.LoadRecord(); //Possible issues with PaintLisenter and ManagerGUI, may need to use CreateWhiteBoard
                    try{
                        Broadcast_B(rl);
                    }catch (IOException e){
                        System.out.println("Error! In broadcasting batches in networking, run()");
                        e.printStackTrace();
                    }
                    String str="ListOfUser";
                    for(String userName: ManagerGUI.CreateWhiteBoard.Usernames){
                        str+="!"+userName;
                    }
                    String[] s1=str.split("!",2);
                    String[] CID=s1[1].split("!");
                    LoadAllUser(CID);
                    Broadcast(str);

                } else if (output[0].equals("Request")) {
                    System.out.println("Message: Request!");
                    String cur1=output[1];
                    NID=cur1;
                    if(ManagerGUI.Usernames.contains(cur1)){
                        Dout.writeUTF("Respond!No");
                        Dout.flush();
                        System.out.println("Respond: No!");
                    }else {
                        System.out.println("Optionpane!");
                        ManagerGUI.CreateWhiteBoard.RequestFromGuest(output[1]);
                        int outcome= Turnitin(output[1]);
                        if(outcome== JOptionPane.YES_OPTION){//Check twice to exclude repeated login from the client
                            if(ManagerGUI.Usernames.contains(cur1)){
                                try{
                                    Dout.writeUTF("Respond!No");
                                    Dout.flush();
                                    System.out.println("Respond: No!");
                                    ManagerGUI.Network.remove(this);
                                    ManagerGUI.CreateWhiteBoard.CID-=1;
                                    socket.close();
                                    break;
                                }catch(Exception e){
                                    ManagerGUI.Network.remove(this);
                                    e.printStackTrace();
                                }

                            }else{
                                ManagerGUI.Usernames.add(cur1);
                                Dout.writeUTF("Respond!Yes");
                                Dout.flush();
                                System.out.println("Respond: Yes!");
                            }

                        } else if (outcome==JOptionPane.CANCEL_OPTION|| outcome==JOptionPane.CLOSED_OPTION||outcome==JOptionPane.NO_OPTION) {
                            Dout.writeUTF("Respond!Rejected");
                            Dout.flush();
                            ManagerGUI.Network.remove(this);
                            System.out.println("Respond: Rejected!");

                        }
                    }

                } else if (output[0].equals("Draw")) {
                    Broadcast(str1);
                    RepaintCanvas(output[1]);

                } else if (output[0].equals("Chat")) {
                    String[] show=output[1].split("!",2);
//                    if (show.length>2){
//                        show= output[1]+" From: "+output[2];
//                    }else{
//                        show= output[1]+" From: someone";
//                    }
                    ManagerGUI.CreateWhiteBoard.Chat0.append("\r\n"+show[1]+" says: " +show[0]+"\r\n");
                    Broadcast("Chat!"+show[0]+"!"+show[1]);
                    ManagerGUI.To_replace=ManagerGUI.CreateWhiteBoard.Chat0.getText().toString();

                } else if (output[0].equals("Over")) {
                    socket.close();
                    break;

                }
                if(output[0].equals("New")){
                    ManagerGUI.CreateWhiteBoard.Canvas.removeAll();
                    ManagerGUI.CreateWhiteBoard.Canvas.updateUI();
                    ManagerGUI.PaintListener.ClearRecord();
                }
            }
        }catch (SocketException e){
            System.out.println("User "+this.NID+"'s connection is interrupted, it goes away! ");
            if(!this.Oust){
                System.out.println("ClientGoAway!");
                ClientGoAway();
            }

        }catch (Exception e1){
            System.out.println("User "+this.NID+"'s connection is interrupted, Accidentally ");
        }


    }

    public void ClientGoAway(){
        System.out.println("ClientGoAway!!!!!!");
       ManagerGUI.CreateWhiteBoard.Network.remove(this);
        ManagerGUI.CreateWhiteBoard.Usernames.remove(NID);
        String ToGo="ClientGoAway!"+NID;
        System.out.println("Togogogogogo!");
        for(String UID:ManagerGUI.CreateWhiteBoard.Usernames){
            ToGo+="!"+UID;
        }
        System.out.println("Togogogogogo! is done. Size of Network: "+ManagerGUI.CreateWhiteBoard.Network.size());

            if(ManagerGUI.CreateWhiteBoard.Network.size()!=0){
                for(int i=0;i<ManagerGUI.CreateWhiteBoard.Network.size();i++){
                    Networking lame=ManagerGUI.CreateWhiteBoard.Network.get(i);
                    try{
                        lame.Dout.writeUTF(ToGo);
                        Dout.flush();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    //ManagerGUI.CreateWhiteBoard.Network.remove(this);
                    System.out.println("Lame is done.");
                    String lamelame=ToGo.split("!",2)[1];
                    System.out.println("Lamelame!");
                    OustClient(lamelame);
                }

            }

        System.out.println("Lame2 is done.");
        String lamelame=ToGo.split("!",2)[1];
        System.out.println("Lamelame2!");
        OustClient(lamelame);



    }
}
