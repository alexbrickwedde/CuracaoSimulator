package test;

import java.io.IOException;
import java.io.OutputStream;

import com.fazecast.jSerialComm.SerialPort;

public class Main {

  private static OutputStream m_OS;

  public static void sendString(String s) {
    try {
      System.out.println("Sending:" + s);

      m_OS.write(s.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
  public static void main(String[] args) throws Exception {
    SerialPort m_Port = SerialPort.getCommPort("COM7");
    m_Port.setBaudRate(115200);
    
    String sCmd = "";

    while(true) {
      if (!m_Port.isOpen())
      {
        m_Port.openPort();
        if (!m_Port.isOpen())
        {
          continue;
        }
        System.out.println("Connected");
      }
      
      m_OS = m_Port.getOutputStream();
      
      int iBytes = m_Port.bytesAvailable();
      if (iBytes > 0)
      {
        byte[] readBuffer = new byte[iBytes];
        int numRead = m_Port.readBytes(readBuffer, iBytes);
        if (numRead != iBytes) {
          continue;
        }

        if (false) {
          System.out.print("Readbytes ");
          for(int u = 0; u < iBytes; u++) {
            System.out.print("," + readBuffer[u]);
          }
          System.out.println("");
        }
        
        sCmd += new String(readBuffer);
        sCmd = sCmd.replace("\n", "\r");
        int i13 = sCmd.indexOf("\r");
        if (i13 == 0) {
          sCmd = sCmd.substring(1);
        } else if (i13 > 0) {
          String sTemp = sCmd.substring(0, i13);
          sCmd = sCmd.substring(i13 + 1);
          
          sCmd = sCmd.replaceAll("\r", "");
          sCmd = sCmd.replaceAll("\n", "");
          
          System.out.println("Received:" + sTemp);
          
          if ("r".equals(sTemp)) {
            sendString("0,0,0\r\n");
          } else if ("z".equals(sTemp)) {
            sendString ("0,0,0\r\n");
          }
        }
      }

    }
  }
}
