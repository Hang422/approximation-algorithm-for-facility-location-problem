package Dual.Components; /**
 * projectName: FLP3.0
 * fileName: Dual.Algorithm.DualAlgorithm.Line.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 19:49
 * copyright(c) 2017-2020 xxx公司
 */

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Dual.Algorithm.DualAlgorithm.Line
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 19:49
 **/
public class Line implements Comparable{
    private Facility facility;
    private double value;

    public Line(Facility facility,double v) {
        this.facility = facility;
        this.value = v;
    }

    public double getValue() {
        return value;
    }

    public Facility getFacility() {
        return facility;
    }

    @Override
    public int compareTo(Object o) {
        Line line = (Line) o;
        if(line.value < value)
            return 1;
        else
            return -1;
    }
}