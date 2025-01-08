package Dual.Algorithm; /**
 * projectName: FLP3.0
 * fileName: Main.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 21:31
 * copyright(c) 2017-2020 xxx公司
 */

import Base.MyComponent;
import Base.Problem;
import Dual.Components.*;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Main
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 21:31
 **/
public class DualAlgorithm {

    public static double solve(Problem problem,boolean isClearUp) {

        int LEVEL = Problem.getLEVEL();
        int sizeC = Problem.getSizeC();
        int sizeF = Problem.getSizeF();
        int sizeO = Problem.getSizeO();

        ArrayList<Client> clients = new ArrayList<>();
        ArrayList<Facility> facilities = new ArrayList<>();

        MyComponent myComponent = problem.getMyComponent();
        double [][] matrix = problem.getDistance();

        for(int i=0;i<sizeF;i++){
            Facility facility = new Facility(LEVEL,i,problem.getCost());
            ArrayList<FacilityBucket> facilityBuckets = new ArrayList<>();
            for(int j=0;j<LEVEL;j++){
                FacilityBucket facilityBucket = new FacilityBucket(facility,j+1);
                facilityBuckets.add(facilityBucket);
            }
            facility.setBuckets(facilityBuckets);
            facilities.add(facility);
        }

        for(int i=0;i<sizeC;i++){
            Client client = new Client((int) (problem.getLevel()[i]) , i);
            ArrayList<Line> lines = new ArrayList<>();
            for(int j=0;j<sizeF;j++){
                Line line = new Line(facilities.get(j),matrix[i][j]);
                lines.add(line);
            }
            ClientBucket clientBucket = new ClientBucket(lines,client);
            client.setBucket(clientBucket);
            clients.add(client);
        }

        for(Facility facility : facilities){
            for(int i = 0;i < LEVEL;i++){
                FacilityBucket facilityBucket = facility.getBuckets().get(i);
                for(Client client : clients){
                    if(facilityBucket.getLevel() < client.getLevel());
                    else {
                        facilityBucket.getBuckets().add(client.getBucket());
                    }
                }
                facilityBucket.setContent(facility.getCost().get(i));
            }
        }

        for(Facility facility : facilities){
            for(FacilityBucket facilityBucket : facility.getBuckets()){
                facilityBucket.update(0);
            }
        }

        while (!Facility.isFinished(clients,sizeC,sizeO)){
//            FacilityBucket minNeedFB = Facility.FindMinNeed(facilities);
            double target;
            target = Facility.GetTarget(Facility.FindMinNeed(facilities),clients,facilities);
            for(Client client:clients){
                if(client.getFacility()==null){
                    client.getBucket().setCurrent(target);
                    if(client.isUpdate()){
                        for(Facility facility:facilities)
                            facility.update(client);
                    }
                }
                if(Facility.isFinished(clients,sizeC,sizeO))
                    break;
            }
            for(Facility facility : facilities){
                for(FacilityBucket facilityBucket : facility.getBuckets()){
                    facilityBucket.update(target);
                }
            }
        }

        if(isClearUp)
            Facility.clearUp(clients,facilities,matrix);

        double totalCostDual = 0;

        for(Client client:clients){
            if(client.getFacility()!=null){
                totalCostDual += matrix[client.getNumber()][client.getFacility().getNumber()];
                if(client.getFacility().getLevel()<client.getLevel())
                    System.out.println("error");
            }
        }


        //Draw result
        myComponent.setFacilities(facilities);
        myComponent.setClients(clients);
        Base.MyFrame myFrame = new Base.MyFrame(myComponent);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);

        return totalCostDual;
    }
}