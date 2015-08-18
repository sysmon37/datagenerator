package cli;

import java.util.ArrayList;
import java.util.List;

public class InputTokenizer
{
	private List<String> inputs;
	public InputTokenizer(List<String> inputs)
	{
		this.inputs = inputs;
	}

	public List<String> getTokens()
	{
		List<String> tokens = new ArrayList<String>();
		
		for(String input : inputs)
			for(String line : input.split("\n"))
			{
				String lineWithoutComments = removeComment(line);
				for(String token : lineWithoutComments.split(" |\t"))
					if(token.length() != 0)
						tokens.add(token);
			}
		return tokens;
	}

	private String removeComment(String input)
	{
		final int commentBeginning = input.indexOf("//");
		if(commentBeginning < 0)
			return input;
		return input.substring(0, commentBeginning);
	}

}
