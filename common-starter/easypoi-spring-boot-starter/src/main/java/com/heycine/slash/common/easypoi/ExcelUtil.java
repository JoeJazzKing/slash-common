package com.heycine.slash.common.easypoi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author zzj
 */
@Slf4j
public class ExcelUtil {

	/**
	 * 导出 -按照代码设定的样式
	 *
	 * @param list
	 * @param title
	 * @param sheetName
	 * @param pojoClass
	 * @param fileName
	 * @param response
	 */
	public static Workbook exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) {
		// 设定样式style
		ExportParams exportParams = new ExportParams(title, sheetName);
		exportParams.setStyle(ExcelStyleUtil.class);
		exportParams.setType(ExcelType.XSSF);

		return defaultExport(list, pojoClass, fileName, response, exportParams);
	}

	/**
	 * 导出 -默认导出(无样式)
	 *
	 * @param list
	 * @param pojoClass
	 * @param fileName
	 * @param response
	 * @param exportParams
	 */
	private static Workbook defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response,
										  ExportParams exportParams) {
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
		if (workbook != null) {
			if (null == response) {
				return workbook;
			} else {
				downLoadExcel(fileName, response, workbook);
			}
		}

		return workbook;
	}

	/**
	 * 导出 -按照模板
	 *
	 * @param list
	 * @param sheetName
	 * @param fileName
	 * @param response
	 */
	public static void exportExcelOfTemplate(List<?> list, Class<?> clazz, String listName, String sheetName, String templatePath,
											 String fileName, HttpServletResponse response) {
		TemplateExportParams params = new TemplateExportParams(templatePath, false);
		params.setSheetName(new String[]{sheetName});
		Map<String, Object> map = new HashMap<>(4);
		map.put(listName, list);
		Workbook workbook = ExcelExportUtil.exportExcel(params, map);

		if (workbook != null) {
			downLoadExcel(fileName, response, workbook);
		}
	}

	/**
	 * 下载Excel
	 *
	 * @param fileName
	 * @param response
	 * @param workbook
	 */
	private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			OutputStream out = response.getOutputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			response.setHeader("Content-Length", String.valueOf(baos.size()));
			out.write(baos.toByteArray());
		} catch (IOException e) {
			log.error("[monitor][IO][表单功能]异常！", e);
		}
	}

	/**
	 * 导入 -按照文件地址
	 *
	 * @param filePath
	 * @param titleRows
	 * @param headerRows
	 * @param pojoClass
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
		if (StringUtils.isBlank(filePath)) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list;
		try {
			list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
		} catch (NoSuchElementException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	/**
	 * 导入 -按照MultipartFile
	 *
	 * @param file
	 * @param titleRows
	 * @param headerRows
	 * @param pojoClass
	 * @param <T>
	 * @return
	 */

	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows,
										  Class<T> pojoClass) {
		if (file == null) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);

		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
		} catch (NoSuchElementException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[monitor][表单功能]异常！", e);
		}
		return list;
	}

	/**
	 * 导入 -按照InputStream
	 *
	 * @param inputStream
	 * @param titleRows
	 * @param headerRows
	 * @param pojoClass
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows,
										  Class<T> pojoClass) {
		if (inputStream == null) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(inputStream, pojoClass, params);
		} catch (NoSuchElementException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("[monitor][表单功能]异常！", e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
