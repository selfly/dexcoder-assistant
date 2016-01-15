//package com.dexcoder.commons.utils;
//
//import java.io.FileOutputStream;
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.Map;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//import com.dexcoder.commons.exceptions.CommonsAssistantException;
//
///**
// * Created by liyd on 11/5/14.
// */
//public class SiteMapUtils {
//
//    /**
//     * 获取xml document对象
//     *
//     * @return
//     */
//    public static Document getDocument() {
//
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            return builder.newDocument();
//        } catch (ParserConfigurationException e) {
//            throw new CommonsAssistantException("创建document对象失败", e);
//        }
//
//    }
//
//    /**
//     * 创建sitemap到指定路径下
//     * 
//     * @param urlList
//     * @param targetPath
//     */
//    public static void createSiteMapXml(List<Map<String, String>> urlList, String targetPath) {
//
//        if (CollectionUtils.isEmpty(urlList)) {
//            return;
//        }
//
//        Document document = getDocument();
//        Element urlset = document.createElement("urlset");
//        urlset.setAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
//        document.appendChild(urlset);
//
//        for (Map<String, String> map : urlList) {
//
//            Element url = document.createElement("url");
//            urlset.appendChild(url);
//
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//
//                Element element = document.createElement(entry.getKey());
//                element.setTextContent(entry.getValue());
//                url.appendChild(element);
//            }
//        }
//
//        outputXml(document, targetPath);
//    }
//
//    /**
//     * 将XML文件输出到指定的路径
//     *
//     * @param doc
//     * @param fileName
//     * @throws Exception
//     */
//    public static void outputXml(Document doc, String fileName) {
//        try {
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer transformer = tf.newTransformer();
//            DOMSource source = new DOMSource(doc);
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            //设置文档的换行与缩进
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
//            StreamResult result = new StreamResult(pw);
//            transformer.transform(source, result);
//        } catch (Exception e) {
//            throw new CommonsAssistantException("写入xml文件失败", e);
//        }
//    }
//}
