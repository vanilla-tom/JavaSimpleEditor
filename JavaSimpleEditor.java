import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;
import java.net.Socket;

public class JavaSimpleEditor extends JFrame {

    private JTextPane textPane;
    private JFileChooser fileChooser;
    private RTFEditorKit rtfEditor;
    private JLabel status;

    public JavaSimpleEditor() {
        init();
    }

    private void init() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Simple Editor");

        // Create the main text pane
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create the status bar label
        status = new JLabel("Ready");
        getContentPane().add(status, BorderLayout.SOUTH);

        // Create the main menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create the file menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Create the file menu items
        JMenuItem menuItemNew = new JMenuItem("New");
        menuItemNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                textPane.setText("");
                status.setText("New document created");
            }
        });
        fileMenu.add(menuItemNew);

        JMenuItem menuItemOpen = new JMenuItem("Open");
        menuItemOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int returnVal = fileChooser.showOpenDialog(JavaSimpleEditor.this);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        rtfEditor.read(fis, textPane.getDocument(), 0);
                        fis.close();
                        status.setText("File opened: " + file.getName());
                    } catch (IOException | BadLocationException ex){
                        ex.printStackTrace();
                        status.setText("Open file failed");
                    }
                }
            }
        });
        fileMenu.add(menuItemOpen);

        JMenuItem menuItemSave = new JMenuItem("Save");
        menuItemSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showSaveDialog(JavaSimpleEditor.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        rtfEditor.write(fos, textPane.getDocument(), 0, textPane.getDocument().getLength());
                        fos.close();
                        status.setText("File saved: " + file.getName());
                    } catch (IOException | BadLocationException ex) {
                        ex.printStackTrace();
                        status.setText("Save file failed");
                    }
                }
            }
        });
        fileMenu.add(menuItemSave);

        JMenuItem menuItemSaveAs = new JMenuItem("Save As");
        menuItemSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showSaveDialog(JavaSimpleEditor.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        rtfEditor.write(fos, textPane.getDocument(), 0, textPane.getDocument().getLength());
                        fos.close();
                        status.setText("File saved as: " + file.getName());
                    } catch (IOException | BadLocationException ex) {
                        ex.printStackTrace();
                        status.setText("Save file failed");
                    }
                }
            }
        });
        fileMenu.add(menuItemSaveAs);

        // Create the edit menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        // Create the edit menu items
        JMenuItem menuItemFind = new JMenuItem("Find");
        menuItemFind.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = JOptionPane.showInputDialog(textPane, "Enter text to find:");
                if (searchText != null) {
                    String documentText = textPane.getText();
                    int index = documentText.indexOf(searchText);
                    if (index != -1) {
                        textPane.setCaretPosition(index);
                        textPane.setSelectionStart(index);
                        textPane.setSelectionEnd(index + searchText.length());
                        JOptionPane.showMessageDialog(textPane, "Text found: " + searchText);
                    } else {
                        JOptionPane.showMessageDialog(textPane, "Text not found: " + searchText);
                    }
                }
            }
        });
        editMenu.add(menuItemFind);

        JMenuItem menuItemReplace = new JMenuItem("Replace");
        menuItemReplace.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = JOptionPane.showInputDialog(textPane, "Enter text to find:");
                if (searchText != null) {
                    String replaceText = JOptionPane.showInputDialog(textPane, "Enter text to replace:");
                    if (replaceText != null) {
                        String documentText = textPane.getText();
                        String updatedText = documentText.replace(searchText, replaceText);
                        textPane.setText(updatedText);

                        JOptionPane.showMessageDialog(textPane,
                                "Text replaced: " + searchText + " with " + replaceText);
                    }
                }
            }
        });
        editMenu.add(menuItemReplace);

        // Create the format menu
        JMenu formatMenu = new JMenu("Format");
        menuBar.add(formatMenu);

        // Create the format menu items
        JMenuItem menuItemFont = new JMenuItem("Font");
        menuItemFont.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
                String selectedFontName = (String) JOptionPane.showInputDialog(textPane, "Select a font:", "Font",
                        JOptionPane.PLAIN_MESSAGE, null, fontNames, null);
                if (selectedFontName != null) {
                    String inputSize = JOptionPane.showInputDialog(textPane, "Enter font size:");
                    if (inputSize != null) {
                        int fontSize = Integer.parseInt(inputSize);
                        Font font = new Font(selectedFontName, Font.PLAIN, fontSize);
                        textPane.setFont(font);
                    }
                }
            }
        });
        formatMenu.add(menuItemFont);

        JMenuItem menuItemColor = new JMenuItem("Color");
        menuItemColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(textPane, "Select a color", textPane.getForeground());
                if (selectedColor != null) {
                    textPane.setForeground(selectedColor);
                }
            }
        });
        formatMenu.add(menuItemColor);

        List sizeList = new List();
        for(int i = 12; i <= 60; i += 4){
            sizeList.add(Integer.toString(i));
        }
        JMenu menuItemSize = new JMenu("Size");
        menuItemSize.add(sizeList);
        sizeList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selectedSize = (String) sizeList.getSelectedItem();
                int fontSize = Integer.parseInt(selectedSize);
                Font currentFont = textPane.getFont();
                Font newFont = currentFont.deriveFont((float) fontSize);
                textPane.setFont(newFont);
            }
        });
        formatMenu.add(menuItemSize);

        // Create the insert menu
        JMenu insertMenu = new JMenu("Insert");
        menuBar.add(insertMenu);

        // Create the insert menu items
        JMenuItem imageMenuItem = new JMenuItem("Image");
        imageMenuItem.addActionListener(new ImageActionListener(textPane));
        insertMenu.add(imageMenuItem);

        // Initialize the file chooser
        fileChooser = new JFileChooser();

        // Initialize the RTF editor kit
        rtfEditor = new RTFEditorKit();

        // Create Save button
        JButton saveButton = new JButton("Save to Server");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new SaveToServerTask(textPane.getText())).start();
            }
        });
        getContentPane().add(saveButton, BorderLayout.NORTH);
    }

    private class SaveToServerTask implements Runnable {
        private String content;

        public SaveToServerTask(String content) {
            this.content = content;
        }

        @Override
        public void run() {
            try (Socket socket = new Socket("localhost", 8808);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                String fileName = JOptionPane.showInputDialog("Enter file name:");
                if (fileName != null && !fileName.trim().isEmpty()) {
                    out.println(fileName);
                    out.println(content);
                    out.println("EOF");
                    String response = in.readLine();
                    JOptionPane.showMessageDialog(null, response);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving to server");
            }
        }
    }

    private class ImageActionListener implements ActionListener {
        private JTextPane textPane;

        public ImageActionListener(JTextPane textPane) {
            this.textPane = textPane;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 打开文件选择对话框并插入选定的图片
            insertImage();
        }

        private void insertImage() {
            // 创建文件选择对话框
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("选择图片");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            // 显示文件选择对话框
            int result = fileChooser.showOpenDialog(null);

            // 如果用户选择了文件
            if (result == JFileChooser.APPROVE_OPTION) {
                // 获取用户选定的文件
                java.io.File selectedFile = fileChooser.getSelectedFile();

                // 创建图标对象
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());

                // 在文本组件的文档中插入图片
                StyledDocument doc = textPane.getStyledDocument();
                Style style = textPane.addStyle("Image", null);
                StyleConstants.setIcon(style, icon);
                try {
                    doc.insertString(doc.getLength(), " ", style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JavaSimpleEditor editor = new JavaSimpleEditor();
                editor.setSize(1000, 600);
                editor.setVisible(true);
            }
        });
    }
}