package com.github.smartbooks.abtest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

public class ZookeeperClientWatcher implements Watcher {

    private String charset = "utf-8";
    private String rootNodePath = "bitmusic";
    private String moduleNodePath = "abtest";
    private String subjectNodePath = "subject";
    private String liveNodePath = "live";
    private String liveNodeId = "-1";
    private String zookeeperHost = "localhost:2181";
    private Integer zookeeperConnectionTimeout = 30000;
    private ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LogManager.getLogger(ZookeeperClientWatcher.class);
    private ZooKeeper zooKeeper = null;

    private ExperimentMatrix experimentMatrix = new ExperimentMatrix();

    public ExperimentMatrix getExperimentMatrix() {
        return experimentMatrix;
    }

    public void setExperimentMatrix(ExperimentMatrix experimentMatrix) {
        this.experimentMatrix = experimentMatrix;
    }

    public void start() {

        zooKeeper = createZookeeper();

        initTreeNode();

        syncExperimentSubject();

    }

    public void stop() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            logger.error(e);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        try {

            logger.debug(String.format("process-%s", watchedEvent));

            String eventPath = watchedEvent.getPath();

            Event.EventType eventType = watchedEvent.getType();

            String subjectPath = String.format("/%s/%s/%s", rootNodePath, moduleNodePath, subjectNodePath);

            if (null != eventPath && eventPath.startsWith(subjectPath)) {

                switch (eventType) {
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeCreated:
                        break;
                    case NodeChildrenChanged:
                        syncExperimentSubject();
                        break;
                    case None:
                        break;
                }

                if (eventPath.startsWith(subjectPath) && watchedEvent.getType() == Event.EventType.NodeDataChanged) {

                    // update bitmusic/abtest/subject/*
                    updateExperimentSubject(eventPath);

                } else if (eventPath.startsWith(subjectPath) && watchedEvent.getType() == Event.EventType.NodeDeleted) {

                    // delete bitmusic/abtest/subject/*
                    deleteExperimentSubject(eventPath);

                }

            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    private void initTreeNode() {

        try {

            //根节点
            String rootPath = String.format("/%s", rootNodePath);
            if (null == zooKeeper.exists(rootPath, false)) {
                zooKeeper.create(rootPath, "".getBytes(charset), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            //模块节点
            String modulePath = String.format("%s/%s", rootPath, moduleNodePath);
            if (null == zooKeeper.exists(modulePath, false)) {
                zooKeeper.create(modulePath, "".getBytes(charset), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            //实验主题节点
            String subjectPath = String.format("%s/%s", modulePath, subjectNodePath);
            if (null == zooKeeper.exists(subjectPath, false)) {
                zooKeeper.create(subjectPath, "".getBytes(charset), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            //在线节点->根节点
            String livePath = String.format("%s/%s", modulePath, liveNodePath);
            if (null == zooKeeper.exists(livePath, false)) {
                zooKeeper.create(livePath, "".getBytes(charset), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            //在线节点->根节点->临时节点
            String curLiveNodePath = String.format("%s/%s", livePath, liveNodeId);
            if (null == zooKeeper.exists(curLiveNodePath, false)) {
                zooKeeper.create(curLiveNodePath, "".getBytes(charset), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    private void syncExperimentSubject() {

        try {

            logger.debug("syncExperimentSubject");

            String subjectPath = String.format("/%s/%s/%s", rootNodePath, moduleNodePath, subjectNodePath);

            List<String> childrenSubjectList = zooKeeper.getChildren(subjectPath, true);

            if (null != childrenSubjectList && childrenSubjectList.isEmpty() == false) {

                for (String subjectNode : childrenSubjectList) {

                    String subjectFullPath = String.format("%s/%s", subjectPath, subjectNode);

                    Stat stat = zooKeeper.exists(subjectFullPath, false);

                    if (null != stat) {

                        String subjectJsonContent = new String(zooKeeper.getData(subjectFullPath, true, stat));

                        if (null != subjectJsonContent && subjectJsonContent.isEmpty() == false) {

                            ExperimentSubject experimentSubject = objectMapper.readValue(subjectJsonContent, ExperimentSubject.class);

                            if (null != experimentSubject) {

                                experimentMatrix.getExperimentSubjectMap().put(subjectNode, experimentSubject);

                                logger.debug(String.format("cmd:%s subject:%s", subjectNode, experimentSubject));

                            }

                        }

                    }

                }

            }

        } catch (Exception e) {
            logger.error(e);
        }

    }

    private void updateExperimentSubject(String path) {

        try {

            logger.debug(String.format("updateExperimentSubject-%s", path));

            String[] pathArray = path.split("\\/");

            String cmd = pathArray[pathArray.length - 1];

            Stat stat = zooKeeper.exists(path, false);

            if (null != stat) {

                String subjectJsonContent = new String(zooKeeper.getData(path, true, stat));

                if (null != subjectJsonContent && subjectJsonContent.isEmpty() == false) {

                    ExperimentSubject experimentSubject = objectMapper.readValue(subjectJsonContent, ExperimentSubject.class);

                    if (null != experimentSubject) {

                        experimentMatrix.getExperimentSubjectMap().put(cmd, experimentSubject);

                    }

                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void deleteExperimentSubject(String path) {

        try {

            logger.debug(String.format("deleteExperimentSubject-%s", path));

            String[] pathArray = path.split("\\/");

            String cmd = pathArray[pathArray.length - 1];

            experimentMatrix.getExperimentSubjectMap().remove(cmd);

        } catch (Exception e) {
            logger.error(e);
        }

    }

    private ZooKeeper createZookeeper() {

        ZooKeeper zooKeeper = null;

        try {

            zooKeeper = new ZooKeeper(zookeeperHost, zookeeperConnectionTimeout, this);

        } catch (IOException e) {
            logger.error(e);
        }

        return zooKeeper;
    }
}
