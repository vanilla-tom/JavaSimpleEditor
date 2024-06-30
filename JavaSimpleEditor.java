import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

public class JavaSimpleEditor extends JFrame {

    private JTextPane textPane;
    private JFileChooser fileChooser;
    private RTFEditorKit rtfEditorKit;
    private JLabel statusLabel;
    private JComboBox<String> sizeComboBox;

    public JavaSimpleEditor() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simple Editor");

        // Create the main text pane
        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create the status bar label
        statusLabel = new JLabel("Ready");
        getContentPane().add(statusLabel, BorderLayout.SOUTH);

        // Create the main menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Create the file menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Create the file menu items
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new NewMenuItemActionListener());
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new OpenMenuItemActionListener());
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new SaveMenuItemActionListener());
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(new SaveAsMenuItemActionListener());
        fileMenu.add(saveAsMenuItem);

        // Create the edit menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        // Create the edit menu items
        JMenuItem findMenuItem = new JMenuItem("Find");
        findMenuItem.addActionListener(new FindMenuItemActionListener(textPane));
        editMenu.add(findMenuItem);

        JMenuItem replaceMenuItem = new JMenuItem("Replace");
        replaceMenuItem.addActionListener(new ReplaceMenuItemActionListener(textPane));
        editMenu.add(replaceMenuItem);

        // Create the format menu
        JMenu formatMenu = new JMenu("Format");
        menuBar.add(formatMenu);

        // Create the format menu items
        JMenuItem fontMenuItem = new JMenuItem("Font");
        fontMenuItem.addActionListener(new FontMenuItemActionListener(textPane));
        formatMenu.add(fontMenuItem);

        JMenuItem colorMenuItem = new JMenuItem("Color");
        colorMenuItem.addActionListener(new ColorMenuItemActionListener(textPane));
        formatMenu.add(colorMenuItem);

        JComboBox<String> sizeComboBox = new JComboBox<>();
        sizeComboBox.addItem("12");
        sizeComboBox.addItem("16");
        sizeComboBox.addItem("20");
        sizeComboBox.addItem("24");
        sizeComboBox.addItem("28");
        sizeComboBox.addItem("32");
        sizeComboBox.addItem("36");
        sizeComboBox.addItem("40");
        sizeComboBox.addItem("44");
        sizeComboBox.addItem("48");
        sizeComboBox.addItem("52");
        sizeComboBox.addItem("56");
        sizeComboBox.addItem("60");
        JMenuItem sizeMenuItem = new JMenuItem("Size");
        SizeMenuItemActionListener sizeMenuItemActionListener = new SizeMenuItemActionListener(textPane, sizeComboBox);
        sizeMenuItem.addActionListener(sizeMenuItemActionListener);
        sizeComboBox.addActionListener(sizeMenuItemActionListener);
        formatMenu.add(sizeMenuItem);

        // Create the insert menu
        JMenu insertMenu = new JMenu("Insert");
        menuBar.add(insertMenu);

        // Create the insert menu items
        JMenuItem imageMenuItem = new JMenuItem("Image");
        imageMenuItem.addActionListener(new ImageMenuItemActionListener(textPane));
        insertMenu.add(imageMenuItem);

        // Initialize the file chooser
        fileChooser = new JFileChooser();

        // Initialize the RTF editor kit
        rtfEditorKit = new RTFEditorKit();
    }

    private class NewMenuItemActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            textPane.setText("");
            statusLabel.setText("New file created");
        }
    }

    private class OpenMenuItemActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            int returnVal = fileChooser.showOpenDialog(JavaSimpleEditor.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileInputStream fis = new FileInputStream(file);
                    rtfEditorKit.read(fis, textPane.getDocument(), 0);
                    fis.close();
                    statusLabel.setText("File opened: " + file.getName());
                } catch (IOException | BadLocationException e) {
                    e.printStackTrace();
                    statusLabel.setText("Error opening file");
                }
            }
        }
    }

    private class SaveMenuItemActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            int returnVal = fileChooser.showSaveDialog(JavaSimpleEditor.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    rtfEditorKit.write(fos, textPane.getDocument(), 0, textPane.getDocument().getLength());
                    fos.close();
                    statusLabel.setText("File saved: " + file.getName());
                } catch (IOException | BadLocationException e) {
                    e.printStackTrace();
                    statusLabel.setText("Error saving file");
                }
            }
        }
    }

    private class SaveAsMenuItemActionListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            int returnVal = fileChooser.showSaveDialog(JavaSimpleEditor.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    rtfEditorKit.write(fos, textPane.getDocument(), 0, textPane.getDocument().getLength());
                    fos.close();
                    statusLabel.setText("File saved as: " + file.getName());
                } catch (IOException | BadLocationException e) {
                    e.printStackTrace();
                    statusLabel.setText("Error saving file");
                }
            }
        }
    }

    private class FindMenuItemActionListener implements ActionListener {
        private JTextPane textPane;

        public FindMenuItemActionListener(JTextPane textPane) {
            this.textPane = textPane;
        }

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
    }

    private class ReplaceMenuItemActionListener implements ActionListener {
        private JTextPane textPane;

        public ReplaceMenuItemActionListener(JTextPane textPane) {
            this.textPane = textPane;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String searchText = JOptionPane.showInputDialog(textPane, "Enter text to find:");
            if (searchText != null) {
                String replaceText = JOptionPane.showInputDialog(textPane, "Enter text to replace:");
                if (replaceText != null) {
                    String documentText = textPane.getText();
                    String updatedText = documentText.replace(searchText, replaceText);
                    textPane.setText(updatedText);

                    JOptionPane.showMessageDialog(textPane, "Text replaced: " + searchText + " with " + replaceText);
                }
            }
        }
    }

    private class FontMenuItemActionListener implements ActionListener {
        private JTextPane textPane;

        public FontMenuItemActionListener(JTextPane textPane) {
            this.textPane = textPane;
        }

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
    }

    private class ColorMenuItemActionListener implements ActionListener {
        private JTextPane textPane;

        public ColorMenuItemActionListener(JTextPane textPane) {
            this.textPane = textPane;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Color selectedColor = JColorChooser.showDialog(textPane, "Select a color", textPane.getForeground());
            if (selectedColor != null) {
                textPane.setForeground(selectedColor);
            }
        }
    }

    private class SizeMenuItemActionListener implements ActionListener {
        private JTextPane textPane;
        private JComboBox<String> sizeComboBox;

        public SizeMenuItemActionListener(JTextPane textPane, JComboBox<String> sizeComboBox) {
            this.textPane = textPane;
            this.sizeComboBox = sizeComboBox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedSize = (String) sizeComboBox.getSelectedItem();
            int fontSize = Integer.parseInt(selectedSize);
            Font currentFont = textPane.getFont();
            Font newFont = currentFont.deriveFont((float) fontSize);
            textPane.setFont(newFont);
        }
    }

    private class ImageMenuItemActionListener implements ActionListener {
        private JTextPane textPane;

        public ImageMenuItemActionListener(JTextPane textPane) {
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
                editor.setSize(800, 600);
                editor.setVisible(true);
            }
        });
    }
}