package pl.poznan.put.cs.idss.generator.generation;

import java.util.List;

import org.mockito.ArgumentMatcher;

class ListContainsElementsMatcher<T> extends ArgumentMatcher<List<T>>
{
	private List<T> expectedElements;
	
	public ListContainsElementsMatcher(List<T> elementsToBeMatched)
	{
		this.expectedElements = elementsToBeMatched;
	}
	   
	@SuppressWarnings("unchecked")
	public boolean matches(Object argument)
	{
		List<T> actualList = (List<T>)argument;
		for(T element : expectedElements)
				if(!actualList.contains(element))
					return false;
		return true;
	}
}