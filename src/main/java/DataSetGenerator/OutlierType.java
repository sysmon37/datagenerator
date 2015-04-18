package DataSetGenerator;

public enum OutlierType
{
    OUTLIER(1), RARE_CASE(2);
    
    private final int numberOfTrainingExamples;
    OutlierType(int numberOfTrainingExamples)
    {
    	this.numberOfTrainingExamples = numberOfTrainingExamples;
    }
    
    public int getNumberOfTrainingExamples()
    {
		return numberOfTrainingExamples;
	}
    
    public int getNumberOfTestingExamples()
    {
		return 1;
	}
}