package com.heycine.slash.common.socket.test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.heycine.slash.common.basic.http.R;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import io.swagger.annotations.Api;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理后台-新项目设置
 *
 * @author zhiji.zhou
 * @version V1.0
 * @date 2021-04-30 14:20
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/excel")
@Api(tags = "Excel拆分处理", protocols = "http,https")
public class ExcelController {

	/**
	 * 识别目标 -导入新增
	 *
	 * @return
	 */
	@PostMapping("/identify/exportAdd")
	public R<?> exportAdd(@RequestParam("file") MultipartFile file,
						  HttpServletResponse response,
						  HttpServletRequest request) throws IOException, InvalidFormatException {
		Set<String> areaSet = new HashSet<>();
		File fileBak = MultipartFileToFile(file);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileBak);

		// 获取大区字段，进行分组
		XSSFSheet sheet1 = xssfWorkbook.getSheetAt(0);
		for (int i = 0 + 3; i < sheet1.getLastRowNum(); i++) {
			 XSSFRow row = sheet1.getRow(i);

			 if (null != row) {
				 String areaStr = row.getCell(1).getStringCellValue();
				 areaSet.add(areaStr);
			 }

		}

		List<Object[]> copyWorkbookList = new ArrayList<>();
		areaSet = areaSet.stream().filter(item -> item.equals("东北")).collect(Collectors.toSet());
		for (String area : areaSet) {
			File fileTarget = File.createTempFile(area + "_", ".xlsx", new File("tmp/excel_test"));
			copyFileUsingChannel(fileBak, fileTarget);
			XSSFWorkbook copyWorkbook = new XSSFWorkbook(fileTarget);
			copyWorkbookList.add(new Object[]{area, copyWorkbook});
		}
		System.out.println(copyWorkbookList);

		// 处理筛选sheet数据
		int startCopyIndex = 3;
		for (Object[] objects : copyWorkbookList) {
			String area = ((String) objects[0]);
			XSSFWorkbook workbook = ((XSSFWorkbook) objects[1]);

			List<Row> rowList = new ArrayList<>();
			XSSFSheet sheetAt0 = workbook.getSheetAt(0);
			for (int i = 3; i <= sheetAt0.getLastRowNum(); i++) {
				XSSFRow row = sheetAt0.getRow(i);

				// 测试只删一行
				/*if (i == 8) {
					continue;
				}*/
				/*sheetAt0.removeRow(row);
				if (i == 42) {
					break;
				}*/

				/*if (row != null) {
					if (!StrUtil.equals(area, row.getCell(1).getStringCellValue())) {
						sheetAt0.removeRow(row);
					} else {
						rowList.add(row);
					}
				}*/
			}
			sheetAt0.removeRow(sheetAt0.getRow(42));
//			sheetAt0.removeRow(sheetAt0.getRow(9));

			long startTime = System.currentTimeMillis();
			for (Row row : rowList) {
				if (startCopyIndex != row.getRowNum()) {
					sheetAt0.shiftRows(row.getRowNum(), row.getRowNum(), -(row.getRowNum() - startCopyIndex));
				}
				startCopyIndex++;
			}
			System.out.println( "移动" +rowList.size()+"行共耗时：" +( System.currentTimeMillis() - startTime) + "ms");
		}

		// 将处理好的sheet保存为文件
		for (Object[] objects : copyWorkbookList) {
			String area = ((String) objects[0]);
			XSSFWorkbook workbook = ((XSSFWorkbook) objects[1]);
			File fileTarget = File.createTempFile(area + "_", ".xlsx", new File("tmp/excel_test/finally"));
			workbook.write(new FileOutputStream(fileTarget));
		}

		// 将文件打成压缩包

		return R.ok(areaSet);
	}

	private static ByteArrayOutputStream cloneInputStream(InputStream input) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = input.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return baos;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void copyFileUsingChannel(File source, File dest) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		} finally {
			sourceChannel.close();
			destChannel.close();
		}
	}

	/**
	 * 将MultipartFile转换为File
	 *
	 * @param multiFile
	 * @return
	 */
	public static File MultipartFileToFile(MultipartFile multiFile) {
		// 获取文件名
		String fileName = multiFile.getOriginalFilename();
		// 获取文件后缀
		String prefix = fileName.substring(fileName.lastIndexOf("."));
		// 若须要防止生成的临时文件重复,能够在文件名后添加随机码

		try {
			File file = File.createTempFile(fileName, prefix);
			multiFile.transferTo(file);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 判断行是否为空
	public static boolean isRowEmpty(Row row) {
		if (null == row) {
			return true;
		} else {
			return false;
		}
	}

	public Sheet getAccuracyContextNum(Sheet sheet) {
		// 删除空行
		for (int i = 3; i <= sheet.getLastRowNum(); i++) {
			// 删除空行
			if (this.isRowEmpty(sheet.getRow(i))) {
				int lastRowNum = sheet.getLastRowNum();
				if (i >= 3 && i < lastRowNum) {
					sheet.shiftRows(i + 1, lastRowNum, -1);
				}
				i--;
			}
		}
		int blankIndex = 0;
		int blankStartIndex = 0;
		int blankEndIndex = 0;
		for (int i = 3; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			// 删除空行
			if (this.isRowEmpty(row)) {
				blankIndex++;
			} else {

			}
		}
		return sheet;
	}
}
