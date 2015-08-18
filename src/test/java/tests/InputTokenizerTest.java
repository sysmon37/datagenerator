package tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import cli.InputTokenizer;


public class InputTokenizerTest
{
	@Test
	public void whenEmptyInput_returnsEmptyTokenList()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList(""));
		assertEquals(0, inputTokenizer.getTokens().size());
	}
	
	@Test
	public void whenSimpleInput_returnsSimpleToken()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("A"));
		assertEquals(Arrays.asList("A"), inputTokenizer.getTokens());
	}
	
	@Test
	public void whenSpacesOccurs_returnsSimpleTokenWithoutSpaces()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("           A"));
		assertEquals(Arrays.asList("A"), inputTokenizer.getTokens());
	}
	
	@Test
	public void whenTabulationsOccurs_returnsSimpleTokenWithoutTabulations()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("\t\t\tA"));
		assertEquals(Arrays.asList("A"), inputTokenizer.getTokens());
	}
	
	@Test
	public void whenCommentsOccurs_returnsSimpleTokenWithoutComments()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("A//this is a comment"));
		assertEquals(Arrays.asList("A"), inputTokenizer.getTokens());
	}	
	
	@Test
	public void whenMultiTokensSeparatedBySpaceInSingleRow_returnsMultiTokens()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("A B"));
		assertEquals(Arrays.asList("A", "B"), inputTokenizer.getTokens());
	}
	
	@Test
	public void whenMultiTokensSeparatedByTabulationInSingleRow_returnsMultiTokens()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("A\tB"));
		assertEquals(Arrays.asList("A", "B"), inputTokenizer.getTokens());
	}
	
	@Test
	public void whenMultiTokensSeparatedByNewLineInSingleRow_returnsMultiTokens()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("A\nB"));
		assertEquals(Arrays.asList("A", "B"), inputTokenizer.getTokens());
	}
	
	@Test
	public void whenRandomInput_returnsEverythingCorrectly()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("AA\n"+
														   "BB//Comment//Comment2\n"+
														   "\t\t\tCC    DD \tEE//Comment3\n" +
														   "//\tComment4\t\t\n" +
														   "FF"));
		assertEquals(Arrays.asList("AA", "BB", "CC", "DD", "EE", "FF"), inputTokenizer.getTokens());
	}
	
	@Test
	public void whenInputComesFromDifferentSources_returnsEverythingCorrectly()
	{
		InputTokenizer inputTokenizer = new InputTokenizer(Arrays.asList("AA","BB","CC","DD","EE","FF"));
		assertEquals(Arrays.asList("AA", "BB", "CC", "DD", "EE", "FF"), inputTokenizer.getTokens());
	}
}