package pl.magneztech.opencv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
//        CascadeClassifier faceDetector = new CascadeClassifier("etc/lbpcascades/lbpcascade_frontalface.xml");
        CascadeClassifier faceDetector = new CascadeClassifier("etc/haarcascades/hand.xml");
//        VideoCapture camera = new VideoCapture(0);

        Mat image = Imgcodecs.imread("good-bye.jpg");
//        Mat image = new Mat();
//        camera.read(image);

        BackgroundSubtractorMOG2 backgroundSubtractorMOG = Video.createBackgroundSubtractorMOG2();
        Mat fgMask = new Mat();
        backgroundSubtractorMOG.apply(image, fgMask, 0.1);
        Mat output = new Mat();
        image.copyTo(output, fgMask);

        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s shapes", faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        String filename = "wynik.jpg";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
        String filename2 = "wynik2.jpg";
        System.out.println(String.format("Writing %s", filename2));
        Imgcodecs.imwrite(filename2, output);
//        camera.release();
    }
}
