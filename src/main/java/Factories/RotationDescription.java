package Factories;

public class RotationDescription
{
	public int rotationAxis[];
	public double angle;
	
	public RotationDescription(int firstRotationAxis,
							   int secondRotationAxis, double angle)
	{
		super();
		this.rotationAxis = new int[2];
		this.rotationAxis[0] = firstRotationAxis;
		this.rotationAxis[1] = secondRotationAxis;
		this.angle = angle;
	}
}
