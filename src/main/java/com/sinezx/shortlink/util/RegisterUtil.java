package com.sinezx.shortlink.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import java.util.List;

@Component
public class RegisterUtil {

    private static final Logger log = LoggerFactory.getLogger(RegisterUtil.class);

    private final static String DEFAULT_SERVER_ID = "00";

    @Autowired
    public CuratorFramework curatorFramework;

    public String getServerId(String path){
        try {
            checkExistAndCreate(path);
            return calServerId(path);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DEFAULT_SERVER_ID;
        }
    }

    private String calServerId(String path) throws Exception {
        String serverId = DEFAULT_SERVER_ID;
        List<String> ids = curatorFramework.getChildren().forPath(path);
        if (ObjectUtils.isEmpty(ids)) {
            curatorFramework.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path + "/" + DEFAULT_SERVER_ID);
        } else {
            //search unused serverid
            for (int i = 0; i < 100; i++) {
                serverId = String.format("%02d", i);
                if (!ids.contains(serverId)) {
                    curatorFramework.create()
                            .withMode(CreateMode.EPHEMERAL)
                            .forPath(path + "/" + serverId);
                    break;
                }
            }
        }
        return serverId;
    }

    private void checkExistAndCreate(String path) throws Exception {
        assert path.length() > 1;
        if(checkNoExist(path)) {
            String[] pathStrArray = path.split("/");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < pathStrArray.length; i++) {
                sb.append("/");
                sb.append(pathStrArray[i]);
                if (checkNoExist(sb.toString())) {
                    curatorFramework.create().forPath(sb.toString());
                }
            }
        }
    }

    private boolean checkNoExist(String path) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(path);
        return stat == null;
    }
}
