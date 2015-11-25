package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.settings.Rotation;

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
        
        public RotationDescription(Rotation rotation) {
            this(rotation.getAxis1(), rotation.getAxis2(), rotation.getAngle());
        }
}
