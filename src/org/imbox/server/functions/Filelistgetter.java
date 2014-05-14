package	org.imbox.server.functions;

import java.util.List;

import org.json.JSONArray;


public class Filelistgetter
{
	private List<String> datalist;
	private JSONArray returnarray;
	public Filelistgetter(List<String> datalist)
	{
		this.datalist = datalist;
	}
	
	public void preparejsonarray()
	{
	    returnarray = new JSONArray();
		for (int i = 0;i < datalist.size();i++)
		{
			returnarray.put(datalist.get(i));
		}
	}
	
	public JSONArray getjsonarray()
	{
		return returnarray;
	}
	
}