package pl.poznan.put.cs.idss.generator.generation;

public enum OutlierType
{
    OUTLIER(1), RARE_CASE(2);
    
    private final int _numLearnExamplesPerGroup;
    OutlierType(int numLearnExamplesPerGroup)
    {
    	this._numLearnExamplesPerGroup = numLearnExamplesPerGroup;
    }
    
    public int numLearnExamplesPerGroup()
    {
		return _numLearnExamplesPerGroup;
	}
    
    public int numTestExamplesPerGroup()
    {
		return 1;
	}
}