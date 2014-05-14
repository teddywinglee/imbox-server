package org.imbox.server.functions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imbox.database.search_block;
import org.imbox.infrastructure.Workspace;
import org.imbox.infrastructure.file.BlockRec;
import org.imbox.infrastructure.file.FileHandler;


public class blocktofile
{
	private String md5;
	private byte[] fileinbyte;
	private File file;
	public blocktofile(String md5)
	{
		this.md5 = md5;
	}
	public void reconstruct() throws IOException
	{
		search_block sb = new search_block(md5);
		sb.searchBlock();
		List<String> blocklist = sb.getList();
		List<BlockRec> blockreclist = getlistofblockrec(blocklist);
		fileinbyte = FileHandler.joinBlocksFromHD(Workspace.SYSDIRs, blockreclist);
	}
	
	private List<BlockRec> getlistofblockrec(List<String> source)
	{
		List<BlockRec> result = new ArrayList<BlockRec>();
		for (int i = 0;i<source.size();i++)
		{
			result.add(new BlockRec(source.get(i), i));
		}
		return result;
	}
	public byte[] getfileinbyte()
	{
		return fileinbyte;
	}
	public File getfile()
	{
		return file;
	}
}