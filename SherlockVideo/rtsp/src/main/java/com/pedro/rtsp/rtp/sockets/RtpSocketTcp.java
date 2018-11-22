package com.pedro.rtsp.rtp.sockets;

import android.util.Log;
import com.pedro.rtsp.rtsp.RtpFrame;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import java.io.IOException;
import java.io.OutputStream;

public class RtpSocketTcp extends BaseRtpSocket {

  private OutputStream outputStream;
  private byte tcpHeader[];

  public RtpSocketTcp(ConnectCheckerRtsp connectCheckerRtsp) {
    super(connectCheckerRtsp);
    tcpHeader = new byte[] { '$', 0, 0, 0 };
  }

  @Override
  public void setDataStream(OutputStream outputStream, String host) {
    this.outputStream = outputStream;
  }

  @Override
  public void sendFrame(RtpFrame rtpFrame) {
    try {
      sendFrameTCP(rtpFrame);
    } catch (IOException e) {
      Log.e(TAG, "TCP send error: ", e);
      connectCheckerRtsp.onConnectionFailedRtsp("Error send packet, " + e.getMessage());
    }
  }

  @Override
  public void close() {

  }

  private void sendFrameTCP(RtpFrame rtpFrame) throws IOException {
    synchronized (outputStream) {
      int len = rtpFrame.getLength();
      tcpHeader[1] = rtpFrame.getChannelIdentifier();
      tcpHeader[2] = (byte) (len >> 8);
      tcpHeader[3] = (byte) (len & 0xFF);
      outputStream.write(tcpHeader);
      outputStream.write(rtpFrame.getBuffer(), 0, len);
      outputStream.flush();
      Log.i(TAG, "wrote packet: "
          + (rtpFrame.getChannelIdentifier() == (byte) 2 ? "Video" : "Audio")
          + ", size: "
          + rtpFrame.getLength());
    }
  }
}
