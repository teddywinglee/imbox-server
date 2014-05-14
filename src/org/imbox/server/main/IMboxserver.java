package org.imbox.server.main;

import java.net.InetSocketAddress;

import org.imbox.infrastructure.Workspace;
import org.imbox.server.functions.LOCK.Lock;
import org.imbox.server.pagehandler.Networkcheckhandler;
import org.imbox.server.pagehandler.Tokenverifyhandler;
import org.imbox.server.pagehandler.block.Deleteblockhandler;
import org.imbox.server.pagehandler.block.Getblockhandler;
import org.imbox.server.pagehandler.block.Getblockrecordhandler;
import org.imbox.server.pagehandler.block.Postblockhandler;
import org.imbox.server.pagehandler.file.Deletefilehandler;
import org.imbox.server.pagehandler.file.Modifyfilehandler;
import org.imbox.server.pagehandler.file.Newfilehandler;
import org.imbox.server.pagehandler.file.Syncrequesthandler;
import org.imbox.server.pagehandler.lock.Getserverlockhandler;
import org.imbox.server.pagehandler.lock.Releaseserverlockhandler;
import org.imbox.server.pagehandler.login.Createnewaccounthandler;
import org.imbox.server.pagehandler.login.Loginhandler;
import org.imbox.server.pagehandler.sharelink.GenerateURLhandler;
import org.imbox.server.pagehandler.sharelink.Generatefilehandler;

import com.sun.net.httpserver.HttpServer;


public class IMboxserver
{
	private HttpServer server;
	public static Lock lockthread;
	public IMboxserver()
	{
		try{
		server = HttpServer.create(new InetSocketAddress(8080), 0);
		server.createContext("/createaccount",new Createnewaccounthandler());
		server.createContext("/login", new Loginhandler());
		server.createContext("/getserverlock",new Getserverlockhandler());
		server.createContext("/releaseserverlock",new Releaseserverlockhandler());
		server.createContext("/getblock",new Getblockhandler());
		server.createContext("/postblock",new Postblockhandler());
		server.createContext("/generateURL",new GenerateURLhandler());
		server.createContext("/generatefile",new Generatefilehandler());
		server.createContext("/networkcheck",new Networkcheckhandler());
		server.createContext("/syncrequest",new Syncrequesthandler());
		server.createContext("/getblockrecord",new Getblockrecordhandler());
		server.createContext("/modifyfile",new Modifyfilehandler());
		server.createContext("/newfile",new Newfilehandler());
		server.createContext("/deletefile",new Deletefilehandler());
		server.createContext("/verifytoken", new Tokenverifyhandler());
		server.createContext("/deleteblock", new Deleteblockhandler());
        server.setExecutor(null); // creates a default executor
        System.out.println("the server is ready, please use startserver command to start");
        Workspace.prepareWorkspaceS();
		lockthread = new Lock();
		lockthread.start();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void startserver()
	{
		server.start();
		System.out.println("Server started...");
	}
	
	public void closeserver()
	{
		server.stop(0);
	}
}