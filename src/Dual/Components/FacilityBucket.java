package Dual.Components; /**
 * projectName: FLP3.0
 * fileName: Dual.Algorithm.DualAlgorithm.FacilityBucket.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 19:55
 * copyright(c) 2017-2020 xxx公司
 */

import java.util.ArrayList;
import java.util.Collections;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Dual.Algorithm.DualAlgorithm.FacilityBucket
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 19:55
 **/
public class FacilityBucket extends Bucket {
    private double content;
    private double needInWaterLine;
    private Facility facility;
    private int level;
    private boolean isFill;

    @Override
    public String toString() {
        return "Dual.Algorithm.DualAlgorithm.FacilityBucket{" +
                "content=" + content +
                ", needInWaterLine=" + needInWaterLine +
                ", facility=" + facility +
                ", level=" + level +
                ", buckets=" + buckets +
                '}';
    }

    private ArrayList<ClientBucket> buckets = new ArrayList<>();
    private static final double INFINITE = 999999999.0;

    public FacilityBucket(Facility facility,int level) {
        this.facility = facility;
        this.needInWaterLine = INFINITE;
        this.level = level;
        this.isFill = false;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    public boolean isFill() {
        return this.getCurrent()==this.content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void connectClients(){
        for(ClientBucket clientBucket : buckets){
            this.facility.connect(clientBucket.getClient());
        }
    }

    public void update(double currentWaterLine){
        if(this.isFill)
            this.needInWaterLine = INFINITE;
        else {
            double sum_value;
            double min_need = INFINITE;
            ArrayList<Line> temp = new ArrayList<>();
            for(ClientBucket clientBucket:buckets){
                if(clientBucket.getClient().getFacility()==null){
                    for(Line line:clientBucket.getLines()){
                        if(line.getFacility()==this.facility){
                            temp.add(line);
                        }
                    }
                }
            }
            Collections.sort(temp);
            for(int i=0;i<temp.size();i++){
                sum_value = 0;
                for(int j=0;j<i+1;j++){
                    sum_value += temp.get(j).getValue();
                }
                sum_value += content - this.getCurrent();

                if((sum_value / (i+1)) < min_need) {
                    min_need = sum_value / (i+1);
                }

            }
            this.needInWaterLine = min_need;
        }
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public double getNeedInWaterLine() {
        return needInWaterLine;
    }

    public void setNeedInWaterLine(double needInWaterLine) {
        this.needInWaterLine = needInWaterLine;
    }

    public double getContent() {
        return content;
    }

    public void setContent(double content) {
        this.content = content;
    }

    public ArrayList<ClientBucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(ArrayList<ClientBucket> buckets) {
        this.buckets = buckets;
    }
}