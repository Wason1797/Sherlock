package com.pedro.encoder.input.video;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by pedro on 20/01/17.
 * This class need use same resolution, fps and imageFormat that VideoEncoder
 * Tested with YV12 and NV21.
 *
 * Advantage = you can control fps of the stream.
 * Disadvantages = you cant use all resolutions, only resolution that your camera support.
 *
 * If you want use all resolutions. You can use libYuv for resize images in OnPreviewFrame:
 * https://chromium.googlesource.com/libyuv/libyuv/
 */

public class Camera1ApiManager implements Camera.PreviewCallback, Camera.FaceDetectionListener {

  private String TAG = "Camera1ApiManager";
  private Camera camera = null;
  private SurfaceView surfaceView;
  private TextureView textureView;
  private SurfaceTexture surfaceTexture;
  private GetCameraData getCameraData;
  private boolean running = false;
  private boolean lanternEnable = false;
  private int cameraSelect;
  private boolean isFrontCamera = false;
  private int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;

  //default parameters for camera
  private int width = 640;
  private int height = 480;
  private int fps = 30;
  private int rotation = 0;
  private int imageFormat = ImageFormat.NV21;
  private byte[] yuvBuffer;
  private List<Camera.Size> previewSizeBack;
  private List<Camera.Size> previewSizeFront;
  private float distance;

  //Face detector
  public interface FaceDetectorCallback {
    void onGetFaces(Camera.Face[] faces);
  }

  private FaceDetectorCallback faceDetectorCallback;

  public Camera1ApiManager(SurfaceView surfaceView, GetCameraData getCameraData) {
    this.surfaceView = surfaceView;
    this.getCameraData = getCameraData;
    init();
  }

  public Camera1ApiManager(TextureView textureView, GetCameraData getCameraData) {
    this.textureView = textureView;
    this.getCameraData = getCameraData;
    init();
  }

  public Camera1ApiManager(SurfaceTexture surfaceTexture) {
    this.surfaceTexture = surfaceTexture;
    init();
  }

  private void init() {
    cameraSelect = selectCameraFront();
    previewSizeFront = getPreviewSize();
    cameraSelect = selectCameraBack();
    previewSizeBack = getPreviewSize();
  }

  public void setRotation(int rotation) {
    this.rotation = rotation;
  }

  public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
    this.surfaceTexture = surfaceTexture;
  }

  public void start(@Camera1Facing int cameraFacing, int width, int height, int fps) {
    this.width = width;
    this.height = height;
    this.fps = fps;
    this.cameraFacing = cameraFacing;
    cameraSelect = (cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) ? selectCameraBack()
        : selectCameraFront();
    start();
  }

  public void start(int width, int height, int fps) {
    start(cameraFacing, width, height, fps);
  }

  private void start() {
    if (!checkCanOpen()) {
      throw new CameraOpenException("This camera resolution cant be opened");
    }
    yuvBuffer = new byte[width * height * 3 / 2];
    if (camera == null) {
      try {
        camera = Camera.open(cameraSelect);
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraSelect, info);
        isFrontCamera = info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;

        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(width, height);
        parameters.setPreviewFormat(imageFormat);
        int[] range = adaptFpsRange(fps, parameters.getSupportedPreviewFpsRange());
        parameters.setPreviewFpsRange(range[0], range[1]);

        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if (supportedFocusModes != null && !supportedFocusModes.isEmpty()) {
          if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
          } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
          } else {
            parameters.setFocusMode(supportedFocusModes.get(0));
          }
        }
        camera.setParameters(parameters);
        camera.setDisplayOrientation(rotation);
        if (surfaceView != null) {
          camera.setPreviewDisplay(surfaceView.getHolder());
          camera.addCallbackBuffer(yuvBuffer);
          camera.setPreviewCallbackWithBuffer(this);
        } else if (textureView != null) {
          camera.setPreviewTexture(textureView.getSurfaceTexture());
          camera.addCallbackBuffer(yuvBuffer);
          camera.setPreviewCallbackWithBuffer(this);
        } else {
          camera.setPreviewTexture(surfaceTexture);
        }
        camera.startPreview();
        running = true;
        Log.i(TAG, width + "X" + height);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      Log.e(TAG, "Camera1ApiManager need be prepared, Camera1ApiManager not enabled");
    }
  }

  public void setPreviewOrientation(final int orientation) {
    this.rotation = orientation;
    if (camera != null && running) {
      camera.stopPreview();
      camera.setDisplayOrientation(orientation);
      camera.startPreview();
    }
  }

  public void setZoom(MotionEvent event) {
    try {
      if (camera != null && running && camera.getParameters() != null && camera.getParameters()
          .isZoomSupported()) {
        android.hardware.Camera.Parameters params = camera.getParameters();
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = CameraHelper.getFingerSpacing(event);

        if (newDist > distance) {
          if (zoom < maxZoom) {
            zoom++;
          }
        } else if (newDist < distance) {
          if (zoom > 0) {
            zoom--;
          }
        }

        distance = newDist;
        params.setZoom(zoom);
        camera.setParameters(params);
      }
    } catch (Exception e) {
      Log.e(TAG, "Error", e);
    }
  }

  private int selectCameraBack() {
    int number = Camera.getNumberOfCameras();
    for (int i = 0; i < number; i++) {
      Camera.CameraInfo info = new Camera.CameraInfo();
      Camera.getCameraInfo(i, info);
      if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
        return i;
      } else {
        cameraSelect = i;
      }
    }
    return cameraSelect;
  }

  private int selectCameraFront() {
    int number = Camera.getNumberOfCameras();
    for (int i = 0; i < number; i++) {
      Camera.CameraInfo info = new Camera.CameraInfo();
      Camera.getCameraInfo(i, info);
      if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        return i;
      } else {
        cameraSelect = i;
      }
    }
    return cameraSelect;
  }

  public void stop() {
    if (camera != null) {
      camera.stopPreview();
      camera.setPreviewCallback(null);
      camera.setPreviewCallbackWithBuffer(null);
      camera.release();
      camera = null;
    }
    running = false;
  }

  public boolean isRunning() {
    return running;
  }

  private int[] adaptFpsRange(int expectedFps, List<int[]> fpsRanges) {
    expectedFps *= 1000;
    int[] closestRange = fpsRanges.get(0);
    int measure = Math.abs(closestRange[0] - expectedFps) + Math.abs(closestRange[1] - expectedFps);
    for (int[] range : fpsRanges) {
      if (range[0] <= expectedFps && range[1] >= expectedFps) {
        int curMeasure = Math.abs(range[0] - expectedFps) + Math.abs(range[1] - expectedFps);
        if (curMeasure < measure) {
          closestRange = range;
          measure = curMeasure;
        }
      }
    }
    return closestRange;
  }

  @Override
  public void onPreviewFrame(byte[] data, Camera camera) {
    getCameraData.inputYUVData(new Frame(data, rotation, isFrontCamera, imageFormat));
    camera.addCallbackBuffer(yuvBuffer);
  }

  /**
   * See: https://developer.android.com/reference/android/graphics/ImageFormat.html to know name of
   * constant values
   * Example: 842094169 -> YV12, 17 -> NV21
   */
  public List<Integer> getCameraPreviewImageFormatSupported() {
    List<Integer> formats;
    if (camera != null) {
      formats = camera.getParameters().getSupportedPreviewFormats();
      for (Integer i : formats) {
        Log.i(TAG, "camera format supported: " + i);
      }
    } else {
      camera = Camera.open(cameraSelect);
      formats = camera.getParameters().getSupportedPreviewFormats();
      camera.release();
      camera = null;
    }
    return formats;
  }

  private List<Camera.Size> getPreviewSize() {
    List<Camera.Size> previewSizes;
    Camera.Size maxSize;
    if (camera != null) {
      maxSize = getMaxEncoderSizeSupported();
      previewSizes = camera.getParameters().getSupportedPreviewSizes();
    } else {
      camera = Camera.open(cameraSelect);
      maxSize = getMaxEncoderSizeSupported();
      previewSizes = camera.getParameters().getSupportedPreviewSizes();
      camera.release();
      camera = null;
    }
    //discard preview more high than device can record
    Iterator<Camera.Size> iterator = previewSizes.iterator();
    while (iterator.hasNext()) {
      Camera.Size size = iterator.next();
      if (size.width > maxSize.width || size.height > maxSize.height) {
        Log.i(TAG, size.width + "X" + size.height + ", not supported for encoder");
        iterator.remove();
      }
    }
    return previewSizes;
  }

  public List<Camera.Size> getPreviewSizeBack() {
    return previewSizeBack;
  }

  public List<Camera.Size> getPreviewSizeFront() {
    return previewSizeFront;
  }

  /**
   * @return max size that device can record.
   */
  private Camera.Size getMaxEncoderSizeSupported() {
    if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_2160P)) {
      return camera.new Size(3840, 2160);
    } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_1080P)) {
      return camera.new Size(1920, 1080);
    } else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)) {
      return camera.new Size(1280, 720);
    } else {
      return camera.new Size(640, 480);
    }
  }

  public boolean isFrontCamera() {
    return isFrontCamera;
  }

  public void switchCamera() throws CameraOpenException {
    if (camera != null) {
      int oldCamera = cameraSelect;
      int number = Camera.getNumberOfCameras();
      for (int i = 0; i < number; i++) {
        if (cameraSelect != i) {
          cameraSelect = i;
          if (!checkCanOpen()) {
            cameraSelect = oldCamera;
            throw new CameraOpenException("This camera resolution cant be opened");
          }
          stop();
          cameraFacing = cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK
              ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
          start();
          return;
        }
      }
    }
  }

  private boolean checkCanOpen() {
    List<Camera.Size> previews;
    if (cameraSelect == selectCameraBack()) {
      previews = previewSizeBack;
    } else {
      previews = previewSizeFront;
    }
    for (Camera.Size size : previews) {
      if (size.width == width && size.height == height) {
        return true;
      }
    }
    return false;
  }

  public boolean isLanternEnable() {
    return lanternEnable;
  }

  /**
   * @required: <uses-permission android:name="android.permission.FLASHLIGHT"/>
   */
  public void enableLantern() {
    if (camera != null) {
      Camera.Parameters parameters = camera.getParameters();
      List<String> supportedFlashModes = parameters.getSupportedFlashModes();
      if (supportedFlashModes != null && !supportedFlashModes.isEmpty()) {
        if (supportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
          parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
          camera.setParameters(parameters);
          lanternEnable = true;
        } else {
          Log.e(TAG, "Lantern unsupported");
        }
      }
    }
  }

  /**
   * @required: <uses-permission android:name="android.permission.FLASHLIGHT"/>
   */
  public void disableLantern() {
    if (camera != null) {
      Camera.Parameters parameters = camera.getParameters();
      parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
      camera.setParameters(parameters);
      lanternEnable = false;
    }
  }

  public void enableFaceDetection(FaceDetectorCallback faceDetectorCallback) {
    if (camera != null) {
      this.faceDetectorCallback = faceDetectorCallback;
      camera.setFaceDetectionListener(this);
      camera.startFaceDetection();
    }
  }

  public void disableFaceDetection() {
    if (camera != null) {
      faceDetectorCallback = null;
      camera.stopFaceDetection();
      camera.setFaceDetectionListener(null);
    }
  }

  public boolean isFaceDetectionEnabled() {
    return faceDetectorCallback != null;
  }

  @Override
  public void onFaceDetection(Camera.Face[] faces, Camera camera) {
    if (faceDetectorCallback != null) faceDetectorCallback.onGetFaces(faces);
  }
}
