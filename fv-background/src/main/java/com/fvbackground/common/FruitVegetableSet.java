package com.fvbackground.common;

import java.util.Arrays;
import java.util.HashSet;

public class FruitVegetableSet {

    public boolean IsFruit(String name) {
        //定义水果集合
        HashSet<String> fruit = new HashSet<>();
        //存放水果数组
        String [] fruitArray = {"苹果","梨","葡萄","红提","枣","柑橘","柚","桃","西瓜","杏","甜瓜","香瓜","荔枝","甘蔗",
                                "柿","柠檬","香蕉","芒果","菠萝","哈密瓜","李","石榴","枸杞","山楂","椰子","桑葚",
                                "荸荠","柚子","草莓","沙糖桔","木瓜","橙","橙子","圣女果","龙眼","枇杷","山竹","红毛丹","无花果",
                                "布朗","杨桃","人参果","猕猴桃","芭乐","杨梅","乌梅","蓝莓","西梅","释迦","百花果","黄皮","樱桃",
                                "雪莲果","榴莲","火龙果","番石榴","菠萝蜜","百香果","罗汉果","莲雾"};
        fruit.addAll(Arrays.asList(fruitArray));
        return fruit.contains(name);
    }

    public boolean IsNut(String name) {
        //存放坚果集合
        HashSet<String> nuts = new HashSet<>();
        String [] nutArray = {"瓜子","榛子仁","核桃仁","花生","杏仁","葵花籽","松籽","开心果","核桃","白果","蚕豆","板栗","榛子",
                              "西瓜籽","腰果","白瓜子","榧子","栗子","莲子","巴旦杏","芝麻","胡桃","喀什巴丹木","扁桃"};
        nuts.addAll(Arrays.asList(nutArray));
        return nuts.contains(name);
    }


    public boolean IsVegetable(String name) {
        //存放蔬菜集合
        HashSet<String> vegetable = new HashSet<>();
        String [] vegetableArray = {"大葱","芹菜","蒜苗","小葱","菠菜","上海青","韭菜","油麦菜","生姜","香菜","红薯","生姜","鸭蛋","大白菜","西芹","南瓜","冬瓜",
                                   "瓠子","紫包菜","小夹豆","玉米粒","海带结","海带","海带丝","水笋","笋","黄菜","去皮山药","山药","玉米","黄瓜","辣椒","豆条","千张","花菜","豆腐"
                                    ,"豆干","红萝卜","萝卜","青萝卜","胡萝卜","水萝卜","西红柿","番茄","苋菜","莲藕","菇","蟹味菇","平菇","金针菇",
                                    "木耳","蒜苔","马铃薯","土豆","丝瓜","西蓝花","羽衣甘蓝"};
        vegetable.addAll(Arrays.asList(vegetableArray));
        return vegetable.contains(name);
    }

    public String getType(String name) {
        if (IsFruit(name)) {
            return "水果";
        } else if (IsNut(name)){
            return "坚果";
        } else if (IsVegetable(name)) {
            return "蔬菜";
        } else {
            return "非果蔬";
        }
    }

}
