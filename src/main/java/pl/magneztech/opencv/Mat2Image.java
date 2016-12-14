package pl.magneztech.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.Video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

public class Mat2Image {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    Mat mat = new Mat();
    BufferedImage img;
    byte[] dat;
    BackgroundSubtractorKNN backgroundSubtractor;

    public Mat2Image() {
        backgroundSubtractor = Video.createBackgroundSubtractorKNN();
    }

    public Mat2Image(Mat mat) {
        getSpace(mat);
    }

    public void getSpace(Mat mat) {
        this.mat = mat;
        int w = mat.cols(), h = mat.rows();
        if (dat == null || dat.length != w * h * 3)
            dat = new byte[w * h * 3];
        if (img == null || img.getWidth() != w || img.getHeight() != h || img.getType() != BufferedImage.TYPE_3BYTE_BGR)
            img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
    }

    public BufferedImage matToBufferedImage(Mat frame) {
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        return image;
    }

    BufferedImage getImage(Mat originalImage) {
        Mat withoutBackground = new Mat();
        Mat grayImage = new Mat();
        Mat cannyImage = new Mat();
        Mat result = new Mat();
//        backgroundSubtractor.apply(originalImage, fgMask, 1);
//        originalImage.copyTo(withoutBackground, fgMask);
        originalImage.copyTo(withoutBackground);
        Imgproc.cvtColor(withoutBackground, grayImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Canny(grayImage, cannyImage, 100, 200);
        List<MatOfPoint> matOfPoints = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Core.inRange(cannyImage, new Scalar(0, 0, 0), new Scalar(0, 0, 0), result);
        Imgproc.findContours(cannyImage, matOfPoints, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_KCOS);

        List<Rect> rects = new ArrayList<Rect>();
        for (MatOfPoint matOfPoint : matOfPoints) {
            MatOfPoint2f thisContour2f = new MatOfPoint2f();
            MatOfPoint approxContour = new MatOfPoint();
            MatOfPoint2f approxContour2f = new MatOfPoint2f();
            new MatOfPoint2f(matOfPoint).convertTo(thisContour2f, CvType.CV_32FC2);
            Imgproc.approxPolyDP(thisContour2f, approxContour2f, 2, true);
            approxContour2f.convertTo(approxContour, CvType.CV_32S);
            if (approxContour.size().height == 4) {
                rects.add(Imgproc.boundingRect(approxContour));
            }
        }


//        CascadeClassifier detector = new CascadeClassifier("etc/haarcascades/hand_cascade.xml");
//        MatOfRect faceDetections = new MatOfRect();
//        detector.detectMultiScale(result, faceDetections);
//        Point handCenter;
//        for (Rect rect : faceDetections.toArray()) {
//            Imgproc.rectangle(result, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
//            handCenter = new Point(rect.x + rect.width, rect.y + rect.height / 2);
//        }

        getSpace(originalImage);
        originalImage.get(0, 0, dat);
        return matToBufferedImage(result);
    }

}

