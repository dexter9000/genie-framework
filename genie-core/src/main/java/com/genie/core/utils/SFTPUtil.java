package com.genie.core.utils;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


/**
 * Created by meng013 on 2018/2/5.
 */
public class SFTPUtil {

    /**
     * 默认端口
     */
    private final static int DEFAULT_PORT = 22;

    private final static String HOST = "host";

    private final static String PORT = "port";

    private final static String USER_NAME = "userName";

    private final static String PASSWORD = "password";

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private int port = DEFAULT_PORT;

    /**
     * 登录名
     */
    private String userName;

    /**
     * 登录密码
     */
    private String password;

    private ChannelSftp sftp;

    public SFTPUtil(String host, int port, String userName, String password) {
        this.init(host, port, userName, password);
    }

    /**
     * 初始化
     *
     * @param host
     * @param port
     * @param userName
     * @param password
     */
    private void init(String host, int port, String userName, String password) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 连接sftp
     *
     * @throws JSchException
     */
    private void connect() throws JSchException {
        JSch jsch = new JSch();
        // 取得一个SFTP服务器的会话
        Session session = jsch.getSession(userName, host, port);
        // 设置连接服务器密码
        session.setPassword(password);
        Properties sessionConfig = new Properties();
        // StrictHostKeyChecking
        // "如果设为"yes"，ssh将不会自动把计算机的密匙加入"$HOME/.ssh/known_hosts"文件，
        // 且一旦计算机的密匙发生了变化，就拒绝连接。
        sessionConfig.setProperty("StrictHostKeyChecking", "no");
        // 设置会话参数
        session.setConfig(sessionConfig);
        // 连接
        session.connect();
        // 打开一个sftp渠道
        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
    }

    public void mkdir(String remotePath) throws JSchException {
        try {
            connect();
            sftp.mkdir(remotePath);
        } catch (SftpException e) {
            throw new JSchException("mkdir error!", e);
        } finally {
            sftp.disconnect();
        }
    }

    /**
     * 上传文件
     */
    public void upload(String fileName, String remotePath, InputStream is) throws JSchException {
        try {
            connect();
            if (!StringUtils.isEmpty(remotePath)) {
                sftp.cd(remotePath);
            }
            sftp.put(is, fileName);
        } catch (SftpException e) {
            throw new JSchException("upload error!", e);
        } finally {
            sftp.disconnect();
        }
    }

    /**
     * 下载
     */
    public void download(String fileName, String remotePath, OutputStream output) throws JSchException {
        try {
            this.connect();
            if (!StringUtils.isEmpty(remotePath)) {
                sftp.cd(remotePath);
            }
            sftp.get(fileName, output);
        } catch (SftpException e) {
            throw new JSchException("download error!", e);
        } finally {
            sftp.disconnect();
        }
    }

    /**
     * 删文件
     */
    public void rm(String fileName, String remotePath) throws JSchException {
        try {
            this.connect();
            if (!StringUtils.isEmpty(remotePath)) {
                sftp.cd(remotePath);
            }
            sftp.rm(fileName);
        } catch (SftpException e) {
            throw new JSchException("download error!", e);
        } finally {
            sftp.disconnect();
        }
    }

    /**
     * 删目录
     */
    public void rmdir(String remotePath) throws JSchException {
        try {
            this.connect();
            sftp.rmdir(remotePath);
        } catch (SftpException e) {
            throw new JSchException("download error!", e);
        } finally {
            sftp.disconnect();
        }
    }

    /**
     * 改文件名，移动文件
     */
    public void rename(String from, String to) throws JSchException {
        try {
            this.connect();
            sftp.rename(from, to);
        } catch (SftpException e) {
            throw new JSchException("rename error!", e);
        } finally {
            sftp.disconnect();
        }
    }

}
