package Dual.Components; /**
 * projectName: FLP3.0
 * fileName: Dual.Algorithm.DualAlgorithm.Client.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 19:49
 * copyright(c) 2017-2020 xxx公司
 */

import java.util.ArrayList;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Dual.Algorithm.DualAlgorithm.Client
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 19:49
 **/
public class Client {
    private int level;
    private final int number;
    private ClientBucket bucket;
    private Facility facility;
    private static final double INFINITE = 999999999.0;



    public Client(int level, int number) {
        this.facility = null;
        this.level = level;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public double nextWaterLineValue(ArrayList<Facility> facilities, double additionOnWaterLine){
        for(Line line : this.bucket.getLines()){
            if(line.getFacility().getLevel()>=level && this.bucket.getCurrent()+additionOnWaterLine > line.getValue())
                return line.getValue();
        }
        return INFINITE;
    }


    public boolean isUpdate(){
        for(Line line : this.bucket.getLines()){
            if(line.getFacility().getLevel()>=level && this.bucket.getCurrent() >= line.getValue()){
                this.setFacility(line.getFacility());
                return true;
            }
        }
        return false;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ClientBucket getBucket() {
        return bucket;
    }

    public void setBucket(ClientBucket bucket) {
        this.bucket = bucket;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    @Override
    public String toString() {
        return "Client{" +
                "facility=" + facility +
                '}';
    }
}