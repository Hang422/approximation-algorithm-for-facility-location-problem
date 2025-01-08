package Heuristic; /**
 * projectName: FLP3.0
 * fileName: simpleFacility.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-22 11:37
 * copyright(c) 2017-2020 xxx公司
 */

import java.util.ArrayList;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: simpleFacility
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-22 11:37
 **/
public class SimpleFacility implements Cloneable{
    private int num;
    private ArrayList<SimpleClient> connectedClients;
    private int level;
    private ArrayList<Double> distance;

    public SimpleFacility(int num, ArrayList<Double> distance) {
        this.num = num;
        this.connectedClients = null;
        this.level = 0;
        this.distance = distance;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public SimpleFacility() {

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayList<Double> getDistance() {
        return distance;
    }

    public void setDistance(ArrayList<Double> distance) {
        this.distance = distance;
    }

    public ArrayList<SimpleClient> getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(ArrayList<SimpleClient> connectedClients) {
        this.connectedClients = connectedClients;
    }

    @Override
    public String toString() {
        return "HeuristicAlgorithm.SimpleFacility{" +
                "num=" + num +
                ", level=" + level +
                '}';
    }

    @Override
    public Object clone() {
        SimpleFacility stu = null;
        try{
            stu = (SimpleFacility) super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }

}