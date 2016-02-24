package com.generate.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * 生成数据库
 */
public class DBGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.im.sdk.dao");

        addWord(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }



    private static void addWord(Schema schema) {
        Entity word = schema.addEntity("Word");
        word.addLongProperty("id").columnName("_id").primaryKey().autoincrement();
        word.addStringProperty("text");
        word.addLongProperty("time");
        //3.1添加
        word.addStringProperty("type");
        word.addBooleanProperty("show_ac");
        word.addBooleanProperty("show_dc");
        word.addBooleanProperty("show_industry_socket");
        word.addBooleanProperty("show_free_status");
        word.addBooleanProperty("show_support_car");
        word.addBooleanProperty("show_complain");

        //3.3新增
        word.addStringProperty("codeBitList");
    }

}
