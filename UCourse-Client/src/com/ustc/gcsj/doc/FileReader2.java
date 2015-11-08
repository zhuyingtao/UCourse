package com.ustc.gcsj.doc;

import android.app.Activity;
import android.os.Bundle;

import com.ustc.gcsj.ucoursenew.R;

//To implement a file reader all by ourself,but it may be a little
//hard to make it better,so this class is not used now.
public class FileReader2 extends Activity {

    // private String docDir = null;
    // private String docName = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_reader);
        //
        // docDir = getIntent().getStringExtra("docDir");
        // docName = getIntent().getStringExtra("docName");
        // System.out.println("docPath : " + docDir + "/" + docName);
        //
        // try {
        // convert2Html(docDir + "/" + docName, docDir + "/" + 1 + ".html");
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // // WebView加载显示本地html文件
        // WebView webView = (WebView) this.findViewById(R.id.doc_view);
        // WebSettings webSettings = webView.getSettings();
        // webSettings.setLoadWithOverviewMode(true);
        // webSettings.setSupportZoom(true);
        // webSettings.setBuiltInZoomControls(true);
        // webView.loadUrl("file:/" + docDir + "/1" + ".html");
    }

    /**
     * word文档转成html格式
     * */
    // public void convert2Html(String inputFile, String outputFile)
    // throws TransformerException, IOException,
    // ParserConfigurationException {
    // HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(
    // inputFile));
    // WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
    // DocumentBuilderFactory.newInstance().newDocumentBuilder()
    // .newDocument());
    //
    // // 设置图片路径
    // wordToHtmlConverter.setPicturesManager(new PicturesManager() {
    // public String savePicture(byte[] content, PictureType pictureType,
    // String suggestedName, float widthInches, float heightInches) {
    // String name = docName.substring(0, docName.indexOf("."));
    // return name + "/" + suggestedName;
    // }
    // });
    //
    // // 保存图片
    // List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
    // if (pics != null) {
    // for (int i = 0; i < pics.size(); i++) {
    // Picture pic = (Picture) pics.get(i);
    // System.out.println(pic.suggestFullFileName());
    // try {
    // String name = docName.substring(0, docName.indexOf("."));
    // pic.writeImageContent(new FileOutputStream(docDir + "/"
    // + name + "/" + pic.suggestFullFileName()));
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // wordToHtmlConverter.processDocument(wordDocument);
    // Document htmlDocument = wordToHtmlConverter.getDocument();
    // ByteArrayOutputStream out = new ByteArrayOutputStream();
    // DOMSource domSource = new DOMSource(htmlDocument);
    // StreamResult streamResult = new StreamResult(out);
    //
    // TransformerFactory tf = TransformerFactory.newInstance();
    // Transformer serializer = tf.newTransformer();
    // serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
    // serializer.setOutputProperty(OutputKeys.INDENT, "yes");
    // serializer.setOutputProperty(OutputKeys.METHOD, "html");
    // serializer.transform(domSource, streamResult);
    // out.close();
    // // 保存html文件
    // writeFile(new String(out.toByteArray()), outputFile);
    // }
    //
    // /**
    // * 将html文件保存到sd卡
    // * */
    // public void writeFile(String content, String path) {
    // FileOutputStream fos = null;
    // BufferedWriter bw = null;
    // try {
    // File file = new File(path);
    // if (!file.exists()) {
    // file.createNewFile();
    // }
    // fos = new FileOutputStream(file);
    // bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
    // bw.write(content);
    // } catch (FileNotFoundException fnfe) {
    // fnfe.printStackTrace();
    // } catch (IOException ioe) {
    // ioe.printStackTrace();
    // } finally {
    // try {
    // if (bw != null)
    // bw.close();
    // if (fos != null)
    // fos.close();
    // } catch (IOException ie) {
    // }
    // }
    // }
}