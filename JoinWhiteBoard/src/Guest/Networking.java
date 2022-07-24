package Guest;



import javax.swing.*;
import java.io.*;
import java.net.Socket;



public class Networking  {

    private Socket socket;
    public  String NID;

    public DataInputStream Din=null;

    public DataOutputStream Dout=null;

    public String State;
    boolean Oust=false;

    public Networking(Socket socket){

        Reset();
        try{
            this.socket=socket;
            Dout=new DataOutputStream(this.socket.getOutputStream());
            Din=new DataInputStream(this.socket.getInputStream());
        }catch (Exception ee){
            ee.printStackTrace();
        }
    }

    public void Reset() {
        State="Waiting";
        return;
    }

    String GetState(){
        return State;
    }


    public void GuestGo(){

       try{

           while(true){
               String request = Din.readUTF();
               System.out.println("Message from the manager!");
               String[] ProcessedRequest=request.split("!",2);
               if (ProcessedRequest.length>=2)
               System.out.println("The message from the manager: "+ProcessedRequest[1]);
               if(ProcessedRequest[0].equals("Draw")){
                   GuestGUI.Listener.UpdateRecord(ProcessedRequest[1]);
                   GuestGUI.GuestPainter.repaint();
               }
               if(ProcessedRequest[0].equals("Chat")){
                   String[] chatchat=ProcessedRequest[1].split("!",2);

                   if(!chatchat[1].equals(GuestGUI.JoinWhiteboard.GuestName)){
                       GuestGUI.JoinWhiteboard.ChatArea.append("\r\n"+chatchat[1]+" says: "+chatchat[0]+"\r\n");
                       GuestGUI.JoinWhiteboard.ChatArea.setCaretPosition(GuestGUI.JoinWhiteboard.ChatArea.getDocument().getLength());
                   }
                   GuestGUI.To_waive=GuestGUI.JoinWhiteboard.ChatArea.getText().toString();


               }
               if(ProcessedRequest[0].equals("ListOfUser")&&GuestGUI.JoinWhiteboard!=null){
                   System.out.println("List of user: "+ProcessedRequest[1]);
                   GuestGUI.JoinWhiteboard.List.setListData(ProcessedRequest[1].split("!"));
               }
               if(ProcessedRequest[0].equals("Delete")){
                   String[] UIDs=ProcessedRequest[1].split("!",2);
                   String[] ListOfUser=UIDs[1].split("!");
                   JOptionPane.showMessageDialog(GuestGUI.JoinWhiteboard.frame, UIDs[0]+" is expelled");
                   GuestGUI.JoinWhiteboard.List.setListData(ListOfUser);
               }
               if(ProcessedRequest[0].equals("Kick")){
                   if(ProcessedRequest[1].equals(NID))
                   Oust=true;
                   JOptionPane.showMessageDialog(GuestGUI.JoinWhiteboard.frame, "You are the persona non grata");
               }
               if(ProcessedRequest[0].equals("Respond")){
                   System.out.println("Node's state should go to: "+ProcessedRequest[1]);
                   if(ProcessedRequest[1].equals("No"))
                       State="No";  //System.out.println("Node's state is set to: "+ProcessedRequest[1]);
                   else if (ProcessedRequest[1].equals("Yes") )
                       State="Yes"; //System.out.println("Node's state is set to: "+ProcessedRequest[1]);
                   else if (ProcessedRequest[1].equals("Rejected") )
                       State="Rejected";


                       System.out.println("Node's state is set to: "+ProcessedRequest[1]);


               }
               if(ProcessedRequest[0].equals("ClientGoAway")){
                   String[] UIDs=ProcessedRequest[1].split("!",2);
                   JOptionPane.showMessageDialog(GuestGUI.JoinWhiteboard.GuestPanel,"User: "+UIDs[0]+" has left!");
                   String [] userlist=UIDs[1].split("!");
                   GuestGUI.JoinWhiteboard.List.setListData(userlist);

               }
               if(ProcessedRequest[0].equals("New")){
                   GuestGUI.GuestPainter.removeAll();
                   GuestGUI.GuestPainter.updateUI();
                   GuestGUI.Listener.ClearRecord();
               }
           }

       }catch(IOException e){
           try{
               if(!Oust){
                   JOptionPane.showMessageDialog(GuestGUI.frame,"Client/Guest Disconnected!");
               }
           }catch (Exception e2){
               e2.printStackTrace();
           }System.exit(0);

       }

    }

}
