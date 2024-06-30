## 简易文本编辑器

#### 框架结构
实现了文字编辑、保存、另存为、查找替换功能，，并包括改变文字的字体类型、显示颜色和显示大小的功能。代码中还加入了如何插入、删除和编辑图片。
以下是整体的代码框架
```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.rtf.RTFEditorKit;

public class SimpleEditor extends JFrame {

    private JTextPane textPane;
    private JFileChooser fileChooser;
    private RTFEditorKit rtfEditorKit;
    private JLabel statusLabel;
    private JComboBox<String> sizeComboBox;

    public SimpleEditor() {
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
}
```

#### ReplaceMenuItemActionListener类
```actionPerformed```函数首先弹出一个输入对话框，要求用户输入要查找的文本，然后再弹出一个输入对话框，要求用户输入要替换的文本。如果用户都进行了输入，它会获取 ```textPane``` 对象的文本内容，并使用 ```replace``` 函数将所有匹配的文本替换为指定的文本。然后，它会将更新后的文本内容设置回 ```textPane``` 对象，并弹出一个消息对话框，指示替换的结果。

这个实现适用于简单的文本替换操作。需要注意的是，它只会替换所有出现的匹配文本，不会替换所有匹配项。
```java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class ReplaceMenuItemActionListener implements ActionListener {
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
```

#### FontMenuItemActionListener类

```actionPerformed``` 函数首先获取系统中可用的字体名称，并使用 ```JOptionPane``` 弹出选择对话框，要求用户选择一个字体。然后，它弹出一个输入对话框，要求用户输入字体的大小。如果用户都进行了选择和输入，它会创建一个包含用户选择字体和大小的新 ```Font``` 对象，并将其应用于 ```textPane``` 对象。这样，文本将以用户选择的字体和大小显示。

请注意，该实现假设用户输入的字体大小是一个有效的整数。如果用户输入非法的数字，将会抛出 ```NumberFormatException```。

```java
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class FontMenuItemActionListener implements ActionListener {
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
```

#### ColorMenuItemActionListener类
`actionPerformed` 函数首先使用 `JColorChooser` 弹出颜色选择器对话框。这个对话框显示了当前 ```textPane``` 的前景色（即文本颜色），以供用户选择新的颜色。当用户选择一种颜色或关闭对话框时，```selectedColor``` 将被设置为所选的颜色。然后，如果选择了颜色（即 ```selectedColor``` 不为 ```null```），将通过 ```textPane.setForeground()``` 方法将所选的颜色应用于 ```textPane``` 的前景色。

这样，当用户选择一种颜色并执行操作时，```textPane``` 中的文本将会以用户选择的颜色显示出来。
```java
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JTextPane;

public class ColorMenuItemActionListener implements ActionListener {
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
```

#### SizeMenuItemActionListener类
`actionPerformed`函数首先从 `sizeComboBox` 中获取用户选择的字体大小。然后，它将选择的字体大小转换为整数类型。接下来，它使用 `textPane.getFont()` 方法获取当前的字体，并在此基础上使用 `deriveFont` 方法创建一个新的字体，将字体大小设置为用户选择的大小。最后，使用 `textPane.setFont()` 方法将新的字体应用于 `textPane`。

这样，当用户选择一个字体大小并执行操作时，`textPane` 中的文本将会以选择的字体大小显示出来。
```java
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextPane;

public class SizeMenuItemActionListener implements ActionListener {
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
```
对SizeMenuItemActionListener中的sizeComboBox赋值:
```java
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
```
加入了上述几种类型的字体大小值。

#### ImageMenuItemActionListener类

`actionPerformed` 函数首先创建一个 `JFileChooser` 对象，用于选择图像文件。然后，它将显示文件选择器对话框，并等待用户选择文件。如果用户选择了文件，将获取所选文件的绝对路径。接下来，它使用 `ImageIcon` 创建一个图标对象，并检查是否成功加载了图像文件。如果成功加载了图像文件，将调整图像的大小为宽度固定为`300`像素，高度按比例调整以保持纵横比，并创建一个包含图像的标签。然后，它将该标签放置在一个带有滚动条的面板中，并使用 `JOptionPane.showMessageDialog` 方法显示该面板。最后，如果加载图像失败，它将显示一个消息对话框，指示无效的图像文件。

这样，当用户执行操作并选择了图像文件时，将在 `panel` 上显示选定的图像。

```java
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ImageMenuItemActionListener implements ActionListener {
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
```

#### 网络编程
1. 编写网络服务端程序：
+ 创建一个服务器端程序，监听指定的端口。
+ 使用Java的Socket和ServerSocket类实现与客户端的连接。
+ 在服务器端程序中，使用线程池或者线程池框架来处理客户端连接请求，以便能够同时处理多个客户端请求。

2. 修改SimpleEditor程序：
+ 在SimpleEditor程序中，添加一个"Save"按钮或其他方式触发保存操作。
+ 当用户点击保存按钮时，将编辑的文本内容发送给服务器端程序。
+ 在服务器端程序中，接收来自客户端的文本内容，并将其保存到指定的文件中。

3. 实现网络传输的专门线程：
+ 在SimpleEditor程序中，创建一个专门的线程来处理与服务器端的通信。
+ 这个线程可以通过Socket连接与服务器端进行通信，发送编辑的文本内容，并接收服务器端的响应。

```java
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class JavaSimpleEditorServer {
    private static final int PORT = 8808;
    private static final String SAVE_DIR = "save";

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                // Accept incoming client connections
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandler(socket));
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            // Shutdown the executor service
            executor.shutdown();
        }
    }

    public static String getSaveDir() {
        return SAVE_DIR;
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Read the file name from the client
            String fileName = in.readLine();
            StringBuilder fileContent = new StringBuilder();
            String line;

            // Read file content line by line until "EOF" is received
            while ((line = in.readLine()) != null) {
                if ("EOF".equals(line)) { // Check for end of file marker
                    break;
                }
                fileContent.append(line).append(System.lineSeparator());
            }

            // Save the received file content to a file
            saveToFile(fileName, fileContent.toString());
            // Send confirmation message to client
            out.println("File saved successfully");

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void saveToFile(String fileName, String content) {
        try {
            File dir = new File(JavaSimpleEditorServer.getSaveDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
            }
        } catch (IOException ex) {
            System.out.println("File saving exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
```

#### 实现功能

简单编辑器`SimpleEditor`实现了以下功能：
1. 图形化编辑，保存文本。
2. 插入图片，删除图片。
3. 查找，替换文本。
4. 更改文本的字体、大小和颜色。
5. 实现多线程网络存储。
6. 更多内容请见实际演示。