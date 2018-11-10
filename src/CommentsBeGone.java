import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class CommentsBeGone {
    private JPanel panel;
    private JButton buttonRun;
    private JFormattedTextField textInputFileName;
    private JButton buttonFileChooser;

    private static int commentNewLine = 1;
    private static int commentOnLine = 2;
    private static int commentMultiLine = 3;

    private static int commentFlagClear = 0;

    private static String getText(String fileName) {
        try {
            String data = new String(Files.readAllBytes(Paths.get(fileName)));
            //System.out.println(data);
            return data;
        } catch (IOException e) {
            return "";
        }
    }

    private static void eraseComments(CommentsBeGone app) {
        String fileName = app.textInputFileName.getText();
        String data = getText(fileName);
        int commentFlag = 0;
        String output = "";
        int i = 0;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){}
        while (i < data.length()) {
            if (i < data.length() - 1) {
                String p = String.valueOf(data.charAt(i));
                p += String.valueOf(data.charAt(i + 1));

                if (p.equals("//")) {
                    if (!String.valueOf(data.charAt(i - 1)).equals("\n")) {
                        commentFlag = commentOnLine;
                    } else {
                        commentFlag = commentNewLine;
                    }

                }

                if (p.equals("/*")) {
                    commentFlag = commentMultiLine;
                }

                if (p.equals("*/")) {
                    commentFlag = commentFlagClear;
                    i += 3;
                }
            }

            if (commentFlag == commentOnLine) {
                if (String.valueOf(data.charAt(i)).equals("\n")) {
                    commentFlag = commentFlagClear;
                    i--;
                }
            }

            if (commentFlag == commentNewLine) {
                if (String.valueOf(data.charAt(i)).equals("\n")) {
                    commentFlag = commentFlagClear;
                }
            }

            if (commentFlag == commentFlagClear) {
                output += String.valueOf(data.charAt(i));
            }

            i++;
            int progress = (int)(i/data.length())*100;
        }

        File fold = new File(fileName);
        fold.delete();
        File fnew = new File(fileName);
        try {
            FileWriter fw = new FileWriter(fnew, false);
            fw.write(output);
            fw.close();
        } catch (IOException e) { }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CommentsBeGone app = new CommentsBeGone();

        LookAndFeel old = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable ex) {
            old = null;
        }

        app.panel.setSize(1000, 1000);

        app.buttonRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eraseComments(app);
            }
        });

        app.buttonFileChooser.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(app.panel);
                String fileName = fc.getSelectedFile().getAbsolutePath();
                app.textInputFileName.setText(fileName);
            }
        }));

        frame.add(app.panel);
        frame.pack();
        frame.setVisible(true);
    }
}