# Excel读写组件使用说明

Excel文件的读写,只针对没有任何特殊样式,格式简单的数据文件。

Excel文件的读支持2003版以后的各版本(.xls、.xlsx)，写出于兼容性考虑输出的是2003版的格式(.xls)。

## Excel读操作

读取Excel文件,主要有以下四个方法:

    /**
    * 读取excel文件
    *
    * @param file
    * @return
    */
    public static List<ExcelSheet> read(File file);

    /**
    * 读取excel文件
    *
    * @param bytes
    * @return
    */
    public static List<ExcelSheet> read(byte[] bytes);

    /**
    * 读取excel的第一个sheet
    *
    * @param file
    * @return
    */
    public static ExcelSheet readFirstSheet(File file);

    /**
    * 读取excel的第一个sheet
    *
    * @param bytes
    * @return
    */
    public static ExcelSheet readFirstSheet(byte[] bytes);
    
分别传入File对象或byte数组,返回`ExcelSheet`对象,对应Excel中的sheet.

`read`方法读取整个Excel文件,即所有的sheet.如果Excel只有一个sheet,可以使用`readFirstSheet`方法直接读取.

拿到`ExcelSheet`对象后,就可以根据需要获取数据了,参考下面示例代码:

    List<ExcelSheet> excelSheets = ExcelTools.read(file);
    for (ExcelSheet excelSheet : excelSheets){

        System.out.println("sheet名称:"+excelSheet.getSheetName());
        System.out.println("总行数:"+excelSheet.getTotalRowsNum());
        //获取标题
        List<String> rowTitles = excelSheet.getRowTitles();
        //打印标题
        for (String title : rowTitles){
            System.out.println(title);
        }
        //获取行数据
        List<ExcelRow> rows = excelSheet.getRows();
        //打印行数据
        for (ExcelRow excelRow : rows){
            System.out.println("总列数:"+excelRow.getTotalCellsNum());
            //行的列数据
            List<ExcelCell> cells = excelRow.getCells();
            //打印数据
            for (ExcelCell excelCell : cells){
                System.out.println(excelCell.getStringValue());
            }
        }
    }
    
## Excel写操作

写入操作一般只需要用到下面三个方法:

    /**
     * 写入数据到excel文件
     * 如果传入的文件已经存在,则追加sheet,否则生成一个新的excel文件
     *
     * @param file        写入的excel文件
     * @param excelSheets 一个sheet的数据
     */
    public static void write(File file, ExcelSheet... excelSheets);

    /**
     * 写入数据到excel的byte数组
     * 如果传入的bytes数组不是一个空的excel文件,则追加sheet,否则生成一个新的excel文件
     *
     * @param bytes       写入的excel文件byte数组
     * @param excelSheets 一个或多个sheet的数据
     */
    public static byte[] write(byte[] bytes, ExcelSheet... excelSheets) ;

    /**
     * 写入数据到excel的byte数组
     *
     * @param excelSheets 一个或多个sheet的数据
     */
    public static byte[] write(ExcelSheet... excelSheets);
    
只需要在创建`ExcelSheet`对象后调用方法即可.

以下代码在创建完Excel文件后返回一个bytes数组,可以根据实际需求将bytes数组写入磁盘或供网页下载等使用.:

    ExcelSheet excelSheet = new ExcelSheet("sheet1");
    excelSheet.createRowTitles("编号", "姓名", "备注");
    excelSheet.addRow("0001", "liyd1", "136666060518956253");
    excelSheet.addRow("0002", "liyd2", "111");
    excelSheet.addRow("0002", "liyd2", new Date());
    byte[] bytes = ExcelTools.write(excelSheet);
    FileUtils.writeByteArrayToFile(new File("f:/test.xls"), bytes);
    
以下代码在生成Excel文件后直接写入到磁盘,如果文件已经存在则追加sheet,不存在则创建文件:

    ExcelSheet excelSheet = new ExcelSheet("sheet1");
    excelSheet.createRowTitles("编号", "姓名", "备注");
    excelSheet.addRow("0001", "liyd1", "136666060518956253");
    excelSheet.addRow("0002", "liyd2", "111");
    excelSheet.addRow("0002", "liyd2", new Date());
    
    ExcelSheet excelSheet2 = new ExcelSheet("sheet2");
    excelSheet2.createRowTitles("编号", "姓名", "备注");
    excelSheet2.addRow("0001", "liyd1", "136666060518956253");
    excelSheet2.addRow("0002", "liyd2", new String[]{"111","222","333"});
    excelSheet2.addRow("0002", "liyd2", new Date());
    
    ExcelTools.write(new File("f:/test.xls"),excelSheet,excelSheet2);
    
另外,组件支持一些简单的组件样式创建,列如以下代码,将按注释上所写那样会生成下拉框、格式化的日期、图片。

    Date date = DateUtils.parseDate("2016-08-28 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"});
    byte[] imageBytes = FileUtils.readFileToByteArray(new File("f:/test.jpg"));

    ExcelSheet excelSheet = new ExcelSheet("sheet1");
    excelSheet.createRowTitles("编号", "姓名", "备注");
    excelSheet.addRow("0001", "liyd1", "136666060518956253");
    //数组,将生成下拉框
    excelSheet.addRow("0002", "liyd2", new String[]{"111","222","333"});
    //date 将生成格式 2016-08-28 20:36:18
    excelSheet.addRow("0002", "liyd2", new Date());
    //date 时分秒为0 将生成格式 2016-08-28
    excelSheet.addRow("0002", "liyd2", date);
    //图片字节流,将生成图片
    excelSheet.addRow("0002", "liyd2", imageBytes);

    ExcelTools.write(new File("f:/test.xls"),excelSheet);
    
## 自定义样式实现

默认生成的Excel文件只有title加粗，其它无任何样式，如果需要样式可以自行实现`ExcelStyleCreator`接口。

里面定义了sheet、title以及各行各列创建的方法，代码可以参考`DefaultExcelStyleCreator`。

然后调用以下方法，传入自行实现的`ExcelStyleCreator`即可:


    /**
     * 指定样式生成器,写入数据到excel的byte数组
     *
     * @param excelSheets the excel sheet
     */
    public static byte[] write(ExcelStyleCreator excelStyleCreator, ExcelSheet... excelSheets);

    /**
     * 指定样式生成器,写入数据到excel的byte数组
     *
     * @param file              the file
     * @param excelStyleCreator the excel style creator
     * @param excelSheets       the excel sheet
     */
    public static void write(File file, ExcelStyleCreator excelStyleCreator, ExcelSheet... excelSheets);

    /**
     * 指定样式生成器,写入数据到excel的byte数组
     *
     * @param bytes             the file
     * @param excelStyleCreator the excel style creator
     * @param excelSheets       the excel sheet
     */
    public static byte[] write(byte[] bytes, ExcelStyleCreator excelStyleCreator, ExcelSheet... excelSheets);