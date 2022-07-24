package Manager;

import javax.swing.*;
import java.awt.event.*;

public class SaveAsDialog extends JDialog {
    private ManagerGUI CreateWhiteBoard;
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JLabel SALabel;
    private JComboBox SAcbox;
    private JTextField FilenameTextField;

    public SaveAsDialog() {
        Init();
    }

    public SaveAsDialog(ManagerGUI WB){
        CreateWhiteBoard=WB;
        Init();
    }

    private void Init(){
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSave);

        buttonSave.addActionListener(new ActionListener() {
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
    private void onOK() {
        // 在此处添加您的代码
        String filetype=SAcbox.getSelectedItem().toString();
        String filename=FilenameTextField.getText().toString();
        if(filetype==".txt"){
            CreateWhiteBoard.SaveIt(filename+filetype);
        } else if (filetype==".jpg") {
            CreateWhiteBoard.SavePic(filename+filetype);
        }
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void main(String[] args) {
        Listeners Listener=ManagerGUI.PaintListener;
        SaveAsDialog dialog = new SaveAsDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
