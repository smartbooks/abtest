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
 * <p>
 * /bitmusic
 *     /abtest
 *         /cmd
 *             com.gituhub.smartbooks.login
 *                 d:{"source":"com.gituhub.smartbooks.login","target":"http://localhost:5000","name":"subject_login","summary":"","experimentlayerList":[{"name":"layer_ui","summary":"","bucketSize":5,"experimentBucketList":[{"name":"bucket_a","summary":"","bucketSet":[0,1,2],"paramMap":{"background":"red","skin":"blue"}},{"name":"bucket_b","summary":"","bucketSet":[3,4],"paramMap":{"background":"white","skin":"black","_target":"http://localhost:5000"}}]},{"name":"layer_ui","summary":"","bucketSize":5,"experimentBucketList":[{"name":"bucket_a","summary":"","bucketSet":[0,1,2],"paramMap":{"background":"red","skin":"blue"}},{"name":"bucket_b","summary":"","bucketSet":[3,4],"paramMap":{"background":"white","skin":"black","_target":"http://localhost:5000"}}]},{"name":"layer_ui","summary":"","bucketSize":5,"experimentBucketList":[{"name":"bucket_a","summary":"","bucketSet":[0,1,2],"paramMap":{"background":"red","skin":"blue"}},{"name":"bucket_b","summary":"","bucketSet":[3,4],"paramMap":{"background":"white","skin":"black","_target":"http://localhost:5000"}}]}]}
 *             com.gituhub.smartbooks.logout
 *         /live
 *             id=100
 *                 d:{"status":"active","url":"http://192.168.1.100:8080/ab/","weight":200}
 *             id=101
 *                 d:{"status":"dead","url":"http://192.168.1.101:8080/ab/","weight":500}
 *         /master
 *             d:id=100
 *
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
