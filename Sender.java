import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.*;
import java.nio.ByteBuffer;

public class Sender {
    private boolean debug = false;
    private JTextField IPField;
    private JTextField ACKPortField;
    private JTextField fileNameField;
    private JButton btnBrowse;
    private JTextField maxPacketSizeField;
    private JTextField timeoutField;
    private JButton btnTransfer;
    private JLabel lostPacketCountLabel;
    private JLabel progressField;
    private JLabel statusLabel;
    private JTextField timeElapsedField;
    private JPanel panel;
    private JTextField dataPortField;
    private JLabel percentageField;
    private InetAddress ip;
    private final JFileChooser fc = new JFileChooser();
    private static File file;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Datagram Sender");
        frame.setContentPane(new Sender().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private Sender() {
        if (debug) debugSettings();
        btnBrowse.addActionListener(e -> {
            int returnVal = fc.showOpenDialog(btnBrowse);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                fileNameField.setText(file.getName());
                System.out.println("Choosing: " + file.getName() + ".");
                if (isPositiveNumeric(maxPacketSizeField.getText())) {
                    updateProgressField(0, turn_left());
                }
            } else {
                System.out.println("File choosing cancelled by user.");
            }
        });

        btnTransfer.addActionListener(e -> {
            if (verify_input()) {
                try {
                    new Thread(new datagramSender()).start();
                } catch (IOException ex) {
                    infoBox("File not found. Something smells fishy.", "Unexpected error");
                    ex.printStackTrace();
                    System.err.println("datagramSender Creation Failed.");
                }
            }
        });
    }

    private int turn_left() {
        System.out.println("PiaoLiangDeXiaoPan!!!");
        long longLength = file.length();
        int intLength = (int) longLength;
        return (intLength == longLength) ? (intLength / Integer.parseInt(maxPacketSizeField.getText()) + 1) : 0;
    }

    private void updateProgressField(int sent, int totalDataPacket) {
        progressField.setText(sent + "/" + (3 + totalDataPacket + 1)); //3 handshake packets +
        // data packets + 1 EOF packet
        percentageField.setText(sent * 100 / (3 + totalDataPacket + 1) + "%");

    }

    private void debugSettings() {
        IPField.setText("localhost");
        dataPortField.setText("9876");
        ACKPortField.setText("9877");
//        filePath = new File("testing/sending/Lorem_Ipsum.txt");
        file = new File("testing/sending/numbers.txt");
        fileNameField.setText(file.getName());
        maxPacketSizeField.setText("200");
        timeoutField.setText("10000");
        updateProgressField(0, turn_left());
    }

    private boolean verify_input() {
        StringBuilder msgBuilder = new StringBuilder();
        try {
            ip = InetAddress.getByName(IPField.getText());
        } catch (UnknownHostException e) {
            msgBuilder.append("Host name or IP address not legal. Host not Found.\n");
        }
        if (!isPositiveNumeric(dataPortField.getText()) || Integer.parseInt(dataPortField.getText()) > 65535) {
            msgBuilder.append("Data Port not legal. 0-65535 is expected\n");
        }
        if (!isPositiveNumeric(ACKPortField.getText()) || Integer.parseInt(ACKPortField.getText()) > 65535) {
            msgBuilder.append("ACK Port not legal. 0-65535 is expected\n");
        }
        if (!isPositiveNumeric(maxPacketSizeField.getText())) {
            msgBuilder.append("Max. Packet Size not legal. Positive integer is expected\n");
        }
        if (!isPositiveNumeric(timeoutField.getText())) {
            msgBuilder.append("Timeout value not legal. Positive integer is expected\n");
        }
        if (file == null) msgBuilder.append("No filePath chosen.");

        if (msgBuilder.toString().equals("")) return true;
        infoBox(msgBuilder.toString(), "Parameter(s) Invalid.");
        return false;
    }

    private static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    private static boolean isPositiveNumeric(String str) {
        int i;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException n) {
            return false;
        }
        return i >= 0;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setAlignmentX(0.5f);
        panel.setAutoscrolls(true);
        panel.setInheritsPopupMenu(false);
        panel.setMinimumSize(new Dimension(720, 150));
        panel.setPreferredSize(new Dimension(720, 150));
        panel.putClientProperty("html.disable", Boolean.FALSE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
        final JLabel label1 = new JLabel();
        label1.setText("IP Addr.");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label1, gbc);
        IPField = new JTextField();
        IPField.setColumns(2);
        IPField.setMinimumSize(new Dimension(64, 28));
        IPField.setPreferredSize(new Dimension(28, 29));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(IPField, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Data Port:");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label2, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("ACK Port:");
        gbc = new GridBagConstraints();
        gbc.gridx = 11;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label3, gbc);
        ACKPortField = new JTextField();
        ACKPortField.setColumns(1);
        ACKPortField.setPreferredSize(new Dimension(17, 29));
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(ACKPortField, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("File Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label4, gbc);
        fileNameField = new JTextField();
        fileNameField.setEditable(false);
        fileNameField.setPreferredSize(new Dimension(64, 29));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(fileNameField, gbc);
        btnBrowse = new JButton();
        btnBrowse.setText("Browse..");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnBrowse, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Max. Packet Size:");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label5, gbc);
        maxPacketSizeField = new JTextField();
        maxPacketSizeField.setPreferredSize(new Dimension(64, 29));
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(maxPacketSizeField, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Timeout(ms):");
        gbc = new GridBagConstraints();
        gbc.gridx = 11;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label6, gbc);
        timeoutField = new JTextField();
        timeoutField.setPreferredSize(new Dimension(64, 29));
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(timeoutField, gbc);
        btnTransfer = new JButton();
        btnTransfer.setText("Transfer");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnTransfer, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Transeferred:");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label7, gbc);
        progressField = new JLabel();
        progressField.setText("N/A");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        panel.add(progressField, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Timed Out Packet Count:");
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label8, gbc);
        lostPacketCountLabel = new JLabel();
        lostPacketCountLabel.setText("0");
        gbc = new GridBagConstraints();
        gbc.gridx = 13;
        gbc.gridy = 3;
        panel.add(lostPacketCountLabel, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Status:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label9, gbc);
        statusLabel = new JLabel();
        statusLabel.setText("Inactive");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 4;
        panel.add(statusLabel, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Time(ms):");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label10, gbc);
        timeElapsedField = new JTextField();
        timeElapsedField.setEditable(true);
        timeElapsedField.setEnabled(true);
        timeElapsedField.setPreferredSize(new Dimension(64, 28));
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(timeElapsedField, gbc);
        dataPortField = new JTextField();
        dataPortField.setPreferredSize(new Dimension(64, 29));
        gbc = new GridBagConstraints();
        gbc.gridx = 9;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(dataPortField, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 14;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer2, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 10;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer3, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer4, gbc);
        final JPanel spacer5 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 8;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer5, gbc);
        final JPanel spacer6 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(spacer6, gbc);
        final JPanel spacer7 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(spacer7, gbc);
        final JPanel spacer8 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 12;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer8, gbc);
        percentageField = new JLabel();
        percentageField.setText("0%");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(percentageField, gbc);
        final JPanel spacer9 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(spacer9, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

    class datagramSender implements Runnable {
        InetAddress ip;
        File file;

        byte[] fileBytes;
        final byte[] HSBYTE = "##HS".getBytes();
        final byte[] DATABYTE = "DATA".getBytes();
        final byte[] ACKBYTE = "#ACK".getBytes();
        final byte[] EOFBYTE = "#EOF".getBytes();
        final int NUMLENGTH = 8;
        int dataPort, ACKPort, totalDataPacket, totalSentPacket = 0, dataSize, timeout;

        datagramSender() throws IOException {
            ip = InetAddress.getByName(IPField.getText());
            file = Sender.file;
            dataPort = Integer.parseInt(dataPortField.getText());
            ACKPort = Integer.parseInt(ACKPortField.getText());
            dataSize = Integer.parseInt(maxPacketSizeField.getText());
            timeout = Integer.parseInt(timeoutField.getText());
            readFile(file);
            totalDataPacket = turn_left();
            updateProgressField(0, totalDataPacket);
        }

        @Override
        public void run() {
            btnTransfer.setEnabled(false);
            btnBrowse.setEnabled(false);
            statusLabel.setText("Sending...");
            DatagramSocket dataSocket;
            DatagramSocket ACKsocket;
            DatagramPacket[] packets = new DatagramPacket[fileBytes.length + 4];

            try {
                dataSocket = new DatagramSocket();
            } catch (IOException e) {
                infoBox("Failed to create/connect dataSocket on port: " + dataPort, "Send Failed");
                e.printStackTrace();
                return;
            }
            try {
                ACKsocket = new DatagramSocket(ACKPort);
                ACKsocket.setSoTimeout(timeout);
            } catch (IOException e) {
                infoBox("Failed to create/connect ACKsocket on port: " + ACKPort, "Send Failed");
                e.printStackTrace();
                return;
            }
            packets[packets.length - 1] = prepareHS(0);
            packets[packets.length - 2] = prepareHS(1);
            packets[packets.length - 3] = prepareHS(2);
            packets[packets.length - 4] = prepareEOF();


            for (int i = 0; i < fileBytes.length; i += dataSize) packets[i] = prepareDATA(i, fileBytes);

            long startTime = System.currentTimeMillis(), elapsedTime;
            int i = packets.length - 1;
            while (true) {
                String[] parsedPacket = parsePacket(packets[i]);
                System.out.println("Sending " + parsedPacket[0] + " packet #" + parsedPacket[1]);
                if (parsedPacket[0].equals("##HS")) {
                    if (Integer.parseInt(parsedPacket[1]) == 0 || Integer.parseInt(parsedPacket[1]) == 2) {
                        System.out.println("Transferring packetSize/fileSize: " + parsedPacket[2]);
                    } else {
                        System.out.println("Transferring File name: " + parsedPacket[2]);
                    }
                } else if (parsedPacket[0].equals("DATA")) {
//                    System.out.println("Data: " + parsedPacket[2]);
                }
                try {
                    dataSocket.send(packets[i]);
                    totalSentPacket++;
                    updateProgressField(totalSentPacket, totalDataPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] in_data = new byte[DATABYTE.length + NUMLENGTH + dataSize];
                DatagramPacket receivedPacket = new DatagramPacket(in_data, in_data.length);
                try {
                    ACKsocket.receive(receivedPacket);
                } catch (SocketTimeoutException e) {
                    System.out.println("ACK receiving timed out for " + parsePacket(packets[i])[0] + " packet #" +
                            parsePacket(packets[i])[1] + ". Resending...");
                    totalDataPacket++;
                    lostPacketCountLabel.setText("" + (Integer.parseInt(lostPacketCountLabel.getText()) + 1));
                    continue;
                } catch (IOException e) {
                    System.err.println("IOException when receiving ACKs. Aborting.");
                    e.printStackTrace();
                    break;
                }
                if (parsePacket(receivedPacket)[0].equals("##HS")) {
                    if (Integer.parseInt(parsePacket(receivedPacket)[1]) == 0) i = packets.length - 2;
                    else if (Integer.parseInt(parsePacket(receivedPacket)[1]) == 1) i = packets.length - 3;
                    else if (Integer.parseInt(parsePacket(receivedPacket)[1]) == 2) i = 0;
                } else if (parsePacket(receivedPacket)[0].equals("#EOF")) {
                    break;
                } else {
                    if (i + dataSize >= fileBytes.length) i = packets.length - 4;
                    else if (parsePacket(receivedPacket)[1].equals(parsePacket(packets[i])[1])) i += dataSize;
                    else System.out.println("Unexpected ACK#" + parsePacket(receivedPacket)[1] + " received while " +
                                "expecting ACK#" + parsePacket(packets[i])[1] + ". Counted as delayed ACK and " +
                                "resending next packet.");
                }

            }
            dataSocket.close();
            ACKsocket.close();
            elapsedTime = System.currentTimeMillis() - startTime;
            timeElapsedField.setText("" + elapsedTime);
            btnTransfer.setEnabled(true);
            btnBrowse.setEnabled(true);
            statusLabel.setText("Inactive");
        }


        String[] parsePacket(DatagramPacket packet) {
            String[] parsedPacket = new String[3];
            parsedPacket[0] = new String(copyOfRange(packet.getData(), 0, 4));
            parsedPacket[1] = "" + ByteBuffer.wrap(packet.getData(), 4, 8).getInt();
            parsedPacket[2] = new String(copyOfRange(packet.getData(), 12, packet.getData().length + 12));
            if (parsedPacket[0].equals("##HS")) {
                if (Integer.parseInt(parsedPacket[1]) == 0 || Integer.parseInt(parsedPacket[1]) == 2) {
                    parsedPacket[2] = "" + ByteBuffer.wrap(packet.getData(), 12, 8).getInt();
                } else {
                    String temp = parsedPacket[2];
                    parsedPacket[2] = temp.split("\n")[0];
                }
            }
            return parsedPacket;
        }

        DatagramPacket prepareDATA(int offset, byte[] dataByte) {
            ByteBuffer packetBuffer =
                    ByteBuffer.allocate(((offset + dataSize < dataByte.length) ? dataSize : dataByte.length - offset) + NUMLENGTH + 5);
            packetBuffer.put(DATABYTE);
            packetBuffer.put(intToByteArr(offset));
            packetBuffer.put(copyOfRange(dataByte, offset, (offset + dataSize < fileBytes.length) ?
                    (offset + dataSize) : fileBytes.length));
            packetBuffer.put("\0".getBytes());
            return new DatagramPacket(packetBuffer.array(), ((offset + dataSize < dataByte.length) ? dataSize :
                    dataByte.length - offset) + NUMLENGTH + 4, ip, dataPort);
        }

        DatagramPacket prepareHS(int mode) {
            int pointer = 0;
            byte[] packetData = new byte[127];
            if (mode == 0) {
                System.arraycopy(HSBYTE, 0, packetData, pointer, HSBYTE.length);
                pointer += HSBYTE.length;
                System.arraycopy(intToByteArr(0), 0, packetData, pointer, NUMLENGTH);
                pointer += NUMLENGTH;
                System.arraycopy(intToByteArr(dataSize), 0, packetData, pointer, NUMLENGTH);
            } else if (mode == 1) {
                System.arraycopy(HSBYTE, 0, packetData, pointer, HSBYTE.length);
                pointer += HSBYTE.length;
                System.arraycopy(intToByteArr(1), 0, packetData, pointer, NUMLENGTH);
                pointer += NUMLENGTH;
                System.arraycopy(file.getName().getBytes(), 0, packetData, pointer, file.getName().length());
                pointer += file.getName().length();
                System.arraycopy("\n".getBytes(), 0, packetData, pointer, 1);
            } else if (mode == 2) {
                System.arraycopy(HSBYTE, 0, packetData, pointer, HSBYTE.length);
                pointer += HSBYTE.length;
                System.arraycopy(intToByteArr(2), 0, packetData, pointer, NUMLENGTH);
                pointer += NUMLENGTH;
                System.arraycopy(intToByteArr(fileBytes.length), 0, packetData, pointer, NUMLENGTH);
                pointer += NUMLENGTH;
                System.arraycopy("\n".getBytes(), 0, packetData, pointer, 1);
            } else {
                throw new IllegalArgumentException("Only mode 0 and 1 is allowed.");
            }
            return new DatagramPacket(packetData, packetData.length, ip, dataPort);
        }

        DatagramPacket prepareEOF() {
            int pointer = 0;
            byte[] packetData = new byte[dataSize];
            System.arraycopy(EOFBYTE, 0, packetData, pointer, HSBYTE.length);
            pointer += EOFBYTE.length;
            System.arraycopy(intToByteArr(0), 0, packetData, pointer, NUMLENGTH);
            return new DatagramPacket(packetData, packetData.length, ip, dataPort);
        }

        byte[] intToByteArr(int i) {
            return ByteBuffer.allocate(NUMLENGTH).putInt(i).array();
        }

        byte[] copyOfRange(byte[] srcArr, int start, int end) {
            int length = (end > srcArr.length) ? srcArr.length - start : end - start;
            byte[] destArr = new byte[length];
            System.arraycopy(srcArr, start, destArr, 0, length);
            return destArr;
        }

        private void readFile(File file) throws IOException {
            try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
                // Get and check length
                long longLength = f.length();
                int intLength = (int) longLength;
                if (intLength != longLength)
                    throw new IOException("File sized greater than 2 GB is not Supported.");
                // Read filePath and return data
                this.fileBytes = new byte[intLength];
                f.readFully(this.fileBytes);
            }
        }
    }
}