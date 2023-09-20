package automate.remoteserver.methods;

import java.io.InputStream;

import com.jcraft.jsch.*;

public class RemoteConnector {

	public static Session session;

	public static Session connectToRemoteServer(String host, String username, String password, int port) {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(password);

			// Avoid strict host key checking (not recommended for production)
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			// Connect to the remote server
			session.connect();

			return session;
		} catch (JSchException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String executeCommand(String host, String username, String password, int port, String command) {
		try {
			connectToRemoteServer(host, username, password, port);
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// Set up streams for capturing command output
			InputStream commandOutput = channel.getInputStream();
			channel.connect();

			// Read the command output
			StringBuilder outputBuffer = new StringBuilder();
			byte[] buffer = new byte[1024];
			while (true) {
				while (commandOutput.available() > 0) {
					int bytesRead = commandOutput.read(buffer, 0, 1024);
					if (bytesRead < 0)
						break;
					outputBuffer.append(new String(buffer, 0, bytesRead));
				}
				if (channel.isClosed()) {
					if (commandOutput.available() > 0)
						continue;
					break;
				}
				Thread.sleep(1000);
			}

			// Disconnect the channel
			channel.disconnect();

			return outputBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String executeCommand(Session session, String command) {
		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// Set up streams for capturing command output
			InputStream commandOutput = channel.getInputStream();
			channel.connect();

			// Read the command output
			StringBuilder outputBuffer = new StringBuilder();
			byte[] buffer = new byte[1024];
			while (true) {
				while (commandOutput.available() > 0) {
					int bytesRead = commandOutput.read(buffer, 0, 1024);
					if (bytesRead < 0)
						break;
					outputBuffer.append(new String(buffer, 0, bytesRead));
				}
				if (channel.isClosed()) {
					if (commandOutput.available() > 0)
						continue;
					break;
				}
				Thread.sleep(1000);
			}

			// Disconnect the channel
			channel.disconnect();

			return outputBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void executeScript(String host, String username, String password, int port, String scriptFilePath) {
		try {
			connectToRemoteServer(host, username, password, port);
			Channel channel = session.openChannel("sftp");
			channel.connect();

			ChannelSftp sftpChannel = (ChannelSftp) channel;

			// Upload the script file to the remote server
			sftpChannel.put(scriptFilePath, "/tmp/script.sh");

			// Make the script executable
			sftpChannel.chmod(755, "/tmp/script.sh");

			// Execute the script
			executeCommand(session, "/tmp/script.sh");

			// Delete the script file from the remote server if needed
			sftpChannel.rm("/tmp/script.sh");

			sftpChannel.exit();
			channel.disconnect();
		} catch (JSchException | SftpException e) {
			e.printStackTrace();
		}
	}

	public static void restartRemoteServer(String host, String username, String password, int port) {
		try {
			connectToRemoteServer(host, username, password, port);
			executeCommand(session, "sudo reboot"); // Replace with the appropriate command for your server
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void shutdownRemoteServer(String host, String username, String password, int port) {
		try {
			connectToRemoteServer(host, username, password, port);
			executeCommand(session, "sudo shutdown -h now"); // Replace with the appropriate command for your server
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void softwareUpdateInRemoteServer(String host, String username, String password, int port) {
		try {
			connectToRemoteServer(host, username, password, port);
			// Update packages using the package manager (e.g., apt-get for Ubuntu)
			executeCommand(session, "sudo apt-get update");
			executeCommand(session, "sudo apt-get upgrade -y"); // -y flag automatically confirms updates

			// You can add similar commands for other package managers or specific update
			// procedures
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Other methods for remote operations (e.g., executeCommand, executeScript,
	// etc.) can be added here
}
