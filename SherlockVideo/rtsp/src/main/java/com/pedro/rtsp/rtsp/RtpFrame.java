package com.pedro.rtsp.rtsp;

public class RtpFrame {

  private byte[] buffer;
  private long timeStamp;
  private int length;
  private int rtpPort; //rtp udp
  private int rtcpPort; //rtcp udp
  private byte channelIdentifier; //rtcp tcp

  public RtpFrame(byte[] buffer, long timeStamp, int length, int rtpPort, int rtcpPort,
      byte channelIdentifier) {
    this.buffer = buffer;
    this.timeStamp = timeStamp;
    this.length = length;
    this.rtpPort = rtpPort;
    this.rtcpPort = rtcpPort;
    this.channelIdentifier = channelIdentifier;
  }

  public byte[] getBuffer() {
    return buffer;
  }

  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getRtpPort() {
    return rtpPort;
  }

  public void setRtpPort(int rtpPort) {
    this.rtpPort = rtpPort;
  }

  public int getRtcpPort() {
    return rtcpPort;
  }

  public void setRtcpPort(int rtcpPort) {
    this.rtcpPort = rtcpPort;
  }

  public byte getChannelIdentifier() {
    return channelIdentifier;
  }

  public void setChannelIdentifier(byte channelIdentifier) {
    this.channelIdentifier = channelIdentifier;
  }
}
