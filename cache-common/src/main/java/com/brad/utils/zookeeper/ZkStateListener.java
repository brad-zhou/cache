package com.brad.utils.zookeeper;

/**
 * @author zhangyu
 * @create 2017-10-09 10:53
 **/
public interface ZkStateListener {

    /**
     * reconnected
     */
    void reconnected();
}
