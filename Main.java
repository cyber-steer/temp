package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	static List<String> dates;
	static int datesHeader = 0;
	static String original;
	static String copy;
	static boolean scriptWorking = false;
	static String fileName;
	static String function;
	static int count = 0;

	public static void insertStringInFile(File inFile, int lineno, String lineToBeInserted) throws Exception {
		// 파일이 없으면 새로 만들기
		if(!inFile.exists()) {
			inFile.createNewFile();

			String message = fileName + " create";
			script(message, true);
			FileWriter writer = new FileWriter(inFile);
			writer.write(lineToBeInserted + "\n");
			writer.close();
		}
		else {
			// 원본 파일을 임시 파일로 복사하면서 특정 줄에 문자열 삽입
			File outFile = new File("outfile.txt");

			// 입력 스트림, 출력 스트림 생성
			FileInputStream fis = new FileInputStream(inFile);
		}
	}
}
