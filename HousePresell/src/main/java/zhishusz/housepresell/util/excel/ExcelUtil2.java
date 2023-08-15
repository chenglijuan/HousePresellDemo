package zhishusz.housepresell.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;

public class ExcelUtil2
{
	private static ExcelUtil2 instance;

	public static final String FILE_EXTENSION_XLS = "xls";
	public static final String FILE_EXTENSION_XLSX = "xlsx";
	public static final String FILE_EXTENSION_CSV = "csv";
	
	public ExcelUtil2()
	{
	}
	public static ExcelUtil2 getInstance()
	{
		if(instance == null) instance = new ExcelUtil2();
		
		return instance;
	}

	@SuppressWarnings("rawtypes")
	public static List read(String filePath)
	{
		if (filePath != null)
		{
			if (filePath.endsWith(FILE_EXTENSION_XLS))
			{
				return readXls(filePath);
			}
			else if (filePath.endsWith(FILE_EXTENSION_XLSX))
			{
				return readXlsx(filePath);
			}
			else if (filePath.endsWith(FILE_EXTENSION_CSV))
			{
				return readCsv(filePath);
			}
			else
			{
				return new ArrayList();
			}
		}
		else
		{
			return new ArrayList();
		}
	}

	/**
	 * 
	 * @param List
	 *            headerList 属性表，"age:年龄"
	 * @param List
	 *            <T> list 需要导出的数据列表对象
	 * @param File
	 *            file 指定输出文件位置，只能导出excel2003以上版本
	 * 
	 * @return true 导出成功 false 导出失败
	 */
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public static <T> boolean excelExport(List<String> headerList, List<T> list, File file)
	{
		try
		{
			Workbook wb = null;
			String filename = file.getName();
			String type = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			if (type.equals(FILE_EXTENSION_XLS))
			{
				wb = new HSSFWorkbook();
			}
			if (type.equals(FILE_EXTENSION_XLSX))
			{
				wb = new XSSFWorkbook();
			}
			CreationHelper createHelper = wb.getCreationHelper();
			Sheet sheet = wb.createSheet("sheet1");
			Row row = sheet.createRow(0);
			int i = 0;
			// 定义表头
			for (int k=0;k<headerList.size();k++)
			{
				Cell cell = row.createCell(i++);
				String[] headArr = headerList.get(k).split("#");
				cell.setCellValue(createHelper.createRichTextString(headArr[1]));
			}
			// 填充表单内容
			System.out.println("--------------------100%");
			float avg = list.size() / 20f;
			int count = 1;
			for (int j = 0; j < list.size(); j++)
			{
				T p = list.get(j);
				Class classType = p.getClass();
				int index = 0;
				Row row1 = sheet.createRow(j + 1);
				for (int k=0;k<headerList.size();k++)
				{
					String[] headArr = headerList.get(k).split("#");
					String firstLetter = headArr[0].substring(0, 1).toUpperCase();
					// 获得和属性对应的getXXX()方法的名字
					String getMethodName = "get" + firstLetter + headArr[0].substring(1);
					// 获得和属性对应的getXXX()方法
					Method getMethod = classType.getMethod(getMethodName, new Class[] {});
					// 调用原对象的getXXX()方法
					Object value = getMethod.invoke(p, new Object[] {});
					if(value == null)
					{
						value = "";
					}
					Cell cell = row1.createCell(index++);
					cell.setCellValue(value.toString());
				}
				if (j > avg * count)
				{
					count++;
					System.out.print("I");
				}
				if (count == 20)
				{
					System.out.print("I100%");
					count++;
				}
			}
			
			//创建表格之后设置行高与列宽
			for(int k = 0; k < headerList.size(); k++) {
			    sheet.setColumnWidth(k, MSExcelUtil.pixel2WidthUnits(155)); //设置列宽
			}
			
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings({
			"rawtypes", "unchecked", "unused"
	})
	public static List readXls(String filePath)
	{
		ArrayList resutList = new ArrayList(); // 用来存储某个表 读取出来的数据
		try
		{
			InputStream is = new FileInputStream(filePath);
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0); // 某个表
			// String sheetName = sheet.getSheetName();//表名
			int count = 0;
			for (int rows = 0; rows <= sheet.getLastRowNum(); rows++)
			{
				// 有多少行
				HSSFRow row = sheet.getRow(rows);// 取得某一行 对象
				List rowList = new ArrayList();

				if(null == row ||  row.getLastCellNum() < 0){
					continue;
				}
				
				for (int columns = 0; columns < row.getLastCellNum(); columns++)
				{// 读取所有列
					// s[columns] = row.getCell(columns).getStringCellValue();
					// //取得某单元格内容，字符串类型
					HSSFCell cell = row.getCell(columns);
					if(cell != null)
					{
						switch (cell.getCellType())
						{
						case Cell.CELL_TYPE_BOOLEAN:
							// 得到Boolean对象的方法
							rowList.add(cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							// 先看是否是日期格式
							if (DateUtil.isCellDateFormatted(cell))
							{
//								Date date = cell.getDateCellValue();
//								// 读取日期格式
//								rowList.add(DateFormatUtils.format(date, "yyyy-MM-dd"));
								// 读取日期格式
								rowList.add(cell.getDateCellValue());
							}
							else
							{
								DecimalFormat df = new DecimalFormat();
								// 单元格的值,替换掉,
								String value = df.format(cell.getNumericCellValue()).replace(",", "");
								// 读取数字
								rowList.add(value);
							}
							break;
						case Cell.CELL_TYPE_FORMULA:
							// 读取公式
							rowList.add(cell.getCellFormula());
							break;
						case Cell.CELL_TYPE_STRING:
							// 读取String
							rowList.add(cell.getRichStringCellValue().toString());
							break;
						default:
							rowList.add(cell.getStringCellValue());
							break;
						}
					}
					else
					{
						rowList.add("");
					}
				}
				count++;
				// System.out.println("第"+count+"行："+rowList);
				resutList.add(rowList);
			}
			is.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return resutList;
	}

	@SuppressWarnings({
			"rawtypes", "unchecked", "unused"
	})
	public static List readXlsx(String filePath)
	{
		ArrayList resutList = new ArrayList(); // 用来存储某个表 读取出来的数据
		try
		{
			InputStream is = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFSheet sheet = wb.getSheetAt(0); // 某个表
			// String sheetName = sheet.getSheetName();//表名
			int count = 0;
			for (int rows = 0; rows <= sheet.getLastRowNum(); rows++)
			{
				// 有多少行
				XSSFRow row = sheet.getRow(rows);// 取得某一行 对象
				List rowList = new ArrayList();

				if(null == row || row.getLastCellNum() == 0){
					continue;
				}else{
					for (int columns = 0; columns < row.getLastCellNum(); columns++)
					{// 读取所有列
						// s[columns] = row.getCell(columns).getStringCellValue();
						// //取得某单元格内容，字符串类型
						XSSFCell cell = row.getCell(columns);
						if(cell != null)
						{
							switch (cell.getCellType())
							{
							case Cell.CELL_TYPE_BOOLEAN:
								// 得到Boolean对象的方法
								rowList.add(cell.getBooleanCellValue());
								break;
							case Cell.CELL_TYPE_NUMERIC:
								// 先看是否是日期格式
								if (DateUtil.isCellDateFormatted(cell))
								{
//									Date date = cell.getDateCellValue();
//									// 读取日期格式
//									rowList.add(DateFormatUtils.format(date, "yyyy-MM-dd"));
									// 读取日期格式
									rowList.add(cell.getDateCellValue());
								}
								else
								{
									DecimalFormat df = new DecimalFormat();
									// 单元格的值,替换掉,
									String value = df.format(cell.getNumericCellValue()).replace(",", "");
									// 读取数字
									rowList.add(value);
								}
								break;
							case Cell.CELL_TYPE_FORMULA:
								// 读取公式
								rowList.add(cell.getCellFormula());
								break;
							case Cell.CELL_TYPE_STRING:
								// 读取String
								rowList.add(cell.getRichStringCellValue().toString());
								break;
							default:
								rowList.add(cell.getStringCellValue());
								break;
							}
						}
						else
						{
							rowList.add("");
						}
					}
				}
				
				count++;
				//System.out.println("第" + count + "行：" + rowList);
				resutList.add(rowList);
			}
			is.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return resutList;
	}
	
	@SuppressWarnings({
		"rawtypes", "unchecked"
})
public static List readCsv(String filePath)
{
	ArrayList resutList = new ArrayList(); // 用来存储某个表 读取出来的数据
	
	File file = new File(filePath);
	
	 /*CsvReader reader = new CsvReader();*/
	/*CsvData data = reader.read(file);*/
	CsvReader reader = new CsvReader();
	CsvData data = reader.read(file, Charset.forName("GBK"));
	
	for (int rows = 0; rows < data.getRowCount(); rows++)
	{
		CsvRow row = data.getRow(rows);
		
		List<String> rawList = row.getRawList();
		
		if(null != rawList && rawList.size() > 0){
			resutList.add(rawList);
		}
	}
		
	return resutList;
}
	
	public static <T> Properties excelExport(List<String> sheetNames, List<String> headerList, List<List<T>> lists, String fileNameHead)
	{
		Properties properties = new MyProperties();
		try
		{
			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("exportExcel");//文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();//项目路径
			
			String saveDirectory = localPath + relativeDir;//文件在服务器文件系统中的完整路径
			
			directoryUtil.mkdir(saveDirectory);

			String strNewFileName = fileNameHead + "-"
				+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(),"yyyyMMddHHmmss")
				+ ".xlsx";
			
			Workbook wb = null;
			String filename = saveDirectory + strNewFileName;
			String type = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			System.out.println("filename:" + filename);
			if (type.equals(FILE_EXTENSION_XLS))
			{
				wb = new HSSFWorkbook();
			}
			if (type.equals(FILE_EXTENSION_XLSX))
			{
				wb = new XSSFWorkbook();
			}
			CreationHelper createHelper = wb.getCreationHelper();
			for (int m = 0; m < sheetNames.size(); m++) {
				Sheet sheet = wb.createSheet(sheetNames.get(m));
				List<T> list = lists.get(m);
				Row row = sheet.createRow(0);
				int i = 0;
				// 定义表头
				for (int k=0;k<headerList.size();k++)
				{
					Cell cell = row.createCell(i++);
					String[] headArr = headerList.get(k).split("#");
					cell.setCellValue(createHelper.createRichTextString(headArr[1]));
				}
				// 填充表单内容
				System.out.println("--------------------100%");
				float avg = list.size() / 20f;
				int count = 1;
				for (int j = 0; j < list.size(); j++)
				{
					System.out.println("size:" + list.size());
					T p = list.get(j);
					Class classType = p.getClass();
					int index = 0;
					Row row1 = sheet.createRow(j + 1);
					for (int k=0;k<headerList.size();k++)
					{
						String[] headArr = headerList.get(k).split("#");
						String firstLetter = headArr[0].substring(0, 1).toUpperCase();
						// 获得和属性对应的getXXX()方法的名字
						String getMethodName = "get" + firstLetter + headArr[0].substring(1);
						// 获得和属性对应的getXXX()方法
						Method getMethod = classType.getMethod(getMethodName, new Class[] {});
						// 调用原对象的getXXX()方法
						Object value = getMethod.invoke(p, new Object[] {});
						if(value == null)
						{
							value = "";
						}
						Cell cell = row1.createCell(index++);
						cell.setCellValue(value.toString());
					}
					if (j > avg * count)
					{
						count++;
						System.out.print("I");
					}
					if (count == 20)
					{
						System.out.print("I100%");
						count++;
					}
				}
				
				//创建表格之后设置行高与列宽
				for(int k = 0; k < headerList.size(); k++) {
				    sheet.setColumnWidth(k, MSExcelUtil.pixel2WidthUnits(155)); //设置列宽
				}
			}
			
			FileOutputStream fileOut = new FileOutputStream(new File(filename));
			wb.write(fileOut);
			fileOut.close();
			properties.put(S_NormalFlag.result, S_NormalFlag.success);
			properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
			
			properties.put("fileName", strNewFileName);
			properties.put("fileRelativePath", relativeDir + strNewFileName);
			properties.put("fileLocalFullPath", saveDirectory + strNewFileName);
			
			return properties;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		properties.put(S_NormalFlag.result, S_NormalFlag.fail);
		properties.put(S_NormalFlag.info, "文件导出异常");
		return properties;
	}
}
