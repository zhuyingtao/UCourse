package com.server.test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
	int port = 8888;// 端口号，必须与客户端一致
	// 选择进行传输的文件(测试)
	String rootPath = "D:\\android_img";
	Socket socket;

	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ServerSocket ss;

	void start() {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (true) {
			try {
				socket = ss.accept();
				System.out.println("\n----- Socket connect -----");
				input = new ObjectInputStream(socket.getInputStream());
				output = new ObjectOutputStream(socket.getOutputStream());

				String action = input.readUTF();
				System.out.println("action : " + action);
				if (action.equals("getList")) {
					sendList();
				} else if (action.equals("sendFile")) {
					// 接收客户端发送的文件
					String docName = input.readUTF();
					String docCourse = input.readUTF();
					getFile(docName, docCourse);
				} else if (action.equals("getFile")) {
					// 向客户端发送文件(测试)
					String docURL = input.readUTF();
					sendFile(docURL);
				}
				this.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.close();
			}
		}

	}

	public void close() {
		try {
			if (socket != null)
				socket.close();
			if (input != null)
				input.close();
			if (output != null)
				output.close();
			System.out.println("----- Socket closed -----\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void sendList() {
		File file = new File(rootPath);
		List list = this.analyseList(file);
		// System.out.println(file.isDirectory());
		try {
			output.writeObject(list);
			output.flush();
			System.out.println("----文件列表发送成功！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Map<String, Object>> analyseList(File root) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] dirs = root.list();
		for (int i = 0; i < dirs.length; i++) {
			File dir = new File(root + "\\" + dirs[i]);
			Map<String, Object> map = new HashMap<String, Object>();
			String dirName = dir.getName();
			String dirTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date(dir.lastModified()));
			String[] files = dir.list();
			String dirFileNum = "共有" + files.length + "个文件";
			map.put("isDir", true);
			map.put("icon", 0);
			map.put("name", dirName);
			map.put("size", dirFileNum);
			map.put("time", dirTime);
			list.add(map);

			for (int j = 0; j < files.length; j++) {
				File file = new File(dir + "\\" + files[j]);
				map = new HashMap<String, Object>();
				String docName = file.getName();
				String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date(file.lastModified()));
				String size = this.formatFileSize(file.length());
				String[] docNameSplit = docName.split("\\.");
				String type = docNameSplit[docNameSplit.length - 1];
				String docURL = file.getPath();
				String docDir = file.getParent().substring(
						file.getParent().lastIndexOf("\\") + 1);
				int imageId = 0;
				if (type.equals("doc") || type.equals("docx")) {
					imageId = 1;
				} else if (type.equals("txt")) {
					imageId = 2;
				} else if (type.equals("ppt")) {
					imageId = 3;
				} else if (type.equals("xls")) {
					imageId = 4;
				} else if (type.equals("pdf")) {
					imageId = 5;
				} else {
					imageId = 1;
				}

				map.put("isDir", false);
				map.put("name", docName);
				map.put("icon", imageId);
				map.put("time", time);
				map.put("size", size);
				map.put("docURL", docURL);
				map.put("docDir", docDir);
				list.add(map);
			}
		}
		return list;
	}

	// change file unit:from B to KB or MB.
	public String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (size < 1024) {
			df = new DecimalFormat();
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "KB";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	// 向客户端发送信息
	public void sendFile(String filePath) {
		File file = new File(filePath);
		System.out.println("要发送的文件名:" + file.getName());

		DataInputStream fis = null; // read file into buffer.
		DataOutputStream dos = null; // send file data to client.
		try {
			fis = new DataInputStream(new FileInputStream(file));
			dos = new DataOutputStream(output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		byte[] buf = new byte[1024];
		int len = 0;
		try {
			while (true) {
				int read = fis.read(buf);
				if (read == -1)
					break;
				dos.write(buf, 0, read);
				dos.flush();
				len += read;
			}
			System.out.println("----传输" + len + " Byte");
			dos.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("-----文件传输完成------");
	}

	// 接收客户端发送的信息
	public void getFile(String docName, String docCourse) {
		try {
			// 本地保存路径
			String dirPath = rootPath + "\\" + docCourse;
			File dir = new File(dirPath);
			// 创建文件夹
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String filePath = dirPath + "\\" + docName;
			File file = new File(filePath);
			System.out.println("----接收的文件路径： " + file.getPath());

			
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					filePath));
			BufferedInputStream dis = new BufferedInputStream(input);

			byte[] buf = new byte[512];
			long len = 0;

			while (true) {
				int read = dis.read(buf);
				if (read == -1)
					break;
				len += read;
				fos.write(buf, 0, read);
				fos.flush();
			}
			// 花费的时间
			System.out.println("----接收完成，文件大小 :" + len);
			fos.close();
		} catch (Exception e) {
			System.out.println("接收消息错误" + "\n" + e.toString());
			return;
		}
	}

	public static String getTime() {
		long tmp = System.currentTimeMillis();// 花费的时间
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年-MM月dd日-HH时mm分ss秒");
		Date date = new Date(tmp);
		return formatter.format(date);
	}

	public static void main(String arg[]) {
		System.out.println("-----准备建立socket链接----");
		Server st = new Server();
		st.start();
	}
}
