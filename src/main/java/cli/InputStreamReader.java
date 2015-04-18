package cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class InputStreamReader
{
	private BufferedReader reader;

	public InputStreamReader(String path) throws IOException
	{
		reader = new BufferedReader(new FileReader(new File(path)));
	}
	
	public List<String> readAll() throws IOException
	{
		List<String> lines = new ArrayList<String>();
		String line = null;
		while((line = reader.readLine()) != null)
		{
			lines.add(line);
		}
		return new InputTokenizer(lines).getTokens();
	}
}