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
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandler(socket));
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
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

            String command = in.readLine();

            if ("SAVE".equals(command)) {
                String fileName = in.readLine();
                StringBuilder fileContent = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    if ("EOF".equals(line)) { // 检查结束标志
                        break;
                    }
                    fileContent.append(line).append(System.lineSeparator());
                }

                saveToFile(fileName, fileContent.toString());
                out.println("File saved successfully");
            } else if ("READ".equals(command)) {
                String fileName = in.readLine();
                String fileContent = readFromFile(fileName);
                if (fileContent != null) {
                    out.println(fileContent);
                    out.println("EOF");
                } else {
                    out.println("File not found");
                }
            }
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

    private String readFromFile(String fileName) {
        File file = new File(JavaSimpleEditorServer.getSaveDir(), fileName);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString();
        } catch (IOException ex) {
            System.out.println("File reading exception: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
