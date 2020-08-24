package com.jt;

import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.ItemDesc;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TestRedis {
    //Jedis jedis = new Jedis("192.168.144.128",6379);
    /**
     * spring-redis入门案例
     */
    @Test
    public void testRedis1(){
        jedis.set("1903","1903班");
        System.out.println(jedis.get("1903"));
    }

    /**
     * 超时的用法
     */
    @Test
    public void testRedis2(){
        jedis.setex("abc",100,"英文字母");
        System.out.println(jedis.get("abc"));
        try {
            Thread.sleep(1000*10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(jedis.ttl("abc"));
    }

    /**
     * 锁机制的用法
     */
    @Test
    public void testRedis3(){
        Long flag1 = jedis.setnx("yue", "大酒店");
        Long flag2 = jedis.setnx("yue", "我家");
        System.out.println(flag1+";;;;"+flag2);
        System.out.println(jedis.get("yue"));
    }

    /**
     * 死锁
     */
    @Test
    public void testRedis4(){
        //设定锁和超时时间
        String result1 = jedis.set("yue", "大酒店", "NX", "EX", 20);
        jedis.del("yue");
        String result2 = jedis.set("yue", "我家", "NX", "EX", 20);
        System.out.println(result1+"====="+result2);
    }

    /**
     * hash
     */
    @Test
    public void testRedis5(){
        jedis.hset("user","id","120");
        jedis.hset("user","name","chen");
        jedis.hset("user","salary","1009");
        System.out.println(jedis.hgetAll("user"));
    }

    @Test
    public void testRedis6(){
        jedis.lpush("list","1,2,3,4,5","fdsfsdf");
        System.out.println(jedis.rpop("list"));
    }

    /**
     *
     */
//    @Test
//    public void testRedis7(){
//        //Transaction transaction = jedis.multi();
//        try {
//            transaction.set("aa","aa");
//            transaction.set("bb","bb");
//            transaction.set("cc","cc");
//            transaction.set("dd","dd");
//            int a = 1/0;
//            transaction.exec();
//        }catch (Exception e){
//            e.printStackTrace();
//            transaction.discard();
//        }
//    }
    @Autowired
    private ShardedJedis jedis;

    @Autowired
    private ItemDescMapper mapper;
    /**
     * spring boot整合
     * 查询itemDesc数据,之后缓存处理
     * redis不允许存储对象
     * Object json的类型转换
     */
    public void testRedisItemDesc(){
        String key = "100";
        String result = jedis.get(key);
        if (StringUtils.isEmpty(result)){
            ItemDesc itemDesc = mapper.selectById(key);
            //jedis.set(key,itemDesc);
        }
    }
    /**
     * 测试redis分片
     */
    @Test
    public void testShards() {
        ArrayList<JedisShardInfo> shards = new ArrayList<>();
        String host = "192.168.144.128";
        shards.add(new JedisShardInfo(host, 6379));
        shards.add(new JedisShardInfo(host, 6380));
        shards.add(new JedisShardInfo(host, 6381));
        ShardedJedis jedis = new ShardedJedis(shards);
        jedis.set("1903", "分片测试");
        System.out.println(jedis.get("1903"));
    }

    @Test
    public void testShards1(){
        jedis.set("5555","6666");
        System.out.println(jedis.get("5555"));
    }

    @Test
    public void testSentinel(){
        HashSet<String> set = new HashSet<>();
        set.add("192.168.144.128:26379");
        JedisSentinelPool pool = new JedisSentinelPool("mymaster", set);
        Jedis jedis = pool.getResource();
        jedis.set("fdsff","fsddsfsf");
        System.out.println(jedis.get("fdsff"));
    }
    @Test
    public void testCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.144.128",7000));
        nodes.add(new HostAndPort("192.168.144.128",7001));
        nodes.add(new HostAndPort("192.168.144.128",7002));
        nodes.add(new HostAndPort("192.168.144.128",7003));
        nodes.add(new HostAndPort("192.168.144.128",7004));
        nodes.add(new HostAndPort("192.168.144.128",7005));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("mychen","redis集群搭建完毕");
        System.out.println(jedisCluster.get("mychen"));
    }
}
