package com.github.smartbooks.abtest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * http://zookeeper.apache.org/doc/r3.4.5/javaExample.html
 * <p>
 * zookeeper-client
 */
public class ZookeeperClient {


    public static void main(String[] args) throws IOException {

        Map<String, ExperimentSubject> map = new EchoExperimentFlowObserver()
                .getExperimentMatrix()
                .getExperimentSubjectMap();

        ObjectMapper mapper = new ObjectMapper();

        String valueJson = mapper.writeValueAsString(map.get("com.gituhub.smartbooks.login"));

        ExperimentSubject subject = mapper.readValue(valueJson, ExperimentSubject.class);

        System.out.println(subject);

    }

    public void zkTest() throws IOException, KeeperException, InterruptedException {

        ZookeeperClientExecutor executor = new ZookeeperClientExecutor();

        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 3000, executor);

        Stat stat = zooKeeper.exists("/smartbooks", false);

        while (true) {

            List<String> dir = zooKeeper.getChildren("/smartbooks", false);

            for (String d : dir) {
                System.out.println(d);
            }

            Thread.sleep(3000);

            String str = new String(zooKeeper.getData("/smartbooks", executor, stat));

            System.out.println(String.format("data:%s\n", str));

        }
    }

}
