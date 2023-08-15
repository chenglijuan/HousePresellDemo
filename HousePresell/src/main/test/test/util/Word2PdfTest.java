package test.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import test.api.BaseJunitTest;

/*
 * 公共类测试
 * Company：ZhiShuSZ
 */
public class Word2PdfTest extends BaseJunitTest
{

	/*@Test
	 public static void vode() throws  Exception
	{
		// 转换pdf文件
		File docxFile = new File("d://输出.docx");
		File pdfFile = new File("d://123.pdf");

		if (docxFile.exists())
		{
			if (!pdfFile.exists())
			{
				InputStream inStream = new FileInputStream(docxFile);
				
				XWPFDocument document = new XWPFDocument(inStream);
				
				OutputStream out = new FileOutputStream(pdfFile);

				PdfOptions options = PdfOptions.create();
				ExtITextFontRegistry fontProvider = ExtITextFontRegistry.getRegistry();
				options.fontProvider(fontProvider);
				PdfConverter.getInstance().convert(document, out, options);
			}
		}
	}*/

	/*@Test
	public void excute() throws Exception
	{
		String url = "d://输出.docx";
		File file = new File("d://123.pdf");
		File docxFile = new File(url);
		OutputStream outputStream = new FileOutputStream(file);
		
		InputStream inputStream = new FileInputStream(docxFile);

		if (!file.exists())
		{
			file.createNewFile();
		}
		
		//FileInputStream fileInputStream = null;
		if (url.endsWith(".docx"))
		{
			DocxToPDFConverter converter = new DocxToPDFConverter(inputStream, outputStream, true, true);
			converter.convert();
			//fileInputStream = new FileInputStream(file);
		}
		else if (url.endsWith(".doc"))
		{
			DocToPDFConverter converter = new DocToPDFConverter(inputStream, outputStream, true, true);
			converter.convert();
			//fileInputStream = new FileInputStream(file);
		}
		
	}*/
	
	 public static void main(String[] args) throws IOException {
	        String docPath = "d://输出.docx";
	        String pdfPath = "d://123.pdf";
	        XWPFDocument document;
	        try (InputStream doc = Files.newInputStream(Paths.get(docPath))) {
	            document = new XWPFDocument(doc);
	        }
	        PdfOptions options = PdfOptions.create();
	        try (OutputStream out = Files.newOutputStream(Paths.get(pdfPath))) {
	            PdfConverter.getInstance().convert(document, out, options);
	        }

	    }

	@Override
	public void execute() throws Exception
	{
		// TODO Auto-generated method stub
		
	}
}
