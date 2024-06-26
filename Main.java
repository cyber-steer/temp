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
			FileOutputStream fos = new FileOutputStream(outFile);

			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			PrintWriter pw = new PrintWriter(fos);

			String line;
			int lineCount = 1;
			
			while ((line = br.readLine()) != null) {
				// 특정 줄에 도달하면, 문자열 삽입
				if(lineCount == lineno) {
					pw.println(lineToBeInserted);
				}

				pw.println(line);
				lineCount++;
			}

			// 만약 마지막 줄이 삽입할 줄이라면, 여기서 문자열 삽입
			if(lineCount == lineno) {
				pw.println(lineToBeInserted);
			}

			pw.flush();
			pw.close();
			br.close();

			// 원본 파일 삭제
			if(!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// 임시 파일을 원본 파일로 변경
			if(!outFile.renameTo(inFile))
				System.out.println("Could not rename file");
		}
	}

	public static int countTabs(String line) {
		int count = 0;
		for (char c : line.toCharArray()) {
			if(c == '\t') {
				count++;
			}
		}
		return count;
	}

	public static String addTabs(String line, int numTabs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numTabs; i++) {
			sb.append('\t');
		}
		sb.append(line);
		return sb.toString();
	}

	public static void main(String args[]) throws Exception {
		original = args[0];
		copy = args[1];
		String timeSet = args[2];
//		original = "SqliteDao.java";
//		copy = "copy.txt";
//		String timeSet = "2023/3/8";
		
		getDateSet(timeSet);
		int lineNumber = 1;

		//     insertStringInFile(new File(copy), lineNumber, "hello");

		Path sourcePath = Paths.get(original); // 기존의 자바 파일
		String targetDirectory = "."; // 현재 디렉토리
		int fileCount = 1;

		BufferedReader br = new BufferedReader(new FileReader(sourcePath.toFile()));
		String line;
		boolean ignore = false;
		int logCount = 1;
		fileName = original.replace(".java", "");
		String[] filePaths = fileName.split("/");
		fileName = filePaths[filePaths.length-1];
		String message = fileName;
		while ((line = br.readLine()) != null) {
			count ++;
			System.out.println((logCount++) + ": " + line);

			if(line.contains("public class")) {
				scriptWorking = true;
				script(fileName + " import add");
			}
			try {
				Path targetPath = null;
				if(line.trim().startsWith("//")) {
					targetPath = Paths.get(targetDirectory, String.valueOf(copy));
					insertStringInFile(new File(copy), lineNumber++, line);
				}
				else if(line.trim().endsWith("{")) {
					if(line.contains("try")) {
						targetPath = Paths.get(targetDirectory, String.valueOf(copy));
						insertStringInFile(new File(copy), lineNumber++, line);
						int numTabs = countTabs(line);
						String catchStart = addTabs("} catch (Exception e) {", numTabs);
						String catchStr = addTabs("e.printStackTrace();", numTabs + 1);
						String catchEnd = addTabs("}", numTabs);
						insertStringInFile(new File(copy), lineNumber, catchStart);
						insertStringInFile(new File(copy), lineNumber + 1, catchStr);
						insertStringInFile(new File(copy), lineNumber + 2, catchEnd);
					} catch (Exception e) {
					else if(line.contains("Thread(()->{")) {
						targetPath = Paths.get(targetDirectory, String.valueOf(copy));
						insertStringInFile(new File(copy), lineNumber++, line);
						int numTabs = countTabs(line);
						String end = addTabs("});", numTabs);
						insertStringInFile(new File(copy), lineNumber, end);
						
						String threadName = line.split(" ")[0].equals("Thread") ? line.split(" ")[1]:line.trim().split(" ")[0];
						message += " "+ threadName;
						script(message);

					});
					else if(line.contains("= {") || line.contains("={")) {
						targetPath = Paths.get(targetDirectory, String.valueOf(copy));
						insertStringInFile(new File(copy), lineNumber++, line);
						int numTabs = countTabs(line);
						String end = addTabs("};", numTabs);
						insertStringInFile(new File(copy), lineNumber, end);
						
						script(message+" update");

					};
					else {
					}
						e.printStackTrace();
					}
						if(!ignore) {
							targetPath = Paths.get(targetDirectory, String.valueOf(copy));
							insertStringInFile(new File(copy), lineNumber++, line);

							int numTabs = countTabs(line);
							String end = addTabs("}", numTabs);
							insertStringInFile(new File(copy), lineNumber, end);
							String[] str = {"while", "if", "for", "else if", "else", "switch", "case"};
							boolean fun = true;
							for(String type : str) {
								if(line.contains(type)) {
									if(type.equals("case")) {
										int start = line.indexOf("case ") + "case ".length();
										int endIdx = line.indexOf(":");
										String temp = line.substring(start, endIdx);
										script(message + " " +temp);
										message += " case";
									}
									else {
										message += " " +type;
										script(message);
									}
									fun = false;
									break;
								}
							}
							if(fun && !line.contains("public class")) {

						        String regex = "\\s(\\w+)\\(";
						        Pattern pattern = Pattern.compile(regex);
						        Matcher matcher = pattern.matcher(line);
						        
						        String funName = "none";
						        if(matcher.find()) {
						        	funName = matcher.group(1);
						}
								
								if(funName.equals(fileName)) {
									message += " structor";
								}
								else {
									System.out.println("funName : "+funName);
									message += " "+funName;
								}
								script(message);
								
							}
						}
				}
			} catch (Exception e) {
				else if(line.trim().startsWith("}")) {
					if(line.trim().contains("}")) {
						if(ignore) {
							lineNumber += 3;
							ignore = false;
							
						}
						else {
							lineNumber++;
							int idx = message.lastIndexOf(" ");
							if(idx != -1) {
								message = message.substring(0, idx);
							}
						}
					}
					else {
						lineNumber++;
						int idx = message.lastIndexOf(" ");
						if(idx != -1) {
							message = message.substring(0, idx);
						}
					}
				}
				else {
					if(!ignore) {
						targetPath = Paths.get(targetDirectory, String.valueOf(copy));
						insertStringInFile(new File(copy), lineNumber++, line);
						
						script(message +" update");
					}
					else {
						//                  lineNumber++;
					}
				}

				// copy.txt를 count를 붙여서 다시 복사해서 저장
				if(targetPath != null) {
					Path copyPath = Paths.get(targetDirectory, String.valueOf(fileCount++) + ".txt");
//					Files.copy(targetPath, copyPath, StandardCopyOption.REPLACE_EXISTING);
				}


				e.printStackTrace();
			}
		}
	}
		br.close();
}

	public static void script(String message, boolean b) {
		if(b) {
			try {
				String cmdStr = dates.get(datesHeader++) + "\"" + message+"\"";
				String[] cmd = getArgument(cmdStr);
				Thread.sleep(100);
				Process process = new ProcessBuilder(cmd).start();

				// Read output
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				    String line;
				    while ((line = reader.readLine()) != null) {
				        System.out.println(line);
				}
				} catch (Exception e) {
				process.waitFor();
					e.printStackTrace();
				}
			} catch (Exception e) {
//			String cmd = dates.get(datesHeader++) + "\"" + message+"\"";
//			System.out.println(cmd);
//			System.out.println();
				e.printStackTrace();
			}

	public static void script(String message) {
		try {
			if(count %3 == 0) {
				script(message, scriptWorking);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getDateSet(String timeData) {
		String[] timeDatas = timeData.split("/");
		int year = Integer.parseInt(timeDatas[0]);
		int month = Integer.parseInt(timeDatas[1]);
		int day = Integer.parseInt(timeDatas[2]);
		// 시작 날짜 설정
		ZonedDateTime startDate = ZonedDateTime.of(LocalDateTime.of(year, Month.of(month), day, 10, 0), ZoneId.of("Asia/Seoul"));

		// 결과를 저장할 리스트
		ArrayList<String> list = new ArrayList<>();
		dates = new ArrayList<>();

		// 10개월 동안 반복
        for (int i = 0; i < 20*4*5; i++) {  // 각 달은 4주, 각 주는 5일(주말 제외)로 가정
            ZonedDateTime date = startDate.plusDays(i);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {

                List<String> times = new ArrayList<>();

                int count = getRandomNumberInRange(5, 20); // 하루에 생성할 시간의 수

                for (int j = 0; j < count; j++) {
                    int hour = getRandomNumberInRange(10, 16);
                    int minute = getRandomNumberInRange(0, 59);
                    int second = getRandomNumberInRange(0, 59);

                    times.add(String.format("%02d:%02d:%02d", hour, minute, second));
}

                Collections.sort(times); // 생성된 시간들을 정렬
                for(String data : times) {
                	String[] datas = data.split(":");
                	int hour = Integer.parseInt(datas[0]);
                	int minute = Integer.parseInt(datas[1]);
                	int sec = Integer.parseInt(datas[2]);
                	list.add(date.withHour(hour).withMinute(minute).withSecond(sec).format(DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH)));
}
                
}
	}
        // 결과 출력
        for (String s : list) {
            dates.add("bash git.sh \""+s+"\" ");
}

		}

	private static int getRandomNumberInRange(int min, int max) {
		if(min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	public static String[] getArgument(String cmdString) {
        Pattern pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher matcher = pattern.matcher(cmdString);

        List<String> cmd = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Add double-quoted string without the quotes
                cmd.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // Add single-quoted string without the quotes
                cmd.add(matcher.group(2));
            } else {
                // Add unquoted word
                cmd.add(matcher.group());
}
}

        // Convert list to array
        String[] cmdArray = cmd.toArray(new String[0]);

}
}
	}
	}
