package Manager;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OpenDialog extends JDialog {

    JFrame Who;

    private ManagerGUI whiteboard;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField FilenameToTXT;
    private JTextField NotExisting;
    private JLabel Username;

    private void INIT(){
        Username.setText(ManagerGUI.UserName+" is using this dialog");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });



        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


    }
    public OpenDialog() {
        INIT();
    }

    public OpenDialog(ManagerGUI whiteboard){
        this.whiteboard=whiteboard;
        INIT();
    }

    private void onOK() {
        String filename=FilenameToTXT.getText();
        try{
            new Scanner(new FileInputStream(filename+".txt"));
        }catch(FileNotFoundException ex){
            NotExisting.setText("Error! The file is not existing!");
            JOptionPane.showMessageDialog(contentPane,"Error! It does not exist in the directory you type in!");
            return;
        }
        whiteboard.OpenFile(filename+".txt");

        // 在此处添加您的代码
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void main(String[] args) {
        OpenDialog dialog = new OpenDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
