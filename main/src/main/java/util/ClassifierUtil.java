package util;

import model.IMUData;
import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassifierUtil {

        public static String Classify(AbstractClassifier model, List<IMUData> data, int type) {
        Attribute ax0 = new Attribute("ax0");
        Attribute ay0 = new Attribute("ay0");
        Attribute az0 = new Attribute("az0");
        Attribute gx0 = new Attribute("gx0");
        Attribute gy0 = new Attribute("gy0");
        Attribute gz0 = new Attribute("gz0");

        Attribute ax1 = new Attribute("ax1");
        Attribute ay1 = new Attribute("ay1");
        Attribute az1 = new Attribute("az1");
        Attribute gx1 = new Attribute("gx1");
        Attribute gy1 = new Attribute("gy1");
        Attribute gz1 = new Attribute("gz1");

        Attribute ax2 = new Attribute("ax2");
        Attribute ay2 = new Attribute("ay2");
        Attribute az2 = new Attribute("az2");
        Attribute gx2 = new Attribute("gx2");
        Attribute gy2 = new Attribute("gy2");
        Attribute gz2 = new Attribute("gz2");

        Attribute ax3 = new Attribute("ax3");
        Attribute ay3 = new Attribute("ay3");
        Attribute az3 = new Attribute("az3");
        Attribute gx3 = new Attribute("gx3");
        Attribute gy3 = new Attribute("gy3");
        Attribute gz3 = new Attribute("gz3");

        Attribute ax4 = new Attribute("ax4");
        Attribute ay4 = new Attribute("ay4");
        Attribute az4 = new Attribute("az4");
        Attribute gx4 = new Attribute("gx4");
        Attribute gy4 = new Attribute("gy4");
        Attribute gz4 = new Attribute("gz4");

        Attribute ax5 = new Attribute("ax5");
        Attribute ay5 = new Attribute("ay5");
        Attribute az5 = new Attribute("az5");
        Attribute gx5 = new Attribute("gx5");
        Attribute gy5 = new Attribute("gy5");
        Attribute gz5 = new Attribute("gz5");

        Attribute ax6 = new Attribute("ax6");
        Attribute ay6 = new Attribute("ay6");
        Attribute az6 = new Attribute("az6");
        Attribute gx6 = new Attribute("gx6");
        Attribute gy6 = new Attribute("gy6");
        Attribute gz6 = new Attribute("gz6");

        Attribute ax7 = new Attribute("ax7");
        Attribute ay7 = new Attribute("ay7");
        Attribute az7 = new Attribute("az7");
        Attribute gx7 = new Attribute("gx7");
        Attribute gy7 = new Attribute("gy7");
        Attribute gz7 = new Attribute("gz7");

        Attribute ax8 = new Attribute("ax8");
        Attribute ay8 = new Attribute("ay8");
        Attribute az8 = new Attribute("az8");
        Attribute gx8 = new Attribute("gx8");
        Attribute gy8 = new Attribute("gy8");
        Attribute gz8 = new Attribute("gz8");

        Attribute ax9 = new Attribute("ax9");
        Attribute ay9 = new Attribute("ay9");
        Attribute az9 = new Attribute("az9");
        Attribute gx9 = new Attribute("gx9");
        Attribute gy9 = new Attribute("gy9");
        Attribute gz9 = new Attribute("gz9");

        Attribute ax10 = new Attribute("ax10");
        Attribute ay10 = new Attribute("ay10");
        Attribute az10 = new Attribute("az10");
        Attribute gx10 = new Attribute("gx10");
        Attribute gy10 = new Attribute("gy10");
        Attribute gz10 = new Attribute("gz10");

        Attribute ax11 = new Attribute("ax11");
        Attribute ay11 = new Attribute("ay11");
        Attribute az11 = new Attribute("az11");
        Attribute gx11 = new Attribute("gx11");
        Attribute gy11 = new Attribute("gy11");
        Attribute gz11 = new Attribute("gz11");

        Attribute ax12 = new Attribute("ax12");
        Attribute ay12 = new Attribute("ay12");
        Attribute az12 = new Attribute("az12");
        Attribute gx12 = new Attribute("gx12");
        Attribute gy12 = new Attribute("gy12");
        Attribute gz12 = new Attribute("gz12");

        Attribute ax13 = new Attribute("ax13");
        Attribute ay13 = new Attribute("ay13");
        Attribute az13 = new Attribute("az13");
        Attribute gx13 = new Attribute("gx13");
        Attribute gy13 = new Attribute("gy13");
        Attribute gz13 = new Attribute("gz13");

        Attribute ax14 = new Attribute("ax14");
        Attribute ay14 = new Attribute("ay14");
        Attribute az14 = new Attribute("az14");
        Attribute gx14 = new Attribute("gx14");
        Attribute gy14 = new Attribute("gy14");
        Attribute gz14 = new Attribute("gz14");
        ArrayList<Attribute> fvWekaAttributes = new ArrayList<>();
        Attribute[] attributes = {ax0, ay0, az0, gx0, gy0, gz0,
                ax1, ay1, az1, gx1, gy1, gz1,
                ax2, ay2, az2, gx2, gy2, gz2,
                ax3, ay3, az3, gx3, gy3, gz3,
                ax4, ay4, az4, gx4, gy4, gz4,
                ax5, ay5, az5, gx5, gy5, gz5,
                ax6, ay6, az6, gx6, gy6, gz6,
                ax7, ay7, az7, gx7, gy7, gz7,
                ax8, ay8, az8, gx8, gy8, gz8,
                ax9, ay9, az9, gx9, gy9, gz9,
                ax10, ay10, az10, gx10, gy10, gz10,
                ax11, ay11, az11, gx11, gy11, gz11,
                ax12, ay12, az12, gx12, gy12, gz12,
                ax13, ay13, az13, gx13, gy13, gz13,
                ax14, ay14, az14, gx14, gy14, gz14};
        fvWekaAttributes.addAll(Arrays.asList(attributes));

        List<String> fvClassVal = new ArrayList<>();
        fvClassVal.add("circleCW");
        fvClassVal.add("leftSwap");
        fvClassVal.add("rightSwap");
        fvClassVal.add("upSwap");
        fvClassVal.add("downSwap");
        fvClassVal.add("circleCCW");
        fvClassVal.add("noInteraction");


        Attribute label = new Attribute("label", fvClassVal);
        fvWekaAttributes.add(label);
        Instances dataset = new Instances("predictionData", fvWekaAttributes, 0);
        double[] attValues = new double[91];
        for (int i = 0; i < data.size(); i++) {
            IMUData tmpStruct = data.get(i);
            if (tmpStruct != null) {
                attValues[i * 6] = tmpStruct.ax;
                attValues[i * 6 + 1] = tmpStruct.ay;
                attValues[i * 6 + 2] = tmpStruct.az;
                attValues[i * 6 + 3] = tmpStruct.gx;
                attValues[i * 6 + 4] = tmpStruct.gy;
                attValues[i * 6 + 5] = tmpStruct.gz;
            }
        }


        Instance i1 = new DenseInstance(1.0, attValues);
        dataset.add(i1);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        try {
            double a = model.classifyInstance(dataset.instance(0));
            String returnString = "";
            if (type == 0) {

                switch (Double.toString(a)) {
                    case "0.0":
                        returnString = "leftSwap";
                        break;
                    case "1.0":
                        returnString = "rightSwap";
                        break;
                    case "2.0":
                        returnString = "upSwap";
                        break;
                    case "3.0":
                        returnString = "downSwap";
                        break;
                    case "4.0":
                        returnString = "circleCW";
                        break;
                    case "5.0":
                        returnString = "circleCCW";
                        break;
                    case "6.0":
                        returnString = "noInteraction";

                }
                return returnString;
            } else if (type == 1) {
                switch (Double.toString(a)) {
                    case "0.0":
                        returnString = "pointing";
                        break;
                    case "1.0":
                        returnString = "noInteraction";
                        break;
                }

                return returnString;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
