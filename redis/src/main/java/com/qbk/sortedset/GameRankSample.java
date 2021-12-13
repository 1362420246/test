package com.qbk.sortedset;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
/**
 * zset 示例
 * 游戏玩家积分排行榜
 */
public class GameRankSample {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        try {
            //实例密码
            String authString = jedis.auth("123456");
            if (!authString.equals("OK")) {
                System.err.println("AUTH Failed: " + authString);
                return;
            }
            //Key（键）
            String key = "Game name: Keep Running!";
            //清除可能的已有数据
            jedis.del(key);
            //模拟生成若干个游戏玩家
            List<String> playerList = new ArrayList<>();
            for (int i = 0; i < 20; ++i) {
                //为每个玩家随机生成一个ID
                playerList.add(UUID.randomUUID().toString());
            }
            System.out.println("Inputs all players ");
            //记录每个玩家的得分
            for (int i = 0; i < playerList.size(); i++) {
                //随机生成数字，模拟玩家的游戏得分
                int score = (int) (Math.random() * 5000);
                String member = playerList.get(i);
                System.out.println("Player ID:" + member + ", Player Score: " + score);
                //将玩家ID和分数添加到相应键的SortedSet中。
                jedis.zadd(key, score, member);
            }
            //输出打印全部玩家排行榜
            System.out.println();
            System.out.println("       " + key);
            System.out.println(" Ranking list of all players");
            //从对应key的SortedSet中获取已经排好序的玩家列表
            Set<Tuple> scoreList = jedis.zrevrangeWithScores(key, 0, -1);
            for (Tuple item : scoreList) {
                System.out.println(
                        "Player ID:" +
                                item.getElement() +
                                ", Player Score:" +
                                Double.valueOf(item.getScore()).intValue()
                );
            }
            //输出打印前五名玩家的信息
            System.out.println();
            System.out.println("       " + key);
            System.out.println("       Top players");
            scoreList = jedis.zrevrangeWithScores(key, 0, 4);
            for (Tuple item : scoreList) {
                System.out.println(
                        "Player ID:" +
                                item.getElement() +
                                ", Player Score:" +
                                Double.valueOf(item.getScore()).intValue()
                );
            }
            //输出打印特定玩家列表
            System.out.println();
            System.out.println("         " + key);
            System.out.println(" Players with scores from 1,000 to 2,000");
            //从对应key的SortedSet中获取已经积分在1000至2000的玩家列表
            scoreList = jedis.zrangeByScoreWithScores(key, 1000, 2000);
            for (Tuple item : scoreList) {
                System.out.println(
                        "Player ID:" +
                                item.getElement() +
                                ", Player Score:" +
                                Double.valueOf(item.getScore()).intValue()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.quit();
            jedis.close();
        }
    }
}
