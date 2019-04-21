package util;

public class KalmanFilter {

    private final float MinAccuracy = 1;
    private float Q_metres_per_second;
    private long TimeStamp_milliseconds;
    private double x;
    private double y;
    private float variance;

    public KalmanFilter(float q_metres_per_second) {
        Q_metres_per_second = q_metres_per_second;
        variance = -1;

    }

    public long get_TimeStamp() { return TimeStamp_milliseconds; }


    public double getX() { return x; }

    public double getY() { return y; }

    public float get_accuracy() { return (float)Math.sqrt(variance); }

    public void SetState(double x, double y, float accuracy, long TimeStamp_milliseconds) {
        this.x=x;
        this.y=y;
        variance = accuracy * accuracy;
        this.TimeStamp_milliseconds=TimeStamp_milliseconds;
    }


    public void Process(double lat_measurement, double lng_measurement, float accuracy, long TimeStamp_milliseconds) {
        if (accuracy <MinAccuracy) accuracy = MinAccuracy;
        if (variance <0) {
            //if variance <0, object is uninitialised, so initialise with current values
            this.TimeStamp_milliseconds = TimeStamp_milliseconds;
            x=lat_measurement; y = lng_measurement; variance = accuracy*accuracy;
        } else {

            //else apply Kalman filter methodology
            long TimeInc_milliseconds = TimeStamp_milliseconds - this.TimeStamp_milliseconds;

            if (TimeInc_milliseconds> 0) {

            //time has moved on, so the uncertainty in the current position increases
                variance += TimeInc_milliseconds * Q_metres_per_second * Q_metres_per_second/1000;

                this.TimeStamp_milliseconds = TimeStamp_milliseconds;
            //TO DO: USE VELOCITY INFORMATION HERE TO GET A BETTER ESTIMATE OF CURRENT POSITION
            }
            //Kalman gain matrix K = Covarariance * Inverse(Covariance + MeasurementVariance)
            //NB: because K is dimensionless, it doesn't matter that variance has different units to lat and lng
            float K = variance/(variance + accuracy * accuracy);
            //apply K
            x += K * (lat_measurement - x);
            y += K * (lng_measurement - y);
            //new Covarariance matrix is (IdentityMatrix - K) * Covarariance
            variance = (1 - K) * variance;
        }

    }


}
