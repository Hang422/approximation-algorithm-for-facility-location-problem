package Dual.Components; /**
 * projectName: FLP3.0
 * fileName: Dual.Algorithm.DualAlgorithm.ClientBucket.java
 * packageName: PACKAGE_NAME
 * date: 2023-05-19 19:53
 * copyright(c) 2017-2020 xxx公司
 */

import java.util.ArrayList;
import java.util.Collections;

/**
 * @version: V1.0
 * @author: Hang Luo  
 * @className: Dual.Algorithm.DualAlgorithm.ClientBucket
 * @packageName: PACKAGE_NAME
 * @description:
 * @data: 2023-05-19 19:53
 **/
public class ClientBucket extends Bucket {
    private ArrayList<Line> lines;
    private Client client;

    @Override
    public String toString() {
        return "Dual.Algorithm.DualAlgorithm.ClientBucket{" +
                "client=" + client +
                '}';
    }

    public ClientBucket(ArrayList<Line> lines, Client client) {
        Collections.sort(lines);
        this.client = client;
        this.lines = lines;
    }

    public Client getClient() {
        return client;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

}