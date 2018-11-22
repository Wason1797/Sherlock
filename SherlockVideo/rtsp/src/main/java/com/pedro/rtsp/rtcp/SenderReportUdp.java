package com.pedro.rtsp.rtcp;

import android.util.Log;
import com.pedro.rtsp.rtsp.RtpFrame;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class SenderReportUdp extends BaseSenderReport {

  private MulticastSocket multicastSocket;
  private DatagramPacket datagramPacket = new DatagramPacket(new byte[] { 0 }, 1);

  public SenderReportUdp() {
    super();
    try {
      multicastSocket = new MulticastSocket();
      multicastSocket.setTimeToLive(64);
    } catch (IOException e) {
      Log.e(TAG, "Error", e);
    }
  }

  @Override
  public void setDataStream(OutputStream outputStream, String host) {
    try {
      datagramPacket.setAddress(InetAddress.getByName(host));
    } catch (UnknownHostException e) {
      Log.e(TAG, "Error", e);
    }
  }

  @Override
  public void sendReport(byte[] buffer, RtpFrame rtpFrame, String type, int packetCount,
      int octetCount) throws IOException {
    sendReportUDP(buffer, rtpFrame.getRtcpPort(), type, packetCount, octetCount);
  }

  @Override
  public void close() {
    multicastSocket.close();
  }

  private void sendReportUDP(byte[] buffer, int port, String type, int packet, int octet)
      throws IOException {
    datagramPacket.setData(buffer);
    datagramPacket.setPort(port);
    datagramPacket.setLength(PACKET_LENGTH);
    multicastSocket.send(datagramPacket);
    Log.i(TAG, "wrote report: " + type + ", packets: " + packet + ", octet: " + octet);
  }
}
