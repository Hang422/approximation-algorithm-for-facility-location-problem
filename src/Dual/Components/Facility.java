package Dual.Components; /**
 * projectName: FLP3.0
 * fileName: Dual.Algorithm.DualAlgorithm.Facility.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 19:49
 * copyright(c) 2017-2020 xxx公司
 */

import java.util.ArrayList;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Dual.Algorithm.DualAlgorithm.Facility
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 19:49
 **/
public class Facility {
    private ArrayList<Double> cost = new ArrayList<>();
    private final int number;
    private static final int BIG = 100000;
    private static final int TooBIG = 10000000;
    private static final int LittleBIG = 10000;
    private static final int MIDDLE = 1000;
    private static final int SMALL = 10;
    private static final double INFINITE = 999999999.0;
    private int level;
    private ArrayList<FacilityBucket> buckets = new ArrayList<>();
    private ArrayList<Client> connectedClients = new ArrayList<>();

    public Facility(int LevelRange, int number,double[][] money) {
        for(int i=0;i<LevelRange;i++){
            cost.add(money[number][i]);
        }
        this.level = 0;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Dual.Algorithm.DualAlgorithm.Facility{" +
                "cost=" + cost +
                ", number=" + (number + 1) +
                ", level=" + level +
                '}';
    }

    public void update(Client client){
        for(int i=client.getLevel()-1;i<buckets.size();i++){
            FacilityBucket bucket = buckets.get(i);
            for(Line line:client.getBucket().getLines()){
                if(line.getFacility()==this&&line.getValue()<client.getBucket().getCurrent()){
                    bucket.setContent(bucket.getContent()+client.getBucket().getCurrent()-line.getValue());
                }
            }
        }
    }

    public static void clearUp(ArrayList<Client> clients,ArrayList<Facility> facilities,double[][] matrix){
        for(Client client : clients){
            if(client.getFacility()==null)
                continue;
            for(Line line : client.getBucket().getLines()){
                if(line.getValue()<client.getBucket().getCurrent() && line.getFacility().getLevel()>=client.getLevel() &&line.getFacility()!=client.getFacility()){
                    if(line.getFacility().getLevel() > client.getFacility().getLevel()){
                        client.getFacility().setLevel(-1);
                        client.setFacility(line.getFacility());
                    }
                    else {
                        line.getFacility().setLevel(-1);
                    }
                }
            }
        }
        for(Client client : clients){
            if(client.getFacility()==null)
                continue;
            if(client.getFacility().getLevel()==-1){
                double min = INFINITE;
                for(Facility facility:facilities){
                    if(facility.getLevel() >= client.getLevel() && matrix[client.getNumber()][facility.getNumber()]<min){
                        client.setFacility(facility);
                        min = matrix[client.getNumber()][facility.getNumber()];
                    }
                }
            }
        }
    }

    public static boolean isFinished(ArrayList<Client> clients, int SizeC, int SizeO) {
        int sum = 0;
        for (Client client : clients) {
            if (client.getFacility() != null) {
                sum++;
            }
        }
        if(sum>=(SizeC-SizeO))
            return true;
        else
            return false;
    }

    public static double GetTarget(FacilityBucket minNeedBucket, ArrayList<Client> clients, ArrayList<Facility> facilities) {
        double min = INFINITE;
        if(minNeedBucket==null){
            for (Client client : clients) {
                if (client.nextWaterLineValue(facilities, INFINITE) < min && client.getFacility()==null) {
                    min = client.nextWaterLineValue(facilities, INFINITE);
                }
            }
            return min;
        }
        double target = minNeedBucket.getNeedInWaterLine();
        double addition = target - clients.get(0).getBucket().getCurrent();

        //System.out.println("addition:" + addition);
        for (Client client : clients) {
            if (client.nextWaterLineValue(facilities, addition) < min && min > client.getBucket().getCurrent() && client.getFacility()==null) {
                min = client.nextWaterLineValue(facilities, addition);
                //System.out.println("current" + client.getBucket().getCurrent());
            }
        }

        if (min == INFINITE || min < clients.get(0).getBucket().getCurrent()) {
            minNeedBucket.getFacility().setLevel(minNeedBucket.getLevel());
            minNeedBucket.setCurrent(minNeedBucket.getContent());
            for (FacilityBucket facilityBucket : minNeedBucket.getFacility().getBuckets()) {
//                if (facilityBucket.getLevel() < minNeedBucket.getLevel())
//                    facilityBucket.setFill(true);
            }
            return clients.get(0).getBucket().getCurrent() + addition;

        } else
            return min;
    }

    public static FacilityBucket FindMinNeed(ArrayList<Facility> facilities) {
        double min = INFINITE;
        FacilityBucket target = null;
        for (Facility facility : facilities) {
            for (FacilityBucket facilityBucket : facility.getBuckets()) {
                if (min > facilityBucket.getNeedInWaterLine() && !facilityBucket.isFill()) {
                    min = facilityBucket.getNeedInWaterLine();
                    target = facilityBucket;
                }
            }
        }
        return target;
    }

    public void setLevel(int level) {
            this.level = level;
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<Client> getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(ArrayList<Client> connectedClients) {
        this.connectedClients = connectedClients;
    }

    public void setBuckets(ArrayList<FacilityBucket> buckets) {
        this.buckets = buckets;
    }

    public void connect(Client client){
        client.setFacility(this);
        connectedClients.add(client);
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Double> getCost() {
        return cost;
    }

    public ArrayList<FacilityBucket> getBuckets() {
        return buckets;
    }
}