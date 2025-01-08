package Heuristic; /**
 * projectName: FLP3.0
 * fileName: simpleClient.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-22 11:37
 * copyright(c) 2017-2020 xxx公司
 */

import java.util.ArrayList;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: simpleClient
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-22 11:37
 **/
public class SimpleClient implements Comparable, Cloneable{
    private int num;
    private int level;
    private SimpleFacility connectFacility;
    private ArrayList<SimpleFacility> facilities;
    private double sum;

    public SimpleClient(int num,int level) {
        this.num = num;
        this.connectFacility = null;
        this.level = level;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public SimpleFacility getConnectFacility() {
        return connectFacility;
    }

    public void setConnectFacility(SimpleFacility connectFacility) {
        this.connectFacility = connectFacility;
    }

    public ArrayList<SimpleFacility> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<SimpleFacility> facilities) {
        this.facilities = facilities;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int compareTo(Object o) {
        SimpleClient simpleClient = (SimpleClient) o;
        if(simpleClient.sum < sum)
            return 1;
        else
            return -1;
    }

    @Override
    public Object clone() {
        SimpleClient stu = null;
        try{
            stu = (SimpleClient) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }

    @Override
    public String toString() {
        return "SimpleClient{" +
                "level" + level +
                ", connectFacility=" + connectFacility.getNum() +
                ", sum=" + sum +
                '}';
    }
}