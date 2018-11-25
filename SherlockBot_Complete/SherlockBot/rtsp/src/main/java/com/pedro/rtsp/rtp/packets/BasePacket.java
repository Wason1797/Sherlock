package com.pedro.rtsp.rtp.packets;

import com.pedro.rtsp.utils.RtpConstants;
import java.util.Random;

public class BasePacket {

  protected final static int maxPacketSize = RtpConstants.MTU - 28;
  protected byte channelIdentifier;
  protected int rtpPort;
  protected int rtcpPort;
  private final long clock;
  private int seq = 0;
  private int ssrc;

  public BasePacket(long clock) {
    this.clock = clock;
    ssrc = new Random().nextInt();
  }

  public void setPorts(int rtpPort, int rtcpPort) {
    this.rtpPort = rtpPort;
    this.rtcpPort = rtcpPort;
  }

  public void reset() {
    seq = 0;
    ssrc = new Random().nextInt();
  }

  protected byte[] getBuffer() {
    byte[] buffer = new byte[RtpConstants.MTU];
    buffer[0] = (byte) Integer.parseInt("10000000", 2);
    buffer[1] = (byte) RtpConstants.payloadType;
    setLongSSRC(buffer, ssrc);
    requestBuffer(buffer);
    return buffer;
  }

  protected void updateTimeStamp(byte[] buffer, long timestamp) {
    long ts = timestamp * clock / 1000000000L;
    setLong(buffer, ts, 4, 8);
  }

  protected void setLong(byte[] buffer, long n, int begin, int end) {
    for (end--; end >= begin; end--) {
      buffer[end] = (byte) (n % 256);
      n >>= 8;
    }
  }

  protected void updateSeq(byte[] buffer) {
    setLong(buffer, ++seq, 2, 4);
  }

  protected void markPacket(byte[] buffer) {
    buffer[1] |= 0x80;
  }

  private void setLongSSRC(byte[] buffer, int ssrc) {
    setLong(buffer, ssrc, 8, 12);
  }

  private void requestBuffer(byte[] buffer) {
    buffer[1] &= 0x7F;
  }
}
