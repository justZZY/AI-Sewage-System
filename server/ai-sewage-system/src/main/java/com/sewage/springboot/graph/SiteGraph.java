package com.sewage.springboot.graph;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sewage.springboot.Global;
import com.sewage.springboot.util.FboxTools;
import okhttp3.*;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.VCARD;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @desc 在服务器启动时后台构造一个知识图谱
 */
public class SiteGraph {
    public SiteGraph() throws IOException {
        tdbPersistence = new TDBPersistence("IOT_Knowledge_Graph");
        Model model = createGraphModel();
        // 配置规则
        Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL("rules\\equipException.rules"));
        this.infModel = ModelFactory.createInfModel(reasoner, model);
        // 生成rdf文件到本地
        OutputStream out = new FileOutputStream("test.rdf");
        RDFDataMgr.write(out, this.infModel, Lang.RDFXML);
        // 把图谱持久化到tdb中
        tdbPersistence.loadModel("sewage_iot", this.infModel, true);
    }
    // URI = [ApplicationDomainName]/[BaseOntology]/[DomainOntology]/[DeviceID]
    final String BASE_URI = "https://www.zm-iot-platform.com/";
    private InfModel infModel;
    private TDBPersistence tdbPersistence;
    private FboxTools fboxTools = new FboxTools();


    /**
     * @desc 图谱模型构造
     */
    private Model createGraphModel () throws IOException {
        JSONArray siteArray = fboxTools.getSiteData();
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("iot", BASE_URI);
        Resource iot = model.createResource(BASE_URI + "IOT/");
        Property hasSite = model.createProperty(BASE_URI, "hasSite");
        Property hasEquip = model.createProperty(BASE_URI, "hasEquip");
        Property hasValue = model.createProperty(BASE_URI, "hasValue");
        Property hasException = model.createProperty(BASE_URI, "hasException");
        Resource exception = model.createResource(BASE_URI + "Exception");
        List<Resource> siteList = new ArrayList<>();
        // 解析站点的唯一URI
        // BASE_URI/site/name/id
        siteArray.forEach(item -> {
            JSONObject obj = JSONObject.parseObject(item.toString());
            JSONArray subSiteArray = obj.getJSONArray("boxRegs");
            subSiteArray.forEach(subItem -> {
                JSONObject subObj = JSONObject.parseObject(subItem.toString());
                Resource site = model.createResource(BASE_URI + "site/" +
                        subObj.getString("alias").replace(" ", "") + "/" + subObj.getJSONObject("box").getString("boxNo") + "/");
                siteList.add(site);
            });
        });
        siteList.forEach(item -> {
            iot.addProperty(hasSite, item);
        });
        // iot-hasSite-site 表示第一层关系
        // site-hasEquip-Equip 表示第二层站点拥有的设备关系
        siteList.forEach(siteResource -> {
            String[] siteStr = siteResource.getURI().split("/");
            String siteNo = siteStr[siteStr.length - 1]; // 末尾的是boxNo用于检索下属设备
            String siteName = siteStr[siteStr.length - 2]; // 用于校验测试站点数据
            try {
                JSONArray equipArray = fboxTools.getEquipData(siteNo);
                equipArray.forEach(group -> {
                    // 在这里解析设备json数据
                    // BASE_URI/equip/name/id
                    JSONObject.parseObject(group.toString()).getJSONArray("items").forEach(item -> {
                        JSONObject equipItem = JSONObject.parseObject(item.toString());
                        Resource equip = model.createResource(BASE_URI + "equip/" +
                                equipItem.getString("name") + "/" + equipItem.getString("id"));
                        siteResource.addProperty(hasEquip, equip);
                        // 如果是测试设备,获取测试设备的数值加入hasValue到图谱
                        // BASE_URI/value/name/number/time
                        // 未开学没有设备暂时模拟数值
                        if (Pattern.matches(".*测试平台.*", siteName)
                                && equipItem.getString("grpName").equals("传感")) {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss"); // 设置日期格式
                            int number = new Random().nextInt(60); // 写个随机
                            Resource value = model.createResource(BASE_URI + "value/"
                                    + equipItem.getString("name") + "/" + number + "/" + df.format(new Date()));
                            equip.addProperty(hasValue, value);
                            // 根据一级A标水质标准判定该项数据是否满足
                            // PH值处于6~9之间
                            if (equipItem.getString("name").equals("传感_pH值_pH") && (number < 6 || number > 9)) {
                                value.addProperty(hasException, exception);
                            }
                            // 溶解氧值<50
                            if (equipItem.getString("name").equals("传感_溶解氧值_mg·L") && number > 50) {
                                value.addProperty(hasException, exception);
                            }
                            // 悬浮物<10
                            if (equipItem.getString("name").equals("传感_浊度值_NTU") && number > 10) {
                                value.addProperty(hasException, exception);
                            }
                        }
                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // 此时我们已经建立了iot-site site-equip之间的关系
        return model;
    }
}
