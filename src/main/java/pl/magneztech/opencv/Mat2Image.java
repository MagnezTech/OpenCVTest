package pl.magneztech.opencv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.image.BufferedImage;

public class Mat2Image {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    Mat mat = new Mat();
    BufferedImage img;
    byte[] dat;

    public Mat2Image() {
    }

    public Mat2Image(Mat mat) {
        getSpace(mat);
    }

    public void getSpace(Mat mat) {
        this.mat = mat;
        int w = mat.cols(), h = mat.rows();
        if (dat == null || dat.length != w * h * 3)
            dat = new byte[w * h * 3];
        if (img == null || img.getWidth() != w || img.getHeight() != h
                || img.getType() != BufferedImage.TYPE_3BYTE_BGR)
            img = new BufferedImage(w, h,
                    BufferedImage.TYPE_3BYTE_BGR);
    }

    BufferedImage getImage(Mat mat) {
        CascadeClassifier faceDetector = new CascadeClassifier("etc/haarcascades/palm.xml");
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(mat, faceDetections);
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
        getSpace(mat);
        mat.get(0, 0, dat);
        img.getRaster().setDataElements(0, 0,
                mat.cols(), mat.rows(), dat);
        return img;
    }
}

